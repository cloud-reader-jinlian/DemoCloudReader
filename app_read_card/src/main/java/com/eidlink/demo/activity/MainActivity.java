package com.eidlink.demo.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eidlink.demo.R;
import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseActivity;
import com.eidlink.idocr.sdk.EidLinkSESDK;
import com.eidlink.idocr.sdk.listener.OnEidInitListener;
import com.eidlink.idocr.sdk.listener.OnGetDelayListener;
import com.eidlink.idocr.sdk.listener.OnGetEidStatusListener;
import com.eidlink.idocr.sdk.util.DelayUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private EditText tv_message;
    private TextView tv_version;
    private Button   bt_get_imei, bt_delay, bt_card, bt_travel, bt_webEC, bt_eid_fun, bt_eid_callback_read;
    private boolean           initEidSuccess;
    private OnEidInitListener mInitListener = new OnEidInitListener() {
        @Override
        public void onSuccess() {
            tv_message.setText("初始化eid成功");
            initEidSuccess = true;
        }

        @Override
        public void onFailed(int i) {
            tv_message.setText("初始化eid失败，错误码:" + i);
            initEidSuccess = false;
        }
    };

    @Override
    protected int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEvent() {
        tv_message = findViewById(R.id.tv_message);
        tv_version = findViewById(R.id.tv_version);
        bt_delay = findViewById(R.id.bt_delay);
        bt_card = findViewById(R.id.bt_card);
        bt_travel = findViewById(R.id.bt_travel);
        bt_webEC = findViewById(R.id.bt_webEC);
        bt_eid_fun = findViewById(R.id.bt_eid_fun);
        bt_eid_callback_read = findViewById(R.id.bt_eid_callback_read);
        bt_get_imei = findViewById(R.id.bt_get_imei);
        bt_card.setOnClickListener(this);
        bt_travel.setOnClickListener(this);
        bt_webEC.setOnClickListener(this);
        bt_eid_callback_read.setOnClickListener(this);
        bt_delay.setOnClickListener(this);
        bt_eid_fun.setOnClickListener(this);
        bt_get_imei.setOnClickListener(this);
        tv_version.setText("SDK版本:" + EidLinkSESDK.getSDKVersion());
        //请求动态权限，读写sd卡权限和读取手机号状态权限
//        bt_travel.setVisibility(View.GONE);
//        bt_get_imei.setVisibility(View.GONE);
//        bt_get_sn.setVisibility(View.GONE);
//        bt_webEC.setVisibility(View.GONE);
//        bt_eidsign.setVisibility(View.GONE);
//        bt_eid_callback_read.setVisibility(View.GONE);
        requestPermissions();
        initEid();
    }

    private void initEid() {
        initEidSuccess = false;
        ReadCardManager.initEid(getApplicationContext(), mInitListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                        tv_message.setText("时延:" + delayTime + "ms");
                    }

                    @Override
                    public void onFailed(int code) {
                        bt_delay.setEnabled(true);
                        tv_message.setText("时延测试错误，错误信息:" + code);
                    }
                });
                break;
            case R.id.bt_card:
                if (!initEidSuccess) {
                    showToast("请初始化sdk成功后再使用sdk功能。");
                    return;
                }
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
