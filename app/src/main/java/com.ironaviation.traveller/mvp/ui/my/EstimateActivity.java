package com.ironaviation.traveller.mvp.ui.my;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.zxing.oned.rss.RSS14Reader;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.CommonUtil;
import com.ironaviation.traveller.app.utils.PriceUtil;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.DaggerEstimateComponent;
import com.ironaviation.traveller.di.module.my.EstimateModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.EstimateContract;
import com.ironaviation.traveller.mvp.model.entity.response.CommentTag;
import com.ironaviation.traveller.mvp.model.entity.response.CommentsResponse;
import com.ironaviation.traveller.mvp.model.entity.response.EstimateResponse;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.presenter.my.EstimatePresenter;
import com.ironaviation.traveller.mvp.ui.my.adapter.EstimateAdapter;
import com.ironaviation.traveller.mvp.ui.my.travel.PaymentDetailsActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.ironaviation.traveller.mvp.ui.widget.CustomerRatingBar;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLinearLayout;

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
    @BindView(R.id.iv_function_left)
    ImageView mIvFunctionLeft;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_function_right)
    TextView mTvFunctionRight;
    @BindView(R.id.iv_function_right)
    ImageView mIvFunctionRight;
    @BindView(R.id.toolbar)
    AutoToolbar mToolbar;
    @BindView(R.id.iv_head)
    ImageView mIvHead;
    @BindView(R.id.it_driver_name)
    TextView mItDriverName;
    @BindView(R.id.ll_name)
    AutoLinearLayout mLlName;
    @BindView(R.id.tv_final_payment)
    TextView mTvFinalPayment;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_unit)
    TextView mTvUnit;
    @BindView(R.id.tw_car_num)
    TextView mTwCarNum;
    @BindView(R.id.et_other_estimate)
    EditText mEtOtherEstimate;

    private EstimateAdapter mEstimateAdapter;
    private RouteStateResponse responses;

    private String[] great_satisfaction_reasons;
    private String[] insufficient_reasons;
    private List<EstimateResponse> greatSatisfactionReasonList = new ArrayList<>();
    private List<EstimateResponse> insufficientReasonList = new ArrayList<>();
    private List<CommentTag> commentShowTags = new ArrayList<>();
    private List<CommentTag> commentDataTags = new ArrayList<>();
    private int rate;

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
        getInitData();


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
//                showDialog();
            if(responses != null){
                CommonUtil.call(EstimateActivity.this,responses.getDriverPhone() != null ? responses.getDriverPhone():Constant.CONNECTION_US);
            }else{
                showMessage(getString(R.string.get_num_failed));
            }
            }
        });
        mRatingBar.setOnRatingChangeListener(new CustomerRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(int RatingCount) {
                setListView(RatingCount + 1);
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
                CommentTag estimateResponse = (CommentTag) adapter.getData().get(position);
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
        if(responses != null) {
            if (!responses.isComment()) {
                mPresenter.getCommentTagInfo();
            } else {
                setAlreadyComment(responses);
            }
        }
    }

    public void getInitData() {
        Bundle bundle = getIntent().getExtras();
        responses = (RouteStateResponse) bundle.getSerializable(Constant.STATUS);
        if(responses!=null) {
            mItDriverName.setText(responses.getDriverName() != null ? responses.getDriverName() : "");
            mItDriverGrade.setText(responses.getDriverRate() != null ? responses.getDriverRate() : "");
            mTwCarNum.setText(responses.getCarLicense() != null ? responses.getCarLicense() : "");
            mTvMoney.setText(PriceUtil.getPrecent(responses.getTotalPrice()));
        }
    }

    public void setPaymentDetail(){
        Intent intent = new Intent(this, PaymentDetailsActivity.class);
        intent.putExtra(Constant.REAL_PRICE,responses.getTotalPrice());
        intent.putExtra(Constant.FIXED_PRICE,responses.getActualPrice());
        int num = 0;
        double myPrice = 0;
        for(int i = 0; i < responses.getPassengers().size(); i++){
            if(responses.getPassengers().get(i).isIsValid() && !responses.getPassengers().get(i).isHasBooked()){
                num++;
                myPrice = myPrice + responses.getPassengers().get(i).getPrice();
            }
        }
        intent.putExtra(Constant.PEOPLE_NUM,responses.getPassengers().size());
        intent.putExtra(Constant.FREE_PASSENGER,num);
        intent.putExtra(Constant.FREE_PASSENGER_PRICE,myPrice);
        if(responses.getExt() != null && responses.getExt().size() > 0){
            for(int i = 0; i < responses.getExt().size(); i++){
                if(responses.getExt().get(i).getName().equals(Constant.PAYMETHOD)){
                    String payment = responses.getExt().get(i).getJsonData();
                    if(Constant.WECHAT.equals(payment)) {
                        intent.putExtra(Constant.PAYMENT, Constant.PAY_WECHAT);
                    }else if(Constant.ALIPAY.equals(payment)){
                        intent.putExtra(Constant.PAYMENT,Constant.PAY_ALIPAY);
                    }else{
                        intent.putExtra(Constant.PAYMENT,Constant.PAYMENT_NOMAL);
                    }
                }
            }
        }

        startActivity(intent);
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

    @Override
    public void setList(List<CommentTag> list) {

        commentDataTags = list;
        setListView(5);
        mRatingBar.setStar(5);

    }

    @Override
    public void isSuccess() { //成功做什么

    }

    @Override
    public void setListView(int count) {
        rate = count;
        commentShowTags = new ArrayList<>();
        for (int i = 0; i < commentDataTags.size(); i++) {
            if (commentDataTags.get(i).getRate() == count) {
                commentShowTags.add(commentDataTags.get(i));
            }

        }
        mEstimateAdapter.setNewData(commentShowTags);
    }

    @Override
    public void AlreadyComment() {


    }

    @Override
    public String getOtherReason() {
        return mEtOtherEstimate.getText().toString();
    }

    @Override
    public int getRate() {
        return rate;
    }

    @Override
    public List<String> getTagIds() {
        List<String> tagids = new ArrayList<>();
        for (int i = 0; i < commentShowTags.size(); i++) {
            if (commentShowTags.get(i).isType()) {
                tagids.add(commentShowTags.get(i).getCTID());
            }

        }
        return tagids;
    }

    @Override
    public RouteStateResponse getRouteStateResponse() {
        return responses;
    }

    @Override
    public void setAlreadyComment(RouteStateResponse responses) {
        for (int i = 0; i < responses.getExt().size(); i++) {
            if (responses.getExt().get(i).getName().equals("Comments")) {
                CommentsResponse commentsResponse = new Gson().fromJson(responses.getExt().get(i).getJsonData(), CommentsResponse.class);
                mTvAnonymousEvaluation.setVisibility(View.INVISIBLE);
                mRatingBar.setmClickable(false);
                mRatingBar.setStar(commentsResponse.getRate());
                if (TextUtils.isEmpty(commentsResponse.getNotes())) {
                    mEtOtherEstimate.setVisibility(View.INVISIBLE);
                } else {
                    mEtOtherEstimate.setEnabled(false);
                    mEtOtherEstimate.setText(commentsResponse.getNotes());
                }
                mEstimateAdapter.setNewData(commentsResponse.getTags());
            }
        }

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


    @OnClick({R.id.tv_anonymous_evaluation,R.id.tv_money})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_anonymous_evaluation:
                mPresenter.isCommentSuccess(mPresenter.getCommentsInfo());
                break;
            case R.id.tv_money:
                setPaymentDetail();
                break;
        }
    }


}
