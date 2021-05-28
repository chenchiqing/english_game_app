package com.cqupt.quanxueapp.Utils;

import android.content.Context;
import android.widget.Toast;

import com.cqupt.quanxueapp.Bean.User;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class BmobUtils {

    //上传照片到Bmob
    public static void upload(String imgpath, Context mContext){
        final BmobFile bmobFile = new BmobFile(new File(imgpath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
                    //Log.w("bbb",bmobFile.getFileUrl());
                    Toast.makeText(mContext,"上传文件成功:" + bmobFile.getFileUrl(),Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(mContext,"上传文件失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
    public static void updata(Context mContext,User user){
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e!=null){
                    Toast.makeText(mContext,"上传文件失败：" + e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
