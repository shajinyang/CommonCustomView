package com.sjy.customview.commoncustomview;

import android.widget.TextView;

/**
 * Created by sjy on 2017/12/2.
 */

public class TextViewWrapper {
    private int Height;
    private TextView textView;

    public int getHeight() {
        return textView.getLayoutParams().height;
    }

    public void setHeight(int height) {
        textView.getLayoutParams().height = height;
        textView.requestLayout();
    }

    public TextViewWrapper(TextView textView) {
        this.textView = textView;
    }
}
