package com.cqupt.quanxueapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cqupt.quanxueapp.Bean.User;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegitActivity extends AppCompatActivity {

    EditText et_user, et_psw, et_pswagain;
    Toolbar toolbar;
    Button btn_regist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        btn_regist = findViewById(R.id.btn_regist);
        et_user = (EditText) findViewById(R.id.et_user_regist);
        et_psw = (EditText) findViewById(R.id.et_psw_regist);
        et_pswagain = (EditText) findViewById(R.id.et_psw_again);
        toolbar = (Toolbar) findViewById(R.id.tb_resgit);

        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_num=et_user.getText().toString();
                String user_password=et_psw.getText().toString().trim();
                String user_passwordagain = et_pswagain.getText().toString().trim();
                // 非空验证
                if (user_num.isEmpty() || user_password.isEmpty()) {
                    Toast.makeText(RegitActivity.this, "账号或密码不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(user_password.length()<8||user_password.length()>16){
                    Toast.makeText(RegitActivity.this, "密码长度不能小于8位", Toast.LENGTH_SHORT).show();
                }
                else if (!user_password.equals(user_passwordagain)) {
                    Toast.makeText(RegitActivity.this, "两个密码不一致!", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    // 使用BmobSDK提供的注册功能
                    User myUser=new User();
                    myUser.setUsername(user_num);
                    myUser.setPassword(user_password);
                    myUser.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e==null){
                                Toast.makeText(RegitActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(RegitActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                RegitActivity.this.finish();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegitActivity.this, LoginActivity.class);
                startActivity(intent);
                RegitActivity.this.finish();
            }
        });
    }
}
