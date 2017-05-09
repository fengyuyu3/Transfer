package com.ironaviation.traveller.mvp.ui.my.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.auth.APAuthInfo;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.PriceUtil;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.ui.webview.WebViewActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
import com.ironaviation.traveller.mvp.ui.widget.FontTextView;
import com.zhy.autolayout.AutoLinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Administrator on 2017/4/26.
 */

public class PaymentDetailsActivity extends WEActivity {

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
    @BindView(R.id.travel_price_fixed_price)
    TextView mTravelPriceFixedPrice;
    @BindView(R.id.tw_fixed_price_details)
    TextView mTwFixedPriceDetails;
    @BindView(R.id.tw_acturl_price)
    FontTextView mTwActurlPrice;
    @BindView(R.id.tw_free)
    TextView mTwFree;
    @BindView(R.id.tw_free_price)
    TextView mTwFreePrice;
    @BindView(R.id.tw_coupon)
    TextView mTwCoupon;
    @BindView(R.id.tw_coupon_detail)
    TextView mTwCouponDetail;
    @BindView(R.id.tw_payment)
    TextView mTwPayment;
    @BindView(R.id.ll_payment)
    AutoLinearLayout mLlPayment;
    @BindView(R.id.tw_price_rule)
    TextView mTwPriceRule;
    private double price;
    private double acturlPrice;
    private double myPrice;
    private int num;
    private int seatNum;
    private String status;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_price_detail, null, false);
    }

    @Override
    protected void initData() {
        initTitle();
        Intent intent = getIntent();
        price = intent.getDoubleExtra(Constant.REAL_PRICE, 0);
        acturlPrice = intent.getDoubleExtra(Constant.FIXED_PRICE, 0);
        num = intent.getIntExtra(Constant.FREE_PASSENGER, 0);
        status = intent.getStringExtra(Constant.PAYMENT);
        myPrice = intent.getDoubleExtra(Constant.FREE_PASSENGER_PRICE, 0);
        seatNum = intent.getIntExtra(Constant.PEOPLE_NUM,0);
        setData();
    }

    public void setData() {
        mTwActurlPrice.setTextType(PriceUtil.getPrecent(acturlPrice)+"");
        mTravelPriceFixedPrice.setText(String.format(getResources().getString(R.string.travel_price_fixed_price),seatNum));
        mTwFixedPriceDetails.setText(PriceUtil.getPrecent(price)+"元");
        mTwFree.setText(String.format(getResources().getString(R.string.travel_price_free),num));
        mTwFreePrice.setText("-"+PriceUtil.getPrecent(myPrice)+"元");
        mTwCouponDetail.setText("-"+PriceUtil.getPrecent(price-acturlPrice-myPrice)+"元");
        if(status.equals(Constant.PAYMENT_NOMAL)){
            mLlPayment.setVisibility(View.INVISIBLE);
        }else{
            mLlPayment.setVisibility(View.VISIBLE);
            mTwPayment.setText(status);
        }
    }

    public void initTitle(){
        setTitle(getString(R.string.travel_price_detail));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void nodataRefresh() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.tw_price_rule)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tw_price_rule:
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra(Constant.TITLE,getResources().getString(R.string.travel_price_rule));
                intent.putExtra(Constant.URL, Api.PHONE_PRICE_ROLE);
                startActivity(intent);
            break;
        }
    }
}
