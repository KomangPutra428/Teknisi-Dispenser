package com.tvip.teknisi_dispenser.splash;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.tvip.teknisi_dispenser.menu_utama.MainActivity;
import com.tvip.teknisi_dispenser.R;
import com.tvip.teknisi_dispenser.menu_login.login;

public class splash extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnected()) {

            sharedPreferences = getSharedPreferences("user_details", MODE_PRIVATE);
            String nik_baru = sharedPreferences.getString("nik_baru", null);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (nik_baru == null) {
                        Intent home = new Intent(splash.this, login.class);
                        startActivity(home);
                    } else {
                        Intent intent = new Intent(splash.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }, 5000);



        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    this);
            alertDialogBuilder.setTitle("Maaf, anda belum terhubung ke internet");
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int id) {
                            finishAffinity();
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }
}