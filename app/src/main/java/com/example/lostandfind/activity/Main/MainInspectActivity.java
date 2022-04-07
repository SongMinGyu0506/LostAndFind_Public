package com.example.lostandfind.activity.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lostandfind.R;
import com.example.lostandfind.data.Post;

//TODO: [작성자:송민규] 임시로 메인 엑티비티에서 값만 넘긴상태, 추후 UI 및 데이터 가공작업 필수
//상세내용 액티비티
public class MainInspectActivity extends AppCompatActivity {
    Intent intent;
    Post post;

    TextView textView4, textView5, textView6, textView7, textView8, textView9, textView10, textView11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inspect);

        textView4 = (TextView)findViewById(R.id.textView4);
        textView5 = (TextView)findViewById(R.id.textView5);
        textView6 = (TextView)findViewById(R.id.textView6);
        textView7 = (TextView)findViewById(R.id.textView7);
        textView8 = (TextView)findViewById(R.id.textView8);
        textView9 = (TextView)findViewById(R.id.textView9);
        textView10 = (TextView)findViewById(R.id.textView10);
        textView11 = (TextView)findViewById(R.id.textView11);

        intent = getIntent();
        post = (Post)intent.getSerializableExtra("post");

        textView4.setText(post.getTitle());
        textView5.setText(post.getText());
        textView6.setText(post.getTime());
        textView7.setText(post.getGetting_item_place());
        textView8.setText(post.getGetting_item_time());
        textView9.setText(post.getUser_email());
        textView10.setText(post.getUser_name());
        textView11.setText(post.getUser_UID());

    }
}