package com.charles.httpsdk.novate.model;

import android.content.Context;
import android.view.View;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by huwei on 2018/8/31.
 *
 * @author : huwei
 * @date : 2018/8/31
 * @Sub :
 */
public class BaseModel<T> implements IModel {
    protected Context mContext;
    private View mView;

    public BaseModel(Context context) {
        mContext = context;
    }

    protected LifecycleProvider getLifecycleProvider() {
        LifecycleProvider provider = null;
        if (null != mContext && mContext instanceof LifecycleProvider) {
            provider = (LifecycleProvider) mContext;
        }
        if(provider == null && mView != null && mView instanceof LifecycleProvider){
            provider = (LifecycleProvider) mView;
        }
        return provider;
    }

    public Observable subscribe(Observable mObservable, Observer observer) {
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(getLifecycleProvider().bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
        return mObservable;
    }

    public Observable subscribe2(Observable mObservable, Observer observer){
        mObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .compose(getLifecycleProvider().bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(observer);
        return mObservable;
    }
}
