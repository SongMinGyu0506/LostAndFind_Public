package com.example.lostandfind.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.login.LoginActivity;
import com.example.lostandfind.activity.my.DeleteAccountActivity;
import com.example.lostandfind.activity.my.ModifyNickActivity;
import com.example.lostandfind.activity.my.ModifyPwActivity;
import com.example.lostandfind.activity.my.NoticeActivity;
import com.example.lostandfind.activity.my.SetupAlertActivity;
import com.example.lostandfind.activity.my.SetupDisturbActivity;

public class MyFragment extends Fragment {
    ViewGroup rootView;

    Button modify_nick_btn;
    TextView notice_btn;

    TextView modify_pw_btn;
    TextView setup_alert_btn;
    TextView setup_disturb_btn;

    TextView logout_btn;
    TextView delete_account_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_my, container, false);

        // 공지사항을 누르면, 공지사항에 대한 페이지 실행
        notice_btn = rootView.findViewById(R.id.notice_btn);
        notice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
            }
        });

        // 닉네임 수정 버튼을 누르면, 닉네임을 수정하는 페이지 실행
        modify_nick_btn = rootView.findViewById(R.id.modify_nick_btn);
        modify_nick_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ModifyNickActivity.class);
                startActivity(intent);
            }
        });

        // 비밀번호 변경 누르면, 비밀번호 변경에 대한 페이지 실행
        modify_pw_btn = rootView.findViewById(R.id.modify_pw_btn);
        modify_pw_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ModifyPwActivity.class);
                startActivity(intent);
            }
        });

        // 알림 설정 누르면, 알림 설정에 대한 페이지 실행
        setup_alert_btn = rootView.findViewById(R.id.setup_alert_btn);
        setup_alert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetupAlertActivity.class);
                startActivity(intent);
            }
        });

        // 방해금지 시간 설정 누르면, 방해금지 시간을 설정할 수 있는 페이지 실행
        setup_disturb_btn = rootView.findViewById(R.id.setup_disturb_btn);
        setup_disturb_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SetupDisturbActivity.class);
                startActivity(intent);
            }
        });

        // [완료] 로그아웃 처리
        logout_btn = rootView.findViewById(R.id.logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("로그아웃 알림")
                        .setMessage("정말 로그아웃 하시겠어요?")
                        .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                Toast.makeText
                                        (getActivity(),
                                        "안녕히 가세요!",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        // 탈퇴하기 페이지 이동
        delete_account_btn = rootView.findViewById(R.id.delete_account_btn);
        delete_account_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DeleteAccountActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}