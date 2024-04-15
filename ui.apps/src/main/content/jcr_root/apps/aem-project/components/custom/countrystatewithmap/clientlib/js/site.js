$(document).ready(function () {
    let url = document.getElementById("pagePath").getAttribute("data-resourcepath");
    $.ajax({
        url: url + ".CountryStateMapData.json",
        dataType: "json",
        success: function (jsonData) {
            populateCountries(jsonData);
            $("#country").on("change", function () {
                populateStates(jsonData);
            });
        }
    });

    function populateCountries(jsonData) {
        let countrySelect = document.getElementById("country");
        for (let country in jsonData.countryStateListMap) {
            let option = document.createElement("option");
            option.text = country;
            option.value = country;
            countrySelect.appendChild(option);
        }
    }

    function populateStates(jsonData) {
        let countrySelect = document.getElementById("country");
        let stateSelect = document.getElementById("state");
        let selectedCountry = countrySelect.value;

        // Remove previous State options
        stateSelect.innerHTML = "";
        if (selectedCountry !== "") {
            let states = jsonData.countryStateListMap[selectedCountry];
            for (let i = 0; i < states.length; i++) {
                let option = document.createElement("option");
                option.text = states[i];
                option.value = states[i];
                stateSelect.appendChild(option);
            }
        } else {
            let option = document.createElement("option");
            option.text = "Select a state...";
            option.value = "";
            stateSelect.appendChild(option);
        }
    }
});