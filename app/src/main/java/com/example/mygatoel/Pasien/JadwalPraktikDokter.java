package com.example.mygatoel.Pasien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Dokter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class JadwalPraktikDokter extends AppCompatActivity {

    private RecyclerView rv_jadwal_dokter;
    private JadwalDokterAdapter rpa;
    private ArrayList<Dokter> dokterModel = new ArrayList<>();
    private ArrayAdapter<String> adapterItem;
    private DatabaseReference ref;
    private ImageView img_kembali_jpd;
    private AutoCompleteTextView actv_pilih_poli;
    private String item;
    private String[] daftarPoli =
            {"Sp. Jantung & Pembuluh Darah", "Sp. Mata", "Sp. Kulit/Kelamin",
                    "Sp. Anak", "Sp. Saraf", "Sp. Bedah Tulang", "Sp. Bedah Umum"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jadwal_praktik_dokter);
        /* getSupportActionBar().hide();*/
        img_kembali_jpd = findViewById(R.id.img_kembali_jpd);
        ref = FirebaseDatabase.getInstance().getReference();
        rv_jadwal_dokter = findViewById(R.id.rv_jadwal_dokter);
        actv_pilih_poli = findViewById(R.id.pt_fill_poli);
        rpa = new JadwalDokterAdapter(dokterModel, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv_jadwal_dokter.setLayoutManager(layoutManager);
        rv_jadwal_dokter.setAdapter(rpa);
        ambilJadwalPraktikDokter();
        filter_jadwal_Dokter();
        img_kembali_jpd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(JadwalPraktikDokter.this, MainPasien.class));
            }
        });
    }

    private void filter_jadwal_Dokter() {
        adapterItem = new ArrayAdapter<String>(this, R.layout.list_item_poli, daftarPoli);
        actv_pilih_poli.setAdapter(adapterItem);
        actv_pilih_poli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                ambilPilihanJadwal();
            }
        });
    }

    private void ambilPilihanJadwal(){
        dokterModel.clear();
        ref.child("Dokter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    Dokter temp = i.getValue(Dokter.class);
                    if(temp.getPoli().equals(item) ){
                        dokterModel.add(temp);
                    }
                }
                rpa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void ambilJadwalPraktikDokter() {
        dokterModel.clear();
        ref.child("Dokter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    Dokter temp = i.getValue(Dokter.class);
                    if(temp!=null){
                        dokterModel.add(temp);
                    }
                }
                rpa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}