package com.sjy.customview.commoncustomview;

import android.view.View;
import android.widget.TextView;

/**
 * Created by sjy on 2017/12/2.
 */

public class ViewWrapper {
    private int Height;
    private View textView;

    public int getHeight() {
        return textView.getLayoutParams().height;
    }

    public void setHeight(int height) {
        textView.getLayoutParams().height = height;
        textView.requestLayout();
    }

    public ViewWrapper(View textView) {
        this.textView = textView;
    }
}
