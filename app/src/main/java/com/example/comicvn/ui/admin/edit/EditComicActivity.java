package com.example.comicvn.ui.admin.edit;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Chapter;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.ui.admin.add.CategoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EditComicActivity extends AppCompatActivity {

    private EditText nameEt, categoryEt, contentEt;
    private ImageView coverView;
    private RecyclerView categoriesView, chaptersView;
    private AppCompatButton addCategoryBtn, choseFileCoverBtn,saveBtn;
    private List<String> categories;
    private CategoryAdapter categoryAdapter;
    private List<Chapter> chapters;
    private ChapterAdapter chapterAdapter;
    private Uri uriCover;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_comic_activity);
        initialize();
        loadData();
    }

    private void initialize(){
        nameEt = findViewById(R.id.name_et);
        categoryEt = findViewById(R.id.category_et);
        contentEt = findViewById(R.id.content_et);
        categories = new ArrayList<>();
        categoriesView = findViewById(R.id.categories_view);
        categoryAdapter = new CategoryAdapter(categories);
        categoriesView.setAdapter(categoryAdapter);
        categoriesView.setLayoutManager(new GridLayoutManager(this, 3));
        addCategoryBtn = findViewById(R.id.add_category);
        saveBtn = findViewById(R.id.save_btn);
        coverView = findViewById(R.id.cover_image);
        choseFileCoverBtn = findViewById(R.id.chose_file_cover_btn);
        chapters = new ArrayList<>();
        chapterAdapter = new ChapterAdapter(chapters);
        chaptersView = findViewById(R.id.chapters_view);

        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        storageReference = FirebaseStorage.getInstance().getReference("comics");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadData(){
        String comicId = getIntent().getStringExtra("COMICID");
        Query query = databaseReference.child(comicId);
        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Comic comic = snapshot.getValue(Comic.class);
                nameEt.setText(comic.getName());
                categories.addAll(comic.getCategory());
                contentEt.setText(comic.getContent());
                chapters.addAll(comic.getChapters());
                Picasso.get()
                        .load(comic.getCover())
                        .into(coverView);
                categoryAdapter.notifyDataSetChanged();
                chapterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
