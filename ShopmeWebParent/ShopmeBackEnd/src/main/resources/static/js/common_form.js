$(document).ready(function() {
    $("#btnCancle").on("click", function() {
        window.location = urlButtonCancel;
    })
    $("#file-image").change((e) => {
        const fileSize = e.target.files[0].size;
        if(fileSize > 1048576) {
            e.target.setCustomValidity("You must choose an image less than 1MB!");
            e.target.reportValidity();
        }else {
            e.target.setCustomValidity("");
            updateImage(e.target);}
    })
})

function updateImage(fileInput) {
    console.log(fileInput);
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    }
    reader.readAsDataURL(file);
}

function showErrorModal(message) {
    return showModalDialog("Error", message);
}

function showWarningModal(message) {
    return showModalDialog("Warning", message);
}
function showModalDialog(title, message) {
    $("#modal-title").text(title);
    $("#model-message").text(message);
    $("#modal-dialog").modal();
}