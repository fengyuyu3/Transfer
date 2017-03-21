package com.ironaviation.traveller.di.component;

import com.jess.arms.di.scope.ActivityScope;

import com.ironaviation.traveller.common.AppComponent;
import dagger.Component;
import com.ironaviation.traveller.di.module.UserModule;
import com.ironaviation.traveller.mvp.ui.activity.UserActivity;

/**
 * Created by jess on 9/4/16 11:17
 * Contact with jess.yan.effort@gmail.com
 */
@ActivityScope
@Component(modules = UserModule.class,dependencies = AppComponent.class)
public interface UserComponent {
    void inject(UserActivity activity);
}
