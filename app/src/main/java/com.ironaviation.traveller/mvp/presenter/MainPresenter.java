package com.ironaviation.traveller.mvp.presenter;

import android.app.Application;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.ironaviation.traveller.R;
import com.ironaviation.traveller.app.utils.CommonUtil;
import com.ironaviation.traveller.common.WEApplication;
import com.ironaviation.traveller.mvp.constant.Constant;
import com.ironaviation.traveller.mvp.contract.MainContract;
import com.ironaviation.traveller.mvp.model.entity.AppVersionEntity;
import com.ironaviation.traveller.mvp.model.entity.BaseData;
import com.ironaviation.traveller.mvp.model.entity.LoginEntity;
import com.ironaviation.traveller.mvp.model.entity.response.RouteStateResponse;
import com.ironaviation.traveller.mvp.ui.widget.AlertDialog;
import com.ironaviation.traveller.mvp.ui.widget.HorizontalProgressBarWithNumber;
import com.ironaviation.traveller.mvp.ui.widget.MaskingDialog;
import com.jess.arms.base.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.DataHelper;
import com.jess.arms.utils.DeviceUtils;
import com.jess.arms.utils.RxUtils;
import com.jess.arms.utils.UiUtils;
import com.jess.arms.widget.imageloader.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

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
 * 创建时间：2017-03-23 10:58
 * 修改人：starRing
 * 修改时间：2017-03-23 10:58
 * 修改备注：
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MainContract.Model, MainContract.View> {
    private RxErrorHandler mErrorHandler;
    private Application mApplication;
    private ImageLoader mImageLoader;
    private AppManager mAppManager;
    private HorizontalProgressBarWithNumber mProgressBar;
    private View upDateView;   //  下载进度条
    private MaskingDialog upDateDialog;//下载进度条蒙版
    private int progress = 0;
    private static final int DOWNLOADING = 1; //表示正在下载
    private static final int DOWNLOADED = 2; //下载完毕
    private static final int DOWNLOAD_FAILED = 3; //下载失败
    private String apkFileAddress;

    private boolean showDialog = false;

    @Inject
    public MainPresenter(MainContract.Model model, MainContract.View rootView
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

    private long firstTime = 0;

    public boolean exit() {
        boolean reason = false;
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) { // 如果两次按键时间间隔大于2秒，则不退出

            UiUtils.makeText("再按一次退出程序");
            firstTime = secondTime;// 更新firstTime
            reason = true;
        } else { // 两次按键小于2秒时，退出应用

            UiUtils.killAll();
            reason = false;
        }
        return reason;
    }

    public void getLatestVersion() {
        mModel.getLatestVersion()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {//显示进度条
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                    }
                }).subscribe(new ErrorHandleSubscriber<BaseData<AppVersionEntity>>(mErrorHandler) {
            @Override
            public void onNext(BaseData<AppVersionEntity> appVersionEntity) {
                if (appVersionEntity != null) {
                    updateApp(appVersionEntity.getData());

                }
            }


        });

    }

    /**
     * 更新 APK
     */
    private void updateApp(final AppVersionEntity versionEntity) {
        if (DeviceUtils.getVersionCode(mApplication) < versionEntity.getVersionNum()) {
            AlertDialog dialog = new AlertDialog(mRootView.getActivity());
            //Log.i("msg", " versionEntity.getData().getUpdateNotification()" + versionEntity.g());

            if (!versionEntity.getIsCoerceUpdate()) {

                dialog.builder().setTitle(mApplication.getString(R.string.app_name) + versionEntity.getVersion() + "更新说明：").setMsg(versionEntity.getNotification())
                        .setPositiveButton("立即更新", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (CommonUtil.isWifi(mApplication)) {
                                    downloadAPK(versionEntity.getDownLoadUrl(), false);

                                } else {
                                    AlertDialog dialog = new AlertDialog(mApplication);
                                    dialog.builder().setTitle("提示").setMsg("当前网络非Wife状态App" + versionEntity.getFileSize() / (1024 * 1024) + "MB" + "是否下载？")
                                            .setCancelable(false).setPositiveButton("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            downloadAPK(versionEntity.getDownLoadUrl(), false);

                                        }
                                    }).setNegativeButton("下次吧", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                                }
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            } else {
                upDateView = LayoutInflater.from(mApplication).inflate(R.layout.layout_homefg_progressbar, null);
                mProgressBar = (HorizontalProgressBarWithNumber) upDateView.findViewById(R.id.id_progressbar01);
                upDateDialog = new MaskingDialog(mRootView.getActivity(), upDateView, true);
                dialog.builder().setCancelable(false).setTitle(mApplication.getString(R.string.app_name) + versionEntity.getVersion() + "更新说明：").setCancelable(false).setMsg(versionEntity.getNotification())
                        .setPositiveButton("立即更新", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                upDateDialog.show();
                                upDateDialog.setCancelable(false);
                                if (CommonUtil.isWifi(mApplication)) {
                                    downloadAPK(versionEntity.getDownLoadUrl(), true);

                                } else {
                                    AlertDialog dialog = new AlertDialog(mApplication);
                                    dialog.setCancelable(false);
                                    dialog.builder().setTitle("提示").setCancelable(false).setMsg(
                                            "当前网络非Wife状态App" + versionEntity.getFileSize()
                                                    / (1024 * 1024) + "MB" + "是否下载？")
                                            .setPositiveButton("确认", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    downloadAPK(versionEntity.getDownLoadUrl(), true);

                                                }
                                            }).setNegativeButton("取消", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                        }
                                    }).show();
                                }
                            }
                        }).show();

            }
        } else {

        }
    }

    /**
     * 下载 APK
     */
   /* private void downloadAPK(String uri, final boolean ifShow) {
        try {
            RequestParams requestParams = new RequestParams(uri);
            LogUtils.getInstance().i("currentProgress", "currentProgress:" + uri.toString());


            final File tempFile = new File(UtilsTool.getInstance().checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/apk/"),
                    System.currentTimeMillis() + ".apk");
            requestParams.setSaveFilePath(tempFile.toString());
            LogUtils.getInstance().i("currentProgress", "currentProgress:" + tempFile.toString());
            Callback.Cancelable cancelable = x.http().get(requestParams, new Callback.ProgressCallback<File>() {

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    int currentProgress = (int) (((double) current / total) * 100);
                    //dialogDownloadProgressBar.setProgress(currentProgress);
                    if (ifShow) {
                        mProgressBar.setProgress(currentProgress);

                    } else {

                    }

                }

                @Override
                public void onSuccess(File file) {
                    try {
                        installApk(tempFile.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        MobclickAgent.reportError(IronAirApplication.getInstance(), e);

                    }

                }

                @Override
                public void onError(Throwable arg0, boolean arg1) {

                    LogUtils.getInstance().i("onError", "onError:" + arg0.getMessage().toString());
                    upDateView.setVisibility(View.GONE);
                    upDateDialog.dismiss();
                    this.onFinished();

                }

                @Override
                public void onStarted() {
                }

                @Override
                public void onFinished() {
                }

                @Override
                public void onCancelled(CancelledException arg0) {
                }

                @Override
                public void onWaiting() {
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(IronAirApplication.getInstance(), e);

        }
    }*/

    /**
     * 安装 apk
     */
    public void installApk(String filePath) {
        try {
            //getActivity().startActivity(getActivity(), new Intent(Intent.ACTION_VIEW).setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive"));

            mApplication.startActivity(new Intent(Intent.ACTION_VIEW).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).setDataAndType(Uri.parse("file://" + filePath), "application/vnd.android.package-archive"));
            UiUtils.killAll();
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(WEApplication.getContext(), e);

        }
    }

    /**
     * 更新UI的handler
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case DOWNLOADING:
                    if (showDialog) {
                        mProgressBar.setProgress(progress);

                    }
                    /*mProgress.setProgress(progress);
                    mProgressContent.setText(progress + "%");*/
                    break;
                case DOWNLOADED:
                    if (upDateDialog != null && upDateDialog.isShowing())
                        upDateDialog.dismiss();
                    installApk(apkFileAddress);
                    break;
                case DOWNLOAD_FAILED:
                    UiUtils.SnackbarText("网络异常，请稍后再试!");
                    break;
                default:
                    break;
            }
        }
    };

    public void downloadAPK(final String uri, boolean f) {
        showDialog = f;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    URL url = new URL("http://apk.r1.market.hiapk.com/data/upload/apkres/2016/10_19/16/com.tencent.mtt_044304.apk");
                    URL url = new URL(uri);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    long length = conn.getContentLength();
                    InputStream is = conn.getInputStream();

//                    File file = new File(savePath);
//                    if (!file.exists()) {
//                        file.mkdirs();
//                    }
                    final File ApkFile = new File(CommonUtil.checkDirPath(Environment.getExternalStorageDirectory().getPath() + "/apk/"),
                            System.currentTimeMillis() + ".apk");
                    //               saveFileName = savePath + "/WaiQin_" + serverVersion + "外勤.apk";
//                    File ApkFile = new File(saveFileName);
                    apkFileAddress = ApkFile.toString();
                    if (!ApkFile.exists()) {
                        ApkFile.createNewFile();
                    }

                    FileOutputStream fos = new FileOutputStream(ApkFile);

                    int count = 0;
                    byte buf[] = new byte[4096];

                    do {
                        int numread = is.read(buf);
                        count += numread;
                        progress = (int) (((float) count / length) * 100);
                        //更新进度
                        mHandler.sendEmptyMessage(DOWNLOADING);
                        if (numread <= 0) {
                            //下载完成通知安装
                            mHandler.sendEmptyMessage(DOWNLOADED);
                            break;
                        }
                        fos.write(buf, 0, numread);
                    } while (true); //点击取消就停止下载.

                    fos.close();
                    is.close();
                } catch (Exception e) {
                    mHandler.sendEmptyMessage(DOWNLOAD_FAILED);
                    e.printStackTrace();
                }
            }
        }).start();
    }

}