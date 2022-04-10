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

import com.example.mygatoel.Pasien.MainPasien;
import com.example.mygatoel.Pasien.PendaftaranOnline;
import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Dokter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class TambahDokter extends AppCompatActivity {
    private String[] itemPoli =
            {"Sp. Jantung & Pembuluh Darah", "Sp. Mata", "Sp. Kulit/Kelamin",
                    "Sp. Anak", "Sp. Saraf", "Sp. Bedah Tulang", "Sp. Bedah Umum"};
    private String[] hari = {"Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};
    private AutoCompleteTextView autoCompleteTextView;
    private AutoCompleteTextView daftarHari;
    private ArrayAdapter<String> adapterItems, adapterItems2;
    private Button bt_tambah;
    private ImageView img_kembali_td;
    private EditText tb_nama_dokter, tb_jadwal, tb_kuota, tb_awal, tb_akhir;
    private String id, item, namadokter, jadwal, jam_awal, jam_akhir, daftar_hari;
    int jam, menit;
    private long kuota;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tambah_dokter);
    /*    getSupportActionBar().hide();*/
        initView();

        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item_poli,itemPoli);
        autoCompleteTextView.setAdapter(adapterItems);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               item = parent.getItemAtPosition(position).toString();
            }
        });

        adapterItems2 = new ArrayAdapter<String>(this,R.layout.daftar_hari, hari);
        daftarHari.setAdapter(adapterItems2);
        daftarHari.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                daftar_hari = parent.getItemAtPosition(position).toString();
            }
        });


        loadTimePicker();

        img_kembali_td.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TambahDokter.this, MainAdmin.class));
                finish();
            }
        });

        bt_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tambahDokter();
            }
        });
    }

    private void initView(){
        img_kembali_td = findViewById(R.id.img_kembali_td);
        autoCompleteTextView = findViewById(R.id.list_poli);
        daftarHari = findViewById(R.id.tb_hari);
        bt_tambah = findViewById(R.id.bt_tambah);
        tb_nama_dokter = findViewById(R.id.tb_nama_dokter);
        tb_jadwal = findViewById(R.id.tb_hari);
        tb_awal = findViewById(R.id.tb_jam_awal);
        tb_akhir = findViewById(R.id.tb_jam_akhir);
        tb_kuota = findViewById(R.id.tb_kuota);
        reference = FirebaseDatabase.getInstance().getReference();
    }

    private void loadTimePicker(){
        tb_awal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        TambahDokter.this, new TimePickerDialog.OnTimeSetListener() {
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
                        TambahDokter.this, new TimePickerDialog.OnTimeSetListener() {
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


    private void tambahDokter(){
        namadokter = tb_nama_dokter.getText().toString().trim();
        jadwal = tb_jadwal.getText().toString().trim();
        jam_awal = tb_awal.getText().toString().trim();
        jam_akhir = tb_akhir.getText().toString().trim();

        if (namadokter.isEmpty()){
            tb_nama_dokter.setError("Harap isi nama dokter");
        } else if (jadwal.isEmpty()){
            tb_jadwal.setError("Harap isi jadwal dokter");
        } else if (tb_kuota.getText().toString().trim().isEmpty()){
            tb_kuota.setError("Harap isi kuota dokter");
        } else {
            kuota = Integer.parseInt(tb_kuota.getText().toString().trim());
            simpanData();
        }
    }

    private void simpanData(){
        id = reference.push().getKey();
        Dokter dkt = new Dokter(id, namadokter, item, daftar_hari, kuota, jam_awal, jam_akhir);
        if (dkt !=null){
            reference.child("Dokter").child(id).setValue(dkt).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Tambah Dokter Berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TambahDokter.this, MainAdmin.class));
                    finish();
                }
            });
        }
    }
}
