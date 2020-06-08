$("#btn-submit-pay").click(function () {
    if ($("#radio-wechat").prop("checked")) {
        alert("暂不支持微信支付，请选择支付宝或Paypal支付")
    } else {
        $("#order-form").attr("action","http://payment.anonyshop.tech/"+$("input[type='radio']:checked").val());
        $("#order-form").submit();
    }
})

$("#ret-index").click(function () {
    window.location.href = "http://anonyshop.tech"
})
