function toSend() {
    $("#card-send-delivery").show()
    $("#card-delivery-list").hide()
}

function toList() {
    $("#card-send-delivery").hide()
    $("#card-delivery-list").show()
}

function deliveryArrive(orderId) {
    var pageNum = $("#dilivery-pagenum").val();
    $("#tab-delivery-seller").load("http://manage.anonyshop.tech/delivery/arrive", {orderId: orderId, pageNum: pageNum})
}

function toPageSeller(pageNum) {
    $("#tab-delivery-seller").load("http://manage.anonyshop.tech/delivery/list", {pageNum: pageNum})
}

function toPageUser(pageNum) {
    var userId = $("#userId").val()
    $("#tab-delivery-user").load("http://manage.anonyshop.tech/delivery/list", {pageNum: pageNum, userId: userId})
}

$("#btn-send-express").click(function () {
    $.ajax({
        url: "http://manage.anonyshop.tech/delivery/send",
        type: "post",
        dataType: "json",
        data: $(this).parent().serialize(),
        success: function (data) {
            alert(data);
            window.location.href = $("#return-url").val()
        }
    })
})

$("body").on("click", ".confirm-rcv", function () {
    var orderId = $(this).next().val()
    var userId = $("#userId").val()
    var pageNum = $("#pageNum").val()
    $("#tab-delivery-user").load("http://manage.anonyshop.tech/delivery/confirmRcv", {
        orderId: orderId,
        pageNum: pageNum,
        userId: userId
    })
    alert("已确认收货")
})

function toComment(orderId) {
    $("#order-comment").show();
    $("#orderItems").load("http://manage.anonyshop.tech/order/getOrder", {orderId: orderId});
}

$("body").on("click", ".btn-product", function () {
    $(this).removeClass("btn-light").addClass("btn-info").siblings("button").removeClass("btn-info")
    $(this).siblings("input").val($(this).text())
})

$("body").on("click", ".btn-service", function () {
    $(this).removeClass("btn-light").addClass("btn-info").siblings("button").removeClass("btn-info")
    $("#serviceScore").val($(this).text())
})

$("body").on("click", ".btn-delivery", function () {
    $(this).removeClass("btn-light").addClass("btn-info").siblings("button").removeClass("btn-info")
    $("#deliveryScore").val($(this).text())
})
$("body").on("click", "#btn-submit-comment", function () {
    $.ajax({
        url: "http://manage.anonyshop.tech/comment/write",
        dataType: "json",
        data: $(this).parent().serialize(),
        success: function (data) {
            alert(data)
        }
    })
})