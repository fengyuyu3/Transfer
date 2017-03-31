package com.ironaviation.traveller.mvp.ui.airportoff;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEFragment;
import com.ironaviation.traveller.di.component.airportoff.DaggerAirPortOffComponent;
import com.ironaviation.traveller.di.module.airportoff.AirPortOffModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.airportoff.AirPortOffContract;
import com.ironaviation.traveller.mvp.contract.airportoff.TravelFloatContract;
import com.ironaviation.traveller.mvp.model.entity.request.AirPortRequest;
import com.ironaviation.traveller.mvp.presenter.airportoff.AirPortOffPresenter;
import com.ironaviation.traveller.mvp.ui.manager.FullyLinearLayoutManager;
import com.ironaviation.traveller.mvp.ui.my.TravelDetailsActivity;
import com.ironaviation.traveller.mvp.ui.widget.NumDialog;
import com.ironaviation.traveller.mvp.ui.widget.PublicTextView;
import com.ironaviation.traveller.mvp.ui.widget.PublicView;
import com.ironaviation.traveller.mvp.ui.widget.TravelPopupwindow;
import com.jess.arms.utils.UiUtils;

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
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/27 18:15
 * 修改人：
 * 修改时间：2017/3/27 18:15
 * 修改备注：
 */

public class AirPortOffFragment extends WEFragment<AirPortOffPresenter> implements AirPortOffContract.View
        , NumDialog.CallBackItem, AirPortAdapter.ItemIdCallBack,AirPortAdapter.ItemIdStatusCallBack {

    @BindView(R.id.pw_person)
    PublicTextView mPwPerson;
    @BindView(R.id.pw_flt_no)
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
    Unbinder unbinder;
    /*@BindView(R.id.pw_id_card)
    PublicView mPwIdCard;*/

    private NumDialog mNumDialog,mTerminal;
    private AirPortAdapter mAirPortAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TravelPopupwindow mTravelPopupwindow;
    private static final int NUM = 6;
    private List<AirPortRequest> list;

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
        mLayoutManager = new FullyLinearLayoutManager(getActivity());
        rwAirport.setNestedScrollingEnabled(false);
        mAirPortAdapter = new AirPortAdapter(this,this);
        mNumDialog = new NumDialog(getActivity(), getNumsData(), this,Constant.AIRPORT_TYPE_SEAT);
        mTerminal = new NumDialog(getActivity(),getTerminal(),this,Constant.AIRPORT_TYPE_TERMINAL);
        rwAirport.setLayoutManager(mLayoutManager);
        rwAirport.setAdapter(mAirPortAdapter);
//        setRidTimeHide();
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


    @OnClick({R.id.pw_seat,R.id.pw_airport,R.id.pw_flt_no})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pw_seat:
                mNumDialog.show();
                break;
            case R.id.pw_airport:
                mTerminal.show();
                break;
            case R.id.pw_flt_no:
                Intent intent = new Intent(getActivity(), TravelFloatActivity.class);
                startActivity(intent);
//                mTravelPopupwindow.showPopupWindow(mPwPerson);
                break;
        }
    }

    public List<String> getNumsData() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < NUM; i++) {
            list.add(i + 1 + "个座位");
        }
        return list;
    }

    public List<String> getTerminal(){
        List<String> list = new ArrayList<>();
        for(int i = 0 ; i < 2; i++){
            list.add("T"+(i+1)+"航站楼");
        }
        return list;
    }

    @Override
    public void getItem(int position , int type) {
        if(type == Constant.AIRPORT_TYPE_SEAT) {
            mPwSeat.setTextInfo("需要" + (position + 1) + "个座位");
            list = new ArrayList<>();
            for (int i = 0; i < position + 1; i++) {
                AirPortRequest request = new AirPortRequest();
                if (i == 0) {
                    request.setStatus(Constant.AIRPORT_SUCCESS);
                    request.setIdCard("510321198507245790");
                } else {
                    request.setStatus(Constant.AIRPORT_NO);
                }
                list.add(request);
            }
            mAirPortAdapter.setData(list);
            mNumDialog.dismiss();
        }else{
            mPwAirport.setTextInfo("T"+(position+1)+"航站楼");
            mTerminal.dismiss();
        }
    }

    //获取验证身份号码的位置和身份证号码
    @Override
    public void getItem(String cardId, int position) {
        UiUtils.makeText(cardId + " " + position);
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

    public void setRidTimeHide(){
        mPwTime.setVisibility(View.GONE);
        mPwAddress.setVisibility(View.GONE);
        mPwAirport.setVisibility(View.GONE);
        mPwSeat.setVisibility(View.GONE);
    }
    public void setRidTimeShow(){
        mPwTime.setVisibility(View.VISIBLE);
        mPwAddress.setVisibility(View.VISIBLE);
        mPwAirport.setVisibility(View.VISIBLE);
        mPwSeat.setVisibility(View.VISIBLE);
    }

}