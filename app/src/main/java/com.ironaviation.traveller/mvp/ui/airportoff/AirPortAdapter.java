package com.ironaviation.traveller.mvp.ui.airportoff;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.CheckIdCardUtils;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.model.entity.request.AirPortRequest;
import com.ironaviation.traveller.mvp.model.entity.response.AirPortResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelResponse;
import com.jess.arms.utils.UiUtils;

import java.util.List;

/**
 * 项目名称：Transfer
 * 类描述：
 * 创建人：flq
 * 创建时间：2017/3/29 8:49
 * 修改人：
 * 修改时间：2017/3/29 8:49
 * 修改备注：
 */

public class AirPortAdapter extends /*BaseAdapter implements TextWatcher*/ RecyclerView.Adapter<AirPortHolder> implements TextWatcher{

    private List<AirPortRequest> mAirPortRequests;
    private ItemIdCallBack mCallBack;
    private AirPortHolder holder;
    private int position;
    private ItemIdStatusCallBack mItemIdStatusCallBack;

    public AirPortAdapter(ItemIdCallBack mCallBack,ItemIdStatusCallBack mItemIdStatusCallBack){
        this.mCallBack = mCallBack;
        this.mItemIdStatusCallBack = mItemIdStatusCallBack;
    }

    public void setData(List<AirPortRequest> mAirPortRequests){
        this.mAirPortRequests = mAirPortRequests;
        notifyDataSetChanged();
    }

    @Override
    public AirPortHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_public_view,parent,false);
        return new AirPortHolder(view);
    }

    @Override
    public void onBindViewHolder(final AirPortHolder holder, final int position) {
        this.holder = holder;
        this.position = position;
        if(mAirPortRequests.get(position).getStatus() == Constant.AIRPORT_SUCCESS){
            holder.ivStatus.setImageResource(R.mipmap.ic_success);
            holder.etText.setText(mAirPortRequests.get(position).getIdCard());
            holder.twCode.setVisibility(View.GONE);
            holder.ivStatus.setVisibility(View.VISIBLE);
            holder.etText.addTextChangedListener(this);
        }else if(mAirPortRequests.get(position).getStatus() == Constant.AIRPORT_FAILURE){
            holder.ivStatus.setImageResource(R.mipmap.ic_failure);
            holder.etText.setText(mAirPortRequests.get(position).getIdCard());
            holder.twCode.setVisibility(View.GONE);
            holder.ivStatus.setVisibility(View.VISIBLE);
            holder.etText.addTextChangedListener(this);
        }else{
            if(mAirPortRequests.get(position).getIdCard() != null) {
                holder.etText.setText(mAirPortRequests.get(position).getIdCard());
            }else{
                holder.etText.setText("");
            }
            holder.twCode.setVisibility(View.VISIBLE);
            holder.ivStatus.setVisibility(View.GONE);
        }

        if(position == mAirPortRequests.size()-1){
            holder.line.setVisibility(View.GONE);
        }else{
            holder.line.setVisibility(View.VISIBLE);
        }

        if(mAirPortRequests.get(position) != null && mAirPortRequests.get(position).getIdCard()!=null &&
                mAirPortRequests.get(position).getIdCard().length() > 0) {
            holder.etText.setSelection(mAirPortRequests.get(position).getIdCard().length());
        }
        holder.twCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.etText.getText().toString().trim() != null && CheckIdCardUtils.validateCard(holder.etText.getText().toString().trim())) {
                    mCallBack.getItem(holder.etText.getText().toString().trim(), position);
                }else{
                    UiUtils.makeText("请输入正确身份证号码:"+holder.etText.getText().toString().trim()+"  "+position);
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(mAirPortRequests.get(position).getStatus() == Constant.AIRPORT_SUCCESS
                || mAirPortRequests.get(position).getStatus() == Constant.AIRPORT_FAILURE) {
            /*holder.twCode.setVisibility(View.VISIBLE);
            holder.ivStatus.setVisibility(View.GONE);*/
            mItemIdStatusCallBack.getStatusItem(editable.toString().trim(),
                    position,Constant.AIRPORT_NO);
            mAirPortRequests.get(position).setStatus(Constant.AIRPORT_NO);
//            holder.ivStatus.setImageResource(R.mipmap.ic_failure);
        }
    }

    public interface ItemIdStatusCallBack{
        void getStatusItem(String cardId,int position,int status);
    }
    public interface ItemIdCallBack{
        void getItem(String cardId,int position);
    }

    @Override
    public int getItemCount() {
        return mAirPortRequests != null ? mAirPortRequests.size():0;
    }
//}

   /* ImageView ivLogo;
    ImageView ivStatus;
    EditText etText;
    TextView twCode;
    View line;
    private List<AirPortRequest> mAirPortRequests;
    private ItemIdCallBack mCallBack;
    private Context mContext;
    private String idCard;
    private int position;

    public AirPortAdapter(Context context, ItemIdCallBack mCallBack){
        this.mCallBack = mCallBack;
        this.mContext = context;
    }
    public void setData(List<AirPortRequest> airPortRequestList){
        this.mAirPortRequests = airPortRequestList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mAirPortRequests != null ? mAirPortRequests.size() : 0 ;
    }

    @Override
    public Object getItem(int i) {
        return mAirPortRequests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        position = i;
        View myView = LayoutInflater.from(mContext).inflate(R.layout.include_public_view,viewGroup,false);
        ivLogo = (ImageView) myView.findViewById(R.id.iv_logo);
        ivStatus = (ImageView) myView.findViewById(R.id.iv_status);
        etText = (EditText) myView.findViewById(R.id.edt_content);
        twCode = (TextView) myView.findViewById(R.id.tv_code);
        line = myView.findViewById(R.id.line_edt);
        etText.addTextChangedListener(this);

        if(mAirPortRequests.get(i).getStatus() == Constant.AIRPORT_SUCCESS){
            showStatus();
            ivStatus.setImageResource(R.mipmap.ic_success);
        }else if(mAirPortRequests.get(i).getStatus() == Constant.AIRPORT_FAILURE){
            showStatus();
            ivStatus.setImageResource(R.mipmap.ic_failure);
        }else if(mAirPortRequests.get(i).getStatus() == Constant.AIRPORT_NO){
            showCode();
        }

        if(i == mAirPortRequests.size()-1){
            line.setVisibility(View.GONE);
        }else{
            line.setVisibility(View.VISIBLE);
        }

        twCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(idCard != null && CheckIdCardUtils.validateCard(idCard)) {
                    mCallBack.getItem(idCard, i);
                }else{
                    UiUtils.makeText("请输入正确身份证号码:"+idCard+"  "+i);
                }
            }
        });
        return myView;
    }

    public void showCode(){
        ivStatus.setVisibility(View.GONE);
        twCode.setVisibility(View.VISIBLE);
    }

    public void showStatus(){
        ivStatus.setVisibility(View.VISIBLE);
        twCode.setVisibility(View.GONE);
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        idCard = editable.toString().trim();
        if(mAirPortRequests.get(position).getStatus() == Constant.AIRPORT_FAILURE ||
                mAirPortRequests.get(position).getStatus() == Constant.AIRPORT_SUCCESS) {
            showStatus();
        }
    }

    public interface ItemIdCallBack{
        void getItem(String cardId,int position);
    }*/
}
