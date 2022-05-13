package com.example.lostandfind.activity.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.chatting.MainChattingActivity;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.query.main.MainInspectQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

//TODO: [작성자:송민규] 임시로 메인 엑티비티에서 값만 넘긴상태, 추후 UI 및 데이터 가공작업 필수
//상세내용 액티비티
public class MainInspectActivity extends AppCompatActivity {
    Intent intent;
    Post post;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView title, text, textView6, location, date, email, name, textView11;
    Button chatBtn;
    TextView btnDelete,btnUpdate;
    ImageView image;
    Toolbar toolbar;

    MainInspectQuery mainInspectQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_inspect);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 툴바 - 뒤로 가기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        mainInspectQuery = new MainInspectQuery(this);

        title = (TextView)findViewById(R.id.title);
        text = (TextView)findViewById(R.id.text);
//        textView6 = (TextView)findViewById(R.id.textView6);
        location = (TextView)findViewById(R.id.location);
        image = (ImageView)findViewById(R.id.image);
        date = (TextView)findViewById(R.id.date);
        email = (TextView)findViewById(R.id.email);
//        name = (TextView)findViewById(R.id.textView10);
//        textView11 = (TextView)findViewById(R.id.textView11);

        btnDelete = (TextView) findViewById(R.id.btnDelete);
        btnUpdate = (TextView) findViewById(R.id.btnUpdate);
        chatBtn = (Button)findViewById(R.id.chatBtn);

        intent = getIntent();
        post = (Post)intent.getSerializableExtra("post");

        title.setText(post.getTitle()); //
        text.setText(post.getContents()); //
//        textView6.setText(post.getTime());
        location.setText(post.getLocation()); //
        date.setText(post.getLostDate()); //
        email.setText(post.getWriterEmail()); //
//        name.setText(post.getUser_name());
//        textView11.setText(post.getUser_UID());
        mainInspectQuery.getStorageImage(post,image);

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainInspectActivity.this, MainChattingActivity.class);
                intent.putExtra("user_email",post.getWriterEmail());
                startActivity(intent);
            }
        });
        btnUpdate.setOnClickListener(onClickListener);
        btnDelete.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnUpdate:
                    updatePost();
                    break;
                case R.id.btnDelete:
                    deletePost();
                    break;
            }
        }
    };

    private void deletePost() {
        db.collection("Posts").document(post.getId())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainInspectActivity.this, "Post deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    private void updatePost() {

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