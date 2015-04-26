
var searchForm = {
    validate: function () {
        var contributor = $('#contributor').val();
        var text = $('#text').val();
        if(contributor.trim() === "" && text.trim() === "") {
            $("#errorContainer").html("");
            $("#errorContainer").removeClass("hidden").addClass("visible");
            $("#errorContainer").html("Please enter alteast one search criteria")
        } else {
            $("#errorContainer").removeClass("visible").addClass("hidden");
            searchModule.search(contributor, text)
        }
    }
}