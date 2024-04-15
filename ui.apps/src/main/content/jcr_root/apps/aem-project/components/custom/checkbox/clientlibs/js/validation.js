(function(document, $) {
    $(document).on("foundation-contentloaded", function (e) {
       var checkboxValue = $("input[name='./checkboxValue']");
        function showHideFunction() {
            if (checkboxValue.prop("checked")) {
                $(".text_class").show();
                $(".link_class").hide();
                 $(".link_class").parent().hide();
				$(".text_class").parent().show();

            } else {
                $(".link_class").show();
                $(".text_class").hide();
				$(".text_class").parent().hide();
                $(".link_class").parent().show();
            }
        }

        // Initial toggle on dialog load
        showHideFunction();

        // Toggle the visibility whenever the "checkboxValue" field changes
        checkboxValue.on("change", showHideFunction);

        // Add a click event listener to perform the logic on click
        $(document).on("click", function() {

            // Initial toggle on dialog load
            showHideFunction();

            // Toggle the visibility whenever the "checkboxValue" field changes
            checkboxValue.on("change", showHideFunction);
        });
    });
})(document, Granite.$);