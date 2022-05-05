package com.example.lostandfind.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.activity.Main.MainInspectActivity;
import com.example.lostandfind.activity.Main2.Main2DetailActivity;
import com.example.lostandfind.data.LostPostInfo;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.query.main.MainAdapterQuery;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;

public class Main2Adapter extends RecyclerView.Adapter<Main2Adapter.Main2ViewHolder> {
    //adapter에 들어갈 list
    private ArrayList<LostPostInfo> arrayList;
    private Context context;
    private FirebaseStorage storage;
    MainAdapterQuery mainAdapterQuery;

    public Main2Adapter(ArrayList<LostPostInfo> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
        this.storage = FirebaseStorage.getInstance();

    }

    @NonNull
    @Override
    public Main2Adapter.Main2ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //main2_recycler_item.xml을 inflate
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main2_recycler_item, parent, false);
        Main2ViewHolder holder = new Main2ViewHolder(view);

        return holder;
    }

    //position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    //item을 하나하나 보여주는 함수
    @Override
    public void onBindViewHolder(@NonNull Main2ViewHolder holder, int position) {
        LostPostInfo lostPostInfo = arrayList.get(position);
        holder.setItem(lostPostInfo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Main2DetailActivity.class);
                intent.putExtra("lostPostInfo", lostPostInfo);
                context.startActivity(intent);
            }
        });
    }

    //전체 데이터 수 반환
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    void addItem(LostPostInfo data){
        arrayList.add(data);
    }

    public void clear() {arrayList.clear();}

    //viewHolder. subView setting
    public class Main2ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView contents;
        private TextView location;
        private TextView lostDate;
        private TextView postDate;
        private ImageView image;


        public Main2ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            contents = itemView.findViewById(R.id.contents);
            location = itemView.findViewById(R.id.location);
            lostDate = itemView.findViewById(R.id.lostDate);
            postDate = itemView.findViewById(R.id.postDate);
            image = itemView.findViewById(R.id.imageView);
        }

        public void setItem(LostPostInfo item) {
            title.setText(item.getTitle());
            contents.setText(item.getContents());
            location.setText(item.getLocation());
            lostDate.setText(item.getLostDate());
            postDate.setText(item.getPostDate());

            StorageReference ref = FirebaseStorage.getInstance().getReference();
            ref.child("photo/"+item.getImage()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(context).load(uri).into(image);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    image.setImageResource(R.drawable.kumoh_symbol);
                }
            });
        }
    }
}
