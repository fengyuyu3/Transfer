package com.ironaviation.traveller.mvp.ui.travel;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.travel.DaggerTravelComponent;
import com.ironaviation.traveller.di.module.travel.TravelModule;
import com.ironaviation.traveller.mvp.contract.travel.TravelContract;
import com.ironaviation.traveller.mvp.model.entity.response.TravelResponse;
import com.ironaviation.traveller.mvp.presenter.travel.TravelPresenter;
import com.jess.arms.utils.UiUtils;


import java.util.List;

import static com.jess.arms.utils.Preconditions.checkNotNull;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 *
 * 项目名称：Transfer      
 * 类描述：   
 * 创建人：flq  
 * 创建时间：2017/3/26 17:29   
 * 修改人：  
 * 修改时间：2017/3/26 17:29   
 * 修改备注：   
 * @version
 *
 */

public class TravelActivity extends WEActivity<TravelPresenter> implements TravelContract.View {


    private TravelAdapter mTravelAdapter;
    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerTravelComponent
                .builder()
                .appComponent(appComponent)
                .travelModule(new TravelModule(this)) //请将TravelModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected void nodataRefresh() {

    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_travel, null, false);
    }

    @Override
    protected void initData() {
        mTravelAdapter = new TravelAdapter(R.layout.item_travel);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        UiUtils.SnackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        UiUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void setDatas(List<TravelResponse> mTravelResponses) {
        mTravelAdapter.setNewData(mTravelResponses);
    }

    @Override
    public void setNodata() {
        showNodata(true);
    }

    @Override
    public void setError() {

    }
}
