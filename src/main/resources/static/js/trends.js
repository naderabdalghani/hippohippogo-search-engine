/*<![CDATA[*/
$(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const region = urlParams.get('region');
    const inputRegion = $('#region');

    inputRegion.val(region);

});
/*]]>*/
