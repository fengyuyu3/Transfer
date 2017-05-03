package com.ironaviation.traveller.mvp.ui.my;

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
import com.ironaviation.traveller.app.utils.LocationService;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.DaggerAddressSearchComponent;
import com.ironaviation.traveller.di.module.my.AddressSearchModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.AddressSearchContract;
import com.ironaviation.traveller.mvp.model.entity.HistoryPoiInfo;
import com.ironaviation.traveller.mvp.presenter.my.AddressSearchPresenter;
import com.ironaviation.traveller.mvp.ui.manager.FullyLinearLayoutManager;
import com.ironaviation.traveller.mvp.ui.my.adapter.AddressAdapter;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;

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
 * 创建时间：2017/5/2 19:40
 * 修改人：starRing
 * 修改时间：2017/5/2 19:40
 * 修改备注：
 */
public class AddressSearchActivity extends WEActivity<AddressSearchPresenter> implements AddressSearchContract.View, OnGetPoiSearchResultListener, OnGetGeoCoderResultListener {

    @BindView(R.id.et_address)
    EditText mEtAddress;
    @BindView(R.id.tv_cancel)
    TextView mTvCancel;
    @BindView(R.id.rv_address)
    RecyclerView mRvAddress;
    @BindView(R.id.ll_address)
    AutoLinearLayout mLlAddress;
    @BindView(R.id.tw_address_text)
    TextView mTwAddressText;

    private RecyclerView.LayoutManager mLayoutManager;

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

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAddressSearchComponent
                .builder()
                .appComponent(appComponent)
                .addressSearchModule(new AddressSearchModule(this)) //请将AddressSearchModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_address_search, null, false);
    }

    @Override
    protected void initData() {
        mTwAddressText.setText(getResources().getString(R.string.address_locationing));
        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mAddressAdapter = new AddressAdapter(R.layout.item_address);
        mLayoutManager = new FullyLinearLayoutManager(this);
        mRvAddress.setLayoutManager(mLayoutManager);
        mRvAddress.setAdapter(mAddressAdapter);
        infos = new ArrayList<>();
        mAddressAdapter.setNewData(infos);
        initLocation();
        Bundle pBundle = getIntent().getExtras();
        if (pBundle != null) {
            setView(pBundle);
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
                if (charSequence.length() <= 0) {
                    //mRvAddress.setVisibility(View.GONE);
                    //infos = null;
                    infos = new ArrayList<>();
                    mAddressAdapter.setNewData(infos);
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
                mPresenter.updateAddressBook(uabId, infos.get(position), addressType);
            }
        });
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
    public void onGetGeoCodeResult(GeoCodeResult result) {

    }


    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {

    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult result) {

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

        dismissProgressDialog();
        if (result == null || result.getAllPoi() == null) {
            mRvAddress.setVisibility(View.GONE);
            return;
        }
        infos = new ArrayList<>();

        for (PoiInfo info : result.getAllPoi()) {
            if (info.location != null) {
                infos.add(new HistoryPoiInfo(info, false));
            }
        }
        mAddressAdapter.setNewData(infos);

        if (infos.size() != 0) {
            mRvAddress.setVisibility(View.VISIBLE);
            //rl_passenger_phone.setVisibility(View.INVISIBLE);
            // rl_passenger_standby_phone.setVisibility(View.INVISIBLE);
            // rl_reservation.setVisibility(View.INVISIBLE);
        } else {
            mRvAddress.setVisibility(View.GONE);
            // rl_passenger_phone.setVisibility(View.VISIBLE);
            //rl_passenger_standby_phone.setVisibility(View.VISIBLE);
            // rl_reservation.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_cancel)
    public void onClick() {
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


    /**
     * 设置页面
     *
     * @param
     */
    private void setView(Bundle bundle) {
        addressType = bundle.getInt(Constant.ADDRESS_TYPE);

        if (!TextUtils.isEmpty(bundle.getString(Constant.UABID))) {
            uabId = bundle.getString(Constant.UABID);
        }

        if (addressType != 0) {
            switch (addressType) {
                case Constant.ADDRESS_TYPE_COMPANY:
                case Constant.ADDRESS_USUAl_COMPANY:
                    mEtAddress.setHint(getString(R.string.hint_company_address));
                    break;
                case Constant.ADDRESS_TYPE_HOME:
                case Constant.ADDRESS_USUAl_HOME:
                    mEtAddress.setHint(getString(R.string.hint_home_address));
                    break;
            }

        }
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
        if (locationService == null) {
            locationService = new LocationService(this);
            locationService.registerListener(mListener);
            locationService.setLocationOption(locationService.getDefaultLocationClientOption());
            locationService.start();
        }
    }

    private BDLocationListener mListener = new BDLocationListener() {
        double longitude = 0;
        double latitude = 0;

        @Override
        public void onReceiveLocation(BDLocation location) {
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                LatLng ptCenter = new LatLng(location.getLatitude() //latitude weidu
                        , location.getLongitude()); //long jingdu
                // 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ptCenter));
            }
        }
    };


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

        HistoryPoiInfo info = new HistoryPoiInfo(result.getPoiList().get(0), false);
        if (result.getAddress() != null && info != null && info.name != null) {
            mTwAddressText.setText(info.name);
            this.info = info;
        }
    }
}
