/**
 * Created by soft_2 on 2016/9/29.
 */
function showMenu() {
    $.get('../data/config.json', function(msg) {
        var interText = doT.template($('#tpl-left-box').text());
        $('#left-box').html(interText(msg.left));//$bw.userInfo.module.items));

    }, 'json');
}

function getRightUrl($rightFrame) {
    var firstMenu = $bw.userInfo.module.items[0];
    if (firstMenu) {
        if (firstMenu.name == "Home") {
            $rightFrame.attr('src', 'home.html');
        } else {
            $rightFrame.attr('src', firstMenu.menu[0].href);
        }

    }
}
function htmlLoad() {
    if(!verifyLogin()) return false;
    showMenu();
    var $rightFrame = $(window.document.getElementById('rightFrame'));
    //getRightUrl($rightFrame);

    var nickName = $bw.userInfo.NickName;
    $('#userTitleName').html(nickName);
    //用户操作框
    $('.topShowAction').click(function() {
        $('.user-action').slideToggle(300);
    });
    $(document).click(function(e) {
        if ($(e.target).hasClass('topShowAction')) return;
        $(".user-action").slideUp(300);
    });
    //退出
    $(document).on('click', '.logout', function() {
        addSession({ name: 'userSessionInfo', info: null });
        addStorage({ name: 'userSessionInfo', info: null });
        $bw.ajax({
            url:'api/Users/SignOut',
            type:'POST',
            callback:function(){
                window.location.href = './login.html';
            }
        });
    });
    $(document).on("click", ".title", function() {
        $('.selectBox').removeClass('selectBox');
        if ($(this).hasClass('select')) {
            $(this).removeClass('select')
                .find('.sign-ico').removeClass('bw-down-ico');
            $(this).next().slideUp(200);
        } else {
            $(this).parent().addClass('selectBox');
            $('.title').removeClass('select')
                .find('.sign-ico').removeClass('bw-down-ico');
            $(this).addClass('select')
                .find('.sign-ico').addClass('bw-down-ico');
            $('.menu-son').slideUp(200);
            $(this).next().slideDown(200);
        }
    });
    //点击时修改内容iframe的window.name  防止页面切换出错
    $(document).on("click", ".menuClick", function() {
        $(".menu-son").find('.active').removeClass("active");
        $(this).addClass("active");
        //var $rightFrame = $('#rightFrame', window.parent.document);
        $rightFrame[0].contentWindow.name = 'rightFrame';
    });
    $('#switch-menu').click(function() {
        var switchImg = $('.switch-img');
        if (switchImg.hasClass('isSmall')) {
            $('.top-left').attr('style', '');
            switchImg.removeClass('isSmall').removeClass('c-fff');
            $('.left-box').removeClass('smallLeft');
            $rightFrame.attr('width', '83%');
            switchImg.attr('src', '../image/logo-2.png');
        } else {
            $('.top-left').attr('style', 'width:5.5%');
            switchImg.addClass('isSmall').addClass('c-fff');
            $('.left-box').addClass('smallLeft');
            $rightFrame.attr('width', '94%');
            switchImg.attr('src', '../image/logo-1.png');
        }
    });
    $(document).on('click', '.title-href', function() {
        $rightFrame[0].contentWindow.location.href = this.dataset.href;
    });
}
