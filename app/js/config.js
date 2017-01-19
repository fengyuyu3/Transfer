if (!$bw) var $bw = {};
$bw.serial = 0; //用于分页序号
$bw.pages = 20; //默认每页的数量
$bw.params = {}; //主页参数
$bw.listData = {}; //主页数据 用于编辑时的数据获取
$bw.lesserParams = {}; //子分页参数
$bw.lesserData = {}; //子分页数据
$bw.userInfo = null; //用户数据
$bw.maxImgSize = 2; //图片最大 单位M
$bw.htmlRes={};//存放全局变量
$bw.httpHeader= 'http://192.168.0.212:8050/';
//$bw.httpHeader = 'http://192.168.0.89:8050/';
//$bw.httpHeader = 'http://192.168.0.20:8050/';

$transfer = {
    /**
     * 获取字典表配置
     * @author duantingting@bestwise.cc 2017-01-18
     * @param  {[type]}   type     [description]
     * @param  {Function} callback [description]
     * @return {[type]}            [description]
     */
    getDictItemList: function(type, callback) {
    	var returnData=getSession('dictItemList' + type);
        if (returnData) {
            callback(returnData);
        } else {
            $bw.ajax({
                url: "api/BaseDictionary/GetDictItemListByCatgCode?catgCode=" + type,
                type: "get",
                callback: function(res) {
                    addSession({ name: 'dictItemList' + type, info: res.Data });
                    callback(res.Data);
                }
            })
        }
    },
    /**
     * 获取区域配置
     * @author duantingting@bestwise.cc 2017-01-10
     * @param  {Function} callback [description]
     * @return {[type]}            [description]
     */
    getDictCity:function(id,callback){
    	if(!id) id='510100';
    	var returnData=getSession('dictCity'+id);
        if (returnData) {
            callback(returnData);
        } else {
            $bw.ajax({
                url: "api/BookingList/GetDictCityList?cityId="+id,
                type: "get",
                callback: function(res) {
                    addSession({ name: 'dictCity'+id, info: res.Data });
                    callback(res.Data);
                }
            })
        }
    },
    /**
     * 获取区域名称
     * @author duantingting@bestwise.cc 2017-01-13
     * @param  {[type]} id [description]
     * @return {[type]}    [description]
     */
    getDictCiryName:function (id){
        var dictCity=$bw.htmlRes.dictCity;
        for(var i in dictCity){
            if(dictCity[i].CityId==id){
                return dictCity[i].Name;
            }
        }
        return '';
    },
    /**
     * 获取所有航班时段
     * @author duantingting@bestwise.cc 2017-01-13
     * @param  {Function} callback [description]
     * @return {[type]}            [description]
     */
    getAllFlightTime:function(callback){
        $bw.ajax({
            url:'api/ShiftTime/GetList',
            type:'post',
            data:{},
            callback:callback
        });
    }

}
