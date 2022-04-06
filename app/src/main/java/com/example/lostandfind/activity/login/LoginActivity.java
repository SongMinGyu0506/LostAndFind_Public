package com.example.lostandfind.activity.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText inputID, inputPW;
    private Button logIn, signUp;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        logIn = (Button)findViewById(R.id.btn_LogIn);
        signUp = (Button)findViewById(R.id.btn_SignUp);
        inputID = (EditText)findViewById(R.id.inputText_ID);
        inputPW = (EditText)findViewById(R.id.inputText_PW);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.setLanguageCode("ko");
        user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseAuth.signOut();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputID.getText().toString().length() == 0 || inputPW.getText().toString().length() == 0) {
                    Toast.makeText(LoginActivity.this,"이메일, 패스워드를 전부 입력해주세요",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    String email = inputID.getText().toString().trim();
                    String pw = inputPW.getText().toString().trim();

                    firebaseAuth.signInWithEmailAndPassword(email,pw)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (task.isSuccessful()) {
                                        if (user.isEmailVerified()) {
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(LoginActivity.this,"Not Vertiy Email",Toast.LENGTH_SHORT).show();
                                        }
                                         // 추가해도 될까?
                                    } else {
                                        if (user == null) {
                                            Toast.makeText(LoginActivity.this,"사용자 계정 없음",Toast.LENGTH_SHORT).show();
                                            inputID.setText("");
                                            inputPW.setText("");
                                            return;
                                        }
                                        else if (user != null) {
                                            //Toast.makeText(LoginActivity.this,"",Toast.LENGTH_SHORT).show();
                                            Toast.makeText(LoginActivity.this,"비밀번호 오류",Toast.LENGTH_SHORT).show();
                                            inputPW.setText("");
                                            firebaseAuth.signOut();
                                            return;
                                        } else {
                                            Toast.makeText(LoginActivity.this,"로그인 실패",Toast.LENGTH_SHORT).show();
                                            inputID.setText("");
                                            inputPW.setText("");
                                            return;
                                        }

                                    }
                                }
                            });
                }
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}