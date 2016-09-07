package io.github.thismj.basic.library;

import android.app.Activity;
import android.app.Application;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.HashMap;

import io.github.thismj.basic.library.manager.BasicManager;
import io.github.thismj.basic.library.manager.impl.GlideManager;
import io.github.thismj.basic.library.manager.impl.OttoManager;
import io.github.thismj.basic.library.manager.impl.RetrofitManager;
import io.github.thismj.basic.library.manager.impl.SystemManager;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 此处写用途
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 09:48
 */

public class BasicApplication extends Application implements Application.ActivityLifecycleCallbacks {

    private static BasicApplication mInstance;

    private static final HashMap<String, BasicManager> APP_MANAGERS = new HashMap<>();

    public static BasicApplication get() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        registerManager(BasicConstant.GLIDE_MANAGER, new GlideManager());
        registerManager(BasicConstant.OTTO_MANAGER, new OttoManager());
        registerManager(BasicConstant.SYSTEM_MANAGER, new SystemManager());
        registerManager(BasicConstant.RETROFIT_MANAGER, new RetrofitManager());
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 添加manager
     */
    private <Manager extends BasicManager> void registerManager(String key, Manager manager) {

        manager.onCreate(this);

        APP_MANAGERS.put(key, manager);
    }

    @SuppressWarnings("unchecked")
    public <Manager extends BasicManager> Manager getManager(String key) {
        return APP_MANAGERS.containsKey(key) ? (Manager) (APP_MANAGERS.get(key)) : null;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        SystemManager.get().pushActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SystemManager.get().popActivity(activity);
    }
}
