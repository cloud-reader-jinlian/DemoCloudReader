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
    private static String appid   = "";
    private static String ip      = "testnidocr.eidlink.com";
    private static int    envCode = 26814;
    private static int    port    = 9989;

    /**
     * SDK初始化
     * 不进行任何设置，如需设置某些参数，可查看sdk文档。
     */
    public static void initEid(Context context, final OnEidInitListener listener) {
        eid = EidLinkSEFactory.getEidLinkSE(new EidlinkInitParams(context, appid, ip, port, envCode), new OnEidInitListener() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
                /**
                 * 启用前端返数据功能，默认为不启用
                 */
                ReadCardManager.eid.setGetDataFromSdk(true);
                /**
                 * 启用银联设备sn。需要设备支持sdk4upos_20191225_umix.jar包获取sn的方法。
                 * sn获取失败会返回错误码。
                 */
//                ReadCardManager.eid.setUnionpaySn(MainActivity.this);
                /**
                 * 启用安卓标准的sn作为设备标识。sn获取方法，
                 */
//                ReadCardManager.eid.setDeviceType(1);
            }

            @Override
            public void onFailed(int i) {
                listener.onFailed(i);
            }
        });
    }
}
