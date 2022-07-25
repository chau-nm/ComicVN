package com.example.comicvn.ui.readcomic;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Page;
import com.example.comicvn.ui.admin.view.ChapterAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.Holder> {

    private List<Page> pages;

    public PageAdapter(List<Page> pages){
        this.pages = pages;
    }

    @NonNull
    @Override
    public PageAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_cardview, parent, false);
        return new PageAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageAdapter.Holder holder, int position) {
        Picasso.get()
                .load(pages.get(position).getImage())
                .fit()
                .into(holder.pageImage);
    }

    @Override
    public int getItemCount() {
        return pages != null ? pages.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ImageView pageImage;

        public Holder(@NonNull View itemView) {
            super(itemView);
            pageImage = itemView.findViewById(R.id.page_image);
        }
    }
}
