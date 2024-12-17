package com.tvip.teknisi_dispenser.menu_form_kunjungan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
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

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.tvip.teknisi_dispenser.R;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class verifikasi_dispenser extends AppCompatActivity {
    ContentValues cv;
    Uri imageUri;

    TextInputLayout seridispenser_textlayout;

    TextView id_dispenser;
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
        setContentView(R.layout.activity_verifikasi_dispenser);
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

        id_dispenser.setText(getIntent().getStringExtra("seridispenser"));

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
                    seridispenser.setText(id_dispenser.getText().toString());
                    seridispenser.setEnabled(false);
                    seridispenser_textlayout.setBoxBackgroundColor(Color.parseColor("#EDEDED"));
                } else {
                    seridispenser.setText("");
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
                    new SweetAlertDialog(verifikasi_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Silahkan pilih salah satu jenis produk gallon")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else if (textuploadseri.getVisibility() == View.VISIBLE){
                    new SweetAlertDialog(verifikasi_dispenser.this, SweetAlertDialog.ERROR_TYPE)
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
                    new SweetAlertDialog(verifikasi_dispenser.this, SweetAlertDialog.ERROR_TYPE)
                            .setContentText("Mohon Upload Foto Depan Dispenser")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                }
                            })
                            .show();
                } else {
                    int selectedIdAir = verifikasi_dispenser.pemakaiangroup.getCheckedRadioButtonId();
                    RadioButton selectedRadioButtonAir = findViewById(selectedIdAir);
                    String selectedValueAir = selectedRadioButtonAir.getText().toString();

                    int selectedIdSeri = verifikasi_dispenser.noseri.getCheckedRadioButtonId();
                    RadioButton selectedRadioButtonSeri = findViewById(selectedIdSeri);
                    String selectedValueSeri = selectedRadioButtonSeri.getText().toString();

                    Intent intent = new Intent(getApplicationContext(), ceklis_dispenser.class);
                    intent.putExtra("air", selectedValueAir);
                    intent.putExtra("seri", selectedValueSeri);
                    startActivity(intent);
                }
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
}