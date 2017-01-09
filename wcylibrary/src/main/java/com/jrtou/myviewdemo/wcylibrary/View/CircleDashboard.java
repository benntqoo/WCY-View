package com.jrtou.myviewdemo.wcylibrary.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.jrtou.myviewdemo.wcylibrary.R;
import com.jrtou.myviewdemo.wcylibrary.Tool.PxUtils;

/**
 * Created by Jrtou on 2017/1/7.
 * 圓形儀表板
 * 圖形大小皆 int 轉換 DP
 * 文字大小皆 int 轉換 SP
 */


public class CircleDashboard extends View {
    private static final String TAG = "CircleDashboard";

    private Paint mPaint = new Paint();
    private RectF mRectF = new RectF();

    private static final int DEFAULT_VALUE = 100;
    private int viewWidth;
    private int viewHeight;

    private int radius;
    private int circleColor;
    private int percentColor;
    private static final int DEFAULT_PERCENT_WIDTH = 10;
    private int percentWidth;
    private int percent;

    private boolean animationMode;

    private static final int textSize = 16;
    private float textSizeResult;

    public CircleDashboard(Context context) {
        this(context, null);
    }

    public CircleDashboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleDashboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.CircleDashboard, defStyleAttr, 0);

        circleColor = mTypeArray.getColor(R.styleable.CircleDashboard_circleColor, getResources().getColor(R.color
                .indigo500));

        percentColor = mTypeArray.getColor(R.styleable.CircleDashboard_percentColor, getResources().getColor(R.color
                .indigo300));

        animationMode = mTypeArray.getBoolean(R.styleable.CircleDashboard_percentAnimation, false);
        mTypeArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int min = PxUtils.dpToPx(DEFAULT_VALUE, getContext());

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                if (width < min) {
                    viewWidth = min;
                } else {
                    viewWidth = width;
                }
                break;
            case MeasureSpec.AT_MOST:
                viewWidth = min;
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                if (height < min) {
                    viewHeight = min;
                } else {
                    viewHeight = height;
                }

                break;
            case MeasureSpec.AT_MOST:
                viewHeight = min;
                break;
        }

        radius = viewWidth > viewHeight ? viewHeight : viewWidth;
        textSizeResult = PxUtils.spToPx(textSize, getContext()) * radius / PxUtils.dpToPx(DEFAULT_VALUE, getContext());
        percentWidth = PxUtils.dpToPx(DEFAULT_PERCENT_WIDTH, getContext()) * radius / PxUtils.dpToPx(DEFAULT_VALUE,
                getContext());
        setMeasuredDimension(viewWidth, viewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setAntiAlias(true);
        mPaint.setColor(circleColor);
        mPaint.setStyle(Paint.Style.FILL);
        //outside circle
        canvas.drawCircle(radius / 2, radius / 2, radius / 2, mPaint);

        //percent
        mPaint.setColor(percentColor);
        mRectF.set(0, 0, radius, radius);
        float drawPercent = (float) (percent * 3.6);
        canvas.drawArc(mRectF, 270, drawPercent, true, mPaint);

        //inside circle
        mPaint.setColor(circleColor);
        canvas.drawCircle(radius / 2, radius / 2, radius / 2 - percentWidth, mPaint);

        //Text
        mPaint.setColor(percentColor);
        mPaint.setTextSize(textSizeResult);
        float textLength = mPaint.measureText(String.valueOf(percent));
        canvas.drawText(String.valueOf(percent), radius / 2 - textLength, radius / 2, mPaint);

        double subTextSize = textSizeResult * 0.6;
        mPaint.setTextSize((float) subTextSize);
        mPaint.setFakeBoldText(false);

        float subTextLength = mPaint.measureText("%");
        canvas.drawText("%", radius / 2 + subTextLength, radius / 2, mPaint);
    }


    public boolean getAnimationMode() {
        return animationMode;
    }

    public void setAnimationMode(Boolean b) {
        animationMode = b;
    }


    public void setPercent(int percent) {
        if (percent > 100) {
            Toast.makeText(getContext(), "Percent has error value", Toast.LENGTH_SHORT).show();
        } else {
            this.percent = percent;

            if (animationMode) {
                animationInvalidate(percent);
            } else {
                invalidate();
            }

        }
    }

    private void animationInvalidate(int p) {
        final int drawPercent = p;
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= drawPercent; i++) {
                    try {
                        Thread.sleep(15);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    percent = i;
                    postInvalidate();
                }
            }

        }).start();
    }
}
