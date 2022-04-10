package com.example.mygatoel.Guest;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mygatoel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class Register extends AppCompatActivity {
    private EditText edt_norm, edt_nama_lengkap, edt_tgl_lahir, edt_email, edt_password;
    private TextInputLayout til_norm, til_nama, til_tgl_lahir, til_email, til_password;
    private Button df_Akun;
    private ImageView img_kembali_rg;
    private FirebaseAuth auth;
    private DatabaseReference ref;
    private CheckBox cekSetuju;
    private FirebaseDatabase firebaseDatabase;
    boolean cek = false;
    private String norm, nama_lengkap, tgl_lahir, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference().child("Pasien");

        initView();
        pilihTanggal();
        df_Akun.setEnabled(false);

        img_kembali_rg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
                finish();
            }
        });

        cekSetuju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                df_Akun.setEnabled(true);
                if(!cekSetuju.isChecked()){
                    df_Akun.setEnabled(false);
                }
            }
        });

        df_Akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftarAkun();
            }
        });
    }

    private void initView() {
        df_Akun = findViewById(R.id.df_akun);
        edt_norm = findViewById(R.id.edt_norm);
        edt_nama_lengkap = findViewById(R.id.edt_nama_lengkap);
        edt_tgl_lahir = findViewById(R.id.edt_tgl_lahir);
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        img_kembali_rg = findViewById(R.id.img_kembali_rg);
        til_norm = findViewById(R.id.til_norm);
        til_nama = findViewById(R.id.til_nama);
        til_tgl_lahir = findViewById(R.id.til_tgl_lahir);
        til_email = findViewById(R.id.til_email);
        til_password = findViewById(R.id.til_password);
        cekSetuju = findViewById(R.id.cekSetuju);
    }

    private void pilihTanggal() {
        Calendar calendar = Calendar.getInstance();
        final int tahun = calendar.get(Calendar.YEAR);
        final int bulan = calendar.get(Calendar.MONTH);
        final int tanggal = calendar.get(Calendar.DAY_OF_MONTH);
        edt_tgl_lahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        edt_tgl_lahir.setText(date);
                    }
                }, tahun, bulan, tanggal);
                datePickerDialog.show();
            }
        });
    }

    private boolean validasiNorm() {
        norm = til_norm.getEditText().getText().toString().trim();
        if (norm.isEmpty()) {
            til_norm.setError("Harap isi No. RM anda");
            return false;
        } else if (norm.length() > 6) {
            til_norm.setError("No. Rekam Medis maksimal 6 digit");
            return false;
        } else {
            til_norm.setError(null);
            return true;
        }

    }

    private boolean validasiNama() {
        nama_lengkap = til_nama.getEditText().getText().toString().trim();
        if (nama_lengkap.isEmpty()) {
            til_nama.setError("Harap isi nama lengkap anda");
            return false;
        } else {
            til_nama.setError(null);
            return true;
        }
    }

    private boolean validasiTanggalLahir() {
        tgl_lahir = til_tgl_lahir.getEditText().getText().toString().trim();
        if (tgl_lahir.isEmpty()) {
            til_tgl_lahir.setError("Harap isi tanggal lahir anda");
            return false;
        } else {
            til_tgl_lahir.setError(null);
            return true;
        }
    }


    private boolean validasiEmail() {
        email = til_email.getEditText().getText().toString().trim();
        if (email.isEmpty()) {
            til_email.setError("Harap isi email anda");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            til_email.setError("Email anda tidak valid");
            return false;
        } else {
            til_email.setError(null);
            return true;
        }

    }

    private boolean validasiPassword() {
        password = til_password.getEditText().getText().toString().trim();
        if (password.isEmpty()) {
            til_password.setError("Harap isi password anda");
            return false;
        } else if (password.length() < 6) {
            til_password.setError("Minimal 6 digit password");
            return false;
        } else {
            til_password.setError(null);
            return true;
        }
    }

    public void daftarAkun() {
        if (!validasiNorm() | !validasiNama() | !validasiTanggalLahir() | !validasiEmail() | !validasiPassword()) {
            return;
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser currentuser = auth.getCurrentUser();
                                DatabaseReference dbuser = ref.child(currentuser.getUid());
                                dbuser.child("no_rm").setValue(norm);
                                dbuser.child("nama_lengkap").setValue(nama_lengkap);
                                dbuser.child("tgl_lahir").setValue(tgl_lahir);
                                Toast.makeText(getApplicationContext(), "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(Register.this, Login.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Registrasi Gagal", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("Cek", exception.toString());
                        }
                    });
        };
    }
}
