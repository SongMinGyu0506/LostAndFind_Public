package com.example.lostandfind.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfind.R;
import com.example.lostandfind.activity.MainActivity;
import com.example.lostandfind.data.Post;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    Context context;
    ArrayList<Post> postArrayList;

    public MainAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.main_recycler_item,parent,false);
        return new MainViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Post post = postArrayList.get(position);
        holder.title.setText(post.getTitle());
        holder.UID.setText(post.getUID());
        holder.text.setText(post.getText());
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        TextView UID,title,text;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            UID = itemView.findViewById(R.id.tvUID);
            title = itemView.findViewById(R.id.tvTitle);
            text = itemView.findViewById(R.id.tvText);

        }
    }
}
