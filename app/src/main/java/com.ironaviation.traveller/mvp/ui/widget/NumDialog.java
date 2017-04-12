package com.ironaviation.traveller.mvp.ui.widget;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.ironaviation.traveller.R;

import java.util.List;

/**
 * Created by flq on 2016/10/20.
 */
public class NumDialog {

    private AlertDialog.Builder builder;
    private AlertDialog alert;
    private Context context;
    private ListView listview;
    private NumAdapter adapter;
    private List<String> list;
    private CallBackItem callBack;
    private int type;
    public NumDialog(Context context, List<String> list,CallBackItem callBack,int type)
    {
        if(list != null) {
            this.list = list;
        }
        this.callBack = callBack;
        this.type = type;
        builder = new AlertDialog.Builder(context);
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_zodiac,null,false);
        builder.setView(view);
        builder.setCancelable(true);
        alert = builder.create();
        listview = (ListView) view.findViewById(R.id.listview_zodiac);
        adapter = new NumAdapter();
        listview.setAdapter(adapter);
    }
    public void show(){
        if(!alert.isShowing()) {
            alert.show();
        }
    }
    public void dismiss(){
        if(alert.isShowing()) {
            alert.dismiss();
        }
    }


    public class NumAdapter extends BaseAdapter {

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
            View view1  =  LayoutInflater.from(context).inflate(R.layout.list_item_zodiac,viewGroup,false);
            TextView tw = (TextView) view1.findViewById(R.id.item_zocdiac);
            tw.setText(list.get(position));
            tw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.getItem(position,type);
                }
            });
            return view1;
        }
    }
    public interface CallBackItem{
        void getItem(int position,int type);
    }
}
