package com.sjy.customview.customview.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 嵌套nestedscorllview的recycleview
 * Created by sjy on 2017/6/22.
 */

public class NestedRecycleView extends RecyclerView {

    public NestedRecycleView(Context context) {
        super(context);
        setNestedScorll();
    }

    public NestedRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setNestedScorll();
    }

    public NestedRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setNestedScorll();
    }


    private void setNestedScorll(){
        this.setHasFixedSize(true);
        this.setNestedScrollingEnabled(false);
    }



}
