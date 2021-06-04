package com.eidlink.demo.activity;

import android.nfc.Tag;
import android.text.TextUtils;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.eidlink.demo.R;
import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseNfcActivity;
import com.eidlink.idocr.sdk.bean.EidlinkResult;
import com.eidlink.idocr.sdk.listener.OnGetResultListener;

/**
 * 读取旅行证件
 * SDK次功能需依赖eid-travel-bc-v1.0.0.jar
 * IDOCR.PubSdk.Android.Std.NFC.Release-*.*.*.jar
 */
public class ReadTravelActivity extends BaseNfcActivity {
    private EditText et_num, et_birth, et_validity;
    private TextView     tv_msg;
    private ToggleButton tb;
    private long         starttime, endtime;
    private boolean isImg = true;


    @Override
    protected void initEvent() {
        tv_msg = this.findViewById(R.id.tv_msg);
        et_num = this.findViewById(R.id.et_num);
        et_birth = this.findViewById(R.id.et_birth);
        et_validity = this.findViewById(R.id.et_validity);
        tb = this.findViewById(R.id.tb);
        initClick();
        et_num.setText("E47378331");
        et_birth.setText("830402");
        et_validity.setText("250329");
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_read_travel;
    }

    private void initClick() {
        tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isImg = isChecked;
            }
        });
        tb.setChecked(true);
    }

    @Override
    protected void onNfcError(boolean has) {
        if (has) {
            tv_msg.setText("请在系统设置中先启用NFC功能");
        } else {
            tv_msg.setText("设备不支持NFC");
        }
    }

    @Override
    protected void onNfcEvent(Tag tag) {
        readTravel(tag);
    }

    private void readTravel(Tag tag) {
        et_num = this.findViewById(R.id.et_num);
        et_birth = this.findViewById(R.id.et_birth);
        et_validity = this.findViewById(R.id.et_validity);
        if (TextUtils.isEmpty(et_num.getText().toString())
                || TextUtils.isEmpty(et_birth.getText().toString())
                || TextUtils.isEmpty(et_validity.getText().toString())) {
            tv_msg.setText("参数不能为空");
        } else {
            /*读取旅行证件方法
             * 读取旅行证件需求三要素
             * 1 证件号
             * 2 生日日期
             * 3 有效期至
             * 日期格式统一为: 950328
             * */
            if(ReadCardManager.eid==null){
                showToast("eid对象为空，请初始化sdk成功后再使用sdk功能。");
                return;
            }
            ReadCardManager.eid.readTravel(tag, et_num.getText().toString().trim(), et_birth.getText().toString().trim(), et_validity.getText().toString().trim(), isImg, mResultListener);
        }
    }

    private OnGetResultListener mResultListener = new OnGetResultListener() {
        @Override
        public void onSuccess(EidlinkResult result) {
            endtime = System.currentTimeMillis() - starttime;
            tv_msg.setText("读卡成功  " + result.getReqId() + "   time:" + endtime);
        }

        @Override
        public void onFailed(int code,String msg) {
            tv_msg.setText("读卡失败: " + code);
        }

        @Override
        public void onStart() {
            super.onStart();
            tv_msg.setText("开始读卡");
            starttime = System.currentTimeMillis();
        }
    };

}
