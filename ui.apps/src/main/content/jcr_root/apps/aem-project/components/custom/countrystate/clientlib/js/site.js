$(document).ready(function () {
    let url = document.getElementById("pagePath").getAttribute("data-resourcepath");
    console.log(url);
    $.ajax({
        url: url +".model.json",
        dataType: "json",
        success: function (response) {

            // To append Country name
            let countrySelect = document.getElementById("Country");
            response.countryList.forEach(function (country) {
                let option = document.createElement("option");
                option.value = country.countryName;
                option.text = country.countryName;
                countrySelect.appendChild(option);
            });

            // To append State Name based on Country selected
            countrySelect.addEventListener("change", function () {
                let selectedCountry = this.value;

                let selectedCountryData = response.countryList.find(function (country) {
                    return country.countryName === selectedCountry;
                });
                let stateSelect = document.getElementById("State");
                stateSelect.innerHTML = "<option value=''>Select a State</option>";

                if (selectedCountryData) {
                    selectedCountryData.stateList.forEach(function (state) {
                        let option = document.createElement("option");
                        option.value = state.stateName;
                        option.text = state.stateName;
                        stateSelect.appendChild(option);
                    });
                }
            });
        },
    });
});
