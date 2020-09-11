package com.cu.android.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.android.DatabaseHelper.DatabaseHelper;
import com.cu.android.Model.NoteData;
import com.cu.android.Activity.NoteActivity;
import com.cu.android.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {
    ArrayList<NoteData> noteData;
    Context context;
    public NoteAdapter(ArrayList<NoteData> noteData, Context context) {
        this.noteData=noteData;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.custom_note,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final String did=noteData.get(position).getId();
        final String link_url=noteData.get(position).getLink();
        final String name=noteData.get(position).getName();
        holder.name.setText(name);
        holder.link.setText(link_url);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit(v,did,name,link_url);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_SEND);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,link_url);
                context.startActivity(i);
            }
        });
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setData(Uri.parse(link_url));
                    context.startActivity(i);
                }catch (Exception e){
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.show.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteConfirm(v,did);
                return false;
            }
        });
    }
    @SuppressLint("SetTextI18n")
    private void edit(View v, final String did, String name_text, String link_url){
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.AlertDialogTheme);
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_note, (ConstraintLayout) v.findViewById(R.id.dialog_layout));
            builder.setView(view);
            final TextView title=view.findViewById(R.id.title);
            final EditText name = view.findViewById(R.id.name);
            final EditText link = view.findViewById(R.id.link);
            title.setText("Edit Note");
            name.setText(name_text);
            link.setText(link_url);
            Button cancel = view.findViewById(R.id.cancel);
            Button ok = view.findViewById(R.id.add);
            ok.setText("Update");
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
                    DatabaseHelper helper=new DatabaseHelper(context);
                    helper.updateNote(did+"",name.getText()+"",link.getText()+"");
                    context.startActivity(new Intent(context, NoteActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                    dialog.dismiss();
                }
            });
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteConfirm(View v, final String did) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext(),R.style.AlertDialogTheme);
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_delete, (ConstraintLayout) v.findViewById(R.id.dialog_layout));
            builder.setView(view);
            Button cancel = view.findViewById(R.id.cancel);
            Button delete = view.findViewById(R.id.delete);
            final AlertDialog dialog = builder.create();
            dialog.show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper helper=new DatabaseHelper(context);
                    helper.deleteNote(did);
                    context.startActivity(new Intent(context, NoteActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));

                    dialog.dismiss();
                }
            });
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return noteData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,link;
        ImageView share,edit;
        CardView show;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name=itemView.findViewById(R.id.name);
            this.link=itemView.findViewById(R.id.link);
            this.share=itemView.findViewById(R.id.share);
            this.edit=itemView.findViewById(R.id.edit);
            this.show=itemView.findViewById(R.id.show);
        }
    }
}
