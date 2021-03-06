package com.ironaviation.traveller.mvp.ui.my;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoToolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.BGAQRCodeUtil;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-31 16:53
 * 修改人：starRing
 * 修改时间：2017-03-31 16:53
 * 修改备注：
 */
public class QRCodeActivity extends WEActivity {

    String OrderNo;

    @BindView(R.id.iv_function_left)
    ImageView mIvFunctionLeft;
    @BindView(R.id.toolbar)
    AutoToolbar mToolbar;
    @BindView(R.id.tv_qr_code_hint)
    TextView mTvQrCodeHint;
    @BindView(R.id.zxingview)
    ImageView mZxingview;
    @BindView(R.id.tv_order_id)
    TextView mTvOrderId;

    @Override
    protected void nodataRefresh() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected View initView() {

        return LayoutInflater.from(this).inflate(R.layout.activity_qr_code, null, false);

    }

    @Override
    protected void initData() {
        Bundle pBundele = getIntent().getExtras();
        OrderNo = pBundele.getString("OrderNo");
        createEnglishQRCode(OrderNo);
        initToolbar();
        mTvOrderId.setText(OrderNo);
    }

    public void initToolbar() {
        setTitle(getString(R.string.order_qr_code));
        setLeftFunction(ContextCompat.getDrawable(this, R.mipmap.ic_back), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.shadow_champagne));

    }

    private void createEnglishQRCode(final String cord) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github.com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {

                return QRCodeEncoder.syncEncodeQRCode(cord, BGAQRCodeUtil.dp2px(QRCodeActivity.this, 150), Color.parseColor("#000000"));
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                if (bitmap != null) {
                    ImageView zxingview = (ImageView) findViewById(R.id.zxingview);
                    zxingview.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(QRCodeActivity.this, "生成二维码失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
