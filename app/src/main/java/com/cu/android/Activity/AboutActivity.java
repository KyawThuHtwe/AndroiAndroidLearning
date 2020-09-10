package com.cu.android.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.palette.graphics.Palette;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.android.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.Objects;

public class AboutActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        CardView google,youtube,firebase,bhiandroid,stacktips,journaldev,javatpoint,dribbble,developer;
        google=findViewById(R.id.google);
        youtube=findViewById(R.id.youtube);
        firebase=findViewById(R.id.firebase);
        bhiandroid=findViewById(R.id.bhiandroid);
        stacktips=findViewById(R.id.stacktips);
        journaldev=findViewById(R.id.journaldev);
        javatpoint=findViewById(R.id.javatpoint);
        dribbble=findViewById(R.id.dribbble);
        developer=findViewById(R.id.developer);

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage("www.google.com");
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage("www.youtube.com");
            }
        });
        firebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage("www.firebase.com");
            }
        });
        bhiandroid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage("bhiandroid.com");
            }
        });
        stacktips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage("www.stacktips.com");
            }
        });
        journaldev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage("www.journaldev.com");
            }
        });
        javatpoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage("www.javatpoint.com");
            }
        });
        dribbble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage("www.dribbble.com");
            }
        });
        developer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPage(" developer.android.com");
            }
        });

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        final CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.collapsingToolBarLayout);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.header);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onGenerated(@Nullable Palette palette) {
                if(palette!=null){
                    collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.color.colorNote));
                }
            }
        });
        TextView email,phone;
        email=findViewById(R.id.email);
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:copypaste673@gmail.com"));
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "Fail.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        phone=findViewById(R.id.phone);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:09750534996"));
                if(ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Please grant the permission to call",Toast.LENGTH_SHORT).show();
                    requestPermission();
                }else {
                    startActivity(intent);
                }
            }
        });
    }

    private void gotoPage(String page) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse("https://"+page));
            startActivity(i);
        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},1);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}