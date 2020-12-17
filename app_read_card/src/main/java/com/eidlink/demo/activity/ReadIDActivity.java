package com.eidlink.demo.activity;

import android.graphics.Bitmap;
import android.nfc.Tag;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseNfcActivity;
import com.eidlink.idocr.sdk.IDOCRCardType;
import com.eidlink.idocr.sdk.bean.EidlinkResult;
import com.eidlink.idocr.sdk.listener.OnGetResultListener;
import com.eidlink.demo.R;
import com.zkteco.android.IDReader.IDCardPhoto;

/**
 * 读取身份证
 * NFC读卡直接贴卡
 * 注意事项: AndroidManifest.xml中所有权限都必须添加
 * targetSdkVersion大于29的时候，清单文件要添加 android:requestLegacyExternalStorage="true" 属性
 */

public class ReadIDActivity extends BaseNfcActivity {

    private EditText  tv_msg;
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
        if(ReadCardManager.eid==null){
            showToast("eid对象为空，请初始化sdk成功后再使用sdk功能。");
            return;
        }
        ReadCardManager.eid.readIDCard(IDOCRCardType.IDCARD,tag, mResultListener);
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
        public void onFailed(int code) {
            tv_msg.setText("读卡失败: " + code);
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
