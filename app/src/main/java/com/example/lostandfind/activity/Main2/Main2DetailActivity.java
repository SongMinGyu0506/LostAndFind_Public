package com.example.lostandfind.activity.Main2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.data.LostPostInfo;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.query.main.MainInspectQuery;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Main2DetailActivity extends AppCompatActivity {
    LostPostInfo lostPostInfo;

    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();

    TextView title, contents, location, lostDate, postDate, category;
    ImageView image;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_detail);

        initializeView(); //view 초기화
        setActionbar();     //Actionbar 관련 설정
        getIntentData();    //넘어오는 Intent data get
        setStorageImage(lostPostInfo, image);   //image get, imageView set
        setTextView();  //TextView set
    }

    public void initializeView()
    {
        toolbar = findViewById(R.id.toolbar);
        title = (TextView)findViewById(R.id.title);
        contents = (TextView)findViewById(R.id.contents);
        location = (TextView)findViewById(R.id.location);
        lostDate = (TextView)findViewById(R.id.lostDate);
        postDate = (TextView)findViewById(R.id.postDate);
        category = (TextView)findViewById(R.id.category);
        image = (ImageView)findViewById(R.id.image);
    }

    private void setActionbar(){
        setSupportActionBar(toolbar);
        // 툴바 - 뒤로 가기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
    }

    private void setTextView(){
        title.setText(lostPostInfo.getTitle());
        contents.setText(lostPostInfo.getContents());
        location.setText(lostPostInfo.getLocation());
        lostDate.setText(lostPostInfo.getLostDate());
        postDate.setText(lostPostInfo.getPostDate());
        category.setText(lostPostInfo.getCategory());
    }

    private void getIntentData(){
        Intent intent = getIntent();
        //객체로 받아옴
        lostPostInfo = (LostPostInfo)intent.getSerializableExtra("lostPostInfo");
    }


    public void setStorageImage(LostPostInfo lostPostInfo, ImageView image) {
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child("photo/"+lostPostInfo.getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getApplicationContext()).load(uri).into(image);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                image.setImageResource(R.drawable.kumoh_symbol);
            }
        });
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