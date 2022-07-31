package com.example.comicvn.ui.admin.edit;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Chapter;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.ui.admin.add.AddChapterActivity;
import com.example.comicvn.ui.admin.add.AddComicActivity;
import com.example.comicvn.ui.admin.add.CategoryAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
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

    private static final int PICK_IMAGE = 1;

    private EditText nameEt, categoryEt, contentEt, comicStateEt;
    private ImageView coverView;
    private RecyclerView categoriesView, chaptersView;
    private AppCompatButton addCategoryBtn, choseFileCoverBtn, saveBtn;
    private AppCompatImageButton addChapterBtn;
    private List<String> categories;
    private CategoryAdapter categoryAdapter;
    private List<Chapter> chapters;
    private ChapterAdapter chapterAdapter;
    private Uri uriCover;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String comicId;
    private ProgressBar progressBar;
    private Comic comic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_comic_activity);
        initialize();
        loadData();
    }

    private void initialize() {
        nameEt = findViewById(R.id.name_et);
        categoryEt = findViewById(R.id.category_et);
        contentEt = findViewById(R.id.content_et);
        comicStateEt = findViewById(R.id.comic_state_et);
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
        chapterAdapter = new ChapterAdapter(chapters, this);
        chaptersView = findViewById(R.id.chapters_view);
        chaptersView.setAdapter(chapterAdapter);
        chaptersView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        progressBar = findViewById(R.id.progress_bar);
        addChapterBtn = findViewById(R.id.add_chapter_btn);

        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        storageReference = FirebaseStorage.getInstance().getReference("comics");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        pressAddChapterBtn();
        pressSaveBtn();
        pressChoseFile();
        pressAddCategoryBtn();
    }

    private String getFileExtention(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void pressAddChapterBtn() {
        addChapterBtn.setOnClickListener(view -> {
            if (comicId != null) {
                Intent intent = new Intent(this, AddChapterActivity.class);
                intent.putExtra("COMICID", comicId);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void pressAddCategoryBtn() {
        addCategoryBtn.setOnClickListener(view -> {
            String category = categoryEt.getText().toString();
            if(!category.equals("")){
                categories.add(category);
                categoryEt.setText("");
                categoryAdapter.notifyDataSetChanged();
            }else{
                new AlertDialog.Builder(EditComicActivity.this)
                        .setTitle("Lời nhắc")
                        .setMessage("Bạn đang bỏ trống ô thể loại kìa")
                        .setPositiveButton("OK", (dialogInterface, i) -> {})
                        .show();
            }
        });
    }

    private void pressChoseFile() {
        choseFileCoverBtn.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriCover = data.getData();
            Picasso.get().load(uriCover).into(coverView);
        }
    }

    private void saveWithChangeCover() {
        StorageReference coverSR = storageReference.child(System.currentTimeMillis() +
                "." + getFileExtention(uriCover));
        coverSR.putFile(uriCover)
                .addOnSuccessListener(taskSnapshot -> {
                    coverSR.getDownloadUrl()
                            .addOnSuccessListener(uri -> {
                                comic.setName(nameEt.getText().toString());
                                comic.setContent(contentEt.getText().toString());
                                comic.setCover(uri.toString());
                                comic.setCategory(categories);
                                comic.setChapters(chapters);
                                databaseReference.child(comic.getId()).setValue(comic)
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(EditComicActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                            progressBar.setProgress(0);
                                            progressBar.setVisibility(View.GONE);
                                            nameEt.setEnabled(true);
                                            comicStateEt.setEnabled(true);
                                            categoryEt.setEnabled(true);
                                            choseFileCoverBtn.setEnabled(true);
                                            contentEt.setEnabled(true);
                                        });
                            });
                })
                .addOnProgressListener(snapshot -> {
                    nameEt.setEnabled(false);
                    categoryEt.setEnabled(false);
                    comicStateEt.setEnabled(false);
                    choseFileCoverBtn.setEnabled(false);
                    contentEt.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    int progress = (int) (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress(progress);
                });
    }

    private void saveWithKeepCode(){
        comic.setName(nameEt.getText().toString());
        comic.setContent(contentEt.getText().toString());
        comic.setCategory(categories);
        comic.setChapters(chapters);

        databaseReference.child(comic.getId())
                .setValue(comic)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(EditComicActivity.this, "Đã lưu", Toast.LENGTH_SHORT).show();
                });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void pressSaveBtn() {
        saveBtn.setOnClickListener(view -> {
            if (comicId != null){
                if (uriCover != null) saveWithChangeCover();
                else saveWithKeepCode();
            }

        });
    }

    private void loadData() {
        comicId = getIntent().getStringExtra("COMICID");
        Query query = databaseReference.child(comicId);
        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comic = snapshot.getValue(Comic.class);
                nameEt.setText(comic.getName());
                comicStateEt.setText(comic.getState());
                if (comic.getCategory() != null) categories.addAll(comic.getCategory());
                contentEt.setText(comic.getContent());
                if (comic.getChapters() != null) chapters.addAll(comic.getChapters());
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
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
