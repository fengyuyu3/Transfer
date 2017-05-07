package com.ironaviation.traveller.mvp.ui.airportoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.ui.widget.MyDialog;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.airportoff.DaggerTravelFloatComponent;
import com.ironaviation.traveller.di.module.airportoff.TravelFloatModule;
import com.ironaviation.traveller.mvp.contract.airportoff.TravelFloatContract;
import com.ironaviation.traveller.mvp.model.entity.response.Flight;
import com.ironaviation.traveller.mvp.model.entity.response.FlightDetails;
import com.ironaviation.traveller.mvp.presenter.airportoff.TravelFloatPresenter;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/30 16:35
 * 修改备注：
 */

public class TravelFloatActivity extends WEActivity<TravelFloatPresenter> implements TravelFloatContract.View {

    @BindView(R.id.iv_logo)
    ImageView mIvLogo;
    @BindView(R.id.edt_travel_num)
    EditText mEdtTravelNum;
    @BindView(R.id.rl_travel_num)
    AutoRelativeLayout mRlTravelNum;
    @BindView(R.id.iw_time)
    ImageView mIwTime;
    @BindView(R.id.tw_fly_time)
    TextView mTwFlyTime;
    @BindView(R.id.rl_airport_fly_time)
    AutoRelativeLayout mRlAirportFlyTime;
    @BindView(R.id.tw_city)
    TextView mTwCity;
    @BindView(R.id.ll_city)
    AutoLinearLayout mllCity;
    @BindView(R.id.rw_city)
    RecyclerView mRwCity;
    @BindView(R.id.ll_port)
    AutoLinearLayout mLlPort;
    @BindView(R.id.rl_child_city)
    AutoRelativeLayout mLlChildCity;
    private RecyclerView.LayoutManager layoutManager;
    private TravelFloatAdapter mTravelFloatAdapter;
    private MyDialog mMyDialog;
    private Flight mFlight;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerTravelFloatComponent
                .builder()
                .appComponent(appComponent)
                .travelFloatModule(new TravelFloatModule(this)) //请将TravelFloatModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.popupwindow_travel, null, false);
    }

    @Override
    protected void initData() {
        mMyDialog = new MyDialog(this);
        setTravelNum();
        setEditorAction();
        setFlyTime();
        initRecyclerView();
        mLlChildCity.setOnClickListener(new View.OnClickListener() { //获取焦点消费事件
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void initRecyclerView(){
        layoutManager = new LinearLayoutManager(this);
        mRwCity.setLayoutManager(layoutManager);
        mTravelFloatAdapter = new TravelFloatAdapter(this,Constant.TYPE_AIRPORT_OFF);
        mRwCity.setAdapter(mTravelFloatAdapter);
    }

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

    public void setTravelNum(){
        mEdtTravelNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //大于2就在下面显示
                if(mEdtTravelNum.getText().toString().trim().length() > 2){
                    setHideTime(editable.toString());
                }else{
                    mRlAirportFlyTime.setVisibility(View.GONE);
                }
                hideFlight();
            }
        });
    }

    public void setEditorAction(){
        mEdtTravelNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    if(mEdtTravelNum.getText().toString().trim().length() <= 2 ){
                        return false;
                    }else{
                        if(mEdtTravelNum.getText().toString().trim().substring(0,2).equalsIgnoreCase(Constant.SC_AIRPORT)) {
                            mRlAirportFlyTime.setVisibility(View.VISIBLE);
                            setShowTime();
                            hideFlight();
                            mMyDialog.showDialog(getList(),getResources().getString(R.string.airport_fly_time_select));
                        }else{
                            showMessage(getResources().getString(R.string.airport_sc));
                        }
                        return false;
                    }
                }
                return false;
            }
        });
    }

    //设置集合数据
    public List<String> getList(){
        return TimerUtils.getSevenDate();
    }

    public void setFlyTime(){

        mRlAirportFlyTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mEdtTravelNum.getText().toString().trim().substring(0,2).equalsIgnoreCase(Constant.SC_AIRPORT)) {
                    mRlAirportFlyTime.setVisibility(View.VISIBLE);
                    setShowTime();
                    hideFlight();
                    mMyDialog.showDialog(getList(),getResources().getString(R.string.airport_fly_time_select));
                }else{
                    showMessage(getResources().getString(R.string.airport_sc));
                }
            }
        });

    }

    public void setShowTime(){
        mRlAirportFlyTime.setVisibility(View.VISIBLE);
        mIwTime.setVisibility(View.VISIBLE);
        mTwFlyTime.setText("请选择当地起飞时间");
        mTwFlyTime.setTextColor(getResources().getColor(R.color.airport_edit_gray));
    }

    public void setHideTime(String text){
        mRlAirportFlyTime.setVisibility(View.VISIBLE);
        mIwTime.setVisibility(View.INVISIBLE);
        mTwFlyTime.setText(text);
    }

    public void setFlyTime(String text){
        mRlAirportFlyTime.setVisibility(View.VISIBLE);
        mIwTime.setVisibility(View.VISIBLE);
        mTwFlyTime.setText(text);
        mTwFlyTime.setTextColor(getResources().getColor(R.color.word_already_input));
    }

    public void hideFlight(){
        mllCity.setVisibility(View.GONE);
    }

    @OnClick({R.id.ll_port})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.ll_port:
                if(mMyDialog.isShowing()){
                    mMyDialog.dismiss();
                }else {
                    finish();
                }
            break;
        }
    }
    @Subscriber(tag = EventBusTags.DIALOG_EVENT)
    public void dialogEvent(int index){
        Date date=new Date();//取时间
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, index);//把日期往后增加一天.整数往后推,负数往前移动
        date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        if(mEdtTravelNum.getText().toString().trim() != null) {
//            mPresenter.getFlightInfo("cz3081", dateString);
            mPresenter.getFlightInfo(mEdtTravelNum.getText().toString().trim(),dateString);
        }else{
            showMessage("数据为空");
        }
    }
    @Override
    public void setData(Flight flight,String date) {
        mFlight = new Flight();
        mFlight.setInfo(flight.getInfo());
        mMyDialog.dismiss();
        setFlyTime(date);
        mllCity.setVisibility(View.VISIBLE);

        if(flight.getList() != null) {
            mTravelFloatAdapter.setList(flight.getList());
            mTwCity.setText("本航段共有"+(flight.getList().size())+"个航班");
        }else{
            mTwCity.setText("本航段无航班");
        }
    }
    @Subscriber(tag = EventBusTags.FLIGHT_INFO)
    public void getFlightInfo(FlightDetails flightDetails){
        List<FlightDetails> list = new ArrayList<>();
        list.add(flightDetails);
        mFlight.setList(list);
        EventBus.getDefault().post(mFlight, EventBusTags.FLIGHT);
        finish();
    }
}
