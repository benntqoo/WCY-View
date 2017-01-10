package com.jrtou.myviewdemo.wcylibrary.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.jrtou.myviewdemo.wcylibrary.R;
import com.jrtou.myviewdemo.wcylibrary.Tool.PxUtils;

/**
 * Created by Administrator on 2017/1/10.
 */

public class WaterWaveDashBoard extends View {
    private static final String TAG = "WaterWaveDashBoard";

    private Paint mBackgroundPaint;
    private Paint mPaint;
    private Paint mTextPaint;
    private PorterDuffXfermode mMode;

    private Bitmap mBitmap;
    private Canvas mCanvas;

    private int percent;

    private static final int DEFAULT_RADIUS = 100;

    private int width;
    private int height;
    private int radius;

    private int x;
    private int y;
    private boolean isLeft;

    private Path mPath;

    public WaterWaveDashBoard(Context context) {
        this(context, null);
    }

    public WaterWaveDashBoard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaterWaveDashBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // Paint
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setAntiAlias(true);
        mBackgroundPaint.setColor(getResources().getColor(R.color.deepPurple300));

        // main paint
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.deepPurpleA200));

        // Text Paint
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.RED);
        mTextPaint.setTextSize(PxUtils.spToPx(24, context));


        // Bitmap 底圖
        mBitmap = Bitmap.createBitmap(PxUtils.dpToPx(500, context), PxUtils.dpToPx(500, context), Bitmap.Config
                .ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        //Path
        mPath = new Path();

        //Mode
        mMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.wtf(TAG, "onMeasure");
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int mRadius = PxUtils.dpToPx(DEFAULT_RADIUS, getContext());

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;

        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = mRadius * 2;
        }


        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = mRadius * 2;
        }

        y = height;

        radius = width > height ? height / 2 : width / 2;
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (x > 50) {
            isLeft = true;
        } else if (x < 0) {
            isLeft = false;
        }

        if (isLeft) {
            x = x - PxUtils.dpToPx(1, getContext());
        } else {
            x = x + PxUtils.dpToPx(1, getContext());
        }

        //Path
        mPath.reset();
        y = (int) ((1 - percent / 100f) * height);
        mPath.moveTo(0, y);                         //起始點
        mPath.cubicTo(PxUtils.dpToPx(100, getContext()) + x * 2, PxUtils.dpToPx(50, getContext()) + y
                , PxUtils.dpToPx(100, getContext()) + x * 2, y - PxUtils.dpToPx(50, getContext())
                , width, y);
        mPath.lineTo(width, height);
        mPath.lineTo(0, height);
        mPath.close();

        mBitmap.eraseColor(Color.parseColor("#00000000"));
        //底層框架
        mCanvas.drawCircle(width / 2, height / 2, radius, mBackgroundPaint);
        mPaint.setXfermode(mMode);
        //src
        mCanvas.drawPath(mPath, mPaint);
        mPaint.setXfermode(null);


        canvas.drawBitmap(mBitmap, 0, 0, null);

        float textSize = mTextPaint.measureText(String.valueOf(percent));
        canvas.drawText(String.valueOf(percent) + "%", width / 2 - textSize, height / 2, mTextPaint);


        postInvalidateDelayed(10);//重複繪圖

    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
