package com.jrtou.myviewdemo.myviewdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.jrtou.myviewdemo.myviewdemo.R;
import com.jrtou.myviewdemo.myviewdemo.Tool.PxUtils;

/**
 * Created by Jrtou on 2016/12/25.
 * 模擬汽車儀錶板
 * 所有長度計算一律使用DP轉換PX
 */

public class DashboardView extends View {
    private static final String TAG = "DashboardView";
    private int mPercent;                                           //設置百分比
    private float percent = 0f;                                     //目前百分比
    private float mFillPercent;                                     //
    private Boolean mMode;                                          //指針或是數值顯示
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


    private String text = ""; //文字内容
    private int textSize;//文字的大小
    private int textColor;//设置文字颜色
    private int arcColor; //小圆和指针颜色


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

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DashboardView, defStyleAttr, 0);
        outsideColor = a.getColor(R.styleable.DashboardView_outSideColor, Color.BLACK);
        outsideStrokeWidth = a.getDimensionPixelSize(R.styleable.DashboardView_outSideStokeWidth, PxUtils.dpToPx
                (2, context));
        progressColor = a.getColor(R.styleable.DashboardView_progressColor, Color.BLUE);
        mTickMarkWidth = a.getDimensionPixelSize(R.styleable.DashboardView_tickMarkWidth, PxUtils.dpToPx(3, context));
        mTickMarkColor = a.getColor(R.styleable.DashboardView_tickMarkColor, Color.BLACK);
        mTickMarksCount = a.getInteger(R.styleable.DashboardView_tickMarkCount, 12);

        mPercent = a.getInteger(R.styleable.DashboardView_percent, 0);
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        outsideWidth = PxUtils.dpToPx(DEFAULT_OUTSIDE_WIDTH, getContext());
        outsideHeight = PxUtils.dpToPx(DEFAULT_OUTSIDE_HEIGHT, getContext());

        int measureMode = MeasureSpec.getMode(widthMeasureSpec);

        if (measureMode == MeasureSpec.EXACTLY) {
            outsideWidth = MeasureSpec.getSize(widthMeasureSpec);
        } else if (measureMode == MeasureSpec.AT_MOST) {
            outsideWidth = Math.min(outsideWidth, MeasureSpec.getSize(widthMeasureSpec));
        }

        measureMode = MeasureSpec.getMode(heightMeasureSpec);

        if (measureMode == MeasureSpec.EXACTLY) {
            outsideHeight = MeasureSpec.getSize(heightMeasureSpec);
        } else if (measureMode == MeasureSpec.AT_MOST) {
            outsideHeight = Math.min(outsideHeight, MeasureSpec.getSize(heightMeasureSpec));
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


        float secondRectWidth = outsideWidth - outsideStrokeWidth - 50 - (outsideStrokeWidth + 50);
        float secondRectHeight = outsideHeight - outsideStrokeWidth - 50 - (outsideStrokeWidth + 50);

        //画粗弧突出部分左端
//        canvas.drawArc(progressRectF, 115, 11, false, mPaint);
//        canvas.drawArc(progressRectF, 144 + mFillPercent + empty, 10, false, mPaint);
/*
        mPaint.setColor(Color.BLUE);
        //绘制小圆外圈
        mPaint.setStrokeWidth(3);
        canvas.drawCircle(outsideLineWidth / 2, outsideHeight / 2, 30, mPaint);


        //绘制小圆内圈
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(8);
        int mMinCircleRadius = 15;
        canvas.drawCircle(outsideLineWidth / 2, outsideHeight / 2, mMinCircleRadius, mPaint);

        //绘制指针

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(4);


        //按照百分比绘制刻度
        canvas.rotate((250 * percent - 250 / 2), outsideLineWidth / 2, outsideHeight / 2);

        canvas.drawLine(outsideLineWidth / 2, (outsideHeight / 2 - secondRectHeight / 2) + 50 / 2 + 2,
                outsideLineWidth / 2,
                outsideHeight / 2 - mMinCircleRadius, mPaint);

        //将画布旋转回来
        canvas.rotate(-(250 * percent - 250 / 2), outsideLineWidth / 2, outsideHeight / 2);

        //绘制矩形
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.YELLOW);
        int mRectWidth = 60;
        int mRectHeight = 25;

        //文字矩形的最底部坐标
        float rectBottomY = outsideHeight / 2 + secondRectHeight / 3 + mRectHeight;
        canvas.drawRect(outsideLineWidth / 2 - mRectWidth / 2, outsideHeight / 2 + secondRectHeight / 3,
                outsideLineWidth /
                        2 + mRectWidth / 2,
                rectBottomY, mPaint);


        mPaint.setTextSize(24);
        mPaint.setColor(Color.WHITE);
        float txtLength = mPaint.measureText("Test");
        canvas.drawText("Test", (outsideLineWidth - txtLength) / 2, rectBottomY + 40, mPaint);
**/
    }

    public void setPercent(int percent) {
        mPercent = percent;
        invalidate();
    }
}


