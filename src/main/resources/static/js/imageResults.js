/*<![CDATA[*/
function imgResult(result) {
    return "<div class=\"tile tile--img has-detail\" style=\"max-width: 25em\">\n" +
        "    <div>\n" +
        "        <a href=\"${image_link}\" target=\"_blank\"><div class=\"tile--img__media\" style=\"display: flex; align-items: center\"><span class=\"tile--img__media__i\"><img\n" +
        "                class=\"tile--img__img  js-lazyload\"\n" +
        "                src="+ result.imageLink +"\n" +
        "                title="+ result.sourceLink +"\n" +
        "                data-src="+ result.imageLink +"\n" +
        "                alt="+ result.title +"\n" +
        "                style=\"object-fit: contain\"\n" +
        "        ></span></div></a>\n" +
        "        <a class=\"tile--img__sub\" href="+ result.sourceLink +" style=\"contain: size\"><span\n" +
        "                class=\"tile--img__title\" title="+ result.sourceLink +" style=\"height: auto\">"+ result.title +"</span><span\n" +
        "                class=\"tile--img__domain\" title="+ result.sourceLink +">"+ result.sourceLink +"</span></a>\n" +
        "    </div>\n" +
        "</div>";
}

function registerClick(link) {
    $.post("http://localhost:8080/history", {
        link: link
    });
}

$(function () {
    const resultsView = $("#img_results");
    const loader = $("#img_results_loader");
    const url = window.location.href;
    const urlParams = new URLSearchParams(window.location.search);
    const query = urlParams.get('q');
    let offset = parseInt(urlParams.get('offset')) + 1;
    const limit = parseInt(urlParams.get('limit'));
    const region = urlParams.get('region');
    let limitReached = false;
    const image = $(".tile--img__img");
    const imageTitle = $(".tile--img__title");
    const imageHyperlink = $(".tile--img__domain");
    const webResultsTab = $("#link--web");
    const imagesResultsTab = $("#link--images");
    const trendsResultsTab = $("#link--trends");
    const inputRegion = $('#region');
    const regionField = $('#regionValue');
    const trendsURL = window.location.origin + '/' + 'trends?' + 'region=' + region;
    inputRegion.val(region);
    webResultsTab.attr("href", url.replace("img", "search"));
    imagesResultsTab.attr("href", url);
    trendsResultsTab.attr("href", trendsURL);

    regionField.on('change', function () {
        const newURL = new URL(url);
        newURL.searchParams.set("region", regionField.val().toString());
        window.location = newURL.href;
    });

    // Infinite scrolling (pagination)
    $(window).on("scroll", function () {
        if ($(window).scrollTop() >= $(document).height() - $(window).height() - 10) {
            if (!limitReached) {
                loader.show();
                $.getJSON("http://localhost:8080/img", {
                    q: query,
                    offset: offset,
                    limit: limit,
                    region: region
                },function(results){
                    loader.hide();
                    limitReached = results.length === 0;
                    offset++;
                    $.each(results, function(i, result){
                        resultsView.append(imgResult(result));
                    });
                });
            }
        }
    });

    // Listen to users' clicks to record their most frequent domains
    image.on('click', function () {
        registerClick(this.title);
    });
    imageTitle.on('click', function () {
        registerClick(this.title);
    });
    imageHyperlink.on('click', function () {
        registerClick(this.title);
    });

});
/*]]>*/
