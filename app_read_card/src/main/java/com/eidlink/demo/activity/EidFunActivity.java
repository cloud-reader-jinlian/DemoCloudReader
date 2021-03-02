package com.eidlink.demo.activity;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.eidlink.demo.R;
import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseActivity;
import com.eidlink.idocr.sdk.bean.EidlinkResult;
import com.eidlink.idocr.sdk.listener.OnEidOpenListener;
import com.eidlink.idocr.sdk.listener.OnGetEidStatusListener;
import com.eidlink.idocr.sdk.listener.OnGetResultListener;

/**
 * eID功能演示界面
 */
public class EidFunActivity extends BaseActivity implements View.OnClickListener {

    private EditText tv_msg;
    private Button   bt_eid_check, bt_eid_open, bt_appeidcode, bt_eid_check_auth,bt_eidsign;

    @Override
    protected int getViewId() {
        return R.layout.activity_eid_fun;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initEvent() {
        tv_msg = this.findViewById(R.id.tv_msg);
        bt_eid_check = this.findViewById(R.id.bt_eid_check);
        bt_eid_open = this.findViewById(R.id.bt_eid_open);
        bt_appeidcode = this.findViewById(R.id.bt_appeidcode);
        bt_eid_check_auth = this.findViewById(R.id.bt_eid_check_auth);
        bt_eidsign = this.findViewById(R.id.bt_eidsign);
        bt_eid_check.setOnClickListener(this);
        bt_eid_open.setOnClickListener(this);
        bt_appeidcode.setOnClickListener(this);
        bt_eid_check_auth.setOnClickListener(this);
        bt_eidsign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_eid_check) {
            ReadCardManager.eid.eidIsOpen(getApplicationContext(), new OnGetEidStatusListener() {
                @Override
                public void isOpened() {
                    tv_msg.setText("设备已开通eID");
                }

                @Override
                public void onFailed(String code) {
                    tv_msg.setText(code);
                }
            });
        } else if (v.getId() == R.id.bt_eid_open) {
            ReadCardManager.eid.eidToOpen(new OnEidOpenListener() {
                @Override
                public void onSuccess() {
                    showToast("开通成功");
                }

                @Override
                public void onFailed(String error) {
                    tv_msg.setText(error);
                }
            });
        } else if (v.getId() == R.id.bt_appeidcode) {
            ReadCardManager.eid.eidGetAppeidcode(new OnGetResultListener() {
                @Override
                public void onSuccess(EidlinkResult result) {
                    tv_msg.setText("获取成功:" + result.getReqId());
                }

                @Override
                public void onFailed(int code) {
                    tv_msg.setText("获取失败,错误码:" + code);
                }
            });
        } else if (v.getId() == R.id.bt_eid_check_auth) {
            ReadCardManager.eid.eidIsOpen(getApplicationContext(), true, new OnGetEidStatusListener() {
                @Override
                public void isOpened() {
                    tv_msg.setText("设备已开通eID");
                }

                @Override
                public void onFailed(String code) {
                    tv_msg.setText(code);
                }
            });
        }else if (v.getId()==R.id.bt_eidsign){
            showProgressDialog("检测eID是否开通,请稍候...");
            ReadCardManager.eid.eidIsOpen(this, new OnGetEidStatusListener() {
                @Override
                public void isOpened() {
                    closeProgressDialog();
                    startActivityNoFinish(EIDSignActivity.class);
                }

                @Override
                public void onFailed(String error) {
                    closeProgressDialog();
                    showToast(error);
                }
            });
        }
    }
}
