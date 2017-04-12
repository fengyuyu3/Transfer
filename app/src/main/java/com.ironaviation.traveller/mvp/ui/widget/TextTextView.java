package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017/3/29 11:24
 * 修改人：
 * 修改时间：2017/3/29 11:24
 * 修改备注：
 */

public class TextTextView extends AutoLinearLayout {

    private String text, title;
    private int textColor = -1,titleColor=-1;

    private TextView tvText;
    private TextView tvTitle;

    private View v;


    public TextTextView(Context context) {
        this(context, null);
    }

    public TextTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextTextView);
        text = a.getString(R.styleable.TextTextView_tt_text);
        title = a.getString(R.styleable.TextTextView_tt_title);
        textColor = a.getColor(R.styleable.TextTextView_tt_text_color, ContextCompat.getColor(context, R.color.word_already_input));
        titleColor= a.getColor(R.styleable.TextTextView_tt_title_color, ContextCompat.getColor(context, R.color.word_already_input));
        a.recycle();
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        v = LayoutInflater.from(context).inflate(R.layout.include_text_text_view, this);
        tvText = (TextView) v.findViewById(R.id.tv_tt_text);
        tvTitle = (TextView) v.findViewById(R.id.tv_tt_title);
        setText(text);
        setTitle(title);
        setTextColor(textColor);
        setTitleColor(titleColor);
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
    public void setTitleColor(@ColorInt int textColor) {
        tvTitle.setTextColor(textColor);


    }


}
