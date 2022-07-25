package com.example.comicvn.ui.readcomic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Chapter;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.obj.Page;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ReadComicActivity extends AppCompatActivity {

    private RecyclerView pagesView;
    private String comicId;
    private String chapterId;
    private int chapterNumber;
    private List<Page> pages;
    private PageAdapter pageAdapter;
    private DatabaseReference databaseReference;
    private BottomNavigationView btnv;
    private Comic comic;
    private Chapter chapter;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_comic_activity);
        initialize();
        loadData();
    }

    private void initialize() {
        pagesView = findViewById(R.id.pages_view);
        pages = new ArrayList<>();
        pageAdapter = new PageAdapter(pages);
        pagesView.setAdapter(pageAdapter);
        pagesView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));
        databaseReference = FirebaseDatabase.getInstance().getReference("comics");

        btnv = findViewById(R.id.read_comic_bottom_nav);
        context = this;
        btnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (comic != null && chapter != null)
                    switch (item.getItemId()) {
                        case R.id.previous_btn:
                            Chapter previousChapter = comic.getPreviousChapter(chapter);
                            if(previousChapter != null){
                                Intent intent = new Intent(context, ReadComicActivity.class);
                                intent.putExtra("COMICID", comicId);
                                intent.putExtra("CHAPTERID", previousChapter.getId());
                                startActivity(intent);
                            }
                            return true;
                        case R.id.next_btn:
                            Chapter nextChapter = comic.getNextChapter(chapter);
                            if(nextChapter != null){
                                Intent intent1 = new Intent(context, ReadComicActivity.class);
                                intent1.putExtra("COMICID", comicId);
                                intent1.putExtra("CHAPTERID", nextChapter.getId());
                                startActivity(intent1);
                            }
                            return true;
                        default:
                            return false;
                    }
                return false;
            }
        });
        Intent intent = getIntent();
        comicId = intent.getStringExtra("COMICID");
        chapterId = intent.getStringExtra("CHAPTERID");
        chapterNumber = intent.getIntExtra("CHAPTERNUMBER", -1);
    }

    private void loadData() {
        Query query = databaseReference.child(comicId);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comic = snapshot.getValue(Comic.class);
                chapter = comic.getChapter(chapterId);
                pages.addAll(chapter.getPages());
                pageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
