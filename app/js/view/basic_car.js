function htmlLoad() {
    getRes(function() {
        $bw.showTpl($('#tpl-car-status'), $('#car-status'), {});
        var options = $bw.getHtmlOption();
        $('#searchInput').val(options.keyword);
        $('#car-status').val(options.carStatus);
        $('#min-data-input').val(options.minDate);
        $('#max-data-input').val(options.maxDate);
    });
    var table = $('#car-info-table');
    var actionBox = $('#action-box');
    var editForm = $('#edit-form');
    //add
    actionBox.on('click', '.add', function() {
        $bw.showTpl($('#tpl-add-layer'), $('#edit-form'), {});
        $('#edit-form').attr('action', 'api/Cars/Add');
        $bw.showPopup({
            type: 2,
            dom: $('#edit-layer'),
            title: "新增车辆信息",
            width: "680px"
        });
    });
    //edit
    table.on('click', '.edit', function() {
        var key = $(this).attr('data-key');
        var id = this.dataset.id;
        var data = $bw.listData[key];
        $bw.showTpl($('#tpl-edit-layer'), $('#edit-form'), data);
        $('#edit-form').attr('action', 'api/Cars/Update?did=' + id);
        $bw.showPopup({
            type: 2,
            dom: $('#edit-layer'),
            title: "编辑车辆信息",
            width: "680px"
        });
    });
    //delete
    table.on('click', '.delete', function() {
        var id = $(this).attr('data-id');
        layer.confirm($bw.lang.deteleTips, {
            icon: 3,
            title: '提示'
        }, function(index) {
            layer.close(index);
            $bw.ajax({
                url: "api/Cars/Delete?cid=" + id,
                type: 'POST',
                data: {},
                callback: function(res) {
                    $bw.reloadForm(res);
                }
            });
        });
    });

    //选择日期
    actionBox.on('click', '#minDate', function() {
        laydate({
            elem: '#min-data-input'
        });
    });
    actionBox.on('click', '#maxDate', function() {
        laydate({
            elem: '#max-data-input'
        });
    });
    $(document).on('click', '#manufacture-date', function() {
        laydate({
            elem: '#manufacture'
        });
    });
    $(document).on('click', '#inspection-date', function() {
        laydate({
            elem: '#inspection'
        });
    });
    //搜索
    $('.bw-search').click(function() {
        searchFN();
    });
    //选择区域筛选
    actionBox.on('change', '#car-status', function() {
        searchFN();
    });
    //日期搜索
    $('#date-search').click(function() {
        searchFN();
    });
    //编辑新增选择车辆状态
    editForm.on('click', '.dict-city-btn', function() {
        $('.dict-city-btn.action').removeClass('action');
        $(this).addClass('action');
    })
    editForm.on('click', '#addSubmit', function() {
        var manufacture = $('#manufacture');
        var inspection = $('#inspection');
        manufacture.val(Date.parse(new Date(manufacture.val())));
        inspection.val(Date.parse(new Date(inspection.val())));
    })

}

function display() {
    var ajaxData = {
        PageIndex: 1,
        ajaxType: "post",
        PageSize: $bw.pages,
        url: "api/Cars/GetListByPage"
    };
    $bw.showTableList({
        table: $('#car-info-table'),
        data: ajaxData,
        init: true
    });
}
/**
 * 搜索和筛选
 */
function searchFN() {
    var value = $('#searchInput').val();
    var carStatus = $('#car-status').val();
    var minDate = $('#min-data-input').val();
    var maxDate = $('#max-data-input').val();
    var Filter = [];
    if (value) {
        Filter.push({ GroupName: "sample1", FieldName: "License", FieldValue: value, SqlOperator: 0 });
        Filter.push({ GroupName: "sample1", FieldName: "Code", FieldValue: value, SqlOperator: 0 });
    }
    if (carStatus) {
        Filter.push({ GroupName: "sample2", FieldName: "State", FieldValue: carStatus, SqlOperator: 4 });
    }
    if (minDate) {
        Filter.push({ GroupName: "sample3", FieldName: "CheckTime", FieldValue: Date.parse(new Date(minDate)), SqlOperator: 8 });
    }
    if (maxDate) {
        Filter.push({ GroupName: "sample4", FieldName: "CheckTime", FieldValue: Date.parse(new Date(maxDate)), SqlOperator: 9 });
    }
    var ajaxData = {
        PageIndex: 1,
        Filter: Filter,
        keyword: value,
        carStatus: carStatus,
        minDate: minDate,
        maxDate: maxDate,
        ajaxType: "POST",
        url: "api/Cars/GetListByPage"
    };
    $bw.showTableList({
        table: $('#car-info-table'),
        data: ajaxData
    });
}

function getRes(callback) {
    var n = 0,
        len = 1;
    //获取汽车状态
    $transfer.getDictItemList('CarsState', function(res) {
        $bw.htmlRes.carsState = res;
        n++;
        if (n >= len) callback();
    });
}
