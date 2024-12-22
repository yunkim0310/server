function getStoreLikeList(){
    let popWin = window.open("/getStoreLikeList", "popWin",
        "left=300, top=200, width=350, height=450, marginwidth=0, marginheight=10, scrollbars=no, scrolling=no, menubar=no, resizable=no");
}

$(function () {

    $(".getStoreLikeList").on("click", function () {
        getStoreLikeList()
    })

})