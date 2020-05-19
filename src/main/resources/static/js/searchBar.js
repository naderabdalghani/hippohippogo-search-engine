/*<![CDATA[*/
$(function () {
  const inputField = $('#search_form_input_homepage');
  const clearButton = $('#search_form_input_clear');
  clearButton.hide();
  console.log("main hide")
  clearButton.on('click',function () {
    inputField.val("");
    clearButton.hide();
    inputField.trigger('focus');
  });
  inputField.on('change keyup paste', function(){
    if (inputField.val() !== "") {
      clearButton.show();
    }
    else {
      clearButton.hide();
    }
  });
});
/*]]>*/
