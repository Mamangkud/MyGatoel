package com.example.mygatoel.Pasien;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mygatoel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BerandaPasien extends Fragment {
    private FirebaseAuth auth;
    private TextView sapa_pasien, tv_tutorial;
    private DatabaseReference ref;
    private LinearLayout daftar_online, jadwal_dokter;
    private FirebaseDatabase firebaseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.beranda_pasien, container, false);
        initView(view);
        daftarOnline();
        jadwalPraktikDokter();
        tutorialPendaftaran();
        return view;
    }

    private void initView(View view){
        sapa_pasien = view.findViewById(R.id.sapa_pasien);
        daftar_online = view.findViewById(R.id.daftar_online);
        jadwal_dokter = view.findViewById(R.id.jadwal_dokter);
        tv_tutorial = view.findViewById(R.id.tv_tutorial_pendaftaran);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference().child("Pasien");
        loadInfoUser();

    }

    private void daftarOnline(){
        daftar_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PendaftaranOnline.class);
                startActivity(intent);
            }
        });
    }

    private void jadwalPraktikDokter(){
        jadwal_dokter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), JadwalPraktikDokter.class);
                startActivity(intent);
            }
        });
    }

    private void tutorialPendaftaran(){
        tv_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TutorialPendaftaranOnline.class);
                startActivity(intent);
            }
        });
    }

    private void loadInfoUser(){
        FirebaseUser currentUser = auth.getCurrentUser();
        assert currentUser != null;
        ref = ref.child(currentUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sapa_pasien.setText(snapshot.child("nama_lengkap").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}