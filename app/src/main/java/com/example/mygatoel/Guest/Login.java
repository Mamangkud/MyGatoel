package com.example.mygatoel.Guest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mygatoel.Admin.MainAdmin;
import com.example.mygatoel.Pasien.MainPasien;
import com.example.mygatoel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private TextView bt_daftar, tv_harap_tunggu;
    private EditText edt_email_log, edt_password_log;
    private Button bt_login;
    private FirebaseAuth auth;
    private String email, password;
    private ProgressDialog progressDialog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        bt_daftar = findViewById(R.id.bt_daftar);
        bt_login = findViewById(R.id. bt_login);
        edt_email_log = findViewById(R.id.edt_email_log);
        edt_password_log = findViewById(R.id.edt_password_log);
        progressBar = findViewById(R.id.prog_2);
        tv_harap_tunggu = findViewById(R.id.tv_harap_tunggu);

        bt_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
                finish();
            }
        });

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

/*        if(currentUser !=null){
            startActivity(new Intent(Login.this, MainPasien.class));
            finish();
        }*/

    }

    private void login(){
        email = edt_email_log.getText().toString();
        password = edt_password_log.getText().toString();

        if (email.isEmpty()){
            edt_email_log.setError("Harap isi email anda");
        } else if (password.isEmpty()){
            edt_password_log.setError("Harap isi password anda");
        } else if (email.equals("AdminGatoel") && password.equals("adminurj")){
            progressBar();
            startActivity(new Intent(Login.this, MainAdmin.class));
            Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_LONG).show();
            finish();
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                progressBar();
                                startActivity(new Intent(Login.this, MainPasien.class));
                                Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                progressBar();
                                Toast.makeText(getApplicationContext(), "Email atau password anda tidak sesuai", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void progressBar(){
        progressBar.setVisibility(View.VISIBLE);
        tv_harap_tunggu.setVisibility(View.VISIBLE);
        progressBar.showContextMenu();
        tv_harap_tunggu.showContextMenu();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
                tv_harap_tunggu.setVisibility(View.INVISIBLE);
            }
        },5000);
    }
}
