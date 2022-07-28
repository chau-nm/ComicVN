package com.example.comicvn.ui.history;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Chapter;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.sqlite.HistoryDb;
import com.example.comicvn.ui.comicdetail.ComicDetailActivity;
import com.example.comicvn.ui.readcomic.ReadComicActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.Holder> {

    private List<Comic> comics;
    private List<Chapter> chapters;
    private Context context;
    private HistoryDb db;

    public ComicAdapter(List<Comic> comics, List<Chapter> chapters, Context context) {
        this.comics = comics;
        this.chapters = chapters;
        this.context = context;
        this.db = new HistoryDb(context);
    }

    @NonNull
    @Override
    public ComicAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comic_history_card, parent, false);
        return new ComicAdapter.Holder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ComicAdapter.Holder holder, int position) {
        Picasso.get()
                .load(comics.get(position).getCover())
                .fit()
                .into(holder.comicImage);
        holder.comicImage.setOnClickListener(view -> {
            Intent intent = new Intent(context, ComicDetailActivity.class);
            intent.putExtra("COMICID", comics.get(position).getId());
            context.startActivity(intent);
        });
        holder.comicNameTv.setText(comics.get(position).getName());
        holder.comicNameTv.setOnClickListener(view -> {
            Intent intent = new Intent(context, ComicDetailActivity.class);
            intent.putExtra("COMICID", comics.get(position).getId());
            context.startActivity(intent);
        });
        holder.chapterNumberTv.setText(chapters.get(position).getNumber() + "");
        holder.chapterView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ReadComicActivity.class);
            intent.putExtra("COMICID", comics.get(position).getId());
            intent.putExtra("CHAPTERID", chapters.get(position).getId());
            intent.putExtra("CHAPTERNUMBER", chapters.get(position).getNumber());
            context.startActivity(intent);
        });
        holder.deleteBtn.setOnClickListener(view -> {
            db.delete(comics.get(position).getId());
            comics.remove(position);
            chapters.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return comics != null ? comics.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView comicImage;
        private AppCompatImageButton deleteBtn;
        private TextView comicNameTv;
        private RelativeLayout chapterView;
        private TextView chapterNumberTv;

        public Holder(@NonNull View itemView) {
            super(itemView);

            comicImage = itemView.findViewById(R.id.comic_image);
            deleteBtn = itemView.findViewById(R.id.delete_history_btn);
            comicNameTv = itemView.findViewById(R.id.comic_name_label_nu);
            chapterView = itemView.findViewById(R.id.chapter_view);
            chapterNumberTv = itemView.findViewById(R.id.chapter_number_tv);
        }
    }
}
