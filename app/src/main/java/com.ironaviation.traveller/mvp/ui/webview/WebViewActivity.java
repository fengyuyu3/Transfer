package com.ironaviation.traveller.mvp.ui.webview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.ui.main.MainActivity;
import com.ironaviation.traveller.mvp.ui.main.MainNewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Administrator on 2017/4/27.
 */

public class WebViewActivity extends WEActivity {


    @BindView(R.id.webvidw)
    WebView mWebvidw;

    private int status;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_webview, null, false);
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String url = intent.getStringExtra(Constant.URL);
        String title = intent.getStringExtra(Constant.TITLE);
        status = intent.getIntExtra(Constant.STATUS,0);
        initToolbar(title);
        WebSettings webSettings = mWebvidw.getSettings();
        //设置编码
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebvidw.loadUrl(url);
    }

    public void initToolbar(String title){
        setTitle(title);
        setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status != 0){
                    switch (status){
                        case Constant.SETTING:
                            finish();
                        break;
                        case Constant.AUTO_SETTTING:
                            startActivity(MainNewActivity.class);
                        break;
                    }
                }else {
                    finish();
                }
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
}
