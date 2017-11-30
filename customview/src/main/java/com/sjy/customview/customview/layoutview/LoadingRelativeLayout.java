package com.sjy.customview.customview.layoutview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.sjy.customview.customview.loadingview.OnStateClickListener;
import com.sjy.customview.customview.loadingview.SLoadingView;


/**
 * 带加载状态的父布局
 * Created by sjy on 2017/10/13.
 */

public class LoadingRelativeLayout extends RelativeLayout {
    private boolean isNeedShow=true;//初次加载时是否需要创建并显示
    private SLoadingView sLoadingView;
    private OnStateClickListener onStateClickListener;
    public LoadingRelativeLayout(Context context) {
        super(context);

    }

    public LoadingRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void addLoadingView(){
        sLoadingView=new SLoadingView(getContext());
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        sLoadingView.setLayoutParams(params);
        addView(sLoadingView);
    }

    /**
     * 该方法可能会在clear之后才调用，所以在决定是否add的时候先判断isShow
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initLoadingView();

    }

    /**
     * 初始化loadingview
     */
    private void initLoadingView() {
        //加载窗体未创建，需要显示
        if(sLoadingView==null&&isNeedShow){
            addLoadingView();
        }
        //加载窗体已创建，点击事件已设置
        if(sLoadingView!=null&&onStateClickListener!=null){
            sLoadingView.setOnStateClick(onStateClickListener);
        }
    }

    /**
     * 显示加载中
     */
    public void showLoading(){
        //表示clearAll 先于onAttach执行，跳过了创建步骤，先创建
        if(sLoadingView==null&&!isNeedShow) {
            isNeedShow=true;
            initLoadingView();
        }
        if(sLoadingView!=null){
            sLoadingView.showLoading();
        }

    }
    /**
     * 显示数据错误
     */
    public void showError(){
        //表示clearAll 先于onAttach执行，跳过了创建步骤，先创建
        if(sLoadingView==null&&!isNeedShow) {
            isNeedShow=true;
            initLoadingView();
        }
        if(sLoadingView!=null){
            sLoadingView.showError();
        }

    }
    /**
     * 显示无网络
     */
    public void showNoNet(){
        //表示clearAll 先于onAttach执行，跳过了创建步骤，先创建
        if(sLoadingView==null&&!isNeedShow) {
            isNeedShow=true;
            initLoadingView();
        }
        if(sLoadingView!=null){
            sLoadingView.showNoNet();
        }
    }
    /**
     * 显示空数据
     */
    public void showEmpty(){
        //表示clearAll 先于onAttach执行，跳过了创建步骤，先创建
        if(sLoadingView==null&&!isNeedShow) {
            isNeedShow=true;
            initLoadingView();
        }
        if(sLoadingView!=null){
            sLoadingView.showEmpty();
        }
    }

    /**
     * 清空进度view
     */
    public void clearAll(){
        isNeedShow=false;//标记关闭窗体，防止addcontent在clear之后执行
        if(sLoadingView!=null){
            sLoadingView.clearAll();
        }
    }

    /**
     * 加载状态点击监听
     * @param onStateClickListener
     */
    public void setOnStateClick(OnStateClickListener onStateClickListener){
        this.onStateClickListener=onStateClickListener;
    }
}
