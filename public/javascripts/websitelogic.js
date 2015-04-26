var searchModule = {
    search: function (contributor, text) {
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
            console.log(result)
        });
        request.fail(function (jqXHR, status, msg) {
            console.log(status + ", " + msg);
        });
    }
}