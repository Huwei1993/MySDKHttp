package com.charles.www.testDemo.util;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;

/**
 * Created by xiao on 2018/7/23.
 *
 * @author xiao
 * @date 2018/7/23
 */

public class RxBus {
    private static volatile RxBus mInstance;

    private final FlowableProcessor<Object> mBus;


    public RxBus()
    {
        mBus =  PublishProcessor.create();
    }

    public static RxBus getInstance() {
        return Holder.BUS;
    }

    public void post(Object obj) {
        mBus.onNext(obj);
    }

    public <T> Flowable<T> toFlowable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Flowable<Object> toFlowable() {
        return mBus;
    }

    public boolean hasSubscribers() {
        return mBus.hasSubscribers();
    }

    private static class Holder {
        private static final RxBus BUS = new RxBus();
    }
}
