package com.example.comicvn.ui.admin.edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Chapter;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.Holder> {

    private List<Chapter> chapters;

    public ChapterAdapter(List<Chapter> chapters) {
        this.chapters = chapters;
    }

    @NonNull
    @Override
    public ChapterAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_cardview, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.Holder holder, int position) {
        holder.chapterNumberTv.setText("Chapter " + chapters.get(position).getNumber());
        holder.chapterUpdateTv.setText("Cập nhật ngày "
                + chapters.get(position).getUpdate().toString());
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
