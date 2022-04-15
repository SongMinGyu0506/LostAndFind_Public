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

    TextView title, text, textView6, location, date, email, name, textView11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inspect);

        title = (TextView)findViewById(R.id.title);
        text = (TextView)findViewById(R.id.text);
//        textView6 = (TextView)findViewById(R.id.textView6);
        location = (TextView)findViewById(R.id.location);
        date = (TextView)findViewById(R.id.date);
        email = (TextView)findViewById(R.id.email);
//        name = (TextView)findViewById(R.id.textView10);
//        textView11 = (TextView)findViewById(R.id.textView11);

        intent = getIntent();
        post = (Post)intent.getSerializableExtra("post");

        title.setText(post.getTitle()); //
        text.setText(post.getText()); //
//        textView6.setText(post.getTime());
        location.setText(post.getGetting_item_place()); //
        date.setText(post.getGetting_item_time()); //
        email.setText(post.getUser_email()); //
//        name.setText(post.getUser_name());
//        textView11.setText(post.getUser_UID());
    }
}