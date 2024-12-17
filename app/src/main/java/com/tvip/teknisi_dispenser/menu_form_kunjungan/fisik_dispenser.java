package com.tvip.teknisi_dispenser.menu_form_kunjungan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.tvip.teknisi_dispenser.menu_history_kunjungan.history_kunjungan;
import com.tvip.teknisi_dispenser.menu_utama.MainActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class fisik_dispenser extends AppCompatActivity {

    static String idtokopost, jamstart, Conditioning;

    TextView tanggal_verifikasi;
    static String sewa;
    static TextView id_dispenser;
    static TextView id_jenis;
    static RadioGroup statusfisik;
    LinearLayout catatan_layout;
    EditText catatan;
    MaterialButton batal, lanjutkan;
    SweetAlertDialog pDialog, success;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisik_dispenser);
        tanggal_verifikasi = findViewById(R.id.tanggal_verifikasi);
        id_dispenser = findViewById(R.id.id_dispenser);
        id_jenis = findViewById(R.id.id_jenis);
        catatan_layout = findViewById(R.id.catatan_layout);
        statusfisik = findViewById(R.id.statusfisik);
        catatan = findViewById(R.id.catatan);

        idtokopost = getIntent().getStringExtra("idtoko");
        jamstart = getIntent().getStringExtra("start");
        batal = findViewById(R.id.batal);
        lanjutkan = findViewById(R.id.lanjut);

        catatan_layout.setVisibility(View.GONE);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        String start = sdf.format(new Date());
        tanggal_verifikasi.setText(start);

        id_dispenser.setText(getIntent().getStringExtra("szSerialNumber"));
        id_jenis.setText(getIntent().getStringExtra("szRentCalcId"));

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        statusfisik.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = statusfisik.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);
                String selectedValue = selectedRadioButton.getText().toString();

                Conditioning = selectedValue;

                if(selectedValue.equals("ADA")){
                    catatan.setText("");
                    catatan_layout.setVisibility(View.GONE);
                } else {
                    catatan_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        sewa = getIntent().getStringExtra("jenis_sewa");
        Toast.makeText(this, sewa, Toast.LENGTH_SHORT).show();

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lanjutkan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(statusfisik.getCheckedRadioButtonId() == -1) {
                    new SweetAlertDialog(fisik_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Silahkan pilih salah satu pilihan")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    int selectedId = statusfisik.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedId);
                    String selectedValue = selectedRadioButton.getText().toString();

                    if(selectedValue.equals("ADA")){
                        Intent intent = new Intent(getApplicationContext(), verifikasi_dispenser.class);
                        intent.putExtra("seridispenser", getIntent().getStringExtra("szSerialNumber"));
                        startActivity(intent);
                    } else {
                        if(catatan.getText().toString().length() == 0){
                            catatan.setError("Wajib Di isi");
                        } else {
                            postVerifikasiTidak();
                        }
                    }
                }
            }
        });
    }

    private void postVerifikasiTidak() {
        pDialog = new SweetAlertDialog(fisik_dispenser.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_dispenser",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        new android.os.Handler().postDelayed(new Runnable() {
                            public void run() {
                                pDialog.dismissWithAnimation();
                                success = new SweetAlertDialog(fisik_dispenser.this, SweetAlertDialog.SUCCESS_TYPE);
                                success.setContentText("Data Berhasil Disimpan");
                                success.setCancelable(false);
                                success.setConfirmText("OK");
                                success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                finish();
                                            }
                                        })
                                        .show();
                            }
                        }, 5000);



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

                int selectedIdFisik = statusfisik.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonFisik = findViewById(selectedIdFisik);
                String selectedValueFisik = selectedRadioButtonFisik.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());


                params.put("nik_baru", sharedPreferences.getString("nik_baru", null));
                params.put("id_toko", getIntent().getStringExtra("idtoko"));
                params.put("id_dispenser", id_dispenser.getText().toString());
                params.put("jenis_dispenser", id_jenis.getText().toString());
                params.put("status_fisik", selectedValueFisik);

                params.put("jenis_verifikasi", "DISPENSER");

                params.put("start_verifikasi", getIntent().getStringExtra("start"));
                params.put("end_verifikasi", currentDateandTime);
                params.put("end_verifikasi", currentDateandTime);
                params.put("jenis_sewa", sewa);




                params.put("catatan", catatan.getText().toString());

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
        RequestQueue requestQueue33 = Volley.newRequestQueue(fisik_dispenser.this);
        requestQueue33.add(stringRequest2);

    }
}