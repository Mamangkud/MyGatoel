package com.example.mygatoel.Admin;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygatoel.R;

public class DetailPendaftaranAd extends AppCompatActivity {
    private TextView dt_no_antrian_ad, dt_norm_ad, dt_nama_ad, dt_tgl_pemeriksaan_ad, dt_poli_ad, dt_dokter_ad, dt_alamat_ad;
    private String id, norm, nama, alamat ,tgl_pemeriksaan, poli, dokter;
    private long no_antrian;
    private Button bt_kembali_dpad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_pendaftaran_ad);


        initView();
        ambilDataPendaftaran();
        bt_kembali_dpad.setOnClickListener(new View.OnClickListener() {
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
        dt_no_antrian_ad = findViewById(R.id.dt_no_antrian_ad);
        dt_norm_ad = findViewById(R.id.dt_norm_ad);
        dt_nama_ad = findViewById(R.id.dt_nama_ad);
        dt_alamat_ad = findViewById(R.id.dt_alamat_ad);
        dt_tgl_pemeriksaan_ad = findViewById(R.id.dt_tgl_pemeriksaan_ad);
        dt_poli_ad = findViewById(R.id.dt_poli_ad);
        dt_dokter_ad = findViewById(R.id.dt_dokter_ad);
        bt_kembali_dpad = findViewById(R.id.bt_kembali_dpad);
    }

    private void ambilDataPendaftaran() {
        Bundle bundle = getIntent().getExtras();
        if (getIntent().getExtras() != null) {
            id = bundle.getString("id");
            no_antrian = bundle.getLong("no_antrian");
            norm = bundle.getString("norm");
            nama = bundle.getString("nama");
            alamat = bundle.getString("alamat");
            tgl_pemeriksaan = bundle.getString("tgl_pemeriksaan");
            poli = bundle.getString("poli");
            dokter = bundle.getString("dokter");

            dt_no_antrian_ad.setText(String.valueOf(no_antrian));
            dt_norm_ad.setText(norm);
            dt_nama_ad.setText(nama);
            dt_alamat_ad.setText(alamat);
            dt_tgl_pemeriksaan_ad.setText(tgl_pemeriksaan);
            dt_poli_ad.setText(poli);
            dt_dokter_ad.setText(dokter);
        }
    }
}
