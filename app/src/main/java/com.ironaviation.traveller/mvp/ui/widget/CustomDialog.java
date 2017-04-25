package com.ironaviation.traveller.mvp.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.AnimationLoader;


/**
 * Created by Vernon on 16/5/19.
 */
public class CustomDialog extends Dialog implements View.OnClickListener {

//  private TextView tv_title;
  private TextView tv_content;
  private TextView tv_left;
  private TextView tv_right;
  private AnimationSet mAnimIn, mAnimOut;

  private OnPositiveListener mPositiveListener;

  private OnNegativeListener mNegativeListener;

  private View mBtnGroupView, mDividerView;

  private int mBackgroundColor, mTitleTextColor, mContentTextColor;

  private CharSequence mTitleText, mContentText, mPositiveText, mNegativeText;
  private boolean mIsShowAnim;
  private @ColorRes int leftColor;
  private @ColorRes int rightColor;


  public CustomDialog(Context context) {
    super(context);
    init();
  }

  protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
    super(context, cancelable, cancelListener);
    init();
  }

  public CustomDialog(Context context, int themeResId) {
    super(context, themeResId);
    init();
  }

  public void setmNegativeText(CharSequence mNegativeText) {
    this.mNegativeText = mNegativeText;
  }

  public void setmPositiveText(CharSequence mPositiveText) {
    this.mPositiveText = mPositiveText;
  }

  public void setmContentText(CharSequence mContentText) {
    this.mContentText = mContentText;
  }

  public void setmTitleText(CharSequence mTitleText) {
    this.mTitleText = mTitleText;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    View contentView = View.inflate(getContext(), R.layout.dialog_default_select, null);
    setContentView(contentView);

    tv_left = (TextView) contentView.findViewById(R.id.btnPositive);
    tv_right = (TextView) contentView.findViewById(R.id.btnNegative);

    tv_content = (TextView) contentView.findViewById(R.id.tv_content);

    mDividerView = contentView.findViewById(R.id.divider);
    mBtnGroupView = contentView.findViewById(R.id.llBtnGroup);

    Window dialogWindow = getWindow();
    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
    DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
    lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
    dialogWindow.setAttributes(lp);

    tv_left.setOnClickListener(this);
    tv_right.setOnClickListener(this);

//    tv_title.setText(mTitleText);
    tv_content.setText(mContentText);
    tv_left.setText(mPositiveText);
    tv_right.setText(mNegativeText);
    setTextColor();
  }

  private void setTextColor() {
    if(0!=leftColor){
      tv_left.setTextColor(getContext().getResources().getColor(leftColor));
    }
    if(0!=leftColor){
      tv_left.setTextColor(getContext().getResources().getColor(leftColor));
    }
    if (0 != mContentTextColor) {
      tv_content.setTextColor(mContentTextColor);
    }
  }

  private void init() {
    mAnimIn = AnimationLoader.getInAnimation(getContext());
    mAnimOut = AnimationLoader.getOutAnimation(getContext());
    initAnimListener();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  private void initAnimListener() {
    mAnimOut.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
      }

      @Override
      public void onAnimationEnd(Animation animation) {
      }

      @Override
      public void onAnimationRepeat(Animation animation) {
      }
    });
  }

  private void callDismiss() {
    super.dismiss();
  }

  public CustomDialog setAnimationEnable(boolean enable) {
    mIsShowAnim = enable;
    return this;
  }

  public CustomDialog setAnimationIn(AnimationSet animIn) {
    mAnimIn = animIn;
    return this;
  }

  public CustomDialog setAnimationOut(AnimationSet animOut) {
    mAnimOut = animOut;
    initAnimListener();
    return this;
  }

  public CustomDialog setColor(int color) {
    mBackgroundColor = color;
    return this;
  }

  public CustomDialog setColor(String color) {
    try {
      setColor(Color.parseColor(color));
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    return this;
  }

  public CustomDialog setTitleTextColor(int color) {
    mTitleTextColor = color;
    return this;
  }

  public CustomDialog setTitleTextColor(String color) {
    try {
      setTitleTextColor(Color.parseColor(color));
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    return this;
  }

  public CustomDialog setContentTextColor(int color) {
    mContentTextColor = color;
    return this;
  }

  public CustomDialog setContentTextColor(String color) {
    try {
      setContentTextColor(Color.parseColor(color));
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    return this;
  }

  public CustomDialog setPositiveListener(@ColorRes int color, CharSequence text, OnPositiveListener l) {
    mPositiveText = text;
    mPositiveListener = l;
    leftColor= color;
    return this;
  }

  public CustomDialog setPositiveListener(@ColorRes int color,int textId, OnPositiveListener l) {
    return setPositiveListener(color,getContext().getText(textId), l);
  }

  public CustomDialog setNegativeListener(@ColorRes int color, CharSequence text, OnNegativeListener l) {
    rightColor=color;
    mNegativeText = text;
    mNegativeListener = l;
    return this;
  }

  public CustomDialog setNegativeListener(@ColorRes int color,int textId, OnNegativeListener l) {
    return setNegativeListener(color,getContext().getText(textId), l);
  }

  public CustomDialog setContentText(CharSequence text) {
    mContentText = text;
    return this;
  }

  public CustomDialog setContentText(int textId) {
    return setContentText(getContext().getText(textId));
  }

  public CharSequence getContentText() {
    return mContentText;
  }

  public CharSequence getTitleText() {
    return mTitleText;
  }

  public CharSequence getPositiveText() {
    return mPositiveText;
  }

  public CharSequence getNegativeText() {
    return mNegativeText;
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();

    if (R.id.btnPositive == id) {
      mPositiveListener.onClick(this);
    } else if (R.id.btnNegative == id) {
      mNegativeListener.onClick(this);
    } else {
    }
  }
  public interface OnPositiveListener {
    void onClick(CustomDialog dialog);
  }

  public interface OnNegativeListener {
    void onClick(CustomDialog dialog);
  }
}
