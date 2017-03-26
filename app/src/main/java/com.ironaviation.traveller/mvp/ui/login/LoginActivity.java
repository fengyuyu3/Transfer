package com.ironaviation.traveller.mvp.ui.login;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.BarUtils;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.login.DaggerLoginComponent;
import com.ironaviation.traveller.di.module.login.LoginModule;
import com.ironaviation.traveller.mvp.contract.login.LoginContract;
import com.ironaviation.traveller.mvp.presenter.Login.LoginPresenter;
import com.jess.arms.utils.UiUtils;

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
 *
 * 项目名称：Transfer      
 * 类描述：   
 * 创建人：flq  
 * 创建时间：2017/3/23 13:30   
 * 修改人：  
 * 修改时间：2017/3/23 13:30   
 * 修改备注：   
 * @version
 *
 */

public class LoginActivity extends WEActivity<LoginPresenter> implements LoginContract.View {

    @BindView(R.id.et_phone)
    EditText etphone;
    @BindView(R.id.et_code)
    EditText etCode;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerLoginComponent
                .builder()
                .appComponent(appComponent)
                .loginModule(new LoginModule(this)) //请将LoginModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected void nodataRefresh() {
        setTitle("测试没有数据");
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_login, null, false);
    }

    @Override
    protected void initData() {
        Log.e("kkk",BarUtils.getStatusBarHeight(this)+"");
        setTitle("测试");
        /*mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });*/
        showStart(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showStart(false);
            }
        },4000);
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
    public String getCode() {
        return etCode.getText().toString().trim();
    }

    @Override
    public String getUserInfo() {
        return etphone.getText().toString().trim();
    }
}
