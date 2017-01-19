var todo;

function htmlLoad() {
    $('.bw-tbody').on('click', '.acceptAppointment', function() {
        var data = {
            PickupAddress: this.dataset.address,
            BLID: this.dataset.id,
            lng: this.dataset.lng,
            lat: this.dataset.lat,
            PickupTime: this.dataset.time
        };
        /*if(!data.lng){
             layer.msg('请选择上车地址！');return false;
         }*/
        if (!data.PickupTime) {
            layer.msg('请选择上车时间！');
            return false;
        }

        layer.confirm('你确定要接受预约吗？', {
            icon: 3,
            title: '提示'
        }, function(index) {
            $bw.ajax({
                url: 'api/BookingList/ConfirmeBooking',
                type: 'post',
                data: data,
                callback: function() {
                    $bw.reloadForm();
                }
            });
        });
    });

    $('#min-data-input').click(function() {
        $('#min-data').click();
    });
    $('#max-data-input').click(function() {
        $('#max-data').click();
    });

    //列表的地址选择和时间选择
    $(document).on('focus', '.select-input', function() {
        var listBox = $(this).next();
        var tpl = $('#' + this.dataset.tpl);
        var isAddress = $(this).hasClass('address-input');
        listBox.css('opacity', '1');
        listBox.css('z-index', '2');
        if (!isAddress) {
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
        var listBox = $(this).next();
        var tpl = $('#' + this.dataset.tpl);
        var isAddress = $(this).hasClass('address-input');
        var val = this.value;
        clearTimeout(todo);
        todo = setTimeout(function() {
            if (!isAddress) {
                getStartTime(val, function(res) {
                    $bw.showTpl(tpl, listBox, res);
                });
            } else {
                getAddressList(val, function(res) {
                    $bw.showTpl(tpl, listBox, res);
                });
            }
        }, 300);
    });
    $(document).on('blur', '.select-input', function() {
        var listBox = $(this).next();
        listBox.css('opacity', '0');
        setTimeout(function() {
            listBox.css('z-index', '-1');
            listBox.html('');
        }, 300);
    });

    $(document).on('click', '.r-start-time-li', function() {
        var value = $(this).html()
        var box = $('#' + $(this).parent().attr('data-id'));
        $(this).parent().prev().val(value);
        box.find('.acceptAppointment').attr('data-time', value);
    });
    $(document).on('click', '.r-addtess-li', function() {
        var value = $(this).html();
        var lng = this.dataset.lng;
        var lat = this.dataset.lat;
        var box = $('#' + $(this).parent().attr('data-id'));
        box.find('.address-input').val(value);
        box.find('.acceptAppointment').attr('data-address', value).attr('data-lng', lng).attr('data-lat', lat);
    });


    //选择日期
    $('#min-data').click(function() {
        laydate({
            elem: '#min-data-input'
        });
    });
    $('#max-data').click(function() {
        laydate({
            elem: '#max-data-input'
        });
    });
    //搜索
    $('#search').click(function() {
        searchFN();
    });
}

function display() {
    var options = $bw.getHtmlOption();
    $('#min-data-input').val(options.minDate);
    $('#max-data-input').val(options.maxDate);
    $('#min-time-input').val(options.minTime);
    $('#max-time-input').val(options.maxTime);

    var ajaxData = {
        PageIndex: 1,
        PageSize: $bw.pages,
        ajaxType: "POST",
        url: "api/BookingList/GetTBCBookingList"
    };
    $bw.showTableList({
        table: $('#wechat-table'),
        data: ajaxData,
        init: true
    });
}
/**
 * 搜索
 * @author duantingting@bestwise.cc 2017-01-18
 * @return {[type]} [description]
 */
function searchFN() {
    var minDate = $('#min-data-input').val();
    var maxDate = $('#max-data-input').val();
    var minTime = $('#min-time-input').val();
    var maxTime = $('#max-time-input').val();
    var ajaxData = {
        PageIndex: 1,
        minDate: minDate,
        maxDate: maxDate,
        minTime: minTime,
        maxTime: maxTime
    };
    if (minDate) {
        minTime = minTime ? minTime : '00:00';
        ajaxData.StartTime = minDate + ' ' + minTime;
    }
    if (maxDate) {
        maxTime = maxTime ? maxTime : '00:00';
        ajaxData.EndTime = maxDate + ' ' + maxTime;
    }

    $bw.showTableList({
        table: $('#wechat-table'),
        data: ajaxData
    });
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
    if (val === '') val = 8;
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
