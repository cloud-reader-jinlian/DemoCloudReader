package com.eidlink.demo.activity;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eidlink.demo.R;
import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseActivity;
import com.eidlink.demo.activity.utils.DeviceUtil;
import com.eidlink.idocr.sdk.EidDeviceType;

/**
 * 包终端付费说明演示页面。
 */
public class DeviceSettingActivity extends BaseActivity implements View.OnClickListener {
    private EditText et_message;
    private TextView tv_message;
    private Button   bt_get_sn, bt_get_imei, bt_set_sn, bt_set_imei, bt_set_no;
    private boolean set_success;

    @Override
    protected int getViewId() {
        return R.layout.activity_device_setting;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void initEvent() {
        et_message = findViewById(R.id.et_message);
        tv_message = findViewById(R.id.tv_message);
        bt_get_sn = findViewById(R.id.bt_get_sn);
        bt_get_imei = findViewById(R.id.bt_get_imei);
        bt_set_imei = findViewById(R.id.bt_set_imei);
        bt_set_sn = findViewById(R.id.bt_set_sn);
        bt_set_no = findViewById(R.id.bt_set_no);

        bt_get_sn.setOnClickListener(this);
        bt_get_imei.setOnClickListener(this);
        bt_set_sn.setOnClickListener(this);
        bt_set_imei.setOnClickListener(this);
        bt_set_no.setOnClickListener(this);
        tv_message.setText(
                "包终端使用说明，安卓10及以上设备，因无法获取手机imei号和sn号无法包终端使用实证功能。\n" +
                        "实证SDK默认不收集IMEI号作为包终端标识，如需启用包终端请按以下步骤操作:\n" +
                        "1、首先前端SDK配置启用SN和IMEI号终端读取方法。\n" +
                        "2、前端设置启用读取设备标识后，联系我司开通终端授权，或开通终端自助开通接口使用授权。");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_set_imei:
                //启用读卡imei号作为设备标识。
                set_success = ReadCardManager.eid.setDeviceType(EidDeviceType.IMEI);
                et_message.setText(set_success ? "启用读取imei成功" : "启用读取imei失败，无法获取imei号，请检查设备权限或检查设备是否支持获取imei号");
                break;
            case R.id.bt_set_sn:
                //启用读卡imei号作为设备标识。
                set_success = ReadCardManager.eid.setDeviceType(EidDeviceType.SN);
                et_message.setText(set_success ? "启用读取sn成功" : "启用读取sn失败，无法获取sn号，请检查设备权限或检查设备是否支持获取sn号");
                break;
            case R.id.bt_set_no:
                //不启用包终端。
                ReadCardManager.eid.setDeviceType(EidDeviceType.NOTSET);
                et_message.setText("已取消包终端设置");
                break;
            case R.id.bt_get_sn:
                et_message.setText("");
                String sn = DeviceUtil.getSN();
                if (TextUtils.isEmpty(sn)) {
                    showToast("获取sn失败");
                    et_message.setText("获取sn失败");
                    return;
                }
                showToast("获取sn成功");
                et_message.setText("sn:" + sn);
                break;

            case R.id.bt_get_imei:
                et_message.setText("");
                String imei = DeviceUtil.getImei(getApplicationContext());
                if (TextUtils.isEmpty(imei)) {
                    showToast("获取imei失败");
                    et_message.setText("获取imei失败");
                    return;
                }
                showToast("获取imei成功");
                et_message.setText("imei:" + imei);
                break;
        }
    }
}
