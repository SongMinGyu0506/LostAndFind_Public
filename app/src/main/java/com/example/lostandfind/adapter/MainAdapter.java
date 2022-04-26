package com.example.lostandfind.adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lostandfind.activity.Main.MainInspectActivity;
import com.example.lostandfind.R;
import com.example.lostandfind.data.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {
    Context context;
    ArrayList<Post> postArrayList;
    FirebaseStorage storage;
    private OnLoadMoreListener onLoadMoreListener;

    public MainAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
        storage = FirebaseStorage.getInstance();
    }
    public void clear() {postArrayList.clear();}

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
        holder.text.setText(post.getText());
        Log.d(TAG,"Developer: "+post.toString());

        StorageReference ref = FirebaseStorage.getInstance().getReference();
        ref.child("photo/"+post.getImageName()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.ivImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
                holder.ivImage.setImageResource(R.drawable.kumoh_symbol);
            }
        });

        //Glide.with(context).load(ref).into(holder.ivImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainInspectActivity.class);
                intent.putExtra("post",post);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        TextView UID,title,text;
        ImageView ivImage;
        public MainViewHolder(@NonNull View itemView) {
            super(itemView);
            UID = itemView.findViewById(R.id.tvUID);
            title = itemView.findViewById(R.id.tvTitle);
            text = itemView.findViewById(R.id.tvText);
            ivImage = itemView.findViewById(R.id.ivImage);
        }

    }
    public interface OnLoadMoreListener {
        void onLoadMore();
    }
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }
}
