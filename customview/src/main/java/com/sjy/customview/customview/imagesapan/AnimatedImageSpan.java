package com.sjy.customview.customview.imagesapan;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.style.DynamicDrawableSpan;

/**
 * 支持gif动画的imagespan
 */
public class AnimatedImageSpan extends DynamicDrawableSpan {

    private Drawable mDrawable;

    public AnimatedImageSpan(Drawable d) {
        super();
        mDrawable = d;
        // Use handler for 'ticks' to proceed to next frame 
        final Handler mHandler = new Handler();
        mHandler.post(new Runnable() {
            public void run() {
                ((AnimatedGifDrawable)mDrawable).nextFrame();
                // Set next with a delay depending on the duration for this frame 
                mHandler.postDelayed(this, ((AnimatedGifDrawable)mDrawable).getFrameDuration());
            }
        });
    }
    @Override
    public Drawable getDrawable() {
        return ((AnimatedGifDrawable)mDrawable).getDrawable();
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();

        if (fm != null) {
            fm.ascent = -rect.bottom; 
            fm.descent = 0; 

            fm.top = fm.ascent;
            fm.bottom = 0;
        }

        return rect.right;
    }
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        canvas.save();

        int transY = bottom - b.getBounds().bottom;
        if (mVerticalAlignment == ALIGN_BASELINE) {
            transY -= paint.getFontMetricsInt().descent;
        }

        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
}