package com.tvip.teknisi_dispenser.pojos;

public class footer_dispenser_pojo {
    private String id_header;
    private String id_dispenser;
    private String jenis_dispenser;
    private String status_fisik;
    private String catatan;

    public footer_dispenser_pojo(String id_header, String id_dispenser, String jenis_dispenser,
                          String status_fisik, String catatan) {
        this.id_header = id_header;
        this.id_dispenser = id_dispenser;
        this.jenis_dispenser =  jenis_dispenser;
        this.status_fisik =  status_fisik;
        this.catatan =  catatan;
    }

    public String getId_header() {
        return id_header;
    }

    public String getId_dispenser() {
        return id_dispenser;
    }

    public String getJenis_dispenser() {
        return jenis_dispenser;
    }

    public String getStatus_fisik() {
        return status_fisik;
    }

    public String getCatatan() {
        return catatan;
    }
}
