package com.cqupt.quanxueapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cqupt.quanxueapp.Bean.User;
import com.cqupt.quanxueapp.R;
import com.cqupt.quanxueapp.Utils.DialogUtils;
import com.cqupt.quanxueapp.Utils.JumpUtils;
import com.cqupt.quanxueapp.Utils.OverWordsNumUtils;
import com.cqupt.quanxueapp.Utils.SPUtils;
import com.cqupt.quanxueapp.Utils.SQLiteUtils;
import com.cqupt.quanxueapp.Activity.ChooseC2EActivity;
import com.cqupt.quanxueapp.Activity.ChooseE2CActivity;
import com.cqupt.quanxueapp.Activity.DictionaryActivity;
import com.cqupt.quanxueapp.Activity.FillC2EActivity;
import com.cqupt.quanxueapp.Activity.FillE2CActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.bmob.v3.BmobUser;


public class HomeFragment extends Fragment implements View.OnClickListener{

    public static final String GOAL = "goal";
    public static final String DATA = "date";

    private View root;
    private TextView tv_time,tv_goalset,mTxvTodayAims,mTxvTodayCompleted;
    private Context mContext;
    private String failureTime = "2021-12-25 00:00:00";
    private ProgressBar mProgressRate;
    private TextView mTxvDictionaryWordsNumber;
    private Button mBtnModeChoose;
    private Button mBtnModeChoose2;
    private Button mBtnModeFill;
    private Button mBtnModeFill2;
    private LinearLayout mLlDictionary;

    @SuppressLint("HandlerLeak")
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            setTime();
            //在handleMessage发送handle消息实现无限刷新
            handler.sendMessageDelayed(handler.obtainMessage(), 1000); // 延迟一秒发送，1秒刷新一次
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(root == null) {
            root = inflater.inflate(R.layout.fragment_home, container, false);
        }
        initView(root);
        initGandT();
        onStart();
        mBtnModeChoose.setOnClickListener(this);
        mBtnModeChoose2.setOnClickListener(this);
        mBtnModeFill.setOnClickListener(this);
        mBtnModeFill2.setOnClickListener(this);
        mLlDictionary.setOnClickListener(this);
        return root;
    }
    //获取Bmob上保存的数据
    @SuppressLint("SetTextI18n")
    private void initGandT() {
        String goal,goaltime;
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            if(user.getGoal() !=null){
                goal = user.getGoal();
            }else {
                goal = (String) SPUtils.get(mContext, GOAL, "考试");
            }
            if(user.getGoaltime() !=null){
                goaltime = user.getGoaltime();
            }else {
                goaltime = (String) SPUtils.get(mContext, DATA, "2021-12-25 00:00:00");
            }
        } else {
            goal = (String) SPUtils.get(mContext, GOAL, "考试");
            goaltime = (String) SPUtils.get(mContext, DATA, "2021-12-25 00:00:00");
        }
        tv_goalset.setText("距离"+goal+"还剩:");
        failureTime = goaltime;
    }

    private void initView(View view) {
        mTxvTodayAims = view.findViewById(R.id.txv_today_aims);
        mTxvTodayCompleted = view.findViewById(R.id.txv_today_completed);
        mProgressRate = view.findViewById(R.id.progress_rate);
        mTxvDictionaryWordsNumber = view.findViewById(R.id.txv_dictionary_words_number);
        mBtnModeChoose = view.findViewById(R.id.btn_mode_choose);
        mBtnModeChoose2 = view.findViewById(R.id.btn_mode_choose2);
        mBtnModeFill = view.findViewById(R.id.btn_mode_fill);
        mBtnModeFill2 = view.findViewById(R.id.btn_mode_fill2);
        mLlDictionary = view.findViewById(R.id.ll_dictionary);
        tv_time = view.findViewById(R.id.tv_time);
        tv_goalset = view.findViewById(R.id.tv_goal);
        handler.sendMessage(handler.obtainMessage()); // 在这里开始时间计时
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0);
        Date date = calendar.getTime();
        mTxvTodayCompleted.setText(SPUtils.get(mContext, OverWordsNumUtils.getDate(date), 0) + "");
        // 获取每日目标
        mTxvTodayAims.setText(String.valueOf(SPUtils.get(mContext, "everyday_aims", 0)));
        // 进度条比例计算
        int bili = (int) ((Float.parseFloat(mTxvTodayCompleted.getText().toString()) / Float.parseFloat(mTxvTodayAims.getText().toString())) * 100);
        mProgressRate.setProgress(bili);
        // 词库单词数获取
        mTxvDictionaryWordsNumber.setText(String.valueOf(SQLiteUtils.cursorCount(mContext)));
        super.onStart();
    }

    public void setDate(String string){
        failureTime = string;
    }

    @SuppressLint("SetTextI18n")
    public void setGoal(String string){
        tv_goalset.setText("距离"+string+"还剩");
    }

    @SuppressLint("SetTextI18n")
    private void setTime() {
        @SuppressLint("SimpleDateFormat")
        // ★★★格式("yyyy-MM-dd HH:mm:ss")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // Date nowTime = new Date(System.currentTimeMillis()); //获取当前时间的另一种
            long nowTime = System.currentTimeMillis(); //获取当前时间
            Date overTime = simpleDateFormat.parse(failureTime); //利用SimpleDateFormat来把字符串日期转换为Date对象类型
            long a = overTime.getTime() - nowTime; // 计算总的时间差（毫秒级别）
            if (a > 0) { //如果大于零 说明时间有剩余
                long day = a / (1000 * 60 * 60 * 24);
                long hour = (a - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
                long minute = (a - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60)) / (1000 * 60);
                long second = (a - day * (1000 * 60 * 60 * 24) - hour * (1000 * 60 * 60) - minute * (1000 * 60)) / 1000;
                //刷新ui显示
                tv_time.setText( day + "天" + hour + "时" + minute + "分" + second + "秒");
            }else {
                Toast.makeText(mContext, "小于等于零", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //词库
            case R.id.ll_dictionary://查询词库
                if (SQLiteUtils.cursorCount(mContext) != 0)
                    JumpUtils.To(mContext, DictionaryActivity.class);
                else
                    DialogUtils.show(mContext, "词库内没有单词，请先添加！", "去添加单词");
                break;
            //四种模式
            case R.id.btn_mode_choose://选择 英-中
                if (SQLiteUtils.cursorCount(mContext) > 4)
                    JumpUtils.To(mContext, ChooseE2CActivity.class);
                else
                    DialogUtils.show(mContext, "词库内单词低于5个，无法进入此模式！请先添加！", "去添加单词");
                break;
            case R.id.btn_mode_choose2://选择 中-英
                if (SQLiteUtils.cursorCount(mContext) > 4)
                    JumpUtils.To(mContext, ChooseC2EActivity.class);
                else
                    DialogUtils.show(mContext, "词库内单词低于5个，无法进入此模式！请先添加！", "去添加单词");
                break;
            case R.id.btn_mode_fill://填写 英-中
                if (SQLiteUtils.cursorCount(mContext) != 0)
                    JumpUtils.To(mContext, FillE2CActivity.class);
                else
                    DialogUtils.show(mContext, "词库内没有单词，请先添加！", "去添加单词");
                break;
            case R.id.btn_mode_fill2://填写 中-英
                if (SQLiteUtils.cursorCount(mContext) != 0)
                    JumpUtils.To(mContext, FillC2EActivity.class);
                else
                    DialogUtils.show(mContext, "词库内没有单词，请先添加！", "去添加单词");
                break;
        }
    }
}