package com.ckjs.ck.Tool.RememberStep;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Service.StepService;
import com.ckjs.ck.Tool.LogUtils;
import com.ckjs.ck.Tool.SPUtils;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class StepDcretor implements SensorEventListener {

    //是否上升的标志位
    boolean isDirectionUp = false;
    //持续上升次数
    int continueUpCount = 0;

    //上一点的状态，上升还是下降
    boolean lastStatus = false;
    //波峰值
    float peakOfWave = 0;
    //波谷值
    float valleyOfWave = 0;
    //此次波峰的时间
    long timeOfThisPeak = 0;
    //上次波峰的时间
    long timeOfLastPeak = 0;

    //上次传感器的值
    float gravityOld = 0;


    //初始范围
    //    float minValue = 11f;
    float minValue = 12f;


    /**
     * 0-准备计时   1-计时中   2-正常计步中
     */
    public static int CURRENT_SETP = 0;
    //用x、y、z轴三个维度算出的平均值
    public static float average = 0;

    public static boolean flagStep;//是否连接手环，true为连接
    public static String SportMode = "步行";

    public StepDcretor() {
        super();
    }


    public synchronized void calc_step(float x, float y, float z, String name) {
        if (!stepBoolean) {

            average = (float) Math.sqrt(Math.pow(x, 2)
                    + Math.pow(y, 2) + Math.pow(z, 2));
            detectorNewStep(average, name);
        }
    }


    /*
     * 检测步子，并开始计步
	 * 1.传入sersor中的数据
	 * 2.如果检测到了波峰，并且符合时间差以及阈值的条件，则判定为1步
	 * 3.符合时间差条件，波峰波谷差值大于initialValue，则将该差值纳入阈值的计算中
	 * */
    public synchronized void detectorNewStep(float values, String name) {
        if (gravityOld == 0) {
            gravityOld = values;
        } else {
            DetectorPeak(values, gravityOld, name);
        }
        gravityOld = values;
    }


    /*
     * 检测波峰
     * 以下四个条件判断为波峰：
     * 1.目前点为下降的趋势：isDirectionUp为false
     * 2.之前的点为上升的趋势：lastStatus为true
     * 3.到波峰为止，持续上升大于等于2次
     * 4.波峰值大于1.2g,小于2g
     * 记录波谷值
     * 1.观察波形图，可以发现在出现步子的地方，波谷的下一个就是波峰，有比较明显的特征以及差值
     * 2.所以要记录每次的波谷值，为了和下次的波峰做对比
     * */
    int continueDownCount;
    float Maybepeak, Maybevalue;
    long Maybetime;
    boolean jibuFlag;

    public synchronized void DetectorPeak(float newValue, float oldValue, String name) {
        lastStatus = isDirectionUp;
        if (newValue >= oldValue) {
            continueUpCount++;
            if (continueUpCount == 1) {
                Maybevalue = oldValue;
            }
            if (continueUpCount >= 2) {
                valleyOfWave = Maybevalue;
                isDirectionUp = true;
                continueDownCount = 0;
                jibuFlag = true;
                //Log.i("波谷", valleyOfWave + "");
            }

        } else {
            continueDownCount++;
            if (continueDownCount == 1) {
                Maybepeak = oldValue;
                Maybetime = System.currentTimeMillis();
            }
            if (continueDownCount >= 2) {
                peakOfWave = Maybepeak;
                isDirectionUp = false;
                continueUpCount = 0;
                timeOfLastPeak = timeOfThisPeak;
                timeOfThisPeak = Maybetime;
                //Log.i("波峰", peakOfWave + "");

                if (jibuFlag && timeOfThisPeak - timeOfLastPeak >= 200 && peakOfWave > minValue) {
                    jibuFlag = false;
                    CURRENT_SETP++;
                }
            }

        }
        if (peakOfWave >= 19.5 || peakOfWave - valleyOfWave > 18) {
            SportMode = "跑步";
        } else {
            SportMode = "步行";
        }

    }

    public static int step;
    public static boolean stepBoolean;//只有登录能计步

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this) {
            Sensor sensor = event.sensor;
            if (!stepBoolean) {
                if (!flagStep) {
                    //1：已绑定；0：未绑定
                    String nameStep = "0";
                    nameStep = (String) SPUtils.get(CkApplication.getInstance(), "nameStepType", nameStep);
                    if (nameStep.equals("0")) {
                        if (StepService.countSensor == null) {
                            LogUtils.d("你的手机硬件不支持后台计步");
                            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                                calc_step(event.values[0], event.values[1], event.values[2], "minValuePhoe");
                            }
                        } else {
                            if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                                step = (int) event.values[0];
                                //                                LogUtils.d("step:" + step);
                                StepService.initGetBeforStep();
                            }
                        }
                        if (StepService.sensor == null) {
                            LogUtils.d("你的手机硬件不支持计步");
                        }
                    }
                }
            }
        }

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
