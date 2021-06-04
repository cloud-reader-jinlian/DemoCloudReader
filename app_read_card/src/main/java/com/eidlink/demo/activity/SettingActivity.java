package com.eidlink.demo.activity;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.eidlink.demo.R;
import com.eidlink.demo.ReadCardManager;
import com.eidlink.demo.activity.base.BaseActivity;
import com.eidlink.demo.activity.utils.SpUtils;

public class SettingActivity extends BaseActivity {

    private RadioGroup rg_get_picture;
    private RadioButton rb_picture_no;
    private RadioButton rb_picture_yes;
    private Boolean mNeedPic;

    @Override
    protected void initEvent() {
        rg_get_picture = findViewById(R.id.rg_get_picture);
        rb_picture_no = findViewById(R.id.rb_picture_no);
        rb_picture_yes = findViewById(R.id.rb_picture_yes);
        mNeedPic = (Boolean) SpUtils.getParam(this, ReadCardManager.tag_need_add_picture, true);
        if (mNeedPic) {
            rb_picture_yes.setChecked(true);
        } else {
            rb_picture_no.setChecked(true);
        }
        rg_get_picture.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_picture_no:
                        SpUtils.setParam(SettingActivity.this, ReadCardManager.tag_need_add_picture, false);
                        ReadCardManager.eid.setReadPicture((Boolean) SpUtils.getParam(SettingActivity.this, ReadCardManager.tag_need_add_picture, true));
                        break;
                    case R.id.rb_picture_yes:
                        SpUtils.setParam(SettingActivity.this, ReadCardManager.tag_need_add_picture, true);
                        ReadCardManager.eid.setReadPicture((Boolean) SpUtils.getParam(SettingActivity.this, ReadCardManager.tag_need_add_picture, true));
                        break;
                }
            }
        });
    }

    @Override
    protected int getViewId() {
        return R.layout.activity_setting;
    }

}
