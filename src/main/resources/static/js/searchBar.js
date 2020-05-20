/*<![CDATA[*/
$(function () {

    /////////////////////////////////////// Clear Text ///////////////////////////////////////

    const inputField = $('#search_form_input_homepage');
    const clearButton = $('#search_form_input_clear');
    clearButton.hide();
    clearButton.on('click', function () {
        inputField.val("");
        clearButton.hide();
        inputField.trigger('focus');
        inputField.autocomplete().clear();
    });
    inputField.on('change keyup paste', function () {
        if (inputField.val() !== "") {
            clearButton.show();
        } else {
            clearButton.hide();
        }
    });

    /////////////////////////////////////// Suggestions //////////////////////////////////////

    inputField.autocomplete({
        serviceUrl: '/suggestions',
        lookupLimit: 8,
        noCache: true
    });
});
/*]]>*/
