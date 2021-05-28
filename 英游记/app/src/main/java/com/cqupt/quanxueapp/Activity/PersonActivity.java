package com.cqupt.quanxueapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.cqupt.quanxueapp.Base.BaseActivity;
import com.cqupt.quanxueapp.R;
import com.cqupt.quanxueapp.Bean.User;
import com.cqupt.quanxueapp.Utils.RealPathFromUriUtils;
import com.cqupt.quanxueapp.Utils.SPUtils;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PersonActivity extends BaseActivity implements View.OnClickListener {

    public static final String PERSON_NAME = "person_name";
    public static final String PERSON_MSG = "person_msg";

    private android.widget.ImageView mImvIcon;
    private android.widget.EditText mEditName;
    private android.widget.EditText mEditMsg;
    private Button mBtnSave;
    //private Uri uri;
    //private String img_path;

    @Override
    protected void initData() {
        getNameMsg();
        mBtnSave.setOnClickListener(this);
        mImvIcon.setOnClickListener(this);
    }

    private void getNameMsg() {
        // 获取设置的姓名和签名，如果没有就设置默认值
        String strName = (String) SPUtils.get(mContext, PERSON_NAME, "游客");
        String strMsg = (String) SPUtils.get(mContext, PERSON_MSG, "这个人很懒，什么也没留下！");
        mEditName.setText(strName);
        mEditMsg.setText(strMsg);
    }

    @Override
    protected int initLayout() {
        return R.layout.layout_person;
    }

    @Override
    protected void initView() {
        mImvIcon = findViewById(R.id.iv_icon);
        mEditName = findViewById(R.id.edit_name);
        mEditMsg = findViewById(R.id.edit_msg);
        mBtnSave = findViewById(R.id.btn_save);
    }

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_icon:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, 1);
                }
                Toast.makeText(mContext,"更换头像功能还在开发中噢！",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_save:
                if (!TextUtils.isEmpty(mEditName.getText().toString()) && !TextUtils.isEmpty(mEditMsg.getText().toString())) {
                    //保存昵称和签名
                    SPUtils.set(mContext, PERSON_NAME, mEditName.getText().toString());
                    SPUtils.set(mContext, PERSON_MSG, mEditMsg.getText().toString());
                    //重新获取一下
                    getNameMsg();
                    User user = User.getCurrentUser(User.class);
                    user.setNickname(mEditName.getText().toString());
                    user.setMassage(mEditMsg.getText().toString());
                    user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(mContext,"保存成功！",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "更新用户信息失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    Log.e("error", e.getMessage());
                                }
                            }
                        });
                } else
                    Toast.makeText(mContext,"您的昵称和签名不能为空哟！",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
