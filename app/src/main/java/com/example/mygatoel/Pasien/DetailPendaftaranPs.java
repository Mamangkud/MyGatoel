package com.example.mygatoel.Pasien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.mygatoel.Admin.DetailPendaftaranAd;
import com.example.mygatoel.Admin.MainAdmin;
import com.example.mygatoel.Admin.RiwayatPendaftaranPasien;
import com.example.mygatoel.R;

public class DetailPendaftaranPs extends AppCompatActivity {
    private TextView dt_no_antrian, dt_norm, dt_nama, dt_tgl_pemeriksaan, dt_poli, dt_dokter, dt_alamat;
    private String id, norm, nama, alamat,tgl_pemeriksaan, poli, dokter;
    private long no_antrian;
    private Button bt_kembali_dps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_pendaftaran_ps);
        initView();
        ambilDataPendaftaran();
        bt_kembali_dps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initView(){
        dt_no_antrian = findViewById(R.id.dt_no_antrian);
        dt_norm = findViewById(R.id.dt_norm);
        dt_nama = findViewById(R.id.dt_nama);
        dt_alamat = findViewById(R.id.dt_alamat);
        dt_tgl_pemeriksaan = findViewById(R.id.dt_tgl_pemeriksaan);
        dt_poli = findViewById(R.id.dt_poli);
        dt_dokter = findViewById(R.id.dt_dokter);
        bt_kembali_dps = findViewById(R.id.bt_kembali_dps);
    }

    private void ambilDataPendaftaran(){
        Bundle bundle = getIntent().getExtras();
        if (getIntent().getExtras() != null){
            id = bundle.getString("id");
            no_antrian = bundle.getLong("no_antrian");
            norm = bundle.getString("norm");
            nama = bundle.getString("nama");
            alamat = bundle.getString("alamat");
            tgl_pemeriksaan = bundle.getString("tgl_pemeriksaan");
            poli = bundle.getString("poli");
            dokter = bundle.getString("dokter");

            dt_no_antrian.setText(String.valueOf(no_antrian));
            dt_norm.setText(norm);
            dt_nama.setText(nama);
            dt_alamat.setText(alamat);
            dt_tgl_pemeriksaan.setText(tgl_pemeriksaan);
            dt_poli.setText(poli);
            dt_dokter.setText(dokter);
        }
    }
}
