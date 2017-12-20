package com.sjy.customview.commoncustomview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sjy.customview.customview.loadingview.OnStateClickListener;
import com.sjy.customview.customview.loadingview.SLoadingView;

public class MainActivity extends AppCompatActivity {
    PopupWindow popupWindow;
    View popView;
    SLoadingView sLoadingView;
    private boolean isOpen=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showPop();
//                showPopWindow();
                showCusPop(v);
            }
        });
        sLoadingView= findViewById(R.id.sloading);
        sLoadingView.showError();
        sLoadingView.setOnStateClick(new OnStateClickListener() {
            @Override
            public void onClickError() {
                sLoadingView.showNoNet();
            }

            @Override
            public void onClickNoNet() {
                sLoadingView.clearAll();
            }
        });
    }


    private void showPop(){
        /*if(popupWindow!=null&&popupWindow.isShowing()){
            popupWindow.dismiss();
            return;
        }
        View view= LayoutInflater.from(this).inflate(R.layout.pop_view,null);
        popupWindow=new PopupWindow(this);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//去除阴影
//        popupWindow.setAnimationStyle(R.style.Popupwindow);
        popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.pop_view,null));
        popupWindow.showAsDropDown(findViewById(R.id.txt));*/
        int startheight=findViewById(R.id.txt).getHeight();
        TextViewWrapper tw=new TextViewWrapper((TextView) findViewById(R.id.txt));
        if(isOpen==false) {
            ObjectAnimator oa= ObjectAnimator
                    .ofInt(tw, "Height", startheight, 3 * startheight)
                    .setDuration(300);
            oa.setInterpolator(new AccelerateDecelerateInterpolator());
            oa.start();
            isOpen = true;
        }else {
            ObjectAnimator oa= ObjectAnimator
                    .ofInt(tw, "Height", startheight, startheight/3)
                    .setDuration(300);
            oa.setInterpolator(new AccelerateDecelerateInterpolator());
            oa.start();
            isOpen = false;
        }
    }

    private void showPopWindow(){
        if(popupWindow!=null&&popupWindow.isShowing()){
            popupWindow.dismiss();
            return;
        }
        View view= LayoutInflater.from(this).inflate(R.layout.pop_view,null);
        popupWindow=new PopupWindow(this);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());//去除阴影
        popupWindow.setContentView(view);
//        popupWindow.setAnimationStyle(R.style.Popupwindow);
        popupWindow.showAsDropDown(findViewById(R.id.txt));
        int startheight=findViewById(R.id.txt).getHeight();
        TextViewWrapper tw=new TextViewWrapper((TextView) view.findViewById(R.id.imageView));
        if(isOpen==false) {
            ObjectAnimator oa= ObjectAnimator
                    .ofInt(tw, "Height", startheight, 3 * startheight)
                    .setDuration(300);
            oa.setInterpolator(new AccelerateDecelerateInterpolator());
            oa.start();
            isOpen = true;
        }else {
            ObjectAnimator oa= ObjectAnimator
                    .ofInt(tw, "Height", startheight, startheight/3)
                    .setDuration(300);
            oa.setInterpolator(new AccelerateDecelerateInterpolator());
            oa.start();
            isOpen = false;
        }
    }

    private void showCusPop(View view){
        if(popView==null) {
            popView = LayoutInflater.from(this).inflate(R.layout.pop_view, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, view.getBottom(), 0, 0);
            addContentView(popView, params);
        }
        ViewWrapper tw = new ViewWrapper(popView.findViewById(R.id.imageView));
        if (isOpen == false) {
            ObjectAnimator oa = ObjectAnimator
                    .ofInt(tw, "Height", 0, 800)
                    .setDuration(300);
            oa.setInterpolator(new AccelerateDecelerateInterpolator());
            oa.start();
            isOpen = true;
        } else {
            ObjectAnimator oa = ObjectAnimator
                    .ofInt(tw, "Height", 800, 0)
                    .setDuration(300);
            oa.setInterpolator(new AccelerateDecelerateInterpolator());
            oa.start();
            isOpen = false;
        }
    }


}
