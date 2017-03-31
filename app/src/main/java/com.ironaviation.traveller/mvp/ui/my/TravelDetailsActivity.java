package com.ironaviation.traveller.mvp.ui.my;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.DaggerTravelDetailsComponent;
import com.ironaviation.traveller.di.module.my.TravelDetailsModule;
import com.ironaviation.traveller.event.TravelCancelEvent;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.TravelDetailsContract;
import com.ironaviation.traveller.mvp.presenter.my.TravelDetailsPresenter;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.ironaviation.traveller.mvp.ui.widget.MoreActionPopupWindow;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.tw_name)
    TextView mTwName;
    @BindView(R.id.tw_score)
    TextView mTwScore;
    @BindView(R.id.tw_car_num)
    TextView mTwCarNum;
    @BindView(R.id.yw_car_color)
    TextView mYwCarColor;
    @BindView(R.id.iw_mobile)
    ImageView mIwMobile;
    @BindView(R.id.ll_driver_info)
    AutoLinearLayout mLlDriverInfo;
    @BindView(R.id.ll_complete)
    AutoLinearLayout mLlComplete;
    @BindView(R.id.ll_ordering)
    AutoLinearLayout mLlOrdering;
    @BindView(R.id.rl_go_to_pay)
    TextView mRlGoToPay;
    @BindView(R.id.ll_going)
    AutoLinearLayout mLlGoing;
    private MoreActionPopupWindow mPopupWindow;

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
//        setRightFunction(R.mipmap.ic_id_card, this);
//        mPopupWindow = new MoreActionPopupWindow(this);
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
                    mPopupWindow.showPopupWindow(mToolbar);
                }
                break;
        }
    }

    public void onEventMainThread(TravelCancelEvent event) {
        switch (event.getEvent()) {
            case Constant.TRAVEL_CANCEL:
                showMessage("取消");
                break;
            case Constant.TRAVEL_CUSTOMER:
                showMessage("联系客户");
                break;
        }
    }
}
