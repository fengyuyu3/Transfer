package com.ironaviation.traveller.mvp.model.entity.response;

import java.util.List;

/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-29 15:48
 * 修改人：starRing
 * 修改时间：2017-03-29 15:48
 * 修改备注：
 */
public class UsualAddressResponse {


    public List<UsualAddress> getUsualAddressList() {
        return mUsualAddressList;
    }

    public void setUsualAddressList(List<UsualAddress> usualAddressList) {
        mUsualAddressList = usualAddressList;
    }

    private List<UsualAddress> mUsualAddressList;

    public class UsualAddress {
        private int viewType;//划分是否可以右滑

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public void setType(int type) {
            this.type = type;
        }

        private String typeName;//类型

        public int getType() {
            return type;
        }

        private int type;//类型

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }


        private String address;//地址

        public int getViewType() {
            return viewType;
        }

        public void setViewType(int viewType) {
            this.viewType = viewType;
        }


    }
}
