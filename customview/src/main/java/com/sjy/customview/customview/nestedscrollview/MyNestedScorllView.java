package com.sjy.customview.customview.nestedscrollview;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by sjy on 2017/6/23.
 */

public class MyNestedScorllView extends NestedScrollView {
    public MyNestedScorllView(Context context) {
        super(context);
    }

    public MyNestedScorllView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNestedScorllView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置滚动到底部方法
     * @param onScrollBottomListener
     */
    public void setScrollBottomMethod(final OnScrollBottomListener onScrollBottomListener ){
        this.setOnScrollChangeListener(new OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    onScrollBottomListener.onBottom();
                }
            }
        });
    }
}
