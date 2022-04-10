package com.example.mygatoel.Pasien;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygatoel.R;

public class StatusPendaftaran extends AppCompatActivity {
    private Button bt_back4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_pendaftaran);
        bt_back4 = findViewById(R.id.bt_back4);
        bt_back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatusPendaftaran.this, MainPasien.class));
                finish();
            }
        });
    }
}
