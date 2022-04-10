package com.example.mygatoel.Pasien;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Pendaftaran;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class RiwayatPendaftaran extends Fragment {
    private RecyclerView rv_riwayatp;
    private RiwayatPsAdapter rpa;
    private ArrayList<Pendaftaran> pendaftaranModel = new ArrayList<>();
    private DatabaseReference ref;
    private FirebaseAuth auth;
    TextView tv_no_data;
    ImageView img_no_data;
    private CardView cr_lis_pendaftaran;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.riwayat_pendaftaran, container, false);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        initView(view);
        rpa = new RiwayatPsAdapter(pendaftaranModel, getContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_riwayatp.setLayoutManager(layoutManager);
        rv_riwayatp.setAdapter(rpa);
        loadData();
        return view;
    }

    private void initView(View view){
        rv_riwayatp = view.findViewById(R.id.rv_riwayatp);
        cr_lis_pendaftaran = view.findViewById(R.id.cr_lis_pendaftaran);
        tv_no_data = view.findViewById(R.id.tv_no_data);
        img_no_data = view.findViewById(R.id.img_no_data);
    }

    private void loadData(){
        ref.child("List_Pendaftaran_Pasien").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                long temp = snapshot.getChildrenCount();
                if(temp == 0){
                    img_no_data.setVisibility(View.VISIBLE);
                    tv_no_data.setVisibility(View.VISIBLE);
                    Log.d("nodata1",String.valueOf(temp));
                } else {
                    img_no_data.setVisibility(View.INVISIBLE);
                    tv_no_data.setVisibility(View.INVISIBLE);
                    for (DataSnapshot i : snapshot.getChildren()) {
                        loadDataPendaftaran(i.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void loadDataPendaftaran(String id){
        ref.child("Pendaftaran").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Pendaftaran pendaftaran = snapshot.getValue(Pendaftaran.class);
                if (pendaftaran !=null){
                    pendaftaranModel.add(pendaftaran);

                }
                rpa.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}