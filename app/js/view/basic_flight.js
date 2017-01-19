function htmlLoad() {
    //设置筛选条件和默认条件
    var timeSet = getTimeSet();
    $bw.showTpl($('#tpl-time'), $('#min-data-select'), timeSet);
    $bw.showTpl($('#tpl-time'), $('#max-data-select'), timeSet);
    var options = $bw.getHtmlOption();
    $('#min-data-select').val(options.Starttime);
    $('#max-data-select').val(options.Endtime);

    var table = $('#flight-info-table');
    var actionBox = $('#action-box');
    //add
    actionBox.on('click', '.add', function() {
        $bw.showTpl($('#tpl-form-layer'), $('#edit-form'), {});
        $('.time-select').each(function(i, e) {
            $bw.showTpl($('#tpl-time'), $(e), timeSet);
        });
        $('#edit-form').attr('action', 'api/ShiftTime/Add');
        $bw.showPopup({
            type: 2,
            dom: $('#edit-layer'),
            title: "新增航班时段",
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
                url: "api/ShiftTime/Delete?diid=" + id,
                type: 'post',
                data: { diid: id },
                callback: function(res) {
                    $bw.reloadForm(res);
                }
            });
        });
    });
    //edit
    table.on('click', '.edit', function() {
        var id = $(this).attr('data-id'),
            key = $(this).attr('data-key'),
            data = $bw.listData[key];
        $bw.showTpl($('#tpl-form-layer'), $('#edit-form'), data);
        $('.time-select').each(function(i, e) {
            $bw.showTpl($('#tpl-time'), $(e), timeSet);
            $(e).val(data[$(e).attr('name')]);
        });

        $('#edit-form').attr('action', 'api/ShiftTime/Update');
        $bw.showPopup({
            type: 2,
            dom: $('#edit-layer'),
            title: "编辑接送地点",
            width: "680px"
        });
    });
    //选择时间筛选
    $('#time-select').click(function() {
        var min = $('#min-data-select').val();
        var max = $('#max-data-select').val();
        if (min >= max) {
            layer.msg('请选择正确的区间');
            return false;
        }
        var ajaxData = {
            PageIndex: 1,
            PageSize: $bw.pages,
            ajaxType: "POST",
            Starttime: min,
            Endtime: max,
            url: "api/ShiftTime/GetListByPage"
        };
        $bw.showTableList({
            table: $('#flight-info-table'),
            data: ajaxData
        });
    });
    /* $(document).on('focus','.time-select',function(){
         $(this).attr('size','5').attr('style','height:inherit');
     })
     $(document).on('blur','.time-select',function(){
         $(this).attr('size','1').attr('sytle','');
     })*/
}

function display() {
    var ajaxData = {
        PageIndex: 1,
        PageSize: $bw.pages,
        ajaxType: "POST",
        url: "api/ShiftTime/GetListByPage"
    };
    $bw.showTableList({
        table: $('#flight-info-table'),
        data: ajaxData,
        init: true
    });
}

function getTimeSet() {
    var timeSet = {};
    for (var i = 0; i < 24; i++) {
        var time = '';
        if (i < 10) {
            time = '0' + i + ':00';
        } else {
            time = i + ':00';
        }
        timeSet[time] = time;
    }
    return timeSet;
}
