$(document).ready(function () {
    $("#btn-logout").click(function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    })
})

function customizeDropdownMenu() {
    $(".dropdown > a").click(function () {
        location.href = this.href;
    })
}