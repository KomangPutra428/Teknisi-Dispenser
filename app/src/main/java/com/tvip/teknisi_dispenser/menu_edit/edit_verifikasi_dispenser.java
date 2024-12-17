package com.tvip.teknisi_dispenser.menu_edit;

import static com.tvip.teknisi_dispenser.menu_detail_kunjungan.footer_detail_kunjungan_dispenser.realdispenser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaderFactory;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_detail_kunjungan.footer_detail_kunjungan_dispenser;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.ceklis_dispenser;
import com.tvip.teknisi_dispenser.menu_form_kunjungan.verifikasi_dispenser;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class edit_verifikasi_dispenser extends AppCompatActivity {
    ContentValues cv;
    Uri imageUri;

    TextInputLayout seridispenser_textlayout;

    static TextView id_dispenser;
    static RadioGroup pemakaiangroup;

    ImageView uploadgambarseri;
    TextView textuploadseri;
    MaterialButton ambil_fotoseri;
    static Bitmap bitmapseri;

    static RadioGroup noseri;
    static EditText seridispenser, lokasi_dispenser;

    ImageView uploadgambardepan;
    TextView textuploaddepan;
    MaterialButton ambil_fotodepan;
    static Bitmap bitmapdepan;

    MaterialButton batal, lanjut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_verifikasi_dispenser);
        id_dispenser = findViewById(R.id.id_dispenser);

        pemakaiangroup = findViewById(R.id.pemakaiangroup);
        lokasi_dispenser = findViewById(R.id.lokasi_dispenser);
        uploadgambarseri = findViewById(R.id.uploadgambarseri);
        textuploadseri = findViewById(R.id.textuploadseri);
        ambil_fotoseri = findViewById(R.id.ambil_fotoseri);

        noseri = findViewById(R.id.noseri);
        seridispenser = findViewById(R.id.seridispenser);
        seridispenser_textlayout = findViewById(R.id.seridispenser_textlayout);

        uploadgambardepan = findViewById(R.id.uploadgambardepan);
        textuploaddepan = findViewById(R.id.textuploaddepan);
        ambil_fotodepan = findViewById(R.id.ambil_fotodepan);

        batal = findViewById(R.id.batal);
        lanjut = findViewById(R.id.lanjut);

        seridispenser.setEnabled(false);

        id_dispenser.setText(getIntent().getStringExtra("validasi_seri"));

        ambil_fotoseri.setOnClickListener(new View.OnClickListener() {
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

        ambil_fotodepan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cv = new ContentValues();
                cv.put(MediaStore.Images.Media.TITLE, "My Picture");
                cv.put(MediaStore.Images.Media.DESCRIPTION, "From Camera");
                imageUri = getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 2);
            }
        });

        noseri.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = noseri.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = findViewById(selectedId);

                String selectedValue = selectedRadioButton.getText().toString();

                if(selectedValue.equals("YA")){
                    seridispenser.setText(realdispenser);
                    seridispenser.setEnabled(false);
                    seridispenser_textlayout.setBoxBackgroundColor(Color.parseColor("#EDEDED"));
                } else {
                    if(getIntent().getStringExtra("validasi_seri").equals(realdispenser)){
                        seridispenser.setText("");
                    } else {
                        seridispenser.setText(getIntent().getStringExtra("validasi_seri"));
                    }

                    seridispenser.setEnabled(true);
                    seridispenser_textlayout.setBoxBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pemakaiangroup.getCheckedRadioButtonId() == -1){
                    new SweetAlertDialog(edit_verifikasi_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Silahkan pilih salah satu jenis produk gallon")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (textuploadseri.getVisibility() == View.VISIBLE){
                    new SweetAlertDialog(edit_verifikasi_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Mohon Upload Foto Untuk Nomor Seri Dispenser")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (lokasi_dispenser.getText().toString().length() == 0){
                    lokasi_dispenser.setError("Wajib Di Isi");
                } else if (seridispenser.getText().toString().length() == 0){
                    seridispenser.setError("Wajib Di Isi");
                } else if (textuploaddepan.getVisibility() == View.VISIBLE){
                    new SweetAlertDialog(edit_verifikasi_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Mohon Upload Foto Depan Dispenser")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    int selectedIdAir = edit_verifikasi_dispenser.pemakaiangroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButtonAir = findViewById(selectedIdAir);
                    String selectedValueAir = selectedRadioButtonAir.getText().toString();

                    int selectedIdSeri = edit_verifikasi_dispenser.noseri.getCheckedRadioButtonId();
                    RadioButton selectedRadioButtonSeri = findViewById(selectedIdSeri);
                    String selectedValueSeri = selectedRadioButtonSeri.getText().toString();

                    Intent intent = new Intent(getApplicationContext(), edit_ceklis_dispenser.class);
                    intent.putExtra("air", selectedValueAir);
                    intent.putExtra("seri", selectedValueSeri);
                    startActivity(intent);
                }
            }
        });
        
        cekDone();
    }

    private void cekDone() {
        // validasi air//
        if (footer_detail_kunjungan_dispenser.air.getText().toString().equals("AQUA")) {
            for (int i = 0; i < pemakaiangroup.getChildCount(); i++) {
                View child = pemakaiangroup.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("AQUA")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < pemakaiangroup.getChildCount(); i++) {
                View child = pemakaiangroup.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("VIT")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }
        // =========== //

        // validasi seri //
        if(getIntent().getStringExtra("validasi").equals("YA")){
            for (int i = 0; i < noseri.getChildCount(); i++) {
                View child = noseri.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("YA")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        } else {
            for (int i = 0; i < noseri.getChildCount(); i++) {
                View child = noseri.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    if (radioButton.getText().toString().equals("TIDAK")) {
                        radioButton.setChecked(true);
                        break;
                    }
                }
            }
        }
        // ============= //

        lokasi_dispenser.setText(footer_detail_kunjungan_dispenser.lokasi.getText().toString());

        LazyHeaders auth = new LazyHeaders.Builder() // can be cached in a field and reused
                .addHeader("Authorization", new BasicAuthorization("admin", "Databa53"))
                .build();

        Glide.with(edit_verifikasi_dispenser.this)
                .asBitmap() // Ensure Glide fetches the image as a Bitmap
                .load(new GlideUrl(footer_detail_kunjungan_dispenser.link_seri, auth))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Bitmap is ready here
                        bitmapseri = resource;

                        // Hide the text view since the bitmap is loaded
                        textuploadseri.setVisibility(View.GONE);

                        // Optionally, set the bitmap to the ImageView
                        uploadgambarseri.setImageBitmap(bitmapseri);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleanup if necessary
                    }
                });

        Glide.with(edit_verifikasi_dispenser.this)
                .asBitmap() // Ensure Glide fetches the image as a Bitmap
                .load(new GlideUrl(footer_detail_kunjungan_dispenser.link_depan, auth))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // Bitmap is ready here
                        bitmapdepan = resource;

                        // Hide the text view since the bitmap is loaded
                        textuploaddepan.setVisibility(View.GONE);

                        // Optionally, set the bitmap to the ImageView
                        uploadgambardepan.setImageBitmap(bitmapdepan);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Handle cleanup if necessary
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1){
            if (resultCode == Activity.RESULT_OK) {
                try {
                    bitmapseri = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    int width=720;
                    int height=720;
                    bitmapseri = Bitmap.createScaledBitmap(bitmapseri, width, height, true);

                    Matrix matrix = new Matrix();

                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapseri, width, height, true);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    bitmapseri = rotatedBitmap;

                    uploadgambarseri.setImageBitmap(rotatedBitmap);
                    textuploadseri.setVisibility(View.GONE);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 2){
            if (resultCode == Activity.RESULT_OK) {
                try {
                    bitmapdepan = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    int width=720;
                    int height=720;
                    bitmapdepan = Bitmap.createScaledBitmap(bitmapdepan, width, height, true);

                    Matrix matrix = new Matrix();

                    matrix.postRotate(90);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmapdepan, width, height, true);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);
                    bitmapdepan = rotatedBitmap;

                    uploadgambardepan.setImageBitmap(rotatedBitmap);
                    textuploaddepan.setVisibility(View.GONE);


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