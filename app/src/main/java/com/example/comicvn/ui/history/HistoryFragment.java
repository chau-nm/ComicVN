package com.example.comicvn.ui.history;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Chapter;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.sqlite.HistoryDb;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HistoryFragment extends Fragment {

    private View view;
    private HistoryDb historyDb;
    private DatabaseReference databaseReference;
    private List<Comic> comics;
    private List<Chapter> chapters;
    private RecyclerView comicsView;
    private ComicAdapter comicAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_history, container, false);
        initialize();
        loadData();
        return view;
    }

    private void initialize(){
        comics = new ArrayList<>();
        chapters = new ArrayList<>();
        comicAdapter = new ComicAdapter(comics, chapters, this.getContext());
        comicsView = view.findViewById(R.id.comics_view);
        comicsView.setAdapter(comicAdapter);
        comicsView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
    }

    private void loadData(){
        historyDb = new HistoryDb(this.getContext());
        Map<String, String> history = historyDb.getHistory();
        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Comic comic = dataSnapshot.getValue(Comic.class);
                    System.out.println(comic);
                    if(history.containsKey(comic.getId())){
                        Chapter chapter = comic.getChapter(history.get(comic.getId()));
                        if(chapter != null){
                            comics.add(comic);
                            chapters.add(chapter);
                        }
                    }
                }
                comicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
