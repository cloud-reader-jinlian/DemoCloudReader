package com.eidlink.demo;

import android.content.Context;

import com.eidlink.idocr.sdk.EidLinkSE;
import com.eidlink.idocr.sdk.EidLinkSEFactory;
import com.eidlink.idocr.sdk.bean.EidlinkInitParams;
import com.eidlink.idocr.sdk.listener.OnEidInitListener;

public class ReadCardManager {
    public static EidLinkSE eid;
    /**
     * 生产环境配置：
     * appid:请填写生产环境分配的appid
     * 生产环境ip：idocrap.eidlink.com
     * 端口：9989
     * envCode：52302
     */

    /**
     * 测试环境配置：
     * appid:请填写测试环境分配的appid
     * 测试环境ip：testnidocr.eidlink.com
     * 端口：9989
     * envCode：26814
     */
    private static String appid   = "1190807";
    private static String ip      = "testnidocr.eidlink.com";
    private static int    envCode = 26814;
    private static int    port    = 9989;

    /**
     * SDK初始化
     * 不进行任何设置，如需设置某些参数，可查看sdk文档。
     */
    public static void initEid(final Context context, final OnEidInitListener listener) {
        eid = EidLinkSEFactory.getEidLinkSE(new EidlinkInitParams(context, appid, ip, port, envCode), new OnEidInitListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }

            @Override
            public void onFailed(int i) {
                listener.onFailed(i);
            }
        });
    }
}
