/* 获取当前分类下的SPU列表展示 */
$("body").on("click", ".catalog3Item", function () {
    catalog3IsChecked = true
    // 获取SPU列表
    $.ajax({
        url: "http://seller.anonyshop.tech/spu/getSpuList",
        type: "post",
        data: {"catalog3Id": $("#catalog3").val()},
        success: function (data) {
            $("#tab-spnInfo").html(newSpuInfoTabRows(data))
        }
    })
    // 查询当前分类平台属性及平台属性值类表
    $.ajax({
        url: "http://seller.anonyshop.tech/attr/getAttrInfoList",
        type: "post",
        data: {"catalog3Id": $("#catalog3").val()},
        success: function (data) {
            var baseAttrRow = ""
            for (var i = 0; i < data.length; i++) {
                if (i % 4 == 0) {
                    baseAttrRow += "<tr style=\"margin: 20px 0px;\">";
                }
                // data = List<PmsBaseAttrInfo>
                // data[i] =   PmsBaseAttrInfo{id, attrName,  catalog3Id, attrValueList}
                // data[i].attrValueList[j] = PmsBaseAttrValue{id, valueName, attrId}
                // List<PmsSkuAttrValue> skuAttrValueList
                //      PmsSkuAttrValue{attrId, valueId}
                // data[i].id  ==>  skuAttrValueList[i].attrId
                // data[i].attrValueList[j].id  ==>  skuAttrValueList[i].valueId

                baseAttrRow +=
                    "<td class=\"d-lg-flex justify-content-lg-end\"style=\"padding: 7px;margin: 10px 0px;\">" + data[i].attrName + "</td>" +
                    "<td style=\"padding: 0px;\"><input type='hidden' name=\"skuAttrValueList[" + i + "].attrId\" value=\"" + data[i].id + "\" disabled>"
                baseAttrRow += "<select class=\"form-control\" style=\"margin: 10px 0px;\" name=\"skuAttrValueList[" + i + "].valueId\">"
                for (var j = 0; j < data[i].attrValueList.length; j++) {
                    baseAttrRow += "<option class='baseAttrItem' value=\"" + data[i].attrValueList[j].id + "\">" + data[i].attrValueList[j].valueName + "</option>"
                }
                baseAttrRow += "</select></td>"
                if (i % 4 == 3 || (i == data.length - 1 && i % 4 != 3)) {
                    baseAttrRow += "</tr>"
                }

            }
            $("#baseAttrDiv").html(baseAttrRow)
        }
    })
});

$("body").on("click", ".baseAttrItem", function () {
    $(this).parentsUntil("td").siblings().eq(0).prop("disabled", false)
})

/* 点击添加SKU按钮 */
$("body").on("click", ".addSku", function () {
    $("#showSpuImgList").load("http://seller.anonyshop.tech/img/skuGetSpuImgs", {productId: $(this).parentsUntil("tr").siblings().eq(1).text()})
    $("#saleAttrRow").remove()
    $("#postProductId").val($(this).parentsUntil("tr").siblings().eq(1).text())
    $("#postCatalog3Id").val($("#catalog3").val())
    // productName 在SKU的地方显示
    var productName = $(this).parentsUntil("tr").siblings().eq(2).text()
    // disable 的input不会提交, 设置spu名称
    $("#disproductName").attr("placeholder", productName)
    // 查询spu销售属性列表
    var spuIndex = $(this).parentsUntil("tr").siblings().eq(0).text() - 1
    $.ajax({
        url: "http://seller.anonyshop.tech/spu/getSpuSaleAttrNoNull",
        type: "post",
        data: {"spuIndex": spuIndex},
        success: function (data) {
            var saleAttrRow = ""
            for (var i = 0; i < data.length; i++) {
                if (i % 4 == 0) saleAttrRow += "<tr id=\"saleAttrRow\" style=\"margin: 20px 0px;\">";
                saleAttrRow += "<td class=\"d-lg-flex justify-content-lg-end\"style=\"padding: 7px;margin: 10px 0px;\">" + data[i].saleAttrName + "</td><td style=\"padding: 0px;\">";
                // 下拉列表,隐藏域提交销售属性，下拉列表提交销售属性值
                //                    value                       name
                //      销售属：        data[i].saleAttrId    ==>  skuSaleAttrValueList[i].saleAttrId,
                //                    data[i].saleAttrName  ==>  skuAttrValuskuSaleAttrValueListeList[i].saleAttrName
                saleAttrRow += "<input type='hidden' name=\"skuSaleAttrValueList[" + i + "].saleAttrName\" value=\"" + data[i].saleAttrName + "\">"
                //      销售属性值：
                //                    data[i].spuSaleAttrValueList[j].id  ==>  skuSaleAttrValueList[i].saleAttrValueId
                //                    data[i].spuSaleAttrValueList[j].saleAttrValueName  ==>  skuSaleAttrValueList[i].saleAttrValueName
                saleAttrRow += "<input type='hidden' name=\"skuSaleAttrValueList[" + i + "].saleAttrValueName\">" +
                    "<select class=\"form-control\" style=\"margin: 10px 0px;\" name=\"skuSaleAttrValueList[" + i + "].saleAttrValueId\">"
                for (var j = 0; j < data[i].spuSaleAttrValueList.length; j++) {
                    saleAttrRow += "<option class='saleAttrValueItem' value=\"" + data[i].spuSaleAttrValueList[j].id + "\">" + data[i].spuSaleAttrValueList[j].saleAttrValueName + "</option>" +
                        " value=\"" + data[i].spuSaleAttrValueList[j].saleAttrValueName + "\">";
                }
                saleAttrRow += "</select><input type='hidden' name=\"skuSaleAttrValueList[" + i + "].saleAttrId\" value=\"" + data[i].saleAttrId + "\" disabled></td>"
                if (i % 4 == 3) {
                    saleAttrRow += "</tr>"
                }
                if (i == data.length - 1 && i % 4 != 3) {
                    saleAttrRow + "</tr>"
                }
            }
            $("#baseAttrDiv").append(saleAttrRow)
        }
    })
})

$("body").on("click", ".saleAttrValueItem", function () {
    $(this).parent().prev().val($(this).text())
    $(this).parent().next().prop("disabled", false)
})

$("body").on("change", "#selectAllImg", function () {
    if ($(this).prop("checked")) {
        $(".spuImgItemCkb").prop("checked", true)
        $(".spuImgItem").prop("disabled", false)
    } else {
        $(".spuImgItemCkb").prop("checked", false)
        $(".spuImgItem").prop("disabled", true)
        $("#skuDefaultImg").val("")
    }
})

$("body").on("click", ".spuImgItemCkb", function () {
    var spuImgInItem = "spuImgItemIn" + $(this).parentsUntil("tr").siblings().eq(0).text()
    if ($(this).prop("checked")) {
        $(":input." + spuImgInItem).prop("disabled", false)
    } else {
        $(":input." + spuImgInItem).prop("disabled", true)
    }
})

$("body").on("click", ".img-setdefault", function () {
    var spuImgInItem = "spuImgItemIn" + $(this).parentsUntil("tr").siblings().eq(0).text()
    if (!$(this).parentsUntil("tr").filter(":input.spuImgItemCkb").prop("checked")) {
        $(this).parent().prev().children().eq(0).prop("checked", true)
        $(":input." + spuImgInItem).prop("disabled", false)
    }
    var url = $("." + spuImgInItem).eq(2).val()
    $("#skuDefaultImg").val(url)
})

// 点击添加按钮，（最后的提交）
$("#saveSku").click(function () {
    $.ajax({
        //几个参数需要注意一下
        type: "POST",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "http://seller.anonyshop.tech/sku/saveSkuInfo",//url
        data: $('#form-addSku').serialize(),
        success: function (result) {
            alert(result.msg)
            if (result.resultCode == 200) {
                alert("SUCCESS");
            }
        },
        error: function () {
            alert("异常！");
        }
    });
})

/*====================================================================================================================*/
/* Common Function */

/* 构造SPU基本信息表 */
function newSpuInfoTabRows(data) {
    var tmp
    for (var i = 0; i < data.length; i++) {
        tmp +=
            "<tr>\n" +
            "<td style=\"padding: 6px;\">" + (i + 1) + "</td>\n" +
            "    <td style=\"padding: 6px;\">" + data[i].id + "</td>\n" +
            "    <td style=\"padding: 6px;\">" + data[i].productName + "</td>\n" +
            "    <td style=\"padding: 6px;\">" + data[i].description + "</td>\n" +
            "    <td style=\"padding: 1px;\">" +
            "      <button class=\"btn btn-primary addSku\" type=\"button\"><i class=\"la la-plus\"></i>添加</button>" +
            "   </td>\n" +
            "</tr>"
    }
    return tmp;
}

/* 构造平台属性及商品销售属性 */
