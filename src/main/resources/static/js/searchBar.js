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
    clearButton.on('mousedown', function () {
        clearButton.css("color", "red");
    });
    clearButton.on('mouseover', function () {
        clearButton.css("color", "#616161");
    });
    clearButton.on('mouseup mouseout', function () {
        clearButton.css("color", "#aaa");
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
