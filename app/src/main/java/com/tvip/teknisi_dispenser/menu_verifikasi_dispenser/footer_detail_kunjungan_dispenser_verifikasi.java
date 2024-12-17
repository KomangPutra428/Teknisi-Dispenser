package com.tvip.teknisi_dispenser.menu_verifikasi_dispenser;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.footer_detail_kunjungan_dispenser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class footer_detail_kunjungan_dispenser_verifikasi extends AppCompatActivity {
    TextView qtyverifikasi, qtymaintenance, qtyperbaikan;
    SweetAlertDialog pDialog;

    String link_seri, link_depan, pelanggan, lokasi_dispenser;
    Chip no_seri;
    TextView air, fisik_dispenser, lokasi, tipe_dispenser, catatan;
    MaterialButton foto_seri, foto_depan;
    RelativeLayout dispenser_footer_layout;
    MaterialCardView fisik_layout;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;

    RelativeLayout air_layout, catatan_layout, seri_layout, depan_layout;
    TextView catatan_luar, catatan_atas, catatan_panas, catatan_dingin,
            catatan_mesin, catatan_arde, catatan_reseptor, catatan_lampu, catatan_kunjungan,
            catatan_keterangan;

    TextView status_luar, status_atas, status_panas, status_dingin, status_mesin, status_arde,
            status_reseptor, status_lampu, status_kunjungan, status_keterangan, status_dispenser_sewa;

    RelativeLayout luar_layout, atas_layout, panas_layout, dingin_layout, mesin_layout,
            arde_layout, reseptor_layout, lampu_layout, kunjungan_layout, keterangan_layout;

    ImageView dropdown_luar, dropdown_atas, dropdown_panas, dropdown_dingin, dropdown_mesin,
            dropdown_arde, dropdown_reseptor, dropdown_lampu, dropdown_kunjungan, dropdown_keterangan;

    LinearLayout catatan_luar_layout, catatan_atas_layout, catatan_panas_layout, catatan_dingin_layout, catatan_mesin_layout,
            catatan_arde_layout, catatan_reseptor_layout, catatan_lampu_layout, catatan_kunjungan_layout, catatan_keterangan_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footer_detail_kunjungan_dispenser_verifikasi);
        no_seri = findViewById(R.id.no_seri);

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

        no_seri.setText(getIntent().getStringExtra("id_dispenser"));
        fisik_dispenser.setText(getIntent().getStringExtra("fisik"));
        tipe_dispenser.setText(getIntent().getStringExtra("jenis"));
        catatan.setText(getIntent().getStringExtra("catatan"));

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



        getFooterDispenser();
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
                                        ImageView fullScreenImage = new ImageView(footer_detail_kunjungan_dispenser_verifikasi.this);
                                        fullScreenImage.setBackgroundColor(Color.BLACK); // Set background color

// Set layout parameters for the full-screen image
                                        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                        );

// Create a new ImageButton for the close button
                                        ImageButton closeButton = new ImageButton(footer_detail_kunjungan_dispenser_verifikasi.this);
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
                                                .addHeader("Authorization", new footer_detail_kunjungan_dispenser_verifikasi.BasicAuthorization("admin", "Databa53"))
                                                .build();

                                        Glide.with(footer_detail_kunjungan_dispenser_verifikasi.this)
                                                .load(new GlideUrl(imageUrl, auth))
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true)
                                                .into(fullScreenImage);
                                    }

                                });

                                foto_depan.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImageView fullScreenImage = new ImageView(footer_detail_kunjungan_dispenser_verifikasi.this);
                                        fullScreenImage.setBackgroundColor(Color.BLACK); // Set background color

// Set layout parameters for the full-screen image
                                        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                        );

// Create a new ImageButton for the close button
                                        ImageButton closeButton = new ImageButton(footer_detail_kunjungan_dispenser_verifikasi.this);
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
                                                .addHeader("Authorization", new footer_detail_kunjungan_dispenser_verifikasi.BasicAuthorization("admin", "Databa53"))
                                                .build();

                                        Glide.with(footer_detail_kunjungan_dispenser_verifikasi.this)
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
                        fisik_layout.setVisibility(View.GONE);

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

}