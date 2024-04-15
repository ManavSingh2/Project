(function ($, window) {
    console.log(" ----Into Dialog Validation------- ");
    var registry = $(window).adaptTo("foundation-registry");//It will register the component
    // Validator for checking duplicate country names in a dialog box
    registry.register("foundation.validation.validator", {
        selector: "[data-validation=country-multifield]",
        validate: function (element) {
            var el = $(element);
            let countryName = el.val().toUpperCase(); // Convert to uppercase for case-insensitive check

            // Check for duplicate country names
            var duplicateFound = false;

            // Find existing country name elements in the dialog
            $('[data-validation=country-multifield]').not(el).each(function () {
                if ($(this).val().toUpperCase() === countryName) {
                    duplicateFound = true;
                    return false; // Break the loop if a duplicate is found
                }
            });

            // Return validation message if duplicate is found
            if (duplicateFound) {
                return "Duplicate country name found. Please enter a unique country name.";
            }
        }
    });

})(jQuery, window);