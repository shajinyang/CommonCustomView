package com.sjy.customview.customview.recycleview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * 嵌套布局
 * Created by sjy on 2017/6/23.
 */

public class NestLinearLayoutManager extends LinearLayoutManager {
    public NestLinearLayoutManager(Context context) {
        super(context);
        setNestedScorll();
    }

    public NestLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
        setNestedScorll();
    }

    public NestLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setNestedScorll();
    }

    private void setNestedScorll(){
            this.setSmoothScrollbarEnabled(true);
            this.setAutoMeasureEnabled(true);
    }
}
