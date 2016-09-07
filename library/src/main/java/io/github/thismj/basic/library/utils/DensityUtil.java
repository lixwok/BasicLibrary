package io.github.thismj.basic.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 屏幕密度工具类以及获取一些系统组件宽高
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 09:49
 */

@SuppressWarnings("unused")
public class DensityUtil {

    private static DisplayMetrics mMetrics = Resources.getSystem().getDisplayMetrics();

    private static int convertUnit(int unit, float value) {
        return Math.round(TypedValue.applyDimension(unit, value, mMetrics));
    }

    /**
     * dp转px
     */
    public static int dp2px(float dpValue) {
        return convertUnit(TypedValue.COMPLEX_UNIT_DIP, dpValue);
    }

    /**
     * sp转px
     */
    public static int sp2px(float spValue) {
        return convertUnit(TypedValue.COMPLEX_UNIT_SP, spValue);
    }

    /**
     * px转dp
     */
    public static int px2dp(float dpValue) {
        return Math.round(dpValue / (mMetrics.density));
    }

    /**
     * px转sp
     */
    public static int px2sp(float spValue) {
        return Math.round(spValue / (mMetrics.scaledDensity));
    }

    /**
     * 获取状态栏高度 m1
     */
    public static int getStatusBarHeight() {
        int statusBarHeight = 0;
        int resourceId = Resources.getSystem().
                getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 获取状态栏高度 m2
     */
    public static int getStatusBarHeight(Activity activity) {
        if (activity == null) return getStatusBarHeight();
        Rect rectangle = new Rect();
        Window window = activity.getWindow();

        if (window != null) {
            window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
            return rectangle.top > 0 ? rectangle.top : getStatusBarHeight();
        } else {
            return getStatusBarHeight();
        }
    }

    /**
     * 获取导航栏高度
     */
    public static int getToolBarHeight(Context context) {
        TypedArray a = null;
        try {
            a = context.getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            return a.getDimensionPixelSize(0, 0);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    /**
     * 获取屏幕宽度
     */

    public static int getScreenWidth() {
        return mMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight() {
        return mMetrics.heightPixels;
    }
}
