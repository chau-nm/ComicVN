package com.example.comicvn.ui.category;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChoseCategoryActivity extends AppCompatActivity {

    private RecyclerView categoriesView;
    private List<String> categories;
    private CategoryAdapter categoryAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_category_activity);

        categoriesView = findViewById(R.id.categories_view);
        categories = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categories,this);
        categoriesView.setAdapter(categoryAdapter);
        categoriesView.setLayoutManager(new GridLayoutManager(this, 3));

        loadData();
    }

    private void loadData(){
        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Comic comic = dataSnapshot.getValue(Comic.class);
                    for(String category: comic.getCategory()){
                        if (!categories.contains(category)) categories.add(category);
                    }
                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
