package com.example.comicvn.ui.admin.edit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Chapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.Holder> {

    private List<Chapter> chapters;
    private Context context;

    public ChapterAdapter(List<Chapter> chapters, Context context) {
        this.chapters = chapters;
        this.context = context;
    }

    @NonNull
    @Override
    public ChapterAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chapter_admin_detail_cardview, parent, false);
        return new Holder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ChapterAdapter.Holder holder, int position) {
        holder.chapterNumberTv.setText("Chapter " + chapters.get(position).getNumber());
        holder.deleteChapterBtn.setOnClickListener(view ->{
            new AlertDialog.Builder(context)
                    .setTitle("Xóa truyện")
                    .setMessage("Bạn chắc chứ?")
                    .setPositiveButton("Chắc chắn", (dialogInterface, i) -> {
                        chapters.remove(position);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("Hủy", (dialogInterface, i) -> {})
                    .show();

        });
    }

    @Override
    public int getItemCount() {
        return chapters != null ? chapters.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView chapterNumberTv;
        private AppCompatImageButton deleteChapterBtn;

        public Holder(@NonNull View itemView) {
            super(itemView);
            chapterNumberTv = itemView.findViewById(R.id.chapter_number_tv);
            deleteChapterBtn = itemView.findViewById(R.id.delete_chapter_btn);
        }
    }
}
