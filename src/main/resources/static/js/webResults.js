/*<![CDATA[*/
function webResult(result) {
    return "<div class=\"result results_links_deep highlight_d result--url-above-snippet\"><div>\n" +
        "    <div class=\"result__body links_main links_deep\">\n" +
        "        <h2 class=\"result__title\">\n" +
        "            <a class=\"result__a\" rel=\"noopener\" href="+ result.link +">"+ result._title +"</a><a rel=\"noopener\" class=\"result__check\" href="+ result.link +"><span class=\"result__check__tt\">Your browser indicates if you've visited this\n" +
        "                          link</span></a>\n" +
        "        </h2>\n" +
        "        <div class=\"result__extras js-result-extras\">\n" +
        "            <div class=\"result__extras__url\">\n" +
        "                <a href="+ result.link +" rel=\"noopener\" class=\"result__url js-result-extras-url\"><span class=\"result__url__domain\">"+ result.link +"</span></a>\n" +
        "            </div>\n" +
        "        </div>\n" +
        "        <div class=\"result__snippet js-result-snippet\">"+ result._description +"</div>\n" +
        "    </div>\n" +
        "</div></div>";
}

function registerClick(link) {
    $.post("http://localhost:8080/history", {
        link: link
    });
}

$(function () {
    const resultsView = $(".results");
    const loader = $("#web_results_loader");
    const urlParams = new URLSearchParams(window.location.search);
    const query = urlParams.get('q');
    let offset = parseInt(urlParams.get('offset')) + 1;
    const limit = parseInt(urlParams.get('limit'));
    const region = urlParams.get('region');
    let limitReached = false;
    const resultTitle1 = $(".result__a");
    const resultTitle2 = $(".result__check");
    const resultHyperlink = $(".result__url");

    // Infinite scrolling (pagination)
    $(window).on("scroll", function () {
        if ($(window).scrollTop() >= $(document).height() - $(window).height() - 10) {
            if (!limitReached) {
                loader.show();
                $.getJSON("http://localhost:8080/search", {
                    q: query,
                    offset: offset,
                    limit: limit,
                    region: region
                },function(results){
                    loader.hide();
                    limitReached = results.length === 0;
                    offset++;
                    $.each(results, function(i, result){
                        resultsView.append(webResult(result));
                    });
                });
            }
        }
    });

    // Listen to users' clicks to record their most frequent domains
    resultTitle1.on('click', function () {
        registerClick(this.href);
    });
    resultTitle2.on('click', function () {
        registerClick(this.href);
    });
    resultHyperlink.on('click', function () {
        registerClick(this.href);
    });

});
/*]]>*/
