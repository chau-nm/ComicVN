package com.example.comicvn.ui.readcomic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Chapter;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.obj.Page;
import com.example.comicvn.sqlite.HistoryDb;
import com.google.android.gms.tasks.OnSuccessListener;
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
    private HistoryDb db;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_comic_activity);
        initialize();
        handleBottomMenu();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initialize() {
        pagesView = findViewById(R.id.pages_view);
        pages = new ArrayList<>();
        pageAdapter = new PageAdapter(pages);
        pagesView.setAdapter(pageAdapter);
        pagesView.setLayoutManager(new LinearLayoutManager(this,
                RecyclerView.VERTICAL, false));
        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        db = new HistoryDb(this);
        btnv = findViewById(R.id.read_comic_bottom_nav);
        context = this;

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        comicId = intent.getStringExtra("COMICID");
        chapterId = intent.getStringExtra("CHAPTERID");
        chapterNumber = intent.getIntExtra("CHAPTERNUMBER", -1);
        db.insert(comicId, chapterId);
        loadData();
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

    @SuppressLint("NotifyDataSetChanged")
    private void handleBottomMenu() {
        btnv.setOnItemSelectedListener(item -> {
            if (comic != null && chapter != null)
                switch (item.getItemId()) {
                    case R.id.previous_btn:
                        movePreviousChapter();
                        return true;
                    case R.id.next_btn:
                        moveNextChapter();
                        return true;
                    default:
                        return false;
                }
            return false;
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void movePreviousChapter() {
        Chapter previousChapter = comic.getPreviousChapter(chapter);
        if (previousChapter != null) {
            chapter = previousChapter;
            db.insert(comicId, chapter.getId());
            System.out.println(chapter.getId());
            System.out.println(db.getHistory());
            pages.clear();
            pages.addAll(chapter.getPages());
            toolbar.setTitle("Chapter " + chapter.getNumber());
            pageAdapter.notifyDataSetChanged();
            pagesView.scrollToPosition(0);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void moveNextChapter() {
        Chapter nextChapter = comic.getNextChapter(chapter);
        if (nextChapter != null) {
            chapter = nextChapter;
            db.insert(comicId, chapter.getId());
            pages.clear();
            pages.addAll(chapter.getPages());
            toolbar.setTitle("Chapter " + chapter.getNumber());
            pageAdapter.notifyDataSetChanged();
            pagesView.scrollToPosition(0);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadData() {
        Query query = databaseReference.child(comicId);
        query.get().addOnSuccessListener(dataSnapshot -> {
            comic = dataSnapshot.getValue(Comic.class);
            chapter = comic.getChapter(chapterId);
            chapter.increaseView();
            pages.addAll(chapter.getPages());
            toolbar.setTitle("Chapter " + chapter.getNumber());
            pageAdapter.notifyDataSetChanged();
            databaseReference.child(comic.getId()).setValue(comic);
        });
    }
}
