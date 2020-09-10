package com.cu.android.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.cu.android.Adapter.LinkAdapter;
import com.cu.android.Adapter.ViewPagerAdapter;
import com.cu.android.DatabaseHelper.DatabaseHelper;
import com.cu.android.Model.LinkData;
import com.cu.android.Model.PagerData;
import com.cu.android.R;
import java.util.ArrayList;
import java.util.List;

import static androidx.viewpager.widget.ViewPager.*;

public class MainActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    ArrayList<LinkData> linkData=new ArrayList<>();
    LinkAdapter linkAdapter;
    LinearLayout favorite,note,about;

    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    List<PagerData> pagerData;
    Integer[] colors=null;
    ArgbEvaluator argbEvaluator=new ArgbEvaluator();
    LinearLayout main;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pagerData=new ArrayList<>();
        pagerData.add(new PagerData(R.drawable.begain,"Basic"));
        pagerData.add(new PagerData(R.drawable.beginner,"Beginner"));
        pagerData.add(new PagerData(R.drawable.intermediate,"Intermediate"));
        pagerData.add(new PagerData(R.drawable.library,"Library"));
        pagerData.add(new PagerData(R.drawable.projects,"Project"));
        pagerData.add(new PagerData(R.drawable.design,"Design"));

        viewPagerAdapter=new ViewPagerAdapter(pagerData,getApplicationContext());
        viewPager=findViewById(R.id.viewPager);
        main=findViewById(R.id.main);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setPadding(50,0,50,0);
        colors= new Integer[]{getResources().getColor(R.color.color1),getResources().getColor(R.color.color2),getResources().getColor(R.color.color3),getResources().getColor(R.color.color4),getResources().getColor(R.color.color5),getResources().getColor(R.color.color6)};
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position<(viewPagerAdapter.getCount()-1) && position<(colors.length-1)){
                    main.setBackgroundColor((Integer) argbEvaluator.evaluate(positionOffset,colors[position],colors[position+1]));
                }else {
                    main.setBackgroundColor(colors[colors.length-1]);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final LinearLayout title=findViewById(R.id.app_title);
        NestedScrollView scrollView=findViewById(R.id.scroll_view);
        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY>1000 && title.getVisibility()==View.VISIBLE){
                    title.setVisibility(View.GONE);
                }else if(scrollY <=1000 && title.getVisibility()==View.GONE){
                    title.setVisibility(View.VISIBLE);
                }
            }
        });
        //data
        dataLoading();

        //
        linkAdapter=new LinkAdapter(linkData,"Main",getApplicationContext());
        about=findViewById(R.id.about);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AboutActivity.class));
            }
        });
        favorite=findViewById(R.id.favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FavoriteActivity.class));
            }
        });
        note=findViewById(R.id.note);
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),NoteActivity.class));
            }
        });
        searchView=findViewById(R.id.search);
        searchView.setIconified(true);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search...");
        final LinearLayout category_title,link_title;
        category_title=findViewById(R.id.category_title);
        link_title=findViewById(R.id.link_title);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()){
                    viewPager.setVisibility(VISIBLE);
                    category_title.setVisibility(VISIBLE);
                    link_title.setVisibility(VISIBLE);
                }else {
                    viewPager.setVisibility(GONE);
                    category_title.setVisibility(GONE);
                    link_title.setVisibility(GONE);
                }
                linkAdapter.getFilter().filter(newText);
                return false;
            }
        });
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(linkAdapter);
    }

    public void dataLoading(){
        try{
            DatabaseHelper helper=new DatabaseHelper(this);
            if(linkData.size()>0){
                linkData.clear();
            }
            Cursor cursor=helper.getLink();
            if(cursor!=null && cursor.getCount()>0){
                while (cursor.moveToNext()) {
                    linkData.add(new LinkData(cursor.getString(0) + "", cursor.getString(1) + "", cursor.getString(2)+"",cursor.getString(3)+"",cursor.getString(4)+""));
                }
            }

            helper.close();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}