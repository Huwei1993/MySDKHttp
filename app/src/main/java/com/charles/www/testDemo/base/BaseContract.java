package com.charles.www.testDemo.base;


import com.charles.www.testDemo.base.presenter.IPresenter;
import com.charles.www.testDemo.base.view.BaseView;

/**
 * Created by liuzhao on 2017/4/13.
 */

public interface BaseContract {
    interface View<M> extends BaseView<M> {
    }

    interface Presenter<V extends BaseView> extends IPresenter<V> {

    }

}
