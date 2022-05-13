package com.example.lostandfind.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lostandfind.R;
import com.example.lostandfind.adapter.ChatRoomAdapter;
import com.example.lostandfind.chatDB.ChatRooms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatFragment extends Fragment {
    private final static String TAG = "ChatFragment";
    ViewGroup rootView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    RecyclerView recyclerView;
    ChatRoomAdapter adapter;
    ArrayList<ChatRooms> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        arrayList = new ArrayList<ChatRooms>();

        adapter = new ChatRoomAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);

        startView();

        return rootView;
    }

    private void startView(){
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
                                Log.d(TAG, "ary: "+chatRooms.getReceiverName());
                                Log.d(TAG, "ary: "+chatRooms.getReceiverUID());
                                Log.d(TAG, "aryid: "+chatRooms.getId());
                                arrayList.add(chatRooms);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }

}