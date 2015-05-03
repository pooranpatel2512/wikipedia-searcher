
var searchForm = {
    validate: function () {
        var contributor = $('#contributor').val();
        var text = $('#text').val();
        if(contributor.trim() === "" && text.trim() === "") {
            this.setError("Please enter alteast one search criteria");
        } else {
            $("#errorContainer").removeClass("visible").addClass("hidden");
            searchModule.search(contributor, text)
        }
    },
    setError: function(error) {
        $("#errorContainer").html("");
        $("#errorContainer").removeClass("hidden").addClass("visible");
        $("#errorContainer").html(error);
    }
}