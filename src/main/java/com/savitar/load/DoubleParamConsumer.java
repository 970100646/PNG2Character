package com.savitar.load;

/*
 * @author Savitar
 * @date 2021/8/10 16:47
 */
public interface DoubleParamConsumer<T,U> {
    void accept(T t,U u);
}
