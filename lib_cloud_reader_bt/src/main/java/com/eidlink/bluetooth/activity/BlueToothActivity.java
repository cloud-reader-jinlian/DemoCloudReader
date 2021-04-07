package com.eidlink.bluetooth.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eidlink.bluetooth.R;
import com.eidlink.bluetooth.BTData;
import com.eidlink.bluetooth.activity.base.BaseActivity;
import com.eidlink.idocr.sdk.IDOCRCardType;
import com.eidlink.idocr.sdk.bean.EidlinkInitParams;
import com.eidlink.idocr.sdk.bean.EidlinkResult;
import com.idocr.bt.IDOCRBT;
import com.idocr.bt.IDOCRBTCallBack;
import com.idocr.bt.IDOCRBTSDK;

import java.util.Objects;


public class BlueToothActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_appid, et_ip, et_port, et_envcode;
    private Button bt_connect, bt_readcard, bt_disconnect, bt_save_config;
    private TextView tv_message, tv_version;
    private static final int tag_select_bt_device = 22;

    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    public static String EXTRA_DEVICE_NAME = "device_name";

    private String addressmac, namemac;
    private boolean btType = false;

    private long starttime;
    public static String appid;
    public static String ip;
    public static int envCode;
    public static int port;

    @Override
    protected int getViewId() {
        return R.layout.activity_lib_bt;
    }

    @Override
    protected void initEvent() {
        initView();
        try {
            /**
             * 获取主菜单传来的Cid
             * */
            appid = getIntent().getStringExtra("appid");
            ip = getIntent().getStringExtra("ip");
            port = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("port")));
            envCode = Integer.parseInt(Objects.requireNonNull(getIntent().getStringExtra("envCode")));
            et_appid.setText(appid);
            et_ip.setText(ip);
            et_port.setText("" +port);
            et_envcode.setText("" +envCode);
            /**
             * 初始化蓝牙读卡对象
             */
            initBT(appid, ip, port, envCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initView() {
        tv_message = this.findViewById(R.id.tv_message);
        bt_connect = this.findViewById(R.id.bt_connect);
        bt_readcard = this.findViewById(R.id.bt_readcard);
        bt_disconnect = this.findViewById(R.id.bt_disconnect);
        /**
         * 显示sdk版本
         */
        tv_version = this.findViewById(R.id.tv_version);
        tv_version.setText("蓝牙SDK版本:" + IDOCRBTSDK.getSDKVersion());

        /**
         * 参数配置控件
         */
        et_appid = this.findViewById(R.id.et_appid);
        et_ip = this.findViewById(R.id.et_ip);
        et_port = this.findViewById(R.id.et_port);
        et_envcode = this.findViewById(R.id.et_envcode);
        bt_save_config = this.findViewById(R.id.bt_save_config);

        /**
         * 点击开始连接设备
         */
        bt_connect.setOnClickListener(this);
        /**
         *点击开始读卡
         */
        bt_readcard.setOnClickListener(this);
        /**
         * 断开设备
         */
        bt_disconnect.setOnClickListener(this);

        /**
         * 保存修改后的配置
         */
        bt_save_config.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_connect) {
            if (btType) {
                showToast("请先检测蓝牙是否打开");
            } else {
                if (IDOCRBT.getInstance().isConnect()) {
                    showToast("已连接蓝牙请勿重复连接");
                } else {
                    /**
                     * 进入选择蓝牙设备界面
                     */
                    Intent serverIntent2 = new Intent(this, DeviceListOneActivity.class);
                    startActivityForResult(serverIntent2, tag_select_bt_device);
                }
            }
        } else if (id == R.id.bt_readcard) {
            Log.e("TAG", "IDOCRBT.getInstance().isConnect()   " + IDOCRBT.getInstance().isConnect());
            if (IDOCRBT.getInstance().isConnect()) {
                tv_message.setText("开始读卡");
                starttime = System.currentTimeMillis();
                IDOCRBT.getInstance().readCard(IDOCRCardType.IDCARD);
            } else {
                tv_message.setText("连接失败");
            }
        } else if (id == R.id.bt_disconnect) {
            tv_message.setText("断开中,请稍候");
            IDOCRBT.getInstance().disConnect();
        } else if (id == R.id.bt_save_config) {
            String cid = et_appid.getText().toString();
            String ip = et_ip.getText().toString();
            String port = et_port.getText().toString();
            String envcode = et_envcode.getText().toString();
            if (TextUtils.isEmpty(cid) || TextUtils.isEmpty(ip) || TextUtils.isEmpty(port) || TextUtils.isEmpty(envcode)) {
                showToast("参数不能为空");
            } else {
                try {
                    initBT(cid, ip, Integer.parseInt(port), Integer.parseInt(envcode));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initBT(String cid, String ip, int port, int envCode) {
        /*读卡器SDK已经封装身份证读取SDK，初始化统一完成，如果需要使用NFC读取身份证，请按读卡SDK文档，重新实现*/
        /*context 上下文
         * cid 分配的cid
         * ip 私有云地址
         * port 私有云端口号
         * times 读卡次数（1~5次）
         * IDOCRBTCallBack 蓝牙SDK回调*/
        Log.e("TAG", "initBT ");
        IDOCRBT.getInstance().initBT(new EidlinkInitParams(getApplicationContext(), cid, ip, port, envCode), 1, new IDOCRBTCallBack() {
            @Override
            public void onInitSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_message.setText("eid初始化成功");
                    }
                });
            }

            @Override
            public void onSuccess(final EidlinkResult data) {
                //读卡成功返回reqID，用作查询身份信息
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", "onSuccess " + data.getReqId());
                        tv_message.setText("数据:" + data.toString() + "  时间 :   " + (System.currentTimeMillis() - starttime) + "ms");
                    }
                });
            }

            @Override
            public void onFailed(final int i) {
                //失败返回错误码，具体描述参见文档
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", "onFailed  " + i);
                        tv_message.setText("  错误码  " + i);
                    }
                });
            }

            @Override
            public void onConnectSuccess() {
                //连接成功时调用,runOnUiThread中修改UI
                Log.e("eid-TAG", "onConnectSuccess " + System.currentTimeMillis());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e("eiddevice", "连接成功 ");
                            tv_message.setText("连接成功");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            }

            @Override
            public void onConnectFailed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_message.setText("onConnectFailed");
                    }
                });
            }

            @Override
            public void onDisconnect() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //断开连接
                        Log.e("TAG", "onDisconnect ");
                        tv_message.setText("断开成功");
                    }
                });
            }

            @Override
            public void onDisconnectFailed() {
                Log.e("TAG", "onDisconnectFailed");
            }

            @Override
            public void onReadCardFailed() {
                Log.e("TAG", "onReadCardFailed");
            }

            @Override
            public void btNotConnect() {
                Log.e("TAG", "btNotConnect");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            btIsOpen();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void btIsOpen() {
        btType = false;
        BluetoothAdapter blueadapter = BluetoothAdapter.getDefaultAdapter();
        if (null == blueadapter) {
            btType = true;
            Toast.makeText(this, "设备无蓝牙", Toast.LENGTH_SHORT).show();
        }
        if (!blueadapter.isEnabled()) {
            btType = true;
            Toast.makeText(this, "蓝牙未打开,请在设置中打开蓝牙", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            //断开连接
            IDOCRBT.getInstance().disConnect();
            //释放资源
            IDOCRBT.getInstance().release();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == tag_select_bt_device) {
            if (resultCode != Activity.RESULT_OK) return;
            String address = data.getStringExtra(EXTRA_DEVICE_ADDRESS);
            String name = data.getStringExtra(EXTRA_DEVICE_NAME);
            addressmac = address;
            namemac = name;
            tv_message.setText("  设备连接中,请稍候   " + address + "   name " + namemac);
            if (null != BTData.getmBD()) {
                //连接读卡器
                IDOCRBT.getInstance().connectBT(BTData.getmBD());
            }
        }
    }

}
