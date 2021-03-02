package com.eidlink.demo.activity.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.lang.reflect.Method;

public class DeviceUtil {
    @SuppressLint({"MissingPermission"})
    public static String getImei(Context context) {
        TelephonyManager tm   = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String           imei = "";
        try {
            if (tm != null) {
                imei = tm.getDeviceId();
            }
        } catch (Exception e) {
        }
        return imei;
    }

    @SuppressLint({"MissingPermission"})
    public static String getSN() {
        String serial;
        //通过android.os获取sn号
        try {
            serial = android.os.Build.SERIAL;
            if (!serial.equals("") && !serial.equals("unknown")) return serial;
        } catch (Exception e) {
            serial = "";
        }

        //通过反射获取sn号
        try {
            Class<?> c   = Class.forName("android.os.SystemProperties");
            Method   get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
            if (!serial.equals("") && !serial.equals("unknown")) return serial;

            //9.0及以上无法获取到sn，此方法为补充，能够获取到多数高版本手机 sn
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) serial = Build.getSerial();
        } catch (Exception e) {
            serial = "";
        }
        return serial;
    }
}
