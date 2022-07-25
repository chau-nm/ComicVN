package com.example.comicvn.ui.admin.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.example.comicvn.ui.admin.add.AddComicActivity;
import com.example.comicvn.ui.home.ForYouAdapter;
import com.example.comicvn.ui.home.NewUpdateAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminFragment extends Fragment {
    private View view;
    private RecyclerView comicsView;
    private ComicAdapter comicAdapter;
    private List<Comic> comics;
    private DatabaseReference databaseReference;
    private AppCompatImageButton addBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_admin, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        initialize();

        addBtn.setOnClickListener(view -> {
            Intent intent = new Intent(this.getContext(), AddComicActivity.class);
            startActivity(intent);
        });
        
        loadData();
        return view;
    }

    private void initialize(){
        comicsView = view.findViewById(R.id.comics_view);
        comicsView.setNestedScrollingEnabled(false);
        comics = new ArrayList<>();
        comicAdapter = new ComicAdapter(comics, this.getContext());
        comicsView.setAdapter(comicAdapter);
        comicsView.setLayoutManager(new LinearLayoutManager(this.getContext(),
                LinearLayoutManager.VERTICAL, false));
        addBtn = view.findViewById(R.id.add_comic);
    }

    private void loadData(){
        Query query = databaseReference.orderByChild("view");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post: snapshot.getChildren()){
                    Comic comic = post.getValue(Comic.class);
                        comics.add(comic);
                }
                comicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}