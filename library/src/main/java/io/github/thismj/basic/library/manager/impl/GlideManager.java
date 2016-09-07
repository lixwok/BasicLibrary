package io.github.thismj.basic.library.manager.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.github.thismj.basic.library.BasicApplication;
import io.github.thismj.basic.library.BasicConstant;
import io.github.thismj.basic.library.manager.BasicManager;
import io.github.thismj.basic.library.utils.DensityUtil;
import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * Glide封装类,获取网络图片
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 11:14
 */

@SuppressWarnings("unused")
public class GlideManager extends BasicManager {

    @IntDef({MODE_NORMAL, MODE_CIRCLE, MODE_ROUND, MODE_BLUR})
    @Retention(RetentionPolicy.SOURCE)
    @interface Mode {
    }

    /**
     * 图片加载模式 正常
     */
    public static final int MODE_NORMAL = 0;

    /**
     * 图片加载模式 圆形
     */
    public static final int MODE_CIRCLE = 1;

    /**
     * 图片加载模式 圆角
     */
    public static final int MODE_ROUND = 2;
    private int mRadius = DensityUtil.dp2px(4.0f);

    /**
     * 图片加载模式 模糊
     */
    public static final int MODE_BLUR = 3;

    public static GlideManager get() {
        return BasicApplication.get().getManager(BasicConstant.GLIDE_MANAGER);
    }

    @Override
    public void onCreate(BasicApplication appContext) {

    }

    /**
     * 获取网络图片
     *
     * @param activity 加载上下文为Activity
     * @param url      图片地址
     * @param target   图片控件
     */
    public void fetch(Activity activity, String url, ImageView target) {
        fetch(activity, url, target, MODE_NORMAL);
    }

    /**
     * 获取网络图片
     *
     * @param activity 加载上下文为Activity
     * @param url      图片地址
     * @param target   图片控件
     * @param radius   圆角半径
     */
    public void fetchRadius(Activity activity, String url, ImageView target, int radius) {
        mRadius = radius;
        fetch(activity, url, target, MODE_ROUND);
    }

    /**
     * 获取网络图片
     *
     * @param activity 加载上下文为Activity
     * @param url      图片地址
     * @param target   图片控件
     * @param mode     图片加载模式 {@link Mode}
     */
    public void fetch(Activity activity, String url, ImageView target, @Mode int mode) {
        fetchInternal(activity, url, target, mode, null);
    }

    /**
     * 获取网络图片
     *
     * @param fragment Fragment
     * @param url      图片地址
     * @param target   图片控件
     */
    public void fetch(Fragment fragment, String url, ImageView target) {
        fetch(fragment, url, target, MODE_NORMAL);
    }

    /**
     * 获取网络图片
     *
     * @param fragment 加载上下文为Fragment
     * @param url      图片地址
     * @param target   图片控件
     * @param radius   圆角半径
     */
    public void fetchRadius(Fragment fragment, String url, ImageView target, int radius) {
        mRadius = radius;
        fetch(fragment, url, target, MODE_ROUND);
    }

    /**
     * 获取网络图片
     *
     * @param fragment 加载上下文为Fragment
     * @param url      图片地址
     * @param target   图片控件
     * @param mode     图片加载模式 {@link Mode}
     */
    public void fetch(Fragment fragment, String url, ImageView target, @Mode int mode) {
        fetchInternal(fragment, url, target, mode, null);
    }

    /**
     * 获取网络图片
     *
     * @param context Context
     * @param url     图片地址
     * @param target  图片控件
     */
    public void fetch(Context context, String url, ImageView target) {
        fetch(context, url, target, MODE_NORMAL);
    }

    /**
     * 获取网络图片
     *
     * @param context 加载上下文为Context
     * @param url     图片地址
     * @param target  图片控件
     * @param radius  圆角半径
     */
    public void fetchRadius(Context context, String url, ImageView target, int radius) {
        mRadius = radius;
        fetch(context, url, target, MODE_ROUND);
    }

    /**
     * 获取网络图片
     *
     * @param context 加载上下文为Context
     * @param url     图片地址
     * @param target  图片控件
     * @param mode    图片加载模式 {@link Mode}
     */
    public void fetch(Context context, String url, ImageView target, @Mode int mode) {
        fetchInternal(context, url, target, mode, null);
    }

    /**
     * 以回调的方式获取网络图片
     *
     * @param activity 加载上下文为Activity
     * @param url      图片地址
     * @param callback 图片加载监听
     */
    public void fetch(Activity activity, String url, FetchCallback callback) {
        fetch(activity, url, callback, MODE_NORMAL);
    }

    /**
     * 以回调的方式获取网络图片
     *
     * @param activity 加载上下文为Activity
     * @param url      图片地址
     * @param callback 图片加载监听
     * @param radius   圆角半径
     */
    public void fetchRadius(Activity activity, String url, FetchCallback callback, int radius) {
        mRadius = radius;
        fetch(activity, url, callback, MODE_ROUND);
    }

    /**
     * 以回调的方式获取网络图片
     *
     * @param activity 加载上下文为Activity
     * @param url      图片地址
     * @param callback 图片加载监听
     * @param mode     图片加载模式 {@link Mode}
     */
    public void fetch(Activity activity, String url, FetchCallback callback, @Mode int mode) {
        fetchInternal(activity, url, null, mode, callback);
    }

    /**
     * 以回调的方式获取网络图片
     *
     * @param fragment 加载上下文为Fragment
     * @param url      图片地址
     * @param callback 图片加载监听
     */
    public void fetch(Fragment fragment, String url, FetchCallback callback) {
        fetch(fragment, url, callback, MODE_NORMAL);
    }

    /**
     * 以回调的方式获取网络图片
     *
     * @param fragment 加载上下文为Fragment
     * @param url      图片地址
     * @param callback 图片加载监听
     * @param radius   圆角半径
     */
    public void fetchRadius(Fragment fragment, String url, FetchCallback callback, int radius) {
        mRadius = radius;
        fetch(fragment, url, callback, MODE_ROUND);
    }

    /**
     * 以回调的方式获取网络图片
     *
     * @param fragment 加载上下文为Fragment
     * @param url      图片地址
     * @param callback 图片加载监听
     * @param mode     图片加载模式 {@link Mode}
     */
    public void fetch(Fragment fragment, String url, FetchCallback callback, @Mode int mode) {
        fetchInternal(fragment, url, null, mode, callback);
    }

    /**
     * 以回调的方式获取网络图片
     *
     * @param context  加载上下文为Context
     * @param url      图片地址
     * @param callback 图片加载监听
     */
    public void fetch(Context context, String url, FetchCallback callback) {
        fetch(context, url, callback, MODE_NORMAL);
    }

    /**
     * 以回调的方式获取网络图片
     *
     * @param context  加载上下文为Context
     * @param url      图片地址
     * @param callback 图片加载监听
     * @param radius   圆角半径
     */
    public void fetchRadius(Context context, String url, FetchCallback callback, int radius) {
        mRadius = radius;
        fetch(context, url, callback, MODE_ROUND);
    }

    /**
     * 以回调的方式获取网络图片
     *
     * @param context  加载上下文为Context
     * @param url      图片地址
     * @param callback 图片加载监听
     * @param mode     图片加载模式 {@link Mode}
     */
    public void fetch(Context context, String url, FetchCallback callback, @Mode int mode) {
        fetchInternal(context, url, null, mode, callback);
    }

    @SuppressWarnings("unchecked")
    private void fetchInternal(Object context, String url, ImageView target, @Mode int mode, final FetchCallback fetchCallback) {

        BitmapTypeRequest<String> request;

        if (context instanceof Activity) {
            request = applyRequest((Activity) context, url).asBitmap();
        } else if (context instanceof Fragment) {
            request = applyRequest((Fragment) context, url).asBitmap();
        } else if (context instanceof Context) {
            request = applyRequest((Context) context, url).asBitmap();
        } else {
            request = null;
        }

        if (request != null) {
            Transformation<Bitmap> transformation = null;
            switch (mode) {
                case MODE_NORMAL:
                    break;
                case MODE_CIRCLE:
                    transformation = new CropCircleTransformation(target.getContext());
                    break;
                case MODE_ROUND:
                    transformation = new RoundedCornersTransformation(target.getContext(), mRadius, 0);
                    break;
                case MODE_BLUR:
                    request.transform(new BlurTransformation(target.getContext())).dontAnimate();
                    break;
                default:
                    transformation = null;
            }

            if (transformation != null) request.transform(transformation).dontAnimate();

            if (fetchCallback != null) {
                request.into(new FetchTarget(target) {

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        fetchCallback.onFetchStart(placeholder);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (glideAnimation == null || !glideAnimation.animate(resource, this)) {
                            fetchCallback.onFetchSuccess(resource);
                        }
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        fetchCallback.onFetchFailed(errorDrawable);
                    }

                });
            } else {
                request.into(target);
            }
        }
    }

    private DrawableTypeRequest<String> applyRequest(Activity activity, String url) {
        return Glide.with(activity).load(url);
    }

    private DrawableTypeRequest<String> applyRequest(Fragment fragment, String url) {
        return Glide.with(fragment).load(url);
    }

    private DrawableTypeRequest<String> applyRequest(Context context, String url) {
        return Glide.with(context).load(url);
    }

    public abstract class FetchTarget extends ViewTarget<ImageView, Bitmap> implements GlideAnimation.ViewAdapter {

        public FetchTarget(ImageView view) {
            super(view);
        }

        @Override
        public Drawable getCurrentDrawable() {
            return getView() != null ? getView().getDrawable() : null;
        }

        @Override
        public void setDrawable(Drawable drawable) {
            if (getView() != null) getView().setImageDrawable(drawable);
        }
    }

    public interface FetchCallback {
        void onFetchStart(Drawable placeholder);

        void onFetchFailed(Drawable errorDrawable);

        void onFetchSuccess(Bitmap resource);
    }
}
