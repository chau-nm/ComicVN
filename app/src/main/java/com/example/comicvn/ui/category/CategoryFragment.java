package com.example.comicvn.ui.category;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private View view;
    private RecyclerView comicsView;
    private List<Comic> comics;
    private ComicAdapter comicAdapter;
    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_category, container, false);

        comicsView = view.findViewById(R.id.comics_view);
        comics = new ArrayList<>();
        comicAdapter = new ComicAdapter(comics, this.getContext());
        comicsView.setAdapter(comicAdapter);
        comicsView.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        loadData();
        return view;
    }

    public void loadData(){
        String category = getActivity().getIntent().getStringExtra("CATEGORY");
        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("TAG", snapshot.toString());
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Comic comic = dataSnapshot.getValue(Comic.class);
                    if(comic.getCategory().contains(category)) comics.add(comic);
                }
                comicAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}
