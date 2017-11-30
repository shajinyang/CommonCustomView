package com.sjy.customview.customview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;

import com.sjy.customview.customview.utils.ScreenUtils;


/**
 * 可拖拽imageview，带吸附效果
 * 已添加点击事件
 */
public class DragFloatImageView extends android.support.v7.widget.AppCompatImageView {
    private int screenWidth;
    private int screenHeight;
    private int screenWidthHalf;
    private int statusHeight;
    private int virtualHeight;
    long time=0l;//记录点击时间
    int startX=0;
    int startY=0;

    private OnImageClickListener onImageClickListener;

    public DragFloatImageView(Context context) {
        super(context);
        init();
    }

    public DragFloatImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DragFloatImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClickable(true);
        screenWidth = ScreenUtils.getScreenWidth(getContext());
        screenWidthHalf = screenWidth / 2;
        screenHeight = ScreenUtils.getScreenHeight(getContext());
        statusHeight = ScreenUtils.getStatusHeight(getContext());
        virtualHeight = ScreenUtils.getVirtualBarHeigh(getContext());
    }


    /**
     * 点击监听
     * @param onImageClickListener
     */
    public void setClickListener(OnImageClickListener onImageClickListener){
        this.onImageClickListener=onImageClickListener;
    }

    private int lastX;
    private int lastY;
    private boolean isDrag;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int rawX = (int) event.getRawX();
        int rawY = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                isDrag = false;
                getParent().requestDisallowInterceptTouchEvent(true);
                lastX = rawX;
                lastY = rawY;
                startX=lastX;
                startY=lastY;
                time= System.currentTimeMillis();
                break;
            case MotionEvent.ACTION_MOVE:
                isDrag = true;
                //计算手指移动了多少
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些手机无法触发点击事件的问题
                /*int distance = (int) Math.sqrt(dx * dx + dy * dy);
                if (distance < 3) {//给个容错范围，不然有部分手机还是无法点击
                    isDrag = false;
                    Logger.e("执行了容错");
                    break;
                }*/
                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > screenWidth - getWidth() ? screenWidth - getWidth() : x;
                // y = y < statusHeight ? statusHeight : (y + getHeight() >= screenHeight ? screenHeight - getHeight() : y);
                if (y < 0) {
                    y = 0;
                }
                if (y > screenHeight - statusHeight - getHeight()) {
                    y = screenHeight - statusHeight - getHeight();
                }
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_UP:
                //根据点击间隔时间以及控件移动距离来判断是否是点击
                if(System.currentTimeMillis()-time<200l&& Math.abs(lastX-startX)<10&& Math.abs(lastY-startY)<10){
                    if(onImageClickListener!=null) {
                        onImageClickListener.onClick();
                    }
                    break;
                }
                if (isDrag) {
                    //恢复按压效果
                    setPressed(false);
                    if (rawX >= screenWidthHalf) {
                        animate().setInterpolator(new DecelerateInterpolator())
                                .setDuration(300)
                                .xBy(screenWidth - getWidth() - getX())
                                .start();
                    } else {
                        ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                        oa.setInterpolator(new DecelerateInterpolator());
                        oa.setDuration(300);
                        oa.start();
                    }
                }
                break;
        }
        //如果是拖拽则消耗事件，否则正常传递即可。
        return isDrag || super.onTouchEvent(event);
    }

    public interface  OnImageClickListener{
        void onClick();
    }
}