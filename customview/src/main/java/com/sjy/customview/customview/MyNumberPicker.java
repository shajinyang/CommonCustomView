package com.sjy.customview.customview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * 设置number字体和diver颜色
 * Created by sjy on 2017/6/29.
 */

public class MyNumberPicker extends NumberPicker {
    public MyNumberPicker(Context context) {
        super(context);
        this.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public MyNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    public MyNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    public void updateView(View view) {
        if (view instanceof EditText) {
            //这里修改字体的属性
            ((EditText) view).setTextColor(Color.parseColor("#333333"));
            ((EditText) view).setTextSize(16);
        }
    }
}
