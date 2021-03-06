package com.ironaviation.traveller.common;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.LBSTraceClient;

import com.baidu.trace.Trace;
import com.baidu.trace.model.BaseRequest;
import com.baidu.trace.model.LocationMode;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.ui.login.LoginActivity;
import com.jess.arms.base.BaseApplication;
import com.jess.arms.di.module.GlobeConfigModule;
import com.jess.arms.http.GlobeHttpHandler;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.UiUtils;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.simple.eventbus.EventBus;

import com.ironaviation.traveller.BuildConfig;
import com.ironaviation.traveller.di.module.CacheModule;
import com.ironaviation.traveller.di.module.ServiceModule;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.atomic.AtomicInteger;

import me.jessyan.rxerrorhandler.handler.listener.ResponseErroListener;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by jess on 8/5/16 11:07
 * contact with jess.yan.effort@gmail.com
 */
public class WEApplication extends BaseApplication {
    private AppComponent mAppComponent;
    private RefWatcher mRefWatcher;//leakCanary观察器
    private LBSTraceClient client = null;
    private Trace trace = null;
    private long serverId = 0 ;

    @Override
    public void onCreate() {
        super.onCreate();
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(getAppModule())//baseApplication提供
                .clientModule(getClientModule())//baseApplication提供
                .imageModule(getImageModule())//baseApplication提供
                .globeConfigModule(getGlobeConfigModule())//全局配置
                .serviceModule(new ServiceModule())//需自行创建
                .cacheModule(new CacheModule())//需自行创建
                .build();

        if (BuildConfig.LOG_DEBUG) {//Timber日志打印
            Timber.plant(new Timber.DebugTree());
        }

        //微信支付
        IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
//        // 将该app注册到微信
        msgApi.registerApp("wxb277d9d5c3cf0829");
        //  installLeakCanary();//leakCanary内存泄露检查
        Bugly.init(getApplicationContext(), "6525888fc6", false);
//        CrashReport.initCrashReport(getApplicationContext(), "6525888fc6", BuildConfig.BUGLY_LOG);
        initMap();
    }

    public void initMap() {
        client = new LBSTraceClient(this);
        client.setLocationMode(LocationMode.High_Accuracy);
        //client.setLocationMode(LocationMode.High_Accuracy);
        /*trace = new Trace(this, Constant.SERVICEID, entityName, traceType);*/
    }

    public LBSTraceClient getClient() {
        return client;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAppComponent != null)
            this.mAppComponent = null;
        if (mRefWatcher != null)
            this.mRefWatcher = null;
    }

    /**
     * 安装leakCanary检测内存泄露
     */
    protected void installLeakCanary() {
        this.mRefWatcher = BuildConfig.USE_CANARY ? LeakCanary.install(this) : RefWatcher.DISABLED;
    }

    /**
     * 获得leakCanary观察器
     *
     * @param context
     * @return
     */
    public static RefWatcher getRefWatcher(Context context) {
        WEApplication application = (WEApplication) context.getApplicationContext();
        return application.mRefWatcher;
    }


    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    public AppComponent getAppComponent() {
        return mAppComponent;
    }


    /**
     * app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * GlobeHttpHandler是在NetworkInterceptor中拦截数据
     * 如果想将请求参数加密,则必须在Interceptor中对参数进行处理,GlobeConfigModule.addInterceptor可以添加Interceptor
     *
     * @return
     */
    @Override
    protected GlobeConfigModule getGlobeConfigModule() {
        return GlobeConfigModule
                .buidler()
                .baseurl(getString(R.string.APP_DOMAIN))
//                .baseurl(Api.APP_DOMAIN)
                .globeHttpHandler(new GlobeHttpHandler() {// 这里可以提供一个全局处理http响应结果的处理类,
                    // 这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                    @Override
                    public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                        //这里可以先客户端一步拿到每一次http请求的结果,可以解析成json,做一些操作,如检测到token过期后
                        //重新请求token,并重新执行请求
                        /*try {
                            if (!TextUtils.isEmpty(httpResult)) {
                                //JSONArray array = new JSONArray(httpResult);
                                //JSONObject object = (JSONObject) array.get(0);
                                //String login = object.getString("login");
                                //String avatar_url = object.getString("avatar_url");
                                // Timber.tag(TAG).w("result ------>" + login + "    ||   avatar_url------>" + avatar_url);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            return response;
                        }*/


                        //这里如果发现token过期,可以先请求最新的token,然后在拿新的token放入request里去重新请求
                        //注意在这个回调之前已经调用过proceed,所以这里必须自己去建立网络请求,如使用okhttp使用新的request去请求
                        // create a new request and modify it accordingly using the new token
//                    Request newRequest = chain.request().newBuilder().header("token", newToken)
//                            .build();

//                    // retry the request
//                    response.body().close();
                        //如果使用okhttp将新的请求,请求成功后,将返回的response  return出去即可

                        //如果不需要返回新的结果,则直接把response参数返回出去
                        isLogin(httpResult);
                        return response;
                    }

                    // 这里可以在请求服务器之前可以拿到request,做一些操作比如给request统一添加token或者header
                    @Override
                    public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                        //如果需要再请求服务器之前做一些操作,则重新返回一个做过操作的的requeat如增加header,不做操作则返回request

                        //return chain.request().newBuilder().header("token", tokenId)
//                .build();
                        Request.Builder builder = chain.request().newBuilder();

                        LoginEntity loginEntity = DataHelper.getDeviceData(getApplicationContext(), Constant.LOGIN);
                        if (loginEntity != null &&
                                !TextUtils.isEmpty(loginEntity.getTokenType()) &&
                                !TextUtils.isEmpty(loginEntity.getAccessToken())) {
                            builder.addHeader("Authorization", loginEntity.getTokenType() + " " + loginEntity.getAccessToken());
                        } else {

                        }
                        builder.addHeader("ClientType", "App");
                        builder.addHeader("appType", "Passenger");
                        builder.addHeader("systemType", "Android");
                        builder.addHeader(Constant.API_VERSION,"1.0");
                        return builder.build();

//                        return request;
                    }
                })
                .responseErroListener(new ResponseErroListener() {
                    //     用来提供处理所有错误的监听
                    //     rxjava必要要使用ErrorHandleSubscriber(默认实现Subscriber的onError方法),此监听才生效
                    @Override
                    public void handleResponseError(Context context, Exception e) {
                        Timber.tag(TAG).w("------------>" + e.getMessage());
                        if(e.toString().contains(Constant.SOCKET_TIME_OUT)){
                            UiUtils.SnackbarText(getString(R.string.travel_time_out));
                        }else if(e.toString().contains(Constant.UNKNOWN_HOST)){
                            UiUtils.SnackbarText(getString(R.string.network_error));
                        }else if(e.toString().contains(Constant.GSON)){
                            UiUtils.SnackbarText(getString(R.string.travel_gson));
                        }else{
                            UiUtils.SnackbarText(getString(R.string.network_error));
                        }

                        MobclickAgent.reportError(WEApplication.getContext(), e);

                    }
                }).build();
    }

    /**
     * 初始化请求公共参数
     *
     * @param request
     */
    public void initRequest(BaseRequest request) {
        request.setTag(getTag());
        request.setServiceId(getServerId());
    }

    public long getServerId(){
        return Long.parseLong(getString(R.string.SERVER_ID));
    }

    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    /**
     * 获取请求标识
     *
     * @return
     */
    public int getTag() {
        return mSequenceGenerator.incrementAndGet();
    }

    public void isLogin(String httpResult) {
        try {
            if (TextUtils.isEmpty(httpResult))
                return;
            JSONObject object = new JSONObject(httpResult);
            int code = object.getInt("Status");
            if (code == Api.OTHER_LOGIN) { //其他设备上登录
                DataHelper.removeSF(WEApplication.this, Constant.LOGIN);
                Intent intent = new Intent(WEApplication.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                UiUtils.SnackbarText(getString(R.string.login_other));
                EventBus.getDefault().post(true, EventBusTags.LOGIN_OTHER);
                WEApplication.this.startActivity(intent);
            }

        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(WEApplication.getContext(), e);

        }
    }


}
