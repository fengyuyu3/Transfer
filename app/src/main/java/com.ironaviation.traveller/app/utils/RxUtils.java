package com.ironaviation.traveller.app.utils;

import android.os.Handler;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.mvp.BaseView;
import com.trello.rxlifecycle.LifecycleTransformer;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by kelvin on 2/10/2016 16:39
 */

public class RxUtils {

    public static <T> Observable.Transformer<T, T> applySchedulers(final BaseView view) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .doOnSubscribe(new Action0() {
                            @Override
                            public void call() {//显示进度条
                                view.showLoading();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doAfterTerminate(new Action0() {
                            @Override
                            public void call() {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        view.hideLoading();
                                    }
                                },1000);
                            }
                        }).compose(RxUtils.<T>bindToLifecycle(view));
            }
        };
    }


    public static <T> LifecycleTransformer<T> bindToLifecycle(BaseView view) {
        if (view instanceof BaseActivity) {
            return ((BaseActivity) view).<T>bindToLifecycle();
        } else if (view instanceof BaseFragment) {
            return ((BaseFragment) view).<T>bindToLifecycle();
        } else {
            throw new IllegalArgumentException("view isn't fragment_find_consultant or fragment");
        }

    }

}
