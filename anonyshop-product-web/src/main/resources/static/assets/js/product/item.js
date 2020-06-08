$("#itemQuantityAdd").click(function() {
    var n = $("#item-quantity").val()
	var num = parseInt(n) + 1
	if(num == 0) {
		return;
	}
    $("#item-quantity").val(num)
})

$("#itemQuantityMinus").click(function() {
	var n = $("#item-quantity").val()
	var num = parseInt(n) - 1
	if(num == 0) {
		return;
	}
    $("#item-quantity").val(num)
})

//红边框
$(".box-attr-2 button").click(function() {
$(this).removeClass("btn-light").addClass("btn-success").siblings("button").removeClass("btn-success");
         switchSkuId();
})

function switchSkuId() {
    var skuSaleAttrValueJsonStr = $("#valuesSku").val();
    var saleAttrValueIds = $(".btn-success label");
    var k = "";
    $(saleAttrValueIds).each(function (i, saleAttrValueId) {
        k = k + $(saleAttrValueId).attr("value") + "|";
    });
    var kuSaleAttrValueJson = JSON.parse(skuSaleAttrValueJsonStr);
    var v_skuId = kuSaleAttrValueJson[k];
    if (v_skuId) {
        window.location.href = "http://item.anonyshop.tech/" + v_skuId + ".html";
    }
}

function addToCart(obj) {
        var canClick = $(obj).attr("canClick");
        if (canClick == '0') {
            return;
        }
        $("#itemForm").submit();
    }