package com.example.comicvn.ui.home;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicvn.R;
import com.example.comicvn.obj.Comic;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View view;
    private RecyclerView forYouListView, newUpdateListView;

    private ForYouAdapter forYouAdapter;
    private NewUpdateAdapter newUpdateAdapter;
    private List<Comic> forYouList, newUpdateList;

    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater
            , @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("comics");
        setUpForYouLayout();
        setUpNewUpdateLayout();
        return view;
    }

    public void setUpForYouLayout(){
        forYouListView = view.findViewById(R.id.for_you_list);
        forYouListView.setLayoutManager(new LinearLayoutManager(view.getContext()
                , RecyclerView.HORIZONTAL, false));
        forYouList = new ArrayList<>();
        forYouAdapter = new ForYouAdapter(forYouList, getContext());
        forYouListView.setAdapter(forYouAdapter);
        forYouListView.smoothScrollBy(5,0);
        loadDataForYou();
        autoScollSlide();
    }

    public void setUpNewUpdateLayout(){
        newUpdateListView = view.findViewById(R.id.new_update_list);
        newUpdateListView.setLayoutManager(new GridLayoutManager(view.getContext()
                , 2));
        newUpdateListView.setNestedScrollingEnabled(false);
        newUpdateList = new ArrayList<>();
        newUpdateAdapter = new NewUpdateAdapter(newUpdateList, getContext());
        newUpdateListView.setAdapter(newUpdateAdapter);
        loadDataNewUpdate();
    }

    private void loadDataNewUpdate(){
        Query query = databaseReference.orderByChild("view").limitToFirst(10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post: snapshot.getChildren()){
                    Comic comic = post.getValue(Comic.class);
                    newUpdateList.add(comic);
                }
                newUpdateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadDataForYou(){
        Query query = databaseReference.orderByChild("view").limitToFirst(10);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post: snapshot.getChildren()){
                    Comic comic = post.getValue(Comic.class);
                    forYouList.add(comic);
                }
                newUpdateAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void autoScollSlide(){
        new CountDownTimer(60000, 1500) {
            int current = 0;
            @Override
            public void onTick(long l) {
                if (current < forYouList.size()){
                    forYouListView.scrollToPosition(current++);
                }else{
                    current = 0;
                }
            }
            @Override
            public void onFinish() {}
        }.start();
    }
}