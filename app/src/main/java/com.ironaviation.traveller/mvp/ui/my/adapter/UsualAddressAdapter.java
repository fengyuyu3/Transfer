package com.ironaviation.traveller.mvp.ui.my.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.WEApplication;
import com.ironaviation.traveller.mvp.model.entity.request.UpdateAddressBookRequest;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：ScalDriver
 * 类描述：
 * 创建时间：2017-01-04 20:58
 * 修改时间：2017-01-04 20:58
 * 修改备注：
 */
public class UsualAddressAdapter extends SwipeMenuAdapter<RecyclerView.ViewHolder> {


    private List<UpdateAddressBookRequest> items = new ArrayList<>();
    private UsualAddressAdapter.OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public UsualAddressAdapter(Context context) {

        mContext = context;
    }

    public void setData(List<UpdateAddressBookRequest> mUpdateAddressBookRequests) {
        if (mUpdateAddressBookRequests != null) {
            items = mUpdateAddressBookRequests;
        } else {
            items = new ArrayList<>();

        }
        notifyDataSetChanged();

    }

    public void setOnItemClickListener(UsualAddressAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }


    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    public UpdateAddressBookRequest getItem(int position) {
        return items.get(position);
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usual_address, parent, false);


        return view;
    }

    @Override
    public RecyclerView.ViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ((DefaultViewHolder) holder).setData(items.get(position));
        ((DefaultViewHolder) holder).setView(items.get(position).getAddressName());
        ((DefaultViewHolder) holder).setOnItemClickListener(mOnItemClickListener);


    }

    class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_usual_address_type;
        ImageView iv_usual_address_logo;
        TextView tv_usual_address_address;
        UsualAddressAdapter.OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tv_usual_address_type = (TextView) itemView.findViewById(R.id.tv_usual_address_type);
            tv_usual_address_address = (TextView) itemView.findViewById(R.id.tv_usual_address_address);
            iv_usual_address_logo = (ImageView) itemView.findViewById(R.id.iv_usual_address_logo);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(UsualAddressAdapter.OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(UpdateAddressBookRequest itemModel) {
            //this.tv_usual_address_type.setText(itemModel.getAddressName());
            if (!TextUtils.isEmpty(itemModel.getAddressName())) {
                this.tv_usual_address_type.setText(itemModel.getAddressName());
            }
            if (!TextUtils.isEmpty(itemModel.getAddress())) {
                this.tv_usual_address_address.setText(itemModel.getAddress());
                this.tv_usual_address_address.setTextColor(ContextCompat.getColor(WEApplication.getContext(), R.color.word_already_input));


            }else {
                this.tv_usual_address_address.setTextColor(ContextCompat.getColor(WEApplication.getContext(), R.color.word_wait_input));
                setHint(itemModel.getAddressName());
            }

        }

        public void setView(String itemModel) {
            if (itemModel.equals(mContext.getString(R.string.home))) {
                iv_usual_address_logo.setBackgroundResource(R.mipmap.ic_home);

            } else if (itemModel.equals(mContext.getString(R.string.company))) {
                iv_usual_address_logo.setBackgroundResource(R.mipmap.ic_business);
            }

        }
        public void setHint(String itemModel) {
            if (itemModel.equals(mContext.getString(R.string.home))) {
                this.tv_usual_address_address.setText(mContext.getString(R.string.hint_home_address));

            } else if (itemModel.equals(mContext.getString(R.string.company))) {
                this.tv_usual_address_address.setText(mContext.getString(R.string.hint_company_address));
            }

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {

                mOnItemClickListener.onItemClick(getAdapterPosition());

            }
        }
    }


    public interface OnItemClickListener {

        void onItemClick(int position);

    }
}