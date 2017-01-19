var IS_LOGIN = true;
function htmlLoad() {
    var codeImg = $('#codeImg');
    codeImg.attr('src', $bw.httpHeader + 'api/Captcha/Image');
    codeImg.click(function() {
        codeImg.attr('src', $bw.httpHeader + 'api/Captcha/Image?' + Math.random());
    });
    verifyLogin();
    if ($bw.userInfo != null && $bw.userInfo.mgr.id > 0) {
        layer.msg('You have logged in.', function() {
            window.location.href = "./index.html";
        });
    }
    $('.login-input').focus(function() {
            $(this).parent().addClass('c-bd-blue c-blue');
        })
        .blur(function() {
            $(this).parent().removeClass('c-bd-blue c-blue');
        });
    $('.rememberPassword').click(function() {
        if ($(this).attr('data-sel') == 0) {
            $(this).find('.bw-unselected-ico').addClass('bw-selected-ico')
                .addClass('c-blue');
            $(this).attr('data-sel', 1);
            $(this).find('input').val('true');
        } else {
            $(this).removeClass('c-blue');
            $(this).find('.bw-unselected-ico').removeClass('bw-selected-ico')
                .removeClass('c-blue');
            $(this).attr('data-sel', 0);
            $(this).find('input').val('false');
        }
    });
    $(document).on('keydown', function(e) {
        if (e.keyCode == 13) {
            $('.goLogin').click();
        }
    });
    //登录
    $('.goLogin').click(function() {
        if($('#captcha').val().length<4) {
            layer.msg('Please enter the verification code');return false;
        }
        var $form=$('#loginForm');
        $form.submit();
    });
    //登录后执行的函数
    $bw.reloadForm = function(res) {
        if (res.Status != 1) {
            layer.msg(res.Message);
            codeImg.attr('src', $bw.httpHeader + 'api/Captcha/Image?' + Math.random());
        } else {
            if ($('#rememberPassword').val() == 'true') {
                addStorage({
                    name: 'userSessionInfo',
                    info: res.Data,
                    time: 60 * 60 * 24
                });
            }
            //登录成功 用户信息写入sessionStorage
            addSession({
                name: 'userSessionInfo',
                info: res.Data
            });
            window.location.href = "./index.html";
        }
    };
    $bw.errorBack = function(res) {
        codeImg.attr('src', $bw.httpHeader + 'api/Captcha/Image?' + Math.random());
        $('.login-error').html(res.Message)
            .show();
    };
}
