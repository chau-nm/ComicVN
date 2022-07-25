package com.example.comicvn.ui.home;

import android.annotation.SuppressLint;
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
import com.example.comicvn.ui.comicdetail.ComicDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForYouAdapter extends RecyclerView.Adapter<ForYouAdapter.ForYouHolder> {

    private List<Comic> forYouList;
    private Context context;

    public ForYouAdapter(List<Comic> forYouList, Context context){
        this.forYouList = forYouList;
        this.context = context;
    }

    @NonNull
    @Override
    public ForYouHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.banner_cardview, parent, false);
        return new ForYouHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForYouHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.comicName.setText(forYouList.get(position).getName());
        Picasso.get()
                .load(forYouList.get(position).getCover())
                .fit()
                .into(holder.comicImage);
        holder.comicImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ComicDetailActivity.class);
                intent.putExtra("COMICID", forYouList.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return forYouList != null ? forYouList.size() : 0;
    }

    public class ForYouHolder extends RecyclerView.ViewHolder {

        private ImageView comicImage;
        private TextView comicName;

        public ForYouHolder(@NonNull View itemView) {
            super(itemView);
            comicImage = itemView.findViewById(R.id.banner_image);
            comicName = itemView.findViewById(R.id.comic_name_label);
        }
    }
}
