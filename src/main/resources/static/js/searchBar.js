/*<![CDATA[*/
$(function () {

    const inputField = $('#search_form_input');
    const clearButton = $('#search_form_input_clear');
    const voiceInputButton = $('#search_form_voice_input');

    /////////////////////////////////////// Clear Text ///////////////////////////////////////

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

    ///////////////////////////////////////// Styling ////////////////////////////////////////

    clearButton.on('mousedown', function () {
        clearButton.css("color", "red");
    });
    clearButton.on('mouseover', function () {
        clearButton.css("color", "#616161");
    });
    clearButton.on('mouseup mouseout', function () {
        clearButton.css("color", "#aaa");
    });

    voiceInputButton.on('mousedown', function () {
        voiceInputButton.css("color", "blue");
    });
    voiceInputButton.on('mouseover', function () {
        voiceInputButton.css("color", "#616161");
    });
    voiceInputButton.on('mouseup mouseout', function () {
        voiceInputButton.css("color", "#aaa");
    });
});
/*]]>*/
