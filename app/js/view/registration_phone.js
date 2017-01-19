var userInfoArr;
var todo;

function htmlLoad() {
    var $form = $('#write-form');
    $('#get-user-info').click(function() {
        if (!$('#arrive-tation-code').val()) {
            layer.msg('目的地需从列表中选择，不能直接输入');
        }
        $('#flight-date-num').val(Date.parse(new Date($('#FlightDate-input').val())));
        $form.attr('action', 'api/Trips/GetTripsList').attr('method', 'post').attr('data-callback', 'showInfo');
        $form.submit();
    });
    $('#trip-date-btn').click(function() {
        laydate({
            elem: '#FlightDate-input'
        });
    });
    $('#flight-info').on('click', '.f-i-list', function() {
        $('#flight-info').find('.action').removeClass('action');
        $(this).addClass('action');
        showWriteBox(this.dataset.key);
    });

    //目的地选择
    var list = $('#arrive-tation-list');
    $(document).on('focus', '#ArriveStation-input', function() {
        list.css('opacity', '1');
        list.css('z-index', '2');
        getSelectList(this.value, function(res) {
            $bw.showTpl($("#tpl-tation-select"), list, res);
        });
    });

    $(document).on('keyup', '#ArriveStation-input', function() {
        getSelectList(this.value, function(res) {
            $bw.showTpl($("#tpl-tation-select"), list, res);
        });
    });
    $(document).on('blur', '#ArriveStation-input', function() {
        list.css('opacity', '0');
        setTimeout(function() {
            list.css('z-index', '-1');
            list.html('');
        }, 300);
    });
    $(document).on('click', '.arrive-tation-name', function() {
        var id = this.dataset.id;
        $('#arrive-tation-code').val(id);
        $('#ArriveStation-input').val($(this).html());
    });


    $(document).on('focus', '.select-input', function() {
        var listBox = $('#' + this.dataset.box);
        var tpl = $('#' + this.dataset.tpl);
        listBox.css('opacity', '1');
        listBox.css('z-index', '2');
        if ($(this).attr('id') == 'start-time') {
            getStartTime(this.value, function(res) {
                $bw.showTpl(tpl, listBox, res);
            });
        } else {
            getAddressList(this.value, function(res) {
                $bw.showTpl(tpl, listBox, res);
            });
        }
    });
    $(document).on('keyup', '.select-input', function() {
        var listBox = $('#' + this.dataset.box);
        var tpl = $('#' + this.dataset.tpl);
        var id = $(this).attr('id');
        var val = this.value;
        clearTimeout(todo);
        todo = setTimeout(function() {
            if (id == 'start-time') {
                getStartTime(val, function(res) {
                    $bw.showTpl(tpl, listBox, res);
                });
            } else {
                getAddressList(val, function(res) {
                    $bw.showTpl(tpl, listBox, res);
                });
            }
        }, 500);
    });
    $(document).on('blur', '.select-input', function() {
        var listBox = $('#' + this.dataset.box);
        listBox.css('opacity', '0');
        setTimeout(function() {
            listBox.css('z-index', '-1');
            listBox.html('');
        }, 300);
    });
    $(document).on('click', '.r-start-time-li', function() {
        $('#start-time').val($(this).html());
    });
    $(document).on('click', '.r-addtess-li', function() {
        $('#PickupAddress-input').val($(this).html());
        $('#PickupLongitude').val(this.dataset.lng);
        $('#PickupLatitude').val(this.dataset.lat);
    });

    //上传
    $(document).on('click', '#submit', function() {
        if ($('#PickupLongitude').val() == '') {
            layer.msg('上车地址必须从列表中选择');
            return false;
        }
        $form.attr('action', 'api/BookingList/AddBookingByCenter').attr('method', 'post').attr('data-callback', '');
        var day = $('#pickup-time')[0].dataset.val;
        var time = $('#start-time').val();
        $('#pickup-time').val(Date.parse(new Date(day + ' ' + time)));
        var val = $('#PickupAddress-input').val();
        $('#Address').val(val);
        return true;
    });

    $('#FlightDate-input').click(function() {
        $('#trip-date-btn').click();
    });
}

/**
 * 显示查询出的航班信息
 * @author duantingting@bestwise.cc 2017-01-12
 * @param  {[type]} res [description]
 * @return {[type]}     [description]
 */
$bw.showInfo = function(res) {
    userInfoArr = res.Data;
    $('#flight-info').html('');
    $('#write-info').html('');
    $bw.showTpl($('#tpl-flight-info'), $('#flight-info'), userInfoArr);
    if (userInfoArr.length == 1) {
        showWriteBox(0);
    }
};
/**
 * 显示预约填写界面（判断是否有资格）
 * @author duantingting@bestwise.cc 2017-01-12
 * @return {[type]} [description]
 */
function showWriteBox(key) {
    $('#write-info').html('');
    if (userInfoArr[key].IsValid && userInfoArr[key].ValidType == 2) {
        $bw.showTpl($('#tpl-write-info'), $('#write-info'), userInfoArr[key]);
    }
}
/**
 * 获取目的地列表
 * @author duantingting@bestwise.cc 2017-01-17
 * @param  {[type]}   keyword  [description]
 * @param  {Function} callback [description]
 * @return {[type]}            [description]
 */
function getSelectList(keyword, callback) {
    if (keyword === '') {
        callback({});
        return;
    }
    var cityAirport = getSession('cityAirport' + keyword);
    if (cityAirport) {
        callback(cityAirport);
    } else {
        $bw.ajax({
            url: 'api/CityAirport/GetCityAirportList',
            type: 'get',
            data: {
                keyword: keyword
            },
            callback: function(res) {
                addSession({
                    name: 'cityAirport' + keyword,
                    info: res.Data
                });
                callback(res.Data);
            }
        });
    }
}
/**
 * 获取上车时间列表
 * @author duantingting@bestwise.cc 2017-01-17
 * @param  {[type]}   val      [description]
 * @param  {Function} callback [description]
 * @return {[type]}            [description]
 */
function getStartTime(val, callback) {
    var returnData = [];
    var time = parseInt(val);
    if (time > 0 && time < 10) {
        var i = 0;
        for (i; i < 6; i++) {
            returnData.push('0' + time + ':' + i + '0');
        }
    } else if (time >= 10 && time <= 23) {
        var i = 0;
        for (i; i < 6; i++) {
            returnData.push(time + ':' + i + '0');
        }
    }
    callback(returnData);
}
/**
 * 获取百度模糊匹配列表
 * @author duantingting@bestwise.cc 2017-01-17
 * @param  {[type]}   val      [description]
 * @param  {Function} callback [description]
 * @return {[type]}            [description]
 */
function getAddressList(val, callback) {
    if (val == '') {
        callback({});
        return;
    }
    var returnData = getSession('suggestion' + val);
    if (returnData) {
        callback(returnData);
    } else {
        $.get('http://api.map.baidu.com/place/v2/suggestion?query=' + val + '&region=成都市&city_limit=true&output=json&ak=BORW59HeNs0amhebvmy1qvrwySYgcWDI', '', function(res) {
            addSession({
                name: 'suggestion' + val,
                info: res.result
            });
            callback(res.result);
        }, 'json');
    }
}
