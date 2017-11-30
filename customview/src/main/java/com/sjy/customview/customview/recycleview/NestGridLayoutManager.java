package com.sjy.customview.customview.recycleview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

/**
 * 嵌套布局
 * Created by sjy on 2017/6/23.
 */

public class NestGridLayoutManager extends GridLayoutManager {


    public NestGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setNestedScorll();
    }

    public NestGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
        setNestedScorll();
    }

    public NestGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        setNestedScorll();
    }

    private void setNestedScorll(){
            this.setSmoothScrollbarEnabled(true);
            this.setAutoMeasureEnabled(true);
    }
}
