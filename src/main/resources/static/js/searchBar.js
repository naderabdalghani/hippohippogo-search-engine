/*<![CDATA[*/
$(function () {
    const gray = "#aaa";
    const darkGray = "#616161";
    const green = "#65af5c";
    const darkGreen = "#5b9e4d";
    const red = "#ce2a2a";
    const darkRed = "#aa2222";

    const inputField = $('#search_form_input');
    const clearButton = $('#search_form_input_clear');
    const searchButton = $('#search_button');
    const voiceInputButton = $('#search_form_voice_input');
    const voiceInputDisabled = $('#search_form_voice_disabled');
    let recording = false;

    /////////////////////////////////////// Clear Text ///////////////////////////////////////

    clearButton.on('click', function () {
        inputField.val("");
        clearButton.css("visibility", "hidden");
        inputField.trigger('focus');
        inputField.autocomplete().clear();
    });

    inputField.on('change keyup paste', function () {
        if ((inputField.val() !== "")) {
            clearButton.css("visibility", "visible");
        } else {
            clearButton.css("visibility", "hidden");
        }
    });

    /////////////////////////////////////// Suggestions //////////////////////////////////////

    inputField.autocomplete({
        serviceUrl: '/suggestions',
        lookupLimit: 8,
        onSelect: function (suggestion) {
            inputField.trigger("focus");
            inputField.autocomplete().hide();
        },
        appendTo: searchForm
    });

    ///////////////////////////////////////// Styling ////////////////////////////////////////

    // clearButton
    clearButton.on('mousedown', function () {
        clearButton.css("color", red);
    });
    clearButton.on('mouseover', function () {
        clearButton.css("color", darkGray);
    });
    clearButton.on('mouseup mouseout', function () {
        clearButton.css("color", gray);
    });

    // voiceInputButton
    voiceInputButton.on('mousedown', function () {
        if (!recording) {
            voiceInputButton.css("color", darkGreen);
        } else {
            voiceInputButton.css("color", darkRed);
        }
    });
    voiceInputButton.on('mouseover', function () {
        if (!recording) {
            voiceInputButton.css("color", darkGray);
        } else {
            voiceInputButton.css("color", red);
        }
    });
    voiceInputButton.on('mouseout', function () {
        if (!recording) {
            voiceInputButton.css("color", gray);
        } else {
            voiceInputButton.css("color", green);
        }
    });
    voiceInputButton.on('mouseup', function () {
        if (!recording) {
            voiceInputButton.css("color", green);
        } else {
            voiceInputButton.css("color", gray);
        }
    });

    // searchButton
    searchButton.on('mousedown', function () {
        searchButton.css("background-color", darkGreen);
    });
    searchButton.on('mouseover', function () {
        searchButton.css("background-color", green);
    });
    searchButton.on('mouseup mouseout', function () {
        searchButton.css("background-color", darkGreen);
    });

    /////////////////////////////////////// Voice Input //////////////////////////////////////
    let voiceInputString = "";

    try {
        const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
        var recognition = new SpeechRecognition();
        recognition.continuous = true;

        recognition.onresult = function (event) {
            const current = event.resultIndex;
            const transcript = event.results[current][0].transcript;

            voiceInputString += transcript;
            inputField.val(voiceInputString).trigger("focus");
        };

        recognition.onstart = function () {
            recording = true;
            voiceInputButton.removeAttr('disabled');
            voiceInputButton.css("color", green);
            clearButton.css("visibility", "visible");
            inputField.attr('readonly', 'readonly');
            voiceInputString = inputField.val();
            if (!voiceInputString.endsWith(" ") && voiceInputString !== "") {
                voiceInputString = voiceInputString + " ";
            }
        }

        recognition.onspeechend = function () {
            recognition.stop();
            recording = false;
            if (inputField.val() === "") {
                clearButton.css("visibility", "hidden");
            }
            voiceInputButton.css("color", gray);
            inputField.removeAttr('readonly');
        }

        recognition.onerror = function (event) {
            console.log("on error", event.error);
            if(event.error === "not-allowed") {
                if (inputField.val() === "") {
                    clearButton.css("visibility", "hidden");
                }
                inputField.removeAttr('readonly');
                inputField.trigger("focus");
            }
            if (event.error === "aborted") {
                if (recording) {
                    recognition.stop();
                    recording = false;
                    if (inputField.val() === "") {
                        clearButton.css("visibility", "hidden");
                    }
                    voiceInputButton.css("color", gray);
                    inputField.removeAttr('readonly');
                }
            }
        }
    } catch (e) {
        console.error(e);
        voiceInputDisabled.css("visibility", "visible");
        voiceInputDisabled.attr("title", "Voice recognition error");
        inputField.removeAttr('readonly');
        voiceInputDisabled.show();
    }

    voiceInputButton.on("click", function () {
        if (!recording) {
            voiceInputButton.attr('disabled','disabled');
            recognition.start();
        } else {
            recognition.abort();
            inputField.trigger("focus");
        }
    });

    clearButton.on("click", function () {
        if (recording) {
            recognition.abort();
        }
    });

    // Permission handling
    navigator.permissions.query({name: 'microphone'}).then(function (result) {
        if (result.state === 'denied') {
            voiceInputDisabled.css("visibility", "visible");
            voiceInputDisabled.show();
            if (recording) {
                recognition.abort();
            }
        } else {
            voiceInputDisabled.hide();
        }
        result.onchange = function () {
            if (result.state === 'denied') {
                voiceInputDisabled.css("visibility", "visible");
                voiceInputDisabled.show();
                if (recording) {
                    recognition.abort();
                }
            } else {
                voiceInputDisabled.hide();
            }
        };
    });
});
/*]]>*/
