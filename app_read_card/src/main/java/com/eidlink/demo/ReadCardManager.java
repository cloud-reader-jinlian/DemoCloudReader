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
    private static String appid   = "";
    private static String ip      = "testeidcloudread.eidlink.com";
    private static int    envCode = 26814;
    private static int    port    = 9989;

    /**
     * SDK初始化
     */
    public static void initEid(final Context context, final OnEidInitListener listener) {
        eid = EidLinkSEFactory.getEidLinkSE(new EidlinkInitParams(context, appid, ip, port, envCode), new OnEidInitListener() {
            @Override
            public void onSuccess() {
                /**
                 * 启用前端返数据功能,目前仅支持二代证读取。
                 * 方法介绍请查看sdk文档。
                 * 如需使用前端返数据功能，需联系我司开通使用授权。
                 */
//                eid.setGetDataFromSdk(true);
                /**
                 * 设置sdk自动重读次数为2次。
                 */
                eid.setReadCount(2);
                listener.onSuccess();
            }

            @Override
            public void onFailed(int i) {
                listener.onFailed(i);
            }
        });
    }
}
