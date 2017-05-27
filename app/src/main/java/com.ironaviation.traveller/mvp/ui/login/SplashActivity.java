package com.ironaviation.traveller.mvp.ui.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.PushClientUtil;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.ui.main.MainActivity;
import com.ironaviation.traveller.mvp.ui.widget.AutoViewPager;
import com.jess.arms.utils.DataHelper;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2016/12/22.
 */

public class SplashActivity extends WEActivity {


    @BindView(R.id.viewpager)
    AutoViewPager viewpager;
    @BindView(R.id.ll)
    AutoRelativeLayout ll;
    @BindView(R.id.ll_point)
    AutoLinearLayout point;
    @BindView(R.id.tw_join)
    TextView btn1;
    @BindView(R.id.tw_jump)
    TextView jump;
    private Gson gson;
    private ImageView[] imageViews;
    private static final int SIZE = 3;
    //    private String locationMsg;
    //    private int locationTimes = 0;//当前定位次数
//    private final static int LOCATION_TIMES_MAX = 5;//最大定位次数
    private static final String FIRST = "isFirst";


    // DemoPushService.class 自定义服务名称, 核心服务
    private static final int REQUEST_PERMISSION = 0;

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_splash, null, false);
    }

    @Override
    protected void initData() {
        if (DataHelper.getStringSF(this, FIRST) != null && DataHelper.getStringSF(this, FIRST).equals("second")) {
            viewpager.setVisibility(View.GONE);
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            imageView.setImageResource(R.mipmap.ic_start);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ll.addView(imageView);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Login(true);
                }
            }, 2000);
        }
//        initClientId();
        PushManager.getInstance().initialize(this);
        DataHelper.SetStringSF(this, FIRST, "second");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(false);
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(false);
            }
        });
        firstLaunch();
        imageViews = new ImageView[SIZE];
        for (int i = 0; i < SIZE; i++) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                layoutParams.leftMargin = 20;
            }
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.ic_page_indicator_theme_splash);
            point.addView(imageView);
            imageViews[i] = imageView;
        }
        imageViews[0].setImageResource(R.drawable.ic_page_indicator_theme_splash_focus);

    }

    public void Login(boolean flag) {
        if (DataHelper.getDeviceData(mApplication, Constant.LOGIN) != null) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            intent.putExtra(Constant.STATUS,flag);
            startActivity(intent);
        }
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        finish();
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    private void firstLaunch() {
        final int[] imgs = new int[]{R.mipmap.ic_launch_one,
                R.mipmap.ic_launch_two,
                R.mipmap.ic_launch_three};
        ImageAdapter adapter = new ImageAdapter(this, imgs);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == imgs.length - 1) {
                    btn1.setVisibility(View.VISIBLE);
//                    point.setVisibility(View.GONE);
//                    viewpager.setDisableScroll(true);
                    jump.setVisibility(View.GONE);
                } else {
                    jump.setVisibility(View.VISIBLE);
                    btn1.setVisibility(View.GONE);
                }
                setClear();
                imageViews[position].setImageResource(R.drawable.ic_page_indicator_theme_splash_focus);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewpager.setAdapter(adapter);
    }


    @Override
    protected void nodataRefresh() {

    }


    public static class ImageAdapter extends PagerAdapter {
        Context context;
        List<ImageView> imageViews;

        public void init(int[] images) {
            imageViews = new ArrayList<>();
            for (int i = 0; i < images.length; i++) {
                ImageView image = new ImageView(context);
                image.setScaleType(ImageView.ScaleType.FIT_CENTER);
                image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                image.setImageResource(images[i]);
                imageViews.add(image);
            }
        }

        public ImageAdapter(Context context, int[] imgs) {
            this.context = context;
            init(imgs);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

    public void setClear() {
        for (int i = 0; i < SIZE; i++) {
            imageViews[i].setImageResource(R.drawable.ic_page_indicator_theme_splash);
        }
    }

    public void toggleFullscreen(boolean fullScreen) {
        // fullScreen为true时全屏，否则相反
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (fullScreen) {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        } else {
            attrs.flags &= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        getWindow().setAttributes(attrs);
    }

}