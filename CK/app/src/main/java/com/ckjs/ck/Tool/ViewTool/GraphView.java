package com.ckjs.ck.Tool.ViewTool;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ckjs.ck.Tool.AppConfig;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */

public class GraphView extends View {
    /**
     *
     */
    //折现的颜色
    protected int mLineColor = Color.parseColor("#ffffff");
    //网格颜色
    protected int mGridColor = Color.parseColor("#50f98090");

    //小网格颜色
    protected int mSGridColor = Color.parseColor("#50999999");
    //背景颜色
    protected int mBackgroundColor = Color.parseColor("#fca29d");
    //自身的大小
    protected int mHeight;
    //网格宽度
    protected int mGridWidth = 50;
    //小网格的宽度
    protected int mSGridWidth = 10;

    private Bitmap mBitmap;
    private Paint mPaint = new Paint();
    private Canvas mCanvas = new Canvas();

    private float mSpeed = 14.6f;  //更改顯示速度(寬窄)，數字越小顯示越密;最小設1.0f。
    private float mLastX;
    private float mScale;
    private float mLastValue = 112.0f;
    private float mYOffset;
    private int mColor;
    private int mWidth;
    private float maxValue = 1024f;

    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mColor = mLineColor; //定義顏色ARGB
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    public void addDataPoint(float value) {
        final Paint paint = mPaint;
        float newX = mLastX + mSpeed;
        final float v = mYOffset + value * mScale;

        paint.setStrokeWidth(2);//画笔粗细
        paint.setColor(mColor);
        mCanvas.drawLine(mLastX, mLastValue, newX, v, paint);
        mLastValue = v;
        mLastX += mSpeed;
        mScale = -(mYOffset * (1.0f / maxValue));

        invalidate();//自动清屏 屏幕刷新
    }

    public void setMaxValue(int max) {
        maxValue = max;
        mScale = -(mYOffset * (1.0f / maxValue));
    }

    //绘制背景
    private void initBackground(Canvas canvas) {

        canvas.drawColor(mBackgroundColor);
        //画小网格

        //竖线个数
        int vSNum = mWidth / mSGridWidth;

        //横线个数
        int hSNum = mHeight / mSGridWidth;
        mPaint.setColor(mSGridColor);
        mPaint.setStrokeWidth(2);
        //画竖线
        for (int i = 0; i < vSNum + 1; i++) {
            canvas.drawLine(i * mSGridWidth, 0, i * mSGridWidth, mHeight, mPaint);
        }
        //画横线
        for (int i = 0; i < hSNum + 1; i++) {

            canvas.drawLine(0, i * mSGridWidth, mWidth, i * mSGridWidth, mPaint);
        }

        //竖线个数
        int vNum = mWidth / mGridWidth;
        //横线个数
        int hNum = mHeight / mGridWidth;
        mPaint.setColor(mGridColor);
        mPaint.setStrokeWidth(2);
        //画竖线
        for (int i = 0; i < vNum + 1; i++) {
            canvas.drawLine(i * mGridWidth, 0, i * mGridWidth, mHeight, mPaint);
        }
        //画横线
        for (int i = 0; i < hNum + 1; i++) {
            canvas.drawLine(0, i * mGridWidth, mWidth, i * mGridWidth, mPaint);
        }


    }

    public void setSpeed(float speed) {
        mSpeed = speed;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        mCanvas.setBitmap(mBitmap);
        mYOffset = h;
        mScale = -(mYOffset * (1.0f / maxValue));
        mWidth = w;
        mHeight = h;
        mLastX = mWidth;

        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        synchronized (this) {
            if (mBitmap != null) {
                if (mLastX >= mWidth) {
                    mLastX = 0;
                    final Canvas cavas = mCanvas;

                    mPaint.setColor(mGridColor);
                    cavas.drawLine(0, mYOffset, mWidth, mYOffset, mPaint);
                    initBackground(cavas);
                }
                canvas.drawBitmap(mBitmap, 0, 0, null);
            }
        }

    }

}
