package io.github.thismj.basic.library.utils;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * 此处写用途
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 10:50
 */

public class LayoutUtil {

    public static View inflater(Context context, @LayoutRes int id, ViewGroup parent) {

        if (context == null) return null;

        return ((LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(id, parent, false);
    }

    @SuppressWarnings("unchecked")
    public static <E extends View> E find(Activity activity, int id) {
        try {
            return activity == null ? null : (E) activity.findViewById(id);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

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

    @SuppressWarnings("unchecked")
    public static <E extends View> E find(View root, int id) {
        try {
            return root == null ? null : (E) root.findViewById(id);
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }
}
