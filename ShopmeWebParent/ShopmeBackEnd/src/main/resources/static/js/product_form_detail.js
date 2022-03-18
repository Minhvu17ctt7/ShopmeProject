
function deleteDetail(btn) {
        alert()
        $(btn).closest('.form-inline').remove();
}

function addNextDetail() {
    htmlDetail = ` <div class="form-inline">
       <label class="m-3">Name: </label>
       <input type="text" class="form-control w-25" name="detailNames" maxlength="255"/>
       <label class="m-3">Value: </label>
       <input type="text" class="form-control w-25" name="detailValues" maxlength="255"/>
       <a class="btn fas fa-times-circle fa-2x dark-icon btn-delete-detail"
           onclick="deleteDetail(this)"
          name="linkRemoveDetail" title="Remove detail"
       ></a>
   </div>`;
    $('#product-detail').append(htmlDetail);
}
