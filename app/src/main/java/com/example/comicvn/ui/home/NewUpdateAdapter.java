package com.example.comicvn.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Caculation;
import com.example.comicvn.obj.Chapter;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.ui.comicdetail.ComicDetailActivity;
import com.example.comicvn.ui.readcomic.ReadComicActivity;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
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
        holder.comicName.setOnClickListener(view -> {
            Intent intent = new Intent(context, ComicDetailActivity.class);
            intent.putExtra("COMICID", newUpdateList.get(position).getId());
            context.startActivity(intent);
        });

        List<Chapter> newUpdateChapter = newUpdateList.get(position).getNewUpdateChapter(3);
        Chapter chapter1 = newUpdateChapter.size() >= 1 ? newUpdateChapter.get(0) : null;
        Chapter chapter2 = newUpdateChapter.size() >= 2 ? newUpdateChapter.get(1) : null;
        Chapter chapter3 = newUpdateChapter.size() >= 3 ? newUpdateChapter.get(2) : null;
        if(chapter1 != null){
            holder.chapter1Name.setText("Chapter " + chapter1.getNumber());
            holder.chapter1Update.setText(Caculation.getMessageDate(chapter1.getUpdate()));
            holder.chapter1.setOnClickListener(view -> {
                Intent intent = new Intent(context, ReadComicActivity.class);
                intent.putExtra("COMICID", newUpdateList.get(position).getId());
                intent.putExtra("CHAPTERID", chapter1.getId());
                intent.putExtra("CHAPTERNUMBER", chapter1.getNumber());
                context.startActivity(intent);
            });
        }
        if(chapter2 != null){
            holder.chapter2Name.setText("Chapter " + chapter2.getNumber());
            holder.chapter2Update.setText(Caculation.getMessageDate(chapter2.getUpdate()));
            holder.chapter2.setOnClickListener(view -> {
                Intent intent = new Intent(context, ReadComicActivity.class);
                intent.putExtra("COMICID", newUpdateList.get(position).getId());
                intent.putExtra("CHAPTERID", chapter2.getId());
                intent.putExtra("CHAPTERNUMBER", chapter2.getNumber());
                context.startActivity(intent);
            });
        }
        if(chapter3 != null){
            holder.chapter3Name.setText("Chapter " + chapter3.getNumber());
            holder.chapter3Update.setText(Caculation.getMessageDate(chapter3.getUpdate()));
            holder.chapter3.setOnClickListener(view -> {
                Intent intent = new Intent(context, ReadComicActivity.class);
                intent.putExtra("COMICID", newUpdateList.get(position).getId());
                intent.putExtra("CHAPTERID", chapter3.getId());
                intent.putExtra("CHAPTERNUMBER", chapter3.getNumber());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return newUpdateList != null ? newUpdateList.size() : 0;
    }

    public class NewUpdateHolder extends RecyclerView.ViewHolder {

        private ImageView comicImage;
        private TextView comicName;
        private RelativeLayout chapter1, chapter2, chapter3;
        private TextView chapter1Name, chapter2Name, chapter3Name,
                        chapter1Update, chapter2Update, chapter3Update;

        public NewUpdateHolder(@NonNull View itemView) {
            super(itemView);
            comicImage = itemView.findViewById(R.id.comic_image);
            comicName = itemView.findViewById(R.id.comic_name_label_nu);
            chapter1 = itemView.findViewById(R.id.chapter1_view);
            chapter2 = itemView.findViewById(R.id.chapter2_view);
            chapter3 = itemView.findViewById(R.id.chapter3_view);
            chapter1Name = itemView.findViewById(R.id.chapter_name_tv1);
            chapter2Name = itemView.findViewById(R.id.chapter_name_tv2);
            chapter3Name = itemView.findViewById(R.id.chapter_name_tv3);
            chapter1Update = itemView.findViewById(R.id.chapter_update_tv1);
            chapter2Update = itemView.findViewById(R.id.chapter_update_tv2);
            chapter3Update = itemView.findViewById(R.id.chapter_update_tv3);
        }
    }
}
