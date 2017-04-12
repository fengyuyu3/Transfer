package com.ironaviation.traveller.mvp.ui.airportoff;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.app.utils.CheckIdCardUtils;
import com.ironaviation.traveller.app.utils.TimerUtils;
import com.ironaviation.traveller.app.utils.TypefaceUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEFragment;
import com.ironaviation.traveller.di.component.airportoff.DaggerAirPortOffComponent;
import com.ironaviation.traveller.di.module.airportoff.AirPortOffModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.airportoff.AirPortOffContract;
import com.ironaviation.traveller.mvp.model.entity.request.AirPortRequest;
import com.ironaviation.traveller.mvp.model.entity.response.Flight;
import com.ironaviation.traveller.mvp.model.entity.response.FlightDetails;
import com.ironaviation.traveller.mvp.presenter.airportoff.AirPortOffPresenter;
import com.ironaviation.traveller.mvp.ui.widget.FontTextView;
import com.ironaviation.traveller.mvp.ui.widget.MyTimeDialog;
import com.ironaviation.traveller.mvp.ui.widget.NumDialog;
import com.ironaviation.traveller.mvp.ui.widget.PublicTextView;
import com.ironaviation.traveller.mvp.ui.widget.TimePicker.MyTimePickerView;
import com.ironaviation.traveller.mvp.ui.widget.TerminalPopupWindow;
import com.ironaviation.traveller.mvp.ui.widget.TravelPopupwindow;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.Subscriber;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/27 18:15
 * 修改人：
 * 修改时间：2017/3/27 18:15
 * 修改备注：
 */

public class AirPortOffFragment extends WEFragment<AirPortOffPresenter> implements AirPortOffContract.View
        , NumDialog.CallBackItem, AirPortAdapter.ItemIdCallBack, AirPortAdapter.ItemIdStatusCallBack,TerminalPopupWindow.CallBackItem
        ,MyTimeDialog.ItemCallBack{

    @BindView(R.id.pw_person)
    PublicTextView mPwPerson;
    @BindView(R.id.pw_flt)
    PublicTextView mPwFltNo;
    @BindView(R.id.pw_time)
    PublicTextView mPwTime;
    @BindView(R.id.pw_address)
    PublicTextView mPwAddress;
    @BindView(R.id.pw_airport)
    PublicTextView mPwAirport;
    @BindView(R.id.pw_seat)
    PublicTextView mPwSeat;
    @BindView(R.id.rw_airport)
    RecyclerView rwAirport;
    @BindView(R.id.ll_certification)
    AutoLinearLayout llCertification;
    Unbinder unbinder;
    @BindView(R.id.ll_state)
    AutoRelativeLayout mLlState;
    @BindView(R.id.tw_best_price)
    FontTextView mTwBestPrice;
    @BindView(R.id.tw_original_price)
    FontTextView mTwOriginalPrice;
    @BindView(R.id.tw_go_to_order)
    TextView mTwGoToOrder;
    @BindView(R.id.ll_price)
    AutoLinearLayout mLlPrice;
    @BindView(R.id.ll_book)
    AutoLinearLayout llBook;

    /*@BindView(R.id.pw_id_card)
    PublicView mPwIdCard;*/

    private NumDialog mNumDialog, mTerminal;
    private AirPortAdapter mAirPortAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TravelPopupwindow mTravelPopupwindow;
    private List<AirPortRequest> list; //这是recylerview 的数据
    private List<AirPortRequest> mAirportRequests;
    private MyTimePickerView pvTime;
    private MyTimeDialog mMyTimeDialog;
    private TerminalPopupWindow mTerminalPopupWindow;
    private int terminalNum = -1;
    private String format = "MM月dd日 HH点mm分";
    private boolean addressFlag= true,timeFlag;

    public static AirPortOffFragment newInstance() {
        AirPortOffFragment fragment = new AirPortOffFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerAirPortOffComponent
                .builder()
                .appComponent(appComponent)
                .airPortOffModule(new AirPortOffModule(this))//请将AirPortOffModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_airport_off, container, false);
    }

    @Override
    protected void initData() {
        mTravelPopupwindow = new TravelPopupwindow(getActivity());
        mNumDialog = new NumDialog(getActivity(), getNumsData(), this, Constant.AIRPORT_TYPE_SEAT);
        mTerminal = new NumDialog(getActivity(), getTerminal(), this, Constant.AIRPORT_TYPE_TERMINAL);
        mTerminalPopupWindow = new TerminalPopupWindow(getActivity(),getTerminal(),this);
        setRidTimeHide();
        initEmptyData();
        setPrice();
        /*pvTime = new MyTimePickerView(getActivity(), MyTimePickerView.Type.MONTH_DAY_HOUR_TEN_MIN);
        pvTime.setTime(new Date());
        Date date=new Date();
        date.setTime(1491782400000l);
        pvTime.setEndDate(date);
        pvTime.setTitle(getResources().getString(R.string.riding_time_select));
        pvTime.setCancelable(true);
        pvTime.setOnTimeSelectListener(new MyTimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) throws Exception {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);

            }
        });*/
//        mPwSeat.setTextInfo(getResources().getString(R.string.airport_seat));
//      setPrice();
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

    }

    @OnClick({R.id.pw_seat, R.id.pw_airport, R.id.pw_flt,R.id.pw_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pw_seat:
                mNumDialog.show();
                break;
            case R.id.pw_airport:
                if(mTerminalPopupWindow != null){
                    mTerminalPopupWindow.setNum(terminalNum);
                    mTerminalPopupWindow.show(mPwSeat);
                }
//                mTerminal.show();
                break;
            case R.id.pw_time:
//                pvTime.show();
                mMyTimeDialog.showDialog(getResources().getString(R.string.airport_input_time));
                break;
            case R.id.pw_flt:
                Intent intent = new Intent(getActivity(), TravelFloatActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in_alpha, R.anim.right_out_alpha);
//                mTravelPopupwindow.showPopupWindow(mPwPerson);
                break;
        }
    }

    public List<String> getNumsData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Constant.SEAT_NUM; i++) {
            list.add(i + 1 + "个座位");
        }
        return list;
    }

    public List<String> getTerminal() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            list.add("成都双流国际机场T"  + (i + 1) + "航站楼");
        }
        return list;
    }

    public void initEmptyData() {
        mAirportRequests = new ArrayList<>();
        for (int i = 0; i < Constant.SEAT_NUM; i++) {
            AirPortRequest request = new AirPortRequest();
            request.setStatus(Constant.AIRPORT_NO);
            mAirportRequests.add(request);
        }
    }

    @Override
    public void getItem(int position, int type) {
        if (type == Constant.AIRPORT_TYPE_SEAT) {
            setSeat(position);
//            mAirPortAdapter.setData(list);
            mNumDialog.dismiss();
        }
    }

    public void setSeat(int position){
        mPwSeat.setTextInfo("需要" + (position + 1) + "个座位");
        clearMoreData(position);
        if(mPwFltNo.getTextInfo().substring(0,2).equalsIgnoreCase(Constant.SC_AIRPORT)) {
            List<AirPortRequest> list = new ArrayList<>();
            for (int i = 0; i < position; i++) {
                list.add(mAirportRequests.get(i));
            }
            addLinearLayout(position);
            mPwSeat.setLineVisiable(true);
        }else{
            mPwSeat.setLineVisiable(false);
        }
    }

    //获取验证身份号码的位置和身份证号码
    @Override
    public void getItem(String cardId, int position) {
        mAirportRequests.get(position).setStatus(Constant.AIRPORT_SUCCESS);
        mAirportRequests.get(position).setIdCard(cardId);
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

    @Override
    public void getStatusItem(String cardId, int position, int status) {
        list.get(position).setIdCard(cardId);
        list.get(position).setStatus(status);
        mAirPortAdapter.setData(list);
    }

    public void setRidTimeHide() {
        mPwTime.setVisibility(View.GONE);
        mPwAddress.setVisibility(View.GONE);
        mPwAirport.setVisibility(View.GONE);
        mPwSeat.setVisibility(View.GONE);
        llCertification.setVisibility(View.GONE);
        showPrice(false);
    }

    public void setRidTimeShow() {
        mPwTime.setVisibility(View.VISIBLE);
        mPwAddress.setVisibility(View.VISIBLE);
        mPwAirport.setVisibility(View.VISIBLE);
        mPwSeat.setVisibility(View.GONE);
        llCertification.setVisibility(View.GONE);
        showPrice(false);
    }
    public void showPrice(){
        mPwSeat.setVisibility(View.VISIBLE);
        llCertification.setVisibility(View.VISIBLE);
        showPrice(true);
    }

    public void addLinearLayout(int position) {
//        llCertification
        llCertification.removeAllViews();
        for (int i = 0; i < position + 1; i++) {
            MyAirportHolder holder = new MyAirportHolder();
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.include_public_view, null, false);
            holder.mIvLogo = (ImageView) view.findViewById(R.id.iv_logo); //右边的图标
            holder.mLineEdt = view.findViewById(R.id.line_edt); //底下的线
            holder.mIvStatus = (ImageView) view.findViewById(R.id.iv_status); //右边的图标  占时不用;
            holder.mEdtContent = (EditText) view.findViewById(R.id.edt_content); // 文本框
            holder.mTvCode = (TextView) view.findViewById(R.id.tv_code);  //验证按钮
            holder.mPwLl = (AutoLinearLayout) view.findViewById(R.id.pw_ll); //整个布局
            if (mAirportRequests.get(i).getStatus() == Constant.AIRPORT_SUCCESS) {
                setSuccess(holder);
            } else if (mAirportRequests.get(i).getStatus() == Constant.AIRPORT_FAILURE) {
                setFailure(holder);
            } else {
                setNomal(holder);
            }
            if (i == position) {
                holder.mLineEdt.setVisibility(View.GONE);
            }
            setAirportData(holder, mAirportRequests.get(i));
            setlistener(holder, i);
            llCertification.addView(view);
        }
    }

    //正常状况
    public void setNomal(MyAirportHolder holder) {
        holder.mIvLogo.setImageResource(R.mipmap.ic_validate);
        holder.mTvCode.setText("验证");
        holder.mTvCode.setTextColor(getResources().getColor(R.color.white));
        holder.mTvCode.setBackgroundResource(R.drawable.btn_code_brown);
        holder.mEdtContent.setEnabled(true);
    }

    //成功状态
    public void setSuccess(MyAirportHolder holder) {
        holder.mIvLogo.setImageResource(R.mipmap.ic_success);
        holder.mTvCode.setText("重置");
        holder.mTvCode.setTextColor(getResources().getColor(R.color.white));
        holder.mTvCode.setBackgroundResource(R.drawable.btn_code_brown);
        holder.mEdtContent.setEnabled(false);
    }

    //失败状态
    public void setFailure(MyAirportHolder holder) {
        holder.mIvLogo.setImageResource(R.mipmap.ic_failure);
        holder.mTvCode.setText("验证");
        holder.mTvCode.setTextColor(getResources().getColor(R.color.white));
        holder.mTvCode.setBackgroundResource(R.drawable.btn_code_brown);
        holder.mEdtContent.setEnabled(true);
    }

    //设置数据
    public void setAirportData(MyAirportHolder holder, AirPortRequest request) {
        holder.mEdtContent.setText(request.getIdCard());
        if (request.getStatus() == Constant.AIRPORT_SUCCESS) {
            setSuccess(holder);
        } else if (request.getStatus() == Constant.AIRPORT_FAILURE) {
            setFailure(holder);
        } else {
            setNomal(holder);
        }
    }

    public void setlistener(final MyAirportHolder holder, final int position) {
        holder.mEdtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAirportRequests.get(position).setIdCard(editable.toString());
            }
        });

        holder.mTvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAirportRequests.get(position).getStatus() != Constant.AIRPORT_SUCCESS) {
                    if (holder.mEdtContent.getText().toString().trim() != null && CheckIdCardUtils.validateCard(holder.mEdtContent.getText().toString().trim())) {
                        mAirportRequests.get(position).setStatus(Constant.AIRPORT_SUCCESS);
                        mAirportRequests.get(position).setIdCard(holder.mEdtContent.getText().toString().trim());
                        UiUtils.makeText(holder.mEdtContent.getText().toString().trim() + "  " + position);
                    } else {
                        UiUtils.makeText("请输入正确身份证号码:" + holder.mEdtContent.getText().toString().trim() + "  " + position);
                    }
                } else {
                    holder.mEdtContent.setText("");
                    setNomal(holder);
                    mAirportRequests.get(position).setStatus(Constant.AIRPORT_NO);
                }
            }
        });
    }

    public void setPrice() {
//        TypefaceUtils.getInstance().setTypeface(getActivity(),mTwBestPrice);
//        TypefaceUtils.getInstance().setTypeface(getActivity(),mTwOriginalPrice);
//        String bestPrice = "<font color='#e83328'>" + "22.04" + "</font>" + "<font color='#b2b2b2' size=40> 元</font>";
        mTwBestPrice.setTextType("22.04");
//        String originalPrice = "<font color='#3a3a3a' >" + "22.04" + "</font>" + "<font color='#3a3a3a' size=40> 元</font>";
        mTwOriginalPrice.setTextType("22.04");
        mTwOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public void clearMoreData(int position) {
        for (int i = position + 1; i < Constant.SEAT_NUM; i++) {
            mAirportRequests.get(i).setIdCard("");
            mAirportRequests.get(i).setStatus(Constant.AIRPORT_NO);
        }
    }
    //获取航站楼位置
    @Override
    public void getItem(int position) {
        terminalNum = position;
        mPwAirport.setTextInfo("成都双流国际机场T" + (position + 1) + "航站楼");
        mTerminalPopupWindow.dismiss();
    }

    //设置时间
    @Override
    public void setTime(long time) {
        mPwTime.setTextInfo(TimerUtils.getDateFormat(time,format));
        timeFlag = true;
        if(timeFlag && addressFlag ){
            showPrice();
            setSeat(Constant.DEFULT_SEAT);
        }
    }

    public class MyAirportHolder {
        public ImageView mIvLogo;
        public View mLineEdt;
        public ImageView mIvStatus;
        public EditText mEdtContent;
        public TextView mTvCode;
        public AutoLinearLayout mPwLl;
    }

    @Subscriber(tag = EventBusTags.FLIGHT)
    public void getFlightInfo(Flight flight){
        clearData();
        setRidTimeShow();
        mPwFltNo.setTextInfo(flight.getInfo().getFlightNo());
        if(getTerminalNum(flight.getList().get(0).getTakeOff()) != -1){
            terminalNum = getTerminalNum(flight.getList().get(0).getTakeOff());
            mPwAirport.setTextInfo(getTerminal().get(getTerminalNum(flight.getList().get(0).getTakeOff())));
        }
        mMyTimeDialog = new MyTimeDialog(getActivity(),this,flight.getList().get(0).getTakeOffTime());
    }
    /*@Subscriber(tag = EventBusTags.AIRPORT_TIME)
    public void getAirportTime(long time){
//        setRidTimeShow();
        mPwTime.setTextInfo(TimerUtils.getDateFormat(time,format));
    }*/
    //清理数据
    public void clearData(){
        mPwTime.setInitInfo(getResources().getString(R.string.airport_time));
        mPwAddress.setInitInfo(getResources().getString(R.string.airport_address));
        mPwAirport.setInitInfo(getResources().getString(R.string.airport_airport));
        mPwSeat.setTextInfo(getResources().getString(R.string.airport_seat));
        mPwSeat.setVisibility(View.GONE);
        showPrice(false);
        setSeat(Constant.DEFULT_SEAT);
    }

    //显示价格
    public void showPrice(boolean flag){
        if(flag) {
            llBook.setVisibility(View.VISIBLE);
        }else{
            llBook.setVisibility(View.GONE);
        }
    }

    public int getTerminalNum(String text){
        if(text.contains("T1")){
            return 0;
        }else if(text.contains("T2")){
            return 1;
        }else{
            return -1;
        }
    }
}