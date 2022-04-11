package com.example.lostandfind.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

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
import android.widget.Button;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main.MainActivity;
import com.example.lostandfind.activity.Main.MainCreateActivity;
import com.example.lostandfind.adapter.MainAdapter;
import com.example.lostandfind.data.Post;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ViewGroup rootView;
    List<Post> tempL; // 임시
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

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    Toast.makeText(getActivity(), "페이지 끝", Toast.LENGTH_SHORT).show();
                }
            }
        });

        upper = rootView.findViewById(R.id.temp_upperBtn);
        upper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainCreateActivity.class);
                startActivity(intent);
            }
        });
        EventChangeListener();
        return rootView;
    }

    //Fragment에서는 작동하지 않는듯? - SongMinGyu
    //MainWriterActivity에서 작성한 데이터로 리사이클러 업데이트 요청
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
                                    // 삭제시 업데이트: O(N) 소요 알고리즘 적용
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