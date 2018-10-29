package com.charles.www.testDemo.adapter;

/**
 * Created by xiao on 2018/5/18.
 *
 * @author xiao
 * @date 2018/5/18
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.charles.www.testDemo.R;
import com.charles.www.testDemo.bean.bean;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.TestViewHolder> {
    private Context context;
    private List<bean> data;

    public TestAdapter(List<bean> data, Context context) {
        this.context = context;
        this.data = data;
    }


    @Override
    public TestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_camera_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final TestViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {

        public TestViewHolder(View itemView) {
            super(itemView);
        }
    }
}
