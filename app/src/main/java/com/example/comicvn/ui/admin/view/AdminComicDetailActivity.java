package com.example.comicvn.ui.admin.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.ui.admin.add.AddChapterActivity;
import com.example.comicvn.ui.admin.edit.EditComicActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminComicDetailActivity extends AppCompatActivity {

    private TextView comicNameTv, comicUpdateTv,
            comicStateTv, comicViewNumberTv, comicContentTv;
    private ImageView comicCoverIV;
    private RecyclerView categoriesView, chaptersView;
    private AppCompatButton editBtn, addChapterBtn;
    private DatabaseReference databaseReference;
    private String comicId;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_detail_admin_activity);
        initalize();
        loadData();
        editBtn.setOnClickListener(view -> {
            if (comicId != null){
                Intent intent = new Intent(this, EditComicActivity.class);
                intent.putExtra("COMICID", comicId);
                startActivity(intent);
            }
        });
        addChapterBtn.setOnClickListener(view -> {
            if (comicId != null){
                Intent intent = new Intent(this, AddChapterActivity.class);
                intent.putExtra("COMICID", comicId);
                startActivity(intent);
            }
        });
    }

    public String getComicId() {
        return comicId;
    }

    private void initalize(){
        comicNameTv = findViewById(R.id.comic_name);
        comicUpdateTv = findViewById(R.id.update_date);
        comicStateTv = findViewById(R.id.comic_state);
        comicViewNumberTv = findViewById(R.id.view_number);
        comicContentTv = findViewById(R.id.content_tv);
        comicCoverIV = findViewById(R.id.comic_cover);
        categoriesView = findViewById(R.id.category_comic);
        chaptersView = findViewById(R.id.chapters_view);
        chaptersView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        editBtn = findViewById(R.id.edit_comic_btn);
        addChapterBtn = findViewById(R.id.add_chapter_btn);
        context = this;

        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
    }

    private void loadData(){
        comicId = getIntent().getStringExtra("COMICID");
        Query query = databaseReference.child(comicId);

        query.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Comic comic = snapshot.getValue(Comic.class);
                comicNameTv.setText(comic.getName());
                String comicUpdate = comic.getUpdate() != null
                        ? "Cập nhật lúc " + comic.getUpdate().toString()
                        : "Chưa có chương nào";
                comicUpdateTv.setText(comicUpdate);
                comicStateTv.setText(comic.getState());
                comicViewNumberTv.setText(comic.getView() + "");
                comicContentTv.setText(comic.getContent());
                Picasso.get()
                        .load(comic.getCover())
                        .fit()
                        .into(comicCoverIV);
                categoriesView.setAdapter(new CategoryAdapter(comic.getCategory()));
                chaptersView.setAdapter(new ChapterAdapter(comic.getChapters(), context, comicId));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
