function htmlLoad() {
    //获取所有区域 用于头部筛选
    getRes(function() {
        $bw.showTpl($('#tpl-area'), $('#area'), {});
        var options = $bw.getHtmlOption();
        $('#searchInput').val(options.keyword);
        $('#area').val(options.areaId);
    })


    var table = $('#driver-info-table');
    var actionBox = $('#action-box');
    var editForm = $('#edit-form');
    //add
    actionBox.on('click', '.add', function() {
        add();
    });
    //delete
    table.on('click', '.delete', function() {
        var id = $(this).attr('data-id');
        deleteFn(id);
    });
    //edit-pwd
    table.on('click', '.edit-pwd', function() {
        var id = $(this).attr('data-id');
        editPwd(id);
    });
    //edit
    table.on('click', '.edit', function() {
        var key = $(this).attr('data-key');
        edit(key);
    });
    //新增时先获取code
    $(document).on('click', '#addSubmit', function() {
        //获取form数据
        var data = $bw.getFormInfo(editForm);
        if (data === false) return false;
        getCode(function(res) {
            data.Code = res.Data;
            $bw.goSubmit(data, editForm);
        });
        return false;
    });
    //选择生日
    $(document).on('click', '#selectDate', function() {
        laydate({
            elem: '#birthday'
        });
    });
    //搜索
    $('.bw-search').click(function() {
        searchFN();
    });
    //选择区域筛选
    actionBox.on('change', '#area', function() {
        searchFN();
    });
    //新增选择区域
    editForm.on('click', '.dict-city-btn', function() {
        $('.dict-city-btn.action').removeClass('action');
        $(this).addClass('action');
    })
}

function display() {
    var ajaxData = {
        PageIndex: 1,
        ajaxType: "post",
        url: "api/Drivers/GetListByPage"
    };
    $bw.showTableList({
        table: $('#driver-info-table'),
        data: ajaxData,
        init: true
    });
}
/**
 * 搜索和筛选
 */
function searchFN() {
    var value = $('#searchInput').val();
    var areaId = $('#area').val();
    var Filter = [];
    if (value) {
        Filter.push({ GroupName: "sample1", FieldName: "Code", FieldValue: value, SqlOperator: 0 });
        Filter.push({ GroupName: "sample1", FieldName: "IDCard", FieldValue: value, SqlOperator: 0 });
        Filter.push({ GroupName: "sample1", FieldName: "Phone", FieldValue: value, SqlOperator: 0 });
    }
    if (areaId) {
        Filter.push({ GroupName: "sample2", FieldName: "AreaId", FieldValue: areaId, SqlOperator: 4 });
    }
    var ajaxData = {
        PageIndex: 1,
        Filter: Filter,
        keyword: value,
        areaId: areaId,
        ajaxType: "POST",
        url: "api/Drivers/GetListByPage"
    };
    $bw.showTableList({
        table: $('#driver-info-table'),
        data: ajaxData
    });
}
/**
 * 新增方法
 */
function add() {
    var show = function() {
        $bw.showTpl($('#tpl-add-layer'), $('#edit-form'), {});
        $('#edit-form').attr('action', 'api/Drivers/Add');
        $bw.showPopup({
            type: 2,
            dom: $('#edit-layer'),
            title: "新增司机信息",
            width: "680px"
        });
    }
    getRes(show);
}
/**
 * 删除
 * @author duantingting@bestwise.cc 2017-01-11
 * @param  {[type]} id [description]
 * @return {[type]}    [description]
 */
function deleteFn(id) {
    layer.confirm($bw.lang.deteleTips, {
        icon: 3,
        title: '提示'
    }, function(index) {
        layer.close(index);
        $bw.ajax({
            url: "api/Drivers/Delete?did=" + id,
            type: 'post',
            data: {
                did: id
            },
            callback: function(res) {
                $bw.reloadForm(res);
            }
        });
    });
}
/**
 * 重置密码
 * @author duantingting@bestwise.cc 2017-01-11
 * @param  {[type]} id [description]
 * @return {[type]}    [description]
 */
function editPwd(id) {
    layer.confirm($bw.lang.editPwdTips, {
        icon: 3,
        title: '提示'
    }, function(index) {
        layer.close(index);
        $bw.ajax({
            url: "api/Drivers/ResetPwd?did=" + id,
            type: 'post',
            data: { did: id },
            callback: function(res) {
                $bw.reloadForm(res);
            }
        });
    });
}
/**
 * 编辑
 * @author duantingting@bestwise.cc 2017-01-11
 * @param  {[type]} key [description]
 * @return {[type]}     [description]
 */
function edit(key) {
    var data = $bw.listData[key];
    getRes(function() {
        $bw.showTpl($('#tpl-edit-layer'), $('#edit-form'), data);
        $('#edit-form').attr('action', 'api/Drivers/Update');
        $bw.showPopup({
            type: 2,
            dom: $('#edit-layer'),
            title: "编辑司机信息",
            width: "680px"
        });
    });
}
/**
 * 获取必要资源
 */
function getRes(callback) {
    var n = 0,
        len = 2;
    //获取区域
    $transfer.getDictCity('', function(res) {
        $bw.htmlRes.dictCity = res;
        n++;
        if (n >= len) callback();
    });
    //获取性别
    $transfer.getDictItemList('Gender', function(res) {
        $bw.htmlRes.gender = res;
        n++;
        if (n >= len) callback();
    });
}
/**
 * 获取司机工号
 */
function getCode(callback) {
    $bw.ajax({
        url: 'api/Drivers/GetCode',
        type: 'get',
        callback: function(res) {
            callback(res);
        }
    });
}
