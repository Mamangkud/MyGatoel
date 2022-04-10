package com.example.mygatoel.Admin;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Dokter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class UbahInformasiDokter extends AppCompatActivity {
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView daftarHari;
    private ArrayAdapter<String> adapterItems, adapterItems2;
    private DatabaseReference reference;
    private ImageView img_kembali_uid;
    EditText tb_nama_dokter, tb_jadwal, tb_kuota, tb_awal, tb_akhir;
    Button bt_edit;
    String id, nama_dokter, poli, jadwal, tempPoli, jam_awal, jam_akhir, tempHari;
    long kuota;
    int jam, menit;
    private String[] itemPoli =
            {"Sp. Jantung & Pembuluh Darah", "Sp. Mata", "Sp. Kulit/Kelamin",
                    "Sp. Anak", "Sp. Saraf", "Sp. Bedah Tulang", "Sp. Bedah Umum"};
    private String[] hari = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ubah_informasi_dokter);
       /* getSupportActionBar().hide();*/
        tb_nama_dokter = findViewById(R.id.tb_nama_dokter);
        tb_jadwal = findViewById(R.id.tb_daftar_hari);
        tb_kuota = findViewById(R.id.tb_kuota);
        tb_awal = findViewById(R.id.tb_jam_awal);
        tb_akhir = findViewById(R.id.tb_jam_akhir);
        bt_edit = findViewById(R.id.bt_edit);
        img_kembali_uid = findViewById(R.id.img_kembali_uid);
        autoCompleteTextView = findViewById(R.id.tb_poli);
        daftarHari = findViewById(R.id.tb_daftar_hari);
        reference = FirebaseDatabase.getInstance().getReference();

        updateDokter();
        loadAutoCompleteTextView();
        loadTimePicker();

        img_kembali_uid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UbahInformasiDokter.this, MainAdmin.class));
               finish();
            }
        });

        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanData();
            }
        });

    }

    private void updateDokter(){
        Bundle bundle = getIntent().getExtras();
        if (getIntent().getExtras() != null){
            id = bundle.getString("id");
            nama_dokter = bundle.getString("nama_dokter");
            poli = bundle.getString("poli");
            jadwal = bundle.getString("jadwal");
            kuota = bundle.getLong("kuota");
            jam_awal = bundle.getString("jam_awal");
            jam_akhir = bundle.getString("jam_akhir");

            tb_nama_dokter.setText(nama_dokter);
            autoCompleteTextView.setText(poli);
            tb_jadwal.setText(jadwal);
            tb_kuota.setText(Integer.toString((int) kuota));
            tb_awal.setText(jam_awal);
            tb_akhir.setText(jam_akhir);
        }
    }
    private void loadAutoCompleteTextView() {
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item_poli,itemPoli);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempPoli = parent.getItemAtPosition(position).toString();
            }
        });

        adapterItems2 = new ArrayAdapter<String>(this,R.layout.daftar_hari, hari);
        daftarHari.setAdapter(adapterItems2);
        daftarHari.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tempHari = parent.getItemAtPosition(position).toString();
            }
        });

    }

    private void loadTimePicker(){
        tb_awal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UbahInformasiDokter.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        jam = hourOfDay;
                        menit = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0, jam, menit);
                        tb_awal.setText(android.text.format.DateFormat.format("H:mm", calendar));
                    }
                }, 24, 0,true
                );
                timePickerDialog.updateTime(jam,menit);
                timePickerDialog.show();
            }
        });

        tb_akhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        UbahInformasiDokter.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        jam = hourOfDay;
                        menit = minute;
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(0,0,0, jam, menit);
                        tb_akhir.setText(android.text.format.DateFormat.format("H:mm", calendar));
                    }
                }, 24, 0,true
                );
                timePickerDialog.updateTime(jam,menit);
                timePickerDialog.show();
            }
        });
    }

    private void simpanData(){
        nama_dokter = tb_nama_dokter.getText().toString().trim();
        tempPoli = autoCompleteTextView.getText().toString().trim();
        tempHari = tb_jadwal.getText().toString().trim();
        jam_awal = tb_awal.getText().toString().trim();
        jam_akhir = tb_akhir.getText().toString().trim();

        if (nama_dokter.isEmpty()){
            tb_nama_dokter.setError("Harap isi nama dokter");
        } else if (tempHari.isEmpty()){
            tb_jadwal.setError("Harap isi hari dokter");
        } else if (tb_kuota.getText().toString().trim().isEmpty()){
            tb_kuota.setError("Harap isi kuota dokter");
        } else {
            kuota = Integer.parseInt(tb_kuota.getText().toString().trim());
            Dokter dkt = new Dokter(id, nama_dokter, tempPoli, tempHari,kuota, jam_awal, jam_akhir);
            if (dkt !=null){
                reference.child("Dokter").child(id).setValue(dkt).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Edit Dokter Berhasil", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(UbahInformasiDokter.this, MainAdmin.class));
                        finish();
                    }
                });
            }
        }
    }
}
