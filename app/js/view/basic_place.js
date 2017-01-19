function htmlLoad() {
    getRes(function() {
        $bw.showTpl($('#tpl-area'), $('#area'), {});
        var options = $bw.getHtmlOption();
        $('#searchInput').val(options.keyword);
        $('#area').val(options.areaId);
    });
    var table = $('#place-info-table');
    var actionBox = $('#action-box');
    //add
    actionBox.on('click', '.add', function() {
        $bw.showTpl($('#tpl-form-layer'), $('#edit-form'), {});
        $('#edit-form').attr('action', 'api/BaseDictionary/AddForDictItems');
        $bw.showPopup({
            type: 2,
            dom: $('#edit-layer'),
            title: "新增接送地点",
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
                url: "api/BaseDictionary/DeleteForDictItems?diid=" + id,
                type: 'get',
                data: {},
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
        $('#edit-select-area').val(data.ExtAttr1);
        $('#edit-form').attr('action', 'api/BaseDictionary/UpdateForDictItems');
        $bw.showPopup({
            type: 2,
            dom: $('#edit-layer'),
            title: "编辑接送地点",
            width: "680px"
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
}
/**
 * 搜索和筛选
 */
function searchFN() {
    var value = $('#searchInput').val();
    var areaId = $('#area').val();
    var Filter = [{ GroupName: "sample1", FieldName: "CatgCode", FieldValue: 'AwaitAddress', SqlOperator: 4 }];
    if (value) {
        Filter.push({ GroupName: "sample2", FieldName: "ItemValue", FieldValue: value, SqlOperator: 0 });
    }
    if (areaId) {
        Filter.push({ GroupName: "sample3", FieldName: "ExtAttr1", FieldValue: areaId, SqlOperator: 4 });
    }
    var ajaxData = {
        PageIndex: 1,
        Filter: Filter,
        keyword: value,
        areaId: areaId
    };
    $bw.showTableList({
        table: $('#place-info-table'),
        data: ajaxData
    });
}

function display() {
    var Filter = [
        { GroupName: "sample1", FieldName: "CatgCode", FieldValue: 'AwaitAddress', SqlOperator: 4 }
    ];
    var ajaxData = {
        PageIndex: 1,
        Filter: Filter,
        PageSize: $bw.pages,
        ajaxType: "post",
        url: "api/BaseDictionary/GetDictItemPageList"
    };
    $bw.showTableList({
        table: $('#place-info-table'),
        data: ajaxData,
        init: true
    });
}
/**
 * 获取必要资源
 * @author duantingting@bestwise.cc 2017-01-13
 * @param  {Function} callback [description]
 * @return {[type]}            [description]
 */
function getRes(callback) {
    var n = 0,
        len = 1;
    //获取区域
    $transfer.getDictCity('', function(res) {
        $bw.htmlRes.dictCity = res;
        n++;
        if (n >= len) callback();
    });

}
