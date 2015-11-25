package com.nevermore.lifecycle;

import com.nevermore.exceptions.LifecycleException;

/**
 * 使组件具有生命周期
 *
 * @author suncheng
 * @since 15/11/25
 */
public interface LifecycleAware {

    /**
     * 开启组件的生命周期
     *
     * @throws LifecycleException
     */
    void start();

    /**
     * 关闭一个组件(仅作关闭动作,不做资源回收)
     *
     * @throws LifecycleException
     */
    void stop();

    /**
     * 获得组件正在处于的生命周期
     *
     * @return 一个枚举对象
     *
     * @see LifecycleState
     */
    LifecycleState getLifecycleState();
}
