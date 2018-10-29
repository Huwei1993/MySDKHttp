package com.charles.www.testDemo.base.presenter;


import com.charles.www.testDemo.base.view.BaseView;

/**
 * Created by liuzhao on 2017/4/13.
 */

public abstract class BasePresenter<V extends BaseView> implements IPresenter<V> {
    public V view;

    @Override
    public void attachView(V view) {
        this.view = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        view = null;
    }
}
