package com.example.mygatoel.Pasien;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mygatoel.R;
import com.example.mygatoel.Entity.Dokter;
import com.example.mygatoel.Entity.Pendaftaran;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PendaftaranOnline extends AppCompatActivity {
    private ImageView img_kembali_po;
    private AutoCompleteTextView actv_poli, actv_dokter;
    private ArrayAdapter<String> adapterItems, adapterItems2;
    private DatabaseReference ref;
    private FirebaseAuth auth;
    private Button bt_daftaro;
    private EditText pt_no_bpjs, pt_alamat, pt_poli, pt_dokter, pt_tgl_pemeriksaan;
    private String no_bjs, alamat, poli, id_dokter, tgl_pemeriksaan, id, id_pengguna, item, item2, dokter, status;
    private TextView tv_jadwalDokter, tv_sisaKuota, tv_status, tv_harap_tunggu_pendaftaran;
    private boolean telahTerdaftar;
    private int indeksDokter = 0;
    private long jumlahPendaftar = 0;
    long no_antrian = 0;

    private String[] daftarPoli =
            {"Sp. Jantung & Pembuluh Darah", "Sp. Mata", "Sp. Kulit/Kelamin",
                    "Sp. Anak", "Sp. Saraf", "Sp. Bedah Tulang", "Sp. Bedah Umum"};
    private ArrayList<String> hari = new ArrayList<>(Arrays.asList
            ("Minggu","Senin", "Selasa", "Rabu", "Kamis", "Jumat","Sabtu"));
    private String[] itemDokter;
    private ArrayList<Dokter> dokterModel = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pendaftaran_online);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference();
        img_kembali_po = findViewById(R.id.img_kembali_po);
        img_kembali_po.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PendaftaranOnline.this, MainPasien.class));
                finish();
            }
        });

        initView();
        pilihPoliDokter();
        ambilDokter();
        
        bt_daftaro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftarOnline();
            }
        });
    }

    private void initView(){
        pt_no_bpjs = findViewById(R.id.pt_no_bpjs);
        pt_alamat = findViewById(R.id.pt_alamat);
        bt_daftaro = findViewById(R.id.bt_daftaro);
        actv_poli = findViewById(R.id.pt_poli);
        actv_dokter = findViewById(R.id.pt_dokter);
        tv_jadwalDokter = findViewById(R.id.tv_jadwalDokter);
        tv_sisaKuota = findViewById(R.id.tv_sisaKuota);
        tv_status = findViewById(R.id.tv_statusDaftar);
/*      progressBar = findViewById(R.id.prog_3);
        tv_harap_tunggu_pendaftaran = findViewById(R.id.tv_harap_tunggu_pendaftaran);*/
    }

    private void pilihPoliDokter(){
        adapterItems = new ArrayAdapter<String>(this,R.layout.list_item_poli,daftarPoli);
        actv_poli.setAdapter(adapterItems);
        actv_poli.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                actv_dokter.setText("");
                item = parent.getItemAtPosition(position).toString();
                ArrayList<String> stringArrayList = new ArrayList<>();
                for(Dokter dokter : dokterModel){
                    if (dokter.getPoli().equals(item)){
                        stringArrayList.add(dokter.getNama_dokter());
                    }
                }
                itemDokter = new String[stringArrayList.size()];
                itemDokter = stringArrayList.toArray(itemDokter);
                adapterItems2 = new ArrayAdapter<String>(PendaftaranOnline.this,R.layout.list_item_poli, itemDokter);
                actv_dokter.setAdapter(adapterItems2);
                actv_dokter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        item2 = parent.getItemAtPosition(position).toString();
                        for(Dokter dokter : dokterModel){
                            if(dokter.getNama_dokter().equals(item2)){
                                indeksDokter = dokterModel.indexOf(dokter);
                                tv_jadwalDokter.setText(dokter.getJadwal()+" ("+dokter.getJam_awal()+" - Selesai)");
                                ambilKuota(dokter);
                                cekKondisiPendaftaran(dokter);
                                break;
                            }
                        }
                    }
                });
            }
        });
    }

    private void ambilKuota(Dokter dokter){
        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        String date = dateFormat.format(new Date());
        c.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrow = dateFormat.format(c.getTime());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int indeks_jadwal = hari.indexOf(dokter.getJadwal());
        if (indeks_jadwal+1 == dayOfWeek) {
            date = tomorrow;
        }
        id_dokter = dokterModel.get(indeksDokter).getId();
        ref.child("Antrian_Pendaftaran").child(id_dokter).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                jumlahPendaftar = snapshot.getChildrenCount();
                tv_sisaKuota.setText(String.valueOf(dokter.getKuota() - jumlahPendaftar));
                jumlahPendaftar++;
                no_antrian = jumlahPendaftar;
                if (tv_sisaKuota.getText().toString().equals("0")) {
                    tv_status.setText("Tidak dapat melakukan pendaftaran");
                    tv_status.setTextColor(Color.RED);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void cekKondisiPendaftaran(Dokter dokter) {
        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("d-M-yyyy", Locale.getDefault());
        String date = dateFormat.format(new Date());
        c.add(Calendar.DAY_OF_YEAR, 1);
        String tomorrow = dateFormat.format(c.getTime());
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        /*int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);*/
        int indeks_jadwal = hari.indexOf(dokter.getJadwal());
        if(indeks_jadwal+2 == dayOfWeek){
            Log.d(String.valueOf(indeks_jadwal),"indeks1");
            Log.d(String.valueOf(dayOfWeek),"day1");
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            SimpleDateFormat sdf1 = new SimpleDateFormat("H:mm:ss");
            Date current = new Date();
            String currentString = sdf1.format(current);
            String jamAkhir = dokter.getJam_akhir();
            Date jamakhir = new Date();
            Date jamsekarang = null;
            try {
                jamsekarang = sdf1.parse(currentString);
                jamakhir = sdf.parse(jamAkhir);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (jamsekarang.after(jamakhir)){
                tv_status.setText("Tidak dapat melakukan pendaftaran");
                tv_status.setTextColor(Color.RED);
                bt_daftaro.setEnabled(false);
                tgl_pemeriksaan = date;
            } else {
                tv_status.setText("Anda dapat melakukan pendaftaran");
                tv_status.setTextColor(getResources().getColor(R.color.teal_700));
                bt_daftaro.setEnabled(true);
                tgl_pemeriksaan = date;
            }
        } else if(indeks_jadwal+1 == dayOfWeek){
            Log.d(String.valueOf(indeks_jadwal),"indeks2");
            Log.d(String.valueOf(dayOfWeek),"day2");
            tv_status.setText("Anda dapat melakukan pendaftaran");
            tv_status.setTextColor(getResources().getColor(R.color.teal_700));
            bt_daftaro.setEnabled(true);
            tgl_pemeriksaan = tomorrow;
        } else {
            Log.d(String.valueOf(indeks_jadwal),"indeks3");
            Log.d(String.valueOf(dayOfWeek),"day3");
            tv_status.setText("Pendaftaran hanya bisa dilakukan H-1 atau hari H");
            tv_status.setTextColor(Color.RED);
            bt_daftaro.setEnabled(false);
        }
    ambilRiwayatPendaftaran();
    }

    private void ambilRiwayatPendaftaran(){
        ref.child("List_Pendaftaran_Pasien").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                telahTerdaftar = false;
                for (DataSnapshot i : snapshot.getChildren()){
                    String tglPemeriksaan = i.getValue().toString();
                    if (tgl_pemeriksaan !=null && tgl_pemeriksaan.equals(tglPemeriksaan)){
                        telahTerdaftar = true;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void daftarOnline(){
        no_bjs = pt_no_bpjs.getText().toString().trim();
        alamat = pt_alamat.getText().toString().trim();
        poli = actv_poli.getText().toString().trim();
        dokter = actv_dokter.getText().toString().trim();

         if (alamat.isEmpty()){
            pt_alamat.setError("Harap isi alamat anda ");
        } else if (poli.isEmpty()){
            actv_poli.setError("Harap isi poli yang dituju");
        } else if (dokter.isEmpty()){
            actv_dokter.setError("Harap isi dokter yang dituju");
        } else if (tv_sisaKuota.getText().toString().equals("0")){
            Toast.makeText(getApplicationContext(), "Kuota dokter penuh", Toast.LENGTH_SHORT).show();
        } else if (telahTerdaftar) {
             Toast.makeText(getApplicationContext(),"Pendaftaran hanya dapat dilakukan 1 kali dalam sehari",Toast.LENGTH_SHORT).show();
         } else{
             savedata();
         }
    }

    private void ambilDokter(){
        ref.child("Dokter").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot i : snapshot.getChildren()){
                    Dokter temp = i.getValue(Dokter.class);
                    if(temp !=null){
                        dokterModel.add(temp);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }

    private void savedata(){
        id = ref.push().getKey();
        id_pengguna = auth.getUid();
        id_dokter = dokterModel.get(indeksDokter).getId();

        Pendaftaran pendaftaran = new Pendaftaran(id,id_pengguna,id_dokter, no_bjs, alamat, item, alamat, tgl_pemeriksaan, no_antrian);
        if (pendaftaran != null){
            ref.child("Pendaftaran").child(id).setValue(pendaftaran).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                 /*   progressBar();*/
                    Toast.makeText(getApplicationContext(), "Tambah Dokter Berhasil", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PendaftaranOnline.this, StatusPendaftaran.class));
                    finish();
                }
            });
            ref.child("Antrian_Pendaftaran").child(id_dokter).child(tgl_pemeriksaan).child(id).setValue(true);
            ref.child("List_Pendaftaran_Pasien").child(id_pengguna).child(id).setValue(tgl_pemeriksaan);
        }
    }

/*    private void progressBar(){
        progressBar.setVisibility(View.VISIBLE);
        tv_harap_tunggu_pendaftaran.setVisibility(View.VISIBLE);
        progressBar.showContextMenu();
        tv_harap_tunggu_pendaftaran.showContextMenu();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                tv_harap_tunggu_pendaftaran.setVisibility(View.INVISIBLE);
            }
        },15000);

    }*/

}
