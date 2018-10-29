package com.charles.www.testDemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by huwei on 2018/8/8.
 *
 * @author : huwei
 * @date : 2018/8/8
 * @Sub :
 */

public class FixedGridView extends GridView {
    public FixedGridView(Context context) {
        super(context);
    }

    public FixedGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
