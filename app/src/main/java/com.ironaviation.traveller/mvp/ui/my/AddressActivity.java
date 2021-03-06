package com.ironaviation.traveller.mvp.ui.my;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.app.utils.BaiDuLBS;
import com.ironaviation.traveller.app.utils.LocationService;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.common.WEApplication;
import com.ironaviation.traveller.di.component.my.DaggerAddressComponent;
import com.ironaviation.traveller.di.module.my.AddressModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.AddressContract;
import com.ironaviation.traveller.mvp.model.entity.HistoryPoiInfo;
import com.ironaviation.traveller.mvp.model.entity.request.UpdateAddressBookRequest;
import com.ironaviation.traveller.mvp.presenter.my.AddressPresenter;
import com.ironaviation.traveller.mvp.ui.manager.FullyLinearLayoutManager;
import com.ironaviation.traveller.mvp.ui.my.adapter.AddressAdapter;
import com.jess.arms.utils.UiUtils;
import com.umeng.analytics.MobclickAgent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
 * 创建时间：2017-03-31 11:41
 * 修改人：starRing
 * 修改时间：2017-03-31 11:41
 * 修改备注：
 */
public class AddressActivity extends WEActivity<AddressPresenter> implements AddressContract.View, OnGetPoiSearchResultListener, OnGetGeoCoderResultListener {


    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.rv_address)
    RecyclerView mRvAddress;
    @BindView(R.id.rl_usual_address)
    AutoRelativeLayout mRlUsualAddress;
    @BindView(R.id.ll_address)
    AutoLinearLayout mLlAddress;
//    @BindView(R.id.tw_address_text)
    TextView mTwAddressText;
    @BindView(R.id.ll_home_address)
    AutoRelativeLayout mLlHomeAddress;
    @BindView(R.id.ll_company_address)
    AutoRelativeLayout mLlCompanyAddress;
    @BindView(R.id.tv_item_detail_address)
    TextView mTvItemDetailAddress;
    @BindView(R.id.tv_company_detail_address)
    TextView mTvCompanyDetailAddress;
    @BindView(R.id.ll_search)
    AutoLinearLayout mLlSearch;
    @BindView(R.id.tw_nodata)
    TextView mTwNodata;
    private RecyclerView.LayoutManager mLayoutManager;
    List<UpdateAddressBookRequest> mUpdateAddressBookRequests = new ArrayList<>();
    private PoiSearch mPoiSearch = null;
    private boolean searchFlag = true;
    private boolean searchListFlag = false;
    private PoiInfo result;
    private List<HistoryPoiInfo> infos;
    private int addressType;
    private AddressAdapter mAddressAdapter;
    private String uabId;
    private GeoCoder mSearch = null;
    private LocationService locationService;
    private HistoryPoiInfo info;
    private boolean locationFlag;
    private BaiDuLBS baiDuLBS;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAddressComponent
                .builder()
                .appComponent(appComponent)
                .addressModule(new AddressModule(this)) //请将AddressModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_address, null, false);
    }

    @Override
    protected void initData() {
        mTwAddressText = (TextView) findViewById(R.id.tw_address_text);
        mTwAddressText.setText(getResources().getString(R.string.address_locationing));
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mAddressAdapter = new AddressAdapter(R.layout.item_address);
        mLayoutManager = new FullyLinearLayoutManager(this);

        mRvAddress.setLayoutManager(mLayoutManager);

        mRvAddress.setAdapter(mAddressAdapter);
        infos = mPresenter.getAddress().getPoiInfos();
        mAddressAdapter.setNewData(infos);

        mPresenter.getUserAddressBook();
        Bundle pBundle = getIntent().getExtras();
        if (pBundle != null) {
            addressType = pBundle.getInt(Constant.ADDRESS_TYPE);
        }
        initLocation();
        baiDuLBS = new BaiDuLBS(this,mListener);


        if (AndPermission.hasPermission(this
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_COARSE_LOCATION
                ,Manifest.permission.WRITE_EXTERNAL_STORAGE
                ,Manifest.permission.READ_PHONE_STATE
                ,Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // 有权限，直接do anything.
//            initLocation();
            baiDuLBS.startLocation();
        } else {
            mTwAddressText.setText("没有获取到定位权限，请开启");
            // 申请权限。
            AndPermission.with(this)
                    .requestCode(101)
                    .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .send();
        }
        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        mEtAddress.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int arg1, int arg2,
                                      int arg3) {
                showSearch();
                if (charSequence.length() <= 0) {
                    //mRvAddress.setVisibility(View.GONE);
                    //infos = null;
                    infos = mPresenter.getAddress().getPoiInfos();
                    if(infos != null) {
                        showRecyClerView();
                        mAddressAdapter.setNewData(infos);
                    }else{
                        showNodata();
                    }
                    searchRunnable.clear();
                    return;
                }
                // mHandler.post(mBackgroundRunnable);//mBackgroundRunnable为线程对象
                getAllSearchTextFilter(charSequence.toString());
            }
        });
        mAddressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mPresenter.saveAddress(infos.get(position));
                mPresenter.isAddress(Constant.CHENGDU_CTU,
                        infos.get(position).location.longitude,
                        infos.get(position).location.latitude,
                        infos.get(position));
            }
        });
    }

    /*
        * 获取权限回调
        * */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 只需要调用这一句，其它的交给AndPermission吧，最后一个参数是PermissionListener。
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, mPermissionListener);
    }

    /*
      * 获取权限监听
      * */
    private PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            if (requestCode == 100) {
                // TODO 相应代码。
            } else if (requestCode == 101) {
                // TODO 相应代码。
                mTwAddressText.setText(getResources().getString(R.string.address_locationing));
//                initLocation();
                baiDuLBS.startLocation();

            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。

            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(AddressActivity.this, deniedPermissions)) {
                // 第一种：用默认的提示语。
                //  AndPermission.defaultSettingDialog(ma, 100).show();

                // 第二种：用自定义的提示语。
                AndPermission.defaultSettingDialog(AddressActivity.this, 100)
                        .setTitle("权限申请失败")
                        .setMessage("我们需要的定位被您拒绝或者系统发生错误申请失败，请您到设置页面手动授权，否则功能无法正常使用！")
                        .setPositiveButton("好，去设置")
                        .show();

                // 第三种：自定义dialog样式。
                // SettingService settingService =
                //    AndPermission.defineSettingDialog(this, REQUEST_CODE_SETTING);
                // 你的dialog点击了确定调用：
                // settingService.execute();
                // 你的dialog点击了取消调用：
                // settingService.cancel();
            }
        }
    };
    @Override
    public void showLoading() {
        showProgressDialog();

    }

    @Override
    public void hideLoading() {
        dismissProgressDialog();
    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    protected void nodataRefresh() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_cancel, R.id.ll_address, R.id.ll_home_address, R.id.ll_company_address, R.id.tv_item_detail_address, R.id.tv_company_detail_address})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.ll_address:
                if (AndPermission.hasPermission(this
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        ,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE)) {
                    // 有权限，直接do anything.
                    if(info != null && locationFlag) {
                        mPresenter.isAddress(Constant.CHENGDU_CTU, info.location.longitude,
                                info.location.latitude, info);
                    }else{
                        /*initLocation();*/
                        baiDuLBS.startLocation();
                    }
                } else {
                    mTwAddressText.setText("没有获取到定位权限，请开启权限");
                    // 申请权限。
                    AndPermission.with(this)
                            .requestCode(101)
                            .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                            .send();
                }
                /*if (addressType == Constant.AIRPORT_GO) {
                    if (info != null) {
                        EventBus.getDefault().post(info, EventBusTags.AIRPORT_GO);
                    } else {
                        showMessage("没有定位成功!");
                    }
                } else if (addressType == Constant.AIRPORT_ON) {
                    if (info != null) {
                        EventBus.getDefault().post(info, EventBusTags.AIRPORT_ON);
                    } else {
                        showMessage("没有定位成功!");
                    }
                }
                finish();*/
                break;
            case R.id.ll_home_address:
                for (int i = 0; i < mUpdateAddressBookRequests.size(); i++) {
                    switch (mUpdateAddressBookRequests.get(i).getAddressName()) {
                        case Constant.HOME:

                            if (mUpdateAddressBookRequests.get(i).getViewType() == 0) {

                                mPresenter.isAddress(Constant.CHENGDU_CTU,mUpdateAddressBookRequests.get(i).getLongitude()
                                ,mUpdateAddressBookRequests.get(i).getLatitude(),newHistoryPoiInfo(mUpdateAddressBookRequests.get(i)));
                                /*if (addressType == Constant.AIRPORT_GO) {
                                    EventBus.getDefault().post(newHistoryPoiInfo(mUpdateAddressBookRequests.get(i)), EventBusTags.AIRPORT_GO);
                                    finish();
                                } else if (addressType == Constant.AIRPORT_ON) {

                                    EventBus.getDefault().post(newHistoryPoiInfo(mUpdateAddressBookRequests.get(i)), EventBusTags.AIRPORT_ON);
                                    finish();
                                } else {

                                }*/

                            } else if (mUpdateAddressBookRequests.get(i).getViewType() == 1) {
                                Bundle pBundle = new Bundle();
                                pBundle.putInt(Constant.ADDRESS_TYPE, Constant.ADDRESS_USUAl_HOME);
                                startActivity(AddressSearchActivity.class, pBundle);
                            }
                            break;

                    }
                }

                break;
            case R.id.ll_company_address:


                for (int i = 0; i < mUpdateAddressBookRequests.size(); i++) {
                    switch (mUpdateAddressBookRequests.get(i).getAddressName()) {
                        case Constant.COMPANY:

                            if (mUpdateAddressBookRequests.get(i).getViewType() == 0) {
                                mPresenter.isAddress(Constant.CHENGDU_CTU,mUpdateAddressBookRequests.get(i).getLongitude()
                                        ,mUpdateAddressBookRequests.get(i).getLatitude(),newHistoryPoiInfo(mUpdateAddressBookRequests.get(i)));
                                /*if (addressType == Constant.AIRPORT_GO) {

                                    EventBus.getDefault().post(newHistoryPoiInfo(mUpdateAddressBookRequests.get(i)), EventBusTags.AIRPORT_GO);
                                    finish();
                                } else if (addressType == Constant.AIRPORT_ON) {

                                    EventBus.getDefault().post(newHistoryPoiInfo(mUpdateAddressBookRequests.get(i)), EventBusTags.AIRPORT_ON);
                                    finish();
                                } else {

                                }*/
                            } else if (mUpdateAddressBookRequests.get(i).getViewType() == 1) {
                                Bundle pBundle = new Bundle();
                                pBundle.putInt(Constant.ADDRESS_TYPE, Constant.ADDRESS_USUAl_COMPANY);
                                startActivity(AddressSearchActivity.class, pBundle);
                            }
                            break;


                    }
                }
                break;
        }
    }

    private HistoryPoiInfo newHistoryPoiInfo(UpdateAddressBookRequest mUpdateAddressBookRequest) {
        HistoryPoiInfo historyPoiInfo = new HistoryPoiInfo(mUpdateAddressBookRequest.getAddress(),
                mUpdateAddressBookRequest.getDetailAddress(),
                new LatLng(mUpdateAddressBookRequest.getLatitude(), mUpdateAddressBookRequest.getLongitude()));
        return historyPoiInfo;
    }

    private void getAllSearchTextFilter(String keyword) {
        //清除掉之前没有完成的搜索请求
        dismissProgressDialog();
        if (keyword == null) {
            return;
        }
        searchRunnable.pushKeyWord(keyword);
    }

    SearchRunnable searchRunnable = new SearchRunnable();


    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     *
     * @param result
     */
    @Override
    public void onGetPoiResult(PoiResult result) {

//        dismissProgressDialog();
        if (result == null || result.getAllPoi() == null) {
//            mRvAddress.setVisibility(View.GONE);
            showNodata();
            return;
        }
        infos = new ArrayList<>();

        for (PoiInfo info : result.getAllPoi()) {
            if (info.location != null && info.city.equals(getString(R.string.address_city))) {
                infos.add(new HistoryPoiInfo(info, false));
            }
        }
        mAddressAdapter.setNewData(infos);

        if (infos.size() != 0) {
            showRecyClerView();
//            mRvAddress.setVisibility(View.VISIBLE);
            //rl_passenger_phone.setVisibility(View.INVISIBLE);
            // rl_passenger_standby_phone.setVisibility(View.INVISIBLE);
            // rl_reservation.setVisibility(View.INVISIBLE);
        } else {
            showNodata();
//            mRvAddress.setVisibility(View.GONE);
            // rl_passenger_phone.setVisibility(View.VISIBLE);
            //rl_passenger_standby_phone.setVisibility(View.VISIBLE);
            // rl_reservation.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     *
     * @param result
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult result) {

    }

    @Override
    public void setView(List<UpdateAddressBookRequest> mUpdateAddressBookRequests) {

        this.mUpdateAddressBookRequests = mUpdateAddressBookRequests;
        for (int i = 0; i < mUpdateAddressBookRequests.size(); i++) {
            switch (mUpdateAddressBookRequests.get(i).getAddressName()) {
                case "家":

                    mTvItemDetailAddress.setText(mUpdateAddressBookRequests.get(i).getAddress());
                    break;
                case "公司":
                    mTvCompanyDetailAddress.setText(mUpdateAddressBookRequests.get(i).getAddress());
                    break;

            }
        }

    }

    @Override
    public void isAddressSuccess(boolean flag,HistoryPoiInfo info) {
        if(flag){
            if (addressType == Constant.AIRPORT_GO) {
                EventBus.getDefault().post(info, EventBusTags.AIRPORT_GO);
                finish();
            } else if (addressType == Constant.AIRPORT_ON) {
                EventBus.getDefault().post(info, EventBusTags.AIRPORT_ON);
                finish();
            } else if(addressType == Constant.AIRPORT_Z_GO){
                EventBus.getDefault().post(info, EventBusTags.AIRPORT_Z_GO);
                finish();
            }else if(addressType == Constant.AIRPORT_Z_ON){
                EventBus.getDefault().post(info, EventBusTags.AIRPORT_Z_ON);
                finish();
            }
        }
    }


    class SearchRunnable implements Runnable {
        String keyWord;
        Handler handler = new Handler();

        public void pushKeyWord(String keyWord) {
            this.keyWord = keyWord;
            handler.removeCallbacks(searchRunnable);
            if (keyWord.equals("")) {
                handler.removeCallbacks(searchRunnable);

            } else {

                handler.postDelayed(this, 650);
            }


        }

        public void clear() {

            handler.removeCallbacks(searchRunnable);
        }

        @Override
        public void run() {
            //此处发起Http请求

            if (searchFlag) {
                result = null;
                //showProgressDialog();
               /* mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(keyWord).city("成都"));*/
                searchListFlag = false;
                mPoiSearch.searchInCity((new PoiCitySearchOption())
                        .city("成都").keyword(keyWord));
            }


        }
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        super.onDestroy();
    }


    public void initMap() {
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    /***
     * 初始化定位sdk
     */
    private void initLocation() {
        initMap();
        /*if (locationService == null) {
            locationService = new LocationService(getApplicationContext());
            locationService.registerListener(mListener);
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            locationService.start();
        }*/
    }

    private BDLocationListener mListener = new BDLocationListener() {
        double longitude = 0;
        double latitude = 0;

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                /*switch (location.getLocType()) {
                    case BDLocation.TypeServerError:
                    case BDLocation.TypeNetWorkException:
                    case BDLocation.TypeCriteriaException:
                        locationFlag = false;
                        showMessage("定位失败！请检查网络或者定位权限是否开启");
                        break;
                    default:*/
                locationFlag = true;
                LatLng ptCenter = new LatLng(location.getLatitude() //latitude weidu
                        , location.getLongitude()); //long jingdu
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ptCenter));
//                        break;
            }else{
                locationFlag = false;
                baiDuLBS.startLocation();
            }
            /*if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                LatLng ptCenter = new LatLng(location.getLatitude() //latitude weidu
                        , location.getLongitude()); //long jingdu
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ptCenter));
            }else if(null != location && location.getLocType() != BDLocation.TypeCriteriaException){

            }*/
        }

    };

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
            if(geoCodeResult.error != null){}
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            return;
        }
        /*if(!addressFlag){
            addressFlag = true;
            mPwAddress.setTextInfo(result.getAddress());
        }*/
        /*HistoryPoiInfo info = new HistoryPoiInfo(result.getPoiList().get(0), false);
        if (result.getAddress() != null) {
            if (addressType == Constant.AIRPORT_GO) {
                EventBus.getDefault().post(info, EventBusTags.AIRPORT_GO);
            } else if (addressType == Constant.AIRPORT_ON) {
                EventBus.getDefault().post(info, EventBusTags.AIRPORT_ON);
            }
        } else {
            dismissProgressDialog();
            showMessage("定位失败,请重新定位");
        }*/

        if(result.getPoiList() != null) {
            HistoryPoiInfo info = new HistoryPoiInfo(result.getPoiList().get(0), false);
            if (result.getAddress() != null && info != null && info.name != null) {
                try {
                    mTwAddressText.setText(info.name);
                    this.info = info;
                } catch (Exception e) {
                    MobclickAgent.reportError(WEApplication.getContext(), e);
                }
            } else {
//            mTwAddressText.setText("获取定位失败,点击重试");
//            initLocation();
            }
        }
    }

    @Subscriber(tag = EventBusTags.ADDRESS)
    public void getUserAddressBook(String usual_address) {
        mPresenter.getUserAddressBook();
    }

    /*@Subscriber(tag = EventBusTags.NO_NETWORK)
    public void noNetWork(boolean flag){
        if(flag) {
            locationFlag = false;
            mTwAddressText.setText("获取定位失败,点击重试");
            showMessage("网络连接失败,请检查网络后重试");
        }else{
            locationFlag = true;
            initLocation();
        }
    }*/

    public void showRecyClerView(){
        setRecyclerView(true);
        setSearch(false);
        setNodata(false);
    }
    public void showSearch(){
        setRecyclerView(false);
        setSearch(true);
        setNodata(false);
    }
    public void showNodata(){
        setRecyclerView(false);
        setSearch(false);
        setNodata(true);
    }

    public void setRecyclerView(boolean show){
        mRvAddress.setVisibility(show == true ? View.VISIBLE : View.GONE);
    }

    public void setSearch(boolean show){
        mLlSearch.setVisibility(show == true ? View.VISIBLE : View.GONE);
    }
    public void setNodata(boolean show){
        mTwNodata.setVisibility(show == true ? View.VISIBLE : View.GONE);
    }

    @Override
    public int[] hideSoftByEditViewIds() {
        int[] ids = {R.id.et_address};
        return ids;
    }
}
