package com.charles.www.testDemo.activity;

import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.charles.www.testDemo.R;
import com.charles.www.testDemo.base.BaseActivity;

import butterknife.BindView;

public class LinkRecycleViewActivity extends BaseActivity {

    @BindView(R.id.rv_sort) RecyclerView rvSort;
    @BindView(R.id.lin_fragment) FrameLayout linFragment;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_link_recycle_view;
    }

    @Override
    protected void initEventAndData() {
        //



    }

}
