package com.ironaviation.traveller.mvp.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.payment.DaggerPaymentComponent;
import com.ironaviation.traveller.di.module.payment.PaymentModule;
import com.ironaviation.traveller.mvp.contract.payment.PaymentContract;
import com.ironaviation.traveller.mvp.presenter.payment.PaymentPresenter;
import com.ironaviation.traveller.mvp.ui.widget.ImageTextImageView;
import com.jess.arms.utils.UiUtils;

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
 * 创建时间：2017-03-28 15:58
 * 修改人：starRing
 * 修改时间：2017-03-28 15:58
 * 修改备注：
 */
public class PaymentActivity extends WEActivity<PaymentPresenter> implements PaymentContract.View {

    @BindView(R.id.ivi_we_chat)
    ImageTextImageView mIviWeChat;
    @BindView(R.id.ivi_ali_pay)
    ImageTextImageView mIviAliPay;
    @BindView(R.id.ivi_union_pay)
    ImageTextImageView mIviUnionPay;
    @BindView(R.id.rl_go_to_pay)
    TextView mRlGoToPay;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerPaymentComponent
                .builder()
                .appComponent(appComponent)
                .paymentModule(new PaymentModule(this)) //请将PaymentModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_payment, null, false);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.payment));
        setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

    @OnClick({R.id.ivi_we_chat, R.id.ivi_ali_pay, R.id.ivi_union_pay,R.id.rl_go_to_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivi_we_chat:
               /* mIviWeChat.setGoOn(R.mipmap.ic_radio_button_checked_black);
                mIviAliPay.setGoOn(R.mipmap.ic_radio_button_unchecked_black);
                mIviUnionPay.setGoOn(R.mipmap.ic_radio_button_unchecked_black);*/
                mIviWeChat.show(true,R.mipmap.ic_pay_select);
                mIviAliPay.show(false,R.mipmap.ic_pay_select);
                mIviUnionPay.show(false,R.mipmap.ic_pay_select);
                mIviAliPay.setBackGround(ContextCompat.getColor(this, R.color.white));
                mIviWeChat.setBackGround(ContextCompat.getColor(this, R.color.id_cord_background));
                mIviUnionPay.setBackGround(ContextCompat.getColor(this, R.color.white));
                break;
            case R.id.ivi_ali_pay:
                /*mIviAliPay.setGoOn(R.mipmap.ic_radio_button_checked_black);
                mIviWeChat.setGoOn(R.mipmap.ic_radio_button_unchecked_black);
                mIviUnionPay.setGoOn(R.mipmap.ic_radio_button_unchecked_black);*/
                mIviAliPay.show(true,R.mipmap.ic_pay_select);
                mIviWeChat.show(false,R.mipmap.ic_pay_select);
                mIviUnionPay.show(false,R.mipmap.ic_pay_select);
                mIviAliPay.setBackGround(ContextCompat.getColor(this, R.color.id_cord_background));
                mIviWeChat.setBackGround(ContextCompat.getColor(this, R.color.white));
                mIviUnionPay.setBackGround(ContextCompat.getColor(this, R.color.white));
                break;
            case R.id.ivi_union_pay:
                /*mIviUnionPay.setGoOn(R.mipmap.ic_radio_button_checked_black);
                mIviWeChat.setGoOn(R.mipmap.ic_radio_button_unchecked_black);
                mIviAliPay.setGoOn(R.mipmap.ic_radio_button_unchecked_black);*/
                mIviUnionPay.show(true,R.mipmap.ic_pay_select);
                mIviWeChat.show(false,R.mipmap.ic_pay_select);
                mIviAliPay.show(false,R.mipmap.ic_pay_select);
                mIviAliPay.setBackGround(ContextCompat.getColor(this, R.color.white));
                mIviWeChat.setBackGround(ContextCompat.getColor(this, R.color.white));
                mIviUnionPay.setBackGround(ContextCompat.getColor(this, R.color.id_cord_background));
                break;
            case R.id.rl_go_to_pay:

                break;
        }
    }


}
