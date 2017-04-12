package com.ironaviation.traveller.mvp.ui.airporton;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.CheckIdCardUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEFragment;
import com.ironaviation.traveller.di.component.airporton.DaggerAirPortOnComponent;
import com.ironaviation.traveller.di.module.airporton.AirPortOnModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.airporton.AirPortOnContract;
import com.ironaviation.traveller.mvp.model.entity.request.AirPortRequest;
import com.ironaviation.traveller.mvp.presenter.airporton.AirPortOnPresenter;
import com.ironaviation.traveller.mvp.ui.airportoff.TravelFloatActivity;
import com.ironaviation.traveller.mvp.ui.widget.MyTimeDialog;
import com.ironaviation.traveller.mvp.ui.widget.NumDialog;
import com.ironaviation.traveller.mvp.ui.widget.PublicTextView;
import com.ironaviation.traveller.mvp.ui.widget.TerminalPopupWindow;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

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
 * Created by Administrator on 2017/4/10 0010.
 */

public class AirPortOnFragment extends WEFragment<AirPortOnPresenter> implements AirPortOnContract.View
        ,NumDialog.CallBackItem,TerminalPopupWindow.CallBackItem{


    @BindView(R.id.pw_person_on)
    PublicTextView mPwPerson;
    @BindView(R.id.pw_flt_no_on)
    PublicTextView mPwFltNo;
    @BindView(R.id.pw_airport_on)
    PublicTextView mPwAirport;
    @BindView(R.id.pw_address_on)
    PublicTextView mPwAddress;
    @BindView(R.id.pw_seat_on)
    PublicTextView mPwSeat;
    @BindView(R.id.rw_airport_on)
    RecyclerView mRwAirport;
    @BindView(R.id.ll_certification_on)
    AutoLinearLayout llCertification;
    @BindView(R.id.ll_state_on)
    AutoRelativeLayout mLlState;
    @BindView(R.id.tw_best_price_on)
    TextView mTwBestPrice;
    @BindView(R.id.tw_original_price_on)
    TextView mTwOriginalPrice;
    @BindView(R.id.tw_go_to_order_on)
    TextView mTwGoToOrder;
    @BindView(R.id.ll_price_on)
    AutoLinearLayout mLlPrice;
    @BindView(R.id.ll_book_on)
    AutoLinearLayout mLlBook;
    private NumDialog mNumDialog;
    private List<AirPortRequest> mAirportRequests;
    private TerminalPopupWindow mTerminalPopupWindow;
    private int terminalNum = -1;
    private MyTimeDialog mMyTimeDialog;

    public static AirPortOnFragment newInstance() {
        AirPortOnFragment fragment = new AirPortOnFragment();
        return fragment;
    }

    @Override
    protected void setupFragmentComponent(AppComponent appComponent) {
        DaggerAirPortOnComponent
                .builder()
                .appComponent(appComponent)
                .airPortOnModule(new AirPortOnModule(this))//请将AirPortOnModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_airporton,container, false);
    }

    @Override
    protected void initData() {
       /* initEmptyData();
        mNumDialog = new NumDialog(getActivity(), getNumsData(), this, Constant.AIRPORT_TYPE_SEAT);
        mMyTimeDialog = new MyTimeDialog(getActivity());*/
    }

    public void initEmptyData() {
        mAirportRequests = new ArrayList<>();
        for (int i = 0; i < Constant.SEAT_NUM; i++) {
            AirPortRequest request = new AirPortRequest();
            request.setStatus(Constant.AIRPORT_NO);
            mAirportRequests.add(request);
        }
    }

    public void clearMoreData(int position) {
        for (int i = position + 1; i < Constant.SEAT_NUM; i++) {
            mAirportRequests.get(i).setIdCard("");
            mAirportRequests.get(i).setStatus(Constant.AIRPORT_NO);
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

    public List<String> getNumsData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < Constant.SEAT_NUM; i++) {
            list.add(i + 1 + "个座位");
        }
        return list;
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
    //获取航站楼位置
    @Override
    public void getItem(int position) {
        terminalNum = position;
        mPwAirport.setTextInfo("成都双流国际机场T" + (position + 1) + "航站楼");
        mTerminalPopupWindow.dismiss();
    }

    @Override
    public void getItem(int position, int type) {
        if (type == Constant.AIRPORT_TYPE_SEAT) {
            mPwSeat.setTextInfo("需要" + (position + 1) + "个座位");
            clearMoreData(position);
            List<AirPortRequest> list = new ArrayList<>();
            for (int i = 0; i < position; i++) {
                list.add(mAirportRequests.get(i));
            }
            addLinearLayout(position);
//            mAirPortAdapter.setData(list);
            mNumDialog.dismiss();
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

    @OnClick({R.id.pw_seat_on, R.id.pw_airport_on})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pw_seat_on:
                mNumDialog.show();
                break;
            case R.id.pw_airport:
                if(mTerminalPopupWindow != null){
                    mTerminalPopupWindow.setNum(terminalNum);
                    mTerminalPopupWindow.show(mPwSeat);
                }
//                mTerminal.show();
                break;
           /* case R.id.pw_time:
                mMyTimeDialog.showDialog("test");
                break;
            case R.id.pw_flt_no:
                Intent intent = new Intent(getActivity(), TravelFloatActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.left_in_alpha, R.anim.right_out_alpha);
//                mTravelPopupwindow.showPopupWindow(mPwPerson);
                break;*/
        }
    }
}