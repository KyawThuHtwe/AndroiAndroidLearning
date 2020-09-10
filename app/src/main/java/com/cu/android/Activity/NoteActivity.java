package com.cu.android.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.android.Adapter.NoteAdapter;
import com.cu.android.DatabaseHelper.DatabaseHelper;
import com.cu.android.Model.NoteData;
import com.cu.android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.view.View.VISIBLE;

public class NoteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<NoteData> noteData;
    ImageView back;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
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
            noteData=new ArrayList<>();
            Cursor cursor=helper.getNote();
            if(cursor!=null && cursor.getCount()>0){
                while (cursor.moveToNext()) {
                    count++;
                    noteData.add(new NoteData(cursor.getString(0) + "", cursor.getString(1) + "", cursor.getString(2)+ ""));
                }
            }
            helper.close();
            TextView count_text=findViewById(R.id.count);
            count_text.setText(count+"");
            recyclerView.setAdapter(new NoteAdapter(noteData,getApplicationContext()));
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();

        }
        final FloatingActionButton floatingActionButton=findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this,R.style.AlertDialogTheme);
                    View view = LayoutInflater.from(NoteActivity.this).inflate(R.layout.dialog_note, (ConstraintLayout) findViewById(R.id.dialog_layout));
                    builder.setView(view);
                    final EditText name = view.findViewById(R.id.name);
                    final EditText link = view.findViewById(R.id.link);
                    Button cancel = view.findViewById(R.id.cancel);
                    Button ok = view.findViewById(R.id.add);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            name.setText("");
                            link.setText("");
                            dialog.dismiss();
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DatabaseHelper helper=new DatabaseHelper(NoteActivity.this);
                            helper.insertNote(name.getText()+"",link.getText()+"");
                            startActivity(new Intent(NoteActivity.this, NoteActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                            dialog.dismiss();
                        }
                    });
                }catch (Exception e){
                    Toast.makeText(NoteActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 && floatingActionButton.getVisibility() != VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }
}