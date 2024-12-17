package com.tvip.teknisi_dispenser.menu_history_kunjungan;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.SearchView;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_dispenser;
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_galon;
import com.tvip.teknisi_dispenser.menu_utama.MainActivity;
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

public class history_kunjungan extends AppCompatActivity {
    ListView kunjungan;
    List<header_pojo> header_pojoList = new ArrayList<>();
    SweetAlertDialog pDialog;

    ListViewAdapterKunjungan adapter;
    SharedPreferences sharedPreferences;
    EditText cari;

    ImageButton filterButton;
    private SimpleDateFormat dateFormatter;
    private Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_kunjungan);
        kunjungan = findViewById(R.id.kunjungan);
        dateFormatter = new SimpleDateFormat("dd/MMM/yyyy", Locale.getDefault());


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = sdf.format(new Date());
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        cari = findViewById(R.id.cari);
        filterButton = findViewById(R.id.filterButton);

        kunjungan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                    intent.putExtra("soldToBranch", header_pojoList.get(position).getSzSoldToBranchId());

                    intent.putExtra("start_awal", header_pojoList.get(position).getStart_verifikasi());
                    intent.putExtra("end_akhir", header_pojoList.get(position).getEnd_verifikasi());
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
                    intent.putExtra("soldToBranch", header_pojoList.get(position).getSzSoldToBranchId());

                    intent.putExtra("start_awal", header_pojoList.get(position).getStart_verifikasi());
                    intent.putExtra("end_akhir", header_pojoList.get(position).getEnd_verifikasi());
                    startActivity(intent);
                }

            }
        });

        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog filter = new Dialog(history_kunjungan.this);
                filter.setContentView(R.layout.filtering);
                filter.setCancelable(false);

                EditText tgl_awal = filter.findViewById(R.id.tgl_awal);
                EditText tgl_akhir = filter.findViewById(R.id.tgl_akhir);
                AutoCompleteTextView jenis_verifikasi = filter.findViewById(R.id.jenis_verifikasi);

                MaterialButton batal = filter.findViewById(R.id.batal);
                MaterialButton lihat = filter.findViewById(R.id.lihat);

                String[] items = new String[]{"Semua", "Gallon", "Dispenser"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(history_kunjungan.this, android.R.layout.simple_dropdown_item_1line, items);
                jenis_verifikasi.setAdapter(adapter);


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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(history_kunjungan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
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
                        DatePickerDialog datePickerDialog = new DatePickerDialog(history_kunjungan.this, dateSetListener, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
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
                        } else if (jenis_verifikasi.getText().toString().length() == 0){
                            jenis_verifikasi.setError("Wajib Di Isi");
                        } else {
                            filter.dismiss();
                            if(tgl_akhir.getText().toString().length() == 0){
                                getDataKunjungan(convertFormatReverse(tgl_awal.getText().toString()), convertFormatReverse(tgl_awal.getText().toString()), jenis_verifikasi.getText().toString());
                            } else {
                                getDataKunjungan(convertFormatReverse(tgl_awal.getText().toString()), convertFormatReverse(tgl_akhir.getText().toString()), jenis_verifikasi.getText().toString());
                            }
                        }
                    }
                });

                filter.show();


            }
        });

        getDataKunjungan(currentDateandTime, currentDateandTime, "Semua");
    }

    private void getDataKunjungan(String date1, String date2, String tipe) {
        pDialog = new SweetAlertDialog(history_kunjungan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        adapter = new ListViewAdapterKunjungan(header_pojoList, getApplicationContext());
        kunjungan.setAdapter(adapter);
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

                                header_pojoList.add(movieItem);


                                if(tipe.equals("Semua")){
                                    adapter.notifyDataSetChanged();
                                } else if (tipe.equals("Gallon")){
                                    if(!movieObject.getString("jenis_verifikasi").equals("GAlLON")){
                                        header_pojoList.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                } else if (tipe.equals("Dispenser")){
                                    if(!movieObject.getString("jenis_verifikasi").equals("DISPENSER")){
                                        header_pojoList.remove(movieItem);
                                        adapter.notifyDataSetChanged();
                                    }
                                }

                                cari.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        adapter.getFilter().filter(s);
                                    }
                                });

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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
        startActivity(intent);
        finish();
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