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
import com.example.lostandfind.data.ChatItem;
import com.example.lostandfind.data.ChatRoom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;


/*
  작성자: 송민규
  TODO: 해당 어댑터에 적용되는 ITEM xml 파일을 생성하지 않았음(디자인 관련 문제)
  TODO: 따라서 chatting_room_item.xml 파일을 생성, 내부 아이템은 세 개의 TextView로 이루어지며
  TODO: TextView의 id는 tvName, tvDate, tvChatContents
  TODO: 현재 진행중인 채팅방 목록 초안 디자인은 카카오톡으로 공유되어있음
* */
public class ChattingAdapter extends RecyclerView.Adapter<ChattingAdapter.ChattingViewHolder> {
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context context;
    ArrayList<ChatRoom> chatRoomArrayList;

    public ChattingAdapter(Context context, ArrayList<ChatRoom> chatRoomArrayList) {
        this.context = context;
        this.chatRoomArrayList = chatRoomArrayList;
    }

    public void clear() {chatRoomArrayList.clear();}

    @NonNull
    @Override
    public ChattingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.chatting_room_item,parent,false);
        return new ChattingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChattingViewHolder holder, int position) {
        ChatRoom chatRoom = chatRoomArrayList.get(position);
        ArrayList<ChatItem> tempChatItem = chatRoom.getChatItems();

        /*
         상대방 이름이 화면에 노출되어야하므로, 클래스 내에서 userName1이나 2 둘중
         하나가 본인 이름이므로, 그 경우를 적용한 IF문
        */
        if(chatRoom.getUserEmail1().equals(user.getEmail())) {
            holder.tvName.setText(chatRoom.getUserName2());
        } else {
            holder.tvName.setText(chatRoom.getUserName1());
        }
        holder.tvChatContents.setText(tempChatItem.get(tempChatItem.size()-1).getContext());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChattingInspectActivity.class);
                intent.putExtra("chatContent",chatRoom);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatRoomArrayList.size();
    }

    public class ChattingViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvDate,tvChatContents;
        public ChattingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvChatContents = itemView.findViewById(R.id.tvChatContents);
        }
    }
}
