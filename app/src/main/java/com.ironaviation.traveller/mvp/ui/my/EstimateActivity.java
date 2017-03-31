package com.ironaviation.traveller.mvp.ui.my;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.DaggerEstimateComponent;
import com.ironaviation.traveller.di.module.my.EstimateModule;
import com.ironaviation.traveller.mvp.contract.my.EstimateContract;
import com.ironaviation.traveller.mvp.model.entity.response.EstimateResponse;
import com.ironaviation.traveller.mvp.presenter.my.EstimatePresenter;
import com.ironaviation.traveller.mvp.ui.widget.CustomerRatingBar;
import com.jess.arms.utils.UiUtils;

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
 * 创建时间：2017-03-29 19:49
 * 修改人：starRing
 * 修改时间：2017-03-29 19:49
 * 修改备注：
 */
public class EstimateActivity extends WEActivity<EstimatePresenter> implements EstimateContract.View {


    @BindView(R.id.it_driver_grade)
    TextView mItDriverGrade;
    @BindView(R.id.rating_bar)
    CustomerRatingBar mRatingBar;
    @BindView(R.id.rv_evaluation_content)
    RecyclerView mRvEvaluationContent;
    @BindView(R.id.tv_anonymous_evaluation)
    TextView mTvAnonymousEvaluation;

    private EstimateAdapter mEstimateAdapter;

    private String[] great_satisfaction_reasons;
    private String[] insufficient_reasons;
    private List<EstimateResponse> greatSatisfactionReasonList = new ArrayList<>();
    private List<EstimateResponse> insufficientReasonList = new ArrayList<>();

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerEstimateComponent
                .builder()
                .appComponent(appComponent)
                .estimateModule(new EstimateModule(this)) //请将EstimateModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_estimate, null, false);
    }

    @Override
    protected void initData() {
        great_satisfaction_reasons = getResources().getStringArray(R.array.great_satisfaction_reason_list);
        insufficient_reasons = getResources().getStringArray(R.array.insufficient_reason_list);
        for (int i = 0; i < insufficient_reasons.length; i++) {
            insufficientReasonList.add(new EstimateResponse(insufficient_reasons[i]));
        }
        for (int i = 0; i < great_satisfaction_reasons.length; i++) {
            greatSatisfactionReasonList.add(new EstimateResponse(great_satisfaction_reasons[i]));
        }
        setTitle(getString(R.string.estimate));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setRightFunction(ContextCompat.getDrawable(this, R.mipmap.ic_phone_write), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        mRatingBar.setOnRatingChangeListener(new CustomerRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(int RatingCount) {

                if (RatingCount >= 3) {

                    mEstimateAdapter.setNewData(greatSatisfactionReasonList);
                } else {
                    mEstimateAdapter.setNewData(insufficientReasonList);
                }
            }
        });
        //GridLayout 3列
        GridLayoutManager mgr = new GridLayoutManager(this, 2);
        mRvEvaluationContent.setLayoutManager(mgr);
        mEstimateAdapter = new EstimateAdapter(R.layout.item_estimate);
        mRvEvaluationContent.setAdapter(mEstimateAdapter);
        mRvEvaluationContent.addItemDecoration(new SpaceItemDecoration(20));
        mEstimateAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                EstimateResponse estimateResponse = (EstimateResponse) adapter.getData().get(position);
                if (estimateResponse.isType()) {
                    estimateResponse.setType(false);
                    view.setBackgroundResource(R.drawable.btn_write_un_select);
                } else {
                    estimateResponse.setType(true);
                    view.setBackgroundResource(R.drawable.btn_grey_select);

                }
                return false;
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

    /**
     * 这是兼容的 AlertDialog
     */
    private void showDialog() {
  /*
  这里使用了 android.support.v7.app.AlertDialog.Builder
  可以直接在头部写 import android.support.v7.app.AlertDialog
  那么下面就可以写成 AlertDialog.Builder
  */
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Material Design Dialog");
        builder.setMessage("这是 android.support.v7.app.AlertDialog 中的样式");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", null);
        builder.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {


            if (parent.getChildLayoutPosition(view) % 2 == 0) {

                outRect.right = space;
            } else {
                outRect.left = space;

            }
        }
    }


    @OnClick({R.id.tv_anonymous_evaluation})
    public void onClick(View view) {

    }
}
