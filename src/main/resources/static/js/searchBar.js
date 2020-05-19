/*<![CDATA[*/
$(function () {

    /////////////////////////////////////// Clear Text ///////////////////////////////////////

    const inputField = $('#search_form_input_homepage');
    const clearButton = $('#search_form_input_clear');
    clearButton.hide();
    console.log("main hide")
    clearButton.on('click', function () {
        inputField.val("");
        clearButton.hide();
        inputField.trigger('focus');
    });
    inputField.on('change keyup paste', function () {
        if (inputField.val() !== "") {
            clearButton.show();
        } else {
            clearButton.hide();
        }
    });

    /////////////////////////////////////// Suggestions //////////////////////////////////////

    const countriesArray = $.map(countries, function (value, key) {
        return {value: value, data: key};
    });

    // Initialize ajax autocomplete:
    inputField.autocomplete({
        // serviceUrl: '/autosuggest/service/url',
        lookup: countriesArray,
        lookupFilter: function (suggestion, originalQuery, queryLowerCase) {
          console.log(suggestion, originalQuery, queryLowerCase);
          const re = new RegExp('\^' + $.Autocomplete.utils.escapeRegExChars(queryLowerCase), 'gi');
          console.log(re);
          return re.test(suggestion.value);
        },
        onSelect: function (suggestion) {
            console.log('You selected: ' + suggestion.value + ', ' + suggestion.data);
        },
        onInvalidateSelection: function () {
            console.log('You selected: none');
        },
        lookupLimit: 8
    });
});
/*]]>*/
