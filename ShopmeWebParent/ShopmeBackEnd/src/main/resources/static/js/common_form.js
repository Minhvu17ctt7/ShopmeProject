$(document).ready(function() {
    $("#btnCancle").on("click", function() {
        window.location = urlButtonCancel;
    })
    $("#file-image").change((e) => {
        // const fileSize = e.target.files[0].size;
        // if(fileSize > MAX_FILE_SIZE) {
        //     e.target.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + "Byte");
        //     e.target.reportValidity();
        // }else {
        //     e.target.setCustomValidity("");
        if(!checkFileSize(e.target)) return
            updateImage(e.target);
    })
})

function checkFileSize(file) {
    const fileSize = file.files[0].size;
    if(fileSize > MAX_FILE_SIZE) {
        file.setCustomValidity("You must choose an image less than " + MAX_FILE_SIZE + "Byte");
        file.reportValidity();
        return false
    }else {
        file.setCustomValidity("");
        return true
    }
}

function updateImage(fileInput) {
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