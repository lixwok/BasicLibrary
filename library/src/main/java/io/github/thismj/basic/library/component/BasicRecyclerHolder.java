package io.github.thismj.basic.library.component;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * 　 ∧__∧
 * 　(●ω●)
 * 　｜つ／(＿＿＿
 * / └-(＿＿＿_／
 * ￣￣￣￣
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-07-20 16:28
 */
public abstract class BasicRecyclerHolder<M> extends RecyclerView.ViewHolder {


    public BasicRecyclerHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindViewHolder(M model, int position);
}
