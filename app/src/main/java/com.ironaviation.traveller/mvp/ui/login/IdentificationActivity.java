package com.ironaviation.traveller.mvp.ui.login;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.login.DaggerIdentificationComponent;
import com.ironaviation.traveller.di.module.login.IdentificationModule;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.login.IdentificationContract;
import com.ironaviation.traveller.mvp.model.api.Api;
import com.ironaviation.traveller.mvp.presenter.Login.IdentificationPresenter;
import com.ironaviation.traveller.mvp.ui.main.MainActivity;
import com.ironaviation.traveller.mvp.ui.webview.WebViewActivity;
import com.ironaviation.traveller.mvp.ui.widget.AlertDialog;
import com.jess.arms.utils.UiUtils;
import com.zhy.autolayout.AutoLayoutInfo;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoLayoutHelper;

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
 * 创建时间：2017-03-26 10:03
 * 修改人：starRing
 * 修改时间：2017-03-26 10:03
 * 修改备注：
 */
public class IdentificationActivity extends WEActivity<IdentificationPresenter> implements IdentificationContract.View,View.OnFocusChangeListener {

    @BindView(R.id.rl_identification)
    RelativeLayout rl_identification;
    @BindView(R.id.rl_id_cord)
    AutoRelativeLayout mRlIdCord;
    @BindView(R.id.rl_name)
    RelativeLayout mRlName;
    @BindView(R.id.rl_id_numeral)
    RelativeLayout mRlIdNumeral;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_numeral)
    EditText mEtNumeral;

    private String status;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerIdentificationComponent
                .builder()
                .appComponent(appComponent)
                .identificationModule(new IdentificationModule(this)) //请将IdentificationModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected void nodataRefresh() {

    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_identification, null, false);
    }

    @Override
    protected void initData() {
        setTitle(getString(R.string.title_identification));
        Intent intent = getIntent();
        status = intent.getStringExtra(Constant.STATUS);
        if(status != null && status.equals(Constant.LOGIN)) {
            setRightFunctionText(getString(R.string.function_skip),R.color.color_eec0be, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(MainActivity.class);
                    finish();
                }
            });
        }else if(status != null && status.equals(Constant.SETTING_NOMAL)){
            mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        mEtName.setOnFocusChangeListener(this);
//        mRlIdNumeralgitgit .setOnFocusChangeListeadbner(this);
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
    public String getName() {
        return mEtName.getText().toString().trim();
    }

    @Override
    public String getNumeral() {
        return mEtNumeral.getText().toString().trim();
    }

    @Override
    public void isSuccess() {
        if(status != null && status.equals(Constant.LOGIN)) {
            showDialog();
        }else if(status != null && status.equals(Constant.SETTING_NOMAL)){
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(Constant.URL, Api.PHONE_ID_CARD+"?userName="+getName()+"&number="+setTextIDCard(getNumeral()));
            intent.putExtra(Constant.TITLE,getResources().getString(R.string.authenticated_success));
            intent.putExtra(Constant.STATUS,Constant.SETTING);
            startActivity(intent);
        }
    }
    public void showDialog(){
        AlertDialog dialog = new AlertDialog(this);
        dialog.builder().setTitle("温馨提示").setMsg(getString(R.string.identification_success))
                .setOneButton("立即体验", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(MainActivity.class);
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_identification})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.rl_identification:
                mPresenter.identification();
                break;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()){
            case R.id.et_name:
                if(b) {
                    mRlName.setBackground(getResources().getDrawable(R.drawable.input_bux_tan_select));
                    mRlIdNumeral.setBackground(getResources().getDrawable(R.drawable.input_bux_grey_un_select));
                }else{
                    mRlName.setBackground(getResources().getDrawable(R.drawable.input_bux_grey_un_select));
                    mRlIdNumeral.setBackground(getResources().getDrawable(R.drawable.input_bux_tan_select));
                }
                break;
            case R.id.et_numeral:
                if(b) {
                    mRlName.setBackground(getResources().getDrawable(R.drawable.input_bux_grey_un_select));
                    mRlIdNumeral.setBackground(getResources().getDrawable(R.drawable.input_bux_tan_select));
                }else{
                    mRlName.setBackground(getResources().getDrawable(R.drawable.input_bux_tan_select));
                    mRlIdNumeral.setBackground(getResources().getDrawable(R.drawable.input_bux_grey_un_select));
                }
                break;
        }
    }

    public String setTextIDCard(String idCard) {
        StringBuilder telBuilder = new StringBuilder(idCard);
        telBuilder.replace(3, 15, "************");
        return telBuilder.toString();
    }

    public static class LayoutParams extends CardView.LayoutParams
            implements AutoLayoutHelper.AutoLayoutParams {
        private AutoLayoutInfo mAutoLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            mAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        @Override
        public AutoLayoutInfo getAutoLayoutInfo() {
            return mAutoLayoutInfo;
        }


        public LayoutParams(int width, int height) {
            super(width, height);
        }


        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }

    }
}
