package com.tvip.teknisi_dispenser.pojos;

import java.util.Objects;

public class tanggal_kunjungan {
    private String tanggal;
    private String verifikasi;

    public tanggal_kunjungan(String tanggal, String verifikasi) {
        this.tanggal = tanggal;
        this.verifikasi = verifikasi;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getVerifikasi() {
        return verifikasi;
    }
}
