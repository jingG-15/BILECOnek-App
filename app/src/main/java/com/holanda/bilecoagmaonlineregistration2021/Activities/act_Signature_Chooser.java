package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


//import com.squareup.picasso.Picasso;
import com.holanda.bilecoagmaonlineregistration2021.Adapters.sign_Recycler_Adapter;
import com.holanda.bilecoagmaonlineregistration2021.Adapters.sign_chooser_var_link;
import com.holanda.bilecoagmaonlineregistration2021.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class act_Signature_Chooser extends AppCompatActivity {

    SwipeRefreshLayout recyclerHolder;
    RecyclerView rec_disp_sign_list;
    GridLayoutManager mGridLayoutmanager;

    int last_index_array;
    int Load_times;
    String last_ID;
    String[] fileNames;
    ArrayList<HashMap<String, String>> Resulting_disp_items_List;
    ArrayList<sign_chooser_var_link> items_list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_chooser);

        Objects.requireNonNull(getSupportActionBar()).hide();
        File dir;


        recyclerHolder = findViewById(R.id.sign_feed_Swiper);
        rec_disp_sign_list = findViewById(R.id.sign_feed_recycler);

        mGridLayoutmanager = new GridLayoutManager(this, 2);
        rec_disp_sign_list.setLayoutManager(mGridLayoutmanager);

        last_index_array = 0;
        last_ID = "0";
        Load_times = 1;
        Resulting_disp_items_List = new ArrayList<>();
        items_list = new ArrayList<>();
        //txtEndofProd.setVisibility(View.INVISIBLE);
        recyclerHolder.setRefreshing(true);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            dir = new File( act_Signature_Chooser.this.getExternalFilesDir( null) + "/BILECOnek_Sign_PNG/" );


        }else{
            dir = new File( "/sdcard/BILECOnek_Sign_PNG/" );


        }

       fileNames = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (new File(dir,name).isDirectory())
                    return false;
                return name.toLowerCase().endsWith(".png");
            }


        });



        Process_Display_Items(fileNames);

        recyclerHolder.setRefreshing(false);

        recyclerHolder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {



                last_index_array = 0;
                last_ID = "0";
                Load_times = 0;
                Resulting_disp_items_List = new ArrayList<>();
                items_list = new ArrayList<>();
                //txtEndofProd.setVisibility(View.INVISIBLE);
                recyclerHolder.setRefreshing(true);

                //TODO Insert function for processing views

                //new frag_Newsfeed.Load_Newsfeed_items().execute();

                Process_Display_Items(fileNames);

                recyclerHolder.setRefreshing(false);

            }
        });




    }

    private void Process_Display_Items(String[] imageName){




        for(int i = 0; i < imageName.length; i++){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                items_list.add(new sign_chooser_var_link(imageName[i],
                        act_Signature_Chooser.this.getExternalFilesDir(null) + "/BILECOnek_Sign_PNG/" + imageName[i]));

            }else{
                items_list.add(new sign_chooser_var_link(imageName[i],
                        act_Signature_Chooser.this.getExternalFilesDir(null) + "/sdcard/BILECOnek_Sign_PNG/" + imageName[i]));



            }


        }


        rec_disp_sign_list.setAdapter(new sign_Recycler_Adapter(act_Signature_Chooser.this, items_list, new sign_Recycler_Adapter.OnItemClickListener() {

            @Override
            public void onItemClick(int position) {
                assert fileNames != null;
                String str = fileNames[position];

                //Toast.makeText(act_Signature_Chooser.this,"/sdcard/BILECOnek_Sign_PNG/" + str, Toast.LENGTH_LONG).show();

                Intent intent = new Intent();

                intent.putExtra("Selected_Sign_Name", str);
                setResult(RESULT_OK, intent);
                finish();


            }
        }));



    }


}