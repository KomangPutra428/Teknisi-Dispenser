package com.tvip.teknisi_dispenser.menu_login;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.tvip.teknisi_dispenser.menu_utama.MainActivity;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.third_party.HttpsTrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class login extends AppCompatActivity {
    EditText nikbaru, editpassword;
    MaterialButton login;
    SweetAlertDialog success, pDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        nikbaru = findViewById(R.id.nikbaru);
        editpassword = findViewById(R.id.editpassword);
        login = findViewById(R.id.login);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        HttpsTrustManager.allowAllSSL();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nikbaru.getText().toString().length()==0){
                    nikbaru.setError("Wajib Di isi");
                } else if (editpassword.getText().toString().length()==0){
                    editpassword.setError("Wajib Di isi");
                } else {
                    sendLogin();
                }
            }
        });

    }

    private void sendLogin() {

        pDialog = new SweetAlertDialog(login.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://hrd.tvip.co.id/rest_server/api/login/index?nik_baru=" + nikbaru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if (obj.getString("status").equals("true")) {
                                JSONArray movieArray = obj.getJSONArray("data");
                                for (int i = 0; i < movieArray.length(); i++) {

                                    final JSONObject movieObject = movieArray.getJSONObject(i);
                                    pDialog.cancel();

                                    if (movieObject.getString("password").equals(md5(editpassword.getText().toString()))) {
                                        final String lokasi = movieObject.getString("lokasi_struktur");
                                        final String jabatan = movieObject.getString("jabatan_struktur");
                                        final String level = movieObject.getString("level_jabatan_karyawan");
                                        final String nama = movieObject.getString("nama_karyawan_struktur");
                                        final String password = editpassword.getText().toString();

                                        CheckingData(lokasi, jabatan, level, nama, password);



                                    } else if (!movieObject.getString("password").equals(md5(editpassword.getText().toString()))) {
                                        new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                                .setContentText("Oops... NIK / Password Salah")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();
                                    }

                                }

                            } else {
                                new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Oops... NIK / Password Salah")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
                                pDialog.cancel();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.cancel();
                        if (error instanceof ServerError) {
                            new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("NIK Baru anda Salah")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else {
                            new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Jaringan Bermasalah")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
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
        };
        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(login.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void CheckingData(String lokasi, String jabatan, String level, String nama, String password) {
        StringRequest biodata = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Login/index_depo?nik_baru=" + nikbaru.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();

                        success = new SweetAlertDialog(login.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setContentText("Selamat Datang");
                        success.setCancelable(false);
                        success.setConfirmText("OK");
                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(login.this, MainActivity.class);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("nik_baru", nikbaru.getText().toString());
                                        editor.putString("lokasi", lokasi);
                                        editor.putString("jabatan", jabatan);
                                        editor.putString("level", level);
                                        editor.putString("nama", nama);
                                        editor.putString("password", password);

                                        editor.apply();
                                        sDialog.dismissWithAnimation();
                                        startActivity(intent);
                                    }
                                })
                                .show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        if (error instanceof ServerError) {
                            new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Maaf, anda belum terdaftar")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else {
                            new SweetAlertDialog(login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Jaringan Bermasalah")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        }
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", "admin", "Databa53");
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };

        biodata.setRetryPolicy(
                new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue biodataQueue = Volley.newRequestQueue(this);
        biodataQueue.getCache().clear();
        biodataQueue.add(biodata);
    }

    public String md5(String s) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                hash = md.digest(s.getBytes(StandardCharsets.UTF_8));
            }
            StringBuilder sb = new StringBuilder(2*hash.length);
            for(byte b : hash)
            {
                sb.append(String.format("%02x", b&0xff));
            }
            digest = sb.toString();
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return digest;
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(login.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Apakah anda yakin?")
                .setContentText("Anda akan keluar dari aplikasi ini")
                .setConfirmText("Yes")
                .setCancelText("Cancel")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        finishAffinity();
                        finish();
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }
}