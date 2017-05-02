package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.travel.DaggerTravelCancelComponent;
import com.ironaviation.traveller.di.module.my.travel.TravelCancelModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelCancelContract;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.model.entity.response.CancelBookingInfo;
import com.ironaviation.traveller.mvp.model.entity.response.TravelCancelReason;
import com.ironaviation.traveller.mvp.presenter.my.travel.TravelCancelPresenter;
import com.ironaviation.traveller.mvp.ui.my.adapter.TravelCancelAdapter;
import com.ironaviation.traveller.mvp.ui.webview.WebViewActivity;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.Subscriber;

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
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-05 14:25
 * 修改人：starRing
 * 修改时间：2017-04-05 14:25
 * 修改备注：
 */
public class TravelCancelActivity extends WEActivity<TravelCancelPresenter> implements TravelCancelContract.View {


    @BindView(R.id.rv_cancel_travel_reason)
    RecyclerView mRvCancelTravelReason;
    @BindView(R.id.tv_determine_cancel)
    TextView mTvDetermineCancel;
    @BindView(R.id.rl_free_view)
    AutoRelativeLayout mRlFreeView;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.et_other_reason)
    EditText mEtOtherReason;
    @BindView(R.id.tv_free_hint)
    TextView mTvFreeHint;
    @BindView(R.id.tw_cancel_rules)
    TextView mTwCancelRules;
    private String[] cancel_reasons;
    private RecyclerView.LayoutManager mLayoutManager;

    private TravelCancelAdapter mTravelCancelAdapter;
    private String bid;
    private String status;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerTravelCancelComponent
                .builder()
                .appComponent(appComponent)
                .travelCancelModule(new TravelCancelModule(this)) //请将TravelCancelModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_travel_cancel, null, false);
    }

    @Override
    protected void initData() {

        setTitle(getString(R.string.travel_cancel));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle bundle = getIntent().getExtras();
        bid = bundle.getString(Constant.BID);

        if (bid != null) {
            mPresenter.getCancelBookInfo(bid);
            if(bundle.getString(Constant.STATUS) != null){
                status = bundle.getString(Constant.STATUS);
            }
        }
        mLayoutManager = new LinearLayoutManager(this);
        mTravelCancelAdapter = new TravelCancelAdapter(R.layout.item_travel_cancel);
        mRvCancelTravelReason.setLayoutManager(mLayoutManager);

        mRvCancelTravelReason.setAdapter(mTravelCancelAdapter);
        mTravelCancelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CancelBookingInfo.Reasons mTravelCancelResponse = (CancelBookingInfo.Reasons) adapter.getData().get(position);
                if (mTravelCancelResponse.isType()) {
                    mTravelCancelResponse.setType(false);

                } else {
                    mTravelCancelResponse.setType(true);
                }
                mTravelCancelAdapter.notifyDataSetChanged();
            }
        });
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

    @OnClick({R.id.tv_determine_cancel,R.id.tw_cancel_rules})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_determine_cancel:
                mPresenter.cancelBook(bid, mTravelCancelAdapter.getData(), getOtherReason());
                //startActivity(CancelSuccessActivity.class);
                break;
            case R.id.tw_cancel_rules:
                Intent intent = new Intent(this, WebViewActivity.class);
                if(status.equals(Constant.CLEAR_PORT)) { //送机
                    intent.putExtra(Constant.TITLE,getResources().getString(R.string.travel_enter_port));
                    intent.putExtra(Constant.URL, Api.PHONE_CANCEL_ROLE_OFF);
                }else if(status.equals(Constant.ENTER_PORT)){ //接机
                    intent.putExtra(Constant.TITLE,getResources().getString(R.string.travel_clear_port));
                    intent.putExtra(Constant.URL, Api.PHONE_CANCEL_ROLE_ON);
                }
                startActivity(intent);
                break;
        }
    }

    @Override
    public void setFreeView(boolean IsFreeCancel, double CancelPrice) {
        if (!IsFreeCancel) {
            mRlFreeView.setVisibility(View.VISIBLE);
            mTvMoney.setText(CancelPrice + "");
            mTvFreeHint.setText(getString(R.string.cancel_travel_cost_hint_1) + CancelPrice + getString(R.string.cancel_travel_cost_hint_2));
        } else {
            mRlFreeView.setVisibility(View.GONE);
        }

    }

    @Override
    public void setReasonView(List<CancelBookingInfo.Reasons> strings) {
        mTravelCancelAdapter.setNewData(strings);

    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public String getOtherReason() {
        return mEtOtherReason.getText().toString();
    }

    static class ViewHolder {
        @BindView(R.id.rv_cancel_travel_reason)
        RecyclerView mRvCancelTravelReason;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
    @Subscriber(tag = EventBusTags.ROUTE_CANCEL) //送机
    public void routeCancel(){
        mPresenter.getCancelBookInfo(bid);
    }
}
