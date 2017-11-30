package com.sjy.customview.customview.mutikeyboardview;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.sjy.customview.customview.NoScrollViewPager;
import com.sjy.customview.customview.R;

import java.util.ArrayList;

/**
 * 复合键盘
 * Created by sjy on 2017/9/16.
 */

public class MultiKeyBoardView extends LinearLayout {



    private static final String SHARE_PREFERENCE_NAME = "EmotionKeyboard";
    private static final String SHARE_PREFERENCE_SOFT_INPUT_HEIGHT = "soft_input_height";
    private AppCompatActivity compatActivity;
    private NoScrollViewPager noScrollViewPager;
    private TabLayout tabLayout;
    private LinearLayout llBg;
    private ImageView keyBoard;
    private String[] titles;
    private ArrayList<Fragment> list;
    private InputMethodManager mInputManager;//软键盘管理类
    private SharedPreferences sp;
    private View mContentView;//内容布局view,即除了表情布局或者软键盘布局以外的布局，用于固定bar的高度，防止跳闪

    public MultiKeyBoardView(Context context) {
        super(context);
    }

    public MultiKeyBoardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MultiKeyBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    /**
     * 初始化键盘布局
     * @param activity
     * @param titles
     * @param list
     * @param mContentView  内容布局
     */
    public MultiKeyBoardView initKeyBoardView(AppCompatActivity activity, String[] titles, ArrayList<Fragment> list, View mContentView){
        this.mContentView=mContentView;
        this.titles=titles;
        this.list=list;
        compatActivity=activity;
        sp=activity.getSharedPreferences(SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
        mInputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view= LayoutInflater.from(activity).inflate(R.layout.view_multi_keyboard,null);
        //将自定义布局加入linearlayout
        addView(view);
        noScrollViewPager= (NoScrollViewPager) view.findViewById(R.id.view_pager);
        keyBoard= (ImageView) view.findViewById(R.id.key_board);
        tabLayout= (TabLayout) view.findViewById(R.id.tab_layout);
        llBg= (LinearLayout) view.findViewById(R.id.ll_bg);
        noScrollViewPager.setAdapter(new ViewPagerCommonAdapter(activity.getSupportFragmentManager(),list,titles));
        tabLayout.setupWithViewPager(noScrollViewPager);
        noScrollViewPager.setOffscreenPageLimit(titles.length);
        initListner();

        return this;
    }



    /**
     * 设置键盘关闭图片
     * @return
     */
    public MultiKeyBoardView setKeyBoardImgRes(int res){
        keyBoard.setImageResource(res);
        return this;
    }

    /**
     * 设置键盘关闭图片主题色
     * @return
     */
    public MultiKeyBoardView setKeyBoardDark(){
        keyBoard.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_black));
        return this;
    }


    /**
     * 设置滚动标题栏背景色
     * @param tabColor
     */
    public MultiKeyBoardView setTabColor(int tabColor){
        if(llBg!=null){
            llBg.setBackgroundColor(tabColor);
        }
        return this;
    }

    /**
     * 设置滚动标题栏  字体颜色 和被选中时的颜色
     * @param fontColor
     * @param selFontColor
     */
    public MultiKeyBoardView setTabTxtColor(int fontColor,int selFontColor){
        if(tabLayout!=null){
            tabLayout.setTabTextColors(fontColor,selFontColor);
        }
       return this;
    }

    /**
     * 初始化键盘逻辑
     */
    private void initListner(){

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (noScrollViewPager.isShown()) {

                } else {
                    if (isSoftInputShown()) {
                        lockContentHeight();
                        hideSoftInput();
                        showSelfLayout();
                        unlockContentHeightDelayed();
                    } else {
                        showSelfLayout();//两者都没显示，直接显示自定义布局
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (noScrollViewPager.isShown()) {

                } else {
                    if (isSoftInputShown()) {
                        lockContentHeight();
                        hideSoftInput();
                        showSelfLayout();
                        unlockContentHeightDelayed();
                    } else {
                        showSelfLayout();//两者都没显示，直接显示自定义布局
                    }
                }
            }


        });

        /*mContentView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Logger.e(event.getAction()+"");
                if (event.getAction() == MotionEvent.ACTION_UP && noScrollViewPager.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideSelfLayout(false);//隐藏表情布局，显示软件盘。
                    //软件盘显示后，释放内容高度
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            unlockContentHeightDelayed();
                        }
                    }, 200L);
                }
                return false;
            }
        });*/
        mContentView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (noScrollViewPager.isShown()) {
                    lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
                    hideSelfLayout(false);//隐藏表情布局，显示软件盘。
                    //软件盘显示后，释放内容高度
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            unlockContentHeightDelayed();
                        }
                    }, 200L);
                }
                return false;
            }
        });
        keyBoard.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noScrollViewPager.isShown()||isSoftInputShown()) {
                    hideAllView();
                }else {
                    showSelfLayout();//两者都没显示，直接显示自定义布局
                }
            }
        });

    }

    /**
     * 强制关闭自定义view 和 键盘
     */
    public void hideAllView(){
        hideSelfLayout(false);//隐藏表情布局，显示软件盘。
        hideSoftInput();
        mContentView.requestLayout();
    }


    /**
     * 显示自定义布局,(viewpager)
     */
    private void showSelfLayout() {
        int softInputHeight = getSupportSoftInputHeight();
        if (softInputHeight == 0) {
            softInputHeight = sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 400);
        }
        //隐藏软键盘和其他布局
        hideSoftInput();
        noScrollViewPager.getLayoutParams().height = softInputHeight;
        noScrollViewPager.setVisibility(View.VISIBLE);
    }
    /**
     * 隐藏自定义布局
     * @param showSoftInput 是否显示软件盘
     */
    public void hideSelfLayout(boolean showSoftInput) {
        if (noScrollViewPager.isShown()) {
            noScrollViewPager.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }
    /**
     * 锁定内容高度，防止跳闪
     */
    private void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContentView.getLayoutParams();
        params.height = mContentView.getHeight();
        params.weight = 0.0F;
    }
    /**
     * 释放被锁定的内容高度
     */
    private void unlockContentHeightDelayed() {
        noScrollViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) mContentView.getLayoutParams()).weight = 1.0F;
            }
        }, 200L);
    }
    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private void showSoftInput() {
        mContentView.post(new Runnable() {
            @Override
            public void run() {
                mInputManager.showSoftInput(mContentView, 0);
            }
        });
    }
    /**
     * 隐藏软件盘
     */
    private void hideSoftInput() {
        mInputManager.hideSoftInputFromWindow(noScrollViewPager.getWindowToken(), 0);
    }
    /**
     * 是否显示软件盘
     * @return
     */
    private boolean isSoftInputShown() {
        return getSupportSoftInputHeight() != 0;
    }
    /**
     * 获取软件盘的高度
     * @return
     */
    private int getSupportSoftInputHeight(){
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        compatActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        //获取屏幕的高度
        int screenHeight = compatActivity.getWindow().getDecorView().getRootView().getHeight();
        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;
        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        if (Build.VERSION.SDK_INT >= 20) {
            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
            softInputHeight = softInputHeight - getSoftButtonsBarHeight();
        }
        if (softInputHeight < 0) {
        }
        //存一份到本地
        if (softInputHeight > 0) {
            sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
        }
        return softInputHeight;
    }
    /**
     * 底部虚拟按键栏的高度
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private int getSoftButtonsBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        compatActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        //获取当前屏幕的真实高度
        compatActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }
    /**
     * 获取软键盘高度
     * @return
     */
    public int getKeyBoardHeight(){
        return sp.getInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, 400);
    }
}
