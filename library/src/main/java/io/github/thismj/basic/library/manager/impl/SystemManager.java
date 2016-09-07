package io.github.thismj.basic.library.manager.impl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import io.github.thismj.basic.library.BasicApplication;
import io.github.thismj.basic.library.BasicConstant;
import io.github.thismj.basic.library.manager.BasicManager;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 系统管理
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 11:20
 */

public class SystemManager extends BasicManager {

    /**
     * 保存app所有未销毁的activity
     */
    private List<Activity> mActivityStack;

    private boolean isInitialized;

    private Toast mToast;

    private String mToastMessage;

    @IntDef({Toast.LENGTH_LONG, Toast.LENGTH_SHORT})
    @Retention(RetentionPolicy.SOURCE)
    @interface Duration {
    }

    public static SystemManager get() {
        return BasicApplication.get().getManager(BasicConstant.SYSTEM_MANAGER);
    }

    @Override
    public void onCreate(BasicApplication appContext) {

        mActivityStack = new ArrayList<>();
    }

    /**
     * 系统弹窗toast
     */
    public void toast(Context context, @Nullable String message) {
        toast(context, message, Toast.LENGTH_SHORT);
    }

    public void toast(Context context, @Nullable String message, @Duration int duration) {
        if (TextUtils.isEmpty(message)) return;

        if (!message.equals(mToastMessage)) {
            mToastMessage = message;
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = null;
        }

        if (mToast == null) {
            mToast = Toast.makeText(context, message, duration);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        }

        mToast.show();
    }

    /**
     * 添加activity,一般在activity回调onCreated()之后
     */
    public void pushActivity(Activity activity) {
        mActivityStack.add(activity);
    }

    /**
     * 移除activity,一般在activity回调onDestroy()之后
     */
    public void popActivity(Activity activity) {
        if (mActivityStack.contains(activity)) {
            mActivityStack.remove(activity);
        }
    }

    /**
     * finish所有未销毁的activity,退出应用
     */
    public void clearStack() {
        for (Activity activity : mActivityStack) {
            activity.finish();
        }
        mActivityStack.clear();
    }

    /**
     * 判断application是否初始化,区分热启动与冷启动
     *
     * @return 返回true为热启动, 反之为冷启动
     */
    public boolean isInitialized() {
        return isInitialized;
    }

    /**
     * 设置application是否已经初始化
     *
     * @param initialized 是否初始化
     */
    public void setInitialized(boolean initialized) {
        isInitialized = initialized;
    }
}
