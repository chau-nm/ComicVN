package com.example.comicvn.ui.admin.add;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Page;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class PageAdapter extends RecyclerView.Adapter<PageAdapter.Hodler> {

    private List<Uri> pages;

    public PageAdapter(List<Uri> pages){
        this.pages = pages;
    }

    @NonNull
    @Override
    public PageAdapter.Hodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.page_admin_cardview, parent, false);
        return new Hodler(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull PageAdapter.Hodler holder, int position) {
        Picasso.get()
                .load(pages.get(position))
                .fit()
                .into(holder.pageImage);
        holder.pageImage.setOnClickListener(view -> {
            pages.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return pages != null ? pages.size() : 0;
    }

    public class Hodler extends RecyclerView.ViewHolder {
        private ImageView pageImage;

        public Hodler(@NonNull View itemView) {
            super(itemView);
            pageImage = itemView.findViewById(R.id.page_image);
        }
    }
}
