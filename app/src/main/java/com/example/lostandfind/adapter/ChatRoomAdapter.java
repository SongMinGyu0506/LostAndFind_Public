package com.example.lostandfind.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.chat.ChatActivity;
import com.example.lostandfind.chatDB.ChatRooms;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.ChatRoomHolder>{
    private final static String TAG = "roomAdapter";

    private ArrayList<ChatRooms> arrayList;
    private Context context;

    public ChatRoomAdapter(ArrayList<ChatRooms> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public ChatRoomAdapter.ChatRoomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_chat_room, parent, false);
        ChatRoomHolder holder = new ChatRoomHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomHolder holder, int position) {
        ChatRooms chatRoom = arrayList.get(position);
        holder.setItem(chatRoom);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("chatRoomUserData", chatRoom);
                intent.putExtra("exist", true);
                intent.putExtra("roomId", chatRoom.getId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    void addItem(ChatRooms data){
        arrayList.add(data);
    }

    public void clear() {arrayList.clear();}

    public class ChatRoomHolder extends RecyclerView.ViewHolder{
        private TextView roomName;
        private TextView roomContents;

        public ChatRoomHolder(@NonNull View itemView) {
            super(itemView);

            roomName = itemView.findViewById(R.id.roomName);
            roomContents = itemView.findViewById(R.id.roomContents);
        }
        public void setItem(ChatRooms item) {
            roomName.setText(item.getReceiverName());
            roomContents.setText(item.getReceiverUID());
        }
    }
}
