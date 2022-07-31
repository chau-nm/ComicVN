package com.example.comicvn.ui.admin.add;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddComicActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;

    private EditText nameEt, categoryEt, contentEt;
    private ImageView coverView;
    private RecyclerView categoriesView;
    private AppCompatButton addCategoryBtn, choseFileCoverBtn,saveBtn;
    private List<String> categories;
    private CategoryAdapter categoryAdapter;
    private Uri uriCover;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private boolean saving;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_comic_activity);
        initialize();
        addCategoryBtn.setOnClickListener(view -> {
            addCategoryEvent();
        });
        choseFileCoverBtn.setOnClickListener(view -> {
            openFileChose();
        });
        saveBtn.setOnClickListener(view -> {
            if (nameEt.equals(""))
                new AlertDialog.Builder(this)
                        .setTitle("Lời nhắc")
                        .setMessage("Vui lòng nhập tên truyện").show();
            else if (!saving){
                saving = true;
                save();
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
        progressBar = findViewById(R.id.progress_bar);

        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        storageReference = FirebaseStorage.getInstance().getReference("comics");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    
    @SuppressLint("NotifyDataSetChanged")
    private void addCategoryEvent(){
        String category = categoryEt.getText().toString();
        if(!category.equals("")){
            categories.add(category);
            categoryEt.setText("");
            categoryAdapter.notifyDataSetChanged();
        }else{
            new AlertDialog.Builder(AddComicActivity.this)
                    .setTitle("Lời nhắc")
                    .setMessage("Bạn đang bỏ trống ô thể loại kìa")
                    .setPositiveButton("OK", (dialogInterface, i) -> {})
                    .show();
        }
    }

    private void openFileChose(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private String getFileExtention(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @SuppressLint("NotifyDataSetChanged")
    private void save(){
        if (uriCover != null){
            StorageReference coverSR = storageReference.child(System.currentTimeMillis() +
                    "." + getFileExtention(uriCover));
            coverSR.putFile(uriCover)
                    .addOnSuccessListener(taskSnapshot -> {
                        coverSR.getDownloadUrl()
                                .addOnSuccessListener(uri -> {
                                    String id = databaseReference.push().getKey();
                                    String name = nameEt.getText().toString();
                                    String cover = uri.toString();
                                    String content = contentEt.getText().toString();
                                    Comic comic = new Comic(id, name, cover, categories, content);
                                    databaseReference.child(id).setValue(comic)
                                            .addOnSuccessListener(unused -> {
                                                Toast.makeText(AddComicActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                                nameEt.setText("");
                                                coverView.setImageURI(Uri.parse(""));
                                                categories.clear();
                                                contentEt.setText("");
                                                categoryEt.setText("");
                                                categoryAdapter.notifyDataSetChanged();
                                                uriCover = null;
                                                saving = false;
                                                progressBar.setProgress(0);
                                                progressBar.setVisibility(View.GONE);
                                                nameEt.setEnabled(true);
                                                categoryEt.setEnabled(true);
                                                choseFileCoverBtn.setEnabled(true);
                                                contentEt.setEnabled(true);
                                            });
                                });
                    })
                    .addOnProgressListener(snapshot -> {
                        nameEt.setEnabled(false);
                        categoryEt.setEnabled(false);
                        choseFileCoverBtn.setEnabled(false);
                        contentEt.setEnabled(false);
                        progressBar.setVisibility(View.VISIBLE);
                        int progress = (int) (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        progressBar.setProgress(progress);
                    });
        }else{

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            uriCover = data.getData();
            Picasso.get().load(uriCover).into(coverView);
        }
    }
}