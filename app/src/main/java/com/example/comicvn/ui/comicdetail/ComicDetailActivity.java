package com.example.comicvn.ui.comicdetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.comicvn.R;
import com.example.comicvn.obj.Caculation;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.ui.readcomic.ReadComicActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ComicDetailActivity extends AppCompatActivity {

    private TextView comicNameTv, comicUpdateTv,
            comicStateTv, comicViewNumberTv, comicContentTv;
    private ImageView comicCoverIV;
    private RecyclerView categoriesView, chaptersView;
    private DatabaseReference databaseReference;
    private String comicId;
    private AppCompatButton startFirstChapBtn;
    private Toolbar toolbar;
    private Comic comic;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_detail_activity);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initalize();
        setStartFirstChapBtn();
        loadData();
    }

    private void initalize(){
        comicNameTv = findViewById(R.id.comic_name);
        comicUpdateTv = findViewById(R.id.update_date);
        comicStateTv = findViewById(R.id.comic_state);
        comicViewNumberTv = findViewById(R.id.view_number);
        comicContentTv = findViewById(R.id.comic_content_tv);
        comicCoverIV = findViewById(R.id.comic_cover);
        startFirstChapBtn = findViewById(R.id.start_first_chap_btn);
        categoriesView = findViewById(R.id.category_comic);
        categoriesView.setLayoutManager(new GridLayoutManager(this, 3));
        categoriesView.setNestedScrollingEnabled(false);
        chaptersView = findViewById(R.id.chapters_view);
        chaptersView.setNestedScrollingEnabled(false);
        chaptersView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
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

    public void setStartFirstChapBtn(){
        startFirstChapBtn.setOnClickListener(view ->{
            if (comic != null && comic.getChapters() != null && comic.getChapters().size() > 0){
                Intent intent = new Intent(ComicDetailActivity.this, ReadComicActivity.class);
                intent.putExtra("COMICID", comicId);
                intent.putExtra("CHAPTERID", comic.getChapters().get(0).getId());
                intent.putExtra("CHAPTERNUMBER", comic.getChapters().get(0).getNumber());
                startActivity(intent);
            }
        });
    }

    public String getComicId() {
        return comicId;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void loadData(){
        comicId = getIntent().getStringExtra("COMICID");
        Query query = databaseReference.child(comicId);
        query.get().addOnSuccessListener(dataSnapshot -> {
            comic = dataSnapshot.getValue(Comic.class);
            comicNameTv.setText(comic.getName());
            String comicUpdate = comic.getUpdate() != null
                    ? "Cập nhật " + Caculation.getMessageDate(comic.getUpdate())
                    : "Chưa có chương nào";
            comicUpdateTv.setText(comicUpdate);
            comicStateTv.setText(comic.getState());
            comicViewNumberTv.setText(comic.getView() + "");
            comicContentTv.setText(comic.getContent());
            Picasso.get()
                    .load(comic.getCover())
                    .fit()
                    .into(comicCoverIV);
            categoriesView.setAdapter(new CategoryAdapter(comic.getCategory(), ComicDetailActivity.this));
            chaptersView.setAdapter(new ChapterAdapter(comic.getChapters(), ComicDetailActivity.this, comic.getId()));
            comic.increaseView();
            databaseReference.child(comic.getId()).setValue(comic);
        });
    }
}
