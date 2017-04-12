package com.ironaviation.traveller.app.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.widget.TextView;


/** 字体工具类 */
public class TypefaceUtils {

    private static TypefaceUtils typefaceUtils;
    private SparseArray<Typeface> fontFaces;

    private TypefaceUtils() {
    }

    /** 获取 TypefaceUtils 的实例 */
    public static TypefaceUtils getInstance() {
        if (typefaceUtils == null)
            typefaceUtils = new TypefaceUtils();
        return typefaceUtils;
    }

    /** 设置字体 */
    public void setTypeface(Context context, TextView textView, int type) {
        try {
            if (fontFaces == null)
                fontFaces = new SparseArray<>();

            switch (type) {
                case 1: // 字体《MSYHL.TTC》
                    if (fontFaces.get(type) == null)
                        fontFaces.put(type, getTypeface(context, "fonts/MSYHL.TTC"));
                    break;
                case 2: // 字体《bottom_navigation.TTC》
                    if (fontFaces.get(type) == null)
                        fontFaces.put(type, getTypeface(context, "fonts/bottom_navigation.TTC"));
                    break;
            }

            textView.setTypeface(fontFaces.get(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTypeface(Context context, TextView textView){
        try {
            textView.setTypeface(getTypeface(context, "fonts/308-CAI978.TTF"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 创建一个 Typeface */
    private Typeface getTypeface(Context context, String path) throws Exception {
        return Typeface.createFromAsset(context.getAssets(), path);
    }

}
