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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main.MainActivity;
import com.example.lostandfind.activity.Main.MainCreateActivity;
import com.example.lostandfind.adapter.MainAdapter;
import com.example.lostandfind.data.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
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

    SwipeRefreshLayout swipeRefreshLayout;

    private int limit = 5;
    private DocumentSnapshot lastVisible;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();
        swipeRefreshLayout = rootView.findViewById(R.id.layout_swipe);
        progressDialog = new ProgressDialog(getActivity());

        CollectionReference collectionReference = db.collection("Posts");
        Query query = collectionReference.orderBy("title",Query.Direction.ASCENDING).limit(limit);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        postArrayList = new ArrayList<Post>();
        mainAdapter = new MainAdapter(getActivity(), postArrayList);
        recyclerView.setAdapter(mainAdapter);

        // FloatingActionButton
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.temp_upperBtn);

        //QueryExcuting
        excuteQuery(query,collectionReference);
        loadSwiper(swipeRefreshLayout,query,collectionReference);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MainCreateActivity.class);
                startActivity(intent);
            }
        });

        RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = ((LinearLayoutManager)recyclerView.getLayoutManager());
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                int visibleItemCount = linearLayoutManager.getChildCount();
                int totalItemCount = linearLayoutManager.getItemCount();

                if (isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
                    isScrolling = false;
                    Query nextQuery = collectionReference.orderBy("title", Query.Direction.ASCENDING).startAfter(lastVisible).limit(limit);
                    nextQueryExcute(nextQuery);
                }
            }
        };
        recyclerView.addOnScrollListener(onScrollListener);
        return rootView;
    }

    private void nextQueryExcute(Query nextQuery) {
        nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (DocumentSnapshot d: task.getResult()) {
                        Post post = d.toObject(Post.class);
                        postArrayList.add(post);
                    }
                    mainAdapter.notifyDataSetChanged();
                    if (task.getResult().size() != 0) {
                        lastVisible = task.getResult().getDocuments().get(task.getResult().size()-1);
                    }
                    if (task.getResult().size() <limit) {
                        isLastItemReached = true;
                    }
                }
            }
        });
    }

    private void loadSwiper(SwipeRefreshLayout swipeRefreshLayout, Query query, CollectionReference collectionReference) {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getActivity(), "testSwipe", Toast.LENGTH_SHORT).show();
                postArrayList.clear();
                mainAdapter.clear();
                excuteQuery(query,collectionReference);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void excuteQuery(Query query, CollectionReference collectionReference) {
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        Post post = document.toObject(Post.class);
                        postArrayList.add(post);
                    }
                    mainAdapter.notifyDataSetChanged();
                    lastVisible = task.getResult().getDocuments().get(task.getResult().size()-1);
                }
            }
        });
    }
}