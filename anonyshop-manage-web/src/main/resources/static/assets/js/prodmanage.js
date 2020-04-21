// NAV item buttons
$("#baseAttrManage").click(function () {
    $("#baseAttrManage").attr("disabled", true)
    $("#spuManage").attr("disabled", false)
    changeManageItem()
});

$("#spuManage").click(function () {
    $("#spuManage").attr("disabled", true)
    $("#baseAttrManage").attr("disabled", false)
    // 切换到spu管理
    changeManageItem()
    // 查询当前分类下的spu
    var tableItem;
    $.ajax({
        url: "http://seller.anonyshop.tech/spu/getSpuList",
        type: "post",
        data: {"catalog3Id": $("#catalog3").val()},
        success: function (data) {
            $("#tab-spnInfo").html(newSpuInfoTabRows(data))
        }
    })
});

function changeManageItem(){
    if (catalogsIsChecked()) {
        $("#addAttrDiv").toggle();
        $("#showAttrDiv").toggle();
        $("#addSpuDiv").toggle();
        $("#editSpuDiv").toggle();
    } else {
        alert("请选择分类！")
    }
}
$(".showEditSaleAttr").click(function(){
    changeEditSaleAttrState()
})

$("#finEditSaleAttr").click(function(){
    changeEditSaleAttrState()
})

function changeEditSaleAttrState(){
    $("#editSpuInfo").toggle()
    $("#editSpuSaleAttr").toggle()
}

function catalogsIsChecked() {
    return  $("#catalog1").val() != null && $("#catalog2").val() != null && $("#catalog3").val() != null
}