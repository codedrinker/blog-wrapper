/**
 * Created by codedrinker on 05/09/2017.
 */
//https://developer.github.com/v3/issues/#list-issues-for-a-repository
//https://api.github.com/repos/commenthub/commenthub/issues?labels=http://www.majiang.life,http://www.majiang.life/blog/simple-in-memory-cache-in-java/
//https://github.com/commenthub/commenthub/issues/1
function init() {
    var eventMethod = window.addEventListener ? "addEventListener" : "attachEvent";
    var eventer = window[eventMethod];
    var messageEvent = eventMethod == "attachEvent" ? "onmessage" : "message";
    eventer(messageEvent, function (e) {
        console.log(e);
        if (e.origin != "https://commenthub.herokuapp.com") {
            return;
        }
        var key = e.message ? "message" : "data";
        var data = e[key];
        document.getElementById("commenthub_iframe").height = data;
    }, false);
    $("#commenthub_thread").html("<iframe id='commenthub_iframe' allowtransparency='true'  horizontalscrolling='no' verticalscrolling='no' frameborder='0' scrolling='no' tabindex='0' width='100%'  style='width: 1px !important; min-width:100% !important; border: none !important; overflow: hidden !important;' src='https://commenthub.herokuapp.com/comments?commenthub_website=" + commenthub_website + "&commenthub_identifier=" + commenthub_identifier + "'></iframe>");
}

if (typeof jQuery == 'undefined') {
    console.log("has no jquery");
    var headTag = document.getElementsByTagName("head")[0];
    var jqTag = document.createElement('script');
    jqTag.type = 'text/javascript';
    jqTag.src = 'https://code.jquery.com/jquery-2.2.4.min.js';
    jqTag.onload = init;
    headTag.appendChild(jqTag);
} else {
    console.log("has jquery")
    init();
}