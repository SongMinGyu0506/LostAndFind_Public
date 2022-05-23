package com.example.lostandfind.activity.Main2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.activity.chat.ChatActivity;
import com.example.lostandfind.chatDB.ChatRooms;
import com.example.lostandfind.data.LostPostInfo;
import com.example.lostandfind.data.UserData;
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
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    LostPostInfo lostPostInfo;
    String myName, myUID;
    ChatRooms chatRoom;
    Boolean exist;
    String chatRoomId;

    TextView title, contents, location, lostDate, postDate, name, category;
    ImageView image;
    Toolbar toolbar;

    TextView update_btn, delete_btn;
    Button chat_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2_detail);

        initializeView();                       //view 초기화
        setActionbar();                         //Actionbar 관련 설정
        getIntentData();                        //넘어오는 Intent data get
        setStorageImage(lostPostInfo, image);   //image get, imageView set
        setTextView();                          //TextView set

        getUserData();
        getRoomId();
        update_btn.setOnClickListener(onClickListener);
        delete_btn.setOnClickListener(onClickListener);
        chat_btn.setOnClickListener(onClickListener);

        String temp_email = user.getEmail();
        String lostPostInfoEmail = lostPostInfo.getWriterEmail();
        try {
            if (!lostPostInfoEmail.equals(temp_email)) {
                update_btn.setVisibility(View.INVISIBLE);
                delete_btn.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            update_btn.setEnabled(false);
            delete_btn.setEnabled(false);
            Log.e(TAG,"Developer Error Log: ",e);
        }

        //setBtnVisibility();
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
                case R.id.chat_btn:
                    enterChat();
                    break;
            }
        }
    };

//    private void setBtnVisibility(){
//        String writerUID = lostPostInfo.getWriterUID();
//        if (writerUID.equals(user.getUid()) == false){
//            update_btn.setVisibility(View.INVISIBLE);
//            delete_btn.setVisibility(View.INVISIBLE);
//        }
//    }

    private void getRoomId(){
        if (lostPostInfo.getWriterUID().equals(user.getUid()) == false) {    //본인이 작성한 글은 채팅걸기를 막음
            db.collection("ChatRoom").document(user.getUid())
                    .collection("userRoom")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    ChatRooms chatRooms = document.toObject(ChatRooms.class); //여기
                                    chatRooms.setId(document.getId());
                                    exist = (chatRooms.getReceiverUID()).equals(lostPostInfo.getWriterUID());   //같으면 true 다르면 false
                                    if (exist) {
                                        chatRoom = chatRooms;
                                        chatRoomId = chatRoom.getId();
                                    }

                                    Log.d(TAG, "aryexist: " + exist);
                                    Log.d(TAG, "aryid: " + chatRoomId);
                                    Log.d(TAG, "ary: " + chatRoom.getReceiverName());
                                    Log.d(TAG, "ary: " + chatRoom.getReceiverUID());
                                    Log.d(TAG, "arychatroom: " + chatRoom.getSenderName());
                                }
                            } else {
                                Log.w(TAG, "Error getting documents.", task.getException());
                            }
                        }
                    });
        }
    }

    public void getUserData() {
        DocumentReference dRef = db.collection("Users").document(user.getUid());
        dRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            myName = (documentSnapshot.toObject(UserData.class).getName());
                            myUID = (documentSnapshot.toObject(UserData.class).getUID());
                            Log.d(TAG, "get sender name: "+myName);
                            Log.d(TAG, "get sender id: "+myUID);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG,"Developer: Error: ",e);
                    }
                });
    }

    private void enterChat(){
        if (lostPostInfo.getWriterUID().equals(myUID) == false){    //본인이 작성한 글은 채팅걸기를 막음
            Intent intent = new Intent(this, ChatActivity.class);

            ChatRooms chatRoomUserData = new ChatRooms(lostPostInfo.getWriterUID(), lostPostInfo.getName(),
                    myUID, myName);
            intent.putExtra("chatRoomUserData", chatRoomUserData);
            intent.putExtra("exist", exist);
            intent.putExtra("roomId", chatRoomId);
            startActivity(intent);
        }
    }

    private void deletePost(){
        db.collection("LostPosts").document(lostPostInfo.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            toast("게시글 삭제 완료");
                            finish();
                        }
                    }
                });
    }

    private void deletePostAlert() {
//        Toast.makeText(getApplicationContext(), "되나?", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("게시글 삭제 알림");
        builder.setMessage("게시글을 정말로 삭제하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletePost();
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
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
        name = (TextView)findViewById(R.id.name);
        category = (TextView)findViewById(R.id.category);
        image = (ImageView)findViewById(R.id.image);

        update_btn = (TextView)findViewById(R.id.update_btn);
        delete_btn = (TextView)findViewById(R.id.delete_btn);
        chat_btn = (Button)findViewById(R.id.chat_btn);
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
        name.setText(lostPostInfo.getName());
        category.setText(lostPostInfo.getCategory());
        //toast("uid: "+lostPostInfo.getWriterUID());
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