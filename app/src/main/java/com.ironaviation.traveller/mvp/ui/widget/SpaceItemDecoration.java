package com.ironaviation.traveller.mvp.ui.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-04-06 13:29
 * 修改人：starRing
 * 修改时间：2017-04-06 13:29
 * 修改备注：
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public SpaceItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {


        if (parent.getChildLayoutPosition(view) % 2 == 0) {

            outRect.right = space;
        } else {
            outRect.left = space;

        }
    }
}