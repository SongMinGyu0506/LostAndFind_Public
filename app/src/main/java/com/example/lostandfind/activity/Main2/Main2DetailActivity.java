package com.example.lostandfind.activity.Main2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.activity.login.LoginActivity;
import com.example.lostandfind.data.LostPostInfo;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.data.UserData;
import com.example.lostandfind.query.main.MainInspectQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Main2DetailActivity extends AppCompatActivity {
    private final static String TAG = "Main2DetailActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    LostPostInfo lostPostInfo;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    TextView title, contents, location, lostDate, postDate, category;
    ImageView image;
    Toolbar toolbar;

    TextView update_btn, delete_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_detail);

        initializeView(); //view 초기화
        setActionbar();     //Actionbar 관련 설정
        getIntentData();    //넘어오는 Intent data get

        String temp_email = user.getEmail();
        String lostPostInfoEmail = lostPostInfo.getWriterEmail();
        try {
            if (!lostPostInfoEmail.equals(temp_email)) {
                update_btn.setEnabled(false);
                delete_btn.setEnabled(false);
            }
        } catch (Exception e) {
            update_btn.setEnabled(false);
            delete_btn.setEnabled(false);
            Log.e(TAG,"Developer Error Log: ",e);
        }

        setStorageImage(lostPostInfo, image);   //image get, imageView set
        setTextView();  //TextView set

        update_btn.setOnClickListener(onClickListener);
        delete_btn.setOnClickListener(onClickListener);
    }

    //버튼 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.update_btn:
                    updatePost();
                    break;
                case R.id.delete_btn:
                    deletePostAlert();
                    break;
            }
        }
    };

    private void deletePost(){
        db.collection("LostPosts").document(lostPostInfo.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            toast("post deleted");
                            finish();
                        }
                    }
                });
    }

    private void deletePostAlert() {
        Toast.makeText(getApplicationContext(), "되나?", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시글 삭제 알림");
        builder.setMessage("게시글을 삭제 하시겠습니까?");
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePost();
            }
        });
        builder.setNegativeButton("아니", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog ad = builder.create();
        ad.show();
    }

    private void updatePost() {
        finish();
        Intent intent = new Intent(this, Main2UpdateActivity.class);
        intent.putExtra("lostPostInfo", lostPostInfo);
        startActivity(intent);
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

        update_btn = (TextView)findViewById(R.id.update_btn);
        delete_btn = (TextView)findViewById(R.id.delete_btn);
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

    //토스트 메소드 간략화
    public void toast(String text){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}