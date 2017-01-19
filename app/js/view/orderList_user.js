function htmlLoad() {
    var table = $('#user-order-table');
    $('.bw-search').click(function() {
        searchFN();
    });
    $('#trip-type').change(function() {
        searchFN();
    });
    $('#user-status').change(function() {
        searchFN();
    });
    //取消
    table.on('click', '.cancel', function() {
        var data = $bw.listData[this.dataset.key];
        $bw.showTpl($('#tpl-cancel-layer'), $('#cancel-form'), data);
        $bw.showPopup({
            type: 2,
            dom: $('#cancel-layer'),
            title: "取消原因",
            width: "680px"
        });
    });
    $(document).on('click', '.cancel-type', function() {
        $('.cancel-btn-box').find('.bw-btn-warning').removeClass('bw-btn-warning').addClass('bw-btn-primary');
        $(this).addClass('bw-btn-warning').removeClass('bw-btn-primary');
        $('#cancel-type').val(this.innerHTML);
    });
    $(document).on('click', '#cancel-submit', function() {
        var cancelType = $('#cancel-type').val();
        var BLID = $('#BLIDS').val();
        if (cancelType == '') {
            layer.msg('请选择取消原因');
            return false;
        }
        if (BLID == '') {
            layer.msg('没有获取到信息标记');
            return false;
        }
        $bw.ajax({
            url: 'api/PickupOrderList/CanceledBooking',
            type: 'post',
            data: {
                Notes: '取消类型：' + cancelType + '。其他原因：' + $('#other').val(),
                BLIDS: [BLID]
            },
            callback: function() {
                $bw.reloadForm();
            }
        });
        return false;
    })
}

function display() {
    var options = $bw.getHtmlOption();
    $('#searchInput').val(options.keyword);
    var ajaxData = {
        PageIndex: 1,
        ajaxType: "post",
        url: "api/PickupOrderList/GetPickupUserOrderList"
    };
    $bw.showTableList({
        table: $('#user-order-table'),
        data: ajaxData,
        init: true
    });
    //显示表头搜索条件
    $transfer.getDictItemList('TripType', function(res) {
        $bw.showTpl($('#tpl-header-select'), $('#trip-type'), res);
        $('#trip-type').val(options.tripType);
    });
    $transfer.getDictItemList('BookState', function(res) {
        $bw.showTpl($('#tpl-header-select'), $('#user-status'), res);
        $('#user-status').val(options.userStatus);
    });
}
/**
 * 搜索
 * @author duantingting@bestwise.cc 2017-01-18
 * @return {[type]} [description]
 */
function searchFN() {
    var keyword = $('#searchInput').val();
    var tripType = $('#trip-type').val();
    var userStatus = $('#user-status').val();
    var Filter = [];
    if (keyword) {
        Filter.push({
            GroupName: "sample1",
            FieldName: "Name",
            FieldValue: keyword,
            SqlOperator: 0
        });
        Filter.push({
            GroupName: "sample1",
            FieldName: "IDCardNo",
            FieldValue: keyword,
            SqlOperator: 0
        });
    }
    if (tripType) {
        Filter.push({
            GroupName: "sample2",
            FieldName: "TripType",
            FieldValue: tripType,
            SqlOperator: 4
        });
    }
    if (userStatus) {
        Filter.push({
            GroupName: "sample3",
            FieldName: "ChildStatus",
            FieldValue: userStatus,
            SqlOperator: 4
        });
    }
    var ajaxData = {
        PageIndex: 1,
        Filter: Filter,
        keyword: keyword,
        tripType: tripType,
        userStatus: userStatus
    };
    $bw.showTableList({
        table: $('#user-order-table'),
        data: ajaxData
    });
}
