package io.github.thismj.basic.library.component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import io.github.thismj.basic.library.utils.ViewUtil;


/**
 * 　 ∧__∧
 * 　(●ω●)
 * 　｜つ／(＿＿＿
 * / └-(＿＿＿_／
 * ￣￣￣￣
 * 组件默认实现接口
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-07-20 16:28
 */
public abstract class BasicRecyclerAdapter<M, H extends BasicRecyclerHolder> extends RecyclerView.Adapter<H> {
    private List<M> mModels;
    private Context mContext;
    private Class<H> mHolder;

    public Context getContext() {
        return mContext;
    }

    public abstract int getViewType(int position);

    @SuppressWarnings("unchecked")
    public BasicRecyclerAdapter(Context context) {
        mContext = context;
        mHolder = (Class<H>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[1];
    }

    public void setData(List<M> data) {
        if (mModels == null) mModels = new ArrayList<>();
        mModels.clear();
        mModels.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<M> data) {
        if (mModels == null) mModels = new ArrayList<>();
        mModels.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    @Override
    public int getItemCount() {
        return mModels == null ? 0 : mModels.size();
    }

    public M getItem(int position) {
        return mModels == null ? null : mModels.get(position);
    }

    @SuppressWarnings({"unchecked", "TryWithIdenticalCatches"})
    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        H holder = null;
        try {
            Constructor c = mHolder.getDeclaredConstructor(getClass(), View.class);

            holder = (H) c.newInstance(this, ViewUtil.inflater(mContext, viewType, parent));

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return holder;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(H holder, int position) {
        holder.bindViewHolder(getItem(position), position);
    }
}
