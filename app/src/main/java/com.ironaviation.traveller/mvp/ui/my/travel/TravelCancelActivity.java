package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.travel.DaggerTravelCancelComponent;
import com.ironaviation.traveller.di.module.my.travel.TravelCancelModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelCancelContract;
import com.ironaviation.traveller.mvp.model.entity.response.TravelCancelReason;
import com.ironaviation.traveller.mvp.presenter.my.travel.TravelCancelPresenter;
import com.jess.arms.utils.UiUtils;
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
    private String[] cancel_reasons;
    private RecyclerView.LayoutManager mLayoutManager;

    private TravelCancelAdapter mTravelCancelAdapter;
    private String bid;

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
        cancel_reasons = getResources().getStringArray(R.array.cancel_reason_list);


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

        mPresenter.getCancelBookInfo(bid);
        mLayoutManager = new LinearLayoutManager(this);
        mTravelCancelAdapter = new TravelCancelAdapter(R.layout.item_travel_cancel);
        mRvCancelTravelReason.setLayoutManager(mLayoutManager);

        mRvCancelTravelReason.setAdapter(mTravelCancelAdapter);
        mTravelCancelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                TravelCancelReason mTravelCancelResponse = (TravelCancelReason) adapter.getData().get(position);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tv_determine_cancel)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_determine_cancel:
                startActivity(CancelSuccessActivity.class);
                break;
        }
    }

    @Override
    public void setFreeView(boolean IsFreeCancel, double CancelPrice) {
        if (!IsFreeCancel) {
            mRlFreeView.setVisibility(View.VISIBLE);
            mTvMoney.setText(CancelPrice + "");
        } else {
            mRlFreeView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setReasonView(List<TravelCancelReason> strings) {
        mTravelCancelAdapter.setNewData(strings);

    }

    static class ViewHolder {
        @BindView(R.id.rv_cancel_travel_reason)
        RecyclerView mRvCancelTravelReason;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
