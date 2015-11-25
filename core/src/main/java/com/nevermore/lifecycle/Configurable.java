package com.nevermore.lifecycle;

import com.nevermore.context.Context;

/**
 * 使组件可配置
 *
 * @author suncheng
 * @since 15/11/25
 */
public interface Configurable {

    /**
     * 跟据上下文提供的信息对组件进行配置
     *
     * @param context 上下文
     */
    void configure(Context context);
}
