package com.tvip.teknisi_dispenser.menu_edit;

import static com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_galon.galon_pinjam;
import static com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_galon.link;
import static com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_galon.pinjaman;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.detail_kunjungan_galon;
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.footer_detail_kunjungan_dispenser;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.verifikasi_galon;
import com.tvip.teknisi_dispenser.menu_history_kunjungan.history_kunjungan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class edit_verifikasi_galon extends AppCompatActivity {
    RadioGroup pemakaiangroup, galonpinjam;

    LinearLayout pinjamangalon_layout;

    SweetAlertDialog pDialog, success;

    LinearLayout layout_pinjam;
    EditText qtypinjam, qtybaik, qtyrusak, qtynotfound, qtytotal, namapelanggan, catatan;

    ImageView uploadgambar;
    TextView textupload;

    MaterialButton ambil_foto;

    int pinjam, baik, rusak, notfound;

    LinearLayout linear_ttd;
    SignaturePad signature_view;
    MaterialButton hapus_ttd;

    ContentValues cv;
    Uri imageUri;
    Bitmap bitmap;

    MaterialButton batal, selesai;

    MaterialRadioButton aqua_pemakaian, vit_pemakaian;


    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_verifikasi_galon);
        aqua_pemakaian = findViewById(R.id.aqua_pemakaian);
        vit_pemakaian = findViewById(R.id.vit_pemakaian);
        pinjamangalon_layout = findViewById(R.id.pinjamangalon_layout);

        pemakaiangroup = findViewById(R.id.pemakaiangroup);
        galonpinjam = findViewById(R.id.galonpinjam);

        layout_pinjam = findViewById(R.id.layout_pinjam);

        qtypinjam = findViewById(R.id.qtypinjam);
        qtybaik = findViewById(R.id.qtybaik);
        qtyrusak = findViewById(R.id.qtyrusak);
        qtynotfound = findViewById(R.id.qtynotfound);
        qtytotal = findViewById(R.id.qtytotal);

        catatan = findViewById(R.id.catatan);
        namapelanggan = findViewById(R.id.namapelanggan);

        uploadgambar = findViewById(R.id.uploadgambar);
        textupload = findViewById(R.id.textupload);
        ambil_foto = findViewById(R.id.ambil_foto);

        linear_ttd = findViewById(R.id.linear_ttd);
        signature_view = findViewById(R.id.signature_view);
        hapus_ttd = findViewById(R.id.hapus_ttd);

        batal = findViewById(R.id.batal);
        selesai = findViewById(R.id.selesai);

        sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);

        qtypinjam.setText(detail_kunjungan_galon.qtypinjam.getText().toString());
        qtybaik.setText(detail_kunjungan_galon.qtybaik.getText().toString());
        qtyrusak.setText(detail_kunjungan_galon.qtyrusak.getText().toString());
        qtynotfound.setText(detail_kunjungan_galon.qtynotfound.getText().toString());
        catatan.setText(detail_kunjungan_galon.catatan.getText().toString());



        cekGalon();

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pemakaiangroup.getCheckedRadioButtonId() == -1) {
                    new SweetAlertDialog(edit_verifikasi_galon.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Silahkan pilih salah satu jenis produk gallon")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (galonpinjam.getCheckedRadioButtonId() == -1) {
                    new SweetAlertDialog(edit_verifikasi_galon.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Silahkan pilih salah satu tersedianya gallon pinjam")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    int selectedId = galonpinjam.getCheckedRadioButtonId();
                    RadioButton selectedRadioButton = findViewById(selectedId);

                    String selectedValue = selectedRadioButton.getText().toString();

                    if (selectedValue.equals("ADA")) {
                        if (qtypinjam.getText().toString().length() == 0) {
                            qtypinjam.setError("Wajib Di Isi");
                        } else if (qtybaik.getText().toString().length() == 0) {
                            qtybaik.setError("Wajib Di Isi");
                        } else if (qtyrusak.getText().toString().length() == 0) {
                            qtyrusak.setError("Wajib Di Isi");
                        } else if (qtynotfound.getText().toString().length() == 0) {
                            qtynotfound.setError("Wajib Di Isi");
                        } else if (catatan.getText().toString().length() == 0) {
                            catatan.setError("Wajib Di Isi");
                        } else if (bitmap == null) {
                            new SweetAlertDialog(edit_verifikasi_galon.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Mohon untuk upload stok gallon")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else if (signature_view.isEmpty()) {
                            new SweetAlertDialog(edit_verifikasi_galon.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Silahkan Tanda Tangan Terlebih Dahulu")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else if (namapelanggan.getText().toString().length() == 0) {
                            namapelanggan.setError("Wajib Di Isi");
                        } else {
                            postGalon();
                        }
                    } else {
                        if (catatan.getText().toString().length() == 0) {
                            catatan.setError("Wajib Di Isi");
                        } else if (bitmap == null) {
                            new SweetAlertDialog(edit_verifikasi_galon.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Mohon untuk upload stok gallon")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else if (signature_view.isEmpty()) {
                            new SweetAlertDialog(edit_verifikasi_galon.this, SweetAlertDialog.ERROR_TYPE)
                                    .setContentText("Silahkan Tanda Tangan Terlebih Dahulu")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else if (namapelanggan.getText().toString().length() == 0) {
                            namapelanggan.setError("Wajib Di Isi");
                        } else {
                            postGalon();
                        }
                    }
                }
            }
        });
        galonpinjam.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = galonpinjam.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);

                String selectedValue = selectedRadioButton.getText().toString();

                if (selectedValue.equals("ADA")) {
                    qtypinjam.setText(detail_kunjungan_galon.qtypinjam.getText().toString());
                    qtybaik.setText(detail_kunjungan_galon.qtybaik.getText().toString());
                    qtyrusak.setText(detail_kunjungan_galon.qtyrusak.getText().toString());
                    qtynotfound.setText(detail_kunjungan_galon.qtynotfound.getText().toString());
                    pinjamangalon_layout.setVisibility(View.VISIBLE);
                    if (qtypinjam.getText().toString().length() == 0) {
                        pinjam = 0;
                    } else {
                        pinjam = Integer.parseInt(qtypinjam.getText().toString());
                    }

                    if (qtybaik.getText().toString().length() == 0) {
                        baik = 0;
                    } else {
                        baik = Integer.parseInt(qtybaik.getText().toString());
                    }

                    if (qtyrusak.getText().toString().length() == 0) {
                        rusak = 0;
                    } else {
                        rusak = Integer.parseInt(qtyrusak.getText().toString());
                    }

                    if (qtynotfound.getText().toString().length() == 0) {
                        notfound = 0;
                    } else {
                        notfound = Integer.parseInt(qtynotfound.getText().toString());
                    }

                    qtytotal.setText(String.valueOf(baik + rusak + notfound));
                } else {
                    pinjamangalon_layout.setVisibility(View.GONE);
                    pinjam = 0;
                    baik = 0;
                    rusak = 0;
                    notfound = 0;

                    qtybaik.setText("0");
                    qtyrusak.setText("0");
                    qtynotfound.setText("0");
                    qtypinjam.setText("0");

                    qtytotal.setText(String.valueOf(baik + rusak + notfound));
                }
            }
        });

        qtypinjam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check and limit the value of qtypinjam
                if (!s.toString().isEmpty()) {
                    int value = Integer.parseInt(s.toString());
                    if (value > 10000) {
                        qtypinjam.setText(String.valueOf(10000));
                        qtypinjam.setSelection(qtypinjam.getText().length()); // Move the cursor to the end
                    }
                }

                // Handle calculations
                if (qtypinjam.getText().toString().length() == 0) {
                    pinjam = 0;
                } else {
                    pinjam = Integer.parseInt(qtypinjam.getText().toString());
                }

                if (qtybaik.getText().toString().length() == 0) {
                    baik = 0;
                } else {
                    baik = Integer.parseInt(qtybaik.getText().toString());
                }

                if (qtyrusak.getText().toString().length() == 0) {
                    rusak = 0;
                } else {
                    rusak = Integer.parseInt(qtyrusak.getText().toString());
                }

                if (qtynotfound.getText().toString().length() == 0) {
                    notfound = 0;
                } else {
                    notfound = Integer.parseInt(qtynotfound.getText().toString());
                }

                qtytotal.setText(String.valueOf(baik + rusak + notfound));
            }
        });

        qtybaik.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    int value = Integer.parseInt(s.toString());
                    if (value > 10000) {
                        qtybaik.setText(String.valueOf(10000));
                        qtybaik.setSelection(qtybaik.getText().length()); // Move the cursor to the end
                    }
                }

                if (qtypinjam.getText().toString().length() == 0) {
                    pinjam = 0;
                } else {
                    pinjam = Integer.parseInt(qtypinjam.getText().toString());
                }

                if (qtybaik.getText().toString().length() == 0) {
                    baik = 0;
                } else {
                    baik = Integer.parseInt(qtybaik.getText().toString());
                }

                if (qtyrusak.getText().toString().length() == 0) {
                    rusak = 0;
                } else {
                    rusak = Integer.parseInt(qtyrusak.getText().toString());
                }

                if (qtynotfound.getText().toString().length() == 0) {
                    notfound = 0;
                } else {
                    notfound = Integer.parseInt(qtynotfound.getText().toString());
                }

                qtytotal.setText(String.valueOf(baik + rusak + notfound));
            }
        });

        qtynotfound.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    int value = Integer.parseInt(s.toString());
                    if (value > 10000) {
                        qtynotfound.setText(String.valueOf(10000));
                        qtynotfound.setSelection(qtynotfound.getText().length()); // Move the cursor to the end
                    }
                }

                if (qtypinjam.getText().toString().length() == 0) {
                    pinjam = 0;
                } else {
                    pinjam = Integer.parseInt(qtypinjam.getText().toString());
                }

                if (qtybaik.getText().toString().length() == 0) {
                    baik = 0;
                } else {
                    baik = Integer.parseInt(qtybaik.getText().toString());
                }

                if (qtyrusak.getText().toString().length() == 0) {
                    rusak = 0;
                } else {
                    rusak = Integer.parseInt(qtyrusak.getText().toString());
                }

                if (qtynotfound.getText().toString().length() == 0) {
                    notfound = 0;
                } else {
                    notfound = Integer.parseInt(qtynotfound.getText().toString());
                }

                qtytotal.setText(String.valueOf(baik + rusak + notfound));
            }
        });

        qtyrusak.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    int value = Integer.parseInt(s.toString());
                    if (value > 10000) {
                        qtyrusak.setText(String.valueOf(10000));
                        qtyrusak.setSelection(qtyrusak.getText().length()); // Move the cursor to the end
                    }
                }

                if (qtypinjam.getText().toString().length() == 0) {
                    pinjam = 0;
                } else {
                    pinjam = Integer.parseInt(qtypinjam.getText().toString());
                }

                if (qtybaik.getText().toString().length() == 0) {
                    baik = 0;
                } else {
                    baik = Integer.parseInt(qtybaik.getText().toString());
                }

                if (qtyrusak.getText().toString().length() == 0) {
                    rusak = 0;
                } else {
                    rusak = Integer.parseInt(qtyrusak.getText().toString());
                }

                if (qtynotfound.getText().toString().length() == 0) {
                    notfound = 0;
                } else {
                    notfound = Integer.parseInt(qtynotfound.getText().toString());
                }

                qtytotal.setText(String.valueOf(baik + rusak + notfound));
            }
        });

        ambil_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv = new ContentValues();
                cv.put(MediaStore.Images.Media.TITLE, "My Picture");
                cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);
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

    private void postGalon() {

        pDialog = new SweetAlertDialog(edit_verifikasi_galon.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Harap Menunggu");
        pDialog.setCancelable(false);
        pDialog.show();

        SimpleDateFormat formatTanggal = new SimpleDateFormat("yyyyMMddHHmmss");
        String namaTanggal = formatTanggal.format(new Date());
        StringRequest stringRequest2 = new StringRequest(Request.Method.PUT, "https://apisec.tvip.co.id/rest_server_dispenser_dummy/Stock/index_galon",
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        uploadGambarGalon(namaTanggal);
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

                int selectedIdProduk = pemakaiangroup.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonProduk = findViewById(selectedIdProduk);
                String selectedValueProduk = selectedRadioButtonProduk.getText().toString();

                int selectedIdPinjam = galonpinjam.getCheckedRadioButtonId();
                RadioButton selectedRadioButtonPinjam = findViewById(selectedIdPinjam);
                String selectedValuePinjam = selectedRadioButtonPinjam.getText().toString();

                params.put("id_header", getIntent().getStringExtra("id"));
                params.put("pemakaian_air", selectedValueProduk);
                params.put("galon_pinjam", selectedValuePinjam);

                if (selectedValuePinjam.equals("ADA")) {
                    params.put("qty_pinjam", qtypinjam.getText().toString());
                } else {
                    params.put("qty_pinjam", "0");
                }

                params.put("qty_baik", String.valueOf(baik));
                params.put("qty_rusak", String.valueOf(rusak));
                params.put("qty_total", qtytotal.getText().toString());
                params.put("catatan", catatan.getText().toString());

                params.put("foto_galon", getIntent().getStringExtra("id_toko") + "_galon_" + namaTanggal + ".jpeg");
                params.put("ttd", getIntent().getStringExtra("id_toko") + "_ttd_" + namaTanggal + ".jpeg");

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
        RequestQueue requestQueue33 = Volley.newRequestQueue(edit_verifikasi_galon.this);
        requestQueue33.add(stringRequest2);

    }

    private void uploadGambarGalon(String namaTanggal) {
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://apisec.tvip.co.id/mobile_eis_2/upload_dispenser.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        uploadttd(namaTanggal);
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

                String gambar = ImageToString(bitmap);

                params.put("nama_foto", getIntent().getStringExtra("id_toko") + "_galon_" + namaTanggal);
                params.put("foto", gambar);
                params.put("folder", "galon");


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


        RequestQueue requestQueue5 = Volley.newRequestQueue(edit_verifikasi_galon.this);
        requestQueue5.add(stringRequest2);

    }

    private void uploadttd(String namaTanggal) {

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, "https://apisec.tvip.co.id/mobile_eis_2/upload_dispenser.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        success = new SweetAlertDialog(edit_verifikasi_galon.this, SweetAlertDialog.SUCCESS_TYPE);
                        success.setContentText("Data Berhasil Disimpan");
                        success.setCancelable(false);
                        success.setConfirmText("OK");
                        success.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        Intent intent = new Intent(getApplicationContext(), history_kunjungan.class);
                                        startActivity(intent);
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

                params.put("nama_foto", getIntent().getStringExtra("id_toko") + "_ttd_" + namaTanggal);
                params.put("foto", gambar);
                params.put("folder", "ttd");


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


        RequestQueue requestQueue5 = Volley.newRequestQueue(edit_verifikasi_galon.this);
        requestQueue5.add(stringRequest2);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    int width = 720;
                    int height = 720;
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

                    Matrix matrix = new Matrix();

                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    bitmap = rotatedBitmap;

                    uploadgambar.setImageBitmap(rotatedBitmap);
                    textupload.setVisibility(View.GONE);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String ImageToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void cekGalon() {


        LazyHeaders auth = new LazyHeaders.Builder() // can be cached in a field and reused
                .addHeader("Authorization", new BasicAuthorization("admin", "Databa53"))
                .build();

        new Thread(() -> {
            try {
                // Fetch the bitmap synchronously
                Bitmap bitmap = Glide.with(edit_verifikasi_galon.this)
                        .asBitmap()
                        .load(new GlideUrl(detail_kunjungan_galon.ttd_link, auth))
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .submit()
                        .get();

                // Use the bitmap on the main thread
                runOnUiThread(() -> {
                    // Do something with the bitmap, e.g., display it in an ImageView
                    signature_view.setSignatureBitmap(bitmap);
                    linear_ttd.setVisibility(View.GONE);
                    namapelanggan.setText(detail_kunjungan_galon.nama);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Glide.with(edit_verifikasi_galon.this)
                .asBitmap() // Ensure Glide fetches the image as a Bitmap
                .load(new GlideUrl(link, auth))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Bitmap is ready here
                        bitmap = resource;

                        // Hide the text view since the bitmap is loaded
                        textupload.setVisibility(View.GONE);

                        // Optionally, set the bitmap to the ImageView
                        uploadgambar.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleanup if necessary
                    }
                });

        if(detail_kunjungan_galon.pemakaian.getText().toString().equals("AQUA")){
            vit_pemakaian.setEnabled(false);
            aqua_pemakaian.setChecked(true);
        } else {
            aqua_pemakaian.setEnabled(false);
            vit_pemakaian.setChecked(true);
        }

        if(detail_kunjungan_galon.galon_pinjam.getText().toString().equals("ADA")){
            for (int i = 0; i < galonpinjam.getChildCount(); i++) {
                View child = galonpinjam.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("ADA")) {
                        radioButton.setChecked(true);
                        pinjamangalon_layout.setVisibility(View.VISIBLE);
                        if (qtypinjam.getText().toString().length() == 0) {
                            pinjam = 0;
                        } else {
                            pinjam = Integer.parseInt(qtypinjam.getText().toString());
                        }

                        if (qtybaik.getText().toString().length() == 0) {
                            baik = 0;
                        } else {
                            baik = Integer.parseInt(qtybaik.getText().toString());
                        }

                        if (qtyrusak.getText().toString().length() == 0) {
                            rusak = 0;
                        } else {
                            rusak = Integer.parseInt(qtyrusak.getText().toString());
                        }

                        if (qtynotfound.getText().toString().length() == 0) {
                            notfound = 0;
                        } else {
                            notfound = Integer.parseInt(qtynotfound.getText().toString());
                        }

                        qtytotal.setText(String.valueOf(baik + rusak + notfound));
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < galonpinjam.getChildCount(); i++) {
                View child = galonpinjam.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("TIDAK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
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
}