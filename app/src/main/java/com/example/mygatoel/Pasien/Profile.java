package com.example.mygatoel.Pasien;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mygatoel.Entity.Pendaftaran;
import com.example.mygatoel.Guest.Login;
import com.example.mygatoel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends Fragment {
    private TextView bt_logoutt, tv_nama_lengkap, tv_norm, tv_tgl_lahir, tv_email;
    private FirebaseAuth auth;
    private FirebaseDatabase mdatabase;
    private DatabaseReference reference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        mdatabase = FirebaseDatabase.getInstance();
        reference = mdatabase.getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile, container, false);


        bt_logoutt = view.findViewById(R.id.bt_logoutt);
        tv_nama_lengkap = view.findViewById(R.id.tv_nama_lengkap);
        tv_norm = view.findViewById(R.id.tv_norm);
        tv_tgl_lahir = view.findViewById(R.id.tv_tgl_lahir);
        tv_email = view.findViewById(R.id.tv_email);

        bt_logoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Apakah anda yakin untuk keluar dari aplikasi?");
                builder.setCancelable(true);
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                        Intent intent = new Intent(getActivity(), Login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        loadInfoAkun();
        return view;
    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
    }
    private void loadInfoAkun(){
        FirebaseUser currentUser = auth.getCurrentUser();
        reference.child("Pasien").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tv_nama_lengkap.setText(snapshot.child("nama_lengkap").getValue().toString());
                tv_norm.setText(snapshot.child("no_rm").getValue().toString());
                tv_tgl_lahir.setText(snapshot.child("tgl_lahir").getValue().toString());
                tv_email.setText(currentUser.getEmail());
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
}