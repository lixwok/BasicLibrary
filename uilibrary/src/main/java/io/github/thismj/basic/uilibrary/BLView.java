package io.github.thismj.basic.uilibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 此处写用途
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-08 16:48
 */

public class BLView extends View {
    public BLView(Context context) {
        super(context);
    }

    public BLView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BLView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BLView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
