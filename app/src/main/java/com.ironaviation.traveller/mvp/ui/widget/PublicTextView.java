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
    private boolean rightPeroson,rightCode,line,enable,rightAddress,rightTime,rightDelete,rightBack,freeInfo;
    private int leftLogo,padding_left,backColor;

    private TextView tvPerson,tvCode,tvInfo,tvArriveTime,tvAddress,tvFreeInfo;
    private ImageView ivLogo,iwDelete,iwBack;
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
        rightAddress = a.getBoolean(R.styleable.PublicTextView_right_address1,false);
        rightTime = a.getBoolean(R.styleable.PublicTextView_right_time1,false);
        rightBack = a.getBoolean(R.styleable.PublicTextView_right_back,false);
        freeInfo = a.getBoolean(R.styleable.PublicTextView_right_free_info,false);
//        rightDelete = a.getBoolean(R.styleable.PublicTextView_right_delete1,false);

        a.recycle();

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View v = LayoutInflater.from(context).inflate(R.layout.include_public_textview, this);
        ivLogo = (ImageView) v.findViewById(R.id.iv_logo1);

        tvPerson = (TextView) v.findViewById(R.id.tv_person1);
        tvCode = (TextView) v.findViewById(R.id.tv_code1);

        tvInfo = (TextView) v.findViewById(R.id.edt_content1);
        lineView = v.findViewById(R.id.line_edt1);
        ll = (AutoLinearLayout) v.findViewById(R.id.pw_ll1);
        tvArriveTime = (TextView) v.findViewById(R.id.tv_arrive_time);
        tvAddress = (TextView) v.findViewById(R.id.tv_address);
        tvFreeInfo = (TextView) v.findViewById(R.id.tw_free_info);
        iwBack = (ImageView) v.findViewById(R.id.iw_right_back);
//        iwDelete = (ImageView) v.findViewById(R.id.iw_delete);

//        setFirstTextColor(backColor);
        setEditHint(hint);
        setLogo(leftLogo);
        setPersonVisible(rightPeroson);
        setCodeVisible(rightCode);
        setLineVisiable(line);
//        setPaddingLeft(padding_left);
        showArriveTime(rightTime);
        showAddress(rightAddress);
        setRightBack(rightBack);
        setFreeInfo(freeInfo);
//        setDelete(rightDelete);
        tvCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                click.onPublicViewClick(view);
            }
        });
    }

    public void setFreeInfo(String text){
        if(text != null) {
            tvFreeInfo.setText(text);
        }else{
            tvFreeInfo.setText("");
        }
    }

    public void setRightBack(boolean flag){
        iwBack.setVisibility(flag ? VISIBLE : GONE);
    }

    public void setFreeInfo(boolean flag){
        tvFreeInfo.setVisibility(flag ? VISIBLE :GONE);
    }

    public void setPaddingLeft(int px){
        ivLogo.setPadding(px,0,32,0);
    }

    public void setLineVisiable(boolean flag){
        lineView.setVisibility(flag  ? VISIBLE : GONE);

    }

    public void setEditHint(String text){
        if(text != null){
            tvInfo.setText(text);
        }else{
            tvInfo.setText("");
        }
    }

    public void setFirstTextColor(int color){
        if(color != 0){
            tvInfo.setTextColor(color);
        }else{
            tvInfo.setTextColor(getResources().getColor(R.color.login_edit));
        }
    }
    /*public void setDelete(boolean flag){
        if(flag){
            iwDelete.setVisibility(VISIBLE);
        }else{
            iwDelete.setVisibility(GONE);
        }
    }*/


    public void showArriveTime(boolean flag){
        if(flag){
            tvArriveTime.setVisibility(VISIBLE);
        }else{
            tvArriveTime.setVisibility(GONE);
        }
//        tvArriveTime.setVisibility(flag == true ? VISIBLE : GONE);
    }
    public void setArriveTime(String text){
        if(text != null){
            tvArriveTime.setText(text);
        }else{
            tvArriveTime.setText("");
        }
    }
    public void showAddress(boolean flag){
        if(flag){
            tvAddress.setVisibility(VISIBLE);
        }else{
            tvAddress.setVisibility(GONE);
        }
//        tvAddress.setVisibility(flag == true ? VISIBLE : GONE);
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
        if(flag){
            tvPerson.setVisibility(VISIBLE);
        }else{
            tvPerson.setVisibility(GONE);
        }
//         tvPerson.setVisibility(flag == true ? VISIBLE : GONE);
    }

    public void setCodeVisible(boolean flag){
        if(flag){
            tvCode.setVisibility(VISIBLE);
        }else{
            tvCode.setVisibility(GONE);
        }
//         tvCode.setVisibility(flag == true ? VISIBLE : GONE);
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

    public void setTextInfo(String text,int color){
        if(text != null){
            tvInfo.setText(text);
            tvInfo.setTextColor(getResources().getColor(color));
        }else{
            tvInfo.setText("");
        }
    }

    public void setInitInfo(String text){
        if(text != null){
            tvInfo.setText(text);
            tvInfo.setTextColor(getResources().getColor(R.color.word_middle_grey));
        }else{
            tvInfo.setText("");
        }
    }
    public void setOnpulicClickListener(PublicTextView.onPublicViewClick click){
        this.click = click;
    }
}
