package com.example.lostandfind.activity.Main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lostandfind.R;
import com.example.lostandfind.data.LostPostInfo;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.query.main.MainInspectQuery;

public class Main2InspectActivity extends AppCompatActivity {
    Intent intent;
    LostPostInfo post;

    TextView title2, text2, textView6, location2, date2, email2, name2, textView11;
    ImageView image2;
    Toolbar toolbar2;

    MainInspectQuery mainInspectQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_inspect);

        toolbar2 = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar2);
        // 툴바 - 뒤로 가기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        mainInspectQuery = new MainInspectQuery(this);

        title2 = (TextView)findViewById(R.id.title2);
        text2 = (TextView)findViewById(R.id.text2);
//        textView6 = (TextView)findViewById(R.id.textView6);
        location2 = (TextView)findViewById(R.id.location2);
        image2 = (ImageView)findViewById(R.id.image2);
        date2 = (TextView)findViewById(R.id.date2);
        email2 = (TextView)findViewById(R.id.email2);
//        name = (TextView)findViewById(R.id.textView10);
//        textView11 = (TextView)findViewById(R.id.textView11);

        intent = getIntent();
        post = (LostPostInfo)intent.getSerializableExtra("lostpost");

        title2.setText(post.getTitle()); //
        text2.setText(post.getContents()); //
//        textView6.setText(post.getTime());
        location2.setText(post.getLocation()); //
        date2.setText(post.getLostDate()); //
        //email2.setText(post.get); //
//        name.setText(post.getUser_name());
//        textView11.setText(post.getUser_UID());
        mainInspectQuery.getStorageImage2(post,image2);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}