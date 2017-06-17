package com.ironaviation.traveller.mvp.ui.airportoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.airportoff.DaggerVerifyIdCardComponent;
import com.ironaviation.traveller.di.module.airportoff.VerifyIdCardModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.airportoff.VerifyIdCardContract;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.model.entity.request.AirPortRequest;
import com.ironaviation.traveller.mvp.model.entity.request.AirportGoInfoRequest;
import com.ironaviation.traveller.mvp.model.entity.request.PassengersRequest;
import com.ironaviation.traveller.mvp.presenter.airportoff.VerifyIdCardPresenter;
import com.ironaviation.traveller.mvp.ui.webview.WebViewActivity;
import com.ironaviation.traveller.mvp.ui.widget.AlertDialog;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.ironaviation.traveller.mvp.ui.widget.NumDialog;
import com.ironaviation.traveller.mvp.ui.widget.PublicTextView;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.EventBus;

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
 * Created by Administrator on 2017/5/12.
 */

public class VerifyIdCardActivity extends WEActivity<VerifyIdCardPresenter> implements VerifyIdCardContract.View,NumDialog.CallBackItem {


    @BindView(R.id.iv_function_left)
    ImageView mIvFunctionLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_function_right)
    TextView mTvFunctionRight;
    @BindView(R.id.iv_function_right)
    ImageView mIvFunctionRight;
    @BindView(R.id.toolbar)
    AutoToolbar mToolbar;
    @BindView(R.id.ll_state_id_card)
    AutoRelativeLayout mLlStateIdCard;
    @BindView(R.id.pw_seat_id_card)
    PublicTextView mPwSeatIdCard;
    @BindView(R.id.ll_recyclerview)
    AutoLinearLayout mLlRecyclerview;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;
    @BindView(R.id.tw_explain_free)
    TextView mTwExplainFree;
    private List<AirPortRequest> mAirportRequests;
    private String idCard;
    private AirportGoInfoRequest mRequest;
    private String status;
    private int seatNum;
    private NumDialog mNumDialog;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerVerifyIdCardComponent
                .builder()
                .appComponent(appComponent)
                .verifyIdCardModule(new VerifyIdCardModule(this)) //请将VerifyIdCardModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_verify_idcard, null, false);
    }

    @Override
    protected void initData() {
        initTitle();
        Bundle bundle = getIntent().getExtras();
        seatNum = bundle.getInt(Constant.PEOPLE_NUM,0);
        status = bundle.getString(Constant.STATUS);
        mRequest = (AirportGoInfoRequest) bundle.getSerializable(Constant.AIRPORT_GO_INFO);
        mNumDialog = new NumDialog(this, getNumsData(), this, Constant.AIRPORT_TYPE_SEAT);
        initEmptyData();
        setSeat(seatNum);
        String text = "<font color='#9C9C9C'>输入</font>"+"<font color='#C3362E'>乘机人</font>"+
                "<font color='#9C9C9C'>证件号,验证是否符合免费接送条件</font>";
        mTwExplainFree.setText(Html.fromHtml(text));
    }

    public void initTitle(){
        setTitle(getString(R.string.travel_free_verify));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setSeat(int position){
        mPwSeatIdCard.setTextInfo("需要" + (position) + "个座位");
        seatNum = position;
        clearMoreData(position);
        if(mRequest != null && mRequest.getFlightNo() != null) {
            if (mRequest.getFlightNo().substring(0, 2).equalsIgnoreCase(Constant.SC_AIRPORT)) {
                List<AirPortRequest> list = new ArrayList<>();
                for (int i = 0; i < position; i++) {
                    list.add(mAirportRequests.get(i));
                }
                addLinearLayout(position);
                mPwSeatIdCard.setLineVisiable(false);
            }
        }
    }

    public void addLinearLayout(int position) {
//        llCertification
        mLlRecyclerview.removeAllViews();
        for (int i = 0; i < position; i++) {
            MyAirportHolder holder = new MyAirportHolder();
            View view = LayoutInflater.from(this).inflate(R.layout.include_public_view, null, false);
            holder.mIvLogo = (ImageView) view.findViewById(R.id.iv_logo); //右边的图标
            holder.mLineEdt = view.findViewById(R.id.line_edt); //底下的线
            holder.mIvStatus = (ImageView) view.findViewById(R.id.iv_status); //右边的图标  占时不用;
            holder.mEdtContent = (EditText) view.findViewById(R.id.edt_content); // 文本框
            holder.mTvCode = (TextView) view.findViewById(R.id.tv_code);  //本人身份证
            holder.mPwLl = (AutoLinearLayout) view.findViewById(R.id.pw_ll); //整个布局
            holder.mIwDelete = (ImageView) view.findViewById(R.id.iw_delete);
            holder.mIwCancel  = (ImageView) view.findViewById(R.id.iw_cancel_port);
            if (mAirportRequests.get(i).getStatus() == Constant.AIRPORT_SUCCESS) {
                setSuccess(holder);
            } else if (mAirportRequests.get(i).getStatus() == Constant.AIRPORT_FAILURE) {
                setFailure(holder);
            } else {
                setNomal(holder);
            }
            if (i == position - 1) {
                holder.mLineEdt.setVisibility(View.GONE);
            }
            setAirportData(holder, mAirportRequests.get(i));
            setlistener(holder, i);
            mLlRecyclerview.addView(view);
        }
    }

    public AirportGoInfoRequest setPassengers(AirportGoInfoRequest mInfo,
                              List<AirPortRequest> mRequests,int num){
        mInfo.setSeatNum(num);
        List<PassengersRequest> list = new ArrayList<>();
        mInfo.setPassengers(list);
        for(int i = 0; i< num;i++){
            PassengersRequest request1 = new PassengersRequest();
            if(mRequests.get(i) != null  &&
                    !TextUtils.isEmpty(mRequests.get(i).getIdCard())) {
                request1.setIDCardNo(mRequests.get(i).getIdCard().toUpperCase());
                list.add(request1);
            }else{
                mRequests.get(i).setStatus(Constant.AIRPORT_NO);
            }
        }
        if(list.size() > 0){
            mInfo.setPassengers(list);
        }
        return mInfo;
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

    @Override
    public void setAirportGoInfoRequest(AirportGoInfoRequest info) {//提交信息价格
        if(status != null && status.equals(Constant.CLEAR_PORT)) { //送机
            EventBus.getDefault().post(info, EventBusTags.ID_CARD);
            finish();
        }else if(status != null && status.equals(Constant.ENTER_PORT)){
            EventBus.getDefault().post(info, EventBusTags.ID_CARD_ON);
            finish();
        }
    }

    @Override
    public void setHasBookInfo(final AirportGoInfoRequest info) {//验证是否有乘坐资格
        if(info!= null ) {
            setSeatNum(info);
        }
        /*if(mRequest != null) {
            mPresenter.getAirportInfo(setPassengers(mRequest, mAirportRequests, seatNum));
        }*/

    }

    public void setisFreeInfo(AirportGoInfoRequest info){
        String text = "";
        int num = 0;
        for(int i = 0; i < info.getPassengers().size(); i++){
            if(info.getPassengers().get(i).isIsValid() && !info.getPassengers().get(i).isHasBooked()){
                num++;
            }
        }
        if(num == seatNum){
            text = "符合免费乘坐资格，可免费乘坐。";
        }else if(num == 0){
            text = "不符合免费乘坐资格，需付费乘坐。";
        }else{
            text = num+"人符合免费乘坐资格，其他人需付费乘坐。";
        }
        AlertDialog dialog = new AlertDialog(this);
        dialog.builder().setTitle("温馨提示").setMsg(text)
                .setPositiveButton("下一步", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mRequest != null) {
                            mPresenter.getAirportInfo(setPassengers(mRequest, mAirportRequests, seatNum));
                        }
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();

    }

    @Override
    public void getItem(int position, int type) {
            mNumDialog.dismiss();
            seatNum = position+1;
       /* if(mRequest != null) {
            setRepeatIdCard(mAirportRequests, setPassengers(mRequest, mAirportRequests, seatNum));
        }*/
//            mPresenter.getAirportInfo(getAirPortInfo());
            setSeat(seatNum);
    }

    public void setRepeatIdCard(List<AirPortRequest> requests,AirportGoInfoRequest info){
        if(getRepeatIDCard(requests)) {
            showIDCardDialog();
        }else{
            mPresenter.getHasBookInfo(info);
        }
    }

    public void showIDCardDialog(){
        AlertDialog dialog = new AlertDialog(this);
        dialog.builder().setTitle("温馨提示").setMsg(getString(R.string.repeat_idcard))
                .setOneButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    public boolean getRepeatIDCard(List<AirPortRequest> requests){
        for(int i = 0; i < requests.size(); i++){
            for(int j = i+1; j < requests.size(); j++){
                if(!TextUtils.isEmpty(requests.get(i).getIdCard()) &&
                        !TextUtils.isEmpty(requests.get(j).getIdCard())){
                    if(requests.get(i).getIdCard().equals(requests.get(j).getIdCard())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void isEmptyPassenger(){
        if(mRequest != null){
            if(setPassengers(mRequest, mAirportRequests, seatNum).getPassengers() != null
                    && setPassengers(mRequest,mAirportRequests,seatNum).getPassengers().size() > 0){
                setRepeatIdCard(mAirportRequests,setPassengers(mRequest, mAirportRequests, seatNum));
            }else{
                showMessage("请输入证件号验证");
            }
        }
    }

    public class MyAirportHolder {
        public ImageView mIvLogo;
        public View mLineEdt;
        public ImageView mIvStatus;
        public EditText mEdtContent;
        public TextView mTvCode;
        public AutoLinearLayout mPwLl;
        public ImageView mIwDelete;
        public ImageView mIwCancel;
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
                mAirportRequests.get(position).setStatus(Constant.AIRPORT_NO);
                setNomal(holder);
                if(isValid()){
                    if(idCard != null && editable.toString().equals(idCard)){
                        holder.mTvCode.setVisibility(View.VISIBLE);
                    }else{
                        holder.mTvCode.setVisibility(View.GONE);
                    }
                }else{
                    holder.mTvCode.setVisibility(View.GONE);
                }

            }
        });

        holder.mIwCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mEdtContent.setText("");
                mAirportRequests.get(position).setStatus(Constant.AIRPORT_NO);
                mAirportRequests.get(position).setIdCard("");
//                holder.mIwCancel.setVisibility(View.GONE);
                setNomal(holder);
            }
        });

        holder.mIwDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mEdtContent.setText("");
                mAirportRequests.get(position).setStatus(Constant.AIRPORT_NO);
                mAirportRequests.get(position).setIdCard("");
                setNomal(holder);
//                setRepeatIdCard(mPassengersRequests,getAirPortInfo());
              /*  if(mRequest != null) {
                    mPresenter.getHasBookInfo(setPassengers(mRequest, mAirportRequests, seatNum));
                }*/
            }
        });
    }

    //正常状况
    public void setNomal(MyAirportHolder holder) {
        if(!TextUtils.isEmpty(holder.mEdtContent.getText().toString().trim())){
            holder.mIwCancel.setVisibility(View.VISIBLE);
        }else{
            holder.mIwCancel.setVisibility(View.GONE);
        }
        holder.mIvLogo.setImageResource(R.mipmap.ic_validate);
        holder.mIwDelete.setVisibility(View.GONE);
        holder.mEdtContent.setEnabled(true);
    }

    //成功状态
    public void setSuccess(MyAirportHolder holder) {
        holder.mIvLogo.setImageResource(R.mipmap.ic_success_id_card);
        holder.mIwDelete.setVisibility(View.VISIBLE);
        holder.mEdtContent.setEnabled(false);
        holder.mIwCancel.setVisibility(View.GONE);
    }

    //失败状态
    public void setFailure(MyAirportHolder holder) {
        holder.mIvLogo.setImageResource(R.mipmap.ic_failure_id_card);
        holder.mIwDelete.setVisibility(View.GONE);
        holder.mEdtContent.setEnabled(true);
        holder.mIwCancel.setVisibility(View.VISIBLE);
    }

    public void initEmptyData() {
        mAirportRequests = new ArrayList<>();
        if(mRequest != null && mRequest.getPassengers() != null && mRequest.getPassengers().size() > 0) {
            for(int  i = 0 ; i < Constant.SEAT_NUM ; i++){
                AirPortRequest request = new AirPortRequest();
                if(mRequest.getPassengers().size() > i){
                    request.setIdCard(mRequest.getPassengers().get(i).getIDCardNo()!= null ?
                    mRequest.getPassengers().get(i).getIDCardNo():"");
                }else{
                }
                request.setStatus(Constant.AIRPORT_NO);
                mAirportRequests.add(request);
            }
        }else{
            for (int i = 0; i < Constant.SEAT_NUM; i++) {
                AirPortRequest request = new AirPortRequest();
                if (isValid() && i == 0 && idCard != null) {
//                request.setStatus(Constant.AIRPORT_SUCCESS);
                    request.setIdCard(idCard);

                } else {
                }
                request.setStatus(Constant.AIRPORT_NO);
                mAirportRequests.add(request);
            }
        }
    }

    public boolean isValid(){
        if(DataHelper.getDeviceData(this,Constant.LOGIN) != null) {
            LoginEntity response = DataHelper.getDeviceData(this, Constant.LOGIN);
            if(response.getIDCard() != null){
                idCard = response.getIDCard();
            }
            return response.isRealValid();
        }else{
            return false;
        }
    }

    //设置数据
    public void setAirportData(MyAirportHolder holder, AirPortRequest request) {
        if(isValid() && request != null && request.getIdCard() != null && request.getIdCard().equalsIgnoreCase(idCard)){
            holder.mEdtContent.setText(request.getIdCard());
            holder.mTvCode.setVisibility(View.VISIBLE);
        }else{
            holder.mTvCode.setVisibility(View.GONE);
            holder.mEdtContent.setText(request.getIdCard());
        }
        if (request.getStatus() == Constant.AIRPORT_SUCCESS) {
            setSuccess(holder);
        } else if (request.getStatus() == Constant.AIRPORT_FAILURE) {
            setFailure(holder);
        } else {
            setNomal(holder);
        }
    }

    public List<String> getNumsData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Constant.SEAT_NUM; i++) {
            list.add(i + 1 + "个座位");
        }
        return list;
    }

    @OnClick({R.id.tv_submit,R.id.pw_seat_id_card,R.id.ll_state_id_card})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_submit:
                if(mRequest != null) {
                    isEmptyPassenger();
//                    mPresenter.getHasBookInfo(setPassengers(mRequest, mAirportRequests, seatNum));
                }
            break;
            case R.id.pw_seat_id_card:
                mNumDialog.show();
                break;
            case R.id.ll_state_id_card:
                explainFree();
                break;
        }
    }

    public  void explainFree(){
        Intent intent2 = new Intent(this, WebViewActivity.class);
        intent2.putExtra(Constant.TITLE,getResources().getString(R.string.travel_explain_detail));
        intent2.putExtra(Constant.URL, Api.PHONE_INTRODUCE);
        startActivity(intent2);
    }

    public void setSeatNum(AirportGoInfoRequest info) {
        //seatNum 座位数
        boolean flag = false;
        if(info.getPassengers() != null && info.getPassengers().size() > 0 ) {
            List<PassengersRequest> list = info.getPassengers();
            StringBuilder prompt = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                for (int j = 0; j < mAirportRequests.size(); j++) {
                    if (list.get(i).getIDCardNo() != null && list.get(i).getIDCardNo().equals(mAirportRequests.get(j).getIdCard())) {
                        if (list.get(i).isIsValid() && !list.get(i).isHasBooked()) {
                            mAirportRequests.get(j).setStatus(Constant.AIRPORT_SUCCESS);
                        } /*else if (list.get(i).isIsValid() && list.get(i).isHasBooked()) {
                            mAirportRequests.get(j).setStatus(Constant.AIRPORT_FAILURE);
                            //弹出dialog
                            flag = true;
                            prompt.append(list.get(i).getIDCardNo()).append(",");
                        }*/ else {
                            mAirportRequests.get(j).setStatus(Constant.AIRPORT_FAILURE);
                        }
                    }
                }
            }
            setSeat(seatNum);
            if (flag) {
                showDialog(prompt.toString().substring(0, prompt.length() - 1));
            } else {
                setisFreeInfo(info);
            }
        }
    }

    public void showDialog(String msg){
        AlertDialog dialog = new AlertDialog(this);
        dialog.builder().setTitle("温馨提示").setMsg(msg +"已经预约此航班")
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        
                    }
                })
                .setPositiveButton("下一步", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mRequest != null) {
                            mPresenter.getAirportInfo(setPassengers(mRequest, mAirportRequests, seatNum));
                        }
                    }
                }).show();
    }

    public void clearMoreData(int position) {
        for (int i = position ; i < Constant.SEAT_NUM; i++) {
            mAirportRequests.get(i).setIdCard("");
            mAirportRequests.get(i).setStatus(Constant.AIRPORT_NO);
        }
    }

}
