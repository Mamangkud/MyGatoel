package com.example.mygatoel.Admin;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mygatoel.Guest.Login;
import com.example.mygatoel.Pasien.MainPasien;
import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Dokter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BerandaAdmin extends Fragment {
    private ImageView iv_tambah, logout_ad;
    private RecyclerView rv_admin;
    private DokterAdapter dokterAdapter;
    private ArrayList<Dokter> dokterModel = new ArrayList<>();
    private DatabaseReference ref;
    private ArrayAdapter<String> adapterItem;
    private AutoCompleteTextView actv_pilih_poli_ad;
    private String item;
    private String[] daftarPoli =
            {"Sp. Jantung & Pembuluh Darah", "Sp. Mata", "Sp. Kulit/Kelamin",
                    "Sp. Anak", "Sp. Saraf", "Sp. Bedah Tulang", "Sp. Bedah Umum"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beranda_admin, container, false);
        ref = FirebaseDatabase.getInstance().getReference().child("Dokter");
        iv_tambah = view.findViewById(R.id.iv_tambah);
        rv_admin = view.findViewById(R.id.rv_admin);
        logout_ad = view.findViewById(R.id.logout_ad);
        actv_pilih_poli_ad = view.findViewById(R.id.pt_fill_poli_ad);
        dokterAdapter = new DokterAdapter(dokterModel, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_admin.setLayoutManager(layoutManager);
        rv_admin.setAdapter(dokterAdapter);
        logoutAd();
        loadTambahDokter();
        loadData();
        filter_jadwal_Dokter();
        return view;
    }

    private void filter_jadwal_Dokter() {
        adapterItem = new ArrayAdapter<String>(getContext(), R.layout.list_item_poli, daftarPoli);
        actv_pilih_poli_ad.setAdapter(adapterItem);
        actv_pilih_poli_ad.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                ambilPilihanJadwal();
            }
        });
    }

    private void ambilPilihanJadwal(){
        dokterModel.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()) {
                    Dokter temp = i.getValue(Dokter.class);
                    if(temp.getPoli().equals(item) ){
                        dokterModel.add(temp);
                    }
                }
                dokterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void loadData(){
        dokterModel.clear();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                dokterModel.clear();
                for (DataSnapshot i : snapshot.getChildren()){
                    Dokter temp = i.getValue(Dokter.class);
                    if(temp !=null){
                       dokterModel.add(temp) ;
                    }
                }
                dokterAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void logoutAd(){
        logout_ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private void loadTambahDokter(){
        iv_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TambahDokter.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}