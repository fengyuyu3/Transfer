package com.ironaviation.traveller.mvp.ui.airportoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.TimeUtils;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.ironaviation.traveller.app.utils.UserInfoUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEFragment;
import com.ironaviation.traveller.di.component.airportoff.DaggerSpecialCarComponent;
import com.ironaviation.traveller.di.module.airportoff.SpecialCarModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.airportoff.SpecialCarContract;
import com.ironaviation.traveller.mvp.model.entity.HistoryPoiInfo;
import com.ironaviation.traveller.mvp.model.entity.PayInfo;
import com.ironaviation.traveller.mvp.model.entity.request.AirportGoInfoRequest;
import com.ironaviation.traveller.mvp.model.entity.request.PreViewRequest;
import com.ironaviation.traveller.mvp.model.entity.response.CarTypeResponse;
import com.ironaviation.traveller.mvp.model.entity.response.Flight;
import com.ironaviation.traveller.mvp.presenter.airportoff.SpecialCarPresenter;
import com.ironaviation.traveller.mvp.ui.airporton.TravelFloatOnActivity;
import com.ironaviation.traveller.mvp.ui.my.AddressActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoViewPager;
import com.ironaviation.traveller.mvp.ui.widget.BalancePopupwindow;
import com.ironaviation.traveller.mvp.ui.widget.MyTimeDialog;
import com.ironaviation.traveller.mvp.ui.widget.PublicTextView;
import com.ironaviation.traveller.mvp.ui.widget.TerminalPopupWindow;
import com.ironaviation.traveller.mvp.ui.widget.TimeNewDialog;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

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

public class SpecialCarFragment extends WEFragment<SpecialCarPresenter> implements SpecialCarContract.View, MyTimeDialog.ItemCallBack,
        TerminalPopupWindow.CallBackItem {

    @BindView(R.id.pw_new_person_airport)
    PublicTextView pwNewPersonAirport;
    @BindView(R.id.pw_new_flt)
    PublicTextView pwNewFlt;
    @BindView(R.id.pw_new_time_on)
    PublicTextView pwNewTimeOn;
    @BindView(R.id.pw_new_airport_on)
    PublicTextView pwNewAirportOn;
    @BindView(R.id.pw_new_address_on)
    PublicTextView pwNewAddressOn;
    @BindView(R.id.ll_new_on_address)
    AutoLinearLayout llNewOnAddress;
    @BindView(R.id.vp_new_car)
    AutoViewPager vpNewCar;
    @BindView(R.id.tw_validate)
    TextView twValidate;
    @BindView(R.id.tw_go_to_order)
    TextView twGoToOrder;
    @BindView(R.id.ll_new_port)
    AutoLinearLayout mLlNewPort;
    Unbinder unbinder;
    @BindView(R.id.ll_enterPort)
    AutoLinearLayout llEnterPort;
    @BindView(R.id.pw_new_address_off)
    PublicTextView pwNewAddressOff;
    @BindView(R.id.pw_new_airport_off)
    PublicTextView pwNewAirportOff;
    @BindView(R.id.ll_clearPort)
    AutoLinearLayout llClearPort;
    @BindView(R.id.viewpager_progress)
    ProgressBar viewpagerProgress;
    @BindView(R.id.al_validate)
    AutoRelativeLayout alValidate;

    private String phone;
    private String flightNo = "";
    private String status;
    private TimeNewDialog mTimeNewDialog;
    private Flight flight;
    private long time;
    private int terminalNum = 0;
    private TerminalPopupWindow mTerminalPopupWindow;
    private HistoryPoiInfo info;
    private List<Fragment> fragmentList = new ArrayList<>();
    private ImageView[] imageViews;
    private MyAdapter adapter;
    private BalancePopupwindow balancePopupwindow;
    private boolean timeFlag,pickAddressFlag;

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerSpecialCarComponent
                .builder()
                .appComponent(appComponent)
                .specialCarModule(new SpecialCarModule(this))//请将SpecialCarModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_specialcar_main, container, false);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        mTimeNewDialog = new TimeNewDialog(getActivity(), this);
        mTerminalPopupWindow = new TerminalPopupWindow(getActivity(), getTerminal(), this);
        mTerminalPopupWindow.setNum(terminalNum);
        balancePopupwindow = new BalancePopupwindow(getActivity());
        status = bundle.getString(Constant.STATUS);
        if(status.equals(Constant.Z_ENTER_PORT)){
            llEnterPort.setVisibility(View.VISIBLE);
            llClearPort.setVisibility(View.GONE);
            pwNewAirportOn.setTextInfo(Constant.AIRPORT_T1);
        }else if(status.equals(Constant.Z_CLEAR_PORT)){
            llEnterPort.setVisibility(View.GONE);
            llClearPort.setVisibility(View.VISIBLE);
            pwNewAirportOff.setTextInfo(Constant.AIRPORT_T1);
        }
        if (UserInfoUtils.getInstance().getInfo(getActivity()) != null
                && UserInfoUtils.getInstance().getInfo(getActivity()).getPhone() != null) {
            phone = UserInfoUtils.getInstance().getInfo(getActivity()).getPhone();
            pwNewPersonAirport.setTextInfo(phone);
        }
        //setViewPager(false);
        mPresenter.getCarType();
    }

    public void InitPort(int size) {
        imageViews = new ImageView[size];
        for (int i = 0; i < size; i++) {
            ImageView imageView = new ImageView(getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                layoutParams.leftMargin = 20;
            }
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.ic_page_indicator_theme_splash);
            mLlNewPort.addView(imageView);
            imageViews[i] = imageView;
        }
        imageViews[0].setImageResource(R.drawable.ic_page_indicator_theme_splash_focus);
    }

    public void setClear(int size) {
        for (int i = 0; i < size; i++) {
            imageViews[i].setImageResource(R.drawable.ic_page_indicator_theme_splash);
        }
    }

    public void setFragmentData(List<CarTypeResponse> list) {
        for(int i = 0 ; i < list.size(); i++){
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STATUS,list.get(i));
            ViewPagerCarFragment fragment = new ViewPagerCarFragment();
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传bundle,里面存一个what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事,和message同理
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在内部onActivityCreated中
     * 初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.pw_new_flt, R.id.pw_new_time_on,
            R.id.pw_new_airport_on, R.id.pw_new_address_on,
            R.id.pw_new_airport_off, R.id.pw_new_address_off,
            R.id.al_validate,R.id.tw_go_to_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pw_new_flt:
                setPwFltNo(status);
                break;
            case R.id.pw_new_time_on:
                judgeFlyNo();
                break;
            case R.id.pw_new_airport_on:
                if (mTerminalPopupWindow != null) {
                    mTerminalPopupWindow.setNum(terminalNum);
                    mTerminalPopupWindow.show(pwNewAirportOn);
                }
                break;
            case R.id.pw_new_address_on:
                setAddress(Constant.Z_ENTER_PORT);
                break;
            case R.id.pw_new_address_off:
                setAddress(Constant.Z_CLEAR_PORT);
                break;
            case R.id.pw_new_airport_off:
                if (mTerminalPopupWindow != null) {
                    mTerminalPopupWindow.setNum(terminalNum);
                    mTerminalPopupWindow.show(pwNewAirportOn);
                }
                break;
            case R.id.al_validate:
                expainFree();
                break;
            case R.id.tw_go_to_order:
                balancePopupwindow.show(twGoToOrder);
                break;
        }
    }

    public void  expainFree(){
        Intent intent3 = new Intent(getActivity(),ZVerifyIdCardActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString(Constant.STATUS,status);
        AirportGoInfoRequest mRequest = new AirportGoInfoRequest();
        /*if(mPassengersRequests != null && mPassengersRequests.size() > 0){
            mRequest.setPassengers(mPassengersRequests);
        }*/
        bundle1.putSerializable(Constant.AIRPORT_GO_INFO,mRequest);
        intent3.putExtras(bundle1);
        startActivity(intent3);
    }

    /**
     * 航班判断是跳转到接机或者送机
     *
     * @param myStatus 状态
     */
    public void setPwFltNo(String myStatus) {
        if (myStatus != null) {
            Intent intent = new Intent();
            if (myStatus.equals(Constant.Z_ENTER_PORT)) {
                intent.setClass(getActivity(), TravelFloatOnActivity.class);
                intent.putExtra(Constant.FLIGHT_NO, flightNo);
                intent.putExtra(Constant.STATUS, Constant.Z_ENTER_PORT);
            } else if (myStatus.equals(Constant.Z_CLEAR_PORT)) {
                intent.setClass(getActivity(), TravelFloatActivity.class);
                intent.putExtra(Constant.FLIGHT_NO, flightNo);
                intent.putExtra(Constant.STATUS, Constant.Z_CLEAR_PORT);
            }
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.left_in_alpha, R.anim.right_out_alpha);
        }
    }

    /**
     * 判断时间
     */
    public void judgeFlyNo() { //判断时间
        String fno = pwNewFlt.getTextInfo();
        if (fno.equals(getActivity().getResources().getString(R.string.airport_no))) {
//            showMessage(getString(R.string.airport_no));
            mTimeNewDialog.setSenvenTime(System.currentTimeMillis(), Constant.AIRPORT_NO_PORT);
        }
        mTimeNewDialog.showDialog(getResources().getString(R.string.airport_input_time));
//          mMyTimeDialog.showDialog(getResources().getString(R.string.airport_input_time));
    }

    public void setAddress(String status) {
        Bundle bundle = new Bundle();
        if (status != null && status.equals(Constant.Z_ENTER_PORT)) {
            bundle.putInt(Constant.ADDRESS_TYPE, Constant.AIRPORT_Z_ON);
        }else if(status != null && status.equals(Constant.Z_CLEAR_PORT)){
            bundle.putInt(Constant.ADDRESS_TYPE,Constant.AIRPORT_Z_GO);
        }
        Intent intent1 = new Intent(getActivity(), AddressActivity.class);
        intent1.putExtras(bundle);
        launchActivity(intent1);
    }

    @Subscriber(tag = EventBusTags.FLIGHT)
    public void getFlightInfo(Flight flight) {
        clearTime();
        this.flight = flight;
        if (status != null && flight.getStatus() != null && flight.getStatus().equals(Constant.Z_ENTER_PORT) && flight.getStatus().equals(status)) {
            mTimeNewDialog.setZTime(flight.getList().get(0).getArriveTime(), Constant.AIRPORT_Z_ON);
            if (getTerminalNum(flight.getList().get(0).getArrive()) != -1) {
                terminalNum = getTerminalNum(flight.getList().get(0).getArrive());
                pwNewAirportOn.setTextInfo(getTerminal(terminalNum));
            }
            if (flight.getList().get(0).getTakeOffTime() != 0) {
                pwNewFlt.setArriveTime(TimerUtils.getDateFormat(flight.getList().get(0).getArriveTime(), Constant.fomartOn));
            }
            if (flight.getInfo().getFlightNo() != null) {
                flightNo = flight.getInfo().getFlightNo();
                pwNewFlt.setTextInfo(flight.getInfo().getFlightNo());
            }
                /*if((flight.getList().get(0).getArriveTime() - System.currentTimeMillis()) > 30*60*1000){
                    currentTime = flight.getList().get(0).getArriveTime() + 10*60*1000;
                }else{
                    currentTime = System.currentTimeMillis() + 30*60*1000;
                }*/
            if (flight.getList().get(0).getArriveTime() - System.currentTimeMillis() > 0) {
                pwNewTimeOn.setTextInfo(TimerUtils.getDateFormat(flight.getList().get(0).getArriveTime() + 30 * 60 * 1000, Constant.format));
                pwNewTimeOn.setArriveTime(getActivity().getResources().getString(R.string.travel_address_time_info));
            }
        } else if (status != null && flight.getStatus() != null && flight.getStatus().equals(Constant.Z_CLEAR_PORT) && flight.getStatus().equals(status)) {
            mTimeNewDialog.setSenvenTime(System.currentTimeMillis(),Constant.AIRPORT_Z_GO);
            if(getTerminalNum(flight.getList().get(0).getTakeOff()) != -1){
                terminalNum = getTerminalNum(flight.getList().get(0).getTakeOff());
                pwNewAirportOff.setTextInfo(getTerminal(getTerminalNum(flight.getList().get(0).getArrive())));
            }
            if(flight.getList().get(0).getArriveTime() != 0){
                pwNewFlt.setArriveTime(TimerUtils.getDateFormat(flight.getList().get(0).getTakeOffTime(),Constant.fomartOFF));
            }
            if(flight.getInfo().getFlightNo() != null){
                flightNo = flight.getInfo().getFlightNo();
                pwNewFlt.setTextInfo(flight.getInfo().getFlightNo());
            }

        }
    }

    public void clearTime() {
        pwNewTimeOn.setTextInfo("");
        pwNewTimeOn.setArriveTime("");
        pwNewTimeOn.setInitInfo(getActivity().getResources().getString(R.string.airport_time));
    }

    //判断是哪个航站楼
    public int getTerminalNum(String text) {
        if (text.contains("T1")) {
            return 0;
        } else if (text.contains("T2")) {
            return 1;
        } else {
            return -1;
        }
    }

    public List<String> getTerminal() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add("成都双流国际机场T" + (i + 1) + "航站楼");
        }
        return list;
    }

    public String getTerminal(int terminalNum) {
        if (terminalNum == 0) {
            return Constant.AIRPORT_T1;
        } else if (terminalNum == 1) {
            return Constant.AIRPORT_T2;
        } else {
            return Constant.AIRPORT_T1;
        }
    }

    @Override
    public void setTime(long time) {
        this.time = time;
        pwNewTimeOn.setTextInfo(TimerUtils.getDateFormat(time, Constant.format));
        pwNewTimeOn.setArriveTime("");
        timeFlag = true;
        if(timeFlag && pickAddressFlag){
//            mPresenter.getPreViewInfo(getPreViewData()); 接口出来了，才可以
        }
    }

    @Override
    public void getItem(int position) {
        if (status != null && status.equals(Constant.Z_ENTER_PORT)) {
            terminalNum = position;
            pwNewAirportOn.setTextInfo(getTerminal(position));
            mTerminalPopupWindow.dismiss();
        }else if(status != null && status.equals(Constant.Z_CLEAR_PORT)){
            terminalNum = position;
            pwNewAirportOff.setTextInfo(getTerminal(position));
            mTerminalPopupWindow.dismiss();
        }
    }

    @Subscriber(tag = EventBusTags.AIRPORT_Z_GO)
    public void getAddress(HistoryPoiInfo info) {
        if (status != null && status.equals(Constant.Z_CLEAR_PORT)) {
            this.info = info;
            pwNewAddressOff.setTextInfo(info.name);
            pickAddressFlag = true;
            if(timeFlag && pickAddressFlag){
//                mPresenter.getPreViewInfo(getPreViewData());
            }
        }
    }

    @Subscriber(tag = EventBusTags.AIRPORT_Z_ON)
    public void getAddressOn(HistoryPoiInfo info) {
        if (status != null && status.equals(Constant.Z_ENTER_PORT)) {
            this.info = info;
            pwNewAddressOn.setTextInfo(info.name);
            pickAddressFlag = true;
            if(timeFlag && pickAddressFlag){
//                mPresenter.getPreViewInfo(getPreViewData());
            }
        }
    }

    @Override
    public void freshCarType(final List<CarTypeResponse> list) {
        setViewPager(true);
        setFragmentData(list);
        InitPort(list.size());
        vpNewCar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setClear(list.size());
                imageViews[position].setImageResource(R.drawable.ic_page_indicator_theme_splash_focus);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        adapter = new MyAdapter(getActivity().getSupportFragmentManager());
        vpNewCar.setAdapter(adapter);
    }

    public class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList != null ? fragmentList.size() : 0;
        }
    }

    public PreViewRequest getPreViewData(){
        PreViewRequest params = new PreViewRequest();
        if(phone != null) {
            params.setPhone(phone);
        }
        if(flightNo != null){
            params.setFlightNo(flightNo);
        }
        params.setFlightDate(TimerUtils.getDateFormat(flight.getList().get(0).getTakeOffTime(),Constant.formatDate));
        params.setCarType(""); // 车型
        if(status.equals(Constant.Z_CLEAR_PORT)){ //送机
            params.setPickupTime(time);
            params.setPickupAddress(info.name);
            params.setPickupDetailAddress(info.address);
            params.setArriveAddress(getTerminal(terminalNum));
            params.setArriveDetailAddress("");
            params.setPortType(Constant.PORT_TYPE_CLEAR_PORT);
        }else if(status.equals(Constant.Z_ENTER_PORT)){//接机
            params.setPickupTime(time);
            params.setPickupAddress("");
            params.setPickupDetailAddress("");
            params.setArriveAddress(info.name);
            params.setArriveDetailAddress(info.address);
            params.setPortType(Constant.PORT_TYPE_ENTER_PORT);
        }
        params.setServiceType(Constant.SERVICE_TYPE_Z);
        params.setIDCardNos(new ArrayList<String>());//身份证集合

        return params;
    }

    public void setViewPager(boolean show){
        vpNewCar.setVisibility(show ? View.VISIBLE : View.GONE);
        mLlNewPort.setVisibility(show ? View.VISIBLE : View.GONE);
        viewpagerProgress.setVisibility(!show ? View.VISIBLE :View.GONE);
    }
}
