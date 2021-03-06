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

import static com.ironaviation.traveller.R.id.view;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/27 18:24
 * 修改人：
 * 修改时间：2017/3/27 18:24
 * 修改备注：
 */

public class PublicView extends AutoLinearLayout {

    private String hint,text;
    private boolean rightPeroson,rightCode,line,enable;
    private int leftLogo,padding_left;

    private TextView tvPerson, tvCode;
    private ImageView ivLogo;
    private EditText mEditText;
    private View lineView;
    private AutoLinearLayout ll;

    private onPublicViewClick click;

    public PublicView(Context context) {
        this(context, null);
    }

    public PublicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PublicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PublicView);
        leftLogo = a.getResourceId(R.styleable.PublicView_left_logo, -1);
        hint = a.getString(R.styleable.PublicView_hint);
//        rightPeroson = a.getBoolean(R.styleable.PublicView_right_peroson, false);
        rightCode = a.getBoolean(R.styleable.PublicView_right_code,false);
        text = a.getString(R.styleable.PublicView_text);
        line = a.getBoolean(R.styleable.PublicView_line,true);
        enable = a.getBoolean(R.styleable.PublicView_my_enable,false);
        padding_left = a.getInt(R.styleable.PublicView_padding_left,-1);

        a.recycle();

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View v = LayoutInflater.from(context).inflate(R.layout.include_public_view, this);
        ivLogo = (ImageView) v.findViewById(R.id.iv_logo);

//        tvPerson = (TextView) v.findViewById(R.id.tv_person);
        tvCode = (TextView) v.findViewById(R.id.tv_code);

        mEditText = (EditText) v.findViewById(R.id.edt_content);
        lineView = v.findViewById(R.id.line_edt);
        ll = (AutoLinearLayout) v.findViewById(R.id.pw_ll);

        setEditHint(hint);
        setEditText(text);
        setLogo(leftLogo);
//        setPersonVisible(rightPeroson);
        setCodeVisible(rightCode);
        setLineVisiable(line);
        setEditEnable(enable);
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
    public void setEditEnable(boolean flag){
        mEditText.setEnabled(flag);
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
            mEditText.setHint(text);
        }else{
            mEditText.setHint("");
        }
    }

    public void setEditText(String text){
        if(text != null){
            mEditText.setText(text);
        }else{
            mEditText.setText("");
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

    /*public void setPersonVisible(boolean flag){
        if(flag) {
            tvPerson.setVisibility(VISIBLE);
        }else{
            tvPerson.setVisibility(GONE);
        }
    }*/

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
        if(mEditText.getText().toString().trim() != null){
            return mEditText.getText().toString().trim();
        }else{
            return "";
        }
    }

    public void setTextInfo(String text){
        if(text != null){
            mEditText.setText(text);
        }else{
            mEditText.setText("");
        }
    }
    public void setOnpulicClickListener(onPublicViewClick click){
        this.click = click;
    }
}
