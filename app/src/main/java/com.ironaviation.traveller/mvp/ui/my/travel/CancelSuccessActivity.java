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

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.travel.DaggerCancelSuccessComponent;
import com.ironaviation.traveller.di.module.my.travel.CancelSuccessModule;
import com.ironaviation.traveller.mvp.contract.my.travel.CancelSuccessContract;
import com.ironaviation.traveller.mvp.model.entity.response.CancelSuccessResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelCancelReason;
import com.ironaviation.traveller.mvp.presenter.my.travel.CancelSuccessPresenter;
import com.ironaviation.traveller.mvp.ui.my.EstimateActivity;
import com.ironaviation.traveller.mvp.ui.widget.SpaceItemDecoration;
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
 * 创建时间：2017-04-05 14:29
 * 修改人：starRing
 * 修改时间：2017-04-05 14:29
 * 修改备注：
 */
public class CancelSuccessActivity extends WEActivity<CancelSuccessPresenter> implements CancelSuccessContract.View {


    @BindView(R.id.rv_cancel_success)
    RecyclerView mRvCancelSuccess;
    private String[] cancel_reasons;
    private List<CancelSuccessResponse> mTravelCancelResponseList = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;

    private CancelSuccessAdapter mCancelSuccessAdapter;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerCancelSuccessComponent
                .builder()
                .appComponent(appComponent)
                .cancelSuccessModule(new CancelSuccessModule(this)) //请将CancelSuccessModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_cancel_success, null, false);
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
        //GridLayout 3列
        GridLayoutManager mgr = new GridLayoutManager(this, 2);
        mRvCancelSuccess.addItemDecoration(new SpaceItemDecoration(20));
        mCancelSuccessAdapter = new CancelSuccessAdapter(R.layout.item_cancel_success);
        mRvCancelSuccess.setLayoutManager(mgr);
        mRvCancelSuccess.setAdapter(mCancelSuccessAdapter);

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

    @Override
    public void setResponsibilityView(boolean flag, double CancelPrice) {

    }

    @Override
    public void setReasonView(List<TravelCancelReason> strings) {
        mCancelSuccessAdapter.setNewData(strings);

    }
}
