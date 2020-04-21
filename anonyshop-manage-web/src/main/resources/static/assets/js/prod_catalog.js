$("#catalog1").click(function () {
    if ($('#catalog1').attr("isnull") == "true") {
        $('#catalog1').attr("isnull", "false")
        var seleceItem;
        $.ajax({
            url: "http://seller.anonyshop.tech/catalog/getCatalog1",
            type: "get",
            success: function (data) {
                var length = data.length;
                var catalog1 = $("#catalog1");
                for (var i = 0; i < length; i++) {
                    seleceItem += "<option class=\"catalog1Item\" value=" + data[i].id + ">" + data[i].name + "</option>";
                }
                catalog1.html(seleceItem)
            }
        })
    }
});


$("body").on("click", ".catalog1Item", function () {
    var seleceItem;
    $.ajax({
        url: "http://seller.anonyshop.tech/catalog/getCatalog2",
        type: "post",
        data: {"catalog1Id": $(this).val()},
        success: function (data) {
            var length = data.length;
            var catalog2 = $("#catalog2");
            for (var i = 0; i < length; i++) {
                seleceItem += "<option class=\"catalog2Item\" value=" + data[i].id + ">" + data[i].name + "</option>";
            }
            catalog2.html(seleceItem)
        }
    })
});

$("body").on("click", ".catalog2Item", function () {
    var seleceItem;
    $.ajax({
        url: "http://seller.anonyshop.tech/catalog/getCatalog3",
        type: "post",
        data: {"catalog2Id": $(this).val()},
        success: function (data) {
            var length = data.length;
            var catalog3 = $("#catalog3");
            for (var i = 0; i < length; i++) {
                seleceItem += "<option class=\"catalog3Item\" value=" + data[i].id + ">" + data[i].name + "</option>";
            }
            catalog3.html(seleceItem)
        }
    })
});

$("body").on("click", ".catalog3Item", function () {
    var tableItem;
    $.ajax({
        url: "http://seller.anonyshop.tech/attr/getAttrInfoList",
        type: "post",
        data: {"catalog3Id": $(this).val()},
        success: function (data) {
            for (var i = 0; i < data.length; i++) {
                tableItem +=
                    "<tr style=\"margin: 10px 0px;\">" +
                    "<td style=\"padding: 6px;\">" + (i + 1) + "</td>" +
                    "<td style=\"padding: 6px;\">" + data[i].id + "</td>" +
                    "<td class=\"d-lg-flex align-items-lg-center\" style=\"padding: 6px;\" contenteditable=\"true\">" + data[i].attrName + "</td>" +
                    "<td class=\"attrValueListStr\" style=\"padding: 6px;\" contenteditable=\"true\">" + data[i].attrValueListStr + "</td>" +
                    "<td style=\"padding: 0px;\"><div class=\"btn-group\" role=\"group\">\n" +
                    "<button class=\"btn btn-info editAttr\" type=\"button\">修改</button>\n" +
                    "<button class=\"btn btn-danger deleteAttr\" type=\"button\">删除</button>\n" +
                    "</div>\n" +
                    "</td>\n" +
                    "</<tr>";
            }
            $("#attrList").html(tableItem)
        }
    })
});