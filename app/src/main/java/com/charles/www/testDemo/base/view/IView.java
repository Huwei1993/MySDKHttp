package com.charles.www.testDemo.base.view;

/**
 * Created by apple on 2017/4/5.
 */

public interface IView<M> {

    /**
     * 展示加载条
     */
    void showLoading();

    void showShortToast(String msg);

    void showLongToast(String msg);

    /**
     * 隐藏加载条
     */
    void hideLoading();

    void showException(Throwable pe);


}
