package com.eidlink.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * Created by lsz on 2019/6/10.
 */

public class BTData {

    public static BluetoothDevice getmBD() {
        return mBD;
    }

    public static void setmBD(BluetoothDevice mBD) {
        BTData.mBD = mBD;
    }

    public static BluetoothDevice mBD;
}
