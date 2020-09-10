package com.cu.android.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.android.DatabaseHelper.DatabaseHelper;
import com.cu.android.Activity.FavoriteActivity;
import com.cu.android.Model.LinkData;
import com.cu.android.R;

import java.util.ArrayList;
import java.util.Collection;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class LinkAdapter extends RecyclerView.Adapter<LinkAdapter.ViewHolder> implements Filterable {
    ArrayList<LinkData> linkData;
    ArrayList<LinkData> linkDataAll;
    Context context;
    String favorite_page;
    public LinkAdapter(ArrayList<LinkData> linkData, String favorite_page, Context context) {
        this.linkData=linkData;
        this.favorite_page=favorite_page;
        this.context=context;
        this.linkDataAll=new ArrayList<>(linkData);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.custom_link,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

            final String did=linkData.get(position).getId();
            final String favorite_check=linkData.get(position).getFavorite();
            final String link_url=linkData.get(position).getLink();
            final String name=linkData.get(position).getName();
            if (favorite_check.equals("false")) {
                holder.favorite.setVisibility(GONE);
                holder.favorite_border.setVisibility(VISIBLE);
            } else {
                holder.favorite.setVisibility(VISIBLE);
                holder.favorite_border.setVisibility(GONE);
            }
            holder.link.setText(name);
            holder.favorite_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.favorite.getVisibility()==GONE) {
                        holder.favorite.setVisibility(VISIBLE);
                        holder.favorite_border.setVisibility(GONE);
                        DatabaseHelper helper=new DatabaseHelper(context);
                        helper.updateLink(did,"true");
                    }else {
                        holder.favorite.setVisibility(GONE);
                        holder.favorite_border.setVisibility(VISIBLE);
                        DatabaseHelper helper=new DatabaseHelper(context);
                        helper.updateLink(did,"false");
                        if(favorite_page.equals("Favorite")){
                            context.startActivity(new Intent(context, FavoriteActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
                        }
                    }
                }
            });
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(Intent.ACTION_SEND);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_TEXT,"https://"+link_url);
                    context.startActivity(i);
                }
            });
            holder.show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setData(Uri.parse("https://"+link_url));
                    context.startActivity(i);
                    }catch (Exception e){
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        return linkData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<LinkData> filteredList=new ArrayList<>();
            if(charSequence.toString().isEmpty()){
                filteredList.addAll(linkDataAll);
            }else {
                for(LinkData data:linkDataAll){
                    if(data.getName().toLowerCase().contains(charSequence.toString().toLowerCase())){
                        filteredList.add(data);
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            linkData.clear();
            linkData.addAll((Collection<? extends LinkData>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView link;
        ImageView favorite,share,favorite_border;
        CardView show;
        LinearLayout favorite_add;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.link=itemView.findViewById(R.id.link);
            this.favorite=itemView.findViewById(R.id.favorite);
            this.favorite_border=itemView.findViewById(R.id.favorite_border);
            this.share=itemView.findViewById(R.id.share);
            this.show=itemView.findViewById(R.id.show);
            this.favorite_add=itemView.findViewById(R.id.favorite_add);

        }
    }
}
