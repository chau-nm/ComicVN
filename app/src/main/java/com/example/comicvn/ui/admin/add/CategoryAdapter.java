package com.example.comicvn.ui.admin.add;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    private List<String> categories;

    public CategoryAdapter(List<String> categories){
        this.categories = categories;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_btn_cardview_category_activity, parent, false);
        return new Holder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.btn.setText(categories.get(position));
        holder.btn.setOnClickListener(view -> {
            categories.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private AppCompatButton btn;

        public Holder(@NonNull View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.category_btn);
        }
    }
}
