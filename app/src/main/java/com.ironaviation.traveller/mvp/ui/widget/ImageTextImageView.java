package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/27 18:24
 * 修改人：
 * 修改时间：2017/3/27 18:24
 * 修改备注：
 */

public class ImageTextImageView extends AutoLinearLayout {

    private String text;
    private int leftLogo, rightGoOn;

    private TextView tvText;
    private ImageView ivLogo;
    private ImageView ivGoOn;

    private View v;

    private AutoRelativeLayout rl_iti;
    public ImageTextImageView(Context context) {
        this(context, null);
    }

    public ImageTextImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageTextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ImageTextImageView);
        leftLogo = a.getResourceId(R.styleable.ImageTextImageView_left_logo_iti, -1);
        rightGoOn = a.getResourceId(R.styleable.ImageTextImageView_right_go_on, -1);
        text = a.getString(R.styleable.ImageTextImageView_text_iti);
        a.recycle();
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v = LayoutInflater.from(context).inflate(R.layout.include_image_text_image_view, this);
        ivLogo = (ImageView) v.findViewById(R.id.iv_logo);
        ivGoOn = (ImageView) v.findViewById(R.id.iv_go_on);
        tvText = (TextView) v.findViewById(R.id.tv_text);
        rl_iti= (AutoRelativeLayout) v.findViewById(R.id.rl_iti)
        ;
        setText(text);
        setLogo(leftLogo);
        setGoOn(rightGoOn);
    }


    public void setText(String text) {
        if (text != null) {
            tvText.setText(text);
        } else {
            tvText.setText("");
        }
    }


    public void setLogo(int resId) {
        if (resId != -1) {
            ivLogo.setImageResource(resId);
            ivLogo.setVisibility(VISIBLE);
        } else {
            ivLogo.setVisibility(GONE);
        }
    }


    public void setGoOn(int resId) {
        if (resId != -1) {
            ivGoOn.setImageResource(resId);
            ivGoOn.setVisibility(VISIBLE);
        } else {
            ivGoOn.setVisibility(GONE);
        }
    }

    public void setBackGround(int resId) {

        rl_iti.setBackgroundColor(resId);
    }
}
