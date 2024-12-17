package com.tvip.teknisi_dispenser.menu_detail_kunjungan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_edit.edit_verifikasi_dispenser;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.verifikasi_dispenser;
import com.tvip.teknisi_dispenser.menu_history_kunjungan.history_kunjungan;
import com.tvip.teknisi_dispenser.pojos.footer_dispenser_pojo;
import com.tvip.teknisi_dispenser.pojos.tanggal_kunjungan;
import com.tvip.teknisi_dispenser.third_party.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class footer_detail_kunjungan_dispenser extends AppCompatActivity {
    TextView qtyverifikasi, qtymaintenance, qtyperbaikan;
    SweetAlertDialog pDialog;

    public static String realdispenser;

    public static String link_seri;
    public static String link_depan;
    String pelanggan, lokasi_dispenser;
    public static Chip no_seri;
    public static TextView air;
    static TextView fisik_dispenser;
    public static TextView lokasi;
    static TextView tipe_dispenser;
    static TextView catatan;
    MaterialButton foto_seri, foto_depan, info_dispenser;
    RelativeLayout dispenser_footer_layout;
    MaterialCardView fisik_layout;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;

    RelativeLayout air_layout, catatan_layout, seri_layout, depan_layout;
    public static TextView catatan_luar, catatan_atas, catatan_panas, catatan_dingin,
            catatan_mesin, catatan_arde, catatan_reseptor, catatan_lampu, catatan_kunjungan,
            catatan_keterangan;

    public static TextView status_luar, status_atas, status_panas, status_dingin, status_mesin, status_arde,
            status_reseptor, status_lampu, status_kunjungan, status_keterangan, status_dispenser_sewa;

    RelativeLayout luar_layout, atas_layout, panas_layout, dingin_layout, mesin_layout,
            arde_layout, reseptor_layout, lampu_layout, kunjungan_layout, keterangan_layout;

    ImageView dropdown_luar, dropdown_atas, dropdown_panas, dropdown_dingin, dropdown_mesin,
            dropdown_arde, dropdown_reseptor, dropdown_lampu, dropdown_kunjungan, dropdown_keterangan;

    LinearLayout catatan_luar_layout, catatan_atas_layout, catatan_panas_layout, catatan_dingin_layout, catatan_mesin_layout,
            catatan_arde_layout, catatan_reseptor_layout, catatan_lampu_layout, catatan_kunjungan_layout, catatan_keterangan_layout;

    MaterialButton kembali, print;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footer_detail_kunjungan_dispenser);
        no_seri = findViewById(R.id.no_seri);
        kembali = findViewById(R.id.kembali);
        print = findViewById(R.id.print);

        qtyverifikasi = findViewById(R.id.qtyverifikasi);
        qtymaintenance = findViewById(R.id.qtymaintenance);
        qtyperbaikan = findViewById(R.id.qtyperbaikan);

        dropdown_luar = findViewById(R.id.dropdown_luar);
        dropdown_atas = findViewById(R.id.dropdown_atas);
        dropdown_panas = findViewById(R.id.dropdown_panas);
        dropdown_dingin = findViewById(R.id.dropdown_dingin);
        dropdown_mesin = findViewById(R.id.dropdown_mesin);

        dropdown_arde = findViewById(R.id.dropdown_arde);
        dropdown_reseptor = findViewById(R.id.dropdown_reseptor);
        dropdown_lampu = findViewById(R.id.dropdown_lampu);
        dropdown_kunjungan = findViewById(R.id.dropdown_kunjungan);
        dropdown_keterangan = findViewById(R.id.dropdown_keterangan);

        catatan_luar_layout = findViewById(R.id.catatan_luar_layout);
        catatan_atas_layout = findViewById(R.id.catatan_atas_layout);
        catatan_panas_layout = findViewById(R.id.catatan_panas_layout);
        catatan_dingin_layout = findViewById(R.id.catatan_dingin_layout);
        catatan_mesin_layout = findViewById(R.id.catatan_mesin_layout);

        catatan_arde_layout = findViewById(R.id.catatan_arde_layout);
        catatan_reseptor_layout = findViewById(R.id.catatan_reseptor_layout);
        catatan_lampu_layout = findViewById(R.id.catatan_lampu_layout);
        catatan_kunjungan_layout = findViewById(R.id.catatan_kunjungan_layout);
        catatan_keterangan_layout = findViewById(R.id.catatan_keterangan_layout);

        luar_layout = findViewById(R.id.luar_layout);
        atas_layout = findViewById(R.id.atas_layout);
        panas_layout = findViewById(R.id.panas_layout);
        dingin_layout = findViewById(R.id.dingin_layout);
        mesin_layout = findViewById(R.id.mesin_layout);

        arde_layout = findViewById(R.id.arde_layout);
        reseptor_layout = findViewById(R.id.reseptor_layout);
        lampu_layout = findViewById(R.id.lampu_layout);
        kunjungan_layout = findViewById(R.id.kunjungan_layout);
        keterangan_layout = findViewById(R.id.keterangan_layout);

        status_luar = findViewById(R.id.status_luar);
        status_atas = findViewById(R.id.status_atas);
        status_panas = findViewById(R.id.status_panas);
        status_dingin = findViewById(R.id.status_dingin);
        status_mesin = findViewById(R.id.status_mesin);

        status_arde = findViewById(R.id.status_arde);
        status_reseptor = findViewById(R.id.status_reseptor);
        status_lampu = findViewById(R.id.status_lampu);
        status_kunjungan = findViewById(R.id.status_kunjungan);
        status_keterangan = findViewById(R.id.status_keterangan);

        catatan_luar = findViewById(R.id.catatan_luar);
        catatan_atas = findViewById(R.id.catatan_atas);
        catatan_panas = findViewById(R.id.catatan_panas);
        catatan_dingin = findViewById(R.id.catatan_dingin);
        catatan_mesin = findViewById(R.id.catatan_mesin);

        catatan_arde = findViewById(R.id.catatan_arde);
        catatan_reseptor = findViewById(R.id.catatan_reseptor);
        catatan_lampu = findViewById(R.id.catatan_lampu);
        catatan_kunjungan = findViewById(R.id.catatan_kunjungan);
        catatan_keterangan = findViewById(R.id.catatan_keterangan);

        air = findViewById(R.id.air);
        fisik_dispenser = findViewById(R.id.fisik_dispenser);
        lokasi = findViewById(R.id.lokasi);
        tipe_dispenser = findViewById(R.id.tipe_dispenser);
        catatan = findViewById(R.id.catatan);
        dispenser_footer_layout = findViewById(R.id.dispenser_footer_layout);
        status_dispenser_sewa = findViewById(R.id.status_dispenser_sewa);

        foto_seri = findViewById(R.id.foto_seri);
        foto_depan = findViewById(R.id.foto_depan);
        info_dispenser = findViewById(R.id.info_dispenser);



        fisik_dispenser.setText(getIntent().getStringExtra("fisik"));
        tipe_dispenser.setText(getIntent().getStringExtra("jenis"));
        catatan.setText(getIntent().getStringExtra("catatan"));

        realdispenser = getIntent().getStringExtra("id_dispenser");

        air_layout = findViewById(R.id.air_layout);
        catatan_layout = findViewById(R.id.catatan_layout);
        seri_layout = findViewById(R.id.seri_layout);
        depan_layout = findViewById(R.id.depan_layout);

        fisik_layout = findViewById(R.id.fisik_layout);

        if(getIntent().getStringExtra("catatan").equals("null")){
            catatan_layout.setVisibility(View.GONE);
        } else {
            seri_layout.setVisibility(View.GONE);
            depan_layout.setVisibility(View.GONE);
            air_layout.setVisibility(View.GONE);
        }

        catatan_luar_layout.setVisibility(View.GONE);
        catatan_atas_layout.setVisibility(View.GONE);
        catatan_panas_layout.setVisibility(View.GONE);
        catatan_dingin_layout.setVisibility(View.GONE);
        catatan_mesin_layout.setVisibility(View.GONE);
        catatan_arde_layout.setVisibility(View.GONE);
        catatan_reseptor_layout.setVisibility(View.GONE);
        catatan_lampu_layout.setVisibility(View.GONE);
        catatan_kunjungan_layout.setVisibility(View.GONE);
        catatan_keterangan_layout.setVisibility(View.GONE);

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new SweetAlertDialog(footer_detail_kunjungan_dispenser.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();
                generatePdf();
            }
        });

        luar_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatan_luar_layout.getVisibility() == View.VISIBLE){
                    dropdown_luar.setImageResource(R.drawable.arrow_down);
                    catatan_luar_layout.setVisibility(View.GONE);
                } else {
                    dropdown_luar.setImageResource(R.drawable.arrow_up);
                    catatan_luar_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        atas_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatan_atas_layout.getVisibility() == View.VISIBLE){
                    dropdown_atas.setImageResource(R.drawable.arrow_down);
                    catatan_atas_layout.setVisibility(View.GONE);
                } else {
                    dropdown_atas.setImageResource(R.drawable.arrow_up);
                    catatan_atas_layout.setVisibility(View.VISIBLE);
                }
            }
        });


        panas_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatan_panas_layout.getVisibility() == View.VISIBLE){
                    dropdown_panas.setImageResource(R.drawable.arrow_down);
                    catatan_panas_layout.setVisibility(View.GONE);
                } else {
                    dropdown_panas.setImageResource(R.drawable.arrow_up);
                    catatan_panas_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        dingin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatan_dingin_layout.getVisibility() == View.VISIBLE){
                    dropdown_dingin.setImageResource(R.drawable.arrow_down);
                    catatan_dingin_layout.setVisibility(View.GONE);
                } else {
                    dropdown_dingin.setImageResource(R.drawable.arrow_up);
                    catatan_dingin_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        mesin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatan_mesin_layout.getVisibility() == View.VISIBLE){
                    dropdown_mesin.setImageResource(R.drawable.arrow_down);
                    catatan_mesin_layout.setVisibility(View.GONE);
                } else {
                    dropdown_mesin.setImageResource(R.drawable.arrow_up);
                    catatan_mesin_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        arde_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatan_arde_layout.getVisibility() == View.VISIBLE){
                    dropdown_arde.setImageResource(R.drawable.arrow_down);
                    catatan_arde_layout.setVisibility(View.GONE);
                } else {
                    dropdown_arde.setImageResource(R.drawable.arrow_up);
                    catatan_arde_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        reseptor_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatan_reseptor_layout.getVisibility() == View.VISIBLE){
                    dropdown_reseptor.setImageResource(R.drawable.arrow_down);
                    catatan_reseptor_layout.setVisibility(View.GONE);
                } else {
                    dropdown_reseptor.setImageResource(R.drawable.arrow_up);
                    catatan_reseptor_layout.setVisibility(View.VISIBLE);
                }

            }
        });

        lampu_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatan_lampu_layout.getVisibility() == View.VISIBLE){
                    dropdown_lampu.setImageResource(R.drawable.arrow_down);
                    catatan_lampu_layout.setVisibility(View.GONE);
                } else {
                    dropdown_lampu.setImageResource(R.drawable.arrow_up);
                    catatan_lampu_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        kunjungan_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatan_kunjungan_layout.getVisibility() == View.VISIBLE){
                    dropdown_kunjungan.setImageResource(R.drawable.arrow_down);
                    catatan_kunjungan_layout.setVisibility(View.GONE);
                } else {
                    dropdown_kunjungan.setImageResource(R.drawable.arrow_up);
                    catatan_kunjungan_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        keterangan_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(catatan_keterangan_layout.getVisibility() == View.VISIBLE){
                    dropdown_keterangan.setImageResource(R.drawable.arrow_down);
                    catatan_keterangan_layout.setVisibility(View.GONE);
                } else {
                    dropdown_keterangan.setImageResource(R.drawable.arrow_up);
                    catatan_keterangan_layout.setVisibility(View.VISIBLE);
                }
            }
        });

        info_dispenser.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                Dialog filter = new Dialog(footer_detail_kunjungan_dispenser.this);
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

                System.out.println("Formatted Range (MMM yyyy): " + firstDateFormatted + " to " + lastDateFormatted);
                System.out.println("Formatted Range (yyyy-MM-dd): " + firstDateFormatted2 + " to " + lastDateFormatted2);


                MaterialButton batal = filter.findViewById(R.id.batal);

                id_dispenser.setText(no_seri.getText().toString());
                jenis_dispenser.setText(tipe_dispenser.getText().toString());
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
                                RequestQueue requestQueue = Volley.newRequestQueue(footer_detail_kunjungan_dispenser.this);
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
                RequestQueue requestQueue = Volley.newRequestQueue(footer_detail_kunjungan_dispenser.this);
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
        });



        getFooterDispenser();
    }

    private void checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_CODE_STORAGE_PERMISSION);
        } else {
            // Izin sudah diberikan, panggil fungsi generatePdf()
            generatePdf();
        }
    }

    private void generatePdf() {
        new GeneratePdfTask().execute(link_seri, link_depan);
    }



    private void addCellToTable(PdfPTable table, String key, String value) {
        table.addCell(new Phrase(key + "", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        table.addCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA, 12)));
    }

    private Image fetchImage(String imageUrl) {
        HttpsURLConnection connection = null; // Use HttpsURLConnection
        try {
            URL url = new URL(imageUrl);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            // Add Basic Authentication header
            String username = "admin"; // Replace with your actual username
            String password = "Databa53"; // Replace with your actual password
            String auth = username + ":" + password;
            String encodedAuth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", "Basic " + encodedAuth);

            connection.connect();

            System.out.println("Link Gambar = " + imageUrl);

            int responseCode = connection.getResponseCode();
            Log.d("ImageFetchResponseCode", "Response Code: " + responseCode);

            if (responseCode != HttpsURLConnection.HTTP_OK) {
                Log.e("ImageFetchError", "HTTP error code: " + responseCode);
                return null;
            }

            InputStream input = connection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            input.close();
            return Image.getInstance(outputStream.toByteArray());
        } catch (MalformedURLException e) {
            Log.e("ImageFetchException", "Malformed URL: " + e.toString(), e);
        } catch (IOException e) {
            Log.e("ImageFetchException", "IO Exception: " + e.toString(), e);
        } catch (Exception e) {
            Log.e("ImageFetchException", "General Exception: "  + e.toString(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return null; // Return null if image fetching fails
    }

    private void addCellToTableImage(PdfPTable table, String key, String imageUrl, AtomicInteger processedImages, int totalImages) {
        // Add key cell
        PdfPCell keyCell = new PdfPCell(new Phrase(key + "", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        keyCell.setBorder(PdfPCell.NO_BORDER); // No border for the key cell
        table.addCell(keyCell);

        // Create a separate thread for fetching the image
        new Thread(() -> {
            Image image = fetchImage(imageUrl);
            runOnUiThread(() -> {
                PdfPCell imageCell;
                if (image != null) {
                    Log.d("ImageFetchSuccess", "Image fetched successfully");
                    image.scaleToFit(150, 150); // Adjust the image size as needed
                    imageCell = new PdfPCell(image);
                } else {
                    Log.e("ImageFetchError", "Failed to fetch image from URL: " + imageUrl);
                    imageCell = new PdfPCell(new Phrase("Image not found", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                }
                imageCell.setBorder(PdfPCell.NO_BORDER); // No border for the image cell
                table.addCell(imageCell);

                // Optional: Add a blank cell for spacing after the image
                PdfPCell blankCell = new PdfPCell(new Phrase(" ")); // Empty cell for spacing
                blankCell.setBorder(PdfPCell.NO_BORDER); // No border for the blank cell
                blankCell.setFixedHeight(10f); // Height for the spacing, adjust as needed
                table.addCell(blankCell); // Add blank cell to the table

                // Increment the processed image counter
                processedImages.incrementAndGet();
            });
        }).start();
    }

    private void addCellToTableImage2(PdfPTable table, String key, String imageUrl, AtomicInteger processedImages, int totalImages) {
        // Create a separate thread for fetching the image
        new Thread(() -> {
            Image image = fetchImage(imageUrl);
            runOnUiThread(() -> {
                PdfPCell imageCell;
                if (image != null) {
                    Log.d("ImageFetchSuccess", "Image fetched successfully");
                    image.scaleToFit(150, 150); // Adjust the image size as needed
                    imageCell = new PdfPCell(image);
                } else {
                    Log.e("ImageFetchError", "Failed to fetch image from URL: " + imageUrl);
                    imageCell = new PdfPCell(new Phrase("Image not found", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                }
                imageCell.setBorder(PdfPCell.NO_BORDER); // No border for the image cell
                table.addCell(imageCell);

                // Optional: Add a blank cell for spacing after the image
                PdfPCell blankCell = new PdfPCell(new Phrase(" ")); // Empty cell for spacing
                blankCell.setBorder(PdfPCell.NO_BORDER); // No border for the blank cell
                blankCell.setFixedHeight(10f); // Height for the spacing, adjust as needed
                table.addCell(blankCell); // Add blank cell to the table

                // Increment the processed image counter
                processedImages.incrementAndGet();
            });
        }).start();


    }






    private void showPdfDialog(String filePath) {
        pDialog.dismissWithAnimation();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("PDF Berhasil Dibuat");
        builder.setMessage("Apakah Anda ingin membuka file PDF?");
        builder.setPositiveButton("Buka", (dialog, which) -> {
            Intent intent = new Intent(this, PdfReviewGalon.class);
            intent.putExtra("pdfFilePath", filePath); // Pass file path to the new activity
            startActivity(intent);
        });
        builder.setNegativeButton("Tidak", (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }

    private void getFooterDispenser() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_footer_dispenser?id_footer="+getIntent().getStringExtra("id_header"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        totalVerifikasi();

                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault());
                                String currentDateandTime = sdf.format(new Date());
                                no_seri.setText(movieObject.getString("no_seri"));

                                if(getIntent().getStringExtra("tanggal_kunjungan").equals(currentDateandTime)){
                                    kembali.setText("Edit");
                                    kembali.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(getApplicationContext(), edit_verifikasi_dispenser.class);
                                            intent.putExtra("seridispenser", no_seri.getText().toString());
                                            try {
                                                intent.putExtra("validasi", movieObject.getString("cek_seri"));
                                                intent.putExtra("validasi_seri", movieObject.getString("no_seri"));
                                            } catch (JSONException e) {
                                                throw new RuntimeException(e);
                                            }
                                            startActivity(intent);
                                        }
                                    });
                                } else {
                                    kembali.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            finish();
                                        }
                                    });

                                }

                                air.setText(movieObject.getString("pemakaian_air"));


                                status_luar.setText(movieObject.getString("cek_penampilan_luar"));
                                status_atas.setText(movieObject.getString("cek_cover_atas"));
                                status_panas.setText(movieObject.getString("cek_kran_panas"));
                                status_dingin.setText(movieObject.getString("cek_kran_dingin"));
                                status_mesin.setText(movieObject.getString("cek_mesin"));

                                status_arde.setText(movieObject.getString("cek_stecker_arde"));
                                status_reseptor.setText(movieObject.getString("cek_reseptor"));
                                status_lampu.setText(movieObject.getString("cek_lampu"));
                                status_kunjungan.setText(movieObject.getString("cek_kunjungan"));
                                status_keterangan.setText(movieObject.getString("cek_keterangan"));

                                catatan_luar.setText(movieObject.getString("catatan_luar"));
                                catatan_atas.setText(movieObject.getString("catatan_atas"));
                                catatan_panas.setText(movieObject.getString("catatan_panas"));
                                catatan_dingin.setText(movieObject.getString("catatan_dingin"));
                                catatan_mesin.setText(movieObject.getString("catatan_mesin"));

                                catatan_arde.setText(movieObject.getString("catatan_arde"));
                                catatan_reseptor.setText(movieObject.getString("catatan_reseptor"));
                                catatan_lampu.setText(movieObject.getString("catatan_lampu"));
                                catatan_kunjungan.setText(movieObject.getString("catatan_kunjungan"));
                                catatan_keterangan.setText(movieObject.getString("catatan_keterangan"));
                                status_dispenser_sewa.setText(movieObject.getString("jenis_sewa"));
                                link_seri = "https://apisec.tvip.co.id/gambar_dispenser/dispenser_seri/" + movieObject.getString("foto_seri");
                                link_depan = "https://apisec.tvip.co.id/gambar_dispenser/dispenser_depan/" + movieObject.getString("foto_depan_dispenser");

                                fisik_layout.setVisibility(View.VISIBLE);

                                if(movieObject.getString("nama_pelanggan").equals("null")){
                                    pelanggan = "pelanggan";
                                    lokasi_dispenser = getIntent().getStringExtra("alamat");
                                    lokasi.setText(getIntent().getStringExtra("alamat"));

                                } else {
                                    pelanggan = movieObject.getString("nama_pelanggan");
                                    lokasi_dispenser = movieObject.getString("lokasi_dispenser");
                                    lokasi.setText(lokasi_dispenser);

                                }
                                foto_seri.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImageView fullScreenImage = new ImageView(footer_detail_kunjungan_dispenser.this);
                                        fullScreenImage.setBackgroundColor(Color.BLACK); // Set background color

// Set layout parameters for the full-screen image
                                        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                        );

// Create a new ImageButton for the close button
                                        ImageButton closeButton = new ImageButton(footer_detail_kunjungan_dispenser.this);
                                        closeButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel); // Use a built-in close icon or your custom drawable
                                        closeButton.setBackgroundColor(Color.TRANSPARENT); // Make the background transparent
                                        closeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // Ensure the image scales properly inside the button

// Set layout parameters for the close button
                                        RelativeLayout.LayoutParams closeParams = new RelativeLayout.LayoutParams(
                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        );

// Position the close button in the top-right corner
                                        closeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                        closeParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                                        closeParams.setMargins(16, 130, 16, 16); // Optional: Adjust margins if needed

// Add the ImageView to the main layout first
                                        dispenser_footer_layout.addView(fullScreenImage, imageParams);

// Add the close button to the main layout after the ImageView to ensure it appears on top
                                        dispenser_footer_layout.addView(closeButton, closeParams);

// Debugging: Log to verify if views are being added
                                        Log.d("ImageView", "Full-screen image added to layout");
                                        Log.d("ImageButton", "Close button added to layout");

// Set click listener for the full-screen image to remove it when clicked
// Set click listener for the close button to remove the full-screen image and itself when clicked
                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Remove the image and the close button from the layout
                                                dispenser_footer_layout.removeView(fullScreenImage);
                                                dispenser_footer_layout.removeView(closeButton);
                                                Log.d("ImageView", "Full-screen image removed from layout");
                                                Log.d("ImageButton", "Close button removed from layout");
                                            }
                                        });
                                        // Get the image URL from the intent
                                        String imageUrl = null; // Key must match the sender's intent
                                        try {
                                            imageUrl = "https://apisec.tvip.co.id/gambar_dispenser/dispenser_seri/" + movieObject.getString("foto_seri");
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }

                                        LazyHeaders auth = new LazyHeaders.Builder() // can be cached in a field and reused
                                                .addHeader("Authorization", new BasicAuthorization("admin", "Databa53"))
                                                .build();

                                        Glide.with(footer_detail_kunjungan_dispenser.this)
                                                .load(new GlideUrl(imageUrl, auth))
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true)
                                                .into(fullScreenImage);
                                    }

                                });

                                foto_depan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImageView fullScreenImage = new ImageView(footer_detail_kunjungan_dispenser.this);
                                        fullScreenImage.setBackgroundColor(Color.BLACK); // Set background color

// Set layout parameters for the full-screen image
                                        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                        );

// Create a new ImageButton for the close button
                                        ImageButton closeButton = new ImageButton(footer_detail_kunjungan_dispenser.this);
                                        closeButton.setImageResource(android.R.drawable.ic_menu_close_clear_cancel); // Use a built-in close icon or your custom drawable
                                        closeButton.setBackgroundColor(Color.TRANSPARENT); // Make the background transparent
                                        closeButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE); // Ensure the image scales properly inside the button

// Set layout parameters for the close button
                                        RelativeLayout.LayoutParams closeParams = new RelativeLayout.LayoutParams(
                                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT
                                        );

// Position the close button in the top-right corner
                                        closeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                                        closeParams.addRule(RelativeLayout.ALIGN_PARENT_END);
                                        closeParams.setMargins(16, 130, 16, 16); // Optional: Adjust margins if needed

// Add the ImageView to the main layout first
                                        dispenser_footer_layout.addView(fullScreenImage, imageParams);

// Add the close button to the main layout after the ImageView to ensure it appears on top
                                        dispenser_footer_layout.addView(closeButton, closeParams);

// Debugging: Log to verify if views are being added
                                        Log.d("ImageView", "Full-screen image added to layout");
                                        Log.d("ImageButton", "Close button added to layout");

// Set click listener for the full-screen image to remove it when clicked
// Set click listener for the close button to remove the full-screen image and itself when clicked
                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Remove the image and the close button from the layout
                                                dispenser_footer_layout.removeView(fullScreenImage);
                                                dispenser_footer_layout.removeView(closeButton);
                                                Log.d("ImageView", "Full-screen image removed from layout");
                                                Log.d("ImageButton", "Close button removed from layout");
                                            }
                                        });
                                        // Get the image URL from the intent
                                        String imageUrl = null; // Key must match the sender's intent
                                        try {
                                            imageUrl = "https://apisec.tvip.co.id/gambar_dispenser/dispenser_depan/" + movieObject.getString("foto_depan_dispenser");
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }

                                        LazyHeaders auth = new LazyHeaders.Builder() // can be cached in a field and reused
                                                .addHeader("Authorization", new BasicAuthorization("admin", "Databa53"))
                                                .build();

                                        Glide.with(footer_detail_kunjungan_dispenser.this)
                                                .load(new GlideUrl(imageUrl, auth))
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true)
                                                .into(fullScreenImage);
                                    }
                                });
                            }
                        } catch(JSONException e){
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        kembali.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });

                        fisik_layout.setVisibility(View.GONE);
                        no_seri.setText(getIntent().getStringExtra("id_dispenser"));

                        status_luar.setText("-");
                        status_atas.setText("-");
                        status_panas.setText("-");
                        status_dingin.setText("-");
                        status_mesin.setText("-");
                        status_arde.setText("-");
                        status_reseptor.setText("-");
                        status_lampu.setText("-");
                        status_kunjungan.setText("-");
                        status_keterangan.setText("-");

                        catatan_luar.setText("-");
                        catatan_atas.setText("-");
                        catatan_panas.setText("-");
                        catatan_dingin.setText("-");
                        catatan_mesin.setText("-");
                        catatan_arde.setText("-");
                        catatan_reseptor.setText("-");
                        catatan_lampu.setText("-");
                        catatan_kunjungan.setText("-");
                        catatan_keterangan.setText("-");
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

    private void totalVerifikasi() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_total_verifikasi?no_seri="+no_seri.getText().toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
    }

    public class BasicAuthorization implements LazyHeaderFactory {
        private final String username;
        private final String password;

        public BasicAuthorization(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override public String buildHeader() {
            return "Basic " + Base64.encodeToString((username + ":" + password).getBytes(), Base64.NO_WRAP);
        }
    }

    private class GeneratePdfTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... imageUrls) {
            Document document = new Document();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String filePath = new File(downloadDir, getIntent().getStringExtra("id_toko") + "_" + currentDateandTime + "BeritaAcaraGalon.pdf").getAbsolutePath();

            try {
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // Adding Header
                document.add(new Paragraph("PT TIRTA VARIA INTIPRATAMA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
                document.add(new Paragraph("Jl. Kedoya Raya No. 1 - Jakarta Barat", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph("Telp. 5835-3885, 5835-3886, 5835-3887", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph(" ")); // Add spacing

                // Adding Title
                document.add(new Paragraph("FORM KUNJUNGAN & VERIFIKASI DISPENSER", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                document.add(new Paragraph(" ")); // Add spacing

                // Creating main information table
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setSpacingBefore(10f);
                infoTable.setSpacingAfter(10f);
                infoTable.getDefaultCell().setBorder(0); // Remove border

                // Information rows
                addCellToTable(infoTable, "Tanggal Kunjungan", getIntent().getStringExtra("tanggal_kunjungan"));
                addCellToTable(infoTable, "Kode Pelanggan", getIntent().getStringExtra("id_toko"));
                addCellToTable(infoTable, "Nama Pelanggan", getIntent().getStringExtra("nama"));
                addCellToTable(infoTable, "Alamat Pelanggan", getIntent().getStringExtra("alamat"));
                addCellToTable(infoTable, "Segmen Pelanggan", "IOD");
                addCellToTable(infoTable, "Depo Coverage", getIntent().getStringExtra("depo"));

                addCellToTable(infoTable, "No Seri Dispenser", no_seri.getText().toString());
                addCellToTable(infoTable, "Fisik Dispenser", fisik_dispenser.getText().toString());
                addCellToTable(infoTable, "Tipe Dispenser", tipe_dispenser.getText().toString());
                addCellToTable(infoTable, "Status Dispenser", status_dispenser_sewa.getText().toString());

                addCellToTable(infoTable, "Pemakaian Air", air.getText().toString());
                addCellToTable(infoTable, "Lokasi Dispenser", lokasi_dispenser);

                // Add table to document
                document.add(infoTable);

                // Adding Sub-Title
                document.add(new Paragraph(" "));
                document.add(new Paragraph("Foto Dispenser", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));

                PdfPTable gambar = new PdfPTable(2);
                gambar.setWidthPercentage(100);
                gambar.setSpacingBefore(10f);
                gambar.setSpacingAfter(10f);
                gambar.getDefaultCell().setBorder(0);

                final int totalImages = 1; // Number of images to fetch
                AtomicInteger processedImages = new AtomicInteger(0);

                addCellToTableImage(gambar, "Foto No Seri Dispenser", link_seri, processedImages, totalImages);
                // addCellToTableImage(gambar, "Foto Tampak Depan Dispenser", link_depan, processedImages, totalImages);

                // Wait until all images are processed
                while (processedImages.get() < totalImages) {
                    Thread.sleep(100);
                }

                document.add(gambar);
                document.add(new Paragraph("")); // Add spacing

                PdfPTable gambar2 = new PdfPTable(2);
                gambar2.setWidthPercentage(100);
                gambar2.setSpacingBefore(10f);
                gambar2.setSpacingAfter(10f);
                gambar2.getDefaultCell().setBorder(0);

                final int totalImages2 = 1; // Number of images to fetch
                AtomicInteger processedImages2 = new AtomicInteger(0);

                addCellToTableImage(gambar2, "Foto Tampak Depan Dispenser", link_depan, processedImages2, totalImages2);

                // Wait until all images are processed
                while (processedImages2.get() < totalImages2) {
                    Thread.sleep(100);
                }

                document.add(gambar2);
                document.add(new Paragraph(" ")); // Add spacing
                document.add(new Paragraph(" "));
                document.add(new Paragraph(" "));
                document.add(new Paragraph("FISIK DISPENSER", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));
                document.add(new Paragraph(" ")); // Add spacing

                PdfPTable infoTablefisik = new PdfPTable(2);
                infoTablefisik.setWidthPercentage(100);
                infoTablefisik.setSpacingBefore(10f);
                infoTablefisik.setSpacingAfter(10f);
                infoTablefisik.getDefaultCell().setBorder(0); // Remove border

                addCellToTable(infoTablefisik, "Penampilan Luar", status_luar.getText().toString());
                addCellToTable(infoTablefisik, "Catatan", catatan_luar.getText().toString());
                addSpacerCell(infoTablefisik);

                addCellToTable(infoTablefisik, "Cover Atas", status_atas.getText().toString());
                addCellToTable(infoTablefisik, "Catatan", catatan_atas.getText().toString());
                addSpacerCell(infoTablefisik);

                addCellToTable(infoTablefisik, "Kran Panas", status_panas.getText().toString());
                addCellToTable(infoTablefisik, "Catatan", catatan_panas.getText().toString());
                addSpacerCell(infoTablefisik);

                addCellToTable(infoTablefisik, "Kran Dingin/Netral", status_dingin.getText().toString());
                addCellToTable(infoTablefisik, "Catatan", catatan_dingin.getText().toString());
                addSpacerCell(infoTablefisik);

                addCellToTable(infoTablefisik, "Mesin", status_mesin.getText().toString());
                addCellToTable(infoTablefisik, "Catatan", catatan_mesin.getText().toString());
                addSpacerCell(infoTablefisik);

                addCellToTable(infoTablefisik, "Stecker Arde", status_arde.getText().toString());
                addCellToTable(infoTablefisik, "Catatan", catatan_arde.getText().toString());
                addSpacerCell(infoTablefisik);

                addCellToTable(infoTablefisik, "Reseptor", status_reseptor.getText().toString());
                addCellToTable(infoTablefisik, "Catatan", catatan_reseptor.getText().toString());
                addSpacerCell(infoTablefisik);

                addCellToTable(infoTablefisik, "Lampu Switch", status_lampu.getText().toString());
                addCellToTable(infoTablefisik, "Catatan", catatan_lampu.getText().toString());
                addSpacerCell(infoTablefisik);

                addCellToTable(infoTablefisik, "Hasil Kunjungan", status_kunjungan.getText().toString());
                addCellToTable(infoTablefisik, "Catatan", catatan_kunjungan.getText().toString());
                addSpacerCell(infoTablefisik);

                addCellToTable(infoTablefisik, "Keterangan", status_keterangan.getText().toString());
                addCellToTable(infoTablefisik, "Catatan", catatan_keterangan.getText().toString());
                addSpacerCell(infoTablefisik);

                document.add(infoTablefisik);

                // Footer
                PdfPTable infoTtd = new PdfPTable(2);
                infoTtd.setWidthPercentage(100);
                infoTtd.setSpacingBefore(10f);
                infoTtd.setSpacingAfter(10f);
                infoTtd.getDefaultCell().setBorder(0); // Hapus border

                addCellToTable(infoTtd, "DISAKSIKAN & DISETUJUI", "VERIFIKATOR");

                document.add(infoTtd);

                PdfPTable gambar3 = new PdfPTable(2);
                gambar3.setWidthPercentage(100);
                gambar3.setSpacingBefore(10f);
                gambar3.setSpacingAfter(10f);
                gambar3.getDefaultCell().setBorder(0);

                final int totalImages3 = 1; // Number of images to fetch
                AtomicInteger processedImages3 = new AtomicInteger(0);

                addCellToTableImage2(gambar3, " ", detail_kunjungan_dispenser.ttd_dispenser, processedImages3, totalImages3);

                System.out.println("Date TTD = " + detail_kunjungan_dispenser.ttd_dispenser);
                // Wait until all images are processed
                while (processedImages3.get() < totalImages3) {
                    Thread.sleep(100);
                }

                document.add(gambar3);

                PdfPTable infoTtd2 = new PdfPTable(2);
                infoTtd2.setWidthPercentage(100);
                infoTtd2.setSpacingBefore(10f);
                infoTtd2.setSpacingAfter(10f);
                infoTtd2.getDefaultCell().setBorder(0); // Hapus border

                addCellToTable(infoTtd2, pelanggan, "TEKNISI/PIC VERIFIKATOR");
                document.add(infoTtd2);



                document.close();
                final String finalFilePath = filePath;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showPdfDialog(finalFilePath);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("PDFCreationError", "Failed to create PDF: " + e.getMessage(), e);
                 // Return null if there was an error
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                showPdfDialog(result);
            } else {

            }
        }
    }

    private void addSpacerCell(PdfPTable table) {
        // Create an empty cell to add as a spacer
        PdfPCell spacerCell = new PdfPCell();
        spacerCell.setColspan(2);  // Span across both columns
        spacerCell.setFixedHeight(20f);  // Height of the spacer (adjust as needed)
        spacerCell.setBorder(Rectangle.NO_BORDER);  // No border for the spacer cell
        table.addCell(spacerCell);  // Add the spacer cell to the table
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


}