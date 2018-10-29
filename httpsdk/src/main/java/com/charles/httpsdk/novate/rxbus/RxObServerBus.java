package com.charles.httpsdk.novate.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;


/**
 * Created by xiao on 2018/5/21.
 *  没有背压处理的 Rxbus
 * @author xiao
 * @date 2018/5/21
 */

public class RxObServerBus {
    private final Subject<Object> mBus;

    private RxObServerBus() {
        // toSerialized method made bus thread safe
        mBus = PublishSubject.create().toSerialized();
    }

    public static RxObServerBus get() {
        return Holder.BUS;
    }

    public void post(Object obj) {
        mBus.onNext(obj);
    }

    public <T> Observable<T> toObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    private static class Holder {
        private static final RxObServerBus BUS = new RxObServerBus();
    }

}
