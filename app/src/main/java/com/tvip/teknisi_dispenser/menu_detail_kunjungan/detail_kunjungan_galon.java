package com.tvip.teknisi_dispenser.menu_detail_kunjungan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.button.MaterialButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.pnikosis.materialishprogress.BuildConfig;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_edit.edit_verifikasi_galon;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.form_kunjungan;
import com.tvip.teknisi_dispenser.menu_utama.MainActivity;
import com.tvip.teknisi_dispenser.pojos.header_pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HttpsURLConnection;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class detail_kunjungan_galon extends AppCompatActivity {
    SweetAlertDialog pDialog;
    public static String link;
    String link_fisik;
    public static String pinjaman, nama;
    TextView tanggal_kunjungan, id_toko, nama_toko,
            alamat_pelanggan, segment, depo;

    RelativeLayout detail_layout;

    public static TextView pemakaian, galon_pinjam, nama_pic;
    public static TextView qtypinjam, qtybaik, qtyrusak, qtytotal, qtynotfound, catatan, verifikasi;
    SharedPreferences sharedPreferences;
    private static final int REQUEST_CODE_STORAGE_PERMISSION = 100;

    MaterialButton kembali,print,transaksi;
    Button foto_galon, foto_ttd;

    public static String ttd_link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_kunjungan_galon);
        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
        detail_layout = findViewById(R.id.detail_layout);

        foto_galon = findViewById(R.id.foto_galon);
        foto_ttd = findViewById(R.id.foto_ttd);

        tanggal_kunjungan = findViewById(R.id.tanggal_kunjungan);
        id_toko = findViewById(R.id.id_toko);
        nama_toko = findViewById(R.id.nama_toko);
        alamat_pelanggan = findViewById(R.id.alamat_pelanggan);
        segment = findViewById(R.id.segment);
        depo = findViewById(R.id.depo);

        pemakaian = findViewById(R.id.pemakaian);
        galon_pinjam = findViewById(R.id.galon_pinjam);
        nama_pic = findViewById(R.id.nama_pic);

        qtypinjam = findViewById(R.id.qtypinjam);
        qtybaik = findViewById(R.id.qtybaik);
        qtyrusak = findViewById(R.id.qtyrusak);
        qtytotal = findViewById(R.id.qtytotal);
        qtynotfound = findViewById(R.id.qtynotfound);
        catatan = findViewById(R.id.catatan);
        verifikasi = findViewById(R.id.verifikasi);

        kembali = findViewById(R.id.kembali);
        print = findViewById(R.id.print);
        transaksi = findViewById(R.id.transaksi);


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new SweetAlertDialog(detail_kunjungan_galon.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Harap Menunggu");
                pDialog.setCancelable(false);
                pDialog.show();
                checkStoragePermission();
            }
        });

        transaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detail_kunjungan_galon.this, form_kunjungan.class);
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

        tanggal_kunjungan.setText(convertFormat(getIntent().getStringExtra("tanggal_kunjungan")));
        id_toko.setText(getIntent().getStringExtra("id_toko"));
        nama_toko.setText(getIntent().getStringExtra("nama"));
        alamat_pelanggan.setText(getIntent().getStringExtra("alamat"));
        segment.setText(getIntent().getStringExtra("segment"));
        depo.setText(getIntent().getStringExtra("depo"));

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yy", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        if(convertFormat(getIntent().getStringExtra("tanggal_kunjungan")).equals(currentDateandTime)){
            kembali.setText("Edit");
            kembali.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), edit_verifikasi_galon.class);
                    intent.putExtra("id", getIntent().getStringExtra("id"));
                    intent.putExtra("id_toko", getIntent().getStringExtra("id_toko"));
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
        
        getDataFooterGalon();


    }

    private void getDataFooterGalon() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_footer?id="+getIntent().getStringExtra("id"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            final JSONArray movieArray = obj.getJSONArray("data");
                            for (int i = 0; i < movieArray.length(); i++) {

                                final JSONObject movieObject = movieArray.getJSONObject(i);

                                link_fisik = "https://apisec.tvip.co.id/gambar_dispenser/galon/" + movieObject.getString("foto_galon");
                                link = "https://apisec.tvip.co.id/gambar_dispenser/galon/" + movieObject.getString("foto_galon");

                                if(movieObject.getString("nama_pelanggan").equals("null")){
                                    nama = "Pelanggan";
                                } else {
                                    nama = movieObject.getString("nama_pelanggan");
                                }

                                foto_galon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImageView fullScreenImage = new ImageView(detail_kunjungan_galon.this);
                                        fullScreenImage.setBackgroundColor(Color.BLACK); // Set background color

// Set layout parameters for the full-screen image
                                        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                        );

// Create a new ImageButton for the close button
                                        ImageButton closeButton = new ImageButton(detail_kunjungan_galon.this);
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
                                        detail_layout.addView(fullScreenImage, imageParams);

// Add the close button to the main layout after the ImageView to ensure it appears on top
                                        detail_layout.addView(closeButton, closeParams);

// Debugging: Log to verify if views are being added
                                        Log.d("ImageView", "Full-screen image added to layout");
                                        Log.d("ImageButton", "Close button added to layout");

// Set click listener for the full-screen image to remove it when clicked
// Set click listener for the close button to remove the full-screen image and itself when clicked
                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Remove the image and the close button from the layout
                                                detail_layout.removeView(fullScreenImage);
                                                detail_layout.removeView(closeButton);
                                                Log.d("ImageView", "Full-screen image removed from layout");
                                                Log.d("ImageButton", "Close button removed from layout");
                                            }
                                        });
                                        // Get the image URL from the intent
                                        String imageUrl = null; // Key must match the sender's intent
                                        try {
                                            imageUrl = "https://apisec.tvip.co.id/gambar_dispenser/galon/" + movieObject.getString("foto_galon");

                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }

                                        LazyHeaders auth = new LazyHeaders.Builder() // can be cached in a field and reused
                                                .addHeader("Authorization", new BasicAuthorization("admin", "Databa53"))
                                                .build();

                                        Glide.with(detail_kunjungan_galon.this)
                                                .load(new GlideUrl(imageUrl, auth))
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true)
                                                .into(fullScreenImage);
                                    }

                                });
                                ttd_link = "https://apisec.tvip.co.id/gambar_dispenser/ttd/" + movieObject.getString("ttd");
                                foto_ttd.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ImageView fullScreenImage = new ImageView(detail_kunjungan_galon.this);
                                        fullScreenImage.setBackgroundColor(Color.BLACK); // Set background color

// Set layout parameters for the full-screen image
                                        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.MATCH_PARENT
                                        );

// Create a new ImageButton for the close button
                                        ImageButton closeButton = new ImageButton(detail_kunjungan_galon.this);
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
                                        detail_layout.addView(fullScreenImage, imageParams);

// Add the close button to the main layout after the ImageView to ensure it appears on top
                                        detail_layout.addView(closeButton, closeParams);

// Debugging: Log to verify if views are being added
                                        Log.d("ImageView", "Full-screen image added to layout");
                                        Log.d("ImageButton", "Close button added to layout");

// Set click listener for the full-screen image to remove it when clicked
// Set click listener for the close button to remove the full-screen image and itself when clicked
                                        closeButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Remove the image and the close button from the layout
                                                detail_layout.removeView(fullScreenImage);
                                                detail_layout.removeView(closeButton);
                                                Log.d("ImageView", "Full-screen image removed from layout");
                                                Log.d("ImageButton", "Close button removed from layout");
                                            }
                                        });
                                        // Get the image URL from the intent
                                        String imageUrl = null; // Key must match the sender's intent
                                        try {
                                            imageUrl = "https://apisec.tvip.co.id/gambar_dispenser/ttd/" + movieObject.getString("ttd");

                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }

                                        LazyHeaders auth = new LazyHeaders.Builder() // can be cached in a field and reused
                                                .addHeader("Authorization", new BasicAuthorization("admin", "Databa53"))
                                                .build();

                                        Glide.with(detail_kunjungan_galon.this)
                                                .load(new GlideUrl(imageUrl, auth))
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .skipMemoryCache(true)
                                                .into(fullScreenImage);
                                    }
                                });

                                int totalgalon = Integer.parseInt(movieObject.getString("qty_pinjam")) - (Integer.parseInt(movieObject.getString("qty_baik")) + Integer.parseInt(movieObject.getString("qty_rusak")));

                                pemakaian.setText(movieObject.getString("pemakaian_air"));
                                galon_pinjam.setText(movieObject.getString("galon_pinjam"));
                                nama_pic.setText(sharedPreferences.getString("nama", null));

                                qtypinjam.setText(movieObject.getString("qty_pinjam"));
                                qtybaik.setText(movieObject.getString("qty_baik"));
                                qtyrusak.setText(movieObject.getString("qty_rusak"));
                                qtytotal.setText(movieObject.getString("qty_total"));
                                catatan.setText(movieObject.getString("catatan"));

                                qtynotfound.setText(String.valueOf(totalgalon));

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                generatePdf();
            } else {
                // Permission denied
                generatePdf();
            }
        }
    }

    private void generatePdf() {
        new GeneratePdfTask().execute(link_fisik);
    }




    private void addCellToTable(PdfPTable table, String key, String value) {
        table.addCell(new Phrase(key + "", FontFactory.getFont(FontFactory.HELVETICA, 12)));
        table.addCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA, 12)));
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
    private void openLocalPdf(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(this, "File tidak ditemukan", Toast.LENGTH_SHORT).show();
            return;
        }

        Uri uri = FileProvider.getUriForFile(this,
                "com.tvip.teknisi_dispenser",
                file);


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant permission to read the URI

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Tidak ada aplikasi untuk membuka PDF", Toast.LENGTH_SHORT).show();
        }
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

    private class GeneratePdfTask extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... imageUrls) {
            Document document = new Document();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());

            File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String filePath = new File(downloadDir, getIntent().getStringExtra("id_toko") + "_" + currentDateandTime + "BeritaAcaraGalon.pdf").getAbsolutePath();

            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                // Menambahkan Header
                document.add(new Paragraph("PT TIRTA VARIA INTIPRATAMA", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
                document.add(new Paragraph("Jl. Kedoya Raya No. 1 - Jakarta Barat", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph("Telp. 5835-3885, 5835-3886, 5835-3887", FontFactory.getFont(FontFactory.HELVETICA, 12)));
                document.add(new Paragraph(" ")); // Tambahkan jarak

                // Menambahkan Judul
                document.add(new Paragraph("BERITA ACARA VERIFIKASI GALLON", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));

                // Membuat tabel informasi utama
                PdfPTable infoTable = new PdfPTable(2);
                infoTable.setWidthPercentage(100);
                infoTable.setSpacingBefore(10f);
                infoTable.setSpacingAfter(10f);
                infoTable.getDefaultCell().setBorder(0); // Hapus border

                // Baris-baris informasi
                addCellToTable(infoTable, "Tanggal Kunjungan", convertFormat(getIntent().getStringExtra("tanggal_kunjungan")));
                addCellToTable(infoTable, "Kode Pelanggan", getIntent().getStringExtra("id_toko"));
                addCellToTable(infoTable, "Nama Pelanggan", getIntent().getStringExtra("nama"));
                addCellToTable(infoTable, "Alamat Pelanggan", getIntent().getStringExtra("alamat"));
                addCellToTable(infoTable, "Segmen Pelanggan", "IOD");
                addCellToTable(infoTable, "Depo Coverage", getIntent().getStringExtra("depo"));
                addCellToTable(infoTable, "Status Verifikasi", "GALLON");
                addCellToTable(infoTable, "Pemakaian Air", pemakaian.getText().toString());
                addCellToTable(infoTable, "Nama PIC", nama_pic.getText().toString());

                // Tambahkan tabel ke dokumen
                document.add(infoTable);

                // Menambahkan Sub-Judul
                document.add(new Paragraph("Kondisi Gallon", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));

                // Tabel kondisi galon
                PdfPTable galonTable = new PdfPTable(2);
                galonTable.setWidthPercentage(100);
                galonTable.setSpacingBefore(10f);
                galonTable.setSpacingAfter(10f);
                galonTable.getDefaultCell().setBorder(0);

                addCellToTable(galonTable, "Jumlah Gallon Pinjam", qtypinjam.getText().toString());
                addCellToTable(galonTable, "Kondisi Fisik Gallon Baik", qtybaik.getText().toString());
                addCellToTable(galonTable, "Kondisi Fisik Gallon Rusak", qtyrusak.getText().toString());
                addCellToTable(galonTable, "Gallon Tidak Ditemukan", qtynotfound.getText().toString());

                addCellToTable(galonTable, "Jumlah Fisik Gallon", qtytotal.getText().toString());

                // Tambahkan tabel galon ke dokumen
                document.add(galonTable);
                document.add(new Paragraph(" ")); // Tambahkan jarak

                PdfPTable gambar = new PdfPTable(2);
                gambar.setWidthPercentage(100);
                gambar.setSpacingBefore(10f);
                gambar.setSpacingAfter(10f);
                gambar.getDefaultCell().setBorder(0);

                final int totalImages = 1; // Number of images to fetch
                AtomicInteger processedImages = new AtomicInteger(0);

                addCellToTableImage(gambar, "Foto Stok Gallon", link_fisik, processedImages, totalImages);
                // addCellToTableImage(gambar, "Foto Tampak Depan Dispenser", link_depan, processedImages, totalImages);

                // Wait until all images are processed
                while (processedImages.get() < totalImages) {
                    Thread.sleep(100);
                }

                document.add(gambar);

                // Menambahkan Catatan
                document.add(new Paragraph("Catatan: " + catatan.getText().toString()));

                // Footer
                document.add(new Paragraph("*Jika ditemukan ketidaksesuaian baik secara kualitas dan kuantitas gallon, maka pelanggan harus membayar sesuai jumlah ketidaksesuaian (rusak/kurang).", FontFactory.getFont(FontFactory.HELVETICA, 10)));
                document.add(new Paragraph(" "));


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

                addCellToTableImage2(gambar3, " ", ttd_link, processedImages3, totalImages3);

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

                addCellToTable(infoTtd2, nama, "STO/TEKNISI/PIC VERIFIKATOR");
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
                Log.e("PDFCreationError", "Failed to create PDF: " + e.getMessage().toString(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
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
            Log.e("ImageFetchException", "Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            Log.e("ImageFetchException", "IO Exception: " + e.getMessage());
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
                    image.scaleToFit(90, 90); // Adjust the image size as needed
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

    private void addSpacerCell(PdfPTable table) {
        // Create an empty cell to add as a spacer
        PdfPCell spacerCell = new PdfPCell();
        spacerCell.setColspan(2);  // Span across both columns
        spacerCell.setFixedHeight(20f);  // Height of the spacer (adjust as needed)
        spacerCell.setBorder(Rectangle.NO_BORDER);  // No border for the spacer cell
        table.addCell(spacerCell);  // Add the spacer cell to the table
    }
}