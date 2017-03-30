package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.ironaviation.traveller.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Title: CustomerRatingBar
 * Description:自定义星级控件
 * Copyright:Copyright(c)2016
 * Company:
 * CreateTime:16/5/17  上午12:51
 *
 * @author dengqinsheng
 * @version 1.0
 */
public class CustomerRatingBar extends LinearLayout {
    private boolean mClickable;
    private int starCount;
    private OnRatingChangeListener onRatingChangeListener;
    private float starImageSize;
    private Drawable starEmptyDrawable;
    private Drawable starFillDrawable;
    private Drawable starHalfDrawable;
    private ImageView iv_star_1;
    private ImageView iv_star_2;
    private ImageView iv_star_3;
    private ImageView iv_star_4;
    private ImageView iv_star_5;
    List<ImageView> mImageViewList = new ArrayList<>();

    public void setStarHalfDrawable(Drawable starHalfDrawable) {
        this.starHalfDrawable = starHalfDrawable;
    }


    public void setOnRatingChangeListener(OnRatingChangeListener onRatingChangeListener) {
        this.onRatingChangeListener = onRatingChangeListener;
    }

    public void setmClickable(boolean clickable) {
        this.mClickable = clickable;
    }

    public void setStarFillDrawable(Drawable starFillDrawable) {
        this.starFillDrawable = starFillDrawable;
    }

    public void setStarEmptyDrawable(Drawable starEmptyDrawable) {
        this.starEmptyDrawable = starEmptyDrawable;
    }

    public void setStarImageSize(float starImageSize) {
        this.starImageSize = starImageSize;
    }


    /**
     * @param context
     * @param attrs
     */
    public CustomerRatingBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        starImageSize = mTypedArray.getDimension(R.styleable.RatingBar_starImageSize, 20);
        starCount = mTypedArray.getInteger(R.styleable.RatingBar_starCount, 5);
        starEmptyDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starEmpty);
        starFillDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starFill);
        starHalfDrawable = mTypedArray.getDrawable(R.styleable.RatingBar_starHalf);
        mClickable = mTypedArray.getBoolean(R.styleable.RatingBar_clickable, true);
        View v = LayoutInflater.from(context).inflate(R.layout.view_rating_bar, this);
        iv_star_1 = (ImageView) v.findViewById(R.id.iv_star_1);
        iv_star_2 = (ImageView) v.findViewById(R.id.iv_star_2);
        iv_star_3 = (ImageView) v.findViewById(R.id.iv_star_3);
        iv_star_4 = (ImageView) v.findViewById(R.id.iv_star_4);
        iv_star_5 = (ImageView) v.findViewById(R.id.iv_star_5);
        mImageViewList.add(iv_star_1);
        mImageViewList.add(iv_star_2);
        mImageViewList.add(iv_star_3);
        mImageViewList.add(iv_star_4);
        mImageViewList.add(iv_star_5);
        for (int i = 0; i < starCount; ++i) {
            ImageView imageView = mImageViewList.get(i);
            imageView.setImageDrawable(starEmptyDrawable);
            imageView.setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mClickable) {
                                int num = setStar(v);
                                if (onRatingChangeListener != null) {
                                    onRatingChangeListener.onRatingChange(num);
                                }
                            }

                        }
                    }
            );
        }
    }

    /**
     * @param context
     * @param attrs
     * @return
     */
    private ImageView getStarImageView(Context context, AttributeSet attrs) {
        ImageView imageView = new ImageView(context);
        LinearLayout.LayoutParams para = new LinearLayout.LayoutParams(
                Math.round(starImageSize),
                Math.round(starImageSize)
        );
        para.setMargins(0, 0, 30, 0);
        imageView.setLayoutParams(para);
        imageView.setPadding(0, 0, 5, 0);

        imageView.setImageDrawable(starEmptyDrawable);
        //imageView.setMaxWidth(10);
        //imageView.setMaxHeight(10);
        return imageView;

    }


    /**
     * setting start
     *
     * @param
     */

    public int setStar(View view) {
        int start = 0;

        switch (view.getId()) {
            case R.id.iv_star_1:
                start = 0;
                break;
            case R.id.iv_star_2:
                start = 1;
                break;
            case R.id.iv_star_3:
                start = 2;
                break;
            case R.id.iv_star_4:
                start = 3;
                break;
            case R.id.iv_star_5:
                start = 4;
                break;
        }
        for (int j = 0; j <= starCount - 1; j++) {
            if (j <= start) {
                mImageViewList.get(j).setImageDrawable(starFillDrawable);
            } else {
                mImageViewList.get(j).setImageDrawable(starEmptyDrawable);

            }

        }
        return start;
   /*     //浮点数的整数部分
        int fint = (int) starCount;
        BigDecimal b1 = new BigDecimal(Float.toString(starCount));
        BigDecimal b2 = new BigDecimal(Integer.toString(fint));
        //浮点数的小数部分
        float fPoint = b1.subtract(b2).floatValue();


        starCount = fint > this.starCount ? this.starCount : fint;
        starCount = starCount < 0 ? 0 : starCount;

        //drawfullstar
        for (int i = 0; i < starCount; ++i) {
            ((ImageView) getChildAt(i)).setImageDrawable(starFillDrawable);
        }

        //drawhalfstar
        if (fPoint > 0) {
            ((ImageView) getChildAt(fint)).setImageDrawable(starHalfDrawable);

            //drawemptystar
            for (int i = this.starCount - 1; i >= starCount + 1; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }


        } else {
            //drawemptystar
            for (int i = this.starCount - 1; i >= starCount; --i) {
                ((ImageView) getChildAt(i)).setImageDrawable(starEmptyDrawable);
            }

        }*/


    }


    /**
     * change stat listener
     */
    public interface OnRatingChangeListener {

        void onRatingChange(int RatingCount);

    }

}
