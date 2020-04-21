// SPU销售属性
$("#btn-addSpuSaleAttr").click(function () {
    if (catalogsIsChecked()) {
        var productName = $("#in-productName").val();
        var description = $("#in-description").val();
        var catalog3Id = $("#catalog3").val();
        // var brandId = $("#brand").val();
        $("#addSpuInfoForm").submit();
        var tabItem
        $.ajax({
            url: "http://seller.anonyshop.tech/spu/addSpuInfo",
            type: "post",
            data: {"productName": productName, "description": description, "catalog3Id": catalog3Id},
            success: function (productInfo) {
                $("#from-addspu").toggle()
                $("#tab-addSaleAttr").toggle()
                $("#head-addSaleAttr").text(productName)
                $.ajax({
                    url: "http://seller.anonyshop.tech/attr/baseSaleAttrList",
                    type: "get",
                    success: function (baseSaleAttrList) {
                        for (var i = 0; i < baseSaleAttrList.length; i++) {
                            tabItem += "<tr>\n" +
                                "    <td style=\"padding: 6px;\">" + baseSaleAttrList[i].id + "</td>\n" +
                                "    <td style=\"padding: 6px;\">" + productInfo.id + "</td>\n" +
                                "    <td style=\"padding: 6px;\">" + baseSaleAttrList[i].name + "</td>\n" +
                                "    <td style=\"padding: 6px;\" contenteditable=\"true\"></td>\n" +
                                "    <td style=\"padding: 2px 6px;\">\n" +
                                "        <div role=\"group\" class=\"btn-group\"><button class=\"btn btn-info addSaleAttr\" type=\"button\">保存</button><button class=\"btn btn-danger deleteSaleAttr\" type=\"button\">删除</button></div>\n" +
                                "    </td>\n" +
                                "</tr>";
                        }
                        $("#tb-addSaleAttr").html(tabItem)
                    }
                })
            }
        })
    } else {
        alert("请选择分类！")
    }
});

// 添加spu销售属性
$("body").on("click", ".addSaleAttr", function () {
    var saleAttrId = $(this).parentsUntil("tr").siblings().eq(0).text()
    var productId = $(this).parentsUntil("tr").siblings().eq(1).text()
    var saleAttrName = $(this).parentsUntil("tr").siblings().eq(2).text()
    var spuSaleAttrValueListStr = $(this).parentsUntil("tr").siblings().eq(3).text()
    $.ajax({
        url: "http://seller.anonyshop.tech/attr/addProdSaleAttr",
        type: "post",
        data: {"saleAttrId": saleAttrId, "productId": productId, "saleAttrName": saleAttrName, "spuSaleAttrValueListStr": spuSaleAttrValueListStr},
        success: function (data) {
            $(this).parentsUntil("tr").siblings().eq(3).text(data.spuSaleAttrValueListStr)
        }
    })
});

// 图片上传
$("#in-spuImg").change(function () {
    $.ajax({
        url: "http://seller.anonyshop.tech/spu/imgUpload",
        type: "post",
        cache: false,
        data: new FormData($('#form-spuUploadImg')[0]),
        processData: false,
        contentType: false,
        success: function (data) {
            var img = "<img class=\"rounded spu_img_preview\" src=\"" + data + "\" alt=\"Something Error\" style=\"margin: 5px;height: inherit;width: auto;\">\n"
            $("#spuPicPreview").append(img)
            $("#spuPicPreview").toggle()
        }
    })

})



// 删除spu销售属性
$("body").on("click", ".deleteSaleAttr", function () {
    var saleAttrId = $(this).parentsUntil("tr").siblings().eq(0).text()
    var productId = $(this).parentsUntil("tr").siblings().eq(1).text()
    var saleAttrName = $(this).parentsUntil("tr").siblings().eq(2).text()
    var spuSaleAttrValueListStr = $(this).parentsUntil("tr").siblings().eq(3).text()
    $.ajax({
        url: "http://seller.anonyshop.tech/attr/deleteSaleAttr",
        type: "post",
        data: {"saleAttrId": saleAttrId, "productId": productId, "saleAttrName": saleAttrName, "spuSaleAttrValueListStr": spuSaleAttrValueListStr},
        success: function (data) {}
    })
    spuSaleAttrValueListStr = ""
    $(this).parentsUntil("tr").siblings().eq(3).html(spuSaleAttrValueListStr)
});

// 编辑商品销售属性
$("body").on("click", ".editSaleAttr", function () {
    var saleAttrId = $(this).parentsUntil("tr").siblings().eq(0).text()
    var productId = $(this).parentsUntil("tr").siblings().eq(1).text()
    var saleAttrName = $(this).parentsUntil("tr").siblings().eq(2).text()
    var spuSaleAttrValueListStr = $(this).parentsUntil("tr").siblings().eq(3).text()
    $.ajax({
        url: "http://seller.anonyshop.tech/attr/addProdSaleAttr",
        type: "post",
        data: {"saleAttrId": saleAttrId, "productId": productId, "saleAttrName": saleAttrName, "spuSaleAttrValueListStr": spuSaleAttrValueListStr},
        success: function (data) {
            $(this).parentsUntil("tr").siblings().eq(3).text(data.spuSaleAttrValueListStr)
        }
    })
});

/* 销售属性添加完成按钮 */
$("#finAddSaleAttr").click(function () {
    $("#from-addspu").toggle()
    $("#tab-addSaleAttr").toggle()
    var tableItem;
    $.ajax({
        url: "http://seller.anonyshop.tech/spu/getSpuList",
        type: "post",
        data: {"catalog3Id": $("#catalog3").val()},
        success: function (data) {
            $("#tab-spnInfo").html(newSpuInfoTabRows(data))
        }
    })
    $("#spuPicPreview").toggle()
    $("#in-productName").val("")
    $("#in-description").val("");
    $("#in-spuImg").val("");
})

// 编辑完成按钮
$("#finEditSaleAttr").click(function(){
    alert("ok")
    $("#editSpuSaleAttr").hide()
    alert("mid")
    $("#editSpuInfo").show()
    alert("over")
});

// 编辑SPU
$("body").on("click", ".btn-showEditSaleAttr", function () {
    $("#editSpuInfo").hide()
    $("#editSpuSaleAttr").show()
    var productName = $(this).parentsUntil("tr").siblings().eq(2).text()
    $("#head-editSaleAttr").text(productName)
    var spuIndex = $(this).parentsUntil("tr").siblings().eq(0).text() - 1
    var tabItem
    $.ajax({
        url: "http://seller.anonyshop.tech/spu/getSpuSaleAttr",
        type: "post",
        data: {"spuIndex": spuIndex},
        success: function (data) {
            $("#editSaleAttr").html(newSpuSaleAttrValRows(data))
        }
    })
});

function newSpuInfoTabRows(data) {
    var tmp
    for (var i = 0; i < data.length; i++) {
        tmp += "<tr>" +
            "<td style=\"padding: 6px;\">" + (i + 1) + "</td>" +
            "<td style=\"padding: 6px;\">" + data[i].id + "</td>" +
            "<td style=\"padding: 6px;\" contenteditable=\"true\">" + data[i].productName + "</td>" +
            "<td style=\"padding: 6px;\" contenteditable=\"true\">" + data[i].description + "</td>" +
            "<td style=\"padding: 2px 6px;\">\n" +
            "<div class=\"btn-group\" role=\"group\"><button class=\"btn btn-info editSpuInfo\" type=\"button\">修改</button>\n" +
            "<button class=\"btn btn-danger deleteSpuInfo\" type=\"button\">删除</button>\n" +
            "<button class=\"btn btn-secondary btn-showEditSaleAttr\" type=\"button\">编辑销售属性</button>\n" +
            "</div>\n" +
            "</td>\n" +
            "</tr>";
    }
    return tmp;
}

function newSpuSaleAttrValRows(data) {
    var tmp
    for (var i = 0; i < data.length; i++) {
        tmp +=
            "<tr>\n" +
            "<td style=\"padding: 6px;\">" + data[i].saleAttrId + "</td>\n" +
            "<td style=\"padding: 6px;\">" + data[i].productId + "</td>\n" +
            "<td style=\"padding: 6px;\">" + data[i].saleAttrName + "</td>\n" +
            "<td contenteditable=\"true\" style=\"padding: 6px;\">" + data[i].spuSaleAttrValueListStr + "</td>\n" +
            "<td style=\"padding: 6px;\">\n" +
            "<div class=\"btn-group\" role=\"group\"><button class=\"btn btn-info editSaleAttr\" type=\"button\">修改</button><button class=\"btn btn-danger deleteSaleAttr\" type=\"button\">删除</button></div>\n" +
            "</td>\n" +
            "</tr>";
    }
    return tmp
}

function changeEditSaleAttrState(){
    $("#editSpuInfo").toggle()
    $("#editSpuSaleAttr").toggle()
}

// ====================================================================================================================
// SPU
$("body").on("click", ".editSpuInfo", function () {
    var id = $(this).parentsUntil("tr").siblings().eq(1).text();
    var productName = $(this).parentsUntil("tr").siblings().eq(2).text();
    var description = $(this).parentsUntil("tr").siblings().eq(3).text();
    $.ajax({
        url: "http://seller.anonyshop.tech/spu/editSpuInfo",
        type: "post",
        data: {"id": id, "productName": productName, "description": description},
        success: function (data) {
            alert("修改成功")
        }
    })
})

$("body").on("click", ".deleteSpuInfo", function () {
    var id = $(this).parentsUntil("tr").siblings().eq(1).text();
    var catalog3Id = $("#catalog3").val()
    $.ajax({
        url: "http://seller.anonyshop.tech/spu/deleteSpuInfo",
        type: "post",
        data: {"id": id, "catalog3Id": catalog3Id},
        success: function (data) {
            $("#tab-spnInfo").html(newSpuInfoTabRows(data))
        }
    })
})