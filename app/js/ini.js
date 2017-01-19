/*Created by soft_2 on 2016/7/6.*/

/**
 * 外部资源引入 css
 * @author duantingting@bestwise.cc 2017-01-09
 * @param  {[type]} url [description]
 * @return {[type]}     [description]
 */
function loadCss(url) {
    var head = document.getElementsByTagName('head');
    if (head && head.length) {
        head = head[0];
    } else {
        head = document.body;
    }

    var link = document.createElement('link');
    link.rel = "stylesheet";
    head.appendChild(link);
    link.onload = link.onreadystatechange = function() {
        if ((!this.readyState) || this.readyState == "complete" || this.readyState == "loaded") {

        }
    };
    link.href = url;
}

/**
 * 外部资源引入 js
 * @author duantingting@bestwise.cc 2017-01-19
 * @param  {[type]}   url      [description]
 * @param  {Function} callback [description]
 * @return {[type]}            [description]
 */
function loadJS(url, callback) {
    var head = document.getElementsByTagName('head');
    if (head && head.length) {
        head = head[0];
    } else {
        head = document.body;
    }

    var script = document.createElement('script');

    script.type = "text/javascript";
    head.appendChild(script);

    script.onload = script.onreadystatechange = function() {
        if ((!this.readyState) || this.readyState == "complete" || this.readyState == "loaded") {
            if (callback) callback();
        }
    };
    script.src = url;
}
/**
 * 初始化
 * @author duantingting@bestwise.cc 2017-01-19
 * @return {[type]} [description]
 */
function init() {
    if ($bw.loadJsIndex) {
        $bw.loadJsIndex++;
    } else {
        $bw.loadJsIndex = 1;
    }
    if ($bw.loadJsIndex == 6) {
        //判断用户是否登录
        if (!verifyLogin()) return;
        //执行统一初始化
        configLoad();
        //执行页面初始化
        if (typeof(htmlLoad) == 'function') htmlLoad();
    }
}
loadCss('../css/init.css');
loadCss('../css/iconfont/iconfont.css');
loadCss('../Plug/laypage/skin/laypage.css');
loadCss('../Plug/layer/skin/layer.css');
loadJS('../Plug/jquery.min.js', init);
loadJS('../Plug/laypage/laypage.js', init);
loadJS('../Plug/doT.min.js', init);
loadJS('../js/config.js', init);
window.onload = function() {
    //延时加载layer  防止layer.css加载出错，也可以防止页面未加载完成
    loadJS('../Plug/layer/layer.js', init);
    var thisHtml=$bw.GetPageName().replace('.html','');
    loadJS('../js/view/'+thisHtml+'.js', init);
}





if (!$bw) var $bw = {};
/**
 * 获取当前页面参数
 * @author duantingting@bestwise.cc 2017-01-11
 * @return {[type]} [description]
 */
$bw.getHtmlOption = function() {
        var key = $bw.getUrlInfo().key;
        if (key) return getSession("htmlOptionInfo" + key);
        return {};
    }
    /**
     * 定义列表参数
     */
$bw.getListOption = function(data, init) {
    var returnData = data;
    var optionInfo = $bw.getHtmlOption();
    if (init === true) {
        //初始化以get数据为准
        if ($bw.getLength(optionInfo) > 0) {
            for (var i in optionInfo) { returnData[i] = optionInfo[i]; }
        }
        $bw.setHistory(returnData, 'replaceState', $bw.getUrlInfo().key);
    } else {
        //其他以传入数据为准
        for (var i in returnData) {
            optionInfo[i] = returnData[i];
        }
        returnData = optionInfo;
        $bw.setHistory(returnData, 'pushState');
    }
    $bw.params = returnData;
    return returnData;
};
/*定义次级列表参数 一般用于二次弹出的页面 不能从get上取参数*/
$bw.getLesserListOption = function(data, name) {
    var returnData = $bw.lesserParams[name];
    if (!returnData) { returnData = {}; }
    for (var i in data) { returnData[i] = data[i]; }
    $bw.lesserParams[name] = returnData;
    return returnData;
};
/**
 * 将数据写入历史
 */
$bw.setHistory = function(data, type, oldkey) {
    var urlkey = '';
    if (type === 'pushState') {
        urlkey = new Date().getTime();
        history.pushState(null, null, '?key=' + urlkey);
    } else {
        urlkey = oldkey ? oldkey : new Date().getTime();
        history.replaceState(null, null, '?key=' + urlkey);
    }
    addSession({
        info: data,
        name: "htmlOptionInfo" + urlkey
    })
};
/**
 * 获取到数据并显示到页面
 * @param obj obj.init 初始化还是其他操作
 */
$bw.showTableList = function(obj) {
    var option;
    if (!obj.data.PageSize) obj.data.PageSize = $bw.pages;
    if (obj.lesser === true) {
        option = $bw.getLesserListOption(obj.data, obj.lesserName);
    } else {
        option = $bw.getListOption(obj.data, obj.init);
    }

    $bw.ajax({
        url: option.url,
        type: option.ajaxType,
        data: (function() {
            delete option.url;
            delete option.keyword;
            delete option.ajaxType;
            return option;
        })(),
        callback: function(res) {
            var $table = obj.table;
            var resData = res.Data;
            //设置序号
            var pageNo = option.PageIndex ? option.PageIndex : 1;
            $bw.serial = (parseInt(pageNo) - 1) * option.PageSize;
            if (resData) {
                //如果不是分页调用则设置分页
                if (!obj.page) {
                    $bw.getPage($table.find('.bw-page')[0], resData.TotalPageCount, pageNo);
                }
                if (obj.lesser === true) {
                    $bw.lesserData[obj.name] = resData.Items;
                } else {
                    $bw.listData = resData.Items;
                }
                //设置总记录
                $table.find('.bw-table-total').html('共' + resData.TotalItemCount + '条记录');
                $bw.showTpl($table.find('.bw-table-tpl'), $table.find('.bw-tbody'), resData.Items);
            } else {
                $bw.showTpl($table.find('.bw-table-tpl'), $table.find('.bw-tbody'), {});
            }


            if (typeof(obj.callback) == 'function') {
                obj.callback(res);
            }
            //验证权限 处理列表内的权限
            getUserActionAuthority();
        }
    });
};
/**
 * 分页方法
 * @param e
 * @param total
 * @param pageNo
 */
$bw.getPage = function(e, total, pageNo) {
    //绑定分页
    laypage({
        cont: $(e), //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
        pages: total, //通过后台拿到的总页数
        curr: pageNo, //当前页
        first: 1, //将首页显示为数字1,。若不显示，设置false即可
        last: total, //将尾页显示为总页数。若不显示，设置false即可
        prev: '<', //若不显示，设置false即可
        next: '>', //若不显示，设置false即可
        skip: true,
        skin: 'molv',
        button: 'go',
        jump: function(obj, first) { //触发分页后的回调
            if (!first) { //点击跳页触发函数自身，并传递当前页：obj.curr
                var options = {
                    table: obj.cont.parent(),
                    data: { PageIndex: obj.curr },
                    page: true,
                };
                var lesser = false;
                if (e.dataset.lesser === 'true') {
                    var name = e.dataset.lessername;
                    if (!name) layer.msg('次级列表，请填写name');
                    options.name = name;
                    lesser = true;
                }
                if (typeof(pageChange) == 'function') {
                    options.callback = pageChange;
                }
                options.lesser = lesser;
                $bw.showTableList(options);
            }
        }
    });
};
/**
 * 默认form表单提交后执行的函数
 */
$bw.reloadForm = function() {
    layer.msg('操作成功', { time: 1500 }, function() {
        window.location.reload();
    });
};
/**
 * 验证文件并上传文件
 * @author duantingting@bestwise.cc 2017-01-05
 * @param  {[type]} $form [description]
 * @return {[type]}       [description]
 */
$bw.uploadFormFile = function($form, callback) {
    //验证上传文件
    var fileVerifyMsg = verifyFile($form);
    //null代表没有上传图片
    if (fileVerifyMsg.type === null) {
        callback(null);
        return null;
    }
    //false上传图片没通过验证
    if (fileVerifyMsg.type === false) {
        layer.msg(fileVerifyMsg.error);
        return false;
    }
    //上传操作
    //开始上传 获取token 上传图片到七牛
    $bw.getQiNiuToken(function(token) {
        var layerObj = layer.load(1);
        //开始上传
        Qiniu_upload({
            form: $form,
            token: token.data,
            type: 1,
            callback: function(upfile) {
                layer.close(layerObj);
                callback(upfile);
            }
        });
    });
};


/*
 * 获取form表单数据并验证
 */
$bw.getFormInfo = function($form) {
    var info = {};
    //处理空格变为+号问题
    var data = $form.serialize().replace(/\+/g, " ").replace(/\&/g, 'bwduan');
    //防止中文乱码
    data = decodeURIComponent(data, true);
    data = data.split(/bwduan/g);
    for (var i in data) {
        if (data.hasOwnProperty(i)) {
            var index = parseInt(data[i].indexOf('='));
            var key = data[i].substring(0, index);
            var val = data[i].substring(index + 1);
            //名字相同的用逗号隔开
            if ($bw.getLength(info[key]) > 0) {
                info[key] = info[key] + ',' + val;
            } else {
                info[key] = val;
            }
        }
    }
    //进行表单验证
    dataVerifyMsg = verifyFormGroup($form, info);
    if (dataVerifyMsg.type === false) {
        layer.msg(dataVerifyMsg.error);
        return false;
    }
    return info;
};
/**
 * 把数组形式的input 名 变为对象 如a[b][c]：123 ；转化为 a:{b:{c:123}}
 * 必要时调用
 */
$bw.arrInputName = function(data) {
    var info = {};
    for (var i in data) {
        var key = i;
        var val = data[i];
        if (key.indexOf('[') >= 0) {
            key = $bw.replaceAll(key, ']', '');
            var keyArr = key.split('[');
            var keyLen = keyArr.length;
            var jsonStr = "info";
            for (var j = 0; j < keyLen; j++) {
                jsonStr += '[keyArr[' + j + ']]';
                if (eval('typeof(' + jsonStr + ')') == 'undefined') {
                    eval(jsonStr + '={}');
                }
            }
            eval(jsonStr + '="' + val + '"');
        }
    }
    return info;
};


/**
 * 表单提交
 * @param _this
 * @returns {boolean}
 */
$bw.formSubmit = function($form) {
    var data = {};
    //获取form数据
    data = $bw.getFormInfo($form);
    if (data === false) return false;
    //获取文件
    $bw.uploadFormFile($form, function(upfile) {
        for (var j in upfile) { data[j] = upfile[j]; }
        $bw.goSubmit(data, $form)
    });
    return false;
};
$bw.goSubmit = function(info, $form) {
    $bw.ajax({
        data: info,
        url: $form.attr('action'),
        type: $form.attr('method'),
        callback: function(msg) {
            var callbackFn = $form.attr('data-callback');
            if (typeof(callbackFn) == 'undefined' || callbackFn == '') {
                $bw.reloadForm(msg);
            } else {
                $bw[callbackFn](msg);
            }
        },
        errorBack: function(msg) {
            var callbackFn = $form.attr('data-errorBack');
            if (typeof($bw[callbackFn]) == 'function') { $bw[callbackFn](msg); }
        }
    });
};
/*
 * 异步获取layer弹窗
 * author：段
 * obj{
 *  type:类型  1异步  2dom
 *  callback：回调函数
 *  width：宽
 *  url：异步时的url
 *  dom：dom时传入的jq对象
 * }
 */
$bw.showPopup = function(obj) {
    //设置默认
    var config = { width: '800px', title: "INFO" };
    //获取配置
    for (var i in obj) { config[i] = obj[i]; }
    var layerLoad = layer.load(1);
    //默认弹窗后执行的函数
    config.fn = function() {
        //绑定关闭弹窗按钮
        $(document).on('click', '.closeLayer', function() {
            layer.close(config.editLayer);
        });
        //执行回调函数
        if (typeof(config.callback) == 'function') { config.callback(config); }
        layer.close(layerLoad);
    };
    //获取页面方式 2传入dom方式 其他异步获取页面方式
    if (config.type == '2') {
        config.editLayer = layer.open({ zIndex: 10, type: 1, area: config.width, content: config.dom, title: config.title });
        config.fn();
    } else {
        $.get(config.url, function(msg) {
            config.editLayer = layer.open({ zIndex: 10, type: 1, area: config.width, content: msg, title: config.title });
            config.fn();
            //设置弹窗高度，避免过大
            $('.layui-layer-content').attr('style', 'max-height:' + (document.body.offsetHeight - 100) + 'px');
            //设置弹窗位置，避免弹窗下移
            $('#layui-layer' + config.editLayer).css('top', '60px');
        });
    }

};
/* 模板替换 author：段
 * tpldom 模板dom
 * dom 容器dom
 * data 数据
 */
$bw.showTpl = function(tpldom, dom, data) {
    if ($(tpldom)[0]) {
        var interText = doT.template($(tpldom).text());
        $(dom).html(interText(data));
    }
};
$bw.addTpl = function(tpldom, dom, data) {
    if ($(tpldom)[0]) {
        var interText = doT.template($(tpldom).text());
        $(dom).append(interText(data));
    }
};
/*转义所有的歧义字符*/
$bw.escape = function(str) {
    var returnStr = str;
    returnStr = $bw.replaceAll(returnStr, "\"", "&#34;");
    returnStr = $bw.replaceAll(returnStr, "\'", "&#39;");
    returnStr = $bw.replaceAll(returnStr, "\<", "&#60;");
    returnStr = $bw.replaceAll(returnStr, "\>", "&#62;");
    return returnStr;
};
/*
 *用户模板替换时 避免输出undefined author：段
 */
$bw.N = function(str) {
    if (typeof(str) == 'undefined' || str === null) str = '';
    return $bw.escape(str);
};
$bw.TN = function(str) {
    if (typeof(str) == 'undefined' || str === null) str = '--';
    return $bw.escape(str);
};
/**
 * replaceAll
 */
$bw.replaceAll = function(str, s1, s2) {
    if (typeof(str) == 'string') {
        return str.replace(new RegExp(s1, "gm"), s2);
    } else {
        return str;
    }
};

/**
 *表单验证
 */
function verifyFormGroup($form, data) {
    var returnMsg = { type: true };
    $form.find('.input-false').removeClass('input-false');
    $form.find('.input-box-false').removeClass('input-box-false');
    for (var i in data) {
        if (typeof(data[i]) != 'string') continue;
        var $e = $form.find("*[name='" + i + "']");
        if (!$e[0]) continue;
        var verifyMsg = verifyForm($e[0]);
        if (!verifyMsg.type) {
            //没通过验证 添加验证错误
            $e.addClass('input-false');
            $e.parent().addClass('input-box-false');
            returnMsg.type = false;
            returnMsg.error = verifyMsg.error;
            break;
        }
    }
    return returnMsg;
}
/*
 * 数据验证 author：段
 * $e 验证的jq对象
 */
function verifyForm(e) {
    var returnMsg = { type: true };
    var type = e.dataset.verify;
    if (!type) return returnMsg;
    var msg = $(e).val();
    var reg = '';
    var errorMsg = e.dataset.errormsg;
    //新的验证方法
    var verifyFunction = {
        notNull: function() {
            if (msg === '') returnMsg = { type: false, error: "不能为空" };
        },
        maxLength: function(max) {
            if (!max) max = e.dataset.max;
            max = max ? max : 0;
            var msgLen = msg.replace(/[^\x00-\xff]/g, 'xx').length;
            if (msgLen > max) returnMsg = { type: false, error: "超过最大限制长度" };
        },
        minLength: function(min) {
            if (!min) min = e.dataset.min;
            min = min ? min : 0;
            var msgLen = msg.replace(/[^\x00-\xff]/g, 'xx').length;
            if (msgLen < min) returnMsg = { type: false, error: "超过最小限制长度" };
        },
        number: function() {
            var max = e.dataset.max;
            var min = e.dataset.min;
            var xiaoshu = e.dataset.xiaoshu;
            if (max && parseFloat(msg) > parseFloat(max)) {
                returnMsg = { type: false, error: '超出最大值' };
                return;
            }
            if (min && parseFloat(msg) < parseFloat(min)) {
                returnMsg = { type: false, error: '超出最小值' };
                return;
            }
            if (xiaoshu && msg.toString().split(".")[1].length > xiaoshu) {
                returnMsg = { type: false, error: '限制小数后' + xiaoshu + '位' };
                return;
            }
            reg = /^(-){0,1}[0-9]+(\.[0-9]+)?$/;
            verifyFunction.reg(reg, '只能填写数字');
        },
        phone: function() {
            reg = /^[0-9]+(\-[0-9]+)*$/;
            verifyFunction.reg(reg, '手机号格式不正确');
        },
        mail: function() {
            reg = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+)+(\.[a-zA-Z0-9]+)+$/;
            verifyFunction.reg(reg, '邮箱格式不正确');
        },
        IDCard: function() {
            if (msg.length === 15) {
                reg = /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$/;
            } else if (msg.length === 18) {
                reg = /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/;
            } else {
                returnMsg = { type: false, error: "身份证格式不正确" };
                return;
            }
            verifyFunction.reg(reg, '身份证格式不正确');
        },
        //正则匹配
        reg: function(reg, error) {
            if (!reg.test(msg)) returnMsg = { type: false, error: error };
        }
    };
    var typeArr = type.split(',');
    for (var i = 0, len = typeArr.length; i < len; i++) {
        if (type && typeof(verifyFunction[typeArr[i]]) == 'function') {
            verifyFunction[typeArr[i]]();
            if (!returnMsg.type) break;
        }
    }
    if (errorMsg) returnMsg.error = errorMsg;
    return returnMsg;
}
/**
 * 验证上传文件
 * @author duantingting@bestwise.cc 2017-01-19
 * @param  {[type]} $form [description]
 * @return {[type]}       [description]
 */
function verifyFile($form) {
    var returnMsg = { type: true, error: '' };
    var files = $form.find("input[type='file']");
    var file = null;
    var dom = null;
    if (files.length === 0) {
        returnMsg.type = null;
        return returnMsg;
    }
    var verifyFunction = {
        notNull: function() {
            if (file.length <= 0 && dom.dataset.hasfile !== 'true') {
                returnMsg.type = false;
                var errorMsg = dom.dataset.errormsg;
                errorMsg = errorMsg ? errorMsg : "Upload files can not be empty";
                returnMsg.error = errorMsg;
            }
        },
        suffix: function() {
            if (file.length <= 0) return returnMsg;
            var fileName = file[0].name;
            var type = fileName.substring(fileName.lastIndexOf(".") + 1);
            var suffixList = dom.dataset.suffix;
            if (!suffixList) return returnMsg;
            if (suffixList.indexOf(type) === -1) {
                returnMsg.type = false;
                returnMsg.error = "The suffix does not meet the requirements";
            }
        },
        imgWidth: function() {
            if (file.length <= 0) return returnMsg;
            var widthAndHeight = dom.dataset.widthandheight;
            if (!widthAndHeight) return returnMsg;
            var imgParent = $(dom).parent().siblings(".upload-img");
            var arr = widthAndHeight.split(',');
            if (imgParent[0].dataset.width != arr[0] || imgParent[0].dataset.height != arr[1]) {
                returnMsg.type = false;
                returnMsg.error = 'Picture size is not standard，use' + arr[0] + 'x' + arr[1];
            }
        },
        fileSize: function() {
            if (file.length <= 0) return returnMsg;
            var maxSize = dom.dataset.maxsize;
            maxSize = maxSize ? maxSize : $bw.maxImgSize;
            if (file[0].size > maxSize * 1048576) {
                returnMsg.type = false;
                returnMsg.error = 'File too large，use' + maxSize + 'M';
            }
        }
    };
    files.each(function(i, e) {
        dom = e;
        file = dom.files;
        var verify = dom.dataset.verify;
        if (verify) {
            var typeArr = verify.split(',');
            for (var i = 0, len = typeArr.length; i < len; i++) {
                if (verify && typeof(verifyFunction[typeArr[i]]) == 'function') {
                    verifyFunction[typeArr[i]]();
                    if (returnMsg.type === false) {
                        break;
                    }
                }
            }
            if (returnMsg.type === false) {
                return false;
            }
        }
    });
    return returnMsg;
}
/*
 * ajax  author：段
 */
$bw.ajax = function(obj) {
    var layerLoad = layer.load(1);
    obj.type = obj.type.toUpperCase();
    obj.data = typeof(obj.data) != 'undefined' ? obj.data : {};
    if (obj.type == 'POST' || obj.type == 'PUT') obj.data = JSON.stringify(obj.data);
    var token = $bw.userInfo ? $bw.userInfo.AccessToken : '';
    $.ajax({
        type: obj.type,
        url: $bw.httpHeader + obj.url,
        data: obj.data,
        dataType: "json",
        headers: { "Authorization": 'Bearer ' + token },
        contentType: 'application/json; charset=utf-8',
        success: function(res) {
            layer.close(layerLoad);
            if (res.Status != 1) {
                layer.msg(res.Message, function() {
                    //没有登录
                    if (res.code == '40100') {
                        addSession({ name: 'userSessionInfo', info: null });
                        addStorage({ name: 'userSessionInfo', info: null });
                        window.parent.location.href = "./login.html";
                    }
                });
                if (typeof(obj.errorBack) == 'function') { obj.errorBack(res); }
                return;
            }
            if (typeof(obj.callback) == 'function') { obj.callback(res); }
        },
        error: function(XMLHttpRequest, textStatus, errorThrown) {

            layer.close(layerLoad);
            layer.msg(JSON.parse(XMLHttpRequest.responseText).Message);
            console.info(XMLHttpRequest.responseText);
            if (typeof(obj.errorBack) == 'function') {
                obj.errorBack(JSON.parse(XMLHttpRequest.responseText));
            }
        }
    });
};

/**
 * 获取对象长度
 * @author duantingting@bestwise.cc 2017-01-19
 * @param  {[type]} obj [description]
 * @return {[type]}     [description]
 */
$bw.getLength = function(obj) {
    var n = 0;
    for (var i in obj) { n++; }
    return n;
};
/**
 * 获取url参数
 * @author duantingting@bestwise.cc 2017-01-19
 * @param  {[type]} name [description]
 * @return {[type]}      [description]
 */
$bw.getUrlInfo = function(name) {
    var url = decodeURI(window.document.location.search.substr(1));
    if (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = url.match(reg);
        if (r !== null) return unescape(r[2]);
        return null;
    } else {
        if (typeof(url) == "string" && url.length > 0) {
            var u = url.split("&"),
                getInfo = {},
                j = '';
            for (var i in u) {
                j = u[i].split("=");
                getInfo[j[0]] = j[1];
            }
            return getInfo;
        }
        return {};
    }
};
/**
 * 获取url文件名
 * @author duantingting@bestwise.cc 2017-01-19
 */
$bw.GetPageName = function() {
    var url = window.location.href; //获取完整URL 
    var tmp = new Array(); //临时变量，保存分割字符串 
    tmp = url.split("/"); //按照"/"分割 
    var pp = tmp[tmp.length - 1]; //获取最后一部分，即文件名和参数 
    tmp = pp.split("?"); //把参数和文件名分割开 
    return tmp[0];
};
//时间戳转文字
function getLocalTime(nS) {
    return new Date(parseInt(nS)).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
}

function add0(m) {
    return m < 10 ? '0' + m : m;
}

function format(shijianchuo, type) {
    if (!shijianchuo) return '--';
    //shijianchuo是整数，否则要parseInt转换
    var time = new Date(parseInt(shijianchuo));
    var y = time.getFullYear();
    var m = time.getMonth() + 1;
    var d = time.getDate();
    var h = time.getHours();
    var mm = time.getMinutes();
    var s = time.getSeconds();
    if (type == 'year') {
        return y + '-' + add0(m) + '-' + add0(d);
    }
    if (type == 'time') {
        return add0(h) + ':' + add0(mm);
    }
    return y + '-' + add0(m) + '-' + add0(d) + ' ' + add0(h) + ':' + add0(mm) + ':' + add0(s);
}
//写入Cookie缓存 时间以秒计算
function addStorage(obj) {
    //如果不规定生命周期 默认一年
    if (!obj.livetime) { obj.livetime = 60 * 60 * 24 * 365; }
    var msg = {
        info: obj.info,
        time: new Date().getTime(),
        livetime: obj.time
    };
    localStorage[obj.name] = JSON.stringify(msg);
}
//取Cookie缓存
function getStorage(name) {
    var msg = null;
    if (typeof(localStorage[name]) == 'undefined' || localStorage[name] === null || localStorage[name] == 'null') {
        return null;
    } else {
        msg = JSON.parse(localStorage[name]);
        if (!msg.info || msg.info === null || msg.info.length === 0) {
            return null;
        }
    }
    if (msg.time + msg.livetime * 1000 < new Date().getTime()) {
        return null;
    }
    return msg.info;
}
//写入session缓存 时间以秒计算
function addSession(obj) {
    var msg = {
        info: obj.info,
        time: new Date().getTime(),
        livetime: obj.time
    };
    sessionStorage[obj.name] = JSON.stringify(msg);
}
//取session缓存
function getSession(name) {
    var msg = null;
    if (typeof(sessionStorage[name]) == 'undefined' || sessionStorage[name] === null || sessionStorage[name] === 'null') {
        return null;
    } else {
        msg = JSON.parse(sessionStorage[name]);
        if (!msg.info || msg.info === null || msg.info.length === 0) {
            return null;
        }
    }
    if (msg.livetime) {
        if (msg.time + msg.livetime * 1000 < new Date().getTime()) {
            return null;
        }
    }
    return msg.info;
}




function verifyLogin() {
    var userInfo = getSession('userSessionInfo');
    if (userInfo === null) {
        userInfo = getStorage('userSessionInfo');
        addSession({ name: 'userSessionInfo', info: userInfo });
    }
    if ((typeof(IS_LOGIN) == 'undefined' || IS_LOGIN === false) && (userInfo === null)) {
        layer.msg('请先登录', function() { window.parent.location.href = './login.html'; });
        return false;
    } else {
        $bw.userInfo = userInfo;
        return true;
    }
}



function configLoad() {
    //载入语言包
    var lang = getSession('lang');
    if (lang) {
        $bw.lang = lang;
    } else {
        $.get('../data/en.json', function(msg) {
            addSession({ info: msg, name: 'lang', time: 3600 * 24 });
            $bw.lang = msg;
            console.info(msg);
        }, 'json');
    }
    if (document.body.offsetWidth > 1600) {
        $('html').attr('style', 'font-size:16px;');
    } else {
        $('html').attr('style', 'font-size:14px;');
    }

    //input 选中样式
    var bw_action_box = $('.bw-action-box');
    bw_action_box.find('input').blur(function() {
        $(this).parent().removeClass('c-bd-blue') /*.find('.iconfont').removeClass('c-bd-blue c-blue').addClass('c-light')*/ ;
    });
    bw_action_box.find('input').focus(function() {
        $(this).parent().addClass('c-bd-blue') /*.find('.iconfont').addClass('c-bd-blue c-blue').removeClass('c-light')*/ ;
    });

    //table排序
    $(document).on('click', '.tableSort', function() {
        var orderBy = $(this).attr('data-orderBy');
        if (orderBy == 'desc') {
            $(this).attr('data-orderBy', 'asc').find('.iconfont').removeClass('bw-solidDown-ico').addClass('bw-solidUp-ico');
        } else {
            $(this).attr('data-orderBy', 'desc').find('.iconfont').removeClass('bw-solidUp-ico').addClass('bw-solidDown-ico');
        }
        var ajaxData = { orderBy: $(this).attr('data-field') + ' ' + orderBy };
        $bw.showTableList({ table: $('.bw-table'), data: ajaxData, init: false, history: false });
    });
    //浏览器后退
    window.onpopstate = function() {
        layer.closeAll('page');
        if (typeof(display) == 'function') display();
    };
    //返回按钮
    $(document).on('click', '.goBack', function() {
        layer.closeAll('page');
        window.history.go(-1);
    });
    //刷新按钮
    $(document).on('click', '.refresh', function() {
        window.location.reload();
    });
    //编辑层点击确定进行提交
    $(document).on('submit', '.bw-form', function() {
        $bw.formSubmit($(this));
        return false;
    });
    //放大图片显示
    $(document).on('click', '.bw-show-img', function() {
        var img_url = $(this).attr('src');
        img_url = img_url.split('?')[0];
        var img = new Image();
        var open = function(width, height) {
            var area = '';
            if (width > height) {
                if (width > 600) width = 600;
                area = width + 'px';
            } else {
                if (height > 500) height = 500;
                area = ['auto', height + 'px'];
            }
            layer.open({ type: 1, area: area, title: false, content: '<div><img src="' + img_url + '" style="max-width:600px;max-height: 500px;display:block;margin:0 auto"></img></div>' });
        };
        img.src = img_url;
        if (img.complete) {
            open(img.width, img.height);
        } else {
            img.onload = function() {
                open(img.width, img.height);
            };
        }

    });

    //隐藏用户操作框
    $(document).click(function(e) {
        if ($(e.target).hasClass('topShowAction')) return;
        $(".user-action", parent.document).slideUp(300);
    });
    //回车搜索
    $(document).on('keydown', '.bw-search-input', function(e) {
        if (e.keyCode == 13) {
            $(this).parent().find('.bw-search').click();
        }
    });
    //图片上传按钮
    $(document).on('click', '.bw-upload-btn', function() {
        $(this).parent().find('.upload-img-input').click();
    });
    $(document).on('click', '.bw-upload-img', function() {
        $(this).parent().find('.upload-img-input').click();
    });
    if (typeof(display) == 'function') { display(); }
    getUserActionAuthority();
}

function exportAjax(obj) {
    var layerLoad = layer.load(1);
    obj.type = obj.type.toUpperCase();
    obj.data = typeof(obj.data) != 'undefined' ? obj.data : null;
    if (obj.type == 'POST' || obj.type == 'PUT') obj.data = JSON.stringify(obj.data);
    var token = $bw.userInfo ? $bw.userInfo.AccessToken : '';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', $bw.httpHeader + obj.url, true);
    xhr.setRequestHeader("Content-Type", "application/json; charset=utf-8");
    xhr.setRequestHeader("Authorization", 'Bearer ' + token);
    xhr.setRequestHeader("Accept", "application/json,text/javascript");
    xhr.responseType = 'arraybuffer';
    xhr.onreadystatechange = function() { //response
        if (xhr.readyState == 4 && xhr.status == 200) {
            layer.close(layerLoad);
            if (typeof(obj.callback) == 'function') { obj.callback(xhr.response); }
        } else if (xhr.readyState == 4 && xhr.status != 200) {
            layer.close(layerLoad);
            layer.msg('network error');
        }
    };
    xhr.send(obj.data);
}

/**
 * 获取用户是否具有操作权限
 * @author duantingting@bestwise.cc 2016-12-07
 * @return {[type]} [description]
 */
function getUserActionAuthority() {
    return;
    if (!$bw.userInfo) return;
    var authorityArr = {};
    var authorityList = $bw.userInfo.operation.items;
    var go = function(authorityId, e) {
        if (authorityArr[authorityId] === true) {
            //有权限

        } else if (authorityArr[authorityId] === false) {
            //无权限
            $(e).attr('onclick', "showAuthorityTips();return false");
            if (e.dataset.authorshow === 'true') {
                $(e).attr('class', '');
            } else {
                $(e).remove();
            }
        } else {
            //没遍历
            authorityArr[authorityId] = false;
            for (var i in authorityList) {
                if (authorityId == authorityList[i].id) {
                    authorityArr[authorityId] = true;
                }
            }
            go(authorityId, e);
        }
    };

    $('.bw-authority').each(function(i, e) {
        var authorName = e.dataset.authorityname;
        var authorityId = getAuthorityList(authorName);
        go(authorityId, e);
    });
}

function showAuthorityTips() {
    layer.msg($bw.lang.showAuthorityTips);
}

function getAuthorityList(name, arr) {
    var authorityList = {
        "vendorCreate": "13aac22d-f520-409b-a200-a904021c5aa1"
    };
    if (arr) return authorityList;
    return authorityList[name];
}
