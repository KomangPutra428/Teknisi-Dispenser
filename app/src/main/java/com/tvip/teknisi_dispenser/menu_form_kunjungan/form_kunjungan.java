package com.tvip.teknisi_dispenser.menu_form_kunjungan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
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
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_galon;
import com.tvip.teknisi_dispenser.menu_login.login;
import com.tvip.teknisi_dispenser.menu_utama.MainActivity;
import com.tvip.teknisi_dispenser.pojos.CustomItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class form_kunjungan extends AppCompatActivity {
    static AutoCompleteTextView pilihdepo;
    static AutoCompleteTextView pilihcustomer;
    static EditText namapelanggan;
    EditText alamatpelanggan;
    RadioGroup statusverifikasi;
    SweetAlertDialog pDialog;

    List<CustomItem> items = new ArrayList<>();
    ArrayList<String> namadepo = new ArrayList<>();
    ArrayList<String> iddepo = new ArrayList<>();


    ArrayList<String> namatoko = new ArrayList<>();
    ArrayList<String> idtoko = new ArrayList<>();
    ArrayList<String> alamattoko = new ArrayList<>();
    MaterialButton mulai;
    static String kodedepo;


    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_kunjungan);

        pilihdepo = findViewById(R.id.pilihdepo);
        pilihcustomer = findViewById(R.id.pilihcustomer);

        namapelanggan = findViewById(R.id.namapelanggan);
        alamatpelanggan = findViewById(R.id.alamatpelanggan);

        statusverifikasi = findViewById(R.id.statusverifikasi);
        mulai = findViewById(R.id.mulai);

        mulai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pilihdepo.getText().toString().length() == 0){
                    pilihdepo.setError("Wajib Di isi");
                } else if(pilihcustomer.getText().toString().length() == 0){
                    pilihcustomer.setError("Wajib Di isi");
                } else if(statusverifikasi.getCheckedRadioButtonId() == -1){
                    new SweetAlertDialog(form_kunjungan.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Silahkan pilih salah satu verifikasi")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    int selectedId = statusverifikasi.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedId);

                    String selectedValue = selectedRadioButton.getText().toString();


                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String start = sdf.format(new Date());

                    if(selectedValue.equals("GALLON")){

                        Intent intent = new Intent(getApplicationContext(), verifikasi_galon.class);
                        intent.putExtra("Jenis", selectedValue);
                        intent.putExtra("id_toko", pilihcustomer.getText().toString());
                        intent.putExtra("start", start);

                        startActivity(intent);
                    } else {

                        if(selectedValue.equals("GALLON")){
                            Intent intent = new Intent(getApplicationContext(), verifikasi_galon.class);
                            intent.putExtra("Jenis", selectedValue);
                            intent.putExtra("id_toko", pilihcustomer.getText().toString());
                            intent.putExtra("start", start);

                            startActivity(intent);
                        } else {
                            if(getIntent().getStringExtra("depo_pelanggan") == null){
                                Intent intent = new Intent(getApplicationContext(), pilih_id_dispenser.class);
                                intent.putExtra("id_toko", pilihcustomer.getText().toString());
                                intent.putExtra("nama_toko", namapelanggan.getText().toString());
                                intent.putExtra("start", start);
                                intent.putExtra("start", start);
                                intent.putExtra("depo", pilihdepo.getText().toString());
                                startActivity(intent);
                            } else {
                                kodedepo = getIntent().getStringExtra("soldToBranch");

                                Intent intent = new Intent(getApplicationContext(), pilih_id_dispenser.class);
                                intent.putExtra("id_toko", pilihcustomer.getText().toString());
                                intent.putExtra("nama_toko", namapelanggan.getText().toString());
                                intent.putExtra("start", start);
                                intent.putExtra("start", start);
                                intent.putExtra("depo", pilihdepo.getText().toString());
                                startActivity(intent);

                            }



                        }
                    }
                }
            }
        });
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        if(getIntent().getStringExtra("depo_pelanggan") == null){
            getDepo();
        } else {
            String depo = getIntent().getStringExtra("depo_pelanggan");
            String result = depo.replaceFirst("DEPO ", "").trim();
            result = capitalizeWords(result);

            pilihdepo.setText(result);
            pilihcustomer.setText(getIntent().getStringExtra("kode_pelanggan"));
            namapelanggan.setText(getIntent().getStringExtra("nama_pelanggan"));
            alamatpelanggan.setText(getIntent().getStringExtra("alamat_pelanggan"));

            pilihdepo.setEnabled(false);
            pilihcustomer.setEnabled(false);

        }





        pilihdepo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                namatoko.clear();
                idtoko.clear();
                alamattoko.clear();
                items.clear();
                kodedepo = iddepo.get(position).toString();

                pilihcustomer.setText("");
                namapelanggan.setText("");
                alamatpelanggan.setText("");
                getToko(iddepo.get(position).toString());
            }
        });

        pilihcustomer.setOnClickListener(v -> {
            if(pilihcustomer.getText().toString().length() != 0){
                pilihcustomer.setText("");
            }
             // Hapus teks
        });



        pilihcustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected item (CustomItem) from the parent (AutoCompleteTextView)
                CustomItem selectedItem = (CustomItem) parent.getItemAtPosition(position);

                // Find the corresponding index in the original list
                int selectedIndex = items.indexOf(selectedItem); // This works if CustomItem overrides equals() and hashCode()

                if (selectedIndex != -1) {
                    // Update the UI components with the selected item data
                    namapelanggan.setText(namatoko.get(selectedIndex));  // Update name of customer
                    alamatpelanggan.setText(alamattoko.get(selectedIndex)); // Update address
                    pilihcustomer.setText(idtoko.get(selectedIndex));  // Update id
                }

                // Reset the selection in AutoCompleteTextView
                pilihcustomer.setSelection(0);

                // Dismiss the keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                // Clear focus from AutoCompleteTextView to prevent showing the keyboard again
                pilihcustomer.clearFocus();
            }
        });

    }

    private void getToko(String depo) {
        pDialog = new SweetAlertDialog(form_kunjungan.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest datacustomer = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_customer?szSoldToBranchId=" + depo +"&szclass=IOD",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {

                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    namatoko.add(jsonObject1.getString("szName"));
                                    idtoko.add(jsonObject1.getString("szId"));
                                    alamattoko.add(jsonObject1.getString("szAddress"));

                                    items.add(new CustomItem(jsonObject1.getString("szId"), jsonObject1.getString("szName")));

                                    CustomAdapter adapter = new CustomAdapter(form_kunjungan.this, items);
                                    pilihcustomer.setAdapter(adapter);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
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
        datacustomer.setRetryPolicy(new DefaultRetryPolicy(
                500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestcustomer = Volley.newRequestQueue(form_kunjungan.this);
        requestcustomer.add(datacustomer);
    }

    private void getDepo() {
        StringRequest rest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Login/index_depo?nik_baru=" + sharedPreferences.getString("nik_baru", null),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("status").equals("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                    namadepo.add(jsonObject1.getString("nama_depo"));
                                    iddepo.add(jsonObject1.getString("kode_depo"));

                                }
                            }
                            pilihdepo.setAdapter(new ArrayAdapter<String>(form_kunjungan.this, android.R.layout.simple_expandable_list_item_1, namadepo));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        };
        rest.setRetryPolicy(new DefaultRetryPolicy(
                20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue requestQueue = Volley.newRequestQueue(form_kunjungan.this);
            requestQueue.getCache().clear();
            requestQueue.add(rest);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(form_kunjungan.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
        startActivity(intent);
        finish();
    }

    public class CustomAdapter extends ArrayAdapter<CustomItem> {
        private List<CustomItem> originalList; // Unfiltered list
        private List<CustomItem> filteredList; // Filtered list for display
        private Filter customFilter;

        public CustomAdapter(Context context, List<CustomItem> items) {
            super(context, 0, items);
            this.originalList = new ArrayList<>(items); // Store original items
            this.filteredList = new ArrayList<>(items); // Initialize filtered list
        }

        @Override
        public int getCount() {
            return filteredList.size(); // Return filtered list size
        }

        @Override
        public CustomItem getItem(int position) {
            return filteredList.get(position); // Return item from filtered list
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            CustomItem item = getItem(position);

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_custom, parent, false);
            }

            TextView title = convertView.findViewById(R.id.title);
            TextView subtitle = convertView.findViewById(R.id.subtitle);

            title.setText(item.getTitle());
            subtitle.setText(item.getSubtitle());

            return convertView;
        }

        @Override
        public Filter getFilter() {
            if (customFilter == null) {
                customFilter = new Filter() {
                    @Override
                    protected FilterResults performFiltering(CharSequence constraint) {
                        FilterResults results = new FilterResults();

                        if (constraint == null || constraint.length() == 0) {
                            // No input, return original list
                            results.values = originalList;
                            results.count = originalList.size();
                        } else {
                            // Perform filtering
                            String query = constraint.toString().toLowerCase();
                            List<CustomItem> filtered = new ArrayList<>();

                            for (CustomItem item : originalList) {
                                if (item.getTitle().toLowerCase().contains(query) ||
                                        item.getSubtitle().toLowerCase().contains(query)) {
                                    filtered.add(item);
                                }
                            }

                            results.values = filtered;
                            results.count = filtered.size();
                        }

                        return results;
                    }

                    @Override
                    protected void publishResults(CharSequence constraint, FilterResults results) {
                        filteredList.clear();

                        if (results.values != null) {
                            filteredList.addAll((List<CustomItem>) results.values);
                        }

                        notifyDataSetChanged(); // Notify that data has changed
                    }
                };
            }

            return customFilter;
        }
    }
    public static String capitalizeWords(String str) {
        String[] words = str.toLowerCase().split(" ");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                capitalized.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }
        return capitalized.toString().trim();
    }





}