package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.common.WEApplication;
import com.ironaviation.traveller.di.component.my.travel.DaggerTravelComponent;
import com.ironaviation.traveller.di.module.my.travel.TravelModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelContract;
import com.ironaviation.traveller.mvp.model.entity.response.RouteItemResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteListResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelResponse;
import com.ironaviation.traveller.mvp.presenter.my.travel.TravelPresenter;
import com.ironaviation.traveller.mvp.ui.my.EstimateActivity;
import com.ironaviation.traveller.mvp.ui.my.adapter.TravelAdapter;
import com.ironaviation.traveller.mvp.ui.payment.InvalidationActivity;
import com.ironaviation.traveller.mvp.ui.payment.WaitingPaymentActivity;
import com.jess.arms.utils.UiUtils;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

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
 * <p>
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/26 17:29
 * 修改人：
 * 修改时间：2017/3/26 17:29
 * 修改备注：
 */

public class TravelActivity extends WEActivity<TravelPresenter> implements TravelContract.View, SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener{

    @BindView(R.id.sl_travel)
    SwipeRefreshLayout mSlTravel;
    @BindView(R.id.rv_travel)
    RecyclerView mRvTravel;
    private int defaultIndex = 1;

    private TravelAdapter mTravelAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<RouteItemResponse> mRouteItemResponses;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerTravelComponent
                .builder()
                .appComponent(appComponent)
                .travelModule(new TravelModule(this)) //请将TravelModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected void nodataRefresh() {
        mPresenter.getTravelData(defaultIndex);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_travel,null,false);
    }

    @Override
    protected void initData() {
        /*showNodata(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showNodata(false);
            }
        }, 300);*/
        setTitle(getString(R.string.travel_detail_title));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setToolbarColor(R.color.base_color);
        mLayoutManager = new LinearLayoutManager(this);
        mRvTravel.setLayoutManager(mLayoutManager);
        mTravelAdapter = new TravelAdapter(R.layout.item_travel);

        mSlTravel.setColorSchemeColors(ContextCompat.getColor(WEApplication.getContext(), R.color.colorPrimaryDark),
                ContextCompat.getColor(WEApplication.getContext(), R.color.colorPrimaryDark));
        mSlTravel.setOnRefreshListener(this);
        mRvTravel.setAdapter(mTravelAdapter);
//        mTravelAdapter.setNewData(getList());
        mTravelAdapter.setOnLoadMoreListener(this,mRvTravel);
        mTravelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if(mRouteItemResponses != null){
                    mPresenter.getRouteState(mRouteItemResponses.get(position).getBID());
                    /*setStatus(mRouteItemResponses.get(position).getStatus()
                            ,mRouteItemResponses.get(position).getBID());*/
                }
            }
        });
        mPresenter.getTravelData(defaultIndex);
    }

    public void setStatus(String status,RouteStateResponse responses){
        if(Constant.REGISTERED .equals(status)){
            setTravelDetailsActivity(responses);
        }else if(Constant.INHAND .equals(status)){
            setTravelDetailsActivity(responses);
        }else if(Constant.ARRIVED .equals(status)){
            setTravelDetailsActivity(responses);
        }else if(Constant.CANCEL .equals(status)){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STATUS,responses);
            intent.putExtra(Constant.STATUS,bundle);
            startActivity(CancelSuccessActivity.class,bundle);
        }else if(Constant.BOOKSUCCESS .equals(status)){
            setTravelDetailsActivity(responses);
        }else if(Constant.COMPLETED .equals(status)){
            /*Intent intent = new Intent();
            intent.putExtra(Constant.STATUS,status);
            startActivity(TravelDetailsActivity.class);*/
        }else if(Constant.WAIT_APPRAISE .equals(status)){
            Intent intent = new Intent(this,EstimateActivity.class);
            intent.putExtra(Constant.STATUS,status);
            startActivity(intent);
        }else if(Constant.NOTPAID .equals(status)){ //跳未支付界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STATUS,responses);
            startActivity(WaitingPaymentActivity.class,bundle);
        }else if(Constant.INVALIDATION .equals(status) ){ //跳失效界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STATUS,responses);
            startActivity(InvalidationActivity.class,bundle);
        }
    }

    public void clearPort(String status,RouteStateResponse responses){ // 送机
        if(Constant.REGISTERED .equals(status)){
            setTravelDetailsActivity(responses);
        }else if(Constant.INHAND .equals(status)){
            setTravelDetailsActivity(responses);
        }else if(Constant.ARRIVED .equals(status)){
            setTravelDetailsActivity(responses);
        }else if(Constant.CANCEL .equals(status)){
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STATUS,responses);
            bundle.putString(Constant.CHILD_STATUS,Constant.OFF);
            startActivity(CancelSuccessActivity.class,bundle);
        }else if(Constant.BOOKSUCCESS .equals(status)){
            setTravelDetailsActivity(responses);
        }else if(Constant.COMPLETED .equals(status)){
            /*Intent intent = new Intent();
            intent.putExtra(Constant.STATUS,status);
            startActivity(TravelDetailsActivity.class);*/
        }else if(Constant.WAIT_APPRAISE .equals(status)){  //等待评价
            Intent intent = new Intent(this,EstimateActivity.class);
            intent.putExtra(Constant.STATUS,status);
            startActivity(intent);
        }else if(Constant.NOTPAID .equals(status)){ //跳未支付界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STATUS,responses);
            bundle.putString(Constant.CHILD_STATUS,Constant.OFF);
            startActivity(WaitingPaymentActivity.class,bundle);
        }else if(Constant.INVALIDATION .equals(status) ){ //跳失效界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STATUS,responses);
            startActivity(InvalidationActivity.class,bundle);
        }
    }

    public void enterPort(String status,RouteStateResponse responses){ // 接机
        if(Constant.REGISTERED .equals(status)){
            setTravelDetailsOnActivity(responses);
        }else if(Constant.INHAND .equals(status)){
            setTravelDetailsOnActivity(responses);
        }else if(Constant.ARRIVED .equals(status)){
            setTravelDetailsOnActivity(responses);
        }else if(Constant.CANCEL .equals(status)){
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STATUS,responses);
            bundle.putString(Constant.CHILD_STATUS,Constant.ON);
            startActivity(CancelSuccessActivity.class,bundle);
        }else if(Constant.BOOKSUCCESS .equals(status)){
            setTravelDetailsOnActivity(responses);
        }else if(Constant.COMPLETED .equals(status)){
            /*Intent intent = new Intent();
            intent.putExtra(Constant.STATUS,status);
            startActivity(TravelDetailsActivity.class);*/
        }else if(Constant.WAIT_APPRAISE .equals(status)){  //等待评价
            Intent intent = new Intent(this,EstimateActivity.class);
            intent.putExtra(Constant.STATUS,status);
            startActivity(intent);
        }else if(Constant.NOTPAID .equals(status)){ //跳未支付界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STATUS,responses);
            bundle.putString(Constant.CHILD_STATUS,Constant.ON);
            startActivity(WaitingPaymentActivity.class,bundle);
        }else if(Constant.INVALIDATION .equals(status) ){ //跳失效界面
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.STATUS,responses);
            startActivity(InvalidationActivity.class,bundle);
        }
    }

    public void setTravelDetailsActivity(RouteStateResponse responses){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.STATUS,responses);
        startActivity(TravelDetailsActivity.class,bundle);
    }

    public void setTravelDetailsOnActivity(RouteStateResponse responses){
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.STATUS,responses);
        startActivity(TravelDetailsOnActivity.class,bundle);
    }

    public List<TravelResponse> getList() {
        List<TravelResponse> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new TravelResponse());
        }
        return list;
    }

    @Override
    public void showLoading() {
//        showProgressDialog();
    }

    @Override
    public void hideLoading() {
//        dismissProgressDialog();
        mNodataSwipeRefresh.setRefreshing(false);
        mSlTravel.setRefreshing(false);
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
    public void setDatas(RouteListResponse responses) {
        showNodata(false);
        showError(false);
        mTravelAdapter.setNewData(responses.getItems());
        mRouteItemResponses = mTravelAdapter.getData();
    }

    @Override
    public void setMoreDatas(RouteListResponse responses) {
        mTravelAdapter.addData(responses.getItems());
        mRouteItemResponses = mTravelAdapter.getData();
    }

    @Override
    public void setNodata() {
        showNodata(true);
    }

    @Override
    public void setError() {
        showError(true);
    }

    @Override
    public void setRouteStateResponse(RouteStateResponse responses) {
        if(responses.getTripType().equals(Constant.CLEAR_PORT)) {//送机
            clearPort(responses.getStatus(),responses);
        }else if(responses.getTripType().equals(Constant.ENTER_PORT)){
            enterPort(responses.getStatus(),responses);
        }
//        setStatus(responses.getStatus(),responses);
    }

    @Override
    public void getState(RouteStateResponse routeStateResponse) {
        if(routeStateResponse.getTripType().equals(Constant.CLEAR_PORT)) {//送机
            clearPort(routeStateResponse.getStatus(),routeStateResponse);
        }else if(routeStateResponse.getTripType().equals(Constant.ENTER_PORT)){
            enterPort(routeStateResponse.getStatus(),routeStateResponse);
        }
//        setStatus(routeStateResponse.getStatus(),routeStateResponse);
    }

    @Override
    public void setMoreComplete() {
        mTravelAdapter.loadMoreComplete();
    }

    @Override
    public void onRefresh() {
//      mPresenter.getTravelData();//刷新数据
        mPresenter.getTravelData(defaultIndex);
    }

    @Override
    public void onLoadMoreRequested() {
        mPresenter.getTravelDataMore(mPresenter.getPage());
    }

    @Subscriber(tag = EventBusTags.PAYMENT)
    public void refresh(String bid){
        mPresenter.getTravelData(defaultIndex);
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }

    @Override
    public void dismissDialog() {
        mNodataSwipeRefresh.setRefreshing(false);
        mSlTravel.setRefreshing(false);
        dismissProgressDialog();
    }

    @Override
    public void setNoMore() {
        mTravelAdapter.loadMoreEnd(true);
    }
}
