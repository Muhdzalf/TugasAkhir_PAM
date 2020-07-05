package com.example.projectakhirpam;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class HewanAdapter extends RecyclerView.Adapter<HewanAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<DataHewan> daftarHewan;
    private DatabaseReference databaseReference;
    private Context context;
    FirebaseRecyclerAdapter listener;

    public HewanAdapter(ArrayList<DataHewan> mhewan, Context ctx){
        daftarHewan = mhewan;
        context = ctx;
        //listener = (ListData) ctx;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvNama;
        TextView tvJenis;

        ViewHolder(View v){
            super(v);
            tvNama = (TextView) v.findViewById(R.id.tv_nama);
            tvJenis = (TextView) v.findViewById(R.id.tv_jenis);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final String nama = daftarHewan.get(position).getNama();
        final String ras = daftarHewan.get(position).getRas();

        holder.tvNama.setText(nama);
        holder.tvJenis.setText(ras);

    }

    @Override
    public int getItemCount() {
        return daftarHewan.size();
    }

    public interface FirebasDataListener{
        void onDeleteData(DataHewan hewan, int position);
    }
}
