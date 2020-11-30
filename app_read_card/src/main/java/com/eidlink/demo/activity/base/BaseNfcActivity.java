package com.eidlink.demo.activity.base;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;

public abstract class BaseNfcActivity extends BaseActivity {
    private NfcAdapter     nfcAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        enableReaderMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableReaderMode();
    }

    private void enableReaderMode() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            onNfcError(false);
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            onNfcError(true);
            return;
        }
        Bundle options = new Bundle();
        //对卡片的检测延迟300ms
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 300);
        int READER_FLAGS = NfcAdapter.FLAG_READER_NFC_B | NfcAdapter.FLAG_READER_NFC_V | NfcAdapter.FLAG_READER_NFC_F
                | NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_BARCODE;
        nfcAdapter.enableReaderMode(this, new NfcAdapter.ReaderCallback() {

            @Override
            public void onTagDiscovered(final Tag tag) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onNfcEvent(tag);
                    }
                });
            }
        }, READER_FLAGS, options);
    }



    public void disableReaderMode() {
        if (nfcAdapter != null) {
            nfcAdapter.disableReaderMode(this);
        }
    }

    protected abstract void onNfcEvent(Tag tag);

    protected abstract void onNfcError(boolean has);
}
