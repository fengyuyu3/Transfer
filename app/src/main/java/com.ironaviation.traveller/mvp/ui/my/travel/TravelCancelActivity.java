package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.travel.DaggerTravelCancelComponent;
import com.ironaviation.traveller.di.module.my.travel.TravelCancelModule;
import com.ironaviation.traveller.mvp.contract.my.travel.TravelCancelContract;
import com.ironaviation.traveller.mvp.model.entity.response.EstimateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelCancelResponse;
import com.ironaviation.traveller.mvp.presenter.my.travel.TravelCancelPresenter;
import com.ironaviation.traveller.mvp.ui.manager.FullyLinearLayoutManager;
import com.ironaviation.traveller.mvp.ui.my.EstimateActivity;
import com.ironaviation.traveller.mvp.ui.my.EstimateAdapter;
import com.ironaviation.traveller.mvp.ui.widget.CustomerRatingBar;
import com.jess.arms.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

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
    private String[] cancel_reasons;
    private List<TravelCancelResponse> mTravelCancelResponseList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private TravelCancelAdapter mTravelCancelAdapter;
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

        for (int i = 0; i < cancel_reasons.length; i++) {
            mTravelCancelResponseList.add(new TravelCancelResponse(cancel_reasons[i]));
        }
        setTitle(getString(R.string.travel_cancel));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        mLayoutManager = new LinearLayoutManager(this);
        mTravelCancelAdapter=new TravelCancelAdapter(R.layout.item_travel_cancel);
        mRvCancelTravelReason.setLayoutManager(mLayoutManager);
        mTravelCancelAdapter.setNewData(mTravelCancelResponseList);
        mTravelCancelAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TravelCancelResponse mTravelCancelResponse = (TravelCancelResponse) adapter.getData().get(position);
                if (mTravelCancelResponse.isType()) {
                    mTravelCancelResponse.setType(false);
                } else {
                    mTravelCancelResponse.setType(true);
                }
                mTravelCancelAdapter.notifyDataSetChanged();
                return false;
            }
        });
        mRvCancelTravelReason.setAdapter(mTravelCancelAdapter);

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

    static class ViewHolder {
        @BindView(R.id.rv_cancel_travel_reason)
        RecyclerView mRvCancelTravelReason;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
