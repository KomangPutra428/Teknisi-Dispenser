package com.tvip.teknisi_dispenser.menu_utama;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_login.login;
import com.tvip.teknisi_dispenser.third_party.HttpsTrustManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class biodata extends AppCompatActivity {
    TextView dept_text;
    TextView jabatan_text;
    Button logout;
    TextView lokasi_text;
    TextView nama_text;
    TextView nik_text;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biodata);
        HttpsTrustManager.allowAllSSL();
        nik_text = (TextView) findViewById(R.id.nik_text);
        nama_text = (TextView) findViewById(R.id.nama_text);
        jabatan_text = (TextView) findViewById(R.id.jabatan_text);
        dept_text = (TextView) findViewById(R.id.dept_text);
        lokasi_text = (TextView) findViewById(R.id.lokasi_text);
        Button button = (Button) findViewById(R.id.logout);
        logout = button;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(biodata.this);
                alertDialogBuilder.setTitle((CharSequence) "Apakah anda yakin ingin logout?");
                alertDialogBuilder.setCancelable(false).setPositiveButton((CharSequence) "Ya", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent inetnt = new Intent(getBaseContext(), login.class);
                        inetnt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(inetnt);
                    }
                }).setNegativeButton((CharSequence) "Tidak", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                alertDialogBuilder.create().show();
            }
        });
        SharedPreferences sharedPreferences2 = getSharedPreferences("user_details", 0);
        sharedPreferences = sharedPreferences2;
        StringRequest stringRequest = new StringRequest(0, "https://hrd.tvip.co.id/rest_server/master/karyawan/index?nik_baru=" + sharedPreferences2.getString("nik_baru", (String) null), new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString(NotificationCompat.CATEGORY_STATUS).equals("true")) {
                        JSONArray movieArray = obj.getJSONArray("data");
                        for (int i = 0; i < movieArray.length(); i++) {
                            JSONObject movieObject = movieArray.getJSONObject(i);
                            nik_text.setText(movieObject.getString("nik_baru"));
                            nama_text.setText(movieObject.getString("nama_karyawan_struktur"));
                            jabatan_text.setText(movieObject.getString("jabatan_karyawan"));
                            dept_text.setText(movieObject.getString("dept_struktur"));
                            lokasi_text.setText(movieObject.getString("lokasi_struktur"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", "Basic " + Base64.encodeToString(String.format("%s:%s", new Object[]{"admin", "Databa53"}).getBytes(), 0));
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(500000, 1, 1.0f));
        Volley.newRequestQueue(this).add(stringRequest);
        
    }
}