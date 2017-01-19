function htmlLoad() {
    var table = $('#driver-order-table');
    //日期插件
    $(document).on('click', '#start-date', function() {
        laydate({
            elem: '#start-date-input'
        });
    });
    //点击显示乘客列表
    table.on('click', '.driver-order-tr', function(e) {
        if ($(e.target).hasClass('bw-btn')) return;
        var $this = $(this);
        var tbody = $this.next().find('.bw-tbody');
        getUserList(this.dataset.id, function(res) {
            if (res) {
                $bw.showTpl($('#tpl-child-table'), tbody, res.Items);
            } else {
                $bw.showTpl($('#tpl-child-table'), tbody, {});
            }

            $this.next().toggle(300);
        });
    });
    /*table.on('click','.trip-class,.driver-status',function(){
        var $this=$(this);
        if($this.hasClass('bw-solidDown-ico')){
            $this.removeClass('bw-solidDown-ico').addClass('bw-solidRight-ico');
        }else{
            $this.removeClass('bw-solidRight-ico').addClass('bw-solidDown-ico');
        }
        $this.next().slideToggle(200);
    });
    $(document).click(function(e){
        var target=$(e.target);
        if(target.hasClass('driver-status') || target.hasClass('trip-class') || target.hasClass('header-select-list')) return;
        $('.bw-solidRight-ico').removeClass('bw-solidRight-ico').addClass('bw-solidDown-ico');
        $('.title-search-select').slideUp(200);
    });
    table.on('click','.header-select-list',function(){
        $(this).parent().slideUp(200).prev().removeClass('bw-solidRight-ico').addClass('bw-solidDown-ico');
        searchFN();
    });*/
    //隐藏所有
    $('.hidden-all').click(function() {
        $('.child-table-tr').hide();
    });
    //搜索
    $('#search').click(function() {
        searchFN();
    });
    //取消重排
    table.on('click', '.cancel', function() {
        var id = this.dataset.id;
        layer.confirm('你确定要取消重排吗？', {
            icon: 3,
            title: '提示'
        }, function(index) {
            $bw.ajax({
                url: 'api/PickupOrders/CancelPickupOrder?poId=' + id,
                type: 'get',
                callback: function() {
                    $bw.reloadForm();
                }
            });
        });
    });
    //确认发送
    table.on('click', '.send-out', function() {
        var id = this.dataset.id;
        layer.confirm('你确定要发送吗？', {
            icon: 3,
            title: '提示'
        }, function(index) {
            $bw.ajax({
                url: 'api/PickupOrders/SendPickupOrder?poId=' + id,
                type: 'get',
                callback: function() {
                    $bw.reloadForm();
                }
            });
        });
    });
}

function display() {
    var options = $bw.getHtmlOption();
    $('#search-driver').val(options.driverValue);
    $('#search-license').val(options.licenseValue);
    $('#start-date-input').val(options.startDate);
    var ajaxData = {
        PageIndex: 1,
        ajaxType: "post",
        url: "api/PickupOrders/GetPickupOrderByDriver"
    };
    $bw.showTableList({
        table: $('#driver-order-table'),
        data: ajaxData,
        init: true
    });

    //显示表头搜索条件
    $transfer.getDictItemList('TripType', function(res) {
        $bw.showTpl($('#tpl-header-select'), $('#trip-type'), res);
        $('#trip-type').val(options.tripType);
    });
    $transfer.getDictItemList('OrderState', function(res) {
        $bw.showTpl($('#tpl-header-select'), $('#driver-status'), res);
        $('#driver-status').val(options.OrderState);
    });

}
/**
 * 搜索和筛选
 * @author duantingting@bestwise.cc 2017-01-16
 * @return {[type]} [description]
 */
function searchFN() {
    var driverValue = $('#search-driver').val();
    var licenseValue = $('#search-license').val();
    var startDate = $('#start-date-input').val();
    var tripType = $('#trip-type').val();
    var OrderState = $('#driver-status').val();

    var Filter = [];
    if (driverValue) {
        Filter.push({
            GroupName: "sample1",
            FieldName: "DriverName",
            FieldValue: driverValue,
            SqlOperator: 0
        });
    }
    if (licenseValue) {
        Filter.push({
            GroupName: "sample2",
            FieldName: "License",
            FieldValue: licenseValue,
            SqlOperator: 0
        });
    }
    if (startDate) {
        Filter.push({
            GroupName: "sample3",
            FieldName: "StartDate",
            FieldValue: startDate,
            SqlOperator: 4
        });
    }
    if (tripType) {
        Filter.push({
            GroupName: "sample4",
            FieldName: "TripType",
            FieldValue: tripType,
            SqlOperator: 4
        });
    }
    if (OrderState) {
        Filter.push({
            GroupName: "sample5",
            FieldName: "OrderState",
            FieldValue: OrderState,
            SqlOperator: 4
        });
    }
    var ajaxData = {
        PageIndex: 1,
        Filter: Filter,
        driverValue: driverValue,
        licenseValue: licenseValue,
        startDate: startDate,
        tripType: tripType,
        OrderState: OrderState
    };
    $bw.showTableList({
        table: $('#driver-order-table'),
        data: ajaxData
    });
}

function getUserList(id, callback) {
    var returnData = getSession('GPUByPoId' + id);
    if (returnData) {
        callback(returnData);
        return;
    } else {
        $bw.ajax({
            url: 'api/PickupOrderList/GetPickupUserByPoId',
            type: 'post',
            data: {
                POID: id,
                PageIndex: 1,
                PageSize: 1000
            },
            callback: function(res) {
                addSession({
                    name: 'GPUByPoId' + id,
                    info: res.Data
                });
                callback(res.Data);
            }
        });
    }
}
