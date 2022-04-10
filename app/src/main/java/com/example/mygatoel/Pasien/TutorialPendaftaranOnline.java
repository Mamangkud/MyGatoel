package com.example.mygatoel.Pasien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygatoel.R;

public class TutorialPendaftaranOnline extends AppCompatActivity {
    private ImageView img_kembali_tp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial_pendaftaran_online);
        img_kembali_tp = findViewById(R.id.img_kembali_tp);

        img_kembali_tp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TutorialPendaftaranOnline.this, MainPasien.class));
                finish();
            }
        });
    }
}
