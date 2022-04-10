package com.example.mygatoel.Entity;

public class Dokter {
    private String id, nama_dokter, poli, hari, jam_awal, jam_akhir;
    private long kuota;

    public Dokter(){ }

    public Dokter(String id,String nama_dokter, String poli, String hari, long kuota, String jam_awal, String jam_akhir){
        this.id = id;
        this.nama_dokter = nama_dokter;
        this.poli = poli;
        this.hari = hari;
        this.kuota = kuota;
        this.jam_awal = jam_awal;
        this.jam_akhir = jam_akhir;
    }

    public Dokter(String nama_dokter, String poli) {
        this.nama_dokter = nama_dokter;
        this.poli = poli;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getNama_dokter() { return nama_dokter; }

    public void setNama_dokter(String nama_dokter) { this.nama_dokter = nama_dokter; }

    public String getPoli() { return poli; }

    public void setPoli(String poli) { this.poli = poli; }

    public String getJadwal() { return hari; }

    public void setJadwal(String hari) { this.hari = hari; }

    public long getKuota() { return kuota; }

    public void setKuota(long kuota) { this.kuota = kuota; }

    public String getJam_awal() { return jam_awal; }

    public void setJam_awal(String jam_awal) { this.jam_awal = jam_awal; }

    public String getJam_akhir() { return jam_akhir; }

    public void setJam_akhir(String jam_akhir) { this.jam_akhir = jam_akhir; }
}
