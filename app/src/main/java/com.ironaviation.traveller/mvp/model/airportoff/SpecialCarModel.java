package com.ironaviation.traveller.mvp.model.airportoff;

import android.app.Application;

import com.google.gson.Gson;
import com.ironaviation.traveller.mvp.contract.airportoff.SpecialCarContract;
import com.ironaviation.traveller.mvp.model.api.cache.CacheManager;
import com.ironaviation.traveller.mvp.model.api.service.CommonService;
import com.ironaviation.traveller.mvp.model.api.service.ServiceManager;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.request.AirportGoInfoRequest;
import com.ironaviation.traveller.mvp.model.entity.request.PreViewRequest;
import com.ironaviation.traveller.mvp.model.entity.response.CarTypeResponse;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BaseModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

import static com.jess.arms.utils.Preconditions.checkNotNull;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Dennis on 2017/6/12.
 */

@ActivityScope
public class SpecialCarModel extends BaseModel<ServiceManager, CacheManager> implements SpecialCarContract.Model {
    private Gson mGson;
    private Application mApplication;
    private CommonService mCommonService;

    @Inject
    public SpecialCarModel(ServiceManager serviceManager, CacheManager cacheManager, Gson gson, Application application) {
        super(serviceManager, cacheManager);
        this.mGson = gson;
        this.mApplication = application;
        this.mCommonService = serviceManager.getCommonService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public Observable<BaseData<AirportGoInfoRequest>> getPreViewInfo(PreViewRequest params) {
        return mCommonService.getZPreViewDetails(params);
    }

    @Override
    public Observable<BaseData<List<CarTypeResponse>>> getCarTypes() {
        return mCommonService.getCarTypes();
    }
}