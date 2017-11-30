package com.sjy.customview.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


/**
 * 圆角矩形,圆形
 * Created by sjy on 2017/5/31.
 */

public class CircleRectangleView  extends AppCompatImageView {
    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLORDRAWABLE_DIMENSION = 1;

    public static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final int DEFAULT_ROUND_RADIUS = 10;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();

    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();

    private int type;
    private int mRoundRadius = DEFAULT_ROUND_RADIUS;
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    private Bitmap mBitmap;
    //渲染图像，使用图像为绘制图形着色
    private BitmapShader mBitmapShader;
    //图片宽高
    private int mBitmapWidth;
    private int mBitmapHeight;

    //布局宽高
    private int mWidth;
    private int mHeight;

    //图片半径，边缘半径(TYPE_CIRCLE才用到)
    private float mDrawableRadius;
    private float mBorderRadius;

    private boolean mReady;
    private boolean mSetupPending;

    public CircleRectangleView(Context context) {
        this(context, null);
    }

    public CircleRectangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleRectangleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleRectangleView, defStyle, 0);
        // 获取类型
        type = a.getInt(R.styleable.CircleRectangleView_type, TYPE_CIRCLE);
        // 获取圆角半径
        mRoundRadius = a.getDimensionPixelSize(R.styleable.CircleRectangleView_round_Radius, DEFAULT_ROUND_RADIUS);
        // 获取边界的宽度
        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleRectangleView_border_width, DEFAULT_BORDER_WIDTH);
        // 获取边缘的颜色
        mBorderColor = a.getColor(R.styleable.CircleRectangleView_border_color,DEFAULT_BORDER_COLOR);
        //调用 recycle() 回收TypedArray,以便后面重用
        a.recycle();

        init();
    }

    /**
     * 作用就是保证第一次执行setup函数里下面代码要在构造函数执行完毕时调用
     */
    private void init() {
        //在这里ScaleType被强制设定为CENTER_CROP，就是将图片水平垂直居中，进行缩放
        super.setScaleType(SCALE_TYPE);
        mReady = true;
        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准
         */
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            setMeasuredDimension(mWidth, mWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //如果图片不存在就不画
        if (getDrawable() == null)
            return;

        if (type == TYPE_ROUND) {
            //绘制内圆角矩形，参数矩形区域，圆角半径，图片画笔为mBitmapPaint
            canvas.drawRoundRect(mDrawableRect, mRoundRadius, mRoundRadius, mBitmapPaint);
            if (mBorderWidth != 0) {
                //如果圆形边缘的宽度不为0 我们还要绘制带边界的外圆角矩形  参数矩形区域，圆角半径，边界画笔为mBorderPaint
                canvas.drawRoundRect(mBorderRect , mRoundRadius + mBorderWidth / 2, mRoundRadius + mBorderWidth / 2, mBorderPaint);
            }
        } else if (type == TYPE_CIRCLE) {
            //绘制内圆形，参数圆心坐标，内圆半径，图片画笔为mBitmapPaint
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
            //如果圆形边缘的宽度不为0 我们还要绘制带边界的外圆形 参数圆心坐标，外圆半径，边界画笔为mBorderPaint
            if (mBorderWidth != 0) {
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
            }
        }
    }

    /**
     * 这个函数很关键，进行图片画笔边界画笔(Paint)一些重绘参数初始化：
     * 构建渲染器BitmapShader用Bitmap来填充绘制区域,设置样式以及内外圆半径计算等，
     * 以及调用updateShaderMatrix()函数和 invalidate()函数；
     */
    private void setup() {
        //因为mReady默认值为false,所以第一次进这个函数的时候if语句为真进入括号体内
        //设置mSetupPending为true然后直接返回，后面的代码并没有执行。
        if (!mReady) {
            mSetupPending = true;
            return;
        }
        //防止空指针异常
        if (mBitmap == null) {
            return;
        }
        // 构建渲染器，用mBitmap来填充绘制区域 ，参数值代表如果图片太小的话 就直接拉伸
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        // 设置图片画笔反锯齿
        mBitmapPaint.setAntiAlias(true);
        // 设置图片画笔渲染器
        mBitmapPaint.setShader(mBitmapShader);
        // 设置边界画笔样式
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        //这个地方是取的原图片的宽高
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();
        //设置含边界显示区域，取的是CircleImageView的布局实际大小
        mBorderRect.set(0, 0, getWidth(), getHeight());
        //初始图片显示区域为mBorderRect减去边缘部分
        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);

        //下面计算的值都是为onDraw中画图做准备
        if (type == TYPE_CIRCLE) {
            //计算 圆形带边界部分（外圆）的半径，取mBorderRect的宽高减去一个边缘大小的一半的较小值
            mBorderRadius = (mBorderRect.width() - mBorderWidth)/2;
            //这里计算的是内圆的半径，也即去除边界宽度的半径
            mDrawableRadius = mDrawableRect.width()/2;
        } else if (type == TYPE_ROUND) {
            //如果是圆角矩形，重新计算边缘区域，使处于边缘正中央
            mBorderRect.set(mBorderWidth/2, mBorderWidth/2, getWidth() - mBorderWidth/2, getHeight() - mBorderWidth/2);
        }

        //设置渲染器的变换矩阵也即是mBitmap用何种缩放形式填充
        updateShaderMatrix();
        //手动触发ondraw()函数 完成最终的绘制
        invalidate();
    }

    /**
     * 这个函数为设置BitmapShader的Matrix参数，设置最小缩放比例，平移参数。
     * 作用：保证图片损失度最小和始终绘制图片正中央的那部分
     */
    private void updateShaderMatrix() {
        float scaleX = 1.0f;
        float scaleY = 1.0f;
        float scale = 1.0f;
        float dx = 0;
        float dy = 0;
        // 如果图片的宽或者高与view的宽高不匹配，计算出需要缩放的比例；缩放后的图片的宽高，一定要大于我们view的宽高；所以我们这里取大值
        if (type == TYPE_CIRCLE) {
            scaleX = mWidth * 1.0f / mBitmapWidth;
            scaleY = mWidth * 1.0f / mBitmapHeight;
            scale = Math.max(scaleX, scaleY);
        } else if (type == TYPE_ROUND) {
            scaleX = getWidth() * 1.0f / mBitmapWidth;
            scaleY = getHeight() * 1.0f / mBitmapHeight;
            scale = Math.max(scaleX, scaleY);
        }
        if (scaleX > scaleY) {
            // x轴缩放 y轴平移 使得图片的x轴方向的边的尺寸缩放到图片显示区域（mDrawableRect）一样）
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        } else {
            // y轴缩放 x轴平移 使得图片的y轴方向的边的尺寸缩放到图片显示区域（mDrawableRect）一样）
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        }

        mShaderMatrix.set(null);
        //缩放
        mShaderMatrix.setScale(scale, scale);
        // 平移
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
        // 设置变换矩阵
        mBitmapShader.setLocalMatrix(mShaderMatrix);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    public int getType() {
        return type;
    }

    public void setType(int mType) {
        if (type == mType) {
            return;
        }
        type = mType;
        requestLayout();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }
        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }
        mBorderWidth = borderWidth;
        setup();
    }

    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    /**
     * 以下四个函数都是
     * 复写ImageView的setImageXxx()方法
     * 注意这个函数先于构造函数调用之前调用
     * @param bm
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }
}
