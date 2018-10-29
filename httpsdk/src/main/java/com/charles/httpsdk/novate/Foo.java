package com.charles.httpsdk.novate;

import java.lang.reflect.ParameterizedType;

/**
 * Created by Administrator on 2018/3/5.
 */

public abstract class Foo<T> {
    public Class<T> getTClass()
    {
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }
}
