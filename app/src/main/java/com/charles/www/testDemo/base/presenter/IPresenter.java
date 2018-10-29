package com.charles.www.testDemo.base.presenter;


import com.charles.www.testDemo.base.view.IView;

/**
 * Created by liuzhao on 2017/4/13.
 */

public interface IPresenter<V extends IView> {
    /**
     * Set or attach the view to this presenter
     */
    void attachView(V view);

    /**
     * Will be called if the view has been destroyed. Typically this method will be invoked from
     * <code>Activity.detachView()</code> or <code>Fragment.onDestroyView()</code>
     */
    void detachView(boolean retainInstance);
}
