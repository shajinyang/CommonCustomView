
![](sjylogo.png)
# 安卓UI组件库

### 如何使用

#### Android Studio
    第一步：
      在项目的gradle里配置
      allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
      }

      第二步：
      在module的gradle里配置
      dependencies {
         ...
      	 compile 'com.github.shajinyang:CommonCustomView:1.0.0'
      }





### 常用自定义View
#### CodeView
    倒计时View,基于TextView
    可设置显示文字，倒计时时间长度
    使用示例：
    xml:
    <com.zx.xsk.views.counttimeview.CodeView
                android:id="@+id/code_view"
                android:layout_width="90dp"
                android:layout_height="wrap_content"
                android:padding="8dp"
                android:gravity="center"
                android:background="@drawable/shape_bg_accent_round"
                android:foreground="?android:attr/selectableItemBackground"
                android:textColor="@color/whiteff"
                android:layout_marginRight="16dp"
                />


    java:
    binding.codeView.setOnTimeListener(new OnTimeListener() {
                @Override
                public void startTime() {

                }

                @Override
                public void endTime() {

                }

                @Override
                public boolean onClickTime() {
                    if(TextViewUtil.isEmpty(binding.tel)){
                        Toaster.showToast("手机号未填写");
                        return false;
                    }else if(!TextViewUtil.isMobileNO(binding.tel.getText().toString().trim())){
                        Toaster.showToast("手机号格式不正确");
                        return false;
                    }else {
                        getTelCode();
                    }
                    return true;
                }
            });

#### AnimatedImageSpan
    支持gif的imagespan
#### MyImageSpan
    居中的imagespan
#### SLoadingView
    加载状态view（网络错误，空数据，加载中，无网络）
#### LoadingRelativeLayout
    加载状态父布局（网络错误，空数据，加载中，无网络）
    使用示例：
    xml:
     <com.zx.xsk.views.LoadingRelativeLayout
            android:id="@+id/loading"
            android:layout_width="match_parent"
            android:layout_height="match_parent">
        ...

     </com.zx.xsk.views.LoadingRelativeLayout>

      java:
      loadingRelativeLayout.showNoNet();//显示网络未连接
      loadingRelativeLayout.showLoading();//显示加载中
      loadingRelativeLayout.showEmpty();//显示空数据
      loadingRelativeLayout.showError();//显示网络错误
      loadingRelativeLayout.clearAll();//关闭状态视图
      //状态点击回调
      loadingRelativeLayout.setOnStateClick(new OnStateClickListener() {
        @Override
        public void onClickError() {
            //todo
        }

        @Override
        public void onClickNoNet() {
            //todo
        }
      });

#### MultiKeyBoardView
     复合键盘布局，基于viewpager,主要是优化了系统键盘和view的显示逻辑
     使用示例：
     xml:
     <com.zx.xsk.views.mutikeyboardview.MultiKeyBoardView
                 android:id="@+id/mul"
                 android:layout_width="match_parent"
                 android:layout_height="wrap_content"
                 android:orientation="vertical"
                 />

      java:
      binding.mulitikeyboardview
             .initKeyBoardView(this,titles,fragmentArrayList,binding.richEditor)
             .setTabColor(0xff333333)
             .setTabTxtColor(0xff999999,0xffffffff);
      具体参数含义看源码注释

#### MyNestedScorllView
     带底部滑动监听的nestscorllview
#### MyRecycleview
     带底部滑动监听的recycleview
#### NestedRecycleView
     嵌套nestedscorllview的recycleview,解决滑动冲突
     配合NestGridLayoutManager、NestLinearLayoutManager使用

#### SToolBarView
     toolbar封装
#### BubbleImageView
     仿微信聊天气泡Imageview
#### CircleImageView
     圆形图片，支持边框设置
#### CircleRectangleView
     圆角图片（可设置圆形图片）支持边框设置
#### MyNumberPicker
     设置picker边框和字体
#### NoScrollViewPager
     无滚动viewerpager
#### SquareLinearLayout
     正方形布局

#### DragFloatImageView
    可拖拽带吸附效果的imageview

















