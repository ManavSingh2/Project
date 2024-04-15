$(document).ready(function () {
    // Initial load
    loadProductData();

    // Handle gender change event
    $("#gender").on("change", function () {
        alert("Filter Changed");
        loadProductData();
    });

    // Handle pagination button click event
    $(".pagination-box").on("click", ".pagination-button", function () {
        loadProductData($(this).data("skip"), $(this).data("load"));
    });
});

// Function to load product data
const loadProductData = (dataSkip = 0, dataLoad = 5) => {
    const gender = $("#gender").val();

    // Reset content
    $("#dataList").html("");
    $(".pagination-box").html('');

    // Fetch data from the server
    $.ajax({
        url: "/bin/productDetailServlet",
        method: "GET",
        dataType: "json",
        data: { gender: gender },
        success: function (data) {
            // Handle the JSON data received from the servlet
            const productList = data.List;

            // Create pagination buttons
            createPaginationButtons(productList.length);

            // Display filtered data
            const filteredData = productList.slice(dataSkip, dataLoad);
            displayProductData(filteredData);
        },
        error: function (error) {
            // Handle errors
            console.error("There was a problem with the fetch operation:", error);
        },
    });
};

// Function to display product data
const displayProductData = (data) => {
    data.forEach((item, i) => {
        $("#dataList").append(
            `<div class="card">
                <img class="card-img-top" src="${item.Image}" alt="Card image cap">
                <div class="card-body">
                    <h5 class="card-title">${item.ProductName}</h5>
                    <p class="card-text">${item.Price}</p>
                </div>
            </div>`
        );
    });
};

// Function to create pagination buttons
const createPaginationButtons = (totalItems, itemsPerPage = 5) => {
    const totalPages = Math.ceil(totalItems / itemsPerPage);

    for (let i = 1; i <= totalPages; i++) {
        const dataSkip = (i - 1) * itemsPerPage;
        const dataLoad = i * itemsPerPage;

        $(".pagination-box").append(
            `<button class='btn btn-danger btn-lg pagination-button border-light' data-skip=${dataSkip} data-load=${dataLoad}>${i}</button>`
        );
    }
};
