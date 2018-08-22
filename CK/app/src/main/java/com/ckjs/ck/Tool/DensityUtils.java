package com.ckjs.ck.Tool;

import android.content.Context;
import android.util.TypedValue;

import com.ckjs.ck.Application.CkApplication;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
/**
 *
 * dp,sp,px，间数据互相转换
 */
public class DensityUtils {
    private DensityUtils() {
/** cannot be instantiated **/
        throw new UnsupportedOperationException("cannot be instantiated");
    }


    /**
     * 数据转换 dp -> px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }


    /**
     * 数据转换 sp -> px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }


    /**
     * 数据转换 px -> dp
     *
     * @param pxVal
     * @return
     */
    public static float px2dp(float pxVal) {
        final float scale = CkApplication.getInstance().getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    public static float px2dp(Context context, int pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }


    /**
     * 数据转换 px -> sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }


}