package com.ckjs.ck.Tool.ViewTool;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import java.util.Random;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class PathView extends CardiographView {
    public PathView(Context context) {
        this(context, null);
    }

    public PathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPath = new Path();
    }

    public boolean isaBoolean() {
        return aBoolean;
    }

    public void setaBoolean(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    private boolean aBoolean;

    public synchronized void setMaxValue(int max) {
        mSpeed = 14.6f;
        mPath.reset();
        mLastX = mWidth;
        if (mLastX == mWidth) {
            mPath.moveTo(mLastX, mHeight / 2);
        }
        maxValue = max;
        mScale = -(mYOffset * (1.0f / maxValue));
    }

    public float getValue() {
        return value;
    }

    public synchronized void setValue(float value) {
        this.value = value;
        aBoolean = true;
        aBooleanOne = false;
        invalidate();//自动清屏 屏幕刷新

    }

    private float value;//真实
    private float mSpeed = 14.6f;  //更改顯示速度(寬窄)，數字越小顯示越密;最小設1.0f。

    private boolean aBooleanOne;

    private synchronized void drawPath(Canvas canvas) {
        if (aBoolean) {
            float newX = mLastX - mSpeed;
            final float v = mYOffset + my_value * mScale;
            //            Log.i(AppConfig.TAG, newX + "," + mLastX);
            if (newX <= 0) {
                if (!aBooleanOne) {
                    aBooleanOne = true;
                    mPath.offset(mSpeed, 0);
                    if (mSpeed == 14.6f) {
                        mSpeed = mSpeed / 2;
                        newX = mLastX - mSpeed;
                    }
                }
            }
            //用path模拟一个心电图样式
            mPath.lineTo(newX, v);
            //设置画笔style
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(mLineColor);
            mPaint.setStrokeWidth(2);
            mLastX -= mSpeed;
        }
        canvas.drawPath(mPath, mPaint);
    }

    float nPoint = 40;

    @Override
    protected void onDraw(Canvas canvas) {
        if (maxValue != 1024f) {
            synchronized (this) {
                initChangeValue();
                drawPath(canvas);
            }
        }
    }

    private float my_value;//画图的
    boolean aBooleanValue;

    //    int randNumber = rand.nextInt(MAX - MIN + 1) + MIN; // randNumber 将被赋值为一个 MIN 和 MAX 范围内的随机数
    private synchronized void initChangeValue() {
        my_value = value;
        //        if (my_value_befor != value) {
        //            nPoint = 40;
        //        }
        if (nPoint == 40) {
            nPoint = 0;
        } else if (nPoint == 0) {
            nPoint = 2;
        } else if (nPoint == 2) {
            nPoint = 5;
        } else if (nPoint == 5) {
            nPoint = 1;
        } else if (nPoint == 1) {
            nPoint = 6;
        } else if (nPoint == 6) {
            nPoint = 15;
        } else if (nPoint == 15) {
            nPoint = -20;
        } else if (nPoint == -20) {
            nPoint = 12;
        } else if (nPoint == 12) {
            nPoint = 7;
        } else if (nPoint == 7) {
            nPoint = 3;
        } else if (nPoint == 3) {
            nPoint = 8;
        } else if (nPoint == 8) {
            nPoint = 4;
        } else if (nPoint == 4) {
            nPoint = 9;
        } else if (nPoint == 9) {
            nPoint = 14;
        } else if (nPoint == 14) {
            nPoint = 40;
        }
        if (value > 160) {
            my_value = 160;
        }
        if (nPoint == 40) {
            if (!aBooleanValue) {
                aBooleanValue = true;
                Random rand = new Random();
                my_value = (float) (my_value + 25 + (int) (rand.nextInt(6) + 10));
            } else {
                Random rand = new Random();
                aBooleanValue = false;
                my_value = (float) (my_value + 25 + (int) (rand.nextInt(6) + 25));
            }
        } else if (nPoint == 14 | nPoint == 15 || nPoint == 12) {
            my_value = my_value + 0;
        } else if (nPoint == -20) {
            my_value = my_value + nPoint;
        } else {
            Random rand = new Random();
            my_value = my_value + nPoint + (int) (rand.nextInt(3) + 1);
        }
        if (my_value > 160) {
            my_value = 160;
        }
        if (my_value < 0) {
            my_value = 0;
        }
    }
}
