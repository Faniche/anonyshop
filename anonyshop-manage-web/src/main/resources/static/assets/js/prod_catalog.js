var catalog1IsChecked
var catalog2IsChecked
var catalog3IsChecked

$("#catalog1").click(function () {
    if ($('#catalog1').attr("isnull") == "true") {
        $('#catalog1').attr("isnull", "false")
        var seleceItem;
        $.ajax({
            url: "http://manage.anonyshop.tech/catalog/getCatalog1",
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
        url: "http://manage.anonyshop.tech/catalog/getCatalog2",
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
    catalog1IsChecked = true;
    catalog2IsChecked = false;
    catalog3IsChecked = false;
});

$("body").on("click", ".catalog2Item", function () {
    var seleceItem;
    $.ajax({
        url: "http://manage.anonyshop.tech/catalog/getCatalog3",
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
    catalog2IsChecked = true;
    catalog3IsChecked = false;
});

function catalogsIsChecked() {
    return catalog1IsChecked && catalog2IsChecked && catalog3IsChecked
}