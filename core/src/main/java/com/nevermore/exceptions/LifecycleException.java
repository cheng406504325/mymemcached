package com.nevermore.exceptions;

/**
 * 生命周期进行中发生的异常
 *
 * @author suncheng
 * @since 15/11/25
 */
public class LifecycleException extends RuntimeException {
    private static final long serialVersionUID = 4689000562519155240L;

    public LifecycleException() {
        super();
    }

    public LifecycleException(String message) {
        super(message);
    }

    public LifecycleException(String message, Throwable t) {
        super(message, t);
    }

    public LifecycleException(Throwable t) {
        super(t);
    }
}
