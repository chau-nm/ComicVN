package com.example.comicvn.ui.comicdetail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Caculation;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.ui.admin.view.ChapterAdapter;
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
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comic_detail_activity);
        initalize();
        loadData();
    }

    private void initalize(){
        comicNameTv = findViewById(R.id.comic_name);
        comicUpdateTv = findViewById(R.id.update_date);
        comicStateTv = findViewById(R.id.comic_state);
        comicViewNumberTv = findViewById(R.id.view_number);
        comicContentTv = findViewById(R.id.content_tv);
        comicCoverIV = findViewById(R.id.comic_cover);
        categoriesView = findViewById(R.id.category_comic);
        categoriesView.setLayoutManager(new GridLayoutManager(this, 3));
        chaptersView = findViewById(R.id.chapters_view);
        chaptersView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        context = this;

        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
    }

    public String getComicId() {
        return comicId;
    }



    private void loadData(){
        comicId = getIntent().getStringExtra("COMICID");
        Query query = databaseReference.child(comicId);

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Comic comic = snapshot.getValue(Comic.class);
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
                chaptersView.setAdapter(new ChapterAdapter(comic.getChapters(), context, comicId));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
