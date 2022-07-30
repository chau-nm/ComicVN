package com.example.comicvn.ui.admin.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.ui.admin.add.AddComicActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class AdminActivity extends AppCompatActivity {
    private RecyclerView comicsView;
    private ComicAdapter comicAdapter;
    private List<Comic> comics;
    private DatabaseReference databaseReference;
    private AppCompatImageButton addBtn;
    private List<Comic> searchComics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        initialize();

        addBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, AddComicActivity.class));
        });

        loadData();
    }

    private void initialize() {
        comicsView = findViewById(R.id.comics_view);
        comicsView.setNestedScrollingEnabled(false);
        comics = new ArrayList<>();
        searchComics = new ArrayList<>();
        comicAdapter = new ComicAdapter(searchComics, this);
        comicsView.setAdapter(comicAdapter);
        comicsView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        addBtn = findViewById(R.id.add_comic);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void loadData() {
        Query query = databaseReference.orderByChild("view");
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post : snapshot.getChildren()) {
                    Comic comic = post.getValue(Comic.class);
                    comics.add(comic);
                    searchComics.add(comic);
                }
                comicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Tìm kiếm");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String s) {
                searchComics.clear();
                searchComics.addAll(comics.stream()
                        .filter(c -> c.getName().toLowerCase().contains(s.toLowerCase()))
                        .collect(Collectors.toList()));
                comicAdapter.notifyDataSetChanged();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu);
        return true;
    }
}