package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.holanda.bilecoagmaonlineregistration2021.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class act_Statistics extends AppCompatActivity {


    TextView txtTop1, txtTop2, txtTop3, txtTop4, txtTop5, txtUserRank;
    TextView txtTop1_emp, txtTop2_emp, txtTop3_emp, txtTop4_emp, txtTop5_emp;
    TextView txtTop1_Regs_emp, txtTop2_Regs_emp, txtTop3_Regs_emp, txtTop4_Regs_emp, txtTop5_Regs_emp;
    TextView txtTotalRegistrations, txtUser_Username, txtUser_FullName;
    TextView txtUser_ContactNumber, txtUser_Address;
    TextView txtTop1_Regs, txtTop2_Regs, txtTop3_Regs, txtTop4_Regs, txtTop5_Regs;
    ImageView Crown_1, Crown_2, Crown_3, Crown_4, Crown_5;
    ImageView Crown_6, Crown_7, Crown_8, Crown_9, Crown_10;
    Button btnRefresh, btnFullRanksMCO, btnFullRanksEmployee;

    private ProgressDialog pDialog;

    String Username, FullName, ContactNumber, Address, Position;
    String json_message, json_message_1;

    String Top1_Name = "";
    String Top2_Name = "";
    String Top3_Name = "";
    String Top4_Name = "";
    String Top5_Name = "";
    String Top1_Count, Top2_Count, Top3_Count, Top4_Count, Top5_Count;

    String Top1_Name_emp = "";
    String Top2_Name_emp = "";
    String Top3_Name_emp = "";
    String Top4_Name_emp = "";
    String Top5_Name_emp = "";
    String Top1_Count_emp, Top2_Count_emp, Top3_Count_emp, Top4_Count_emp, Top5_Count_emp;

    String User_Current_Rank, User_Current_Rank_emp;
    String User_Regs, User_Regs_emp;


    private boolean adLoaded=false;
    private AdView bannerAdView;

    SwipeRefreshLayout stats_Swipe_Container;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //getSupportActionBar().setTitle("Stats and Ranks");
        Objects.requireNonNull(getSupportActionBar()).hide();


        bannerAdView = (AdView) findViewById(R.id.bannerAdView_8);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

                //Showing a simple Toast Message to the user when The Google AdMob Sdk Initialization is Completed
                //Toast.makeText (MainActivity.this, "AdMob Sdk Initialize "+ initializationStatus.toString(), Toast.LENGTH_LONG).show();

                loadBannerAd();



            }
        });


        bannerAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                // setting adLoaded to true
                adLoaded=true;
                // Showing a simple Toast message to user when an ad is loaded
                //Toast.makeText (MainActivity.this, "Ad is Loaded", Toast.LENGTH_LONG).show();
                if(getString(R.string.ShowAd).equals("True")){

                    showBannerAd();

                }
            }


            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // setting adLoaded to false
                adLoaded=false;

                //Showing a simple Toast message to user when and ad is failed to load
                //Toast.makeText (MainActivity.this, "Ad Failed to Load ", Toast.LENGTH_LONG).show();
                MobileAds.initialize(act_Statistics.this);

            }

            @Override
            public void onAdOpened() {

                // Showing a simple Toast message to user when an ad opens and overlay and covers the device screen
                //Toast.makeText (MainActivity.this, "Ad Opened", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onAdClicked() {

                // Showing a simple Toast message to user when a user clicked the ad
                //Toast.makeText (MainActivity.this, "Ad Clicked", Toast.LENGTH_LONG).show();

            }

//            @Override
//            public void onAdLeftApplication() {
//
//                // Showing a simple Toast message to user when the user left the application
//                //Toast.makeText (MainActivity.this, "Ad Left the Application", Toast.LENGTH_LONG).show();
//
//            }

            @Override
            public void onAdClosed() {

                // Showing a simple Toast message to user when the user interacted with ad and got the other app and then return to the app again
                //Toast.makeText (MainActivity.this, "Ad is Closed", Toast.LENGTH_LONG).show();
                loadBannerAd();

            }
        });




        txtTop1 = findViewById(R.id.stats_txt_Top_1);
        txtTop2 = findViewById(R.id.stats_txt_Top_2);
        txtTop3 = findViewById(R.id.stats_txt_Top_3);
        txtTop4 = findViewById(R.id.stats_txt_Top_4);
        txtTop5 = findViewById(R.id.stats_txt_Top_5);
        txtTop1_emp = findViewById(R.id.stats_txt_Top_1_emp);
        txtTop2_emp = findViewById(R.id.stats_txt_Top_2_emp);
        txtTop3_emp = findViewById(R.id.stats_txt_Top_3_emp);
        txtTop4_emp = findViewById(R.id.stats_txt_Top_4_emp);
        txtTop5_emp = findViewById(R.id.stats_txt_Top_5_emp);

        txtTop1_Regs = findViewById(R.id.stats_txt_Top_1_Reg_Count);
        txtTop2_Regs = findViewById(R.id.stats_txt_Top_2_Reg_Count);
        txtTop3_Regs = findViewById(R.id.stats_txt_Top_3_Reg_Count);
        txtTop4_Regs = findViewById(R.id.stats_txt_Top_4_Reg_Count);
        txtTop5_Regs = findViewById(R.id.stats_txt_Top_5_Reg_Count);
        txtTop1_Regs_emp = findViewById(R.id.stats_txt_Top_1_Reg_Count_emp);
        txtTop2_Regs_emp = findViewById(R.id.stats_txt_Top_2_Reg_Count_emp);
        txtTop3_Regs_emp = findViewById(R.id.stats_txt_Top_3_Reg_Count_emp);
        txtTop4_Regs_emp = findViewById(R.id.stats_txt_Top_4_Reg_Count_emp);
        txtTop5_Regs_emp = findViewById(R.id.stats_txt_Top_5_Reg_Count_emp);

        txtUserRank = findViewById(R.id.stats_txt_User_Rank);
        txtTotalRegistrations = findViewById(R.id.stats_txt_User_Total_Reg);

        txtUser_Username = findViewById(R.id.stats_txt_Username);
        txtUser_FullName = findViewById(R.id.stats_txt_FullName);
        txtUser_ContactNumber = findViewById(R.id.stats_txt_ContactNumber);
        txtUser_Address = findViewById(R.id.stats_txt_Address);

        btnRefresh = findViewById(R.id.stats_btn_Refresh);
        btnFullRanksEmployee = findViewById(R.id.stats_btn_View_Full_Ranks_Employees);
        btnFullRanksMCO = findViewById(R.id.stats_btn_View_Full_Ranks_MCO);

        stats_Swipe_Container = findViewById(R.id.stats_Swipe_Layout);

        Username = getIntent().getExtras().getString("Log_Username");
        FullName = getIntent().getExtras().getString("Log_FullName");
        ContactNumber = getIntent().getExtras().getString("Log_Contact");
        Address = getIntent().getExtras().getString("Log_Address");
        Position = getIntent().getExtras().getString("Log_Position");

        txtUser_Username.setText(Username);
        txtUser_FullName.setText(FullName);
        txtUser_ContactNumber.setText(ContactNumber);
        txtUser_Address.setText(Address);

        Crown_1 = findViewById(R.id.stat_img_Crown_1);
        Crown_2 = findViewById(R.id.stat_img_Crown_2);
        Crown_3 = findViewById(R.id.stat_img_Crown_3);
        Crown_4 = findViewById(R.id.stat_img_Crown_4);
        Crown_5 = findViewById(R.id.stat_img_Crown_5);
        Crown_6 = findViewById(R.id.stat_img_Crown_6);
        Crown_7 = findViewById(R.id.stat_img_Crown_7);
        Crown_8 = findViewById(R.id.stat_img_Crown_8);
        Crown_9 = findViewById(R.id.stat_img_Crown_9);
        Crown_10 = findViewById(R.id.stat_img_Crown_10);

        if(Position.substring(0,6).equals("BILECO")){

            txtTop1_emp.setVisibility(View.VISIBLE);
            txtTop2_emp.setVisibility(View.VISIBLE);
            txtTop3_emp.setVisibility(View.VISIBLE);
            txtTop4_emp.setVisibility(View.VISIBLE);
            txtTop5_emp.setVisibility(View.VISIBLE);

            txtTop1_Regs_emp.setVisibility(View.VISIBLE);
            txtTop2_Regs_emp.setVisibility(View.VISIBLE);
            txtTop3_Regs_emp.setVisibility(View.VISIBLE);
            txtTop4_Regs_emp.setVisibility(View.VISIBLE);
            txtTop5_Regs_emp.setVisibility(View.VISIBLE);

            Crown_6.setVisibility(View.VISIBLE);
            Crown_7.setVisibility(View.VISIBLE);
            Crown_8.setVisibility(View.VISIBLE);
            Crown_9.setVisibility(View.VISIBLE);
            Crown_10.setVisibility(View.VISIBLE);

            btnFullRanksEmployee.setVisibility(View.VISIBLE);

            findViewById(R.id.textView17).setVisibility(View.VISIBLE);

            TextView Rank_Label_Emp = findViewById(R.id.Stats_txt_Ranking_Type);
            Rank_Label_Emp.setText("Your Rank (Employee)");


        }else{


            txtTop1_emp.getLayoutParams().height = 0;
            txtTop2_emp.getLayoutParams().height = 0;
            txtTop3_emp.getLayoutParams().height = 0;
            txtTop4_emp.getLayoutParams().height = 0;
            txtTop5_emp.getLayoutParams().height = 0;

            txtTop1_Regs_emp.getLayoutParams().height = 0;
            txtTop2_Regs_emp.getLayoutParams().height = 0;
            txtTop3_Regs_emp.getLayoutParams().height = 0;
            txtTop4_Regs_emp.getLayoutParams().height = 0;
            txtTop5_Regs_emp.getLayoutParams().height = 0;

            Crown_6.getLayoutParams().height = 0;
            Crown_7.getLayoutParams().height = 0;
            Crown_8.getLayoutParams().height = 0;
            Crown_9.getLayoutParams().height = 0;
            Crown_10.getLayoutParams().height = 0;

            btnFullRanksEmployee.setVisibility(View.GONE);

            findViewById(R.id.textView17).getLayoutParams().height = 0;

            TextView Rank_Label_Emp = findViewById(R.id.Stats_txt_Ranking_Type);
            Rank_Label_Emp.setText("Your Rank");

        }

        btnRefresh.setVisibility(View.INVISIBLE);

        stats_Swipe_Container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                if(internetConnectionAvailable(10000) == true){
                    new Fetch_Top_5_MCO().execute();


                }else {
                    Toast.makeText(act_Statistics.this, "Please check your internet connection", Toast.LENGTH_LONG).show();

                    stats_Swipe_Container.setRefreshing(false);
                }


            }
        });

        if(internetConnectionAvailable(10000) == true){
            new Fetch_Top_5_MCO().execute();


        }else {
            Toast.makeText(act_Statistics.this, "Please check your internet connection", Toast.LENGTH_LONG).show();


        }



        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRefresh.setVisibility(View.INVISIBLE);
                if(internetConnectionAvailable(10000) == true){
                    new Fetch_Top_5_MCO().execute();


                }else {
                    Toast.makeText(act_Statistics.this, "Please check your internet connection", Toast.LENGTH_LONG).show();


                }




            }
        });


        btnFullRanksMCO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerActivity = new Intent(getApplicationContext(), act_Fullview_Rankings.class);

                registerActivity.putExtra("Rank_Type", "MCO");
                startActivity(registerActivity);


            }
        });


        btnFullRanksEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerActivity = new Intent(getApplicationContext(), act_Fullview_Rankings.class);

                registerActivity.putExtra("Rank_Type", "EMP");
                startActivity(registerActivity);


            }
        });


        ObjectAnimator anim_1 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flip);
        anim_1.setTarget( Crown_1);
        anim_1.setDuration(3000);
        anim_1.setInterpolator(new BounceInterpolator());
        anim_1.setRepeatCount(ValueAnimator.INFINITE);
        anim_1.start();

        ObjectAnimator anim_2 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flip_rev);
        anim_2.setTarget( Crown_2);
        anim_2.setDuration(4500);
        anim_2.setInterpolator(new BounceInterpolator());
        anim_2.setRepeatCount(ValueAnimator.INFINITE);
        anim_2.start();

        ObjectAnimator anim_3 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flip);
        anim_3.setTarget( Crown_3);
        anim_3.setDuration(6000);
        anim_3.setInterpolator(new BounceInterpolator());
        anim_3.setRepeatCount(ValueAnimator.INFINITE);
        anim_3.start();

        final Animation animShake = AnimationUtils.loadAnimation(this, R.anim.rough_shake);

        Crown_4.startAnimation(animShake);
        Crown_5.startAnimation(animShake);


        ObjectAnimator anim_4 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flip);
        anim_4.setTarget( Crown_6);
        anim_4.setDuration(3000);
        anim_4.setInterpolator(new BounceInterpolator());
        anim_4.setRepeatCount(ValueAnimator.INFINITE);
        anim_4.start();

        ObjectAnimator anim_5 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flip_rev);
        anim_5.setTarget( Crown_7);
        anim_5.setDuration(4500);
        anim_5.setInterpolator(new BounceInterpolator());
        anim_5.setRepeatCount(ValueAnimator.INFINITE);
        anim_5.start();

        ObjectAnimator anim_6 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flip);
        anim_6.setTarget( Crown_8);
        anim_6.setDuration(6000);
        anim_6.setInterpolator(new BounceInterpolator());
        anim_6.setRepeatCount(ValueAnimator.INFINITE);
        anim_6.start();


        Crown_9.startAnimation(animShake);
        Crown_10.startAnimation(animShake);


//        ObjectAnimator anim_4 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flip);
//        anim_4.setTarget( Crown_4);
//        anim_4.setDuration(3000);
//        anim_4.setInterpolator(new BounceInterpolator());
//        anim_4.setRepeatCount(ValueAnimator.INFINITE);
//        anim_4.start();
//
//        ObjectAnimator anim_5 = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.flip);
//        anim_5.setTarget( Crown_5);
//        anim_5.setDuration(3000);
//        anim_5.setInterpolator(new BounceInterpolator());
//        anim_5.setRepeatCount(ValueAnimator.INFINITE);
//        anim_5.start();


    }


    class Fetch_Top_5_MCO extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Statistics.this);
            pDialog.setMessage("Contacting Server. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            try {


                String link = getString(R.string.Webshost_IP) + "/AGMA_2024_API/CheckUsernameRanking.php";


                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);


                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);

                    break;
                }
                reader.close();
                if (line != null){
                    JSONObject jsonObj = new JSONObject(line);
                    if (jsonObj.getString("Top5").equals("No Records Found.")){
                        json_message = "No Records Found.";

                    }else{
                        JSONArray Top5_Ranking = jsonObj.getJSONArray("Top5");
                        int curr_rank_grouping = 1;
                        String curr_count = "";
                        for (int i = 0; i < Top5_Ranking.length(); i++) {

                            JSONObject c = Top5_Ranking.getJSONObject(i);

                            if(curr_count.equals("")){
                                curr_count = c.getString("Regs");
                                Top1_Name = c.getString("Username_Full_Name");
                                Top1_Count = c.getString("Regs");

                            }else if(!curr_count.equals(c.getString("Regs"))){
                                curr_count = c.getString("Regs");
                                curr_rank_grouping = curr_rank_grouping + 1;

                                if(curr_rank_grouping == 1){
                                    Top1_Name = c.getString("Username_Full_Name");
                                    Top1_Count = c.getString("Regs");

                                }else if(curr_rank_grouping == 2){
                                    Top2_Name = c.getString("Username_Full_Name");
                                    Top2_Count = c.getString("Regs");

                                }else if(curr_rank_grouping == 3){
                                    Top3_Name = c.getString("Username_Full_Name");
                                    Top3_Count = c.getString("Regs");

                                }else if(curr_rank_grouping == 4){
                                    Top4_Name = c.getString("Username_Full_Name");
                                    Top4_Count = c.getString("Regs");

                                }else if(curr_rank_grouping == 5){
                                    Top5_Name = c.getString("Username_Full_Name");
                                    Top5_Count = c.getString("Regs");

                                }

                            }else if(curr_count.equals(c.getString("Regs"))){
                                if(curr_rank_grouping == 1){
                                    Top1_Name = Top1_Name + "\n" + c.getString("Username_Full_Name");
                                    Top1_Count = c.getString("Regs");

                                }else if(curr_rank_grouping == 2){
                                    Top2_Name = Top2_Name  + "\n" + c.getString("Username_Full_Name");
                                    Top2_Count = c.getString("Regs");

                                }else if(curr_rank_grouping == 3){
                                    Top3_Name = Top3_Name  + "\n" + c.getString("Username_Full_Name");
                                    Top3_Count = c.getString("Regs");

                                }else if(curr_rank_grouping == 4){
                                    Top4_Name = Top4_Name  + "\n" + c.getString("Username_Full_Name");
                                    Top4_Count = c.getString("Regs");

                                }else if(curr_rank_grouping == 5){
                                    Top5_Name = Top5_Name  + "\n" + c.getString("Username_Full_Name");
                                    Top5_Count = c.getString("Regs");

                                }


                            }


                            if(Username.equals(c.getString("Username"))){
                                User_Current_Rank = String.valueOf(curr_rank_grouping);
                                User_Regs = c.getString("Regs");
                            }

                        }


                        //Listview Process Trigger Here
                        json_message = "Data Loaded";
                    }
                    // Getting JSON Array node

                }else {
                    json_message = "No Response from server";
                }
            } catch (Exception e) {
                e.printStackTrace();
                json_message = new String("Exception: " + e.getMessage());
                //Toast.makeText(getContext(), new String("Exception: " + e.getMessage()), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            btnRefresh.setVisibility(View.VISIBLE);

            stats_Swipe_Container.setRefreshing(false);

            if (json_message.equals("Data Loaded")) {
                if(!Top1_Name.isEmpty()){
                    txtTop1.setText(Top1_Name);
                    txtTop1_Regs.setText("Total Registrations: " + Top1_Count);

                }else{
                    txtTop1.setText("- - - - -");
                    txtTop1_Regs.setText("- - - - -");

                }


                if(!Top2_Name.isEmpty()){
                    txtTop2.setText(Top2_Name);
                    txtTop2_Regs.setText("Total Registrations: " + Top2_Count);

                }else{
                    txtTop2.setText("- - - - -");
                    txtTop2_Regs.setText("- - - - -");

                }

                if(!Top3_Name.isEmpty()){
                    txtTop3.setText(Top3_Name);
                    txtTop3_Regs.setText("Total Registrations: " + Top3_Count);

                }else{
                    txtTop3.setText("- - - - -");
                    txtTop3_Regs.setText("- - - - -");

                }

                if(!Top4_Name.isEmpty()){
                    txtTop4.setText(Top4_Name);
                    txtTop4_Regs.setText("Total Registrations: " + Top4_Count);

                }else{
                    txtTop4.setText("- - - - -");
                    txtTop4_Regs.setText("- - - - -");

                }


                if(!Top5_Name.isEmpty()){
                    txtTop5.setText(Top5_Name);
                    txtTop5_Regs.setText("Total Registrations: " + Top5_Count);

                }else{
                    txtTop5.setText("- - - - -");
                    txtTop5_Regs.setText("- - - - -");

                }

                if(!Position.substring(0,6).equals("BILECO")){
                    txtUserRank.setText(User_Current_Rank);
                    txtTotalRegistrations.setText(User_Regs);

                    pDialog.dismiss();

                }else{
                    new Fetch_Top_5_Emp().execute();

                }


                Toast.makeText(act_Statistics.this,"Done!", Toast.LENGTH_LONG).show();

            }else if (json_message.equals("No Records Found.")) {

                Toast.makeText(act_Statistics.this,json_message, Toast.LENGTH_LONG).show();
                new Fetch_Top_5_Emp().execute();

            }else if (json_message.equals("No Response from server")) {
                Toast.makeText(act_Statistics.this,json_message , Toast.LENGTH_LONG).show();

            } else {

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Statistics.this, finmsg + "\nException 418", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Statistics.this, json_message.replace(getString(R.string.Webshost_IP), "Server").replace("/", "").replace(getString(R.string.Webhost_Account_Ledgertest), "Server"), Toast.LENGTH_LONG).show();
            }
        }

    }


    class Fetch_Top_5_Emp extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            pDialog_1 = new ProgressDialog(act_Statistics.this);
//            pDialog_1.setMessage("Fetching Employee Rankings. Please wait...");
//            pDialog_1.setIndeterminate(false);
//            pDialog_1.setCancelable(false);
//            pDialog_1.show();
        }


        protected String doInBackground(String... args) {
            try {


                String link = getString(R.string.Webshost_IP) + "/AGMA_2024_API/CheckEmployeeRanking.php";


                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);


                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;
                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);

                    break;
                }
                reader.close();
                if (line != null){

                    JSONObject jsonObj = new JSONObject(line);
                    Log.d("return_string", line);
                    if (jsonObj.getString("Top5").equals("No Records Found.")){
                        json_message_1 = "No Records Found.";

                    }else{
                        JSONArray Top5_Ranking = jsonObj.getJSONArray("Top5");
                        int curr_rank_grouping = 1;
                        String curr_count = "";
                        for (int i = 0; i < Top5_Ranking.length(); i++) {

                            JSONObject c = Top5_Ranking.getJSONObject(i);

                            if(curr_count.equals("")){
                                curr_count = c.getString("Regs");
                                Top1_Name_emp = c.getString("Username_Full_Name");
                                Log.d("return_string_T1", c.getString("Username_Full_Name"));
                                Log.d("return_string_T2", String.valueOf(i));
                                Top1_Count_emp = c.getString("Regs");

                            }else if(!curr_count.equals(c.getString("Regs"))){
                                curr_count = c.getString("Regs");
                                curr_rank_grouping = curr_rank_grouping + 1;

                                if(curr_rank_grouping == 1){
                                    Top1_Name_emp = c.getString("Username_Full_Name");
                                    Top1_Count_emp = c.getString("Regs");

                                }else if(curr_rank_grouping == 2){
                                    Top2_Name_emp = c.getString("Username_Full_Name");
                                    Top2_Count_emp = c.getString("Regs");

                                }else if(curr_rank_grouping == 3){
                                    Top3_Name_emp = c.getString("Username_Full_Name");
                                    Top3_Count_emp = c.getString("Regs");

                                }else if(curr_rank_grouping == 4){
                                    Top4_Name_emp = c.getString("Username_Full_Name");
                                    Top4_Count_emp = c.getString("Regs");

                                }else if(curr_rank_grouping == 5){
                                    Top5_Name_emp = c.getString("Username_Full_Name");
                                    Top5_Count_emp = c.getString("Regs");

                                }

                            }else if(curr_count.equals(c.getString("Regs"))){
                                if(curr_rank_grouping == 1){
                                    Top1_Name_emp = Top1_Name_emp + "\n" + c.getString("Username_Full_Name");
                                    Top1_Count_emp = c.getString("Regs");

                                }else if(curr_rank_grouping == 2){
                                    Top2_Name_emp = Top2_Name_emp  + "\n" + c.getString("Username_Full_Name");
                                    Top2_Count_emp = c.getString("Regs");

                                }else if(curr_rank_grouping == 3){
                                    Top3_Name_emp = Top3_Name_emp  + "\n" + c.getString("Username_Full_Name");
                                    Top3_Count_emp = c.getString("Regs");

                                }else if(curr_rank_grouping == 4){
                                    Top4_Name_emp = Top4_Name_emp  + "\n" + c.getString("Username_Full_Name");
                                    Top4_Count_emp = c.getString("Regs");

                                }else if(curr_rank_grouping == 5){
                                    Top5_Name_emp = Top5_Name_emp  + "\n" + c.getString("Username_Full_Name");
                                    Top5_Count_emp = c.getString("Regs");

                                }


                            }


                            if(Username.equals(c.getString("Username"))){
                                User_Current_Rank_emp = String.valueOf(curr_rank_grouping);
                                User_Regs_emp = c.getString("Regs");
                            }

                        }


                        //Listview Process Trigger Here
                        json_message_1 = "Data Loaded";
                    }
                    // Getting JSON Array node

                }else {
                    json_message_1 = "No Response from server";
                }
            } catch (Exception e) {
                e.printStackTrace();
                json_message_1 = new String("Exception: " + e.getMessage());
                //Toast.makeText(getContext(), new String("Exception: " + e.getMessage()), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            btnRefresh.setVisibility(View.VISIBLE);

            stats_Swipe_Container.setRefreshing(false);
            if (json_message_1.equals("Data Loaded")) {
                if(!Top1_Name_emp.isEmpty()){
                    txtTop1_emp.setText(Top1_Name_emp);
                    txtTop1_Regs_emp.setText("Total Registrations: " + Top1_Count_emp);
                }else{
                    txtTop1_emp.setText("- - - - -");
                    txtTop1_Regs_emp.setText("- - - - -");

                }


                if(!Top2_Name_emp.isEmpty()){
                    txtTop2_emp.setText(Top2_Name_emp);
                    txtTop2_Regs_emp.setText("Total Registrations: " + Top2_Count_emp);

                }else{
                    txtTop2_emp.setText("- - - - -");
                    txtTop2_Regs_emp.setText("- - - - -");

                }

                if(!Top3_Name_emp.isEmpty()){
                    txtTop3_emp.setText(Top3_Name_emp);
                    txtTop3_Regs_emp.setText("Total Registrations: " + Top3_Count_emp);

                }else{
                    txtTop3_emp.setText("- - - - -");
                    txtTop3_Regs_emp.setText("- - - - -");

                }

                if(!Top4_Name_emp.isEmpty()){
                    txtTop4_emp.setText(Top4_Name_emp);
                    txtTop4_Regs_emp.setText("Total Registrations: " + Top4_Count_emp);

                }else{
                    txtTop4_emp.setText("- - - - -");
                    txtTop4_Regs_emp.setText("- - - - -");

                }


                if(!Top5_Name_emp.isEmpty()){

                    txtTop5_emp.setText(Top5_Name_emp);
                    txtTop5_Regs_emp.setText("Total Registrations: " + Top5_Count_emp);

                }else{

                    txtTop5_emp.setText("- - - - -");
                    txtTop5_Regs_emp.setText("- - - - -");

                }

                txtUserRank.setText(User_Current_Rank_emp);
                txtTotalRegistrations.setText(User_Regs_emp);

                Toast.makeText(act_Statistics.this,"Done!", Toast.LENGTH_LONG).show();

            }else if (json_message_1.equals("No Records Found.")) {

                Toast.makeText(act_Statistics.this,json_message, Toast.LENGTH_LONG).show();

            }else if (json_message_1.equals("No Response from server")) {
                Toast.makeText(act_Statistics.this,json_message , Toast.LENGTH_LONG).show();

            } else {

                String finmsg = json_message_1;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Statistics.this, finmsg + "\nException 418", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Statistics.this, json_message.replace(getString(R.string.Webshost_IP), "Server").replace("/", "").replace(getString(R.string.Webhost_Account_Ledgertest), "Server"), Toast.LENGTH_LONG).show();
            }
        }

    }



    private void loadBannerAd()
    {
        // Creating  a Ad Request
        AdRequest adRequest = new AdRequest.Builder().build();

        // load Ad with the Request
        bannerAdView.loadAd(adRequest);

        // Showing a simple Toast message to user when an ad is Loading
        //Toast.makeText (MainActivity.this, "Banner Ad is loading ", Toast.LENGTH_LONG).show();

    }

    private void showBannerAd()
    {
        if ( adLoaded )
        {
            //showing the ad Banner Ad if it is loaded
            bannerAdView.setVisibility(View.VISIBLE);

            // Showing a simple Toast message to user when an banner ad is shown to the user
            //Toast.makeText (MainActivity.this, "Banner Ad Shown", Toast.LENGTH_LONG).show();
        }
        else
        {
            //Load the banner ad if it is not loaded
            loadBannerAd();

            // Showing a simple Toast message to user when an ad is not loaded
            //Toast.makeText (MainActivity.this, "Banner Ad is Loaded ", Toast.LENGTH_LONG).show();

        }
    }


    private void hideBannerAd()
    {
        // Hiding the Banner
        bannerAdView.setVisibility(View.GONE);
    }


    private boolean internetConnectionAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("222.127.55.108");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException | ExecutionException | TimeoutException ignored) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }



}