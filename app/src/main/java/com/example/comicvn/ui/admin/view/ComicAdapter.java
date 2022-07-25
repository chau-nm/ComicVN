package com.example.comicvn.ui.admin.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.Holder> {
    private List<Comic> comics;
    private Context context;

    public ComicAdapter(List<Comic> comics, Context context){
        this.comics = comics;
        this.context = context;
    }

    @NonNull
    @Override
    public ComicAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.admin_comic_item_cardview, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicAdapter.Holder holder, int position) {
        Picasso.get()
                .load(comics.get(position).getCover())
                .fit()
                .into(holder.coverImage);
        holder.nameComicTv.setText(comics.get(position).getName());
        holder.viewTv.setText(comics.get(position).getView() + "");
        holder.favoriteTv.setText(comics.get(position).getLike() + "");
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, AdminComicDetailActivity.class);
            intent.putExtra("COMICID", comics.get(position).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return comics != null ? comics.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView coverImage;
        private TextView nameComicTv, viewTv, favoriteTv;

        public Holder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.cover_image);
            nameComicTv = itemView.findViewById(R.id.name_comic_tv);
            viewTv = itemView.findViewById(R.id.view_tv);
            favoriteTv =  itemView.findViewById(R.id.favorite_tv);
        }
    }
}
