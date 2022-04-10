package com.example.mygatoel.Admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygatoel.Pasien.DetailPendaftaranPs;
import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Dokter;
import com.example.mygatoel.Entity.Pendaftaran;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RiwayatAdAdapter extends RecyclerView.Adapter<RiwayatAdAdapter.ViewHolder> {
    private ArrayList<Pendaftaran> pendaftaranModel;
    private DatabaseReference ref;
    private Context ctx;

    public RiwayatAdAdapter(ArrayList<Pendaftaran> pendaftaranModel, Context ctx) {
        this.pendaftaranModel = pendaftaranModel;
        ref = FirebaseDatabase.getInstance().getReference();
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public RiwayatAdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cr_list_pendaftaran, parent, false);
        ViewHolder vh  = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatAdAdapter.ViewHolder holder, int position) {
        holder.tv_poli_pp2.setText(pendaftaranModel.get(position).getPoli());
        loadDokter(pendaftaranModel.get(position).getId_dokter(), holder);
        holder.tv_noantrian1.setText(String.valueOf(pendaftaranModel.get(position).getNo_antrian()));
        Pendaftaran pendaftaran = pendaftaranModel.get(position);
        holder.tv_tglPemeriksaan.setText(pendaftaranModel.get(position).getTgl_pemeriksaan());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPengguna(pendaftaran, pendaftaranModel.get(position).getId_pengguna(),holder.tv_nama_dokter2.getText().toString() );
            }
        });
    }


    private void loadPengguna(Pendaftaran pendaftaran, String id_pengguna, String nama_dokter){
        ref.child("Pasien").child(id_pengguna).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent = new Intent(ctx, DetailPendaftaranAd.class);
                intent.putExtra("nama", snapshot.child("nama_lengkap").getValue().toString());
                intent.putExtra("norm", snapshot.child("no_rm").getValue().toString());
                intent.putExtra("id", pendaftaran.getId());
                intent.putExtra("no_antrian", pendaftaran.getNo_antrian());
                intent.putExtra("alamat", pendaftaran.getAlamat());
                intent.putExtra("tgl_pemeriksaan", pendaftaran.getTgl_pemeriksaan());
                intent.putExtra("poli", pendaftaran.getPoli());
                intent.putExtra("dokter", nama_dokter);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadDokter(String id_dokter, RiwayatAdAdapter.ViewHolder holder){
        ref.child("Dokter").child(id_dokter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Dokter dokter = snapshot.getValue(Dokter.class);
                holder.tv_nama_dokter2.setText(dokter.getNama_dokter());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return pendaftaranModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_poli_pp2, tv_nama_dokter2, tv_noantrian1, tv_tglPemeriksaan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_poli_pp2 = itemView.findViewById(R.id.tv_poli_jpd);
            tv_nama_dokter2 = itemView.findViewById(R.id.tv_nama_dokter_jpd);
            tv_noantrian1 = itemView.findViewById(R.id.tv_noantrian_jpd);
            tv_tglPemeriksaan = itemView.findViewById(R.id.tv_tglPemeriksaan_jpd);
        }
    }
}
