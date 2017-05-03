package com.ironaviation.traveller.mvp.presenter.my;

import android.app.Application;

import com.google.gson.JsonObject;
import com.ironaviation.traveller.app.EventBusTags;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.my.travel.AddressSearchContract;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.HistoryPoiInfo;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.widget.imageloader.ImageLoader;

import org.simple.eventbus.EventBus;

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
 * 创建时间：2017/5/2 19:40
 * 修改人：starRing
 * 修改时间：2017/5/2 19:40
 * 修改备注：
 */
@ActivityScope
public class AddressSearchPresenter extends BasePresenter<AddressSearchContract.Model, AddressSearchContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private WEActivity weActivity;

    @Inject
    public AddressSearchPresenter(AddressSearchContract.Model model, AddressSearchContract.View rootView
            , RxErrorHandler handler, Application application
            , ImageLoader imageLoader, AppManager appManager) {
        super(model, rootView);
        this.mErrorHandler = handler;
        this.mApplication = application;
        this.mImageLoader = imageLoader;
        this.mAppManager = appManager;
        this.weActivity = (WEActivity) mAppManager.getCurrentActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }


    public void updateAddressBook(String UABID, HistoryPoiInfo position, int addressType) {
        switch (addressType) {
            case Constant.ADDRESS_TYPE_COMPANY:
            case Constant.ADDRESS_USUAl_COMPANY:
                updateAddressBook(UABID, Constant.COMPANY, position.name, position.location.longitude, position.location.latitude, addressType);

                break;
            case Constant.ADDRESS_TYPE_HOME:
            case Constant.ADDRESS_USUAl_HOME:
                updateAddressBook(UABID, Constant.HOME, position.name, position.location.longitude, position.location.latitude, addressType);

                break;

        }

    }

    private void updateAddressBook(String UABID,
                                   String AddressName,
                                   String Address,
                                   double Longitude,
                                   double Latitude,
                                   final int addressType) {

        mModel.updateAddressBook(UABID, AddressName, Address, Longitude, Latitude)
                .compose(RxUtils.<BaseData<List<JsonObject>>>applySchedulers(mRootView))
                .subscribe(new ErrorHandleSubscriber<BaseData<List<JsonObject>>>(mErrorHandler) {
                    @Override
                    public void onNext(BaseData<List<JsonObject>> loginEntityBaseData) {
                        if (loginEntityBaseData.isSuccess()) {
                            switch (addressType) {
                                case Constant.ADDRESS_TYPE_COMPANY:
                                case Constant.ADDRESS_TYPE_HOME:
                                    EventBus.getDefault().post(EventBusTags.USUAL_ADDRESS, EventBusTags.USUAL_ADDRESS);
                                    break;
                                case Constant.ADDRESS_USUAl_COMPANY:
                                case Constant.ADDRESS_USUAl_HOME:
                                    EventBus.getDefault().post(EventBusTags.USUAL_ADDRESS, EventBusTags.ADDRESS);
                                    break;
                            }
                            mAppManager.getCurrentActivity().finish();


                        } else {
                        }
                    }
                });
    }
}