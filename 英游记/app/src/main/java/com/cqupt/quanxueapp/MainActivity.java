package com.cqupt.quanxueapp;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cqupt.quanxueapp.Adapter.FragmentPagerAdapter;
import com.cqupt.quanxueapp.Base.BaseActivity;
import com.cqupt.quanxueapp.Fragment.HomeFragment;
import com.cqupt.quanxueapp.Fragment.MyFragment;
import com.cqupt.quanxueapp.Fragment.RelaxationFragment;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    ViewPager2 viewPager;
    private ImageView iv_relaxation,iv_home,iv_mine,iv_current;
    public ArrayList<Fragment> fragments= new ArrayList<>();


    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(){
        LinearLayout llrelaxation = findViewById(R.id.bottom_relaxation);
        llrelaxation.setOnClickListener(this);
        LinearLayout llhome = findViewById(R.id.bottom_home);
        llhome.setOnClickListener(this);
        LinearLayout llmine = findViewById(R.id.bottom_mine);
        llmine.setOnClickListener(this);
        iv_relaxation = findViewById(R.id.iv_relaxation);
        iv_home = findViewById(R.id.iv_home);
        iv_mine = findViewById(R.id.iv_mine);

        iv_home.setSelected(true);
        iv_current = iv_home;
    }
    @Override
    protected void initData(){
        viewPager = findViewById(R.id.vp);
        fragments.add(new HomeFragment());
        fragments.add(new RelaxationFragment());
        fragments.add(new MyFragment());
        FragmentStateAdapter pageradapter = new FragmentPagerAdapter(getSupportFragmentManager(),getLifecycle(),fragments);
        viewPager.setAdapter(pageradapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            //滑动动画
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            //响应页面改变
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                changeTabe(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    //改变底部导航栏颜色和切换Fragment
    @SuppressLint("NonConstantResourceId")
    private void changeTabe(int position) {
        iv_current.setSelected(false);
        switch (position){
            case R.id.bottom_home:
                viewPager.setCurrentItem(0);
            case 0:
                iv_home.setSelected(true);
                iv_current = iv_home;
                break;
            case R.id.bottom_relaxation:
                viewPager.setCurrentItem(1);
            case 1:
                iv_relaxation.setSelected(true);
                iv_current = iv_relaxation;
                break;
            case R.id.bottom_mine:
                viewPager.setCurrentItem(2);
            case 2:
                iv_mine.setSelected(true);
                iv_current = iv_mine;
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View view) {
        changeTabe(view.getId());
    }
    
}
