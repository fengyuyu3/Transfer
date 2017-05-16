package com.ironaviation.traveller.mvp.ui.my;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.DaggerMessageComponent;
import com.ironaviation.traveller.di.module.my.MessageModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.MessageContract;
import com.ironaviation.traveller.mvp.model.entity.response.MessageResponse;
import com.ironaviation.traveller.mvp.presenter.my.MessagePresenter;
import com.ironaviation.traveller.mvp.ui.my.adapter.MessageAdapter;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoRelativeLayout;

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
 * 项目名称：Traveller
 * 类描述：   消息页面
 * 创建人：starRing
 * 创建时间：2017-03-27 10:12
 * 修改人：starRing
 * 修改时间：2017-03-27 10:12
 * 修改备注：
 */
public class MessageActivity extends WEActivity<MessagePresenter> implements MessageContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_message)
    RecyclerView mRvMessage;
    @BindView(R.id.sl_message)
    SwipeRefreshLayout mSlMessage;
    @BindView(R.id.nodata)
    AutoRelativeLayout mNodata;
    @BindView(R.id.error)
    AutoRelativeLayout mError;
    @BindView(R.id.tw_reset_network)
    TextView mTwResetNetWork;
    @BindView(R.id.tw_nodata)
    TextView mTwNodata;
    @BindView(R.id.iw_nodata)
    ImageView mIwNodata;
    private MessageAdapter mMessageAdapter;

    int pageIndex=1;
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMessageComponent
                .builder()
                .appComponent(appComponent)
                .messageModule(new MessageModule(this)) //请将MessageModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected void nodataRefresh() {
        showMessage("没有数据");
        mNodataSwipeRefresh.setRefreshing(false);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_message, null, false);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.my_message));
        setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRvMessage.setLayoutManager(new LinearLayoutManager(this));
        mMessageAdapter = new MessageAdapter(R.layout.item_message);
        mMessageAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);

        mRvMessage.setAdapter(mMessageAdapter);
        mSlMessage.setOnRefreshListener(this);
        mPresenter.getFirstMessageData(pageIndex,true);
        mMessageAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                mPresenter.getMessageData(mPresenter.getPage());
            }
        }, mRvMessage);

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
    public void setDatas(MessageResponse mTravelResponses) {
        mMessageAdapter.setNewData(mTravelResponses.getItems());
        mMessageAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void addDatas(MessageResponse mTravelResponses) {
        mMessageAdapter.addData(mTravelResponses.getItems());
        mMessageAdapter.disableLoadMoreIfNotFullPage();
    }

    @Override
    public void loadMoreFail() {
        mMessageAdapter.loadMoreFail();
    }


    @Override
    public void setNodata() {
//        showNodata(true);
        mRvMessage.setVisibility(View.GONE);
        mNodata.setVisibility(View.VISIBLE);
        mIwNodata.setImageResource(R.mipmap.ic_nodata_img);
        mTwNodata.setText(getResources().getString(R.string.no_data_message));
        mError.setVisibility(View.GONE);
    }

    @Override
    public void setError() {
//        showError(true);
        mRvMessage.setVisibility(View.GONE);
        mNodata.setVisibility(View.GONE);
        mError.setVisibility(View.VISIBLE);
    }

    @Override
    public void setData(){
        mRvMessage.setVisibility(View.VISIBLE);
        mNodata.setVisibility(View.GONE);
        mError.setVisibility(View.GONE);
    }

    @Override
    public void showDialog() {
        showProgressDialog();
    }

    @Override
    public void dismissDialog() {
        dismissProgressDialog();
    }

    @Override
    public void stopRefreshing() {
        mSlMessage.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPresenter.getFirstMessageData(pageIndex,false);
    }

    @Override
    public void setNoMore() {
        mMessageAdapter.loadMoreEnd(true);
    }

    @Override
    public void setMoreComplete() {
        mMessageAdapter.loadMoreComplete();
    }
}
