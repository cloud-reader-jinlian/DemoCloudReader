package com.eidlink.demo.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.eidlink.demo.R;
import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseActivity;
import com.eidlink.idocr.sdk.bean.EidlinkResult;
import com.eidlink.idocr.sdk.listener.OnGetResultListener;

/**
 * 前端返电子证照
 * 调起本机钱包的中eID证件，目前只支持华为钱包
 * 此方法调用钱包多为异步调用，所以其中有请求时间限制，如长时间无操作，可能会导致失败
 */
public class ReadWalletECActivity extends BaseActivity {

    private TextView tv_msg;
    private Button   okbtn;
    private long     starttime, endtime;
    /**
     * DataToBeDisplayed  调起钱包是显示的文字(最多70个可见字符)
     */
    public static String DataToBeDisplayed = "金联汇通测试";

    @Override
    protected void initEvent() {
        initView();
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_read_wallet_ec;
    }

    private void initView() {
        tv_msg = this.findViewById(R.id.tv_msg);
        okbtn = this.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 前端返电子证照方法
                 */
                if(ReadCardManager.eid==null){
                    showToast("eid对象为空，请初始化sdk成功后再使用sdk功能。");
                    return;
                }
                ReadCardManager.eid.readWalletEC(DataToBeDisplayed, mResultListener);
            }
        });
    }

    private OnGetResultListener mResultListener = new OnGetResultListener() {
        @Override
        public void onSuccess(EidlinkResult result) {
            endtime = System.currentTimeMillis() - starttime;
            tv_msg.setText("读卡成功  " + result.getReqId() + "  时间 : " + endtime + "ms");
        }

        @Override
        public void onFailed(int code) {
            endtime = System.currentTimeMillis() - starttime;
            tv_msg.setText("读卡失败: " + code);
        }

        @Override
        public void onStart() {
            super.onStart();
            starttime = System.currentTimeMillis();
            tv_msg.setText("开始读卡");
        }
    };
}
