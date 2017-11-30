package com.sjy.customview.customview.counttimeview;

/**
 *
 * Created by sjy on 2017/9/21.
 */

public interface OnTimeListener {
    //计时动画开始时
    void startTime();
    //计时动画结束
    void endTime();
    //点击时判断是否触发动画
    boolean onClickTime();
}
