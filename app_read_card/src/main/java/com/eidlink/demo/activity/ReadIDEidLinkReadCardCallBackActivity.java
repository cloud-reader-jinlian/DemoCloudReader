package com.eidlink.demo.activity;

import android.nfc.Tag;
import android.nfc.tech.NfcB;
import android.widget.EditText;
import android.widget.TextView;

import com.eidlink.demo.R;
import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseNfcActivity;
import com.eidlink.idocr.sdk.IDOCRCardType;
import com.eidlink.idocr.sdk.bean.EidlinkResult;
import com.eidlink.idocr.sdk.listener.EidLinkReadCardCallBack;
import com.eidlink.idocr.sdk.listener.OnGetResultListener;

import java.io.IOException;

/**
 * 演示EidLinkReadCardCallBack指令下发方式读卡。
 * 适用于身份证，eid电子证照，护照，蓝牙读卡器等功能。
 * 以指令下发的方式实现读卡，客户可以使用第三方设备，通过设备非接接口透传sdk下发的读卡指令，最终完成读卡。
 * 此类中演示以身份证为例。
 * eidLinkReadCardCallBack下发指令，演示Demo使用安卓接口nfcB.transceive(bytes)完成指令和卡片的交互。
 * 第三方设备适配步骤：
 * 1、实现IC卡寻卡激活。
 * 2、激活卡片后，调用我方sdk读卡方法。
 * 3、收到EidLinkReadCardCallBack下发的读卡指令，通过设备非接接口透传给卡片，最终完成读卡。
 * 4、读卡成功，sdk会返回查询的reqId，读卡失败会返回具体错误码。
 * 第三方设备非接接口，需要设备厂商提供，可咨询厂商如何实现读卡指令和卡片的交互。
 */
public class ReadIDEidLinkReadCardCallBackActivity extends BaseNfcActivity {

    private EditText tv_msg;
    private NfcB     nfcB;
    private long     starttime, endtime;

    @Override
    protected void initEvent() {
        tv_msg = this.findViewById(R.id.tv_msg);
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_eid_callback;
    }

    private OnGetResultListener mResultListener = new OnGetResultListener() {
        @Override
        public void onSuccess(EidlinkResult result) {
            endtime = System.currentTimeMillis() - starttime;
            tv_msg.setText("读卡成功  " + result.toString() + "\n耗时: " + endtime + "ms");
            closeNfc();
        }

        @Override
        public void onFailed(int code,String msg) {
            tv_msg.setText("读卡失败: " + code + msg);
            closeNfc();
        }

        @Override
        public void onStart() {
            super.onStart();
            tv_msg.setText("开始读卡");
            starttime = System.currentTimeMillis();
        }
    };

    private void closeNfc() {
        if (nfcB != null) {
            try {
                nfcB.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 第三方适配读卡方法，此读卡方法不是NFC触发的！！！
     * readCard这是读卡方法，和NFC读卡方法功能类似
     * 参数1  CardType 设置读卡类型 ，建议指定Type   ，例如 IDOCRCardType.IDCARD 身份证  ，IDOCRCardType.ECCARD 电子证照
     * 参数2 EidLinkReadCardCallBack 读卡方法实现，将读卡的transceive方法在回调中实现
     * PS：设备返回的是标准数据，一般都没有问题
     **/
    @Override
    protected void onNfcEvent(Tag tag) {
        try {
            nfcB = NfcB.get(tag);
            nfcB.connect();
            if (null != nfcB && nfcB.isConnected()) {
                if(ReadCardManager.eid==null){
                    showToast("eid对象为空，请初始化sdk成功后再使用sdk功能。");
                    return;
                }
                ReadCardManager.eid.readIDCard(IDOCRCardType.IDCARD, eidLinkReadCardCallBack, mResultListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNfcError(boolean has) {

    }

    /**
     * EidLinkReadCardCallBack 中实现transceive方法用于读卡
     * bytes：下发的读卡指令
     * transceive：返回值为读取密文
     * 读取电子证照实现transceiveTypeA
     * 读取身份证实现transceiveTypeB
     * 方法实现,bytes为读卡指令,return 身份证密文数据（密文格式：xxx9000或xxx900000,xxx为密文数据）
     * 如需实现回调读卡，transceiveTypeB/transceiveTypeA 回调负责下发指令，将执行指令后的数据返给sdk即可。
     */
    EidLinkReadCardCallBack eidLinkReadCardCallBack = new EidLinkReadCardCallBack() {

        @Override
        public byte[] transceiveTypeB(byte[] bytes) {
            try {
                return nfcB.transceive(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public byte[] transceiveTypeA(byte[] bytes) {
            return null;
        }
    };
}
