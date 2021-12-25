dropdownBrand = $("#brand");
dropdownCategory = $("#category");

$(document).ready(function () {

    $("#shortDescription").richText();
    $("#fullDescription").richText();

    dropdownBrand.change(function () {
        dropdownCategory.empty();
        getCategories();
    })

    getCategoriesEditMode();
})

function getCategoriesEditMode() {
    const categoryId = $("#categoryId");
    let editMode = false;

    if(categoryId.length) editMode = true;

    if(!editMode)
        getCategories();
}

function getCategories() {
    brandId = dropdownBrand.val();
    url = brandModuleUrl + "/" + brandId + "/categories";

    $.get(url, (responseCategories) => {
        $.each(responseCategories, function (index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategory)
        })
    })
}
function submitForm(e, form) {
    e.preventDefault();
    const name = $("#name").val();
    const csrfValue = $("input[name='_csrf']").val();
    const id = $("#id-hidden").val() || null;
    const param = {id: id,name: name, _csrf: csrfValue};
    $.post(urlCheckUnique, param, (res) => {
        if(res == "OK") {
            form.submit();
        } else if(res == "DUPLICATE"){
            showWarningModal(name + " is already");
        } else {
            showErrorModal("Unknown response from server")
        }
    }).fail(() => {
        showErrorModal("Do not connect to server");
    })
    return false;
}