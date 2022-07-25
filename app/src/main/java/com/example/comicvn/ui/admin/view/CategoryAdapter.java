package com.example.comicvn.ui.admin.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    public CategoryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_textview_cardview, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Holder holder, int position) {
        String text = position != categories.size() - 1
                ? categories.get(position) + " - "
                : categories.get(position);
        holder.categoryView.setText(text);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView categoryView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            categoryView = itemView.findViewById(R.id.category_tv);
        }
    }
}
