package com.example.mygatoel.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Pendaftaran;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RiwayatPendaftaranPasien extends Fragment {
    private RecyclerView rv_riwayatAd;
    private RiwayatAdAdapter rpa;
    private ArrayList<Pendaftaran> pendaftaranModel = new ArrayList<>();
    private DatabaseReference ref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.riwayat_pendaftaran_pasien, container, false);

        ref = FirebaseDatabase.getInstance().getReference();
        rv_riwayatAd = view.findViewById(R.id.rv_riwayatAd);
        rpa = new RiwayatAdAdapter(pendaftaranModel, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_riwayatAd.setLayoutManager(layoutManager);
        rv_riwayatAd.setAdapter(rpa);
        ambilDataPendaftaran();
        return view;
    }

    private void ambilDataPendaftaran(){
        ref.child("Pendaftaran").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()){
                    Pendaftaran temp = i.getValue(Pendaftaran.class);
                    if (temp !=null){
                        pendaftaranModel.add(temp);
                    }
                }

                rpa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}