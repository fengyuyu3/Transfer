package com.ironaviation.traveller.mvp.model;

import android.app.Application;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.ironaviation.traveller.app.utils.CommonUtil;
import com.ironaviation.traveller.mvp.contract.MainContract;
import com.ironaviation.traveller.mvp.model.api.cache.CacheManager;
import com.ironaviation.traveller.mvp.model.api.service.CommonService;
import com.ironaviation.traveller.mvp.model.api.service.ServiceManager;
import com.ironaviation.traveller.mvp.model.entity.AppVersionEntity;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.Login;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BaseModel;

import org.json.JSONObject;

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
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-23 9:38
 * 修改人：starRing
 * 修改时间：2017-03-23 9:38
 * 修改备注：
 */
@ActivityScope
public class MainModel extends BaseModel<ServiceManager, CacheManager> implements MainContract.Model {
    private Gson mGson;
    private Application mApplication;
    private CommonService mCommonService;

    @Inject
    public MainModel(ServiceManager serviceManager, CacheManager cacheManager, Gson gson, Application application) {
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

    @Override
    public Observable<BaseData<LoginEntity>> login(String ID, String password) {
        Login login = new Login();

       /* login.setUserName(ID);//禁用司机

        login.setPassword(CommonUtil.MD5Code(password.toUpperCase()).toString());//转大写后加密

        if (!TextUtils.isEmpty("6a82c94cf27bbfcae827297ade87ce88")) {
            login.setClientID("6a82c94cf27bbfcae827297ade87ce88");

        } else {
            // login.put("ClientID", PushManager.getInstance().getClientid(getApplicationContext()));
        }*/


        return mCommonService.login(login);
    }

    @Override
    public Observable<BaseData<AppVersionEntity>> getLatestVersion() {
        return mCommonService.getLatestVersion();
    }
}
