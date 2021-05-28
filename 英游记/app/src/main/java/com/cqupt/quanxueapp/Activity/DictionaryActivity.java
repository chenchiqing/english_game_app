package com.cqupt.quanxueapp.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cqupt.quanxueapp.Adapter.DictionaryAdapter;
import com.cqupt.quanxueapp.Base.BaseActivity;
import com.cqupt.quanxueapp.Bean.WordsBean;
import com.cqupt.quanxueapp.R;
import com.cqupt.quanxueapp.SQLiteHelper.DictionaryHelper;
import com.cqupt.quanxueapp.Utils.DialogUtils;
import com.cqupt.quanxueapp.Utils.NotificationUtils;
import com.cqupt.quanxueapp.Utils.SQLiteUtils;
//import com.cqupt.quanxueapp.Utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class DictionaryActivity extends BaseActivity implements View.OnClickListener {

    private RecyclerView mRecycleWords;
    private List<WordsBean> mList;
    private SQLiteDatabase mSQLiteDatabase;

    private Button mBtnDelete;
    private LinearLayoutManager mManager;
    private EditText mEditSearchWord;
    private Button mBtnSearch;
    private DictionaryAdapter mAdapter;

    @Override
    protected void initData() {
        mBtnSearch.setOnClickListener(this);
        mBtnDelete.setOnClickListener(this);
        DictionaryHelper SQWordsHelper = new DictionaryHelper(mContext);
        mSQLiteDatabase = SQWordsHelper.getWritableDatabase();

        //获取数据库中的数据,传入List
        getData();

        mAdapter = new DictionaryAdapter(mContext, mList);
        mRecycleWords.setAdapter(mAdapter);
        //布局管理器
        mManager = new LinearLayoutManager(mContext);
        mManager.setOrientation(RecyclerView.VERTICAL);
        mRecycleWords.setLayoutManager(mManager);
        //子项分割线
        mRecycleWords.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
    }
    //查找单词
    private void searchData(String str) {
        for (int i = 0; i < mList.size(); i++) {
            if (TextUtils.equals(str, mList.get(i).getWord())) {
                moveToPosition(i);
                break;
            } else if (i == mList.size() - 1)
                Toast.makeText(mContext,"词库中查无此“词”！",Toast.LENGTH_SHORT).show();
        }
    }

    //根据 position 跳转至RecycleView的某个子项位置
    private void moveToPosition(int position) {
        mAdapter.setItem(position);
        mManager.scrollToPositionWithOffset(position, 0);
        mManager.setStackFromEnd(true);
    }

    //获取词库数据
    private void getData() {
        mList = new ArrayList<>();
        @SuppressLint("Recycle") Cursor cursor = mSQLiteDatabase.query(DictionaryHelper.TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            WordsBean wordsBean = new WordsBean();
            wordsBean.setWord(cursor.getString(0));
            wordsBean.setChinese(cursor.getString(1));
            mList.add(wordsBean);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search://按单词查找
                if (!TextUtils.isEmpty(mEditSearchWord.getText().toString()))
                    searchData(mEditSearchWord.getText().toString());
                else
                    Toast.makeText(mContext,"查找的单词不能为空噢!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_delete://清空词库
                showDialog();
                break;
        }
    }

    //清空单词确定对话框
    @SuppressLint("SetTextI18n")
    private void showDialog() {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_finish, null);
        final Dialog dialog = DialogUtils.show(mContext, view);
        TextView mTxvTitle = view.findViewById(R.id.txv_title);
        TextView mTxvText = view.findViewById(R.id.txv_text);
        Button mBtnYes = view.findViewById(R.id.btn_yes);
        Button mBtnNo = view.findViewById(R.id.btn_no);
        mTxvTitle.setText("确定删除");
        mTxvText.setText("确定删除词库中所有单词吗？");
        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                NotificationUtils.show(mContext,
                        "温馨提示",
                        "整整删除了：" + SQLiteUtils.calern(mContext) + "个单词！词库中没有单词了哦！"
                        , AddWordsActivity.class);
                dialog.dismiss();
                finish();
            }
        });
        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected int initLayout() {
        return R.layout.layout_dictionary;
    }

    @Override
    protected void initView() {
        mRecycleWords = findViewById(R.id.recycle_words);
        mBtnDelete = findViewById(R.id.btn_delete);
        mEditSearchWord = findViewById(R.id.edit_search_word);
        mBtnSearch = findViewById(R.id.btn_search);
    }

}