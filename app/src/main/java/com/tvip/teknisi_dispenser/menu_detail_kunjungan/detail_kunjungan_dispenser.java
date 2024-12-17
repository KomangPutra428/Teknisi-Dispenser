package com.tvip.teknisi_dispenser.menu_detail_kunjungan;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.google.android.material.chip.Chip;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.form_kunjungan;
import com.tvip.teknisi_dispenser.menu_history_kunjungan.history_kunjungan;
import com.tvip.teknisi_dispenser.pojos.dispenser_pojo;
import com.tvip.teknisi_dispenser.pojos.footer_dispenser_pojo;
import com.tvip.teknisi_dispenser.pojos.header_pojo;
import com.tvip.teknisi_dispenser.third_party.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class detail_kunjungan_dispenser extends AppCompatActivity {
    static String ttd_dispenser, id_header;
    public static String idtokopost;
    TextView tanggal_kunjungan, id_toko, nama_toko, alamat_pelanggan,
            depo, segment, nama_pic, verifikasi;

    SharedPreferences sharedPreferences;
    
    ListView list_dispenser_kunjungan;
    List<footer_dispenser_pojo>footer_dispenser_pojoList = new ArrayList<>();
    ListViewAdapterDispenser adapter;

    MaterialButton kembali, transaksi, foto_ttd;

    RelativeLayout dispenser_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kunjungan_dispenser);

        tanggal_kunjungan = findViewById(R.id.tanggal_kunjungan);
        id_toko = findViewById(R.id.id_toko);
        nama_toko = findViewById(R.id.nama_toko);
        alamat_pelanggan = findViewById(R.id.alamat_pelanggan);
        segment = findViewById(R.id.segment);
        depo = findViewById(R.id.depo);
        nama_pic = findViewById(R.id.nama_pic);
        verifikasi = findViewById(R.id.verifikasi);
        list_dispenser_kunjungan = findViewById(R.id.list_dispenser_kunjungan);
        foto_ttd = findViewById(R.id.foto_ttd);
        dispenser_layout = findViewById(R.id.dispenser_layout);

        kembali = findViewById(R.id.kembali);
        transaksi = findViewById(R.id.transaksi);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        tanggal_kunjungan.setText(convertFormat(getIntent().getStringExtra("tanggal_kunjungan")));
        id_toko.setText(getIntent().getStringExtra("id_toko"));
        nama_toko.setText(getIntent().getStringExtra("nama"));
        alamat_pelanggan.setText(getIntent().getStringExtra("alamat"));
        segment.setText(getIntent().getStringExtra("segment"));
        depo.setText(getIntent().getStringExtra("depo"));
        nama_pic.setText(sharedPreferences.getString("nama", null));

        String startString = getIntent().getStringExtra("start_awal");
        String endString = getIntent().getStringExtra("end_akhir");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date startDate = sdf.parse(startString);
            Date endDate = sdf.parse(endString);

            long differenceInMillis = endDate.getTime() - startDate.getTime();
            long minutes = (differenceInMillis / (1000 * 60)) % 60;
            long seconds = (differenceInMillis / 1000) % 60;

            verifikasi.setText(minutes + " min, " + seconds + " dtk");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        getListDispenser();

        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        idtokopost = id_toko.getText().toString();


        transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detail_kunjungan_dispenser.this, form_kunjungan.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
                intent.putExtra("depo_pelanggan", depo.getText().toString());
                intent.putExtra("kode_pelanggan", id_toko.getText().toString());
                intent.putExtra("nama_pelanggan", nama_toko.getText().toString());
                intent.putExtra("alamat_pelanggan", alamat_pelanggan.getText().toString());
                intent.putExtra("soldToBranch", getIntent().getStringExtra("soldToBranch"));

                startActivity(intent);
                finish();
            }
        });
        list_dispenser_kunjungan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), footer_detail_kunjungan_dispenser.class);
                intent.putExtra("id_dispenser", footer_dispenser_pojoList.get(position).getId_dispenser());
                intent.putExtra("catatan", footer_dispenser_pojoList.get(position).getCatatan());
                intent.putExtra("jenis", footer_dispenser_pojoList.get(position).getJenis_dispenser());
                intent.putExtra("id_header", footer_dispenser_pojoList.get(position).getId_header());
                intent.putExtra("fisik", footer_dispenser_pojoList.get(position).getStatus_fisik());
                intent.putExtra("tanggal_kunjungan", convertFormat(getIntent().getStringExtra("tanggal_kunjungan")));
                intent.putExtra("id_toko", getIntent().getStringExtra("id_toko"));

                intent.putExtra("nama", getIntent().getStringExtra("nama"));
                intent.putExtra("alamat", getIntent().getStringExtra("alamat"));
                intent.putExtra("depo", getIntent().getStringExtra("depo"));


                startActivity(intent);
            }
        });
    }

    private void getListDispenser() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_header_dispenser?id_header="+getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final footer_dispenser_pojo movieItem = new footer_dispenser_pojo(
                                        movieObject.getString("id"),
                                        movieObject.getString("id_dispenser"),
                                        movieObject.getString("jenis_dispenser"),
                                        movieObject.getString("status_fisik"),
                                        movieObject.getString("catatan"));

                                footer_dispenser_pojoList.add(movieItem);

                                adapter = new ListViewAdapterDispenser(footer_dispenser_pojoList, getApplicationContext());
                                Utility.setListViewHeightBasedOnChildren(list_dispenser_kunjungan);
                                list_dispenser_kunjungan.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                ttd_dispenser = "https://apisec.tvip.co.id/gambar_dispenser/dispenser_verifikasi_ttd/" + movieObject.getString("ttd");

                                foto_ttd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImageView fullScreenImage = new ImageView(detail_kunjungan_dispenser.this);
                                        fullScreenImage.setBackgroundColor(Color.BLACK); // Set background color

// Set layout parameters for the full-screen image
                                        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                        );

// Create a new ImageButton for the close button
                                        ImageButton closeButton = new ImageButton(detail_kunjungan_dispenser.this);
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
                                        dispenser_layout.addView(fullScreenImage, imageParams);

// Add the close button to the main layout after the ImageView to ensure it appears on top
                                        dispenser_layout.addView(closeButton, closeParams);

// Debugging: Log to verify if views are being added
                                        Log.d("ImageView", "Full-screen image added to layout");
                                        Log.d("ImageButton", "Close button added to layout");

// Set click listener for the full-screen image to remove it when clicked
// Set click listener for the close button to remove the full-screen image and itself when clicked
                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Remove the image and the close button from the layout
                                                dispenser_layout.removeView(fullScreenImage);
                                                dispenser_layout.removeView(closeButton);
                                                Log.d("ImageView", "Full-screen image removed from layout");
                                                Log.d("ImageButton", "Close button removed from layout");
                                            }
                                        });
                                        // Get the image URL from the intent
                                        String imageUrl = null; // Key must match the sender's intent
                                        try {
                                            imageUrl = "https://apisec.tvip.co.id/gambar_dispenser/dispenser_verifikasi_ttd/" + movieObject.getString("ttd");
                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }

                                        LazyHeaders auth = new LazyHeaders.Builder() // can be cached in a field and reused
                                                .addHeader("Authorization", new BasicAuthorization("admin", "Databa53"))
                                                .build();

                                        Glide.with(detail_kunjungan_dispenser.this)
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
        private List<footer_dispenser_pojo> dispenserPojoList;

        private final Context context;

        public ListViewAdapterDispenser(List<footer_dispenser_pojo> dataPelangganDalamRutePojos, Context context) {
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

            View listViewItem = getLayoutInflater().inflate(R.layout.item_dispenser, null, true);

            TextView id_dispenser = listViewItem.findViewById(R.id.id_dispenser);
            RelativeLayout status = listViewItem.findViewById(R.id.status);
            TextView jenis_dispenser = listViewItem.findViewById(R.id.jenis_dispenser);

            jenis_dispenser.setVisibility(View.GONE);
            status.setVisibility(View.GONE);

            id_dispenser.setText(dispenserPojoList.get(position).getId_dispenser());


            return listViewItem;
        }
    }

    public static String convertFormat(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd/MMM/yy");
        return convetDateFormat.format(date);
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