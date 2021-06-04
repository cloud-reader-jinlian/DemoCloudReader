package com.eidlink.demo.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eidlink.bluetooth.activity.BlueToothActivity;
import com.eidlink.demo.R;
import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseActivity;
import com.eidlink.demo.activity.utils.SpUtils;
import com.eidlink.idocr.sdk.EidLinkSESDK;
import com.eidlink.idocr.sdk.listener.OnEidInitListener;
import com.eidlink.idocr.sdk.listener.OnGetDelayListener;
import com.eidlink.idocr.sdk.listener.OnGetEidStatusListener;
import com.eidlink.idocr.sdk.util.DelayUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_message;
    private TextView tv_version;
    private Button bt_clear, bt_save, bt_get_imei, bt_delay, bt_card, bt_travel, bt_webEC, bt_eid_fun, bt_eid_callback_read, bt_bluetooth;
    private boolean initEidSuccess;
    private Button bt_setting;

    private OnEidInitListener mInitListener = new OnEidInitListener() {
        @Override
        public void onSuccess() {
            et_message.setText("初始化成功,当前账号:" + ReadCardManager.appid);
            initEidSuccess = true;
        }

        @Override
        public void onFailed(int i) {
            et_message.setText("初始化eid失败，错误码:" + i);
            initEidSuccess = false;
        }
    };

    @Override
    protected int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEvent() {
        et_message = findViewById(R.id.et_message);
        tv_version = findViewById(R.id.tv_version);
        bt_delay = findViewById(R.id.bt_delay);
        bt_card = findViewById(R.id.bt_card);
        bt_travel = findViewById(R.id.bt_travel);
        bt_webEC = findViewById(R.id.bt_webEC);
        bt_eid_fun = findViewById(R.id.bt_eid_fun);
        bt_eid_callback_read = findViewById(R.id.bt_eid_callback_read);
        bt_get_imei = findViewById(R.id.bt_get_imei);
        bt_save = findViewById(R.id.bt_save);
        bt_clear = findViewById(R.id.bt_clear);
        bt_bluetooth = findViewById(R.id.bt_bluetooth);
        bt_setting = findViewById(R.id.bt_setting);
        bt_setting.setOnClickListener(this);
        bt_card.setOnClickListener(this);
        bt_travel.setOnClickListener(this);
        bt_webEC.setOnClickListener(this);
        bt_eid_callback_read.setOnClickListener(this);
        bt_delay.setOnClickListener(this);
        bt_eid_fun.setOnClickListener(this);
        bt_get_imei.setOnClickListener(this);
        bt_save.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_bluetooth.setOnClickListener(this);
        tv_version.setText("SDK版本:" + EidLinkSESDK.getSDKVersion());
        //请求动态权限，读写sd卡权限和读取手机号状态权限
//        bt_travel.setVisibility(View.GONE);
//        bt_get_imei.setVisibility(View.GONE);
//        bt_get_sn.setVisibility(View.GONE);
//        bt_webEC.setVisibility(View.GONE);
//        bt_eidsign.setVisibility(View.GONE);
//        bt_eid_callback_read.setVisibility(View.GONE);
        requestPermissions();


        if (!TextUtils.isEmpty(ReadCardManager.appid)) {
            et_message.setText(ReadCardManager.appid);
            initEid();
            return;
        }

        //如果appid没有设置,就获取上一次保存的appid
        if (!TextUtils.isEmpty(SpUtils.getAppid(getApplicationContext()))) {
            et_message.setText(SpUtils.getAppid(getApplicationContext()));
            initEid();
        }
    }

    private void initEid() {
        initEidSuccess = false;
        ReadCardManager.initEid(getApplicationContext(), mInitListener);
        ReadCardManager.eid.setReadPicture(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_setting:
                startActivityNoFinish(SettingActivity.class);
                break;
            case R.id.bt_save:
                if (initEidSuccess) {
                    showToast("请清除账号后再重新配置账号");
                    return;
                }
                String appid = et_message.getText().toString().trim();
                if (TextUtils.isEmpty(appid)) {
                    showToast("请填写cid或appid后再点击保存按钮。");
                    return;
                }
                SpUtils.setAppid(getApplicationContext(), appid);
                initEid();
                break;
            case R.id.bt_clear:
                SpUtils.clear(getApplicationContext());
                ReadCardManager.appid = null;
                initEidSuccess = false;
                et_message.setText("");
                break;
            case R.id.bt_delay:
                if (!initEidSuccess) {
                    showToast("请初始化sdk成功后再使用sdk功能。");
                    return;
                }
                bt_delay.setEnabled(false);
                DelayUtil.getDelayTime(5, new OnGetDelayListener() {
                    @Override
                    public void onSuccess(long delayTime) {
                        bt_delay.setEnabled(true);
                        et_message.setText("时延:" + delayTime + "ms");
                    }

                    @Override
                    public void onFailed(int code) {
                        bt_delay.setEnabled(true);
                        et_message.setText("时延测试错误，错误信息:" + code);
                    }
                });
                break;
            case R.id.bt_card:
//                if (!initEidSuccess) {
//                    showToast("请初始化sdk成功后再使用sdk功能。");
//                    return;
//                }
                startActivityNoFinish(ReadIDActivity.class);
                break;
            case R.id.bt_travel:
                if (!initEidSuccess) {
                    showToast("请初始化sdk成功后再使用sdk功能。");
                    return;
                }
                startActivityNoFinish(ReadTravelActivity.class);
                break;
            case R.id.bt_eid_callback_read:
                if (!initEidSuccess) {
                    showToast("请初始化sdk成功后再使用sdk功能。");
                    return;
                }
                startActivityNoFinish(ReadIDEidLinkReadCardCallBackActivity.class);
                break;
            case R.id.bt_bluetooth:
                Intent intent = new Intent(this, BlueToothActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("appid", ReadCardManager.appid);
                intent.putExtra("ip", ReadCardManager.ip);
                intent.putExtra("port", ReadCardManager.port + "");
                intent.putExtra("envCode", ReadCardManager.envCode + "");
                startActivity(intent);
                break;
            case R.id.bt_webEC:
                if (!initEidSuccess) {
                    showToast("请初始化sdk成功后再使用sdk功能。");
                    return;
                }
                if (ReadCardManager.eid == null) {
                    showToast("eid对象为空，请初始化sdk成功后再使用sdk功能。");
                    return;
                }
                showProgressDialog("检测eID是否开通,请稍候...");
                ReadCardManager.eid.eidIsOpen(this, new OnGetEidStatusListener() {
                    @Override
                    public void isOpened() {
                        closeProgressDialog();
                        startActivityNoFinish(ReadWalletECActivity.class);
                    }

                    @Override
                    public void onFailed(String error) {
                        closeProgressDialog();
                        showToast(error);
                    }
                });
                break;
            case R.id.bt_eid_fun:
                if (!initEidSuccess) {
                    showToast("请初始化sdk成功后再使用sdk功能。");
                    return;
                }
                if (ReadCardManager.eid == null) {
                    showToast("eid对象为空，请初始化sdk成功后再使用sdk功能。");
                    return;
                }
                startActivityNoFinish(EidFunActivity.class);
                break;
            case R.id.bt_get_imei:
                if (!initEidSuccess) {
                    showToast("请初始化sdk成功后再使用sdk功能。");
                    return;
                }
                startActivityNoFinish(DeviceSettingActivity.class);
                break;
        }
    }

}
