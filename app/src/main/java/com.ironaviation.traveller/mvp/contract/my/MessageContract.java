package com.ironaviation.traveller.mvp.contract.my;

import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.request.MessageRequest;
import com.ironaviation.traveller.mvp.model.entity.response.MessageResponse;
import com.ironaviation.traveller.mvp.model.entity.response.TravelResponse;
import com.jess.arms.mvp.BaseView;
import com.jess.arms.mvp.IModel;

import java.util.List;

import rx.Observable;

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
 * 创建时间：2017-03-27 10:11
 * 修改人：starRing
 * 修改时间：2017-03-27 10:11
 * 修改备注：
 */
public interface MessageContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        void setDatas(MessageResponse mTravelResponses);

        void addDatas(MessageResponse mTravelResponses);

        void loadMoreFail();

        void setNodata();

        void setError();
        void stopRefreshing();
        void setData();
        void showDialog();
        void dismissDialog();
        void setNoMore();
        void setMoreComplete();
    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {

        Observable<BaseData<MessageResponse>> getMessageData(int pageSize, int pageIndex);
    }
}