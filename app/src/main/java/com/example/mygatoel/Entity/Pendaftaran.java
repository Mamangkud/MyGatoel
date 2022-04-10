package com.example.mygatoel.Entity;

public class Pendaftaran {
    private String id_pengguna,id, no_bjs, id_dokter,alamat, poli, tgl_pemeriksaan;
    private long no_antrian;

    public Pendaftaran(){ }

    public Pendaftaran(String id, String id_pengguna,String id_dokter,String no_bjs, String alamat, String poli, String dokter, String tgl_pemeriksaan, long no_antrian) {
        this.id = id;
        this.id_pengguna = id_pengguna;
        this.id_dokter = id_dokter;
        this.no_bjs = no_bjs;
        this.alamat = alamat;
        this.poli = poli;
        this.tgl_pemeriksaan = tgl_pemeriksaan;
        this.no_antrian = no_antrian;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo_bjs() {
        return no_bjs;
    }

    public void setNo_bjs(String no_bjs) {
        this.no_bjs = no_bjs;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPoli() {
        return poli;
    }

    public void setPoli(String poli) {
        this.poli = poli;
    }

    public String getId_dokter() {
        return id_dokter;
    }

    public void setId_dokter(String id_dokter) {
        this.id_dokter = id_dokter;
    }

    public String getTgl_pemeriksaan() {
        return tgl_pemeriksaan;
    }

    public void setTgl_pemeriksaan(String tgl_pemeriksaan) { this.tgl_pemeriksaan = tgl_pemeriksaan; }

    public String getId_pengguna() { return id_pengguna; }

    public void setId_pengguna(String id_pengguna) { this.id_pengguna = id_pengguna;}

    public long getNo_antrian() { return no_antrian; }

    public void setNo_antrian(long no_antrian) { this.no_antrian = no_antrian; }
}

