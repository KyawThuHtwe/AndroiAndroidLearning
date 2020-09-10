package com.cu.android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.android.Adapter.LinkAdapter;
import com.cu.android.DatabaseHelper.DatabaseHelper;
import com.cu.android.Model.LinkData;
import com.cu.android.R;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<LinkData> linkData;
    ImageView back;
    TextView title;
    LinearLayout main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        main=findViewById(R.id.main);
        title=findViewById(R.id.title);
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
            Integer[] color={getResources().getColor(R.color.color1),getResources().getColor(R.color.color2),getResources().getColor(R.color.color3),getResources().getColor(R.color.color4),getResources().getColor(R.color.color5),getResources().getColor(R.color.color6)};
            Intent intent=getIntent();
            String category_title=intent.getStringExtra("Category");
            if(category_title.toLowerCase().equals("basic")){
                main.setBackgroundColor(color[0]);
            }else if(category_title.toLowerCase().equals("beginner")){
                main.setBackgroundColor(color[1]);
            }else if(category_title.toLowerCase().equals("intermediate")){
                main.setBackgroundColor(color[2]);
            }else if(category_title.toLowerCase().equals("library")){
                main.setBackgroundColor(color[3]);
            }else if(category_title.toLowerCase().equals("project")){
                main.setBackgroundColor(color[4]);
            }else if(category_title.toLowerCase().equals("design")){
                main.setBackgroundColor(color[5]);
            }
            title.setText(category_title);
            DatabaseHelper helper=new DatabaseHelper(this);
            linkData=new ArrayList<>();
            Cursor cursor=helper.getLink();
            if(cursor!=null && cursor.getCount()>0){
                while (cursor.moveToNext()) {
                    if(cursor.getString(4).toLowerCase().equals(category_title.toLowerCase())){
                        linkData.add(new LinkData(cursor.getString(0) + "", cursor.getString(1) + "", cursor.getString(2)+ "",cursor.getString(3)+"",cursor.getString(4)+""));
                    }
                }
            }
            helper.close();
            recyclerView.setAdapter(new LinkAdapter(linkData,"Category",getApplicationContext()));
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }
    }
}