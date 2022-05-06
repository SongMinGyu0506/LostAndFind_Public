package com.example.lostandfind.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main2.LostPostActivity;
import com.example.lostandfind.adapter.Main2Adapter;
import com.example.lostandfind.data.LostPostInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Home2Fragment extends Fragment {
    private final static String TAG = "Home2Fragment";
    ViewGroup rootView;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    FirebaseFirestore db;
    ArrayList<LostPostInfo> arrayList;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home2, container, false);

//        progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fetching Data...");
//        progressDialog.show();

        db = FirebaseFirestore.getInstance();

        rootView.findViewById(R.id.floatingActionButton).setOnClickListener(onClickListener);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrayList = new ArrayList<LostPostInfo>();

        adapter = new Main2Adapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);

        startView();

//        EventChangeListener();

        return rootView;
    }

    private void startView(){
        db.collection("LostPosts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                LostPostInfo lostPostInfo = document.toObject(LostPostInfo.class); //여기
                                lostPostInfo.setId(document.getId());
                                arrayList.add(lostPostInfo);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    //오류 두 가지 있어서 폐기 오류 부분은 '여기' 라고 표시함
    private void EventChangeListener(){
        db.collection("LostPosts").orderBy("title", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() { //여기
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null){
                            if (progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange dc : value.getDocumentChanges()){
                            if (dc.getType() == DocumentChange.Type.ADDED){
                                arrayList.add(dc.getDocument().toObject(LostPostInfo.class)); //여기
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (progressDialog.isShowing()){
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    View.OnClickListener onClickListener = view -> {
        switch (view.getId()){
            case R.id.floatingActionButton:
                Intent intent = new Intent(getActivity(), LostPostActivity.class);
                startActivity(intent);
                break;
        }
    };

    public void toast(String text){
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }
}