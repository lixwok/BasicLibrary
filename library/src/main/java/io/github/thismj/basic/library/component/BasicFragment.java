package io.github.thismj.basic.library.component;

import android.app.Fragment;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;

import static io.github.thismj.basic.library.component.BasicDelegate.INVALID_LAYOUT;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 此处写用途
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 11:04
 */

public class BasicFragment extends Fragment implements BasicDelegate.DelegateCallback {

    private BasicDelegate mDelegate;

    protected BasicActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (BasicActivity) getActivity();

        mDelegate = new BasicDelegate(mActivity, this);
    }

    final int getComponentLayout() {
        return mDelegate.getComponentLayout();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(getComponentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //初始化Views
        initViews(view);
    }

    private void initViews(View view) {
        mDelegate.initViews(view);
    }

    /**
     * 重写此方法设置页面内容布局,列表页模式无需重写
     */
    public int getContentLayout() {
        return INVALID_LAYOUT;
    }

    /**
     * 配置页面模式
     */
    @Override
    public int getPageMode() {
        return BasicDelegate.PAGE_MODE_DEFAULT;
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
     * 配置页面是否可以下拉刷新,默认只有列表模式才能刷新
     */
    @Override
    public boolean enableRefresh() {
        return getPageMode() == BasicDelegate.PAGE_MODE_SIMPLE_LIST;
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
                new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
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
}
