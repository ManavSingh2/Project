$(document).ready(function () {
    console.log("Into Js of Pagination");
    $("#jsonContent").hide();
    $(".leftRight").hide();
    $(".errorMsg").hide();
    $("#submitButton").click(function () {
        var searchText = $("#searchText").val();
        if(searchText===""){
            $("#searchText").css("border-color", "red");
            $(".errorMsg").css("color", "red");
            $(".errorMsg").show();

        }else{
            $(".errorMsg").hide();
            $("#searchText").css("border-color", "blue");
            $("#jsonContent").show();
            $(".leftRight").show();
            $(".paginationBox").empty();
            $.ajax({
                url: "/bin/pagination",
                method: "POST",
                dataType: "json",
                data: { searchText: searchText },
                success: function (response) {
                    let data = response.data;
                    console.log(data);
                    if (data.length === 0) {
                        $("#jsonContent").hide();
                        $(".leftRight").hide();
                        $(".dataNotFound").html(`<h2  class="text-secondary"> Data Doesn't Exist Please Search For Other Values </h2>  `)
                    } else {
                        $(".dataNotFound").html('');
                        getData(data, 0, 5);
                        createPaginationButtons(data);
                        attachPaginationEvents(data);
                    }
                },
            });
        }
    });


    // Get Data based on Pagination
    const getData = (data, dataSkip, dataLoad) => {
        $(".forEmpty").html("");
        let filter = data.slice(dataSkip, dataLoad);
        filter.forEach((item, i) => {
            console.log(i + "  " + item.title + "  " + item.path);
            $(".tableHead").append(`<tr class="forEmpty">
                        <td class="table-active">${i + 1}</td>
                        <td class="table-active">${item.title}</td>
                        <td class="table-active"> <a href='${item.path}' target='_blank' >${item.path}</td>
                  </tr>`);
        });
    };

    const createPaginationButtons = (data) => {
        let dataSkip = 0;
        let dataLoad = 5;
        let length = Number(data.length / 5);
        if (length.toString().indexOf(".") != -1) {
            length = length + 1;
        }
        for (let i = 1; i <= length; i++) {
            $(".paginationBox").append(
                `<button class='btn btn-danger btn-lg paginationButton border-light' data-skip=${dataSkip} data-load=${dataLoad}>${i}</button>`
            );
            dataSkip += 5;
            dataLoad += 5;
        }
    };

    const attachPaginationEvents = (data) => {
        $(".paginationBox").on("click", ".paginationButton", function () {
            let skip = $(this).data("skip");
            let load = $(this).data("load");
            getData(data, skip, load);
        });
    };

});
