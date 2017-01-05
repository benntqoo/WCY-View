package com.jrtou.myviewdemo.wcylibrary.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jrtou.myviewdemo.wcylibrary.R;
import com.jrtou.myviewdemo.wcylibrary.Tool.PxUtils;

/**
 * Created by Jrtou on 2017/1/5.
 * 模擬汽車儀錶板
 * 所有長度計算一律使用DP轉換PX
 */

public class DashboardView extends View {
    private static final String TAG = "DashboardView";
    private int mPercent;                                           //設置百分比
    private float percent = 0f;                                     //目前百分比
    private float mFillPercent;                                     //
    private Boolean mMode;                                          //true:指針 false:數值顯示
    private Boolean animation;                                      //動畫增長
    private static Paint mPaint = new Paint();
    //刻度線
    private int mTickMarksCount = 12;                               //數量
    private int mTickMarkWidth;                                     //線寬度
    private static final int mTickMarkLength = 35;                  //長度
    private int mTickMarkX;                                         //X座標
    private int mTickMArkY;                                         //Y座標
    private int mTickMarkColor;                                     //顏色

    // 外弧線 outside
    private static final int DEFAULT_OUTSIDE_WIDTH = 200;           //View default width
    private static final int DEFAULT_OUTSIDE_HEIGHT = 200;          //View default height
    private int outsideWidth;                                       //Width
    private int outsideHeight;                                      //Height
    private RectF outsideRectF = new RectF();                       //外弧線
    private int outsideStrokeWidth;                                 //線寬度
    private int outsideColor;                                       //顏色

    //progress rate
    private RectF progressRectF = new RectF();
    private int progressColor;
    private static final int PROGRESS_STROKE_WIDTH = 15;

    public DashboardView(Context context) {
        this(context, null);
    }

    public DashboardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 繼承 constructor
     * 在裡面獲取自定義 attrs 屬性與設定 Default value
     *
     * @param context      當前環境
     * @param attrs        attrs
     * @param defStyleAttr ??
     */
    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray mTypeArray = context.obtainStyledAttributes(attrs, R.styleable.DashboardView, defStyleAttr, 0);
        outsideColor = mTypeArray.getColor(R.styleable.DashboardView_outSideColor, Color.BLACK);
        outsideStrokeWidth = mTypeArray.getDimensionPixelSize(R.styleable.DashboardView_outSideStokeWidth, PxUtils
                .dpToPx
                        (2, context));
        progressColor = mTypeArray.getColor(R.styleable.DashboardView_progressColor, Color.BLUE);
        mTickMarkWidth = mTypeArray.getDimensionPixelSize(R.styleable.DashboardView_tickMarkWidth, PxUtils.dpToPx(3,
                context));
        mTickMarkColor = mTypeArray.getColor(R.styleable.DashboardView_tickMarkColor, Color.BLACK);
        mTickMarksCount = mTypeArray.getInteger(R.styleable.DashboardView_tickMarkCount, 12);

        mPercent = mTypeArray.getInteger(R.styleable.DashboardView_percent, 0);

        mMode = mTypeArray.getBoolean(R.styleable.DashboardView_mode, true);
        animation = mTypeArray.getBoolean(R.styleable.DashboardView_animation, false);
        mTypeArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        outsideWidth = PxUtils.dpToPx(DEFAULT_OUTSIDE_WIDTH, getContext());
        outsideHeight = PxUtils.dpToPx(DEFAULT_OUTSIDE_HEIGHT, getContext());

        int measureMode = View.MeasureSpec.getMode(widthMeasureSpec);

        if (measureMode == View.MeasureSpec.EXACTLY) {
            outsideWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        } else if (measureMode == View.MeasureSpec.AT_MOST) {
            outsideWidth = Math.min(outsideWidth, View.MeasureSpec.getSize(widthMeasureSpec));
        }

        measureMode = View.MeasureSpec.getMode(heightMeasureSpec);

        if (measureMode == View.MeasureSpec.EXACTLY) {
            outsideHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        } else if (measureMode == View.MeasureSpec.AT_MOST) {
            outsideHeight = Math.min(outsideHeight, View.MeasureSpec.getSize(heightMeasureSpec));
        }

        mTickMarkX = outsideWidth / 2;
        mTickMArkY = outsideStrokeWidth - PxUtils.dpToPx(1, getContext());
        setMeasuredDimension(outsideWidth, outsideHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //outside paint setting
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(outsideStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(outsideColor);

        outsideRectF.set(outsideStrokeWidth, outsideStrokeWidth, outsideWidth -
                        outsideStrokeWidth,
                outsideHeight - outsideStrokeWidth);

        canvas.drawArc(outsideRectF, 135, 270, false, mPaint);


        //中間刻度線
        mPaint.setStrokeWidth(mTickMarkWidth);
        mPaint.setColor(mTickMarkColor);

        canvas.drawLine(mTickMarkX, mTickMArkY, mTickMarkX, mTickMarkLength, mPaint);
        //轉動畫線
        float rAngle = 270f / mTickMarksCount;
        //右邊刻度線
        for (int i = 0; i < mTickMarksCount / 2; i++) {
            canvas.rotate(rAngle, mTickMarkX, outsideHeight / 2);
            canvas.drawLine(mTickMarkX, mTickMArkY, mTickMarkX, mTickMarkLength, mPaint);
        }
        //還原畫布
        canvas.rotate(-rAngle * mTickMarksCount / 2, mTickMarkX, outsideHeight / 2);

        //左邊刻度線
        for (int i = 0; i < mTickMarksCount / 2; i++) {
            canvas.rotate(-rAngle, mTickMarkX, outsideHeight / 2);
            canvas.drawLine(mTickMarkX, mTickMArkY, mTickMarkX, mTickMarkLength, mPaint);
        }
        //還原畫布
        canvas.rotate(rAngle * mTickMarksCount / 2, outsideWidth / 2, outsideHeight / 2);


        //進度條
        progressRectF.set(outsideStrokeWidth + 75, outsideStrokeWidth + 75, outsideWidth -
                outsideStrokeWidth - 75, outsideHeight - outsideStrokeWidth - 75);
        mPaint.setStrokeWidth(PxUtils.dpToPx(PROGRESS_STROKE_WIDTH, getContext()));//進度條粗度

        percent = mPercent / 100f;
        mFillPercent = 270 * percent;
        float empty = 270 - mFillPercent;
        //起始框架
        mPaint.setColor(Color.YELLOW);
        canvas.drawArc(progressRectF, 125, 10, false, mPaint);
        //中間框架
        mPaint.setColor(progressColor);
        canvas.drawArc(progressRectF, 135, mFillPercent, false, mPaint);
        mPaint.setColor(Color.WHITE);
        canvas.drawArc(progressRectF, 135 + mFillPercent, empty, false, mPaint);
        //結束框架
        mPaint.setColor(Color.RED);
        canvas.drawArc(progressRectF, 45, 10, false, mPaint);


        if (mMode) {
            //內圓
            mPaint.setColor(Color.BLACK);
            mPaint.setStrokeWidth(3);
            canvas.drawCircle(outsideWidth / 2, outsideHeight / 2, 20, mPaint);//內大圓
            mPaint.setStrokeWidth(5);
            canvas.drawCircle(outsideWidth / 2, outsideHeight / 2, 10, mPaint);//內小圓

            //指針
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(5);
            //根據 percent 繪製指針
            canvas.rotate((270 * percent - 270 / 2), outsideWidth / 2, outsideHeight / 2);

            canvas.drawLine(outsideWidth / 2, (outsideHeight / 2 - outsideHeight / 2) + 50 / 2 + 2,
                    outsideWidth / 2,
                    outsideHeight / 2 - 10, mPaint);

            //還原畫布
            canvas.rotate(-(270 * percent - 270 / 2), outsideWidth / 2, outsideHeight / 2);
        } else {
            mPaint.setTextSize(PxUtils.spToPx(36, getContext()));
            mPaint.setColor(Color.BLUE);
            mPaint.setStyle(Paint.Style.FILL);


            int mNumber = (int) (percent * 100);
            float textLength = mPaint.measureText(String.valueOf(mNumber));
            float percentLength = mPaint.measureText("%");
            canvas.drawText(String.valueOf(mNumber), (outsideWidth - textLength - percentLength) / 2, outsideHeight /
                    2, mPaint);

            mPaint.setTextSize(PxUtils.spToPx(16, getContext()));
            canvas.drawText("%", (outsideWidth + textLength) / 2, outsideHeight / 2, mPaint);
        }
    }

    public void setPercent(int percent) {
        mPercent = percent;
        invalidate();
    }

    public void setMode(Boolean b) {
        mMode = b;
        invalidate();
    }
}

