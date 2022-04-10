package com.example.mygatoel.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygatoel.Guest.Login;
import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Dokter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DokterAdapter extends RecyclerView.Adapter<DokterAdapter.ViewHolder> {

    private ArrayList<Dokter> dokterModel;
    private DatabaseReference ref;
    private Context ctx;
    public DokterAdapter(ArrayList<Dokter> dokterModel, Context context) {
        this.dokterModel = dokterModel;
        this.ctx = context;
        ref = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public DokterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewTypet) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cr_list_dokter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(DokterAdapter.ViewHolder holder, int position) {
        holder.tv_poli.setText(dokterModel.get(position).getPoli());
        holder.tv_dokter.setText(dokterModel.get(position).getNama_dokter());
        Dokter dokter = dokterModel.get(position);
        holder.menu_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.edit_menu:
                                updateDokter(dokter);
                                break;
                            case R.id.delete_menu:
                                hapusDokter(v,String.valueOf(dokter.getId()), dokter);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dokterModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_dokter, tv_poli;
        ImageView menu_popup;

        public ViewHolder(View itemView) {
            super(itemView);
            tv_poli = itemView.findViewById(R.id.tv_poli);
            tv_dokter = itemView.findViewById(R.id.tv_nama_dokter);
            menu_popup = itemView.findViewById(R.id.img_menu);
        }
    }

   private void updateDokter(Dokter dkt){
        Intent intent = new Intent(ctx, UbahInformasiDokter.class);
        intent.putExtra("id", dkt.getId());
        intent.putExtra("nama_dokter", dkt.getNama_dokter());
        intent.putExtra("poli", dkt.getPoli());
        intent.putExtra("jadwal", dkt.getJadwal());
        intent.putExtra("kuota", dkt.getKuota());
        intent.putExtra("jam_awal", dkt.getJam_awal());
        intent.putExtra("jam_akhir", dkt.getJam_akhir());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    private void hapusDokter (View v, String id_dokter, Dokter dokter){
        final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Apakah anda yakin untuk menghapus data dokter tersebut?");
        builder.setCancelable(true);
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(v.getContext(), "Menghapus " + dokter.getNama_dokter() , Toast.LENGTH_SHORT).show();
                ref = FirebaseDatabase.getInstance().getReference();
                ref.child("Dokter").child(id_dokter).removeValue();
                notifyDataSetChanged();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
   }
}
