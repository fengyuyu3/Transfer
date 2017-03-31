package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.zhy.autolayout.AutoLinearLayout;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/28 17:06
 * 修改人：
 * 修改时间：2017/3/28 17:06
 * 修改备注：
 */

public class PublicTextView extends AutoLinearLayout {
    private String hint,text;
    private boolean rightPeroson,rightCode,line,enable;
    private int leftLogo,padding_left,backColor;

    private TextView tvPerson,tvCode,tvInfo;
    private ImageView ivLogo;
    private View lineView;
    private AutoLinearLayout ll;

    private PublicTextView.onPublicViewClick click;

    public PublicTextView(Context context) {
        this(context, null);
    }

    public PublicTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PublicTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PublicTextView);
        leftLogo = a.getResourceId(R.styleable.PublicTextView_left_logo1, -1);
        hint = a.getString(R.styleable.PublicTextView_hint1);
        rightPeroson = a.getBoolean(R.styleable.PublicTextView_right_peroson1, false);
        rightCode = a.getBoolean(R.styleable.PublicTextView_right_code1,false);
        text = a.getString(R.styleable.PublicTextView_text1);
        line = a.getBoolean(R.styleable.PublicTextView_line1,true);
        enable = a.getBoolean(R.styleable.PublicTextView_my_enable1,false);
        padding_left = a.getInt(R.styleable.PublicTextView_padding_left1,-1);
        backColor = a.getColor(R.styleable.PublicTextView_textColor,-1);

        a.recycle();

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View v = LayoutInflater.from(context).inflate(R.layout.include_public_textview, this);
        ivLogo = (ImageView) v.findViewById(R.id.iv_logo1);

        tvPerson = (TextView) v.findViewById(R.id.tv_person1);
        tvCode = (TextView) v.findViewById(R.id.tv_code1);

        tvInfo = (TextView) v.findViewById(R.id.edt_content1);
        lineView = v.findViewById(R.id.line_edt1);
        ll = (AutoLinearLayout) v.findViewById(R.id.pw_ll1);

        setEditHint(hint);
        setLogo(leftLogo);
        setPersonVisible(rightPeroson);
        setCodeVisible(rightCode);
        setLineVisiable(line);
        setPaddingLeft(padding_left);
        tvCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                click.onPublicViewClick(view);
            }
        });
    }

    public void setPaddingLeft(int px){
        ivLogo.setPadding(px,0,32,0);
    }

    public void setLineVisiable(boolean flag){
        if(flag) {
            lineView.setVisibility(VISIBLE);
        }else{
            lineView.setVisibility(GONE);
        }
    }

    public void setEditHint(String text){
        if(text != null){
            tvInfo.setText(text);
        }else{
            tvInfo.setText("");
        }
    }


    public void setLogo(int resId){
        if(resId != -1) {
            ivLogo.setImageResource(resId);
            ivLogo.setVisibility(VISIBLE);
        }else{
            ivLogo.setVisibility(GONE);
        }
    }

    public void setPersonVisible(boolean flag){
        if(flag) {
            tvPerson.setVisibility(VISIBLE);
        }else{
            tvPerson.setVisibility(GONE);
        }
    }

    public void setCodeVisible(boolean flag){
        if(flag){
            tvCode.setVisibility(VISIBLE);
        }else{
            tvCode.setVisibility(GONE);
        }
    }

    public interface onPublicViewClick{
        void onPublicViewClick(View view);
    }

    public String getTextInfo(){
        if(tvInfo.getText().toString().trim() != null){
            return tvInfo.getText().toString().trim();
        }else{
            return "";
        }
    }

    public void setTextInfo(String text){
        if(text != null){
            tvInfo.setText(text);
            tvInfo.setTextColor(getResources().getColor(R.color.login_edit));
        }else{
            tvInfo.setText("");
        }
    }
    public void setOnpulicClickListener(PublicTextView.onPublicViewClick click){
        this.click = click;
    }
}
