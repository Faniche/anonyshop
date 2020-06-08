function submitlogin() {
    $.ajax({
        //几个参数需要注意一下
        type: "POST",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "http://passport.anonyshop.tech/login",//url
        data: $("#form-login").serialize(),
        success: function (data) {
            var retUrl = $("#return-url").val()
            var fail = retUrl.indexOf("?token=fail")
            if (fail != -1) {
                retUrl = retUrl.substring(0, fail)
            }
            window.location.href=retUrl+"?token="+data;
        }
    })
}