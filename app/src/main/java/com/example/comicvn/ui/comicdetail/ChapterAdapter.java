package com.example.comicvn.ui.comicdetail;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Caculation;
import com.example.comicvn.obj.Chapter;
import com.example.comicvn.sqlite.HistoryDb;
import com.example.comicvn.ui.readcomic.ReadComicActivity;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.Holder> {
    private List<Chapter> chapters;
    private Context context;
    private String comicId;
    private HistoryDb historyDb;

    public ChapterAdapter(List<Chapter> chapters, Context context, String comicId){
        this.chapters = chapters;
        this.context = context;
        this.comicId = comicId;
        historyDb = new HistoryDb(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_cardview, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.chapterNumberTv.setText("Chapter " + chapters.get(position).getNumber());
        holder.chapterUpdateTv.setText("Cập nhật "
                + Caculation.getMessageDate(chapters.get(position).getUpdate()));
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, ReadComicActivity.class);
            intent.putExtra("COMICID", comicId);
            intent.putExtra("CHAPTERID", chapters.get(position).getId());
            intent.putExtra("CHAPTERNUMBER", chapters.get(position).getNumber());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chapters != null ? chapters.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView chapterNumberTv, chapterUpdateTv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            chapterNumberTv = itemView.findViewById(R.id.chapter_number_tv);
            chapterUpdateTv = itemView.findViewById(R.id.chapter_update_tv);
        }
    }
}
