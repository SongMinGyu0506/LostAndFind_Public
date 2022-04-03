package com.example.lostandfind.adapter;

import android.content.Context;
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
        holder.firstName.setText(post.getFirstName());
        holder.lastName.setText(post.getLastName());
        holder.Age.setText(String.valueOf(post.getAge()));
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        TextView firstName,lastName,Age;

        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            firstName = itemView.findViewById(R.id.tvfirstName);
            lastName = itemView.findViewById(R.id.tvlastName);
            Age = itemView.findViewById(R.id.age);
        }
    }
}
