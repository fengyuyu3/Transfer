package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.WEApplication;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017/3/29 11:24
 * 修改人：
 * 修改时间：2017/3/29 11:24
 * 修改备注：
 */

public class TextTextImageView extends AutoLinearLayout {

    private String text, title;
    private int textColor = -1;
    private int rightGoOn,img;

    private TextView tvText;
    private TextView tvTitle;
    private ImageView ivGoOn,iwImg;

    private View v;


    public TextTextImageView(Context context) {
        this(context, null);
    }

    public TextTextImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextTextImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextTextImageView);
        rightGoOn = a.getResourceId(R.styleable.TextTextImageView_tti_right_go_on, -1);
        text = a.getString(R.styleable.TextTextImageView_tti_text);
        title = a.getString(R.styleable.TextTextImageView_tti_title);
        textColor = a.getColor(R.styleable.TextTextImageView_tti_text_color, ContextCompat.getColor(context, R.color.word_already_input));
        img = a.getResourceId(R.styleable.TextTextImageView_tti_img,-1);
        a.recycle();
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v = LayoutInflater.from(context).inflate(R.layout.include_text_text_image_view, this);
        ivGoOn = (ImageView) v.findViewById(R.id.iv_go_on);
        tvText = (TextView) v.findViewById(R.id.tv_tti_text);
        tvTitle = (TextView) v.findViewById(R.id.tv_tti_title);
        iwImg = (ImageView) v.findViewById(R.id.iw_identification);
        setText(text);
        setTitle(title);
        setGoOn(rightGoOn);
        setTextColor(textColor);
        setImg(img);
    }


    public void setText(String text) {
        if (text != null) {
            tvText.setText(text);
        } else {
            tvText.setText("");
        }
    }

    public void setTitle(String title) {
        if (title != null) {
            tvTitle.setText(title);
        } else {
            tvTitle.setText("");
        }
    }

    public void setTextColor(@ColorInt int textColor) {
        tvText.setTextColor(textColor);


    }

    public void setGoOn(int resId) {
        if (resId != -1) {
            ivGoOn.setImageResource(resId);
            ivGoOn.setVisibility(VISIBLE);
        } else {
            ivGoOn.setVisibility(GONE);
        }
    }

    public void setImg(int resId){
        if(resId != -1){
            iwImg.setImageResource(resId);
            iwImg.setVisibility(VISIBLE);
        }else{
            iwImg.setVisibility(GONE);
        }
    }

}
