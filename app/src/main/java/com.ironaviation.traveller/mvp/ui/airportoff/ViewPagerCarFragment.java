package com.ironaviation.traveller.mvp.ui.airportoff;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.entity.PayInfo;

/**
 * Created by Dennis on 2017/6/12.
 */

public class ViewPagerCarFragment extends Fragment{

    private int status;
    private ImageView mImageView;
    private TextView twCarType,twPrice,twConcessions,twMultiple;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_car,container,false);
        mImageView = (ImageView) view.findViewById(R.id.iw_car);
        twCarType = (TextView) view.findViewById(R.id.tw_car_type);
        twPrice = (TextView) view.findViewById(R.id.tw_price);
        twConcessions = (TextView) view.findViewById(R.id.tw_concessions);
        twMultiple = (TextView) view.findViewById(R.id.tw_multiple);
        Bundle bundle = getArguments();
        if(bundle != null) {
            status = bundle.getInt(Constant.STATUS, 0);
            PayInfo info = (PayInfo) bundle.getSerializable("test");
            Log.e("kkk",status+"  "+info.getBID());
        }
        setData();
        return view;
    }

    public void setData(){
        mImageView.setImageResource(R.drawable.btn_login_code_shap);
        twCarType.setText(Html.fromHtml("<font color='#666666'> " + "豪华型  " +
                "</font>"+"<font color='#A6A6A6'> " + "准载4人" + "</font>"));
        twPrice.setText(20+"");
        twConcessions.setText("优惠8元");
        twMultiple.setText("高峰期预估价 x1.2倍");
    }
}
