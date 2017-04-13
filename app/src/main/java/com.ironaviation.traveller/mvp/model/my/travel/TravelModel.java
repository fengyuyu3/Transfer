package com.ironaviation.traveller.mvp.model.my.travel;

import android.app.Application;

import com.google.gson.Gson;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelContract;
import com.ironaviation.traveller.mvp.model.api.cache.CacheManager;
import com.ironaviation.traveller.mvp.model.api.service.CommonService;
import com.ironaviation.traveller.mvp.model.api.service.ServiceManager;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.request.RouteListMoreRequest;
import com.ironaviation.traveller.mvp.model.entity.request.TravelRequest;
import com.ironaviation.traveller.mvp.model.entity.response.RouteListResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelResponse;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 *
 * 项目名称：Transfer      
 * 类描述：   
 * 创建人：flq  
 * 创建时间：2017/3/26 17:28   
 * 修改人：  
 * 修改时间：2017/3/26 17:28   
 * 修改备注：   
 * @version
 *
 */

@ActivityScope
public class TravelModel extends BaseModel<ServiceManager, CacheManager> implements TravelContract.Model {
    private Gson mGson;
    private Application mApplication;
    private CommonService mCommonService;

    @Inject
    public TravelModel(ServiceManager serviceManager, CacheManager cacheManager, Gson gson, Application application) {
        super(serviceManager, cacheManager);
        this.mGson = gson;
        this.mApplication = application;
        mCommonService = serviceManager.getCommonService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

   /* @Override
    public Observable<BaseData<List<TravelResponse>>> getTravelData() {
        TravelRequest travelRequest = new TravelRequest();

        return mCommonService.getTravelData(travelRequest);
    }
*/
    @Override
    public Observable<BaseData<RouteListResponse>> getRouteListMore(int index) {
        RouteListMoreRequest params = new RouteListMoreRequest();
        params.setPageIndex(index);
        params.setPageSize(Constant.INDEX);
        params.setPaged(true);
        return mCommonService.getRouteInfoMore(params);
    }
    @Override
    public Observable<BaseData<RouteStateResponse>> getRouteStateInfo(String bid) {
        return mCommonService.getRouteStateInfo(bid);
    }
}
