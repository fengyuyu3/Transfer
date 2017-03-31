package com.ironaviation.traveller.mvp.ui.my;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.common.AppComponent;
import com.ironaviation.traveller.common.WEActivity;
import com.ironaviation.traveller.di.component.my.DaggerUsualAddressComponent;
import com.ironaviation.traveller.di.module.my.UsualAddressModule;
import com.ironaviation.traveller.mvp.contract.my.UsualAddressContract;
import com.ironaviation.traveller.mvp.model.entity.response.TravelResponse;
import com.ironaviation.traveller.mvp.model.entity.response.UsualAddressResponse;
import com.ironaviation.traveller.mvp.presenter.my.UsualAddressPresenter;
import com.ironaviation.traveller.mvp.ui.widget.AutoSwipeMenuRecyclerView;
import com.jess.arms.utils.UiUtils;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
 * 项目名称：Traveller
 * 类描述：
 * 创建人：starRing
 * 创建时间：2017-03-29 15:05
 * 修改人：starRing
 * 修改时间：2017-03-29 15:05
 * 修改备注：
 */
public class UsualAddressActivity extends WEActivity<UsualAddressPresenter> implements UsualAddressContract.View {


    @BindView(R.id.recycler_view)
    AutoSwipeMenuRecyclerView mRecyclerView;
    UsualAddressAdapter mUsualAddressAdapter;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerUsualAddressComponent
                .builder()
                .appComponent(appComponent)
                .usualAddressModule(new UsualAddressModule(this)) //请将UsualAddressModule()第一个首字母改为小写
                .build()
                .inject(this);
    }

    @Override
    protected View initView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_usual_address, null, false);
    }

    @Override
    protected void initData() {

        setTitle(getString(R.string.usual_address));
        setNavigationIcon(ContextCompat.getDrawable(this, R.mipmap.ic_back));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置菜单创建器。
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));// 布局管理器。
        mRecyclerView.setHasFixedSize(true);// 如果Item够简单，高度是确定的，打开FixSize将提高性能。
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        //mRecyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
        mRecyclerView.setSwipeMenuCreator(swipeMenuCreator);
        //mRecyclerView.getChildAt(0).setEnabled(false);
        //设置菜单Item点击监听。
        mRecyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
        mUsualAddressAdapter = new UsualAddressAdapter(this);
        mUsualAddressAdapter.setOnItemClickListener(onItemClickListener);
        mRecyclerView.setAdapter(mUsualAddressAdapter);
        mUsualAddressAdapter.setData(getData());
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
    protected void nodataRefresh() {

    }


    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            if (viewType == 0) {
                SwipeMenuItem addItem = new SwipeMenuItem(UsualAddressActivity.this)
                        .setBackgroundDrawable(R.color.bg_champagne)
                        .setText("      删除      ") // 文字。
                        .setTextColor(Color.WHITE) // 文字颜色
                        // 。
                        .setTextSize(14) // 文字大小。
                        .setWeight(height)
                        .setHeight(height);

                swipeRightMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。
            }


            // 上面的菜单哪边不要菜单就不要添加。
        }
    };


    private UsualAddressAdapter.OnItemClickListener onItemClickListener = new UsualAddressAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position) {
            startActivity(AddressActivity.class);
        }


    };


    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {

        @Override
        public void onItemClick(Closeable closeable, int adapterPosition, int menuPosition, int direction) {
            closeable.smoothCloseMenu();// 关闭被点击的菜单。

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    public UsualAddressResponse getData() {
        UsualAddressResponse mUsualAddressResponse = new UsualAddressResponse();
        List<UsualAddressResponse.UsualAddress> mUsualAddressList = new ArrayList<>();
        mUsualAddressResponse.setUsualAddressList(mUsualAddressList);
        UsualAddressResponse.UsualAddress home = mUsualAddressResponse.new UsualAddress();
        home.setAddress("天府5街666号");
        home.setType(0);
        home.setTypeName("家");
        UsualAddressResponse.UsualAddress company = mUsualAddressResponse.new UsualAddress();
        company.setAddress("吉泰路33号");
        company.setType(1);
        company.setTypeName("公司");
        mUsualAddressList.add(home);
        mUsualAddressList.add(company);
        return mUsualAddressResponse;
    }

}
