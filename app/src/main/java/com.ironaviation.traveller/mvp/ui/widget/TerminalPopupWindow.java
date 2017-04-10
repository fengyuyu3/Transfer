package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/8 0008.
 */

public class TerminalPopupWindow extends PopupWindow {
    private Context context;
    private ListView listview;
    private List<String> list;
    private int count ;
    private TerminalAdapter mTerminalAdapter;
    private CallBackItem callBack;
    private AutoRelativeLayout rlTerminal;
    public TerminalPopupWindow(Context context,List<String> list,CallBackItem callBack)
    {
        if(list != null) {
            this.list = list;
        }else{
            this.list = new ArrayList<>();
        }
        this.callBack = callBack;
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_terminal,null,false);
        setContentView(view);
        setWidth(AutoLinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(AutoLinearLayout.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        listview = (ListView) view.findViewById(R.id.lw_terminal);
        rlTerminal = (AutoRelativeLayout) view.findViewById(R.id.rl_terminal);
        setClippingEnabled(false);
        rlTerminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mTerminalAdapter = new TerminalAdapter();
        listview.setAdapter(mTerminalAdapter);
    }
    public void show(View view){
        if(!isShowing()) {
            showAtLocation(view, Gravity.CENTER, 0, 0);
        }else{
            dismiss();
        }
    }

    public void setNum(int mCount){
        count = mCount;
        if(mTerminalAdapter != null) {
            mTerminalAdapter.notifyDataSetChanged();
        }
    }


    public class TerminalAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            View view1  =  LayoutInflater.from(context).inflate(R.layout.item_terminal,viewGroup,false);
            TextView tw = (TextView) view1.findViewById(R.id.tw_terminal);
            ImageView iw = (ImageView) view1.findViewById(R.id.iw_point);
            AutoLinearLayout mLlTerminal = (AutoLinearLayout) view1.findViewById(R.id.ll_terminal);
            tw.setText(list.get(position));
            if(count == position){
                iw.setImageResource(R.mipmap.ic_green_point);
                tw.setTextColor(context.getResources().getColor(R.color.word_already_input));
            }else{
                iw.setImageResource(R.mipmap.ic_blue_point);
                tw.setTextColor(context.getResources().getColor(R.color.airport_edit_gray));
            }
            mLlTerminal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.getItem(position);
                }
            });
            return view1;
        }
    }
    public interface CallBackItem{
        void getItem(int position);
    }
}
