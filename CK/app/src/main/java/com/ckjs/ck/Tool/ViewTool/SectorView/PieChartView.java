package com.ckjs.ck.Tool.ViewTool.SectorView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ckjs.ck.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class PieChartView extends View {
    private static final String TAG = "PieChartView";
    private static final int Gravity_TOP = 0;
    private static final int Gravity_CENTRE = 1;
    private static final int Gravity_FIXXY = 2;
    private RectF FanRectF;//主绘图区域
    private Paint p;//画笔
    private PointF centre;//圆心
    private PointF startPoint;//开始画扇形的开始点
    private float radius;//外圆的半径
    private float centreRadius;//内圆半径,不可点击
    private List<FanItem> datas;//扇形Item数据
    private double SO = -1;//左边到圆心距离
    private int Gravity = Gravity_TOP;//重力

    public PieChartView(Context context) {
        super(context);
        init(context, null);
    }

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PieChartView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        int lableTextSize = 0;
        if (null != context && null != attrs) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PieChartView);
            Gravity = ta.getInt(R.styleable.PieChartView_gravity_piecharview, Gravity_TOP);
            centreRadius = ta.getDimensionPixelSize(R.styleable.PieChartView_centreRadius, 0);
            lableTextSize = ta.getDimensionPixelSize(R.styleable.PieChartView_lableTextSize, 0);
            ta.recycle();
        }
        // 创建画笔
        p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(lableTextSize);
        p.setTextAlign(Paint.Align.CENTER);
    }

    /**
     * 设置扇形数据
     *
     * @param datas                   数据源
     * @param productColors           颜色
     * @param minClickAbleAreaPercent 最小百分比(如果比这百分比小就用这个的)
     */
    public void setFanClickAbleData(double[] datas, int[] productColors, double minClickAbleAreaPercent) {
        if (null == datas || datas.length > productColors.length) {
            return;
        }
        float totalAll = 0f;
        for (double value : datas) {
            totalAll += value;
        }
        double blankValue = totalAll * minClickAbleAreaPercent;
        List<FanItem> itemList = new ArrayList<>();
        for (int i = 0; i < datas.length; i++) {
            //修正点击范围
            Double value = datas[i];
            if (value > 0) {
                if (value < blankValue) {
                    value = blankValue;
                }
                FanItem item = new FanItem(i, Float.parseFloat(value.toString()), productColors[i]);
                item.setRealValue((float) datas[i]);
                item.setPercent((int) (100 * datas[i] / totalAll));
                itemList.add(item);
            }
        }
        if (itemList.size()>0) {
            setDatas(itemList);
            postInvalidate();
        }
    }

    public void setDatas(List<FanItem> datas) {
        this.datas = datas;
        initDatas(this.datas);
    }

    public List<FanItem> getDatas() {
        return datas;
    }

    /**
     * 初始化角度数据
     *
     * @param datas
     */
    private void initDatas(List<FanItem> datas) {
        if (null == datas) {
            return;
        }
        float total = 0;
        for (int i = 0; i < datas.size(); i++) {
            total += datas.get(i).getValue();
        }
        float startAngle = -1;
        for (int i = 0; i < datas.size(); i++) {
            FanItem f = datas.get(i);
            f.setIndex(i);
            f.setAngle(360 * (f.getValue() / total));
            //第一个开始
            if (-1 == startAngle) {
                startAngle = 90 - f.getAngle() / 2;
            }
            if (startAngle < 0) {
                startAngle = startAngle + 360;
            } else if (startAngle > 360) {
                startAngle = startAngle - 360;
            }
            f.setStartAngle(startAngle);
            startAngle = startAngle + f.getAngle();
        }
    }

    /**
     * 扇形的绘图信息
     */
    private void initFanDrawAbleInfo() {
        if (null == FanRectF) {
            switch (Gravity) {
                case Gravity_TOP: {
                    FanRectF = new RectF(0, 0, getWidth(), getWidth());
                    break;
                }
                case Gravity_CENTRE: {
                    int w = getWidth();
                    int h = getHeight();
                    if (w == h) {
                        FanRectF = new RectF(0, 0, w, w);
                    } else if (h > w) {
                        FanRectF = new RectF(0, (h - w) / 2, w, w);
                    } else if (w > h) {
                        FanRectF = new RectF((w - h) / 2, 0, h, h);
                    }
                    break;
                }
                case Gravity_FIXXY: {
                    FanRectF = new RectF(0, 0, getWidth(), getHeight());
                    break;
                }
            }

        }
        if (null == centre) {
            centre = new PointF(FanRectF.centerX(), FanRectF.centerY());
        }
        if (null == startPoint) {
            startPoint = new PointF(FanRectF.width(), FanRectF.height() / 2);
        }
        if (SO == -1) {
            SO = getDistance(startPoint, centre);
        }
        radius = FanRectF.width() / 2;
    }

    /**
     * 在扇形的对角线中间绘制文字
     *
     * @param canvas
     * @param item
     * @param txtCentre
     */
    private void drawText(Canvas canvas, RectF txtCentre, FanItem item) {
        float angle = (item.getStartAngle() + item.getAngle() + item.getStartAngle()) / 2;
        Log.d(TAG, "开始 :" + item.getStartAngle() + " 结束:" + (item.getStartAngle() + item.getAngle()) + " 中心:" + angle);
        String str = item.getPercent() + "%";
        Rect txtRect = new Rect();
        p.getTextBounds(str, 0, str.length(), txtRect);
        float cenX = (float) (txtCentre.centerX() + (radius - txtRect.height() - txtRect.width()) * Math.cos(Math.toRadians(angle)));
        float cexY = (float) (txtCentre.centerY() + (radius - txtRect.height() - txtRect.width()) * Math.sin(Math.toRadians(angle)));
        p.setColor(getResources().getColor(R.color.c_33));
        canvas.drawText(str, cenX, cexY, p);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == datas) {
            return;
        }
        initFanDrawAbleInfo();
        for (int i = 0; i < datas.size(); i++) {
            RectF drawRect;
            drawRect = FanRectF;
            FanItem item = datas.get(i);
            float value=item.getValue();
            if (value >= 1&&value<2) {
                p.setTextSize(getResources().getDimensionPixelSize(R.dimen.d4));
            } else if (value > 1 && value <3) {
                p.setTextSize(getResources().getDimensionPixelSize(R.dimen.d6));
            } else if (value >= 3 && value <4) {
                p.setTextSize(getResources().getDimensionPixelSize(R.dimen.d7));
            } else if (value >=4 && value <5) {
                p.setTextSize(getResources().getDimensionPixelSize(R.dimen.d8));
            } else if (value >=5 && value <21) {
                p.setTextSize(getResources().getDimensionPixelSize(R.dimen.d10dp));
            } else {
                p.setTextSize(getResources().getDimensionPixelSize(R.dimen.d12));
            }
            p.setColor(item.getColor());
            canvas.drawArc(drawRect, item.getStartAngle(), item.getAngle(), true, p);
            drawText(canvas, drawRect, item);
        }
    }


    /**
     * 两点之间的距离
     *
     * @param a
     * @param b
     * @return
     */
    private double getDistance(PointF a, PointF b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }


}
