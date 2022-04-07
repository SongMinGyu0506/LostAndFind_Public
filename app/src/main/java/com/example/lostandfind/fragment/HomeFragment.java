package com.example.lostandfind.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main.MainActivity;
import com.example.lostandfind.activity.Main.MainWriterActivity;
import com.example.lostandfind.adapter.MainAdapter;
import com.example.lostandfind.data.Post;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    ViewGroup rootView;

    Button upper,lower;
    RecyclerView recyclerView;
    ArrayList<Post> postArrayList;
    MainAdapter mainAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("데이터 가져오는중...");
        progressDialog.show();

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        db = FirebaseFirestore.getInstance();
        postArrayList = new ArrayList<Post>();
        mainAdapter = new MainAdapter(getActivity(), postArrayList);

        recyclerView.setAdapter(mainAdapter);

        upper = rootView.findViewById(R.id.temp_upperBtn);
        upper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainWriterActivity.class);
                startActivity(intent);
            }
        });

        EventChangeListener();


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //postArrayList.add((Post)data.getSerializableExtra("post"));
            EventChangeListener();
        }
    }

    private void EventChangeListener() {
        db.collection("Posts").orderBy("title", Query.Direction.ASCENDING)
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
//                                case REMOVED:
//                                    Post tmp = dc.getDocument().toObject(Post.class);
//                                    for(int i = 0; i <postArrayList.size(); i++) {
//                                        Post pi = postArrayList.get(i);
//                                        int numElements = postArrayList.size();
//                                        if(tmp.getFirstName().equals((pi.getFirstName()))) {
//                                            postArrayList.set(i,postArrayList.get(numElements-1));
//                                            postArrayList.set(numElements-1,pi);
//                                            postArrayList.remove(numElements-1);
//                                            i--;
//                                        }
//                                    }
//                                    break;
                            }
                            mainAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                        }
                    }
                });
    }
}