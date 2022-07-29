package com.example.comicvn.ui.comicdetail;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.ui.MainActivity;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    private List<String> categories;
    private Context context;

    public CategoryAdapter(List<String> categories, Context context){
        this.categories = categories;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_btn_cardview_comicdetail_activity, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.categoryBtn.setText(categories.get(position));
        holder.categoryBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("CATEGORY_MENU", R.layout.fragment_category);
            intent.putExtra("CATEGORY", categories.get(position));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    public class Holder extends RecyclerView.ViewHolder {
        private AppCompatButton categoryBtn;

        public Holder(@NonNull View itemView) {
            super(itemView);
            categoryBtn = itemView.findViewById(R.id.category_btn);
        }
    }
}
