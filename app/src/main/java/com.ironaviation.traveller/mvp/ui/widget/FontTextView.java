package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class FontTextView extends TextView {
    private String font = "fonts/308-CAI978.TTF";
    private Context mContext;
    public FontTextView(Context context) {
        super(context);
        this.mContext = context;
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void setTextType(String text){
        setTypeface(mContext,this);
        setText(text);
    }

    public void setFont(String font){
        this.font = font;
    }
    public void setTypeface(Context context, TextView textView){
        try {
            textView.setTypeface(getTypeface(context, font));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 创建一个 Typeface */
    private Typeface getTypeface(Context context, String path) throws Exception {
        return Typeface.createFromAsset(context.getAssets(), path);
    }

}
