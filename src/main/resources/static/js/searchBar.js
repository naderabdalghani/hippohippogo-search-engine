/*<![CDATA[*/
$(function () {

    const inputField = $('#search_form_input');
    const clearButton = $('#search_form_input_clear');
    const searchButton = $('#search_button');
    const voiceInputButton = $('#search_form_voice_input');
    const voiceInputDisabled = $('#search_form_voice_disabled');

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

    searchButton.on('mousedown', function () {
        searchButton.css("background-color", "#5b9e4d");
    });
    searchButton.on('mouseover', function () {
        searchButton.css("background-color", "#65af5c");
    });
    searchButton.on('mouseup mouseout', function () {
        searchButton.css("background-color", "#5b9e4d");
    });

});
/*]]>*/
