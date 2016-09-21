package io.github.thismj.basic.library.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import io.github.thismj.basic.library.BasicApplication;
import io.github.thismj.basic.library.R;

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

    public static final int CONTENT_STATE_LOADING = 0;
    public static final int CONTENT_STATE_EMPTY = 1;
    public static final int CONTENT_STATE_ERROR = 2;
    public static final int CONTENT_STATE_CONTENT = 3;

    @IntDef({View.VISIBLE, View.INVISIBLE, View.GONE})
    public @interface Visibility {
    }

    /**
     * 显示页面加载中
     */
    public static void showLoading(ViewGroup content) {
        if (!checkNotNull(content)) return;

        if (content.getTag() != null) {
            switch ((Integer) content.getTag()) {
                case CONTENT_STATE_EMPTY:
                    removeView(content, content.findViewWithTag(R.id.pageEmpty));
                    break;
                case CONTENT_STATE_ERROR:
                    removeView(content, content.findViewWithTag(R.id.pageError));
                    break;
            }
        }

        setTag(content, CONTENT_STATE_LOADING);
        changeChildrenVisibility(content, View.GONE);
        View pageLoading = inflater(R.layout.page_loading, content);
        setTag(pageLoading, R.id.pageLoading);
        preventParentEvent(pageLoading);
        addView(content, pageLoading, matchParentParams());
    }

    /**
     * 显示页面空数据
     */
    public static void showEmpty(ViewGroup content, View.OnClickListener onClickListener) {
        if (!checkNotNull(content)) return;

        if (content.getTag() != null) {
            switch ((Integer) content.getTag()) {
                case CONTENT_STATE_LOADING:
                    removeView(content, content.findViewWithTag(R.id.pageLoading));
                    break;
                case CONTENT_STATE_ERROR:
                    removeView(content, content.findViewWithTag(R.id.pageError));
                    break;
            }
        }
        setTag(content, CONTENT_STATE_EMPTY);
        changeChildrenVisibility(content, View.GONE);
        View pageEmpty = inflater(R.layout.page_empty, content);
        setTag(pageEmpty, R.id.pageEmpty);
        if (onClickListener != null) {
            setOnClick(pageEmpty, onClickListener);
        } else {
            preventParentEvent(pageEmpty);
        }

        addView(content, pageEmpty, matchParentParams());
    }

    /**
     * 显示错误页面
     */
    public static void showError(ViewGroup content, View.OnClickListener onClickListener) {
        if (!checkNotNull(content)) return;

        if (content.getTag() != null) {
            switch ((Integer) content.getTag()) {
                case CONTENT_STATE_LOADING:
                    removeView(content, content.findViewWithTag(R.id.pageLoading));
                    break;
                case CONTENT_STATE_EMPTY:
                    removeView(content, content.findViewWithTag(R.id.pageEmpty));
                    break;
            }
        }
        setTag(content, CONTENT_STATE_ERROR);
        changeChildrenVisibility(content, View.GONE);
        View pageError = inflater(R.layout.page_error, content);
        pageError.setTag(R.id.pageError);
        if (onClickListener != null) {
            setOnClick(pageError, onClickListener);
        } else {
            preventParentEvent(pageError);
        }

        addView(content, pageError, matchParentParams());
    }

    /**
     * 显示页面内容
     */
    public static void showContent(ViewGroup content) {
        if (!checkNotNull(content)) return;

        if (content.getTag() != null) {
            switch ((Integer) content.getTag()) {
                case CONTENT_STATE_LOADING:
                    removeView(content, content.findViewWithTag(R.id.pageLoading));
                    break;
                case CONTENT_STATE_EMPTY:
                    removeView(content, content.findViewWithTag(R.id.pageEmpty));
                    break;
                case CONTENT_STATE_ERROR:
                    removeView(content, content.findViewWithTag(R.id.pageError));
                    break;
            }
        }
        setTag(content, CONTENT_STATE_CONTENT);
        changeChildrenVisibility(content, View.VISIBLE);
    }

    /**
     * 改变ViewGroup的所有子View的可见状态
     */
    public static void changeChildrenVisibility(ViewGroup content, @Visibility int visibility) {
        if (checkNotNull(content)) {
            for (int index = 0; index < content.getChildCount(); index++) {
                View child = content.getChildAt(index);
                setVisibility(child, visibility);
            }
        }
    }

    private static ViewGroup.LayoutParams matchParentParams() {
        return new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    /**
     * 子View消耗触摸事件
     */
    private static void preventParentEvent(View child) {
        setOnTouch(child, new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    /**
     * {@link #inflater(int, ViewGroup, boolean)} attachToRoot=false
     */
    public static View inflater(@LayoutRes int id, ViewGroup parent) {
        return inflater(id, parent, false);
    }

    /**
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     */
    public static View inflater(@LayoutRes int id, ViewGroup parent, boolean attach) {
        return getInflaterService().inflate(id, parent, attach);
    }

    /**
     * 获取LayoutInflater
     */
    private static LayoutInflater getInflaterService() {
        return (LayoutInflater) BasicApplication.get().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
     * {@link View#setId(int)}
     */
    public static void setId(View view, @IdRes int id) {
        if (checkNotNull(view)) view.setId(id);
    }

    /**
     * {@link View#setTag(Object)}
     */
    public static void setTag(View view, Object tag) {
        if (checkNotNull(view)) view.setTag(tag);
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
     * View延时点击策略
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
     * {@link View#setOnTouchListener(View.OnTouchListener)}
     */
    public static void setOnTouch(View view, View.OnTouchListener onTouchListener) {
        if (checkNotNull(view)) view.setOnTouchListener(onTouchListener);
    }

    /**
     * {@link View#setVisibility(int)}
     */
    public static void setVisibility(View view, boolean visible) {
        setVisibility(view, visible ? View.VISIBLE : View.GONE);
    }

    /**
     * {@link View#setVisibility(int)}
     */
    public static void setVisibility(View view, @Visibility int visibility) {
        if (checkNotNull(view) && view.getVisibility() != visibility) {
            view.setVisibility(visibility);
        }
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

    /**
     * {@link ViewGroup#addView(View)}
     */
    public static void addView(ViewGroup parent, View child) {
        if (checkNotNull(parent) && checkNotNull(child)) {
            parent.addView(child);
        }
    }

    /**
     * {@link ViewGroup#addView(View, ViewGroup.LayoutParams)}
     */
    public static void addView(ViewGroup parent, View child, ViewGroup.LayoutParams params) {
        if (checkNotNull(parent) && checkNotNull(child) && checkNotNull(params)) {
            parent.addView(child, params);
        }
    }

    /**
     * {@link ViewGroup#removeView(View)}
     */
    public static void removeView(ViewGroup parent, View child) {
        if (checkNotNull(parent) && checkNotNull(child)) {
            parent.removeView(child);
        }
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
