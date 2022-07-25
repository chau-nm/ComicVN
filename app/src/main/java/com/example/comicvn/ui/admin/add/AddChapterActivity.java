package com.example.comicvn.ui.admin.add;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Chapter;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.obj.Page;
import com.example.comicvn.ui.admin.view.CategoryAdapter;
import com.example.comicvn.ui.admin.view.ChapterAdapter;
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

public class AddChapterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    private TextView comicNameTv;
    private EditText chapterNumberEt, chapterTitleEt;
    private AppCompatImageButton addPageBtn;
    private AppCompatButton saveBtn;
    private List<Uri> pages;
    private PageAdapter pageAdapter;
    private RecyclerView pagesView;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Comic comic;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_chapter_activity);
        initialize();
        loadData();
        addPageBtn.setOnClickListener(view -> {
            openFileChose();
        });
        saveBtn.setOnClickListener(view -> {
            save();
        });
    }

    private void loadData(){
        String comicId = getIntent().getStringExtra("COMICID");
        Query query = databaseReference.child(comicId);

        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                comic = snapshot.getValue(Comic.class);
                comicNameTv.setText(comic.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void initialize(){
        comicNameTv = findViewById(R.id.comic_name);
        chapterNumberEt = findViewById(R.id.chapter_number_et);
        chapterTitleEt = findViewById(R.id.title_chapter_et);
        addPageBtn = findViewById(R.id.add_page_btn);
        pagesView = findViewById(R.id.pages_view);
        saveBtn = findViewById(R.id.save_btn);
        pages = new ArrayList<>();
        pageAdapter = new PageAdapter(pages);
        pagesView.setAdapter(pageAdapter);
        pagesView.setNestedScrollingEnabled(false);
        pagesView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        storageReference = FirebaseStorage.getInstance().getReference("comics");
    }

    private void openFileChose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            Uri uri = data.getData();
            pages.add(uri);
            pageAdapter.notifyDataSetChanged();
        }
    }

    private String getFileExtention(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void save(){
        List<Page> pageList = new ArrayList<>();

        pages.forEach(uri -> {
            StorageReference pageSR = storageReference.child(System.currentTimeMillis() +
                    "." + getFileExtention(uri));
            pageSR.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        pageSR.getDownloadUrl().addOnSuccessListener(uri1 -> {
                            pageList.add(new Page(pageList.size(), uri1.toString()));
                            if (uri.equals(pages.get(pages.size() - 1))){
                                int chapterNumber = Integer.parseInt(chapterNumberEt.getText().toString());
                                String chapteTitle = chapterTitleEt.getText().toString();
                                Chapter chapter = new Chapter(chapterNumber, chapteTitle, pageList);
                                comic.addChapter(chapter);
                                databaseReference.child(comic.getId()).setValue(comic);
                                Toast.makeText(this, "Saved", Toast.LENGTH_LONG).show();
                            }
                        });
                    });
        });
    }
}
