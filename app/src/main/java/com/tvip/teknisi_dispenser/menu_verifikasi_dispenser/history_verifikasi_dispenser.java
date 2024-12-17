package com.tvip.teknisi_dispenser.menu_verifikasi_dispenser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

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
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_dispenser;
import com.tvip.teknisi_dispenser.menu_history_kunjungan.history_kunjungan;
import com.tvip.teknisi_dispenser.pojos.header_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class history_verifikasi_dispenser extends AppCompatActivity {
    TextView id_toko, nama_toko;
    ImageButton filterButton;
    private Calendar date;
    private SimpleDateFormat dateFormatter;

    ListView list_dispenser_verifikasi;
    List<header_pojo> header_pojoList = new ArrayList<>();
    SweetAlertDialog pDialog;
    ListViewAdapterDispenserVerifikasi adapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_verifikasi_dispenser);
        dateFormatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());

        id_toko = findViewById(R.id.id_toko);
        nama_toko = findViewById(R.id.nama_toko);
        filterButton = findViewById(R.id.filterButton);
        list_dispenser_verifikasi = findViewById(R.id.list_dispenser_verifikasi);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        getDataKunjungan(currentDateandTime, currentDateandTime);

        id_toko.setText(getIntent().getStringExtra("idtoko") + " • " + getIntent().getStringExtra("depo"));
        nama_toko.setText(getIntent().getStringExtra("namatoko"));

        list_dispenser_verifikasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), detail_kunjungan_dispenser_verifikasi.class);
                    intent.putExtra("id", header_pojoList.get(position).getId());
                    intent.putExtra("tanggal_kunjungan", header_pojoList.get(position).getTanggal_verifikasi());
                    intent.putExtra("id_toko", header_pojoList.get(position).getId_toko());
                    intent.putExtra("nama", header_pojoList.get(position).getSzName());
                    intent.putExtra("alamat", header_pojoList.get(position).getSzAddress());
                    intent.putExtra("segment", "IOD");
                    intent.putExtra("depo", header_pojoList.get(position).getDepo());
                    intent.putExtra("soldToBranch", header_pojoList.get(position).getSzSoldToBranchId());

                    intent.putExtra("start_awal", header_pojoList.get(position).getStart_verifikasi());
                    intent.putExtra("end_akhir", header_pojoList.get(position).getEnd_verifikasi());
                    startActivity(intent);
            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog filter = new Dialog(history_verifikasi_dispenser.this);
                filter.setContentView(R.layout.filtering_dispenser);
                filter.setCancelable(false);

                EditText tgl_awal = filter.findViewById(R.id.tgl_awal);
                EditText tgl_akhir = filter.findViewById(R.id.tgl_akhir);

                MaterialButton batal = filter.findViewById(R.id.batal);
                MaterialButton lihat = filter.findViewById(R.id.lihat);


                tgl_akhir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar currentDate = Calendar.getInstance();
                        Calendar twoDaysAgo = (Calendar) currentDate.clone();
                        twoDaysAgo.add(Calendar.DATE, 0);

                        date = currentDate.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth);

                                tgl_akhir.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(history_verifikasi_dispenser.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                        datePickerDialog.show();
                    }
                });

                tgl_awal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar currentDate = Calendar.getInstance();
                        Calendar twoDaysAgo = (Calendar) currentDate.clone();
                        twoDaysAgo.add(Calendar.DATE, 0);

                        date = currentDate.getInstance();

                        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                date.set(year, monthOfYear, dayOfMonth);

                                tgl_awal.setText(dateFormatter.format(date.getTime()));
                            }
                        };
                        DatePickerDialog datePickerDialog = new DatePickerDialog(history_verifikasi_dispenser.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMaxDate(twoDaysAgo.getTimeInMillis());
                        datePickerDialog.show();
                    }
                });

                batal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        filter.dismiss();
                    }
                });

                lihat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(tgl_awal.getText().toString().length() == 0){
                            tgl_awal.setError("Wajib Di Isi");
                        } else {
                            filter.dismiss();
                            if(tgl_akhir.getText().toString().length() == 0){
                                getDataKunjungan(convertFormatReverse(tgl_awal.getText().toString()), convertFormatReverse(tgl_awal.getText().toString()));
                            } else {
                                getDataKunjungan(convertFormatReverse(tgl_awal.getText().toString()), convertFormatReverse(tgl_akhir.getText().toString()));
                            }
                        }
                    }
                });

                filter.show();


            }
        });


    }

    private void getDataKunjungan(String date1, String date2) {
        pDialog = new SweetAlertDialog(history_verifikasi_dispenser.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        adapter = new ListViewAdapterDispenserVerifikasi(header_pojoList, getApplicationContext());
        list_dispenser_verifikasi.setAdapter(adapter);
        adapter.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_header?nik_baru="+sharedPreferences.getString("nik_baru", null)+"&date1="+date1+"&date2="+date2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismissWithAnimation();
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




                                if(movieObject.getString("jenis_verifikasi").equals("DISPENSER") && movieObject.getString("id_toko").equals(getIntent().getStringExtra("idtoko"))){
                                    header_pojoList.add(movieItem);
                                    adapter.notifyDataSetChanged();
                                }



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

    public class ListViewAdapterDispenserVerifikasi extends BaseAdapter implements Filterable {
        private final List<header_pojo> dataPelangganDalamRutePojos;
        private List<header_pojo> dataPelangganDalamRutePojosFiltered;

        private final Context context;

        public ListViewAdapterDispenserVerifikasi(List<header_pojo> dataPelangganDalamRutePojos, Context context) {
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

            View listViewItem = getLayoutInflater().inflate(R.layout.list_verifikasi_dispenser, null, true);

            TextView tanggal_verifikasi = listViewItem.findViewById(R.id.tanggal_kunjungan);


            tanggal_verifikasi.setText(convertFormat(dataPelangganDalamRutePojosFiltered.get(position).getTanggal_verifikasi()));


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
                            if(itemsModel.getSzName().contains(searchStr) || itemsModel.getId_toko().contains(searchStr)){
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
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        return convetDateFormat.format(date);
    }

    public static String convertFormatReverse(String inputDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yy");
        Date date = null;
        try {
            date = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date == null) {
            return "";
        }
        SimpleDateFormat convetDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return convetDateFormat.format(date);
    }

}