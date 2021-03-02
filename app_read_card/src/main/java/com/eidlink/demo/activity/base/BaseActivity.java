package com.eidlink.demo.activity.base;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import pub.devrel.easypermissions.EasyPermissions;

public abstract class BaseActivity extends FragmentActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewId());
        initEvent();
    }

    protected abstract void initEvent();

    protected abstract int getViewId();

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 去申请权限
     */
    protected void requestPermissions() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
        } else {
            EasyPermissions.requestPermissions(this, "如无权限可能导致读取失败", 81, perms);
        }
    }

    /**
     * 权限申请成功的回调
     *
     * @param requestCode 申请权限时的请求码
     * @param perms       申请成功的权限集合
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    /**
     * 权限申请拒绝的回调
     *
     * @param requestCode 申请权限时的请求码
     * @param perms       申请拒绝的权限集合
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        showToast("获取读写sdk卡权限失败。");
    }

    public void startActivityNoFinish(Class<?> cls) {
        Intent intent =new Intent(getApplicationContext(), cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void showToast(String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected ProgressDialog progressDialog;

    protected void showProgressDialog(String msg) {
        try {
            if ((!isFinishing()) && (this.progressDialog == null)) {
                this.progressDialog = new ProgressDialog(this);
            }
            this.progressDialog.setMessage(msg);
            this.progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void closeProgressDialog() {
        try {
            if (this.progressDialog != null)
                this.progressDialog.dismiss();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
