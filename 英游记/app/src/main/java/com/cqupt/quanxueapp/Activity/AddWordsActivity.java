package com.cqupt.quanxueapp.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.cqupt.quanxueapp.Base.BaseActivity;
import com.cqupt.quanxueapp.R;
import com.cqupt.quanxueapp.SQLiteHelper.DictionaryHelper;
import com.cqupt.quanxueapp.Utils.AddBookUtils;
import com.cqupt.quanxueapp.Utils.DialogUtils;
import com.cqupt.quanxueapp.Utils.NotificationUtils;
//import com.cqupt.quanxueapp.Utils.NotificationUtils;
//import com.cqupt.quanxueapp.Utils.ToastUtils;

public class AddWordsActivity extends BaseActivity implements View.OnClickListener {
    private android.widget.EditText mEditEnglish;
    private android.widget.EditText mEditChinese;
    private Button mBtnAdd;

    DictionaryHelper mSQHelper;
    SQLiteDatabase mSQLiteDatabase;
    ContentValues mContentValues;
    private Button mBtnMschool;
    private Button mBtnHschool;
    private Button mBtnUni4;
    private Button mBtnUni6;
    private android.widget.Spinner mSpinnerPort;

    @Override
    protected void initData() {
        mSQHelper = new DictionaryHelper(mContext);
        mSQLiteDatabase = mSQHelper.getWritableDatabase();

        mBtnAdd.setOnClickListener(this);
        mBtnMschool.setOnClickListener(this);
        mBtnHschool.setOnClickListener(this);
        mBtnUni4.setOnClickListener(this);
        mBtnUni6.setOnClickListener(this);
    }


    @SuppressLint("SetTextI18n")
    private void showDialog(final String bookName) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_finish, null);
        final Dialog dialog = DialogUtils.show(mContext, view);
        TextView mTxvTitle = view.findViewById(R.id.txv_title);
        TextView mTxvText = view.findViewById(R.id.txv_text);
        Button mBtnYes = view.findViewById(R.id.btn_yes);
        Button mBtnNo = view.findViewById(R.id.btn_no);
        mTxvTitle.setText("????????????");
        mTxvText.setText("????????????" + bookName + "??????????????????????????????");
        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                AddBookUtils.add(mContext, bookName);
                NotificationUtils.show(mContext,
                        "???????????????",
                        bookName + "????????????????????????????????????????????????????????????"
                        , ChooseE2CActivity.class);
                dialog.dismiss();
            }
        });
        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_mschool://????????????
                showDialog("??????");
                break;
            case R.id.btn_hschool://????????????
                showDialog("??????");
                break;
            case R.id.btn_uni4://??????????????????
                showDialog("????????????");
                break;
            case R.id.btn_uni6://??????????????????
                showDialog("????????????");
                break;
            case R.id.btn_add://????????????
                String english = mEditEnglish.getText().toString();
                String chinese = mEditChinese.getText().toString();
                String port = mSpinnerPort.getSelectedItem().toString();
                if (!TextUtils.isEmpty(english) && !TextUtils.isEmpty(chinese)) {
                    if (!port.equals("??????"))
                        initSQLiteData(english, port + chinese);
                } else
                    Toast.makeText(mContext,"????????????????????????????????????",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void initSQLiteData(String english, String chinese) {
        mContentValues = new ContentValues();
        mContentValues.put(DictionaryHelper.ENGLISH, english);
        mContentValues.put(DictionaryHelper.CHINESE, chinese);
        if (mSQLiteDatabase.insert(DictionaryHelper.TABLE_NAME, null, mContentValues) == -1) {
            Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "???????????????", Toast.LENGTH_SHORT).show();
            mEditEnglish.setText("");
            mEditChinese.setText("");
        }
    }

    @Override
    protected int initLayout() {
        return R.layout.layout_addwords;
    }

    @Override
    protected void initView() {
        mEditEnglish = findViewById(R.id.edit_english);
        mEditChinese = findViewById(R.id.edit_from);
        mBtnAdd = findViewById(R.id.btn_add);
        mBtnMschool = findViewById(R.id.btn_mschool);
        mBtnHschool = findViewById(R.id.btn_hschool);
        mBtnUni4 = findViewById(R.id.btn_uni4);
        mBtnUni6 = findViewById(R.id.btn_uni6);
        mSpinnerPort = findViewById(R.id.spinner_port);
    }

}