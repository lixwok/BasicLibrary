package io.github.thismj.basic.library.component;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.IntDef;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

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
import io.github.thismj.basic.uilibrary.recyclerview.BLRecyclerView;


/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 此处写用途
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-08 10:01
 */

public class BasicDelegate {

    public static final int INVALID_LAYOUT = -1;

    private Context mContext;

    private DelegateCallback mCallback;

    private View mContentView;

    /**
     * appBarLayout偏移量
     */
    private int mAppBarOffset = -1;

    /**
     * 默认的页面模式
     */
    public final static int PAGE_MODE_DEFAULT = 0;

    /**
     * 简单列表页面模式
     */
    public final static int PAGE_MODE_SIMPLE_LIST = 1;

    /**
     * ScrollView包裹的页面模式
     */
    public final static int PAGE_MODE_SCROLLABLE = 2;

    private BLRecyclerView mRecyclerView;

    private PtrFrameLayout mPtrFrameLayout;

    public BasicDelegate(Context context, DelegateCallback callback) {
        mContext = context;
        mCallback = callback;
    }

    @IntDef({PAGE_MODE_DEFAULT, PAGE_MODE_SIMPLE_LIST, PAGE_MODE_SCROLLABLE})
    public @interface PageMode {

    }

    public int getComponentLayout() {
        switch (getPageMode()) {
            case PAGE_MODE_DEFAULT:
                return R.layout.component_none;
            case PAGE_MODE_SIMPLE_LIST:
                return R.layout.component_simple_list;
            case PAGE_MODE_SCROLLABLE:
                return R.layout.component_scrollable;
            default:
                return R.layout.component_none;
        }
    }

    @PageMode
    public int getPageMode() {
        return mCallback == null ? PAGE_MODE_DEFAULT : mCallback.getPageMode();
    }

    /**
     * 添加页面内容,列表页模式除外
     */
    public void addPageContent(View root) {
        if (getPageMode() != PAGE_MODE_SIMPLE_LIST) {
            ViewGroup content = ViewUtil.find(root, R.id.pageContent);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            if (content != null && mCallback != null) {
                try {
                    mContentView = ViewUtil.inflater(mContext, mCallback.getContentLayout(), content);
                    content.addView(mContentView, params);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initViews(View content) {

        initRecyclerView(content);

        initPtrFrameLayout(content);
    }

    /**
     * 列表页初始化RecyclerView
     */
    private void initRecyclerView(View content) {
        if (getPageMode() == PAGE_MODE_SIMPLE_LIST) {
            mRecyclerView = ViewUtil.find(content, R.id.recycler);
            if (mCallback != null) {
                mCallback.initRecyclerView(mRecyclerView);
            }
        }
    }

    /**
     * 初始化配置下拉刷新组件
     */
    private void initPtrFrameLayout(View content) {
        mPtrFrameLayout = ViewUtil.find(content, R.id.refresh);
        AppBarLayout appBarLayout = ViewUtil.find(content, R.id.appLayout);

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
                    if (mCallback != null) mCallback.onRefresh(frame);
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

        final StoreHouseHeader header = new StoreHouseHeader(mContext);
        header.setPadding(0, DensityUtil.dp2px(20), 0, 0);
        header.initWithString(mContext.getString(R.string.app_name));
        header.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
        return header;
    }

    /**
     * Ptr Material Design风格的头部实现
     */
    private MaterialHeader obtainMaterial() {
        mPtrFrameLayout.setPinContent(true);

        final MaterialHeader header = new MaterialHeader(mContext);
        header.setPadding(0, DensityUtil.dp2px(20), 0, 0);
        header.setColorSchemeColors(new int[]{ContextCompat.getColor(mContext, R.color.colorPrimary)});
        return header;
    }

    /**
     * Ptr 经典下拉刷新实现
     */
    private PtrClassicDefaultHeader obtainClassicDefault() {
        mPtrFrameLayout.setPinContent(false);
        return new PtrClassicDefaultHeader(mContext);
    }

    /**
     * 调用此方法刷新完成
     */
    public void refreshComplete() {
        if (mPtrFrameLayout != null) {
            mPtrFrameLayout.refreshComplete();
        }
    }

    private int getPtrMode() {
        return enableRefresh() ? 2 : -1;
    }

    /**
     * 配置页面是否可以下拉刷新,默认只有列表模式才能刷新
     */
    public boolean enableRefresh() {
        return mCallback != null && mCallback.enableRefresh();
    }

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
     * 填充列表页数据集
     */
    @SuppressWarnings("unchecked")
    public <E, T extends ViewDataBinding> void setListData(List<E> data, SimpleRecycler<E, T> simpleRecycler) {
        if (getPageMode() == PAGE_MODE_SIMPLE_LIST && mRecyclerView != null) {
            if (mRecyclerView.getAdapter() == null) {
                SimpleRecyclerAdapter<E, T> mAdapter = new SimpleRecyclerAdapter<>(
                        mContext, simpleRecycler);
                mRecyclerView.setAdapter(mAdapter);
            }

            ((SimpleRecyclerAdapter<E, T>) mRecyclerView.getAdapter()).setData(data);
        }
    }

    @SuppressWarnings("unchecked")
    public <E> void addListData(List<E> data) {
        if (getPageMode() == PAGE_MODE_SIMPLE_LIST && mRecyclerView != null) {
            if (mRecyclerView.getAdapter() != null) {
                ((SimpleRecyclerAdapter) mRecyclerView.getAdapter()).addData(data);
            }
        }
    }

    /**
     * 列表页adapter
     */
    public class SimpleRecyclerAdapter<E, T extends ViewDataBinding> extends BasicRecyclerAdapter<E, SimpleRecyclerAdapter.SimpleRecyclerHolder> {

        SimpleRecycler<E, T> mSimpleRecycler;

        public SimpleRecyclerAdapter(Context context, SimpleRecycler<E, T> simpleRecycler) {
            super(context);
            mSimpleRecycler = simpleRecycler;
        }

        @Override
        public int getViewType(int position) {
            return mCallback == null ? 0 : mCallback.getItemLayout(position);
        }

        public class SimpleRecyclerHolder extends BasicRecyclerHolder<E> {

            T bind; //dataBinding

            public SimpleRecyclerHolder(View itemView) {
                super(itemView);
                bind = getBindView(itemView);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void bindViewHolder(final E model, final int position) {

                if (mSimpleRecycler != null) {
                    mSimpleRecycler.bindItemData(bind, model, position);
                    ViewUtil.setOnClick(itemView, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSimpleRecycler.onItemClick(model, position);
                        }
                    });
                }
            }
        }
    }

    public interface SimpleRecycler<E, T extends ViewDataBinding> {

        void bindItemData(T bind, E model, int position);

        void onItemClick(E model, int position);
    }

    public interface DelegateCallback {

        /**
         * 配置页面模式
         */
        @PageMode
        int getPageMode();

        /**
         * 是否允许下拉刷新
         */
        boolean enableRefresh();

        /**
         * 页面刷新回调
         */
        void onRefresh(PtrFrameLayout frame);

        /**
         * 页面内容布局
         */
        int getContentLayout();

        /**
         * 列表页Item布局
         */
        int getItemLayout(int position);

        /**
         * 配置列表页RecyclerView
         */
        void initRecyclerView(RecyclerView recyclerView);
    }
}
