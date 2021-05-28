package com.cqupt.quanxueapp.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cqupt.quanxueapp.Activity.Webactivity;
import com.cqupt.quanxueapp.R;

//休闲模块fragment
public class RelaxationFragment extends Fragment implements View.OnClickListener{

    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(root == null) {
            root = inflater.inflate(R.layout.fragment_relaxation, container, false);
        }
        Button btn_play = root.findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), Webactivity.class);
        startActivity(intent);
    }
}