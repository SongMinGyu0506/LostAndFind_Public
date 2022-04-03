package com.example.lostandfind.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.example.lostandfind.R;
import com.example.lostandfind.adapter.MainAdapter;
import com.example.lostandfind.data.Post;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button upper,lower;
    RecyclerView recyclerView;
    ArrayList<Post> postArrayList;
    MainAdapter mainAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("데이터 가져오는중...");
        progressDialog.show();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        postArrayList = new ArrayList<Post>();
        mainAdapter = new MainAdapter(MainActivity.this,postArrayList);

        recyclerView.setAdapter(mainAdapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("Userss").orderBy("firstName", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("Firestore error",error.getMessage());
                            return ;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    postArrayList.add(dc.getDocument().toObject(Post.class));
                                    break;
                                case REMOVED:
                                    Post tmp = dc.getDocument().toObject(Post.class);
                                    for(int i = 0; i <postArrayList.size(); i++) {
                                        Post pi = postArrayList.get(i);
                                        int numElements = postArrayList.size();
                                        if(tmp.getFirstName().equals((pi.getFirstName()))) {
                                            postArrayList.set(i,postArrayList.get(numElements-1));
                                            postArrayList.set(numElements-1,pi);
                                            postArrayList.remove(numElements-1);
                                            i--;
                                        }
                                    }
                                    break;
                            }
                            mainAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }
}