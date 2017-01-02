package com.jrtou.myviewdemo.myviewdemo.Tool;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by Jrtou on 2016/12/30.
 */

public class PxUtils {
    public static int dpToPx(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources()
                .getDisplayMetrics());
    }

    public static int spToPx(int sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources()
                .getDisplayMetrics());
    }
}
