package com.example.comicvn.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.ui.comicdetail.ComicDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewUpdateAdapter extends RecyclerView.Adapter<NewUpdateAdapter.NewUpdateHolder> {

    private List<Comic> newUpdateList;
    private Context context;

    public NewUpdateAdapter(List<Comic> newUpdateList, Context context){
        this.newUpdateList = newUpdateList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewUpdateHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_main_cardview, parent, false);
        return new NewUpdateHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewUpdateHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.comicName.setText(newUpdateList.get(position).getName());
        Picasso.get()
                .load(newUpdateList.get(position).getCover())
                .fit()
                .into(holder.comicImage);
        holder.comicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ComicDetailActivity.class);
                intent.putExtra("COMICID", newUpdateList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newUpdateList != null ? newUpdateList.size() : 0;
    }

    public class NewUpdateHolder extends RecyclerView.ViewHolder {

        private ImageView comicImage;
        private TextView comicName;

        public NewUpdateHolder(@NonNull View itemView) {
            super(itemView);
            comicImage = itemView.findViewById(R.id.comic_image);
            comicName = itemView.findViewById(R.id.comic_name_label_nu);
        }
    }
}
