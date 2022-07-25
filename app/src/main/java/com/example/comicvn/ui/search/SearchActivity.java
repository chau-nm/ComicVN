package com.example.comicvn.ui.search;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private List<Comic> comics;
    private RecyclerView comicsView;
    private ComicAdapter comicAdapter;
    private EditText searchEt;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        initialize();
        search();
    }

    private void initialize(){
        comics = new ArrayList<>();
        comicsView = findViewById(R.id.comics_view);
        comicAdapter = new ComicAdapter(comics, SearchActivity.this);
        comicsView.setAdapter(comicAdapter);
        comicsView.setLayoutManager(new GridLayoutManager(this, 2));
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchEt = toolbar.findViewById(R.id.search_et);
        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
    }

    private void search(){
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadData(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void loadData(String keyword){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comics.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Comic comic = dataSnapshot.getValue(Comic.class);
                    if (comic.getName().toLowerCase().contains(keyword.toLowerCase()))
                        comics.add(comic);
                }
                comicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        menu.findItem(R.id.action_search).setChecked(true);
        return false;
    }
}
