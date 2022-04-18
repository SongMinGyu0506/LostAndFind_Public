package com.example.lostandfind.activity.my;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ModifyNickActivity extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    EditText etModifyNickname;
    Button btnModifyNickname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_nick);

        etModifyNickname = (EditText) findViewById(R.id.etModifyNickname);
        btnModifyNickname = (Button)findViewById(R.id.btnModifyNickname);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");

        etModifyNickname.setText(name);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String temp_user_uid = user.getUid();

        btnModifyNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference DRef = db.collection("Users").document(temp_user_uid);
                DRef.update("name",etModifyNickname.getText().toString());
                new AlertDialog.Builder(ModifyNickActivity.this)
                        .setTitle("닉네임 변경")
                        .setMessage("닉네임 변경이 완료되었습니다.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ModifyNickActivity.this, MainActivity.class);
                                finish();
                                startActivity(intent);

                            }
                        }).show();
            }
        });
    }
}