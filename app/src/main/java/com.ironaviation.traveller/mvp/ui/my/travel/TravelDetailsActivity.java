package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.travel.DaggerTravelDetailsComponent;
import com.ironaviation.traveller.di.module.my.travel.TravelDetailsModule;
import com.ironaviation.traveller.event.TravelCancelEvent;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelDetailsContract;
import com.ironaviation.traveller.mvp.presenter.my.travel.TravelDetailsPresenter;
import com.ironaviation.traveller.mvp.ui.widget.MoreActionPopupWindow;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;


import org.simple.eventbus.Subscriber;

import butterknife.BindView;
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
 * 创建时间：2017/3/29 15:41
 * 修改人：
 * 修改时间：2017/3/29 15:41
 * 修改备注：
 */

public class TravelDetailsActivity extends WEActivity<TravelDetailsPresenter> implements TravelDetailsContract.View, View.OnClickListener {

    @BindView(R.id.tw_name) //司机名字
            TextView mTwName;
    @BindView(R.id.tw_score) //分数
            TextView mTwScore;
    @BindView(R.id.tw_car_num) //车牌号码
            TextView mTwCarNum;
    @BindView(R.id.yw_car_color) //汽车颜色
            TextView mYwCarColor;
    @BindView(R.id.iw_mobile) //打电话
            ImageView mIwMobile;
    @BindView(R.id.ll_driver_info)
    AutoLinearLayout mLlDriverInfo; //司机信息
    @BindView(R.id.ll_complete)
    AutoLinearLayout mLlComplete; //派单成功
    @BindView(R.id.ll_ordering)
    AutoLinearLayout mLlOrdering; //派单中
    @BindView(R.id.rl_go_to_pay)
    TextView mRlGoToPay;  //确认到达按钮
    @BindView(R.id.ll_going)
    AutoLinearLayout mLlGoing; //派单进行中
    @BindView(R.id.id_line)
    View idLine;
    @BindView(R.id.ll_arrive)
    AutoLinearLayout mLlArrive; // 确认到达
    @BindView(R.id.tw_order_info)
    TextView mTwOrderInfo; //派单中标题
    @BindView(R.id.iw_zoom)
    ImageView mIwZoom;
    @BindView(R.id.iw_zoom_nomal)
    ImageView mIwZoomNomal;
    private MoreActionPopupWindow mPopupWindow;
    private int status;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerTravelDetailsComponent
                .builder()
                .appComponent(appComponent)
                .travelDetailsModule(new TravelDetailsModule(this)) //请将TravelDetailsModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_travel_details, null, false);
    }

    @Override
    protected void initData() {
        setRightFunction(R.mipmap.ic_airport, this);
        mPopupWindow = new MoreActionPopupWindow(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_function_right:
                if (mPopupWindow != null) {
                    mPopupWindow.showPopupWindow(idLine);
                }
                break;
        }
    }

    @OnClick({R.id.iw_zoom,R.id.iw_zoom_nomal})
    public void myOnClick(View view){
        switch(view.getId()){
            case R.id.iw_zoom:
                AllGone();
            break;
            case R.id.iw_zoom_nomal:
                showStatus(status);
                break;
        }
    }

    public void showStatus(int status){
        switch (status){
            case Constant.TRAVEL_DETAILS_GOING:
                going();
                break;
            case Constant.TRAVEL_DETAILS_COMPLETE:
                complete();
                break;
            case Constant.TRAVEL_DETAILS_ORDER:
                order();
                break;
            case Constant.TRAVEL_DETAILS_ARRIVE:
                arrive();
                break;
        }
    }

    @Subscriber(tag = EventBusTags.TRAVEL_DETAILS)
    public void onEventMainThread(TravelCancelEvent event) {
        Log.e("kkk", event.getEvent() + "");
        switch (event.getEvent()) {
            case Constant.TRAVEL_CANCEL:
                startActivity(TravelCancelActivity.class);
                break;
            case Constant.TRAVEL_CUSTOMER:
                showMessage("联系客户");
                break;
        }
    }


    public void going() {  //派单进行中
        mLlGoing.setVisibility(View.VISIBLE); //正在进行中
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlComplete.setVisibility(View.GONE);//派单成功
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlArrive.setVisibility(View.GONE);  // 确认到达a
        status = Constant.TRAVEL_DETAILS_GOING;
    }

    public void complete() { //派单成功
        mLlComplete.setVisibility(View.VISIBLE);//派单成功
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mLlGoing.setVisibility(View.GONE); //正在进行中
        status = Constant.TRAVEL_DETAILS_COMPLETE;
    }

    public void order() { //派单中
        mLlOrdering.setVisibility(View.VISIBLE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
        mLlDriverInfo.setVisibility(View.GONE); //司机信息
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mLlGoing.setVisibility(View.GONE); //正在进行中
        status = Constant.TRAVEL_DETAILS_ORDER;
    }

    public void arrive() { //确认到达
        mLlArrive.setVisibility(View.VISIBLE);  // 确认到达
        mLlDriverInfo.setVisibility(View.VISIBLE); //司机信息
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
        mLlGoing.setVisibility(View.GONE); //正在进行中
        status = Constant.TRAVEL_DETAILS_ARRIVE;
    }

    public void AllGone(){
        mLlArrive.setVisibility(View.GONE);  // 确认到达
        mLlOrdering.setVisibility(View.GONE);//派单中
        mLlComplete.setVisibility(View.GONE);//派单成功
        mLlGoing.setVisibility(View.GONE); //正在进行中
    }
}
