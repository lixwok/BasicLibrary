package io.github.thismj.basic.library.component;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import io.github.thismj.basic.library.R;
import io.github.thismj.basic.library.utils.DensityUtil;
import io.github.thismj.basic.library.utils.ViewUtil;

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

public class BasicActivity extends AppCompatActivity {

    private final static int INVALID_LAYOUT = -1;

    /**
     * 不带有toolbar的activity模式
     */
    public final static int ACTIVITY_MODE_NONE = 0;

    /**
     * 带有toolbar的activity
     */
    public final static int ACTIVITY_MODE_WITH_TOOLBAR = 1;

    /**
     * 简单列表页的activity
     */
    public final static int ACTIVITY_MODE_SIMPLE_LIST = 2;

    /**
     * ScrollView包裹的的activity
     */
    public final static int ACTIVITY_MODE_PAGE_SCROLLABLE = 3;

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

    private PtrFrameLayout mPtrFrameLayout;

    private Toolbar mToolBar;

    private RecyclerView mRecyclerView;

    private View mContentView;

    /**
     * appBarLayout偏移量
     */
    private int mAppBarOffset = -1;

    /**
     * 是否可以进入这个界面
     */
    public boolean isEnable() {
        return true;
    }

    @IntDef({ACTIVITY_MODE_NONE, ACTIVITY_MODE_WITH_TOOLBAR,
                    ACTIVITY_MODE_SIMPLE_LIST, ACTIVITY_MODE_PAGE_SCROLLABLE})
    public @interface ActivityMode {

    }

    @IntDef({PENDING_TRANSITION_DEFAULT, PENDING_TRANSITION_SLIDE, PENDING_TRANSITION_BOTTOM
                    , PENDING_TRANSITION_FADE, PENDING_TRANSITION_ZOOM})
    public @interface TransitionMode {

    }

    @ActivityMode
    public int getActivityMode() {
        return ACTIVITY_MODE_WITH_TOOLBAR;
    }

    public int getContentLayout() {
        return INVALID_LAYOUT;
    }

    final int getLayout() {
        switch (getActivityMode()) {
            case ACTIVITY_MODE_NONE:
                return R.layout.activity_none;
            case ACTIVITY_MODE_WITH_TOOLBAR:
                return R.layout.activity_with_toolbar;
            case ACTIVITY_MODE_SIMPLE_LIST:
                return R.layout.activity_list;
            case ACTIVITY_MODE_PAGE_SCROLLABLE:
                return R.layout.activity_scrollable;
            default:
                return R.layout.activity_none;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        setContentView(getLayout());

        //添加内容布局
        addPageContent();

        initViews();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 初始化Views
     */
    private void initViews() {
        immerseStatus();

        initToolBar();

        initRecyclerView();

        initPtrFrameLayout();
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        if (getActivityMode() != ACTIVITY_MODE_NONE) {
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
    }

    /**
     * 重写此方法处理刷新逻辑,完成之后调用{@link #refreshComplete()}
     */
    protected void onRefresh(PtrFrameLayout ptrFrameLayout) {

    }

    /**
     * 调用此方法刷新完成
     */
    protected void refreshComplete() {
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.refreshComplete();
        }
    }

    /**
     * 列表页初始化RecyclerView
     */
    private void initRecyclerView() {
        if (getActivityMode() == ACTIVITY_MODE_SIMPLE_LIST) {
            mRecyclerView = ViewUtil.find(this, R.id.recycler);
            initRecyclerView(mRecyclerView);
        }
    }

    /**
     * 初始化配置下拉刷新组件
     */
    private void initPtrFrameLayout() {
        mPtrFrameLayout = ViewUtil.find(this, R.id.refresh);
        AppBarLayout appBarLayout = ViewUtil.find(this, R.id.appLayout);

        View header;
        switch (getPtrMode()) {
            case 0:
                header = obtainClassicDefault();
                break;
            case 1:
                header = obtainMaterial();
                break;
            case 2:
                header = obtainStoreHouse();
                break;
            default:
                header = null;
        }

        if (header != null) {
            mPtrFrameLayout.setHeaderView(header);
            mPtrFrameLayout.addPtrUIHandler((PtrUIHandler) header);
            mPtrFrameLayout.disableWhenHorizontalMove(true);
            mPtrFrameLayout.setKeepHeaderWhenRefresh(true);
            mPtrFrameLayout.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    return mAppBarOffset == 0 && PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
                }

                @Override
                public void onRefreshBegin(PtrFrameLayout frame) {
                    onRefresh(frame);
                }
            });
        }

        if (appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    mAppBarOffset = verticalOffset;
                }
            });
        }
    }

    /**
     * Ptr StoreHouse风格的头部实现
     */
    private StoreHouseHeader obtainStoreHouse() {
        mPtrFrameLayout.setPinContent(false);

        final StoreHouseHeader header = new StoreHouseHeader(this);
        header.setPadding(0, DensityUtil.dp2px(20), 0, 0);
        header.initWithString(getString(R.string.app_name));
        header.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        return header;
    }

    /**
     * Ptr Material Design风格的头部实现
     */
    private MaterialHeader obtainMaterial() {
        mPtrFrameLayout.setPinContent(true);

        final MaterialHeader header = new MaterialHeader(this);
        header.setPadding(0, DensityUtil.dp2px(20), 0, 0);
        header.setColorSchemeColors(new int[]{ContextCompat.getColor(this, R.color.colorPrimary)});
        return header;
    }

    /**
     * Ptr 经典下拉刷新实现
     */
    private PtrClassicDefaultHeader obtainClassicDefault() {
        mPtrFrameLayout.setPinContent(false);
        return new PtrClassicDefaultHeader(this);
    }

    private int getPtrMode() {
        return enableRefresh() ? 2 : -1;
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
     * 添加页面内容,列表页模式除外
     */
    private void addPageContent() {

        if (getActivityMode() != ACTIVITY_MODE_SIMPLE_LIST && getContentLayout() != INVALID_LAYOUT) {
            ViewGroup content = ViewUtil.find(this, R.id.pageContent);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (content != null) {
                try {
                    mContentView = ViewUtil.inflater(this, getContentLayout(), content);
                    content.addView(mContentView, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onContentChanged() {
        super.onContentChanged();
    }

    /**
     * data bind,获取ViewDataBind
     */
    @SuppressWarnings("unchecked")
    public <T extends ViewDataBinding> T getBindView() {
        return getBindView(mContentView);
    }

    @SuppressWarnings("unchecked")
    public <T extends ViewDataBinding> T getBindView(View root) {
        try {
            return root == null ? null : (T) DataBindingUtil.bind(root);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 配置页面是否可以下拉刷新,默认只有列表模式才能刷新
     */
    public boolean enableRefresh() {
        return getActivityMode() == ACTIVITY_MODE_SIMPLE_LIST;
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
        return getActivityMode() == ACTIVITY_MODE_SIMPLE_LIST;
    }

    /******************************************
     * * 重写以下方法配置Toolbar
     ******************************************/

    public void initToolBar(Toolbar mToolBar) {

    }

    /******************************************
     * * 重写以下方法配置列表页
     ******************************************/

    //item布局
    public int getItemLayout() {
        return INVALID_LAYOUT;
    }

    //item绑定数据,可以通过data bind获取itemView的ViewDataBind对象
    public <E> void bindItemData(View itemView, E entity, int position) {
//        ViewDataBinding binding = getBindView(itemView);
//        ViewSetUtil.setText(binding.text,"text");
    }

    //初始化recyclerView的LayoutManager,分割线等
    public void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * 填充列表页数据集
     */
    @SuppressWarnings("unchecked")
    public <E> void setListData(List<E> data) {
        if (getActivityMode() == ACTIVITY_MODE_SIMPLE_LIST && mRecyclerView != null) {

            if (mRecyclerView.getAdapter() == null) {
                SimpleRecyclerAdapter<E> mAdapter = new SimpleRecyclerAdapter<>(this);
                mRecyclerView.setAdapter(mAdapter);
            }

            ((SimpleRecyclerAdapter<E>) mRecyclerView.getAdapter()).setData(data);
        }
    }

    /**
     * 列表页adapter
     */
    public class SimpleRecyclerAdapter<E> extends BasicRecyclerAdapter<E, SimpleRecyclerAdapter.SimpleRecyclerHolder> {


        public SimpleRecyclerAdapter(Context context) {
            super(context);
        }

        @Override
        public int getViewType(int position) {
            return getItemLayout();
        }

        public class SimpleRecyclerHolder extends BasicRecyclerHolder<E> {

            public SimpleRecyclerHolder(View itemView) {
                super(itemView);
            }

            @Override
            public void bindViewHolder(E model, int position) {
                bindItemData(itemView, model, position);
            }
        }
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
