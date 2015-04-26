var searchModule = {
    search: function (contributor, text) {
        $("#searchButton").addClass("loading");
        var request = $.ajax({
            url: "./search",
            method: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            data: JSON.stringify({
                contributor: contributor,
                text: text
            })
        });
        request.done(function (result) {
            $("#searchButton").removeClass("loading");
            $("#results").html("");
            $.each(result, function(i, value) {
                $("#results").append("<ul class=\"item\">"+value+"</ul>");
            });
        });
        request.fail(function (jqXHR, status, msg) {
            $("#searchButton").removeClass("loading");
            console.log(status + ", " + msg);
        });
    }
}