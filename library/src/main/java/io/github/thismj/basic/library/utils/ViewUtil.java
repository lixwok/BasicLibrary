package io.github.thismj.basic.library.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * View相关工具类
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-07 17:05
 */
@SuppressWarnings("unused")
public class ViewUtil {

    public static void showLoading(ViewGroup content) {

        ViewGroup.LayoutParams params;

    }

    public static View inflater(Context context, @LayoutRes int id, ViewGroup parent) {
        return inflater(context, id, parent, false);
    }

    public static View inflater(Context context, @LayoutRes int id, ViewGroup parent, boolean attach) {
        if (context == null) return null;
        return ((LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(id, parent, attach);
    }

    /**
     * {@link Activity#findViewById(int)}
     */
    @SuppressWarnings("unchecked")
    public static <E extends View> E find(Activity activity, int id) {
        try {
            return activity == null ? null : (E) activity.findViewById(id);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@link Fragment#getView()} {@link View#findViewById(int)}
     */
    @SuppressWarnings("unchecked")
    public static <E extends View> E find(Fragment fragment, int id) {
        try {
            return (fragment == null || fragment.getView() == null) ?
                    null : (E) fragment.getView().findViewById(id);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@link View#findViewById(int)}
     */
    @SuppressWarnings("unchecked")
    public static <E extends View> E find(View root, int id) {
        try {
            return root == null ? null : (E) root.findViewById(id);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * {@link TextView#setText(CharSequence)}
     */
    public static void setText(TextView view, CharSequence text) {
        if (checkNotNull(view)) view.setText(text);
    }

    /**
     * {@link TextView#setText(int)}
     */
    public static void setText(TextView view, @StringRes int id) {
        if (checkNotNull(view)) view.setText(id);
    }

    /**
     * {@link TextView#setTextColor(int)}
     */
    public static void setTextColor(TextView view, int color) {
        if (checkNotNull(view)) view.setTextColor(color);
    }

    /**
     * {@link TextView#setTextSize(float)}
     */
    public static void setTextSize(TextView view, float dp) {
        if (checkNotNull(view)) view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, dp);
    }

    /**
     * {@link View#setOnClickListener(View.OnClickListener)}
     */
    public static void setOnClick(View view, final View.OnClickListener onClickListener) {
        if (checkNotNull(view) && checkNotNull(onClickListener)) {
            view.setOnClickListener(new ForbidFastClickListener() {
                @Override
                public void onForbidFastClick(View v) {
                    onClickListener.onClick(v);
                }
            });
        }
    }

    /**
     * {@link View#setVisibility(int)}
     */
    public static void setVisibility(View view, boolean visible) {
        if (checkNotNull(view)) view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    /**
     * {@link ImageView#setImageResource(int)}
     */
    public static void setImageResource(ImageView view, @DrawableRes int id) {
        if (checkNotNull(view)) view.setImageResource(id);
    }

    /**
     * {@link ImageView#setImageBitmap(Bitmap)}
     */
    public static void setImageBitmap(ImageView view, Bitmap bitmap) {
        if (checkNotNull(view)) view.setImageBitmap(bitmap);
    }

    /**
     * {@link View#setBackgroundColor(int)}
     */
    public static void setBackgroundColor(View view, int color) {
        if (checkNotNull(view)) view.setBackgroundColor(color);
    }

    /**
     * {@link View#setBackgroundResource(int)}
     */
    public static void setBackgroundResource(View view, @DrawableRes int color) {
        if (checkNotNull(view)) view.setBackgroundResource(color);
    }

    /**
     * {@link View#setBackground(Drawable)}
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setBackground(View view, Drawable drawable) {
        if (checkNotNull(view)) view.setBackground(drawable);
    }

    /**
     * {@link RadioButton#setChecked(boolean)}
     */
    public static void setChecked(RadioButton view, boolean checked) {
        if (checkNotNull(view)) view.setChecked(checked);
    }

    /**
     * {@link CheckBox#setChecked(boolean)}
     */
    public static void setChecked(CheckBox view, boolean checked) {
        if (checkNotNull(view)) view.setChecked(checked);
    }

    /**
     * {@link CheckedTextView#setChecked(boolean)}
     */
    public static void setChecked(CheckedTextView view, boolean checked) {
        if (checkNotNull(view)) view.setChecked(checked);
    }

    private static boolean checkNotNull(Object object) {
        return object != null;
    }

    public static abstract class ForbidFastClickListener implements View.OnClickListener {

        private static long mLastTimeMillis;

        private long DELAY_TIME;

        public ForbidFastClickListener() {
            DELAY_TIME = 800L;
        }

        public ForbidFastClickListener(long delay) {
            DELAY_TIME = delay;
        }

        private boolean isFastClick() {
            long current = System.currentTimeMillis();
            long delay = current - mLastTimeMillis;

            if (delay > 0 && delay < DELAY_TIME) {
                return true;
            }

            mLastTimeMillis = current;

            return false;
        }

        @Override
        public void onClick(View v) {

            if (isFastClick()) {
                return;
            }

            onForbidFastClick(v);
        }

        public abstract void onForbidFastClick(View v);
    }


}
