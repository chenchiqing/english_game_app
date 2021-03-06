package com.cqupt.quanxueapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cqupt.quanxueapp.Activity.AddWordsActivity;
import com.cqupt.quanxueapp.Bean.MenuBean;
import com.cqupt.quanxueapp.R;
import com.cqupt.quanxueapp.Utils.DescriptionUtils;
import com.cqupt.quanxueapp.Utils.DialogUtils;
import com.cqupt.quanxueapp.Utils.JumpUtils;
import com.cqupt.quanxueapp.Utils.SPUtils;

import java.util.List;

public class PersonMenuAdapter extends RecyclerView.Adapter<PersonMenuAdapter.ViewHolder> {

    private Context mContext;
    private List<MenuBean> mList;

    public PersonMenuAdapter(Context context, List<MenuBean> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.mImvIcon.setImageResource(mList.get(position).getIcon());
        holder.mTxvText.setText(mList.get(position).getText());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        @SuppressLint("InflateParams") View view1 = LayoutInflater.from(mContext).inflate(R.layout.dialog_aims, null);
                        String[] numbers = new String[50];
                        for (int i = 0; i < 50; i++)
                            numbers[i] = String.valueOf(i * 25);
                        final NumberPicker mNumberPicker = view1.findViewById(R.id.number_picker);
                        Button mBtnModify = view1.findViewById(R.id.btn_modify);
                        final Dialog dialog = DialogUtils.show(mContext, view1);
                        mNumberPicker.setMinValue(0);
                        mNumberPicker.setMaxValue(45);
                        mNumberPicker.setDisplayedValues(numbers);
                        mNumberPicker.setValue((Integer) SPUtils.get(mContext, "everyday_aims", 0) / 25);
                        mBtnModify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                SPUtils.set(mContext, "everyday_aims", mNumberPicker.getValue() * 25);
                                Toast.makeText(mContext, "???????????????",Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case 1:
                        JumpUtils.To(mContext, AddWordsActivity.class);
                        break;
                    case 2:
                        DescriptionUtils.showDescription(mContext);
                        break;
                    case 3:
                        @SuppressLint("InflateParams") View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_about, null);
                        DialogUtils.show(mContext, view);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mImvIcon;
        private TextView mTxvText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mImvIcon = itemView.findViewById(R.id.imv_icon);
            mTxvText = itemView.findViewById(R.id.txv_text);
        }
    }
}
