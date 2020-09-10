package com.cu.android.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.cu.android.DatabaseHelper.DatabaseHelper;
import com.cu.android.Model.LinkData;
import com.cu.android.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayList<LinkData> linkData=new ArrayList<>();

    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        linkReading("basic");
        linkReading("beginner");
        linkReading("intermediate");
        linkReading("library");
        linkReading("project");
        linkReading("design");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        },1000);
    }
    public void linkReading(final String type){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("data").child(type);
        try {
            myRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        linkData= new ArrayList<>();
                        try {
                            count+=snapshot.getChildrenCount();
                            for (DataSnapshot dss : snapshot.getChildren()) {
                                String name=dss.getKey();
                                String link = Objects.requireNonNull(dss.getValue()).toString();
                                DatabaseHelper helper=new DatabaseHelper(getApplicationContext());
                                Cursor cursor=helper.getLink();
                                if(cursor.getCount()<count) {
                                    helper.insertLink(name, link,"false",type);
                                }
                            }
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }

            });
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

}