package io.github.thismj.basic.library.manager.impl;

import io.github.thismj.basic.library.BasicApplication;
import io.github.thismj.basic.library.BasicConstant;
import io.github.thismj.basic.library.manager.BasicManager;

/**
 * ╭══╮　┌═════┐
 * ╭╯上车║═║老司机专用║
 * └══⊙═⊙═~----╰⊙═⊙╯
 * ----------------
 * square时间总线框架otto封装类
 *
 * @author tangmingjian
 * @version v1.0
 * @date 2016-09-06 09:48
 */

public class OttoManager extends BasicManager {

    public static OttoManager get() {
        return BasicApplication.get().getManager(BasicConstant.OTTO_MANAGER);
    }

    @Override
    public void onCreate(BasicApplication appContext) {

    }
}
