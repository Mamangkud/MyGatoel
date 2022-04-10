package com.example.mygatoel.Pasien;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Dokter;
import com.example.mygatoel.Entity.Pendaftaran;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RiwayatPsAdapter extends RecyclerView.Adapter<RiwayatPsAdapter.ViewHolder> {

    private ArrayList<Pendaftaran> pendaftaranModel;
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private Context ctx;

    public RiwayatPsAdapter(ArrayList<Pendaftaran> pendaftaranModel, Context ctx) {
        this.pendaftaranModel = pendaftaranModel;
        this.ctx = ctx;
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public RiwayatPsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cr_list_pendaftaran_pasien, parent, false);
        ViewHolder vh  = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RiwayatPsAdapter.ViewHolder holder, int position) {
        holder.tv_poli_pp1.setText(pendaftaranModel.get(position).getPoli());
        loadDokter(pendaftaranModel.get(position).getId_dokter(), holder);
        holder.tv_noantrian.setText(String.valueOf(pendaftaranModel.get(position).getNo_antrian()));
        Pendaftaran pendaftaran = pendaftaranModel.get(position);
        holder.tv_tglPemeriksaanAd.setText(pendaftaranModel.get(position).getTgl_pemeriksaan());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               loadPengguna(pendaftaran, pendaftaranModel.get(position).getId_pengguna(),holder.tv_nama_dokter1.getText().toString() );
           }
       });
    }

    private void loadPengguna(Pendaftaran pendaftaran, String id_pengguna, String nama_dokter){
        ref.child("Pasien").child(id_pengguna).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Intent intent = new Intent(ctx, DetailPendaftaranPs.class);
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

    private void loadDokter(String id_dokter, ViewHolder holder){
        ref.child("Dokter").child(id_dokter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Dokter dokter = snapshot.getValue(Dokter.class);
                holder.tv_nama_dokter1.setText(dokter.getNama_dokter());
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

        TextView tv_poli_pp1, tv_nama_dokter1, tv_noantrian, tv_tglPemeriksaanAd;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_poli_pp1 = itemView.findViewById(R.id.tv_poli_riwayat_ad);
            tv_nama_dokter1 = itemView.findViewById(R.id.tv_nama_dokter_riwayatAd);
            tv_noantrian = itemView.findViewById(R.id.tv_noantrian_riwayatAd);
            tv_tglPemeriksaanAd = itemView.findViewById(R.id.tv_tglPemeriksanAd);
        }
    }
}
