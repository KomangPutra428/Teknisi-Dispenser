package com.tvip.teknisi_dispenser.menu_utama;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.view.View;

import androidx.core.view.WindowCompat;


import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.databinding.ActivityMainBinding;
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_dispenser;
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_galon;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.form_kunjungan;
import com.tvip.teknisi_dispenser.menu_history_kunjungan.history_kunjungan;
import com.tvip.teknisi_dispenser.pojos.header_pojo;
import com.tvip.teknisi_dispenser.third_party.HttpsTrustManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    TextView txt_nama, date_text;
    MaterialCardView button_form_kunjungan, button_history_kunjungan;
    SharedPreferences sharedPreferences;
    ListView list_kunjungan;
    RelativeLayout error_layout;

    List<header_pojo> header_pojoList = new ArrayList<>();
    SweetAlertDialog pDialog;

    ListViewAdapterKunjungan adapter;
    ImageButton setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpsTrustManager.allowAllSSL();
        list_kunjungan = findViewById(R.id.list_kunjungan);
        error_layout = findViewById(R.id.error);
        setting = findViewById(R.id.setting);

        txt_nama = findViewById(R.id.txt_nama);
        date_text = findViewById(R.id.date_text);

        button_form_kunjungan = findViewById(R.id.button_form_kunjungan);
        button_history_kunjungan = findViewById(R.id.button_history_kunjungan);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        txt_nama.setText(sharedPreferences.getString("nama", null));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        String currentDateandTime = sdf.format(new Date());
        date_text.setText(currentDateandTime);

        button_form_kunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), form_kunjungan.class);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), biodata.class);
                startActivity(intent);
            }
        });

        list_kunjungan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(header_pojoList.get(position).getJenis_verifikasi().equals("GALLON")){
                    Intent intent = new Intent(getApplicationContext(), detail_kunjungan_galon.class);
                    intent.putExtra("id", header_pojoList.get(position).getId());
                    intent.putExtra("tanggal_kunjungan", header_pojoList.get(position).getTanggal_verifikasi());
                    intent.putExtra("id_toko", header_pojoList.get(position).getId_toko());
                    intent.putExtra("nama", header_pojoList.get(position).getSzName());
                    intent.putExtra("alamat", header_pojoList.get(position).getSzAddress());
                    intent.putExtra("segment", "IOD");
                    intent.putExtra("depo", header_pojoList.get(position).getDepo());

                    intent.putExtra("start_awal", header_pojoList.get(position).getStart_verifikasi());
                    intent.putExtra("end_akhir", header_pojoList.get(position).getEnd_verifikasi());
                    intent.putExtra("soldToBranch", header_pojoList.get(position).getSzSoldToBranchId());

                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), detail_kunjungan_dispenser.class);
                    intent.putExtra("id", header_pojoList.get(position).getId());
                    intent.putExtra("tanggal_kunjungan", header_pojoList.get(position).getTanggal_verifikasi());
                    intent.putExtra("id_toko", header_pojoList.get(position).getId_toko());
                    intent.putExtra("nama", header_pojoList.get(position).getSzName());
                    intent.putExtra("alamat", header_pojoList.get(position).getSzAddress());
                    intent.putExtra("segment", "IOD");
                    intent.putExtra("depo", header_pojoList.get(position).getDepo());

                    intent.putExtra("start_awal", header_pojoList.get(position).getStart_verifikasi());
                    intent.putExtra("end_akhir", header_pojoList.get(position).getEnd_verifikasi());
                    intent.putExtra("soldToBranch", header_pojoList.get(position).getSzSoldToBranchId());

                    startActivity(intent);
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
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

    @Override
    protected void onResume() {
        super.onResume();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        getDataKunjungan(currentDateandTime, currentDateandTime);
    }

    private void getDataKunjungan(String currentDateandTime, String currentDateandTime1) {
        pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();


        adapter = new ListViewAdapterKunjungan(header_pojoList, getApplicationContext());
        list_kunjungan.setAdapter(adapter);
        adapter.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_header?nik_baru="+sharedPreferences.getString("nik_baru", null)+"&date1="+currentDateandTime+"&date2="+currentDateandTime,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {

                        button_history_kunjungan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getApplicationContext(), history_kunjungan.class);
                                startActivity(intent);
                            }
                        });

                        pDialog.dismissWithAnimation();
                        list_kunjungan.setVisibility(View.VISIBLE);
                        error_layout.setVisibility(View.GONE);
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);
                                final header_pojo movieItem = new header_pojo(
                                        movieObject.getString("id"),
                                        movieObject.getString("id_toko"),
                                        movieObject.getString("depo"),
                                        movieObject.getString("tanggal_verifikasi"),
                                        movieObject.getString("szName"),
                                        movieObject.getString("szAddress"),
                                        movieObject.getString("jenis_verifikasi"),
                                        movieObject.getString("start_verifikasi"),
                                        movieObject.getString("end_verifikasi"),
                                        movieObject.getString("szSoldToBranchId"));

                                header_pojoList.add(movieItem);
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
                        pDialog.dismissWithAnimation();
                        list_kunjungan.setVisibility(View.GONE);
                        error_layout.setVisibility(View.VISIBLE);

                        button_history_kunjungan.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                        .setContentText("Belum Ada Kunjungan")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                            }
                                        })
                                        .show();
                            }
                        });
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

    public class ListViewAdapterKunjungan extends BaseAdapter implements Filterable {
        private final List<header_pojo> dataPelangganDalamRutePojos;
        private List<header_pojo> dataPelangganDalamRutePojosFiltered;

        private final Context context;

        public ListViewAdapterKunjungan(List<header_pojo> dataPelangganDalamRutePojos, Context context) {
            this.dataPelangganDalamRutePojos = dataPelangganDalamRutePojos;
            this.dataPelangganDalamRutePojosFiltered = dataPelangganDalamRutePojos;
            this.context = context;
        }

        @Override
        public int getCount() {
            return dataPelangganDalamRutePojosFiltered.size();
        }

        @Override
        public Object getItem(int position) {
            return dataPelangganDalamRutePojosFiltered.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void clear() {
            dataPelangganDalamRutePojosFiltered.clear();

        }

        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View listViewItem = getLayoutInflater().inflate(R.layout.kunjungal_list_item, null, true);

            TextView tanggal_verifikasi = listViewItem.findViewById(R.id.tanggal_verifikasi);
            Chip chip_text = listViewItem.findViewById(R.id.chip_text);
            TextView id_toko = listViewItem.findViewById(R.id.id_toko);
            TextView nama_toko = listViewItem.findViewById(R.id.nama_toko);


            tanggal_verifikasi.setText("Submit date • " + convertFormat(dataPelangganDalamRutePojosFiltered.get(position).getTanggal_verifikasi()));
            id_toko.setText(dataPelangganDalamRutePojosFiltered.get(position).getId_toko() + " • " + dataPelangganDalamRutePojosFiltered.get(position).getDepo());
            nama_toko.setText(dataPelangganDalamRutePojosFiltered.get(position).getSzName());
            chip_text.setText(dataPelangganDalamRutePojosFiltered.get(position).getJenis_verifikasi());

            if(dataPelangganDalamRutePojosFiltered.get(position).getJenis_verifikasi().equals("GALLON")){
                chip_text.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#AFC3D5")));
                chip_text.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#EDF7FF")));
                chip_text.setTextColor(ColorStateList.valueOf(Color.parseColor("#0F4C81")));
            } else {
                chip_text.setChipStrokeColor(ColorStateList.valueOf(Color.parseColor("#B9EED0")));
                chip_text.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor("#E8FFF2")));
                chip_text.setTextColor(ColorStateList.valueOf(Color.parseColor("#2ECC71")));
            }

            return listViewItem;
        }
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {

                    FilterResults filterResults = new FilterResults();
                    if(constraint == null || constraint.length() == 0){
                        filterResults.count = dataPelangganDalamRutePojos.size();
                        filterResults.values = dataPelangganDalamRutePojos;

                    }else{
                        List<header_pojo> resultsModel = new ArrayList<>();
                        String searchStr = constraint.toString().toUpperCase();

                        for(header_pojo itemsModel:dataPelangganDalamRutePojos){
                            if(itemsModel.getSzName().contains(searchStr) || itemsModel.getId().contains(searchStr)){
                                resultsModel.add(itemsModel);

                            }
                            filterResults.count = resultsModel.size();
                            filterResults.values = resultsModel;
                        }


                    }

                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    dataPelangganDalamRutePojosFiltered = (List<header_pojo>) results.values;
                    notifyDataSetChanged();

                }
            };
            return filter;
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
}