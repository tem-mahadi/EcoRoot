package com.temmahadi.ecoroot;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link frag_home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class frag_home extends Fragment {

    RecyclerView recyclerView;
    List<UserData> dataList;
    MyAdapter adapter;
    SearchView searchView;
    FloatingActionButton fab;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public frag_home() {
        // Required empty public constructor
    }
    public static frag_home newInstance(String param1, String param2) {
        frag_home fragment = new frag_home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("frag_home", "onCreateView");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("frag_home", "onCreateView");
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_frag_home, container, false);
        recyclerView= view.findViewById(R.id.recyclerView);
        searchView= view.findViewById(R.id.search);
        searchView.clearFocus();
        fab=view.findViewById(R.id.fab);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dataList = new ArrayList<>();
        adapter = new MyAdapter(requireContext(), dataList);
        recyclerView.setAdapter(adapter);

        DatabaseReference dr = FirebaseDatabase.getInstance().getReference("Usage").child(mParam1);
        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) { // Check if data exists
                    dataList.clear(); // Clear dataList before adding new data
                    for (DataSnapshot child : snapshot.getChildren()) {
                        UserData userData = child.getValue(UserData.class);
                        dataList.add(userData);
                    }
                    adapter.setDataList(dataList); // Notify adapter of data changes
                } else {
                    // Handle case where no data exists in the snapshot
                    Log.d("firebase", "onDataChange: error");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Log error message or handle the error appropriately
                Log.e("Firebase", "Error fetching data: " + error.getMessage());
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<UserData> searchList = new ArrayList<>();
                for (UserData dataClass: dataList) {
                    if (dataClass.getTitle().toLowerCase().contains(s.toLowerCase())) {
                        searchList.add(dataClass);
                    }
                }
                // Update dataList with searchList
                adapter.searchDataList(searchList);
                return true;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), UploadData.class);
                intent.putExtra("username",mParam1);
                startActivity(intent);
            }
        });
        return view;
    }
}