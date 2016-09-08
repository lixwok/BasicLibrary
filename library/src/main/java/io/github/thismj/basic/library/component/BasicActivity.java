package io.github.thismj.basic.library.component;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;
import io.github.thismj.basic.library.R;
import io.github.thismj.basic.library.utils.DensityUtil;
import io.github.thismj.basic.library.utils.ViewUtil;

import static io.github.thismj.basic.library.R.id.pageContainer;
import static io.github.thismj.basic.library.component.BasicDelegate.INVALID_LAYOUT;
import static io.github.thismj.basic.library.component.BasicDelegate.PAGE_MODE_SIMPLE_LIST;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * Activity基类
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 10:02
 */

public class BasicActivity extends AppCompatActivity implements BasicDelegate.DelegateCallback {

    /**
     * 默认Activity切换动画
     */
    public final static int PENDING_TRANSITION_DEFAULT = 0;

    /**
     * 左右滑动Activity切换动画
     */
    public final static int PENDING_TRANSITION_SLIDE = 1;

    /**
     * 底部上升下降Activity切换动画
     */
    public final static int PENDING_TRANSITION_BOTTOM = 2;

    /**
     * 显隐Activity切换动画
     */
    public final static int PENDING_TRANSITION_FADE = 3;

    /**
     * 放大缩小Activity切换动画
     */
    public final static int PENDING_TRANSITION_ZOOM = 4;

    private Toolbar mToolBar;

    private CoordinatorLayout mCoordinatorLayout;

    private BasicDelegate mDelegate;

    /**
     * 是否可以进入这个界面
     */
    public boolean isEnable() {
        return true;
    }

    @IntDef({PENDING_TRANSITION_DEFAULT, PENDING_TRANSITION_SLIDE, PENDING_TRANSITION_BOTTOM
                    , PENDING_TRANSITION_FADE, PENDING_TRANSITION_ZOOM})
    public @interface TransitionMode {

    }

    @Override
    public int getPageMode() {
        return BasicDelegate.PAGE_MODE_DEFAULT;
    }

    final int getComponentLayout() {
        return mDelegate.getComponentLayout();
    }

    /**
     * 重写此方法设置页面内容布局,列表页模式无需重写
     */
    public int getContentLayout() {
        return INVALID_LAYOUT;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDelegate = new BasicDelegate(this, this);

        switch (getTransitionMode()) {
            case PENDING_TRANSITION_DEFAULT:
                break;
            case PENDING_TRANSITION_SLIDE:
                overridePendingTransition(R.anim.slide_left_in, android.R.anim.fade_out);
                break;
            case PENDING_TRANSITION_BOTTOM:
                overridePendingTransition(R.anim.slide_bottom_in, android.R.anim.fade_out);
                break;
            case PENDING_TRANSITION_FADE:
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case PENDING_TRANSITION_ZOOM:
                overridePendingTransition(R.anim.zoom_in, android.R.anim.fade_out);
                break;
        }

        if (!isEnable()) {
            finish();
        }

        //设置容器布局
        setContentView(R.layout.activity_basic);

        //添加页面模式布局
        addComponentContent();

        //添加页面内容布局
        addPageContent();

        //变色透明状态栏设置
        immerseStatus();

        //Toolbar初始化
        initToolBar();

        initViews();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化Views
     */
    private void initViews() {
        mDelegate.initViews(mCoordinatorLayout);
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        mToolBar = ViewUtil.find(this, R.id.appBar);
        if (mToolBar != null && enableNavigationBack()) {
            mToolBar.setNavigationIcon(R.drawable.ic_back_white);
            mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolBar.getLayoutParams();

            if (enableToolbarBehavior()) {
                params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                        | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
                        | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
            } else {
                params.setScrollFlags(0);
            }

            mToolBar.setLayoutParams(params);

            initToolBar(mToolBar);
        }
    }

    /**
     * 重写此方法处理刷新逻辑,完成之后调用{@link #refreshComplete()}
     */
    public void onRefresh(PtrFrameLayout ptrFrameLayout) {

    }

    /**
     * 调用此方法刷新完成
     */
    protected void refreshComplete() {
        mDelegate.refreshComplete();
    }

    /**
     * 透明化状态栏
     */
    private void immerseStatus() {
        Window window = getWindow();
        if (window == null) return;

        //android 4.4
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            WindowManager.LayoutParams winParams = window.getAttributes();
            winParams.flags |= (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setAttributes(winParams);
            ViewGroup contentView = ViewUtil.find(this, android.R.id.content);
            View statusBarView = new View(this);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DensityUtil.getStatusBarHeight(this));
            statusBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            if (contentView != null) contentView.addView(statusBarView, lp);
        }

        //android 5.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
            window.setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    /**
     * 添加对应页面模式布局
     */
    private void addComponentContent() {

        mCoordinatorLayout = ViewUtil.find(this, pageContainer);

        if (mCoordinatorLayout != null) {
            View componentView = ViewUtil.inflater(this, getComponentLayout(), mCoordinatorLayout);
            mCoordinatorLayout.addView(componentView);
        }
    }

    /**
     * 添加页面内容,列表页模式除外
     */
    private void addPageContent() {
        mDelegate.addPageContent(mCoordinatorLayout);
    }

    /**
     * data bind,获取ViewDataBind
     */
    @SuppressWarnings("unchecked")
    public <T extends ViewDataBinding> T getBindView() {
        return mDelegate.getBindView();
    }

    @SuppressWarnings("unchecked")
    public <T extends ViewDataBinding> T getBindView(View root) {
        return mDelegate.getBindView(root);
    }

    /**
     * 配置页面是否可以下拉刷新,默认只有列表模式才能刷新
     */
    public boolean enableRefresh() {
        return getPageMode() == PAGE_MODE_SIMPLE_LIST;
    }

    /**
     * 配置页面是否有导航返回键
     */
    public boolean enableNavigationBack() {
        return true;
    }

    /**
     * 配置页面是否能滑动返回
     */
    public boolean enableSwipeBack() {
        return true;
    }

    /**
     * 配置是否允许Toolbar随滚动消失等行为,默认只有列表模式打开
     */
    public boolean enableToolbarBehavior() {
        return getPageMode() == PAGE_MODE_SIMPLE_LIST;
    }

    /******************************************
     * * 重写以下方法配置Toolbar
     ******************************************/

    public void initToolBar(Toolbar mToolBar) {

    }

    /******************************************
     * * 重写以下方法配置列表页
     ******************************************/
    @Override
    public int getItemLayout(int position) {
        return INVALID_LAYOUT;
    }

    @Override
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * 填充列表页数据集
     */
    @SuppressWarnings("unchecked")
    public <E, T extends ViewDataBinding> void setListData(List<E> data, BasicDelegate.SimpleRecycler<E, T> simpleRecycler) {
        mDelegate.setListData(data, simpleRecycler);
    }

    @SuppressWarnings("unchecked")
    public <E> void addListData(List<E> data) {
        mDelegate.addListData(data);
    }

    /**
     * 带有ToolBar的相关配置方法,{@link #initToolBar(Toolbar)}
     */
    public void setToolBarTitle(String title) {
        if (mToolBar != null) mToolBar.setTitle(title);
    }

    public void setToolBarMenu(@MenuRes int menu, final Toolbar.OnMenuItemClickListener listener) {
        if (mToolBar != null) {
            mToolBar.inflateMenu(menu);
            mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return listener != null && listener.onMenuItemClick(item);
                }
            });
        }
    }

    /**
     * 切换动画
     */
    @Override
    public void finish() {
        super.finish();
        switch (getTransitionMode()) {
            case PENDING_TRANSITION_DEFAULT:
                break;
            case PENDING_TRANSITION_SLIDE:
                overridePendingTransition(android.R.anim.fade_in, R.anim.slide_right_out);
                break;
            case PENDING_TRANSITION_BOTTOM:
                overridePendingTransition(android.R.anim.fade_in, R.anim.slide_bottom_out);
                break;
            case PENDING_TRANSITION_FADE:
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
            case PENDING_TRANSITION_ZOOM:
                overridePendingTransition(android.R.anim.fade_in, R.anim.zoom_out);
                break;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }

    /**
     * activity切换动画,默认为系统动画
     */
    @TransitionMode
    public int getTransitionMode() {
        return PENDING_TRANSITION_DEFAULT;
    }

}
