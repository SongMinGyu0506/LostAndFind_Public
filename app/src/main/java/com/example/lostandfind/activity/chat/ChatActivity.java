package com.example.lostandfind.activity.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.adapter.ChatAdapter;
import com.example.lostandfind.chatDB.Chat;
import com.example.lostandfind.chatDB.ChatRooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private final static String TAG = "ChatActivity";

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    String receiverName;
    String receiverUID;
    String senderName;
    String senderUID;

    String text;
    EditText message_et;
    String curTime;
    String newRoomId = db.collection("Chat").document().getId();
    String oldRoomId;
    String tempRoom = "tempRoom";
    ChatRooms chatRoomData;
    Boolean exist;

    FrameLayout layout_send;
    TextView chat_room_name;
    Toolbar toolbar;
    RecyclerView recyclerView;
    ChatAdapter adapter;
    ProgressBar progressBar;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initializeView();   //view init
        setActionbar();     //Actionbar 관련 설정
        getIntentData();    //넘어오는 Intent data get (글쓴이 name, UID)
        setChatRoomName();  //room name set
        fetchChat();
        addChatRoom();

        progressBar.setVisibility(View.INVISIBLE);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        layout_send.setOnClickListener(onClickListener);
    }

    //버튼 리스너
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.layout_send:
                    sendMessage();
                    break;
            }
        }
    };

    private void setActionbar(){
        setSupportActionBar(toolbar);
        // 툴바 - 뒤로 가기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
    }

    private void addChatRoom(){
        Log.d(TAG, "roomId: "+ newRoomId);

        ChatRooms myInfo = new ChatRooms(senderUID, senderName, receiverUID, receiverName);
        Log.d(TAG, "mine name: "+senderName);
        Log.d(TAG, "mine uid: "+senderUID);

        ChatRooms yourInfo = new ChatRooms(receiverUID, receiverName, senderUID, senderName);
        Log.d(TAG, "yours: "+ receiverUID);
        Log.d(TAG, "yours: "+ receiverName);

//        db.collection("ChatRoom").document(senderUID)
//                .collection("userRoom").document(tempRoom).set(yourInfo);
//
//        db.collection("ChatRoom").document(receiverUID)
//                .collection("userRoom").document(tempRoom).set(myInfo);


        if (exist == false){
            db.collection("ChatRoom").document(senderUID)
                .collection("userRoom").document(newRoomId).set(yourInfo);

            db.collection("ChatRoom").document(receiverUID)
                .collection("userRoom").document(newRoomId).set(myInfo);
        }

    }


    private void fetchChat(){
        adapter = new ChatAdapter(user.getUid());
        recyclerView.setAdapter(adapter);

        Log.d(TAG, "Fetching msg");
        Log.d(TAG, "fetch name: "+senderName);
        Log.d(TAG, "fetch uid: "+senderUID);

        if (exist == false){
            db.collection("Chat")
                    .document(newRoomId)
                    .collection("chats")
                    .orderBy("sendDate")
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null){
                                Log.e(TAG, error.getMessage(), error);
                            } else {
                                List<Chat> chat = value.toObjects(Chat.class);
                                adapter.setData(chat);
                                recyclerView.smoothScrollToPosition(adapter.getItemCount()-1);
                            }
                        }
                    });
        } else {
            db.collection("Chat")
                    .document(oldRoomId)
                    .collection("chats")
                    .orderBy("sendDate")
                    .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null){
                                Log.e(TAG, error.getMessage(), error);
                            } else {
                                List<Chat> chat = value.toObjects(Chat.class);
                                adapter.setData(chat);
                                if (adapter.getItemCount() != 0) {
                                    recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
                                }
                            }
                        }
                    });
        }
//        db.collection("Chat")
//                .document(tempRoom)   //roomId가 null이 되었음
//                .collection("chats")
//                .orderBy("sendDate")
//                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (error != null){
//                            Log.e(TAG, error.getMessage(), error);
//                        } else {
//                            List<Chat> chat = value.toObjects(Chat.class);
//                            adapter.setData(chat);
//                        }
//                    }
//                });
    }

    private void getCurTime() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String getTime = dateFormat.format(date);

        curTime = getTime;
    }

    private void sendMessage(){
        text = message_et.getText().toString();
        if (!text.isEmpty()){
            progressBar.setVisibility(View.INVISIBLE);
            getCurTime();
            Chat chat = new Chat(senderUID, senderName, text, curTime);
            Log.d(TAG, "sendMsg senderUID: "+senderUID);

            if (exist == false){
                db.collection("Chat").document(newRoomId)
                        .collection("chats").add(chat);
            } else {
                db.collection("Chat").document(oldRoomId)
                        .collection("chats").add(chat);
            }
//            db.collection("Chat").document(roomId)
//                    .collection("chats").add(chat);
//            db.collection("Chat").document(tempRoom)
//                    .collection("chats").add(chat);

            message_et.setText("");
        }
    }

    private void setChatRoomName(){
        chat_room_name.setText(receiverName);
    }

    private void initializeView(){
        layout_send = (FrameLayout) findViewById(R.id.layout_send);
        chat_room_name = (TextView) findViewById(R.id.chat_room_name);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        message_et = (EditText) findViewById(R.id.message_et);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void getIntentData(){
        Intent intent = getIntent();

        ChatRooms chatRoomUserData = (ChatRooms)intent.getSerializableExtra("chatRoomUserData");
        exist = intent.getBooleanExtra("exist", false);
        Log.d(TAG, "exist: "+exist);
        oldRoomId = intent.getStringExtra("roomId");
        Log.d(TAG, "oldRoomID: "+oldRoomId);

        receiverUID = chatRoomUserData.getReceiverUID();
        receiverName = chatRoomUserData.getReceiverName();
        senderUID = chatRoomUserData.getSenderUID();
        senderName = chatRoomUserData.getSenderName();
        Log.d(TAG, "get your name: "+ receiverName);
        Log.d(TAG, "get your id: "+ receiverUID);
        Log.d(TAG, "get my name: "+senderName);
        Log.d(TAG, "get my id: "+senderUID);
        Log.d(TAG, "get  old: "+oldRoomId);
        Log.d(TAG, "get  new: "+newRoomId);
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

    // EditText가 아닌 다른 곳 터치 시 키보드 내리기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}