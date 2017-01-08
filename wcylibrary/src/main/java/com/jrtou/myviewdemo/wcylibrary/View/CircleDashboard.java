package com.jrtou.myviewdemo.wcylibrary.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

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
    private int percentWidth;
    private static final int a = 12;
    private int b;
    private int percent;

    private static final int textSize = 16;
    private float textSizeReault;

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

        percentWidth = mTypeArray.getDimensionPixelSize(R.styleable.CircleDashboard_percentWidth, PxUtils.dpToPx(12,
                context));
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
        textSizeReault = PxUtils.spToPx(textSize, getContext()) * radius / PxUtils.dpToPx(DEFAULT_VALUE, getContext());
        b = PxUtils.dpToPx(a, getContext()) * radius / PxUtils.dpToPx(DEFAULT_VALUE, getContext());
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
        canvas.drawArc(mRectF, 270, 50, true, mPaint);

        //inside circle
        mPaint.setColor(circleColor);
        canvas.drawCircle(radius / 2, radius / 2, radius / 2 - b, mPaint);
        //TODO 文字等比例放大未完成
        //Text
        mPaint.setColor(percentColor);
        mPaint.setTextSize(textSizeReault);
        float textLength = mPaint.measureText("2");
        canvas.drawText("20", radius / 2-textLength, radius / 2, mPaint);

        mPaint.setTextSize(textSizeReault - PxUtils.spToPx(20,getContext()));
        mPaint.setFakeBoldText(true);

        canvas.drawText("%", radius / 2  +textLength, radius / 2, mPaint);
    }

    private void setPercent(int percent) {
        this.percent = percent;
        invalidate();
    }
}
