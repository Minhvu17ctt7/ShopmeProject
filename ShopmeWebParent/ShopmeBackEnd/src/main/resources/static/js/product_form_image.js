
let extraImageCount = 0;

$(document).ready(function () {

    $("#extraThumbnail1").change((e) => {
        if(!checkFileSize(e.target)) return
        updateImageExtra(e.target);
    })

    $("input[name='extraImage']").each(function (index){
        extraImageCount++;
        $(this).change(function () {

            updateImageExtra(this, index)
        })
    })

    $("a[name='linkRemoveExtraImage']").each(function(index) {
        $(this).click(function () {
            removeExtraImage(index);
        })
    })
})

function updateImageExtra(fileInput, index) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#extratImage"+index).attr("src", e.target.result);
    }
    reader.readAsDataURL(file);

    if(index >= extraImageCount - 1) {
        addExtraInputImage(index + 1)
    }
}

function addExtraInputImage(index) {
    html = `<div class="col border m-2 p-2" id="divExtraImage${index}">
            <div>
                <label>Extra image ${index + 1}</label>
            </div>
            <div>
                <img src="${srcImageThumbnailDefault}" alt="Extra image" class="img-fluid"
                     id="extratImage${index}"
                >
            </div>
            <div>
                <input type="file" name="extraImage" accept="image/jpeg image/png"
                 onchange="updateImageExtra(this, ${index})" />
            </div>
        </div>`

    htmlIconRemove = `<a href="javascript:removeExtraImage(${index - 1})" class="btn fas fa-times-circle fa-2x icon-dark float-right"></a>`

    $("#extraImageHeader" + (index - 1)).append(htmlIconRemove);

    $("#add-Image").append(html);
    extraImageCount++;
}

function removeExtraImage(index) {
    $("#divExtraImage" + index).remove()
}
