package com.tvip.teknisi_dispenser.menu_form_kunjungan;

import static com.tvip.teknisi_dispenser.menu_form_kunjungan.form_kunjungan.kodedepo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_utama.MainActivity;
import com.tvip.teknisi_dispenser.menu_verifikasi_dispenser.history_verifikasi_dispenser;
import com.tvip.teknisi_dispenser.pojos.dispenser_pojo;
import com.tvip.teknisi_dispenser.pojos.tanggal_kunjungan;
import com.tvip.teknisi_dispenser.third_party.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class pilih_id_dispenser extends AppCompatActivity {
    MaterialCardView verifikasi_layout;
    TextView tanggal_verifikasi, id_toko, nama_toko;
    ListView list_dispenser;
    List<dispenser_pojo> dispenserPojoList = new ArrayList<>();
    String kondisi;
    EditText namapelanggan;

    int sudah, belum;
    String ttd;
    
    ListViewAdapterDispenser adapter;
    SweetAlertDialog pDialog, errorType, success;

    LinearLayout ttd_layout;
    LinearLayout linear_ttd;
    SignaturePad signature_view;
    MaterialButton hapus_ttd;

    MaterialButton batal, selesai;
    SharedPreferences sharedPreferences;
    TextView summary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilih_id_dispenser);


        tanggal_verifikasi = findViewById(R.id.tanggal_verifikasi);
        id_toko = findViewById(R.id.id_toko);
        nama_toko = findViewById(R.id.nama_toko);
        list_dispenser = findViewById(R.id.list_dispenser);
        summary = findViewById(R.id.summary);

        ttd_layout = findViewById(R.id.ttd_layout);
        linear_ttd = findViewById(R.id.linear_ttd);
        signature_view = findViewById(R.id.signature_view);
        hapus_ttd = findViewById(R.id.hapus_ttd);
        namapelanggan = findViewById(R.id.namapelanggan);

        verifikasi_layout = findViewById(R.id.verifikasi_layout);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        String start = sdf.format(new Date());
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);


        tanggal_verifikasi.setText(start);
        id_toko.setText(getIntent().getStringExtra("id_toko") + " • " + getIntent().getStringExtra("depo"));
        nama_toko.setText(getIntent().getStringExtra("nama_toko"));

        batal = findViewById(R.id.batal);
        selesai = findViewById(R.id.selesai);

        verifikasi_layout.setVisibility(View.GONE);

        verifikasi_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), history_verifikasi_dispenser.class);

                intent.putExtra("idtoko", getIntent().getStringExtra("id_toko"));
                intent.putExtra("depo", getIntent().getStringExtra("depo"));
                intent.putExtra("namatoko", getIntent().getStringExtra("nama_toko"));

                startActivity(intent);
            }
        });




        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(pilih_id_dispenser.this, form_kunjungan.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
                startActivity(intent);
                finish();
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signature_view.isEmpty()){
                    new SweetAlertDialog(pilih_id_dispenser.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Mohon Isi Tanda Tangan")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (namapelanggan.getText().toString().length() == 0){
                    namapelanggan.setError("Wajib Di Isi");
                } else {
                    if(kondisi.equals("lengkap")){
                        verifikasiOK("");
                    } else {
                        new SweetAlertDialog(pilih_id_dispenser.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText("Apakah anda yakin?")
                                .setContentText("Pastikan data verifikasi dispenser yang Anda masukan sudah sesuai.")
                                .setConfirmText("Ya")
                                .setCancelText("Tidak")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Dialog filter = new Dialog(pilih_id_dispenser.this);
                                        filter.setContentView(R.layout.notes);
                                        filter.setCancelable(false);

                                        EditText catatan = filter.findViewById(R.id.catatan);
                                        MaterialButton tidak = filter.findViewById(R.id.tidak);
                                        MaterialButton ya = filter.findViewById(R.id.ya);

                                        tidak.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                filter.dismiss();
                                            }
                                        });

                                        ya.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(catatan.getText().toString().length()==0){
                                                    catatan.setError("Harap Di Isi");
                                                } else {
                                                    verifikasiOK(catatan.getText().toString());
                                                }
                                            }
                                        });
                                        filter.show();

                                        sDialog.cancel();
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
            }
        });
        


        linear_ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_ttd.setVisibility(View.GONE);
            }
        });

        hapus_ttd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_ttd.setVisibility(View.VISIBLE);
                signature_view.clear();
            }
        });

    }

    private void cekVerifikasi() {
        String link = "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_cek_verifikasi?id_toko="+getIntent().getStringExtra("id_toko")+"&nik_baru="+sharedPreferences.getString("nik_baru", null);
        System.out.println("Link = " + link);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_cek_verifikasi?id_toko="+getIntent().getStringExtra("id_toko")+"&nik_baru="+sharedPreferences.getString("nik_baru", null),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);



                                if(movieObject.getString("ttd").equals("null")){
                                    ttd = "Empty";
                                } else {
                                    ttd = "Not Empty";
                                    selesai.setVisibility(View.GONE);
                                    ttd_layout.setVisibility(View.GONE);
                                    batal.setText("Kembali");
                                }
                            }
                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismissWithAnimation();
                        ttd_layout.setVisibility(View.GONE);
                        selesai.setEnabled(false);
                        selesai.setBackgroundColor(Color.parseColor("#808080"));
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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    private void verifikasiOK(String notes) {
        pDialog = new SweetAlertDialog(pilih_id_dispenser.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();
        SimpleDateFormat gambar = new SimpleDateFormat("yyyyMMddHHmmss");
        String gambarDateandTime = gambar.format(new Date());

        String namattdverifikasi = getIntent().getStringExtra("id_toko")+"_"+"ttd_verifikasi_"+gambarDateandTime;
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_verifikasi_dispenser",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        uploadVerifikasi(namattdverifikasi);
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

                params.put("nik_baru", sharedPreferences.getString("nik_baru", null));
                params.put("id_toko", getIntent().getStringExtra("id_toko"));
                params.put("ttd", namattdverifikasi + ".jpeg");
                params.put("catatan", notes);
                params.put("nama_pelanggan", namapelanggan.getText().toString());


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
        RequestQueue requestQueue33 = Volley.newRequestQueue(pilih_id_dispenser.this);
        requestQueue33.add(stringRequest2);
    }

    private void uploadVerifikasi(String namattdverifikasi) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://apisec.tvip.co.id/mobile_eis_2/upload_dispenser.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
                        success = new SweetAlertDialog(pilih_id_dispenser.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setContentText("Data Berhasil Disimpan");
                        success.setCancelable(false);
                        success.setConfirmText("OK");
                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(pilih_id_dispenser.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
                                        startActivity(intent);
                                        finish();
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

                String gambar = ImageToString(signature_view.getSignatureBitmap());

                params.put("nama_foto", namattdverifikasi);
                params.put("foto", gambar);
                params.put("folder", "dispenser_verifikasi_ttd");


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


        RequestQueue requestQueue5 = Volley.newRequestQueue(pilih_id_dispenser.this);
        requestQueue5.add(stringRequest2);
    }

    private void getDispenserList() {
        final String[] jenissewa = new String[1];
        pDialog = new SweetAlertDialog(pilih_id_dispenser.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        adapter = new ListViewAdapterDispenser(dispenserPojoList, getApplicationContext());
        list_dispenser.setAdapter(adapter);
        adapter.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_dispenser_list?szSoldToBranchId="+kodedepo+"&szCustomerId="+getIntent().getStringExtra("id_toko"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                if(movieObject.getString("decCharge").equals("0.0000")){
                                    jenissewa[0] = "FOC";
                                } else {
                                    jenissewa[0] = "SEWA";
                                }
                                final dispenser_pojo movieItem = new dispenser_pojo(
                                        movieObject.getString("szSerialNumber"),
                                        movieObject.getString("szName"),
                                        jenissewa[0],
                                        movieObject.getString("conditions"));

                                dispenserPojoList.add(movieItem);

                                cekVerifikasi();

                                ttd = "Empty";

                                if(movieObject.getString("conditions").equals("1")){
                                    sudah ++;
                                    ttd_layout.setVisibility(View.VISIBLE);
                                    selesai.setEnabled(true);
                                    selesai.setBackgroundColor(Color.parseColor("#0F4C81"));
                                }

                                belum = movieArray.length();
                                summary.setText("List Dispenser " + "(" + String.valueOf(sudah) + "/" + String.valueOf(belum) + ")");





                                Utility.setListViewHeightBasedOnChildren(list_dispenser);
                                adapter.notifyDataSetChanged();

                            }
                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        errorType = new SweetAlertDialog(pilih_id_dispenser.this, SweetAlertDialog.ERROR_TYPE);
                        errorType.setContentText("Data Verifikasi Kosong");
                        errorType.setCancelable(false);
                        errorType.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        finish();
                                    }
                                });
                        errorType.show();
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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

    public class ListViewAdapterDispenser extends BaseAdapter {
        private List<dispenser_pojo> dispenserPojoList;

        private final Context context;

        public ListViewAdapterDispenser(List<dispenser_pojo> dataPelangganDalamRutePojos, Context context) {
            this.dispenserPojoList = dataPelangganDalamRutePojos;
            this.context = context;
        }

        @Override
        public int getCount() {
            return dispenserPojoList.size();
        }

        @Override
        public Object getItem(int position) {
            return dispenserPojoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clear() {
            dispenserPojoList.clear();

        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View listViewItem = getLayoutInflater().inflate(R.layout.item_dispenser_2, null, true);

            TextView id_dispenser = listViewItem.findViewById(R.id.id_dispenser);
            Chip chip_condition = listViewItem.findViewById(R.id.chip_condition);
            TextView jenis_dispenser = listViewItem.findViewById(R.id.jenis_dispenser);
            TextView status_dispenser = listViewItem.findViewById(R.id.status_dispenser);
            RelativeLayout layout_dispenser= listViewItem.findViewById(R.id.layout_dispenser);
            ImageButton info_icon = listViewItem.findViewById(R.id.info_icon);
            MaterialButton verifikasi = listViewItem.findViewById(R.id.verifikasi);

            layout_dispenser.setVisibility(View.VISIBLE);

            status_dispenser.setText(dispenserPojoList.get(position).getSzOrderItemTypeId());

            info_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    detail_dispenser(dispenserPojoList.get(position).getSzSerialNumber(), dispenserPojoList.get(position).getSzRentCalcId());
                }
            });

            verifikasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(ttd.equals("Empty")){
                        if(dispenserPojoList.get(position).getCondition().equals("1")){

                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String mulai = sdf.format(new Date());

                            Intent intent = new Intent(getApplicationContext(), fisik_dispenser.class);
                            intent.putExtra("idtoko", getIntent().getStringExtra("id_toko"));

                            intent.putExtra("szSerialNumber", dispenserPojoList.get(position).getSzSerialNumber());
                            intent.putExtra("szRentCalcId", dispenserPojoList.get(position).getSzRentCalcId());
                            intent.putExtra("jenis_sewa", dispenserPojoList.get(position).getSzOrderItemTypeId());

                            intent.putExtra("start", mulai);

                            startActivity(intent);
                        }
                    } else {
                        new SweetAlertDialog(pilih_id_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                                .setContentText("Maaf, Kunjungan sudah selesai diakhiri")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                    }
                                })
                                .show();
                    }

                }
            });

            id_dispenser.setText(dispenserPojoList.get(position).getSzSerialNumber());
            jenis_dispenser.setText(dispenserPojoList.get(position).getSzRentCalcId());
            if(dispenserPojoList.get(position).getCondition().equals("0")){
                chip_condition.setText("Belum");
                chip_condition.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#FDCD69")));
                chip_condition.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#FFF9ED")));
                chip_condition.setTextColor(ColorStateList.valueOf(Color.parseColor("#FF9900")));
                kondisi = "kurang lengkap";
            } else {
                chip_condition.setText("Selesai");
                chip_condition.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#B9EED0")));
                chip_condition.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#E8FFF2")));
                chip_condition.setTextColor(ColorStateList.valueOf(Color.parseColor("#2ECC71")));
            }

            return listViewItem;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void detail_dispenser(String szSerialNumber, String szRentCalcId) {
        Dialog filter = new Dialog(pilih_id_dispenser.this);
        filter.setContentView(R.layout.detail_dispenser);
        filter.setCancelable(false);
        List<tanggal_kunjungan> tanggal_kunjungans = new ArrayList<>();
        final ListViewAdapterTanggalKunjungan[] adapterTanggalKunjungan = new ListViewAdapterTanggalKunjungan[1];

        TextView id_dispenser = filter.findViewById(R.id.id_dispenser);
        TextView jenis_dispenser = filter.findViewById(R.id.jenis_dispenser);
        TextView tanggalverifikasi = filter.findViewById(R.id.tanggalverifikasi);

        TextView qtyperbaikan = filter.findViewById(R.id.qtyperbaikan);
        TextView qtymaintenance = filter.findViewById(R.id.qtymaintenance);
        TextView qtyverifikasi = filter.findViewById(R.id.qtyverifikasi);

        ListView list_tanggal = filter.findViewById(R.id.list_tanggal);

        LocalDate today = LocalDate.now();

        // Calculate the first day of three months ago
        LocalDate firstDate = today.with(TemporalAdjusters.firstDayOfYear());

        // Calculate the last day of the last month in the current year
        LocalDate lastDate = today.with(TemporalAdjusters.lastDayOfYear());

        // Format the dates
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        String firstDateFormatted = firstDate.format(formatter);
        String lastDateFormatted = lastDate.format(formatter);

        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String firstDateFormatted2 = firstDate.format(formatter2);
        String lastDateFormatted2 = lastDate.format(formatter2);

        MaterialButton batal = filter.findViewById(R.id.batal);

        id_dispenser.setText(szSerialNumber);
        jenis_dispenser.setText(szRentCalcId);
        tanggalverifikasi.setText(firstDateFormatted + " - " + lastDateFormatted);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_total_verifikasi?no_seri="+id_dispenser.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getTanggalKunjungan(id_dispenser.getText().toString());
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                qtymaintenance.setText(movieObject.getString("total_maintenance"));
                                qtyperbaikan.setText(movieObject.getString("total_perbaikan"));
                                qtyverifikasi.setText(movieObject.getString("total_verifikasi"));
                            }
                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }

                    private void getTanggalKunjungan(String dispenser) {
                        adapterTanggalKunjungan[0] = new ListViewAdapterTanggalKunjungan(tanggal_kunjungans, getApplicationContext());
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_verifikasi_bulan?no_seri="+dispenser,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            final JSONArray movieArray = obj.getJSONArray("data");
                                            for (int i = 0; i < movieArray.length(); i++) {

                                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                                final tanggal_kunjungan movieItem = new tanggal_kunjungan(
                                                        movieObject.getString("start_verifikasi"),
                                                        movieObject.getString("cek_kunjungan"));

                                                tanggal_kunjungans.add(movieItem);

//                                                Utility.setListViewHeightBasedOnChildren(list_tanggal);
                                                list_tanggal.setAdapter(adapterTanggalKunjungan[0]);
                                                adapterTanggalKunjungan[0].notifyDataSetChanged();

                                            }
                                        } catch(JSONException e){
                                            e.printStackTrace();

                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

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

                        stringRequest.setRetryPolicy(
                                new DefaultRetryPolicy(
                                        5000,
                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                                ));
                        RequestQueue requestQueue = Volley.newRequestQueue(pilih_id_dispenser.this);
                        requestQueue.getCache().clear();
                        requestQueue.add(stringRequest);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

        stringRequest.setRetryPolicy(
                new DefaultRetryPolicy(
                        5000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                ));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter.dismiss();
            }
        });

        filter.show();
    }

    public class ListViewAdapterTanggalKunjungan extends BaseAdapter {
        private List<tanggal_kunjungan> dispenserPojoList;

        private final Context context;

        public ListViewAdapterTanggalKunjungan(List<tanggal_kunjungan> dataPelangganDalamRutePojos, Context context) {
            this.dispenserPojoList = dataPelangganDalamRutePojos;
            this.context = context;
        }

        @Override
        public int getCount() {
            return dispenserPojoList.size();
        }

        @Override
        public Object getItem(int position) {
            return dispenserPojoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clear() {
            dispenserPojoList.clear();

        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View listViewItem = getLayoutInflater().inflate(R.layout.list_tanggal_kunjungan, null, true);


            TextView tanggal_kunjungan_3_bulan = listViewItem.findViewById(R.id.tanggal_kunjungan_3_bulan);
            TextView jenis_verifikasi_3_bulan = listViewItem.findViewById(R.id.jenis_verifikasi_3_bulan);


            tanggal_kunjungan_3_bulan.setText(dispenserPojoList.get(position).getTanggal());
            jenis_verifikasi_3_bulan.setText(dispenserPojoList.get(position).getVerifikasi());

            return listViewItem;
        }
    }

    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sudah = 0;
        belum = 0;
        kondisi = "lengkap";
        getDispenserList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(pilih_id_dispenser.this, form_kunjungan.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
        startActivity(intent);
        finish();
    }
}