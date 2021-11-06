function showDeleteComfirmModel(url, id, type) {
    $("#btnYes").attr("href", url);
    $(".modal-body").text("Delete " + type + " " + id + "?");
    $("#modal-dialog").modal();
}

function onClearFormSearch() {
    window.location = moduleUrl;
}