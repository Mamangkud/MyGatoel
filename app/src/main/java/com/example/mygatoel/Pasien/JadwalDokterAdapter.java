package com.example.mygatoel.Pasien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Dokter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class JadwalDokterAdapter extends RecyclerView.Adapter<JadwalDokterAdapter.ViewHolder> {
    private ArrayList<Dokter> dokterModel;
    private DatabaseReference ref;
    private Context ctx;

    public JadwalDokterAdapter(ArrayList<Dokter> dokterModel, Context ctx) {
        this.dokterModel = dokterModel;
        this.ctx = ctx;
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public JadwalDokterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cr_list_jadwal_dokter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull JadwalDokterAdapter.ViewHolder holder, int position) {
        holder.tv_poli2.setText(dokterModel.get(position).getPoli());
        holder.tv_nama_dokter2.setText(dokterModel.get(position).getNama_dokter());
        holder.tv_jadwal.setText(dokterModel.get(position).getJadwal()+
                " ("+dokterModel.get(position).getJam_awal()+" - Selesai)");
    }


    @Override
    public int getItemCount() {
        return dokterModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_poli2, tv_nama_dokter2, tv_sisa_kuota,tv_jadwal;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_poli2 = itemView.findViewById(R.id.tv_poli_jpd);
            tv_nama_dokter2 = itemView.findViewById(R.id.tv_nama_dokter_jpd);
          /*  tv_sisa_kuota = itemView.findViewById(R.id.tv_sisa_kuota);*/
            tv_jadwal = itemView.findViewById(R.id.tv_jadwal_jpd);
        }
    }


   /* private void loadJumlahAntrian(String id, long kuota, JadwalDokterAdapter.ViewHolder holder){
        String date = new SimpleDateFormat("d-M-yyyy", Locale.getDefault()).format(new Date());
        ref.child("Antrian_Pendaftaran").child(id).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              long jumlah_antrian = snapshot.getChildrenCount();
              holder.tv_sisa_kuota.setText("Sisa Kuota Hari ini: " + String.valueOf(kuota-jumlah_antrian));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }*/

}
