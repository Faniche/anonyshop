$("#catalog1").click(function () {
    if ($('#catalog1').attr("isnull") == "true") {
        $('#catalog1').load("http://seller.anonyshop.tech/getCatalog1")
        $('#catalog1').attr("isnull", "false")
    }
});

// $("#catalog1").click(function () {
//     $.get("http://seller.anonyshop.tech/getCatalog1",function(data,status){
//         alert("数据：" + data + "\n状态：" + status);
//     });
//     // if ($('#catalog1').attr("isnull") == "true") {
//     //     $.get("http://seller.anonyshop.tech/getCatalog1",function(data,status){
//     //         alert("数据：" + data + "\n状态：" + status);
//     //     });
//
//         // $.ajax({
//         //     url:"http://seller.anonyshop.tech/getCatalog1",
//         //     type:"get",
//         //     async:false,
//         //     contentType:'application/json',
//         //     // dataType:'json',
//         //     success:function (data) {
//         //         console.log(data)
//         //     }
//         // })
//         //
//         // $('#catalog1').attr("isnull", "false")
//     //}
// });


$(".editAttrInfoRaw").click(function () {
    if (window.confirm("确认修改吗")) {
        var id = $(this).parentsUntil("tr").siblings().eq(0).text();
        alert(id);
        var attrName = $(this).parentsUntil("tr").siblings().eq(1).text()
        $('#attrList').load("/editAttrInfo", {"id": id, "attrName": attrName, "catalog3Id": catalog3Id})
    }
});

$("body").on("click",".catalog1Item",function(){
    $('#catalog2').load("http://seller.anonyshop.tech/getCatalog2", {"catalog1Id": $(this).val()})
});

$("body").on("click",".catalog2Item",function(){
    $('#catalog3').load("http://seller.anonyshop.tech/getCatalog3", {"catalog2Id": $(this).val()})
});

$("body").on("click",".catalog3Item",function(){
    $('#attrList').load("http://seller.anonyshop.tech/getAttrInfoList", {"catalog3Id": $(this).val()})
});

$("body").on("click",".deleteAttr",function(){
    if (window.confirm("确认删除吗")) {
        var id = $(this).parentsUntil("tr").siblings().eq(0).text()
        var attrName = $(this).parentsUntil("tr").siblings().eq(1).text()
        var catalog3Id = $("#catalog3").val()
        $('#attrList').load("http://seller.anonyshop.tech/deleteAttrInfo", {"id": id, "attrName": attrName, "catalog3Id": catalog3Id})
    }
});

$("body").on("click",".editAttr",function(){
    if (window.confirm("确认修改吗")) {
        var id = $(this).parentsUntil("tr").siblings().eq(0).text()
        var attrName = $(this).parentsUntil("tr").siblings().eq(1).text()
        var catalog3Id = $("#catalog3").val()
        var attrValueListStr = $(this).parentsUntil("tr").siblings().eq(2).text()
        $('#attrList').load("http://seller.anonyshop.tech/editAttrInfo",
            {"id": id, "attrName": attrName, "catalog3Id": catalog3Id, "attrValueListStr": attrValueListStr})
    }
});

$("#addAttrBtn").click(function () {
    if ($("#catalog1").val() != null && $("#catalog2").val() != null && $("#catalog3").val() != null) {
        if ($("#inputAttrName").val() == null) {
            alert("请输入属性！")
        } else {
            var catalog3Id = $("#catalog3").val()
            var attrValueListStr = $("#inputAttrValue").val();
            $('#attrList').load("http://seller.anonyshop.tech/addAttrInfo",
                {"attrName": $("#inputAttrName").val(), "catalog3Id": catalog3Id, "attrValueListStr": attrValueListStr})
            $("#inputAttrName").val("");
            $("#inputAttrValue").val("");
            $("#addAttrDiv").hide();
    $("#showAttrDiv").show();
        }
    } else {
        alert("请选择分类！")
    }
});

$("#changeStatus").click(function(){
    if ($("#catalog1").val() != null && $("#catalog2").val() != null && $("#catalog3").val() != null) {
        $("#addAttrDiv").show();
        $("#showAttrDiv").hide();
    } else {
        alert("请选择分类！")
    }
});