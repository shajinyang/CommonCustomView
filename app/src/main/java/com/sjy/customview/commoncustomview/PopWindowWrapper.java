package com.sjy.customview.commoncustomview;

import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * Created by sjy on 2017/12/2.
 */

public class PopWindowWrapper {
    private int Height;
    private PopupWindow popupWindow;

    public int getHeight() {
        return popupWindow.getContentView().getLayoutParams().height;
    }

    public void setHeight(int height) {
        popupWindow.getContentView().getLayoutParams().height=height;
        popupWindow.getContentView().requestLayout();
    }

    public PopWindowWrapper(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }
}
