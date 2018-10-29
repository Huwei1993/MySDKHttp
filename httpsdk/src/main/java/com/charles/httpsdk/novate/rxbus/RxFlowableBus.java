package com.charles.httpsdk.novate.rxbus;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;


/**
 * Created by xiao on 2018/5/21.
 *  有背压处理的 Rxbus
 * @author xiao
 * @date 2018/5/21
 */

public class RxFlowableBus {
    private final FlowableProcessor<Object> mBus;

    private RxFlowableBus() {
        // toSerialized method made bus thread safe
        mBus = PublishProcessor.create().toSerialized();
    }

    public static RxFlowableBus get() {
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
        private static final RxFlowableBus BUS = new RxFlowableBus();
    }

}
