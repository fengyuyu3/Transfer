package com.ironaviation.traveller.app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

/**
 * Created by johnzheng on 1/26/16.
 */
@SuppressWarnings("deprecation")
public class Utils {

    public static final int PHOTO_SUCCESS = 1;

    public static final int CAMERA_SUCCESS = 2;

    public static final int PHOTO_CROP_SUCCESS = 3;

    public static final String DIR = "mouth";


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /*public static int dip2px(float dipValue) {
        final float scale = BizApplication.getAppContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }*/

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static Long getLong(String s) {
        try {
            return Long.parseLong(s.trim());
        } catch (Exception e) {
            return 0L;
        }
    }

    public static boolean getBoolean(String s) {
        try {
            return Boolean.parseBoolean(s.trim());
        } catch (Exception e) {
            return false;
        }
    }

    public static Float getFloat(String s) {
        try {
            return Float.parseFloat(s.trim());
        } catch (Exception e) {
            return 0f;
        }
    }

    public static Integer getInteger(String s) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public static Double getDouble(String s) {
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0D;
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public static void goGallery(Activity context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        if (Build.VERSION.SDK_INT < 19) { //19以后这个api不可用，demo这里简单处理成图库选择图片
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        context.startActivityForResult(intent, PHOTO_SUCCESS);
    }

    public static Uri goCamera(Activity context) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR);

        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR, System.currentTimeMillis() + ".jpg");
        if (!f.exists()) {
            f.mkdirs();
        }

        Uri outputFileUri = Uri.fromFile(file);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        context.startActivityForResult(intent, CAMERA_SUCCESS);
        return outputFileUri;
    }


    public static String getPath(Activity context, Uri uri) {
        String str = "";
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            str = cursor.getString(column_index);
            if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                cursor.close();
            }
        }
        return str;
    }


    /**
     * 裁剪图片
     *
     * @param
     */
    public static Uri startPhotoZoom(Activity context) {
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR);
        if (!!f.exists()) {
            f.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR + "img.jpg");
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image");
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
        intent.putExtra("outputX", 200);//图片输出大小
        intent.putExtra("outputY", 200);
        intent.putExtra("output", uri);
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        context.startActivityForResult(intent, PHOTO_CROP_SUCCESS);
        return uri;
    }

    public static void goCameraCrop(Activity context) {
        File f = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR);
        if (!!f.exists()) {
            f.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()
                + File.separator + DIR + "img.jpg");
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        context.startActivityForResult(intent, CAMERA_SUCCESS);
    }

    // 屏幕宽度（像素）
    public static int getWindowWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(Activity activity) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    public static String getHtmlStr(String content) {
        String header = "<!doctype html><html lang='cn'><head>" +
                "<meta name=\"viewport\" content=\"width=device-width, maximum-scale=1, minimum-scale=1, user-scale=1\">" +
                "<title>图文详情</title><style>img {\n" +
                "            width:100%;\n" +
                "            display:block;\n" +
                "            height:auto;\n" +
                "        }</style></head><body>";
        String end = "</body></html>";

        return header + content + end;
    }

    public static boolean copyApkFromAssets(Context context, String fileName, String path) {
//        Loger.e("copyApkFromAssets fileName = " + fileName + "### path = " + path);

        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            if (file.exists()) {
                copyIsFinish = true;
            } else {
                creatFile(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + File.separator + context.getPackageName() + File.separator, fileName);
                FileOutputStream fos = new FileOutputStream(file);
                byte[] temp = new byte[1024];
                int i = 0;
                while ((i = is.read(temp)) > 0) {
                    fos.write(temp, 0, i);
                }
                fos.close();
                is.close();
                copyIsFinish = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    public static boolean creatFile(String rootPath, String fileName) {
        File f = new File(rootPath);
        if (!f.exists()) {
            f.mkdirs();
        }

        String apkfilepath = rootPath + fileName;
        File apkfile = new File(apkfilepath);

        try {
            if (!apkfile.createNewFile()) {
//                Loger.e("File already exists");
                return false;
            }

        } catch (IOException ex) {
//            Loger.e(ex.toString());
            return false;
        }
        return true;
    }


    /**
     * 转换显示的距离
     * @param distance
     * @return
     */
    public static String transDistance(double distance) {
        String str = "";
        DecimalFormat fmt = new DecimalFormat("0.00");
        if (distance > 999) {
            str = fmt.format(distance / 1000) + "km";
        } else {
            str = fmt.format(distance) + "m";
        }

        return str;
    }
}
