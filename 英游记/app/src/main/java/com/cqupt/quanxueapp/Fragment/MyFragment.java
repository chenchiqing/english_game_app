package com.cqupt.quanxueapp.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cqupt.quanxueapp.Adapter.PersonMenuAdapter;
import com.cqupt.quanxueapp.Bean.MenuBean;
import com.cqupt.quanxueapp.Bean.User;
import com.cqupt.quanxueapp.Fragment.HomeFragment;
import com.cqupt.quanxueapp.MainActivity;
import com.cqupt.quanxueapp.R;
import com.cqupt.quanxueapp.Utils.BmobUtils;
import com.cqupt.quanxueapp.Utils.JumpUtils;
import com.cqupt.quanxueapp.Activity.PersonActivity;
import com.cqupt.quanxueapp.Utils.SPUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobUser;

public class MyFragment extends Fragment {

    public static final String PERSON_NAME = "person_name";
    public static final String PERSON_MSG = "person_msg";

    public static final String GOAL = "goal";
    public static final String DATA = "date";

    private EditText etGoal;  //目标
    private EditText etDate;  //倒计时
    private Button btnTimeSet;
    private Button btnGoalset;
    private View root;
    private Context mContext;

    private RecyclerView mRecycleMenu;
    private List<MenuBean> mList;
    private TextView mTxvName;
    private TextView mTxvMsg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(root == null) {
            root = inflater.inflate(R.layout.fragment_mine, container, false);
        }
        initView(root);
        initMenuData();
        queryUser();
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        PersonMenuAdapter adapter = new PersonMenuAdapter(mContext, mList);
        mRecycleMenu.setAdapter(adapter);
        mRecycleMenu.setLayoutManager(layoutManager);
        //子项动画
        mRecycleMenu.setItemAnimator(new DefaultItemAnimator());
        //子项分割线
        mRecycleMenu.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        //日期
        btnOnClick(btnTimeSet);
        btnOnClick(btnGoalset);
        showDateOnClick(etDate);
        return root;
    }

    private void initMenuData() {
        mList = new ArrayList<>();

        MenuBean bean = new MenuBean();
        bean.setIcon(R.mipmap.img_modify);
        bean.setText("修改每日目标");
        mList.add(bean);

        bean = new MenuBean();
        bean.setIcon(R.mipmap.img_add);
        bean.setText("添加单词");
        mList.add(bean);

        bean = new MenuBean();
        bean.setIcon(R.mipmap.img_help);
        bean.setText("软件使用帮助");
        mList.add(bean);

        bean = new MenuBean();
        bean.setIcon(R.mipmap.img_about);
        bean.setText("关于");
        mList.add(bean);
    }

    private void initView(View view) {
        mTxvName = view.findViewById(R.id.txv_name);
        mTxvMsg = view.findViewById(R.id.txv_msg);
        //userIcon = view.findViewById(R.id.user_icon);
        mRecycleMenu = view.findViewById(R.id.recycle_menu);
        etDate = root.findViewById(R.id.time_setting);
        etGoal = root.findViewById(R.id.goal_setting);
        btnTimeSet = root.findViewById(R.id.btn_timeset);
        btnGoalset = root.findViewById(R.id.btn_goalset);
        LinearLayout llPerson = view.findViewById(R.id.ll_person);
        llPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JumpUtils.To(mContext, PersonActivity.class);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public void showDateOnClick(final EditText editText){
        //点击事件传递，接触，点击打开
        editText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg(editText);
                    return true;
                }
                return false;
            }
        });
        //改变日期
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickDlg(editText);
                }
            }
        });
    }

    //选择日期,改变文本
    protected void showDatePickDlg(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener(){
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth+" 00:00:00");
                Toast.makeText(mContext,"设置成功" , Toast.LENGTH_SHORT).show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    //倒计时设置
    @SuppressLint("NonConstantResourceId")
    public void btnOnClick(final Button btn){
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (btn.getId()) {
                    case R.id.btn_timeset:
                        MainActivity mainActivity = (MainActivity) getActivity();
                        assert mainActivity != null;
                        HomeFragment homeFragment = (HomeFragment) mainActivity.fragments.get(0);
                        homeFragment.setDate(etDate.getText().toString());
                        SPUtils.set(mContext, DATA, etDate.getText().toString());
                        User user = User.getCurrentUser(User.class);
                        user.setGoaltime(etDate.getText().toString());
                        BmobUtils.updata(mContext,user);
                        Toast.makeText(mContext,"设置成功",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.btn_goalset:
                        String string = etGoal.getText().toString();
                        if(string.isEmpty()){
                            Toast.makeText(mContext,"目标不能为空",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            mainActivity = (MainActivity) getActivity();
                            assert mainActivity != null;
                            homeFragment = (HomeFragment) mainActivity.fragments.get(0);
                            homeFragment.setGoal(etGoal.getText().toString());
                            SPUtils.set(mContext, GOAL, etGoal.getText().toString());
                            user = User.getCurrentUser(User.class);
                            user.setGoal(etGoal.getText().toString());
                            BmobUtils.updata(mContext,user);
                            Toast.makeText(mContext,"设置成功",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    //初始化头像、呢称、签名
    private void queryUser() {
        mTxvName.setText((String)SPUtils.get(mContext, PERSON_NAME, "游客"));
        mTxvMsg.setText((String) SPUtils.get(mContext,  PERSON_MSG, "这个人很懒，什么也没留下！"));
        if (BmobUser.isLogin()) {
            User user = BmobUser.getCurrentUser(User.class);
            String nickname = user.getNickname();
            String massage = user.getMassage();
            if (nickname !=null){
                mTxvName.setText(nickname);
            }else {
                mTxvName.setText((String)SPUtils.get(mContext, PERSON_NAME, "游客"));
            }
            if(massage != null){
                mTxvMsg.setText(massage);
            }else {
                mTxvMsg.setText((String) SPUtils.get(mContext,  PERSON_MSG, "这个人很懒，什么也没留下！"));
            }
        } else {
            mTxvName.setText((String)SPUtils.get(mContext, PERSON_NAME, "游客"));
            mTxvMsg.setText((String) SPUtils.get(mContext,  PERSON_MSG, "这个人很懒，什么也没留下！"));
        }
    }
}