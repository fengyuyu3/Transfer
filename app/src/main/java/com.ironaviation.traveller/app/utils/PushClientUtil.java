package com.ironaviation.traveller.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

import com.igexin.sdk.PushManager;
import com.ironaviation.traveller.app.service.WEGTIntentService;
import com.ironaviation.traveller.app.service.WEPushService;
import com.ironaviation.traveller.common.WEApplication;
import com.ironaviation.traveller.mvp.constant.Constant;

/**
 * Created by Administrator on 2017/5/10.
 */

public class PushClientUtil {

    private static final int REQUEST_PERMISSION = 0;
    public static void initClientId(Activity activity) {

        // com.getui.demo.DemoPushService 为第三方自定义推送服务
        PackageManager pkgManager = WEApplication.getContext().getPackageManager();

        // 读写 sd card 权限非常重要, android6.0默认禁止的, 建议初始化之前就弹窗让用户赋予该权限
        boolean sdCardWritePermission =
                pkgManager.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, activity.getPackageName()) == PackageManager.PERMISSION_GRANTED;

        // read phone state用于获取 imei 设备信息
        boolean phoneSatePermission =
                pkgManager.checkPermission(Manifest.permission.READ_PHONE_STATE, activity.getPackageName()) == PackageManager.PERMISSION_GRANTED;

        if (Build.VERSION.SDK_INT >= 23 && !sdCardWritePermission || !phoneSatePermission) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE},
                    REQUEST_PERMISSION);
        } else {
            PushManager.getInstance().initialize(WEApplication.getContext(), WEPushService.class);
        }
        PushManager.getInstance().initialize(WEApplication.getContext(), WEPushService.class);
        PushManager.getInstance().registerPushIntentService(WEApplication.getContext(), WEGTIntentService.class);
    }
}
