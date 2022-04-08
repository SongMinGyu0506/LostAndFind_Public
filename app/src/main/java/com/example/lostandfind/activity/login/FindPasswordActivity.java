package com.example.lostandfind.activity.login;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FindPasswordActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText etFindEmail;
    Button btnFindEmail;
    String temp_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        firebaseAuth = FirebaseAuth.getInstance();

        etFindEmail = (EditText) findViewById(R.id.etFindEmail);
        btnFindEmail = (Button) findViewById(R.id.btnFindEmail);
        btnFindEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp_email = etFindEmail.getText().toString();
                firebaseAuth.sendPasswordResetEmail(temp_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(FindPasswordActivity.this);
                            dlg.setTitle("이메일 전송");
                            dlg.setMessage("가입하신 이메일로 비밀번호 재전송 메일이 발송되었습니다.");
                            dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(TAG,"Developer Message: Success reset Password Dialog");
                                    FindPasswordActivity.this.finish();
                                }
                            });
                            dlg.show();
                        } else {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(FindPasswordActivity.this);
                            dlg.setTitle("이메일 전송 에러");
                            dlg.setMessage("이메일을 다시 확인해주시기 바랍니다.");
                            dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(TAG,"Developer Message: Failed reset Password Dialog");
                                }
                            });
                            dlg.show();
                        }
                    }
                });
            }
        });
    }
}