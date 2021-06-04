package com.eidlink.demo.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseActivity;
import com.eidlink.idocr.sdk.bean.EidlinkResult;
import com.eidlink.idocr.sdk.listener.OnGetResultListener;

/**
 * eID签名功能。
 */
@SuppressLint("SetTextI18n")
public class EIDSignActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_msg;
    private Button bt_sign;
    private long starttime, endtime;
    private String eidShowMsg = "您已阅读XX协议所有内容，请点击确认进行eID签名。";

    @Override
    protected void initEvent() {
        initView();
    }

    @Override
    protected int getViewId() {
        return com.eidlink.demo.R.layout.activity_eid_sign;
    }

    private void initView() {
        tv_msg = this.findViewById(com.eidlink.demo.R.id.tv_msg);
        bt_sign = this.findViewById(com.eidlink.demo.R.id.bt_sign);
        bt_sign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == com.eidlink.demo.R.id.bt_sign) {
            ReadCardManager.eid.eidSign(eidShowMsg, mResultListener);
        }
    }

    private OnGetResultListener mResultListener = new OnGetResultListener() {

        @Override
        public void onSuccess(EidlinkResult result) {
            endtime = System.currentTimeMillis() - starttime;
            tv_msg.setText("签名验签成功  " + result.toString() + "   耗时 :" + endtime + "ms");
        }

        @Override
        public void onFailed(int code, String msg) {
            tv_msg.setText("读卡失败: " + code + msg + "   耗时 :" + endtime + "ms");
        }

        @Override
        public void onStart() {
            super.onStart();
            tv_msg.setText("开始读卡");
            starttime = System.currentTimeMillis();
        }
    };
}
