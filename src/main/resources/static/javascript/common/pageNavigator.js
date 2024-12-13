function pageNavigator(submitFnc) {

    $(document).on("click", ".paging a", function(){

        // const $this = $(this);
        var id = $(this).attr("id");
        var page = $(this).data("page");
        var ids = ["firstPage", "prevPage", "goPage","nextPage", "lastPage"];

        console.log(id, page);

        if (ids.includes(id)) {
            submitFnc(page);
        }

    });

}