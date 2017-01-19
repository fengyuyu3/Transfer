var SELECTINFO = {};

function htmlLoad() {
    var table = $('#waiting-table');

    $('#start-data').on('click', function() {
        laydate({
            elem: '#start-data-input'
        });
    });
    //搜索
    $('#search-btn').click(function() {
        searchFN();
    });
    //全选
    table.on('change', '.select-all', function() {
        if ($(this).is(':checked')) {
            $('.btn-checkbox').prop('checked', true);
        } else {
            $('.btn-checkbox').prop('checked', false);
        }
        createBtnObj();
    });
    //单选
    table.on('change', '.btn-checkbox', function() {
        if ($bw.getLength(SELECTINFO) >= 6) {
            layer.msg($bw.lang.maxUser);
            return false;
        }

        if (!$(this).is(':checked')) {
            $('.select-all').prop('checked', false);
        }
        createBtnObj();
    });
    //点击排班
    $('#artificial-btn').click(function() {
        if ($bw.getLength(SELECTINFO) === 0) {
            layer.msg($bw.lang.nullUser);
            return false;
        }
        showArtificialLayer();
    });
}

function display() {
    var options = $bw.getHtmlOption();
    $('#start-data-input').val(options.TakeOffDate);
    var ajaxData = {
        PageIndex: 1,
        PageSize: $bw.pages,
        ajaxType: "POST",
        url: "api/BookingList/GetBookingPendingList"
    };
    $bw.showTableList({
        table: $('#waiting-table'),
        data: ajaxData,
        init: true
    });
    //获取所有的航班时段
    $transfer.getAllFlightTime(function(res) {
        $bw.showTpl($('#tpl-flight-time'), $('#flight-time'), res.Data);
        $('#flight-time').val(options.TimeTable);
    });
}
/**
 * 搜索
 * @author duantingting@bestwise.cc 2017-01-12
 * @return {[type]} [description]
 */
function searchFN() {
    var startData = $('#start-data-input').val();
    var flightTime = $('#flight-time').val();
    var ajaxData = {
        PageIndex: 1,
        TimeTable: flightTime,
        TakeOffDate: startData,
    };
    $bw.showTableList({
        table: $('#waiting-table'),
        data: ajaxData
    });
}
/**
 * 构建被选中的关联数组
 * @author duantingting@bestwise.cc 2016-10-27
 * @return 
 */
function createBtnObj() {
    var isAll = true;
    $('.btn-checkbox').each(function(i, e) {
        var key = e.dataset.key;
        if ($(e).is(':checked')) {
            SELECTINFO[key] = key;
        } else {
            isAll = false;
            delete(SELECTINFO[key]);
        }
    });
    if (isAll) {
        $('.select-all').prop('checked', true);
    } else {
        $('.select-all').prop('checked', false);
    }
}

function showArtificialLayer() {
    getDriverList(function(res) {
        var artificialLayer = $('#artificial-layer');
        $bw.showTpl($('#tpl-artificial-layer'), artificialLayer, {});
        $bw.showPopup({
            type: 2,
            dom: artificialLayer,
            title: "手动排班——选择司机",
            width: "680px"
        });
    });
}

function getDriverList(callback) {
    var arr = [];
    for (var i in SELECTINFO) {
        arr.push(SELECTINFO[i]);
    }
    $bw.ajax({
        url: 'api/Drivers/GetScheduleDriver',
        type: 'post',
        data: arr,
        callback: function(res) {
            callback(res);
        }
    });
}
