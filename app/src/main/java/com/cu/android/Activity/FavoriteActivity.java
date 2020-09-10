package com.cu.android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.android.Adapter.LinkAdapter;
import com.cu.android.DatabaseHelper.DatabaseHelper;
import com.cu.android.Model.LinkData;
import com.cu.android.R;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<LinkData> linkData;
    ImageView back;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try{
            DatabaseHelper helper=new DatabaseHelper(this);
            linkData=new ArrayList<>();
            Cursor cursor=helper.getLink();
            if(cursor!=null && cursor.getCount()>0){
                while (cursor.moveToNext()) {
                    if(cursor.getString(3).equals("true")){
                        count++;
                        linkData.add(new LinkData(cursor.getString(0) + "", cursor.getString(1) + "", cursor.getString(2)+ "",cursor.getString(3)+"",cursor.getString(4)+""));
                    }
                }
            }
            helper.close();
            TextView count_text=findViewById(R.id.count);
            count_text.setText(count+"");
            recyclerView.setAdapter(new LinkAdapter(linkData,"Favorite",getApplicationContext()));
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}