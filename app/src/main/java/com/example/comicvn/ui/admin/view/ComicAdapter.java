package com.example.comicvn.ui.admin.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.Holder> {
    private List<Comic> comics;
    private Context context;
    private DatabaseReference databaseReference;

    public ComicAdapter(List<Comic> comics, Context context) {
        this.comics = comics;
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
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
        holder.comicStateTv.setText(comics.get(position).getState());
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(context, AdminComicDetailActivity.class);
            intent.putExtra("COMICID", comics.get(position).getId());
            context.startActivity(intent);
        });
        holder.deleteComicBtn.setOnClickListener(view -> {
            Query query = databaseReference.child(comics.get(position).getId());
            new AlertDialog.Builder(context)
                    .setTitle("Xóa truyện")
                    .setMessage("Bạn chắc chứ?")
                    .setPositiveButton("Chắc chắn", (dialogInterface, i) -> query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            snapshot.getRef().removeValue();
                            Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    }))
                    .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return comics != null ? comics.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {

        private ImageView coverImage;
        private TextView nameComicTv, viewTv, comicStateTv;
        private AppCompatImageButton deleteComicBtn;

        public Holder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.cover_image);
            nameComicTv = itemView.findViewById(R.id.name_comic_tv);
            viewTv = itemView.findViewById(R.id.view_tv);
            comicStateTv = itemView.findViewById(R.id.comic_state_tv);
            deleteComicBtn = itemView.findViewById(R.id.delete_comic_btn);
        }
    }
}
