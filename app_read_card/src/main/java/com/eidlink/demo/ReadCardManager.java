package com.eidlink.demo;

import android.content.Context;
import android.text.TextUtils;

import com.eidlink.demo.activity.utils.SpUtils;
import com.eidlink.idocr.sdk.EidLinkSE;
import com.eidlink.idocr.sdk.EidLinkSEFactory;
import com.eidlink.idocr.sdk.bean.EidlinkInitParams;
import com.eidlink.idocr.sdk.listener.OnEidInitListener;

public class ReadCardManager {
    public static EidLinkSE eid;
    public static String tag_need_add_picture = "tag_need_add_picture";
    /**
     * 生产环境配置：
     * appid:请填写生产环境分配的appid
     * 生产环境ip：eidcloudread.eidlink.com
     * 端口：9989
     * envCode：52302
     */

    /**
     * 测试环境配置：
     * appid:请填写测试环境分配的appid
     * 测试环境ip：testeidcloudread.eidlink.com
     * 端口：9989
     * envCode：26814
     */
    public static String appid;
    public static String ip = "testeidcloudread.eidlink.com";
    public static int envCode = 26814;
    public static int port = 9989;

    /**
     * SDK初始化
     */
    public static void initEid(final Context context, final OnEidInitListener listener) {
        if (TextUtils.isEmpty(appid)) {
            appid = SpUtils.getAppid(context);
        }
        eid = EidLinkSEFactory.getEidLinkSE(new EidlinkInitParams(context, appid, ip, port, envCode), new OnEidInitListener() {
            @Override
            public void onSuccess() {
                //setGetDataFromSdk:启用前端返数据功能,目前仅支持二代证读取.如需使用前端返数据功能，需联系我司开通使用授权。
                // eid.setGetDataFromSdk(true);
                listener.onSuccess();
            }

            @Override
            public void onFailed(int i) {
                listener.onFailed(i);
            }
        });
    }
}
