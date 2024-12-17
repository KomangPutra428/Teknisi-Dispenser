package com.tvip.teknisi_dispenser.menu_edit;

import static com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_dispenser.idtokopost;
import static com.tvip.teknisi_dispenser.menu_detail_kunjungan.footer_detail_kunjungan_dispenser.realdispenser;
import static com.tvip.teknisi_dispenser.menu_edit.edit_verifikasi_dispenser.lokasi_dispenser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.footer_detail_kunjungan_dispenser;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.ceklis_dispenser;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.fisik_dispenser;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.form_kunjungan;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.pilih_id_dispenser;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.verifikasi_dispenser;
import com.tvip.teknisi_dispenser.menu_history_kunjungan.history_kunjungan;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class edit_ceklis_dispenser extends AppCompatActivity {
    SweetAlertDialog pDialog, success;

    SharedPreferences sharedPreferences;
    RadioGroup radioLuar;
    LinearLayout luar_layout;
    EditText catatanLuar;

    RadioGroup radioAtas;
    LinearLayout atas_layout;
    EditText catatanAtas;

    RadioGroup radioPanas;
    LinearLayout panas_layout;
    EditText catatanPanas;

    RadioGroup radioDingin;
    LinearLayout dingin_layout;
    EditText catatanDingin;

    RadioGroup radioMesin;
    LinearLayout mesin_layout;
    EditText catatanMesin;

    RadioGroup radioArde;
    LinearLayout arde_layout;
    EditText catatanArde;

    RadioGroup radioReseptor;
    LinearLayout reseptor_layout;
    EditText catatanReseptor;

    RadioGroup radioLampu;
    LinearLayout lampu_layout;
    EditText catatanLampu;

    RadioGroup radioKunjungan;
    LinearLayout kunjungan_layout;
    EditText catatanKunjungan;

    RadioGroup radioKeterangan;
    LinearLayout keterangan_layout;
    EditText catatanKeterangan;

    MaterialButton batal, selesai;

    String namafotoseri;
    String namafotodepan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ceklis_dispenser);

        radioLuar = findViewById(R.id.radioLuar);
        luar_layout = findViewById(R.id.luar_layout);
        catatanLuar = findViewById(R.id.catatanLuar);

        radioAtas = findViewById(R.id.radioAtas);
        atas_layout = findViewById(R.id.atas_layout);
        catatanAtas = findViewById(R.id.catatanAtas);

        radioPanas = findViewById(R.id.radioPanas);
        panas_layout = findViewById(R.id.panas_layout);
        catatanPanas = findViewById(R.id.catatanPanas);

        radioDingin = findViewById(R.id.radioDingin);
        dingin_layout = findViewById(R.id.dingin_layout);
        catatanDingin = findViewById(R.id.catatanDingin);

        radioMesin = findViewById(R.id.radioMesin);
        mesin_layout = findViewById(R.id.mesin_layout);
        catatanMesin = findViewById(R.id.catatanMesin);

        radioArde = findViewById(R.id.radioArde);
        arde_layout = findViewById(R.id.arde_layout);
        catatanArde = findViewById(R.id.catatanArde);

        radioReseptor = findViewById(R.id.radioReseptor);
        reseptor_layout = findViewById(R.id.reseptor_layout);
        catatanReseptor = findViewById(R.id.catatanReseptor);

        radioLampu = findViewById(R.id.radioLampu);
        lampu_layout = findViewById(R.id.lampu_layout);
        catatanLampu = findViewById(R.id.catatanLampu);

        radioKunjungan = findViewById(R.id.radioKunjungan);
        kunjungan_layout = findViewById(R.id.kunjungan_layout);
        catatanKunjungan = findViewById(R.id.catatanKunjungan);

        radioKeterangan = findViewById(R.id.radioKeterangan);
        keterangan_layout = findViewById(R.id.keterangan_layout);
        catatanKeterangan = findViewById(R.id.catatanKeterangan);

        batal = findViewById(R.id.batal);
        selesai = findViewById(R.id.selesai);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatanLuar.getText().toString().length() == 0){
                    new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Catatan Penampilan Luar Harap Di Isi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(catatanAtas.getText().toString().length() == 0){
                    new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Catatan Cover Atas Harap Di Isi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(catatanPanas.getText().toString().length() == 0){
                    new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Catatan Kran Panas Harap Di Isi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(catatanDingin.getText().toString().length() == 0){
                    new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Catatan Kran Dingin/Netral Harap Di Isi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(catatanMesin.getText().toString().length() == 0){
                    new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Catatan Mesin Harap Di Isi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(catatanArde.getText().toString().length() == 0){
                    new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Catatan Stecker Arde Harap Di Isi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(catatanReseptor.getText().toString().length() == 0){
                    new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Catatan Reseptor Harap Di Isi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(catatanLampu.getText().toString().length() == 0){
                    new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Catatan Lampu Switch Harap Di Isi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(catatanKunjungan.getText().toString().length() == 0){
                    new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Catatan Hasil Kunjungan Harap Di Isi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if(catatanKeterangan.getText().toString().length() == 0){
                    new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Catatan Keterangan Harap Di Isi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    postFull();
                }
            }
        });


        radioLuar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioLuar.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                if(selectedValue.equals("BAIK")){
                    luar_layout.setVisibility(View.GONE);
                    catatanLuar.setText("Tidak Ada Kendala");
                } else {
                    luar_layout.setVisibility(View.VISIBLE);
                    if(footer_detail_kunjungan_dispenser.status_luar.getText().toString().equals("BAIK")){
                        catatanLuar.setText("");
                    } else {
                        catatanLuar.setText(footer_detail_kunjungan_dispenser.catatan_luar.getText().toString());
                    }

                }
            }
        });

        radioAtas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioAtas.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                if(selectedValue.equals("BAIK")){
                    atas_layout.setVisibility(View.GONE);
                    catatanAtas.setText("Tidak Ada Kendala");
                } else {
                    atas_layout.setVisibility(View.VISIBLE);
                    if(footer_detail_kunjungan_dispenser.status_atas.getText().toString().equals("BAIK")){
                        catatanAtas.setText("");
                    } else {
                        catatanAtas.setText(footer_detail_kunjungan_dispenser.catatan_atas.getText().toString());
                    }
                }
            }
        });

        radioPanas.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioPanas.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                if(selectedValue.equals("BAIK")){
                    panas_layout.setVisibility(View.GONE);
                    catatanPanas.setText("Tidak Ada Kendala");
                } else {
                    panas_layout.setVisibility(View.VISIBLE);
                    if(footer_detail_kunjungan_dispenser.status_panas.getText().toString().equals("BAIK")){
                        catatanPanas.setText("");
                    } else {
                        catatanPanas.setText(footer_detail_kunjungan_dispenser.catatan_panas.getText().toString());
                    }
                }
            }
        });

        radioDingin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioDingin.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                if(selectedValue.equals("BAIK")){
                    dingin_layout.setVisibility(View.GONE);
                    catatanDingin.setText("Tidak Ada Kendala");
                } else {
                    dingin_layout.setVisibility(View.VISIBLE);
                    if(footer_detail_kunjungan_dispenser.status_dingin.getText().toString().equals("BAIK")){
                        catatanDingin.setText("");
                    } else {
                        catatanDingin.setText(footer_detail_kunjungan_dispenser.catatan_dingin.getText().toString());
                    }
                }
            }
        });

        radioMesin.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioMesin.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                if(selectedValue.equals("BAIK")){
                    mesin_layout.setVisibility(View.GONE);
                    catatanMesin.setText("Tidak Ada Kendala");
                } else {

                    mesin_layout.setVisibility(View.VISIBLE);
                    if(footer_detail_kunjungan_dispenser.status_mesin.getText().toString().equals("BAIK")){
                        catatanMesin.setText("");
                    } else {
                        catatanMesin.setText(footer_detail_kunjungan_dispenser.catatan_mesin.getText().toString());
                    }
                }
            }
        });

        radioArde.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioArde.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                if(selectedValue.equals("BAIK")){
                    arde_layout.setVisibility(View.GONE);
                    catatanArde.setText("Tidak Ada Kendala");
                } else {
                    arde_layout.setVisibility(View.VISIBLE);
                    if(footer_detail_kunjungan_dispenser.status_arde.getText().toString().equals("BAIK")){
                        catatanArde.setText("");
                    } else {
                        catatanArde.setText(footer_detail_kunjungan_dispenser.catatan_arde.getText().toString());
                    }
                }
            }
        });

        radioReseptor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioReseptor.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                if(selectedValue.equals("BAIK")){
                    reseptor_layout.setVisibility(View.GONE);
                    catatanReseptor.setText("Tidak Ada Kendala");
                } else {
                    reseptor_layout.setVisibility(View.VISIBLE);
                    if(footer_detail_kunjungan_dispenser.status_reseptor.getText().toString().equals("BAIK")){
                        catatanReseptor.setText("");
                    } else {
                        catatanReseptor.setText(footer_detail_kunjungan_dispenser.catatan_reseptor.getText().toString());
                    }
                }
            }
        });

        radioLampu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioLampu.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                if(selectedValue.equals("BAIK")){
                    lampu_layout.setVisibility(View.GONE);
                    catatanLampu.setText("Tidak Ada Kendala");
                } else {
                    lampu_layout.setVisibility(View.VISIBLE);
                    if(footer_detail_kunjungan_dispenser.status_lampu.getText().toString().equals("BAIK")){
                        catatanLampu.setText("");
                    } else {
                        catatanLampu.setText(footer_detail_kunjungan_dispenser.catatan_lampu.getText().toString());
                    }
                }
            }
        });

        radioKunjungan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioKunjungan.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                if(selectedValue.equals("VERIFIKASI")){
                    kunjungan_layout.setVisibility(View.GONE);
                    catatanKunjungan.setText("Verifikasi");
                } else {
                    kunjungan_layout.setVisibility(View.VISIBLE);
                    if(footer_detail_kunjungan_dispenser.status_kunjungan.getText().toString().equals("VERIFIKASI")){
                        catatanKunjungan.setText("");
                    } else {
                        catatanKunjungan.setText(footer_detail_kunjungan_dispenser.catatan_kunjungan.getText().toString());
                    }
                }
            }
        });

        radioKeterangan.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = radioKeterangan.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();
                if(selectedValue.equals("OK")){
                    keterangan_layout.setVisibility(View.GONE);
                    catatanKeterangan.setText("Tidak Ada Kendala");
                } else {
                    keterangan_layout.setVisibility(View.VISIBLE);
                    if(footer_detail_kunjungan_dispenser.status_keterangan.getText().toString().equals("OK")){
                        catatanKeterangan.setText("");
                    } else {
                        catatanKeterangan.setText(footer_detail_kunjungan_dispenser.catatan_keterangan.getText().toString());
                    }
                }
            }
        });

        cekFisikDispenser();
    }

    private void cekFisikDispenser() {
        // luar //
        if(footer_detail_kunjungan_dispenser.status_luar.getText().toString().equals("BAIK")){
            for (int i = 0; i < radioLuar.getChildCount(); i++) {
                View child = radioLuar.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("BAIK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < radioLuar.getChildCount(); i++) {
                View child = radioLuar.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("TIDAK BAIK")) {
                        radioButton.setChecked(true);
                        catatanLuar.setText(footer_detail_kunjungan_dispenser.catatan_luar.getText().toString());
                        break;
                    }
                }
            }
        }
        // ==== //

        // atas //
        if(footer_detail_kunjungan_dispenser.status_atas.getText().toString().equals("BAIK")){
            for (int i = 0; i < radioAtas.getChildCount(); i++) {
                View child = radioAtas.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("BAIK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < radioAtas.getChildCount(); i++) {
                View child = radioAtas.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("TIDAK BAIK")) {
                        radioButton.setChecked(true);
                        catatanAtas.setText(footer_detail_kunjungan_dispenser.catatan_atas.getText().toString());
                        break;
                    }
                }
            }
        }
        // ==== //

        // panas //
        if(footer_detail_kunjungan_dispenser.status_panas.getText().toString().equals("BAIK")){
            for (int i = 0; i < radioPanas.getChildCount(); i++) {
                View child = radioPanas.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("BAIK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < radioPanas.getChildCount(); i++) {
                View child = radioPanas.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("TIDAK BAIK")) {
                        radioButton.setChecked(true);
                        catatanPanas.setText(footer_detail_kunjungan_dispenser.catatan_panas.getText().toString());
                        break;
                    }
                }
            }
        }
        // ==== //

        // dingin //
        if(footer_detail_kunjungan_dispenser.status_dingin.getText().toString().equals("BAIK")){
            for (int i = 0; i < radioDingin.getChildCount(); i++) {
                View child = radioDingin.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("BAIK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < radioDingin.getChildCount(); i++) {
                View child = radioDingin.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("TIDAK BAIK")) {
                        radioButton.setChecked(true);
                        catatanDingin.setText(footer_detail_kunjungan_dispenser.catatan_dingin.getText().toString());
                        break;
                    }
                }
            }
        }
        // ==== //

        // mesin //
        if(footer_detail_kunjungan_dispenser.status_mesin.getText().toString().equals("BAIK")){
            for (int i = 0; i < radioMesin.getChildCount(); i++) {
                View child = radioMesin.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("BAIK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < radioMesin.getChildCount(); i++) {
                View child = radioMesin.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("TIDAK BAIK")) {
                        radioButton.setChecked(true);
                        catatanMesin.setText(footer_detail_kunjungan_dispenser.catatan_mesin.getText().toString());
                        break;
                    }
                }
            }
        }
        // ==== //

        // arde //
        if(footer_detail_kunjungan_dispenser.status_arde.getText().toString().equals("BAIK")){
            for (int i = 0; i < radioArde.getChildCount(); i++) {
                View child = radioArde.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("BAIK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < radioArde.getChildCount(); i++) {
                View child = radioArde.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("TIDAK BAIK")) {
                        radioButton.setChecked(true);
                        catatanArde.setText(footer_detail_kunjungan_dispenser.catatan_arde.getText().toString());
                        break;
                    }
                }
            }
        }
        // ==== //

        // reseptor //
        if(footer_detail_kunjungan_dispenser.status_reseptor.getText().toString().equals("BAIK")){
            for (int i = 0; i < radioReseptor.getChildCount(); i++) {
                View child = radioReseptor.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("BAIK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < radioReseptor.getChildCount(); i++) {
                View child = radioReseptor.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("TIDAK BAIK")) {
                        radioButton.setChecked(true);
                        catatanReseptor.setText(footer_detail_kunjungan_dispenser.catatan_reseptor.getText().toString());
                        break;
                    }
                }
            }
        }
        // ==== //

        // lampu //
        if(footer_detail_kunjungan_dispenser.status_lampu.getText().toString().equals("BAIK")){
            for (int i = 0; i < radioLampu.getChildCount(); i++) {
                View child = radioLampu.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("BAIK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < radioLampu.getChildCount(); i++) {
                View child = radioLampu.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("TIDAK BAIK")) {
                        radioButton.setChecked(true);
                        catatanLampu.setText(footer_detail_kunjungan_dispenser.catatan_lampu.getText().toString());
                        break;
                    }
                }
            }
        }
        // ==== //

        // kunjungan //
        if(footer_detail_kunjungan_dispenser.status_kunjungan.getText().toString().equals("PERBAIKAN")){
            for (int i = 0; i < radioKunjungan.getChildCount(); i++) {
                View child = radioKunjungan.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("PERBAIKAN")) {
                        catatanKunjungan.setText(footer_detail_kunjungan_dispenser.catatan_kunjungan.getText().toString());
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else if(footer_detail_kunjungan_dispenser.status_kunjungan.getText().toString().equals("MAINTENANCE")){
            for (int i = 0; i < radioKunjungan.getChildCount(); i++) {
                View child = radioKunjungan.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("MAINTENANCE")) {
                        catatanKunjungan.setText(footer_detail_kunjungan_dispenser.catatan_kunjungan.getText().toString());
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < radioKunjungan.getChildCount(); i++) {
                View child = radioKunjungan.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("VERIFIKASI")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }
        // ==== //

        // kunjungan //
        if(footer_detail_kunjungan_dispenser.status_keterangan.getText().toString().equals("OK")){
            for (int i = 0; i < radioKeterangan.getChildCount(); i++) {
                View child = radioKeterangan.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("OK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < radioKeterangan.getChildCount(); i++) {
                View child = radioKeterangan.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("PASANG TARIK")) {
                        catatanKunjungan.setText(footer_detail_kunjungan_dispenser.catatan_keterangan.getText().toString());
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }
        // ==== //

    }

    private void postFull() {
        pDialog = new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        SimpleDateFormat gambar = new SimpleDateFormat("yyyyMMddHHmmss");
        String gambarDateandTime = gambar.format(new Date());

        namafotoseri = idtokopost+"_"+"seri_dispenser_"+gambarDateandTime;
        namafotodepan = idtokopost+"_"+"depan_"+gambarDateandTime;

        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_dispenser_full",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        uploadSeri();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

                params.put("id_dispenser", realdispenser);

                params.put("pemakaian_air", getIntent().getStringExtra("air"));
                params.put("foto_seri", namafotoseri + ".jpeg");
                params.put("cek_seri", getIntent().getStringExtra("seri"));
                params.put("no_seri", edit_verifikasi_dispenser.seridispenser.getText().toString());
                params.put("foto_depan_dispenser", namafotodepan + ".jpeg");
                params.put("lokasi_dispenser", lokasi_dispenser.getText().toString());


                int selectedIdLuar = radioLuar.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonLuar = findViewById(selectedIdLuar);
                String selectedValueLuar = selectedRadioButtonLuar.getText().toString();
                params.put("cek_penampilan_luar", selectedValueLuar);
                params.put("catatan_luar", catatanLuar.getText().toString());

                int selectedIdAtas = radioAtas.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonAtas = findViewById(selectedIdAtas);
                String selectedValueAtas = selectedRadioButtonAtas.getText().toString();
                params.put("cek_cover_atas", selectedValueAtas);
                params.put("catatan_atas", catatanAtas.getText().toString());

                int selectedIdPanas = radioPanas.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonPanas = findViewById(selectedIdPanas);
                String selectedValuePanas = selectedRadioButtonPanas.getText().toString();
                params.put("cek_kran_panas", selectedValuePanas);
                params.put("catatan_panas", catatanPanas.getText().toString());

                int selectedIdDingin = radioDingin.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonDingin = findViewById(selectedIdDingin);
                String selectedValueDingin = selectedRadioButtonDingin.getText().toString();
                params.put("cek_kran_dingin", selectedValueDingin);
                params.put("catatan_dingin", catatanDingin.getText().toString());

                int selectedIdMesin = radioMesin.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonMesin = findViewById(selectedIdMesin);
                String selectedValueMesin = selectedRadioButtonMesin.getText().toString();
                params.put("cek_mesin", selectedValueMesin);
                params.put("catatan_mesin", catatanMesin.getText().toString());

                int selectedIdArde = radioArde.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonArde = findViewById(selectedIdArde);
                String selectedValueArde = selectedRadioButtonArde.getText().toString();
                params.put("cek_stecker_arde", selectedValueArde);
                params.put("catatan_arde", catatanArde.getText().toString());

                int selectedIdReseptor = radioReseptor.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonReseptor = findViewById(selectedIdReseptor);
                String selectedValueReseptor = selectedRadioButtonReseptor.getText().toString();
                params.put("cek_reseptor", selectedValueReseptor);
                params.put("catatan_reseptor", catatanReseptor.getText().toString());

                int selectedIdLampu = radioLampu.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonLampu = findViewById(selectedIdLampu);
                String selectedValueLampu = selectedRadioButtonLampu.getText().toString();
                params.put("cek_lampu", selectedValueLampu);
                params.put("catatan_lampu", catatanLampu.getText().toString());

                int selectedIdKunjungan = radioKunjungan.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonKunjungan = findViewById(selectedIdKunjungan);
                String selectedValueKunjungan = selectedRadioButtonKunjungan.getText().toString();
                params.put("cek_kunjungan", selectedValueKunjungan);
                params.put("catatan_kunjungan", catatanKunjungan.getText().toString());

                int selectedIdKeterangan = radioKeterangan.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonKeterangan = findViewById(selectedIdKeterangan);
                String selectedValueKeterangan = selectedRadioButtonKeterangan.getText().toString();
                params.put("cek_keterangan", selectedValueKeterangan);
                params.put("catatan_keterangan", catatanKeterangan.getText().toString());

                System.out.println("Return Params = " + params);




                return params;
            }

        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );
        RequestQueue requestQueue33 = Volley.newRequestQueue(edit_ceklis_dispenser.this);
        requestQueue33.add(stringRequest2);

    }

    private void uploadSeri() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://apisec.tvip.co.id/mobile_eis_2/upload_dispenser.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        uploadDepan();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                String gambar = ImageToString(edit_verifikasi_dispenser.bitmapseri);

                params.put("nama_foto", namafotoseri);
                params.put("foto", gambar);
                params.put("folder", "dispenser_seri");


                return params;
            }
        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );


        RequestQueue requestQueue5 = Volley.newRequestQueue(edit_ceklis_dispenser.this);
        requestQueue5.add(stringRequest2);

    }

    private void uploadDepan() {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://apisec.tvip.co.id/mobile_eis_2/upload_dispenser.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        success = new SweetAlertDialog(edit_ceklis_dispenser.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setContentText("Data Berhasil Disimpan");
                        success.setCancelable(false);
                        success.setConfirmText("OK");
                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(getApplicationContext(), history_kunjungan.class);
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                String gambar = ImageToString(edit_verifikasi_dispenser.bitmapdepan);

                params.put("nama_foto", namafotodepan);
                params.put("foto", gambar);
                params.put("folder", "dispenser_depan");


                return params;
            }
        };
        stringRequest2.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                )
        );


        RequestQueue requestQueue5 = Volley.newRequestQueue(edit_ceklis_dispenser.this);
        requestQueue5.add(stringRequest2);

    }

    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}