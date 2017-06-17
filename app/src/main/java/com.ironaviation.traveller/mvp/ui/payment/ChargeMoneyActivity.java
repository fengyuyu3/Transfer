package com.ironaviation.traveller.mvp.ui.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.payment.DaggerChargeMoneyComponent;
import com.ironaviation.traveller.di.module.payment.ChargeMoneyModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.payment.ChargeMoneyContract;
import com.ironaviation.traveller.mvp.presenter.payment.ChargeMoneyPresenter;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;
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
 * Created by flq on 2017/6/17.
 */

public class ChargeMoneyActivity extends WEActivity<ChargeMoneyPresenter> implements ChargeMoneyContract.View,TextWatcher{

    @BindView(R.id.tw_money)
    EditText twMoney;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.tw_price_1)
    TextView twPrice1;
    @BindView(R.id.tw_price_2)
    TextView twPrice2;
    @BindView(R.id.tw_price_3)
    TextView twPrice3;
    @BindView(R.id.tw_price_4)
    TextView twPrice4;
    @BindView(R.id.tw_price_5)
    TextView twPrice5;
    @BindView(R.id.tw_price_6)
    TextView twPrice6;
    @BindView(R.id.ivi_we_chat)
    ImageTextImageView iviWeChat;
    @BindView(R.id.ivi_ali_pay)
    ImageTextImageView iviAliPay;
    @BindView(R.id.tw_go_to_order)
    TextView twGoToOrder;
    @BindView(R.id.iw_cancel_money)
    ImageView iwCancelMoney;
    private String payStatus;
    String myText = "";

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerChargeMoneyComponent
                .builder()
                .appComponent(appComponent)
                .chargeMoneyModule(new ChargeMoneyModule(this)) //请将ChargeMoneyModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_charge_money, null, false);
    }

    @Override
    protected void initData() {
        iviWeChat.newShow(true);
        iviAliPay.newShow(false);
        payStatus = Constant.WECHAT;
        twMoney.addTextChangedListener(this);
        setSelectText(twPrice1);
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

    @OnClick({R.id.tw_price_1, R.id.tw_price_2, R.id.tw_price_3,
            R.id.tw_price_4, R.id.tw_price_5, R.id.tw_price_6,
            R.id.ivi_ali_pay, R.id.ivi_we_chat,R.id.iw_cancel_money,
            R.id.tw_go_to_order})
    public void onClick(View view) {
        setNomalText();
        switch (view.getId()) {
            case R.id.tw_price_1:
                setSelectText(twPrice1);
                break;
            case R.id.tw_price_2:
                setSelectText(twPrice2);
                break;
            case R.id.tw_price_3:
                setSelectText(twPrice3);
                break;
            case R.id.tw_price_4:
                setSelectText(twPrice4);
                break;
            case R.id.tw_price_5:
                setSelectText(twPrice5);
                break;
            case R.id.tw_price_6:
                setSelectText(twPrice6);
                break;
            case R.id.ivi_we_chat:
                iviWeChat.newShow(true);
                iviAliPay.newShow(false);
                payStatus = Constant.WECHAT;
                break;
            case R.id.ivi_ali_pay:
                iviWeChat.newShow(false);
                iviAliPay.newShow(true);
                payStatus = Constant.ALIPAY;
                break;
            case R.id.iw_cancel_money:
                twMoney.setText("");
                break;
            case R.id.tw_go_to_order:
                String text = twMoney.getText().toString().substring(twMoney.getText().toString().length()-1,
                        twMoney.getText().toString().length());
                if(text.equals("")){
                    text = twMoney.getText().toString().substring(0,twMoney.getText().toString().length()-1);
                }
                break;
        }
    }

    public String isNum(String text){
        String str = "";
        if(text.length() > 0 && text.length() == 1){
            if(text.substring(0,1).equals(".")){
                twMoney.setText("");
                str = "";
            }
        }else {
            if (isTwoPoint(text)) {
                str = myText;
                twMoney.setText(str);
                twMoney.setSelection(str.length());
            }else {
                if (text.substring(0, 1).equals(".")) {
                    str = text.substring(1, text.length());
                    twMoney.setText(str);
                }else{
                    str = text;
                }
            }
        }

        return str;
    }

    public boolean isTwoPoint(String text){
        int num = 0;
        for(int i = 0; i< text.length(); i++){
            if(text.substring(i,i+1).equals(".")){
                num++;
            }
        }
        if(num >= 2){
            return  true;
        }else{
            return  false;
        }
    }


    public void setNomalText() {
//        twPrice1.setTextColor(getResources().getColor(R.color.red_bright));
        twPrice1.setTextColor(getResources().getColor(R.color.color_2b2b2b));
        twPrice2.setTextColor(getResources().getColor(R.color.color_2b2b2b));
        twPrice3.setTextColor(getResources().getColor(R.color.color_2b2b2b));
        twPrice4.setTextColor(getResources().getColor(R.color.color_2b2b2b));
        twPrice5.setTextColor(getResources().getColor(R.color.color_2b2b2b));
        twPrice6.setTextColor(getResources().getColor(R.color.color_2b2b2b));
        twPrice1.setBackgroundResource(R.drawable.white_4_radius_stroke_shap);
        twPrice2.setBackgroundResource(R.drawable.white_4_radius_stroke_shap);
        twPrice3.setBackgroundResource(R.drawable.white_4_radius_stroke_shap);
        twPrice4.setBackgroundResource(R.drawable.white_4_radius_stroke_shap);
        twPrice5.setBackgroundResource(R.drawable.white_4_radius_stroke_shap);
        twPrice6.setBackgroundResource(R.drawable.white_4_radius_stroke_shap);
    }

    public void setSelectText(TextView text) {
        text.setTextColor(getResources().getColor(R.color.red_bright));
        text.setBackgroundResource(R.drawable.red_4_radius_stroke_shap);
        twMoney.setText(text.getText().toString().substring(1,text.getText().length()));
        twMoney.setSelection(text.length()-1);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        /*String text = isNum(s.toString());
        myText = text;*/
        if(s.length() > 0){
            iwCancelMoney.setVisibility(View.VISIBLE);
        }else{
            iwCancelMoney.setVisibility(View.GONE);
        }
    }
}
