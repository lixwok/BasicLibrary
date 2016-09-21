package io.github.thismj.basic;

import android.os.Bundle;
import android.support.annotation.Nullable;

import io.github.thismj.basic.library.component.BasicActivity;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 此处写用途
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 17:15
 */

public class SecondActivity extends BasicActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setToolBarTitle("第二页");
    }

    @Override
    public int getContentLayout() {
        return R.layout.second_acti;
    }

    @Override
    public int getTransitionMode() {
        return PENDING_TRANSITION_ZOOM;
    }
}
