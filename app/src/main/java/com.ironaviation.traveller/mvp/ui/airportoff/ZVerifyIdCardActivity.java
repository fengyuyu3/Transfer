package com.ironaviation.traveller.mvp.ui.airportoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.airportoff.DaggerZVerifyIdCardComponent;
import com.ironaviation.traveller.di.module.airportoff.ZVerifyIdCardModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.airportoff.ZVerifyIdCardContract;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.model.entity.request.AirPortRequest;
import com.ironaviation.traveller.mvp.model.entity.request.AirportGoInfoRequest;
import com.ironaviation.traveller.mvp.presenter.airportoff.ZVerifyIdCardPresenter;
import com.jess.arms.utils.DataHelper;
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
 * Created by flq on 2017/6/16.
 */

public class ZVerifyIdCardActivity extends WEActivity<ZVerifyIdCardPresenter> implements ZVerifyIdCardContract.View {

    @BindView(R.id.recycler_view)
    AutoLinearLayout recyclerView;
    @BindView(R.id.tw_free_rule)
    TextView twFreeRule;
    @BindView(R.id.tw_go_to_order)
    TextView twGoToOrder;

    private String idCard;
    private List<AirPortRequest> mAirportRequests;
    private int seatNum = 0;
    private AirportGoInfoRequest mRequest;
    private String status;
    private int maxSize = 5;
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerZVerifyIdCardComponent
                .builder()
                .appComponent(appComponent)
                .zVerifyIdCardModule(new ZVerifyIdCardModule(this)) //请将ZVerifyIdCardModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_z_verify_idcard, null, false);
    }

    @Override
    protected void initData() {
        initTitile();
        Bundle bundle = getIntent().getExtras();
        status = bundle.getString(Constant.STATUS);
        mRequest = (AirportGoInfoRequest) bundle.getSerializable(Constant.AIRPORT_GO_INFO);
        initEmptyData();
        if(mRequest != null && mRequest.getPassengers() != null && mRequest.getPassengers().size() > 0) {
            addLinearLayout(mRequest.getPassengers().size());
        }else{
            addLinearLayout(Constant.DEFULT_SEAT);
        }
    }

    public void initTitile(){
        setTitle(getString(R.string.travel_free_verify_Z));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setRightFunction(ContextCompat.getDrawable(this, R.mipmap.ic_add), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLinearOnelayout();
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

    public void initEmptyData() {
        mAirportRequests = new ArrayList<>();
        if(mRequest != null && mRequest.getPassengers() != null && mRequest.getPassengers().size() > 0) {
            for(int  i = 0 ; i < mRequest.getPassengers().size() ; i++){
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
            for (int i = 0; i < Constant.DEFULT_SEAT; i++) {
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

    public void addLinearLayout(int position) {

        if(position < maxSize) {
            recyclerView.removeAllViews();
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
                holder.mIwCancel = (ImageView) view.findViewById(R.id.iw_cancel_port);
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
                recyclerView.addView(view);
            }
        }else{
            showMessage("超出最大座位数!");
        }
    }

    public void addLinearOnelayout(){
        AirPortRequest request = new AirPortRequest();
        request.setStatus(Constant.AIRPORT_NO);
        mAirportRequests.add(request);
        addLinearLayout(mAirportRequests.size());
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
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

    @OnClick({R.id.tw_go_to_order})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tw_go_to_order:
                showMessage(mAirportRequests.toString());
                break;
        }
    }
}
