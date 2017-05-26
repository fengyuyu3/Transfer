package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.AnimationUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;


/**
 * Created by Vernon on 16/6/20.
 */
public class FirstInfoPopupwindow extends PopupWindow implements View.OnClickListener {

  private Context context;// 上下文

  private ImageButton btnCancel;
  private TextView twOne,twTwo,twThree,twCommit;
  private AnimationUtil mAnimationUtil;
  private AutoLinearLayout rlDriverCode,llText;
  private EditText etText;


  public FirstInfoPopupwindow(Context mContext) {
    super(mContext);
    this.context = mContext;
    init();
  }

  private void init() {
    mAnimationUtil = AnimationUtil.getInstance(context);
    View view = LayoutInflater.from(context).inflate(R.layout.popupwindow_update_level, null);
    setContentView(view);
    setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
    setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
    setOutsideTouchable(true);
    setTouchable(true);
    setFocusable(true);
    setBackgroundDrawable(new BitmapDrawable());
    findById(view);
    btnCancel.setOnClickListener(this);
    llText.setOnClickListener(this);
    twCommit.setOnClickListener(this);
    setAnimationStyle(R.style.popwindow_action_show);
    setEnable(false);
    etText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override
      public void afterTextChanged(Editable s) {
          clearText();
          if(s.toString().length() == 0){
             twOne.setBackgroundResource(R.drawable.text_red_shap);
             twOne.setText("");
             setEnable(false);
          }else if(s.toString().length() == 1){
             twTwo.setBackgroundResource(R.drawable.text_red_shap);
             twOne.setText(s.toString());
             twTwo.setText("");
             setEnable(false);
//             twThree.setText("");
          }else if(s.toString().length() == 2){
             twThree.setBackgroundResource(R.drawable.text_red_shap);
             twTwo.setText(s.toString().charAt(1)+"");
             twThree.setText("");
             setEnable(false);
          }else{
             setEnable(true);
             twThree.setText(s.toString().charAt(2)+"");
          }
      }
    });
    /*mAnimationUtil.moveToViewCenter(rlDriverCode);
    rlDriverCode.setVisibility(View.VISIBLE);*/
  }

  public void findById(View view){
    btnCancel = (ImageButton) view.findViewById(R.id.btn_cancel);
    rlDriverCode = (AutoLinearLayout) view.findViewById(R.id.rl_driver_code);
    twOne = (TextView) view.findViewById(R.id.et_driver_one);
    twTwo = (TextView) view.findViewById(R.id.et_driver_two);
    twThree = (TextView) view.findViewById(R.id.et_driver_three);
    etText = (EditText) view.findViewById(R.id.et_text);
    llText = (AutoLinearLayout) view.findViewById(R.id.ll_text);
    twCommit = (TextView) view.findViewById(R.id.tw_commit);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_cancel:
//      Toast.makeText(context,etText.getText().toString(),Toast.LENGTH_SHORT).show();
        /*mAnimationUtil.moveToViewTop(rlDriverCode);
        rlDriverCode.setVisibility(View.GONE);*/
        dismiss();
      case R.id.ll_text:
        etText.requestFocus();
        InputMethodManager imm = (InputMethodManager) etText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
        etText.setSelection(etText.getText().length());
        break;
      case R.id.tw_commit:
        Toast.makeText(context,etText.getText().toString(),Toast.LENGTH_SHORT).show();
      break;
    }
  }

  public void clearText(){
    twOne.setBackgroundResource(R.drawable.text_grey_shap);
    twTwo.setBackgroundResource(R.drawable.text_grey_shap);
    twThree.setBackgroundResource(R.drawable.text_grey_shap);
  }

  public void setEnable(boolean show){
      if(show){
          twCommit.setEnabled(show);
          twCommit.setBackgroundResource(R.drawable.btn_driver_code_selector);
          twCommit.setTextColor(context.getResources().getColor(R.color.white));
      }else{
          twCommit.setEnabled(show);
          twCommit.setBackgroundResource(R.drawable.text_enable_grey_shap);
          twCommit.setTextColor(context.getResources().getColor(R.color.word_toolbar_un_select));
      }
  }
  public void show(View parentView) {
    showAtLocation(parentView, Gravity.CENTER, 0, 0);
  }
}
