package com.tvip.teknisi_dispenser.menu_detail_kunjungan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import com.tvip.teknisi_dispenser.R;

import java.io.File;

public class PdfReviewGalon extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_review_galon);

        String pdfFilePath = getIntent().getStringExtra("pdfFilePath");

        File pdfFile = new File(pdfFilePath);
        if (pdfFile.exists()) {
            Uri pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", pdfFile);

            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(pdfUri, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(this, "No PDF viewer app found.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "PDF file not found.", Toast.LENGTH_SHORT).show();
        }
    }
}