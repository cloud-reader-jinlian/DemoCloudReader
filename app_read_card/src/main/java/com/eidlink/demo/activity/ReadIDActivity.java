package com.eidlink.demo.activity;

import android.graphics.Bitmap;
import android.nfc.Tag;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseNfcActivity;
import com.eidlink.idocr.sdk.bean.EidlinkResult;
import com.eidlink.idocr.sdk.listener.OnGetResultListener;
import com.eidlink.demo.R;
import com.zkteco.android.IDReader.IDCardPhoto;

public class ReadIDActivity extends BaseNfcActivity {

    private EditText tv_msg;
    private ImageView iv_image;
    private long starttime, endtime;

    @Override
    protected int getViewId() {
        return R.layout.activity_read_id;
    }

    @Override
    protected void initEvent() {
        tv_msg = this.findViewById(R.id.tv_msg);
        iv_image = this.findViewById(R.id.iv_image);
    }

    @Override
    protected void onNfcEvent(Tag tag) {
        if (ReadCardManager.eid == null) {
            showToast("eid对象为空，请初始化sdk成功后再使用sdk功能。");
            return;
        }
        /**
         * ReadCardManager.eid.readIDCard(tag, mResultListener);
         * 通用模式，同时支持二代证读取和eID电子证照读取
         */

        ReadCardManager.eid.readIDCard(tag, mResultListener);

        /**
         * ReadCardManager.eid.readIDCard(IDOCRCardType.IDCARD,tag, mResultListener);
         * 设置只支持二代证读取
         */
//        ReadCardManager.eid.readIDCard(IDOCRCardType.IDCARD,tag, mResultListener);

        /**
         * ReadCardManager.eid.readIDCard(IDOCRCardType.ECCARD,tag, mResultListener);
         * 设置只支持eID电子证照读取
         */
//        ReadCardManager.eid.readIDCard(IDOCRCardType.ECCARD,tag, mResultListener);
    }

    @Override
    protected void onNfcError(boolean has) {
        if (has) {
            tv_msg.setText("请在系统设置中先启用NFC功能");
        } else {
            tv_msg.setText("设备不支持NFC");
        }
    }

    private OnGetResultListener mResultListener = new OnGetResultListener() {

        @Override
        public void onSuccess(EidlinkResult result) {
            endtime = System.currentTimeMillis() - starttime;
            tv_msg.setText("读卡成功  " + result.toString() + "\n耗时: " + endtime + "ms");
            if (result.getIdentity() != null && !TextUtils.isEmpty(result.getIdentity().getPicture())) {
                Bitmap bt = IDCardPhoto.getIDCardPhoto(result.getIdentity().getPicture());
                if (bt != null) {
                    iv_image.setVisibility(View.VISIBLE);
                    iv_image.setImageBitmap(bt);
                }
            }
        }

        @Override
        public void onFailed(int code, String msg) {
            tv_msg.setText("读卡失败: " + code + msg);
        }

        @Override
        public void onStart() {
            super.onStart();
            tv_msg.setText("开始读卡");
            starttime = System.currentTimeMillis();
            iv_image.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onDestroy() {
        ReadCardManager.eid.release();
        super.onDestroy();
    }
}
