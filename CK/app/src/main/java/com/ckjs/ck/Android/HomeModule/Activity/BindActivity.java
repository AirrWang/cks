package com.ckjs.ck.Android.HomeModule.Activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ckjs.ck.Android.HealthModule.Adapter.LeDeviceListAdapter;
import com.ckjs.ck.Android.MineModule.Activity.ShareHSHActivity;
import com.ckjs.ck.Api.NpApi;
import com.ckjs.ck.Application.CkApplication;
import com.ckjs.ck.Bean.BandBean;
import com.ckjs.ck.Bean.BandstatusBean;
import com.ckjs.ck.R;
import com.ckjs.ck.Tool.AppConfig;
import com.ckjs.ck.Tool.BluethUtils;
import com.ckjs.ck.Tool.Kyloading.KyLoadingBuilder;
import com.ckjs.ck.Tool.MoudleUtils;
import com.ckjs.ck.Tool.RetrofitUtils;
import com.ckjs.ck.Tool.SPUtils;
import com.ckjs.ck.Tool.SavaDataLocalUtils;
import com.ckjs.ck.Tool.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
public class BindActivity extends AppCompatActivity {
    @BindView(R.id.lv_bind)
    ListView lv_bind;
    @BindView(R.id.ll_sou_suo)
    RelativeLayout ll_sou_suo;
    @BindView(R.id.tv_show)
    TextView tv_show;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.btn_sure)
    Button btn_sure;
    @BindView(R.id.tv_xq)
    TextView tv_more;
    @BindView(R.id.textview_83)
    TextView textview_83;


    // stop scan after 10 second
    private static final long SCAN_PERIOD = 1000;
    private String tag = "bind";
    private Handler mHandler;
    private Toolbar toolbar;
    private TextView textView;
    private boolean flag = false;
    private KyLoadingBuilder kyLoadingBuilder;
    private ProgressDialog dialig;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind);
        ButterKnife.bind(this);
        MoudleUtils.viewShow(ll_sou_suo);
        mHandler = new Handler();
        initToolbar();
        textview_83.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        textview_83.getPaint().setAntiAlias(true);//抗锯齿
        textview_83.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * type0：默认；1：共享手环；
                 */
                Intent intent = new Intent(BindActivity.this, PoiSearchListJsfActivity.class).putExtra("type", 1);
                startActivity(intent);
            }
        });
        dialig = new ProgressDialog(this);
        btn_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoudleUtils.textViewSetText(tv_show, "搜索设备中...");
                btn_sure.setEnabled(false);
                scanLeDevice(true);
            }
        });
        tv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("acurl", AppConfig.h5_shjs);
                intent.setClass(BindActivity.this, AcH5Activity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 获取当前所要绑定的手环的绑定状态
     *
     * @param context
     * @param band
     * @param p
     */
    private void toBindFistApi(final Context context, final String band, final int p) {
        int userid = (int) SPUtils.get(context, "user_id", 0);
        final String token = (String) SPUtils.get(context, "token", "");
        Call<BandstatusBean> bandstatusBean = RetrofitUtils.retrofit.create(NpApi.class).bandstatus(token, userid, band);
        bandstatusBean.enqueue(new Callback<BandstatusBean>() {
            @Override
            public void onResponse(Call<BandstatusBean> call, Response<BandstatusBean> response) {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                BandstatusBean bandstatusBean = response.body();
                if (bandstatusBean == null) {

                    return;
                }
                //status|状态|0：未绑定；1：已自己账号绑定；2：已与其他账号绑定；
                /**
                 *  type	 手环类型	1：正常；2：租凭
                 pay	     支付状态	0：未支付；1：已支付
                 ordernum 订单号	已支付则为空
                 */
                String statusOne = bandstatusBean.getStatus();
                String msg = bandstatusBean.getMsg();
                if (statusOne == null)
                    return;
                if (statusOne.equals("2") || statusOne.equals("0")) {
                    ToastUtils.showShort(BindActivity.this, bandstatusBean.getMsg());
                    return;
                }
                if (statusOne.equals("1")) {
                    String status = bandstatusBean.getInfo().getStatus();
                    String type = bandstatusBean.getInfo().getType();
                    String pay = bandstatusBean.getInfo().getPay();
                    String ordernum = bandstatusBean.getInfo().getOrdernum();

                    if (status.equals("2")) {
                        ToastUtils.showShort(BindActivity.this, msg);
                    } else if (status.equals("0")) {
                        if (type.equals("1")) {
                            initBind(band);
                            MoudleUtils.dialogShow(dialig);
                            initToBindTask(context, band);
                        } else if (type.equals("2")) {
                            if (pay.equals("1")) {
                                initBind(band);
                                MoudleUtils.dialogShow(dialig);
                                initToBindTask(context, band);
                            } else if (pay.equals("0")) {
                                initToYaJin(ordernum, band);
                            }
                        }
                    } else if (status.equals("1") && type.equals("2") && pay.equals("1")) {//121已绑定已支付
                        initBind(band);
                        initToBind(band);
                    } else if (status.equals("1") && type.equals("2") && pay.equals("0")) {//120已绑定未支付
                        /**
                         * 跳转到支付押金页面
                         */
                        initToYaJin(ordernum, band);
                    } else if (status.equals("1") && type.equals("1")) {//11已绑定正常
                        initBind(band);
                        initToBind(band);
                    } else if (status.equals("3")) {
                        startActivity(new Intent().setClass(BindActivity.this, BackStateActivity.class));
                    }
                }


            }

            @Override
            public void onFailure(Call<BandstatusBean> call, Throwable t) {
                MoudleUtils.kyloadingDismiss(kyLoadingBuilder);
                ToastUtils.showShort(BindActivity.this, getResources().getString(R.string.not_wlan_show));
            }
        });
    }


    void initToYaJin(String ordernum, String bind) {

        Intent intent = new Intent();
        intent.putExtra("ordernum", ordernum);
        intent.putExtra("bind", bind);
        intent.setClass(BindActivity.this, BindHSHActivity.class);
        startActivity(intent);
    }

    void initBind(String band) {
        if (band == null || band.equals(""))
            return;
    }

    private void initToBindTask(final Context context, final String bind) {
        int userid = (int) SPUtils.get(context, "user_id", 0);
        String token = (String) SPUtils.get(context, "token", "");
        Call<BandBean> bandstatusBean = RetrofitUtils.retrofit.create(NpApi.class).band(token, userid, bind);
        bandstatusBean.enqueue(new Callback<BandBean>() {
            @Override
            public void onResponse(Call<BandBean> call, Response<BandBean> response) {
                MoudleUtils.dialogDismiss(dialig);
                BandBean bandstatusBean = response.body();
                if (bandstatusBean == null)
                    return;
                //status|状态|0：绑定失败；1：绑定成功；2：token错误；

                String status = bandstatusBean.getStatus();
                String msg = bandstatusBean.getMsg();
                if (status == null)
                    return;
                if (status.equals("0") || status.equals("2")) {
                    ToastUtils.showShort(BindActivity.this, msg);
                    return;
                }
                //type1正常2租赁
                if (status.equals("1")) {
                    SavaDataLocalUtils.saveDataString(CkApplication.getInstance(), "isbind", bandstatusBean.getInfo().getIsbind());
                    initBind(bind);
                    initToBind(bind);
                }
            }

            @Override
            public void onFailure(Call<BandBean> call, Throwable t) {
                MoudleUtils.dialogDismiss(dialig);
                ToastUtils.showShort(BindActivity.this, getResources().getString(R.string.not_wlan_show));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((SPUtils.get(this, "mDeviceAddress", "") + "") != null
                && !(SPUtils.get(this, "mDeviceAddress", "") + "").equals("") && AppConfig.mBluetoothGatt != null && AppConfig.bluetoothGattServices != null) {
            finish();
        } else {

        }
    }

    private void initSet() {
        lv_bind.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!flag) {
                    ToastUtils.showShort(BindActivity.this, "扫描未完成");
                    return;
                }
                if (mLeDeviceListAdapter != null && mLeDeviceListAdapter.getCount() > 0) {
                    if (mLeDeviceListAdapter.getDevice(position) == null)
                        return;
                    String bind = mLeDeviceListAdapter.getDevice(position).getAddress();
                    initBind(bind);
                    kyLoadingBuilder = new KyLoadingBuilder(BindActivity.this);
                    MoudleUtils.kyloadingShow(kyLoadingBuilder);
                    toBindFistApi(BindActivity.this, bind, position);
                }
            }
        });
    }

    void initToBind(String bind) {
        final Intent intent = new Intent(BindActivity.this, DeviceControlActivity.class);
        intent.putExtra(AppConfig.EXTRAS_DEVICE_ADDRESS, bind);
        startActivity(intent);
    }

    /**
     * 返回设置
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify shouhuan_serch parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 标题栏
     */
    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("绑定设备");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        scanLeDevice(false);
    }

    /**
     * 开始搜索手环
     *
     * @param enable
     */
    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after shouhuan_serch pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    initStop();
                }
            }, SCAN_PERIOD);
            initStart();
        } else {
            initStop();
        }
    }

    private void initStart() {
        if (BluethUtils.bluetoothAdapter != null && mLeScanCallback != null) {
            BluethUtils.bluetoothAdapter.startLeScan(mLeScanCallback);
        }
    }

    private void initStop() {
        try {
            if (BluethUtils.bluetoothAdapter != null && mLeScanCallback != null) {
                if (mLeDeviceListAdapter != null) {
                    if (mLeDeviceListAdapter.getCount() == 0) {
                        initNotHave();
                    } else if (mLeDeviceListAdapter.getCount() > 0) {
                        flag = true;
                        initShowDivce();
                        BluethUtils.bluetoothAdapter.stopLeScan(mLeScanCallback);
                    }
                } else {
                    initNotHave();
                }
            } else {
                initNotHave();
            }
        } catch (Exception e) {
            e.printStackTrace();
            initNotHave();
        }

    }

    /**
     * 未搜索到手环
     */
    private void initNotHave() {
        btn_sure.setEnabled(true);
        MoudleUtils.textViewSetText(tv_show, "搜索超时，未发现可用设备");
        MoudleUtils.viewShow(ll_sou_suo);
    }

    private void initShowDivce() {
        if (mLeDeviceListAdapter != null && mLeDeviceListAdapter.getCount() > 0) {
            MoudleUtils.textViewSetText(textView, "可用设备");
            MoudleUtils.textViewSetText(tv_name, "当前设备");
            MoudleUtils.viewGone(ll_sou_suo);
            lv_bind.setAdapter(mLeDeviceListAdapter);
        }
    }

    private LeDeviceListAdapter mLeDeviceListAdapter;
    /**
     * 蓝牙搜索手环回掉
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    String struuid = MoudleUtils.bytes2HexString(MoudleUtils.reverseBytes(scanRecord)).replace("-", "").toLowerCase();
                    Log.d(tag, "run: " + struuid);
                    String name = device.getName();
                    Log.d(tag, "run:sring " + name);
                    if (!TextUtils.isEmpty(name) && "HSH".contains(name)) {
                        if (mLeDeviceListAdapter == null) {
                            mLeDeviceListAdapter = new LeDeviceListAdapter();
                            mLeDeviceListAdapter.setContext(BindActivity.this);
                            mLeDeviceListAdapter.addDevice(device);
                            initSet();
                        } else {
                            if (!flag) {
                                mLeDeviceListAdapter.addDevice(device);
                                mLeDeviceListAdapter.notifyDataSetChanged();
                            }
                        }

                    }
                }
            });
        }
    };
}
