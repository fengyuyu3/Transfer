package com.ironaviation.traveller.mvp.presenter.my;

import android.app.Application;
import android.text.TextUtils;

import com.baidu.mapapi.search.core.PoiInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.AddressContract;
import com.ironaviation.traveller.mvp.model.entity.AddressHistory;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.HistoryPoiInfo;
import com.ironaviation.traveller.mvp.model.entity.request.AddressLimitRequest;
import com.ironaviation.traveller.mvp.model.entity.request.UpdateAddressBookRequest;
import com.ironaviation.traveller.mvp.model.entity.response.AddressResponse;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import javax.inject.Inject;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */


/**
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-31 11:40
 * 修改人：starRing
 * 修改时间：2017-03-31 11:40
 * 修改备注：
 */
@ActivityScope
public class AddressPresenter extends BasePresenter<AddressContract.Model, AddressContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;

    @Inject
    public AddressPresenter(AddressContract.Model model, AddressContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void saveAddress(HistoryPoiInfo position) {

        AddressHistory addressHistory = null;
        position.setFlagHistory(true);
        position.postCode = Constant.ADDRESS_HISTORY;
        String history = DataHelper.getStringSF(mApplication, Constant.ADDRESS_HISTORY);
        if (!TextUtils.isEmpty(history)) {
            addressHistory = new Gson().fromJson(history, AddressHistory.class);

        } else {
            addressHistory = new AddressHistory();
            addressHistory.setPoiInfos(new ArrayList<HistoryPoiInfo>());
        }
        for (int i = 0; i < addressHistory.getPoiInfos().size(); i++) {
            if (position.uid.equals(addressHistory.getPoiInfos().get(i).uid)) {
                addressHistory.getPoiInfos().remove(addressHistory.getPoiInfos().get(i));
                i--;
            }

        }
        addressHistory.getPoiInfos().add(0, position);
        if (addressHistory.getPoiInfos().size()>10){
            addressHistory.getPoiInfos().remove(addressHistory.getPoiInfos().size()-1);
        }
        DataHelper.saveDeviceDataToString(mApplication, Constant.ADDRESS_HISTORY, addressHistory);
    }

    public AddressHistory getAddress() {
        AddressHistory addressHistory = null;
        String history = DataHelper.getStringSF(mApplication, Constant.ADDRESS_HISTORY);
        if (!TextUtils.isEmpty(history)) {
            addressHistory = new Gson().fromJson(history, AddressHistory.class);

        } else {
            addressHistory = new AddressHistory();
            addressHistory.setPoiInfos(new ArrayList<HistoryPoiInfo>());
        }
        return addressHistory;
    }

    public void setDefaultData(List<UpdateAddressBookRequest> updateAddressBookRequests) {
        if (updateAddressBookRequests == null || updateAddressBookRequests.size() == 0) {

            UpdateAddressBookRequest home = new UpdateAddressBookRequest();
            home.setAddressName(mApplication.getString(R.string.home));
            home.setViewType(1);
            UpdateAddressBookRequest company = new UpdateAddressBookRequest();
            company.setViewType(1);
            company.setAddressName(mApplication.getString(R.string.company));
            updateAddressBookRequests.add( home);
            updateAddressBookRequests.add( company);

        } else if (updateAddressBookRequests.size() == 1) {
            if (updateAddressBookRequests.get(0).getAddressName().equals(mApplication.getString(R.string.home))) {

                UpdateAddressBookRequest company = new UpdateAddressBookRequest();
                company.setAddressName(mApplication.getString(R.string.company));
                updateAddressBookRequests.add(1, company);
                company.setViewType(1);

            }
            if (updateAddressBookRequests.get(0).getAddressName().equals(mApplication.getString(R.string.company))) {

                UpdateAddressBookRequest home = new UpdateAddressBookRequest();
                home.setAddressName(mApplication.getString(R.string.home));
                home.setViewType(1);
                updateAddressBookRequests.add(0, home);
            }
        }

    }


    public void getUserAddressBook() {
        mModel.getUserAddressBook()
                .compose(RxUtils.<BaseData<List<UpdateAddressBookRequest>>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<List<UpdateAddressBookRequest>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<List<UpdateAddressBookRequest>> loginEntityBaseData) {

                        if (loginEntityBaseData.getData() == null || loginEntityBaseData.getData().size() < 2) {
                            setDefaultData(loginEntityBaseData.getData());
                        }
                        mRootView.setView(loginEntityBaseData.getData());

                    }
                });

    }

    public void isAddress(String address, double longitude, double latitude, final HistoryPoiInfo info){
        mModel.isAddress(address,longitude,latitude)
                .compose(RxUtils.<BaseData<AddressResponse>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<AddressResponse>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<AddressResponse> booleanBaseData) {
                        if(booleanBaseData.isSuccess()){
                            if(booleanBaseData.getData() != null) {
                                if(booleanBaseData.getData().isValid()) {
                                    mRootView.isAddressSuccess(booleanBaseData.getData().isValid(), info);
                                }else{
                                    mRootView.showMessage(booleanBaseData.getData().getMessage());
                                }
                            }else{
                                mRootView.showMessage("数据出错,请重新请求");
                            }
                        }else{
                            mRootView.showMessage(booleanBaseData.getMessage());
                        }
                    }
                });

    }


}