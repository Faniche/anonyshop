// Base Attr Operation
$("body").on("click", ".catalog3Item", function () {
    var tableItem;
    $.ajax({
        url: "http://seller.anonyshop.tech/attr/getAttrInfoList",
        type: "post",
        data: {"catalog3Id": $(this).val()},
        success: function (data) {
            $("#attrList").html(newBaseAttrRows(data))
        }
    })
    catalog3IsChecked = true;
});


/* 添加 */
$("#addAttrBtn").click(function () {
    if (catalogsIsChecked()) {
        if ($("#inputAttrName").val() == null) {
            alert("请输入属性！")
        } else if ($("#inputAttrValue").val() == null) {
            alert("请输入属性值！")
        } else {
            var catalog3Id = $("#catalog3").val()
            var attrValueListStr = $("#inputAttrValue").val();
            var tableItem;
            $.ajax({
                url: "http://seller.anonyshop.tech/attr/addAttrInfo",
                type: "post",
                data: {
                    "attrName": $("#inputAttrName").val(),
                    "catalog3Id": catalog3Id,
                    "attrValueListStr": attrValueListStr
                },
                success: function (data) {
                    $("#attrList").html(newBaseAttrRows(data))
                }
            })
            $("#inputAttrName").val("");
            $("#inputAttrValue").val("");
        }
    } else {
        alert("请选择分类！")
    }
})
;

/* 删除 */
$("body").on("click", ".deleteAttr", function () {
    if (window.confirm("确认删除吗")) {
        var id = $(this).parentsUntil("tr").siblings().eq(1).text()
        var attrName = $(this).parentsUntil("tr").siblings().eq(2).text()
        var catalog3Id = $("#catalog3").val()
        $.ajax({
            url: "http://seller.anonyshop.tech/attr/deleteAttrInfo",
            type: "post",
            data: {"id": id, "attrName": attrName, "catalog3Id": catalog3Id},
            success: function (data) {
                $("#attrList").html(newBaseAttrRows(data))
            }
        })
    }
});

/* 修改 */
$("body").on("click", ".editAttr", function () {
    if (window.confirm("确认修改吗")) {
        var id = $(this).parentsUntil("tr").siblings().eq(1).text()
        var attrName = $(this).parentsUntil("tr").siblings().eq(2).text()
        var catalog3Id = $("#catalog3").val()
        var attrValueListStr = $(this).parentsUntil("tr").siblings().eq(3).text()
        $.ajax({
            url: "http://seller.anonyshop.tech/attr/editAttrInfo",
            type: "post",
            data: {"id": id, "attrName": attrName, "catalog3Id": catalog3Id, "attrValueListStr": attrValueListStr},
            success: function (data) {
                $("#attrList").html(newBaseAttrRows(data))
            }
        })
    }
});

function newBaseAttrRows(data) {
    var tmp
    for (var i = 0; i < data.length; i++) {
        tmp +=
            "<tr style=\"margin: 10px 0px;\">" +
            "<td style=\"padding: 6px;\">" + (i + 1) + "</td>" +
            "<td style=\"padding: 6px;\">" + data[i].id + "</td>" +
            "<td style=\"padding: 6px;\" contenteditable=\"true\">" + data[i].attrName + "</td>" +
            "<td class=\"attrValueListStr\" style=\"padding: 6px;\" contenteditable=\"true\">" + data[i].attrValueListStr + "</td>" +
            "<td style=\"padding: 0px;\"><div class=\"btn-group\" role=\"group\">\n" +
            "<button class=\"btn btn-info editAttr\" type=\"button\">修改</button>\n" +
            "<button class=\"btn btn-danger deleteAttr\" type=\"button\">删除</button>\n" +
            "</div>\n" +
            "</td>\n" +
            "</<tr>";
    }
    return tmp;
}