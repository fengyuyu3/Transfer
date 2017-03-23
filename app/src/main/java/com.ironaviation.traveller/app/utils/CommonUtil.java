package com.ironaviation.traveller.app.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by Rex on 2016/9/28
 */
public class CommonUtil {

    private static final String TAG = "LOG";
    public static final String KEY = "cd962540";

    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

   /* *//**
     * 描述：打印调试Log信息
     *
     * @return
     *//*
    public static void logDebug(String msg) {
        if (BuildConfig.LOG_DEBUG) {
            Log.d(TAG, "------>: " + msg);
        }

    }*/

    /**
     * 描述：获取版本号
     *
     * @return
     */
    public static int getVersion(Context context) {
        int version = 0;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionCode;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return version;
    }


    @NonNull
    public static String getPass(String originPass) {
        return (int) (Math.random() * 900) + 100 + originPass + ((int) (Math.random() * 900000) + 100000);
    }

    /**
     * 描述：获取版本名称
     *
     * @return
     */
    public static String getVersionName(Context context) {
        String version = "1.0";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean isSDCardEnable() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 描述：打开软键盘
     *
     * @param mEditText 输入框
     * @param context   上下文
     * @return
     */
    public static void openKeybord(EditText mEditText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 描述：关闭软键盘
     *
     * @param mEditText 输入框
     * @param context   上下文
     * @return
     */
    public static void closeKeybord(EditText mEditText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    /**
     * 描述：网络监测
     *
     * @return true 可用,false不可用
     */
    public final static boolean isNetWorkConnected(Context ctx) {
        ConnectivityManager manager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = manager.getActiveNetworkInfo();
        if (network != null && network.isConnected()) {
            if (network.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    /**
     * 描述：判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;
        return cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;

    }

    /**
     * 描述： 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 描述：获取移动设备标识码
     *
     * @return String
     */
    public static String getImei(Context context, String imei) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
            Log.d("", e.getMessage());
        }
        return imei;
    }


    /**
     * 将字符串MD5加密
     *
     * @param string
     * @return String
     */
    public static String MD5Code(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

    /**
     * 将字符串DES加密
     *
     * @param encryptString 需要加密的明文
     * @return encryptKey 秘钥
     */
    public static String encryptDES(String encryptString, String encryptKey) {
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
            return Base64.encodeToString(encryptedData, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 将字符串DES解密
     *
     * @param decryptString 需要解密的明文
     * @return encryptKey 秘钥
     */
    public static String decryptDES(String decryptString, String decryptKey) {
        try {
            byte[] byteMi = Base64.decode(decryptString, Base64.DEFAULT);
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            byte decryptedData[] = cipher.doFinal(byteMi);
            return new String(decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * email是否有效
     *
     * @param mail
     * @return boolean
     */
    public static boolean isValidEmail(String mail) {
        Pattern pattern = Pattern
                .compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
        Matcher mc = pattern.matcher(mail);
        return mc.matches();
    }

    /**
     * 打电话
     *
     * @param phoneNum
     * @return boolean
     */
    public static void call(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 描述：SD卡是否能用.
     *
     * @return true 可用,false不可用
     */
    public static boolean SDCanBeUsed() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 格式化金额（添加逗号分隔）
     *
     * @param money
     * @param len:保留小数位数
     * @return
     */
    public static String formatMoney(String money, int len) {
        if (TextUtils.isEmpty(money))
            return "";
        NumberFormat formater = null;
        double num = Double.parseDouble(money);
        if (len == 0) {
            formater = new DecimalFormat("###,###");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,###.");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        String result = formater.format(num);
        if (result.indexOf(".") == -1) {
            result = result + ".00";
        } else {
            result = result;
        }
        return result;
    }


    public static String changeNull(String str) {
        if (str == null)
            return "";
        else
            return str;
    }

    /**
     * 读取文本数据
     *
     * @param context  程序上下文
     * @param fileName 文件名
     * @return String, 读取到的文本内容，失败返回null
     */
    public static String readAssets(Context context, String fileName) {
        InputStream is = null;
        String content = null;
        try {
            is = context.getAssets().open(fileName);
            if (is != null) {

                byte[] buffer = new byte[1024];
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    int readLength = is.read(buffer);
                    if (readLength == -1) break;
                    arrayOutputStream.write(buffer, 0, readLength);
                }
                is.close();
                arrayOutputStream.close();
                content = new String(arrayOutputStream.toByteArray());

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            content = null;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return content;
    }

    /**
     * 描述：隐藏电话号码中间4位
     *
     * @return
     */
    public static String hidePhoneNum(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } else
            return "";
    }

    /**
     * 获取取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }
/*    public static String getImageUrl(String id,int width,int hight){
        JSONObject json=new JSONObject();
        try {
            json.put("id",id);
            json.put("width",width+"");
            json.put("hight",hight+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url= Api.APP_DOMAIN+Api.IMAGE_URL+json.toString();
        logDebug(url);
        return url;
    }*/
    public static float numformater4(float f){
        BigDecimal b = new BigDecimal(f);
        float f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return f1;
    }

    public static String getDeviceId(Context context){
        return Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ANDROID_ID);
    }
}
