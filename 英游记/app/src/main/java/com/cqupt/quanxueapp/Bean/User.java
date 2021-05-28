package com.cqupt.quanxueapp.Bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class User extends BmobUser {
    //昵称
    private String nickname;
    //头像
    private BmobFile icon;
    //签名
    private String massage;
    //目标
    private String goal;
    //目标时间
    private String goaltime;

    public String getNickname() {
        return nickname;
    }

    public User setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public User setIcon(BmobFile icon) {
        this.icon = icon;
        return this;
    }

    public String getMassage(){
        return massage;
    }

    public User setMassage(String massage){
        this.massage = massage;
        return this;
    }

    public String getGoal() {
        return goal;
    }

    public User setGoal(String goal) {
        this.goal = goal;
        return this;
    }

    public String getGoaltime() {
        return goaltime;
    }

    public User setGoaltime(String gaoltime) {
        this.goaltime = gaoltime;
        return this;
    }
}
