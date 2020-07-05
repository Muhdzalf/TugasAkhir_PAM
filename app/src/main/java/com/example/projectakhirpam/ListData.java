package com.example.projectakhirpam;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ListData extends Fragment  implements  HewanAdapter.FirebasDataListener{
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DataHewan> daftarHewan;


    public ListData(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listdata, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.dataList);
        getActivity();

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        setDatabaseReference();

        return v;
    }

    private void setDatabaseReference (){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Hewan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                daftarHewan = new ArrayList<>();
                for (DataSnapshot noteDataSnapShot : snapshot.getChildren()){

                    DataHewan hewan = noteDataSnapShot.getValue(DataHewan.class);
                    hewan.setKey(noteDataSnapShot.getKey());

                    daftarHewan.add(hewan);
                }

                adapter = new HewanAdapter(daftarHewan, getActivity());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error.getDetails()+""+error.getMessage());
            }
        });
    }

    public static Intent getActIntent(Activity activity) {
        return new Intent(activity, ListData.class);
    }

    @Override
    public void onDeleteData(DataHewan hewan, final int position) {
        if (databaseReference != null){
            databaseReference.child("Barang")
                    .child(hewan.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Success delete", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

}