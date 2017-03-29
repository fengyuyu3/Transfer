package com.ironaviation.traveller.mvp.ui.my;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.mvp.model.entity.response.UsualAddressResponse;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.zhy.autolayout.AutoRelativeLayout;

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

    private UsualAddressResponse mUsualAddressResponse = new UsualAddressResponse();

    private List<UsualAddressResponse.UsualAddress> items = new ArrayList<>();
    private UsualAddressAdapter.OnItemClickListener mOnItemClickListener;
    private Context mContext;

    public UsualAddressAdapter(Context context) {

        mContext = context;
    }

    public void setData(UsualAddressResponse mUsualAddressResponse) {
        this.mUsualAddressResponse = mUsualAddressResponse;
        if (mUsualAddressResponse.getUsualAddressList() != null) {
            items = mUsualAddressResponse.getUsualAddressList();
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
        ((DefaultViewHolder) holder).setView(items.get(position).getViewType());
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

        public void setData(UsualAddressResponse.UsualAddress itemModel) {
            this.tv_usual_address_type.setText(itemModel.getTypeName());

            this.tv_usual_address_address.setText(itemModel.getAddress());
        }

        public void setView(int itemModel) {
            switch (itemModel) {
                case 0:
                    iv_usual_address_logo.setBackgroundResource(R.mipmap.ic_home);
                    break;
                case 1:
                    iv_usual_address_logo.setBackgroundResource(R.mipmap.ic_business);
                    break;

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