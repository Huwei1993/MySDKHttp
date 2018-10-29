package com.charles.httpsdk.novate.rxbus;

import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import io.reactivex.Observable;


/**
 * Created by xiao on 2018/5/21.
 *  有异常处理的 Rxbus
 * @author xiao
 * @date 2018/5/21
 */

public class RxExcepTionBus {
    private static volatile RxExcepTionBus instance;
    private final Relay<Object> mBus;


    public RxExcepTionBus(){
        this.mBus = PublishRelay.create().toSerialized();
    }

    public static RxExcepTionBus getDefault(){
        if (instance == null){
            synchronized (RxExcepTionBus.class){
                if (instance == null){
                    instance = Holder.BUS;
                }
            }
        }
        return instance;
    }

    public void post(Object obj) {
        mBus.accept(obj);
    }

    public <T> Observable<T> toObservable(Class<T> tClass) {
        return  mBus.ofType(tClass);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    private static class Holder {
        private static final RxExcepTionBus BUS = new RxExcepTionBus();
    }


}
