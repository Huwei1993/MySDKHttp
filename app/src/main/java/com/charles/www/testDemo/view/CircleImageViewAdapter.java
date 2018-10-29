package com.charles.www.testDemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.charles.www.testDemo.R;
import com.charles.www.testDemo.bean.SignBean;
import com.charles.www.testDemo.view.badge.BadgeTextView;

import java.util.ArrayList;

/**
 * Created by huwei on 2018/8/8.
 *
 * @author : huwei
 * @date : 2018/8/8
 * @Sub :
 */

public abstract class CircleImageViewAdapter extends BaseAdapter {
    private GridView mGridView;
    private Context mContext;
    private ArrayList<SignBean> mList;
    private int max_num = 9;//默认
    private int columns = 4;//默认
    private boolean isNoPic = true;
    private ViewHolder holder = null;
    private int mSelectPosision = -1;

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public int getCount() {
        return (mList == null ? 0 : mList.size());
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
//        mSelectPosision = i;
        return i;
    }

    public CircleImageViewAdapter(Context mContext, ArrayList<SignBean> list, GridView mGridView) {
        this.mContext = mContext;
        this.mList = list;
        this.mGridView = mGridView;
    }

    public CircleImageViewAdapter(Context mContext, ArrayList<SignBean> list, GridView mGridView, int max_num, int columns) {
        this.mContext = mContext;
        this.mList = list;
        this.mGridView = mGridView;
        this.max_num = max_num;
        this.columns = columns;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        int horizontalSpacing = mGridView.getHorizontalSpacing();

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.circle_image_item, null);
            holder.iv = (CircleImageView) convertView.findViewById(R.id.iv_circle_head);
            holder.hosetName = (TextView) convertView.findViewById(R.id.tv_host_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.icon_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (onDeleteClickListener != null) {
//                    onDeleteClickListener.delete(position);
//                }
//            }
//        });
        Glide.with(mContext).load(mList.get(position).getImageUrl()).into(holder.iv);
        if (holder != null && mList.get(position).isSelect()) {
            holder.iv.setBorderWidth(5);
            holder.iv.setBorderColor(mContext.getResources().getColor(R.color.colorGray));
        } else {
            holder.iv.setBorderWidth(0);
        }
        BadgeTextView badgeTextView = null;
        int host_ballot_num = Integer.valueOf(mList.get(position).getHost_num());
        if (host_ballot_num > 0){
            badgeTextView  = new BadgeTextView(mContext);
            badgeTextView.setTargetView(holder.iv);
            badgeTextView.setBadgeCount(host_ballot_num).setDefaultRadius(5).setmDefaultRightPadding(20).setmDefaultTopPadding(20).built();
            badgeTextView.setBadgeShown(true);
        }
        holder.hosetName.setText(mList.get(position).getHost_name());
        notifyDataSetChanged();
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        updateView();
    }

    protected abstract void updateView();

    public class ViewHolder {
        public CircleImageView iv;
        public TextView hosetName;


    }

    public interface OnDeleteClickListener {
        void delete(int position);
    }

}
