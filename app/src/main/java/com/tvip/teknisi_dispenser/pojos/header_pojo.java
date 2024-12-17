package com.tvip.teknisi_dispenser.pojos;

public class header_pojo {
    private String id;
    private  String id_toko;
    private String depo;
    private String tanggal_verifikasi;

    private String szName;
    private String szAddress;
    private String jenis_verifikasi;
    private String start_verifikasi;
    private String end_verifikasi;
    private String szSoldToBranchId;


    public header_pojo(String id, String id_toko, String depo, String tanggal_verifikasi,
                         String szName, String szAddress, String jenis_verifikasi,
                         String start_verifikasi, String end_verifikasi, String szSoldToBranchId) {
        this.id = id;
        this.id_toko = id_toko;
        this.depo =  depo;
        this.tanggal_verifikasi = tanggal_verifikasi;

        this.szName = szName;
        this.szAddress = szAddress;
        this.jenis_verifikasi =  jenis_verifikasi;
        this.start_verifikasi = start_verifikasi;
        this.end_verifikasi = end_verifikasi;
        this.szSoldToBranchId = szSoldToBranchId;

    }

    public String getId() {
        return id;
    }

    public String getId_toko() {
        return id_toko;
    }

    public String getDepo() {
        return depo;
    }

    public String getTanggal_verifikasi() {
        return tanggal_verifikasi;
    }

    public String getSzName() {
        return szName;
    }

    public String getSzAddress() {
        return szAddress;
    }

    public String getJenis_verifikasi() {
        return jenis_verifikasi;
    }

    public String getStart_verifikasi() {
        return start_verifikasi;
    }

    public String getEnd_verifikasi() {
        return end_verifikasi;
    }

    public String getSzSoldToBranchId() {
        return szSoldToBranchId;
    }
}
