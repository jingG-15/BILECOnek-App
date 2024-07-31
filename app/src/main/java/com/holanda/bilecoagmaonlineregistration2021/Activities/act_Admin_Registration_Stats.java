package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class act_Admin_Registration_Stats extends AppCompatActivity {


    SwipeRefreshLayout admin_swipe_cont;

    private ProgressDialog pDialog;

    String json_message;

    ArrayList<HashMap<String, String>> Resulting_Town_Regs;
    ListView reg_listview;

    private boolean adLoaded=false;
    private AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_registration_stats);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Registrations Per District");


        bannerAdView = (AdView) findViewById(R.id.bannerAdView_11);

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
                MobileAds.initialize(act_Admin_Registration_Stats.this);

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


        admin_swipe_cont = findViewById(R.id.admin_swiper);

        reg_listview = findViewById(R.id.list_Reg_Counts);

        admin_swipe_cont.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(internetConnectionAvailable(10000)){
                    Resulting_Town_Regs = new ArrayList<>();
                    new Fetch_Totals().execute();

                }else {
                    Toast.makeText(act_Admin_Registration_Stats.this, "Please check your internet connection", Toast.LENGTH_LONG).show();


                }
            }
        });



        if(internetConnectionAvailable(10000)){
            Resulting_Town_Regs = new ArrayList<>();
            new Fetch_Totals().execute();

        }else {
            Toast.makeText(act_Admin_Registration_Stats.this, "Please check your internet connection", Toast.LENGTH_LONG).show();


        }

    }


    private boolean internetConnectionAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName(getString(R.string.Net_IP_Check));
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



    class Fetch_Totals extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Admin_Registration_Stats.this);
            pDialog.setMessage("Contacting Server. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            try {


                String link = getString(R.string.Webshost_IP) + "/AGMA_2024_API/CheckRegCounts.php";


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
                    if (jsonObj.getString("TownRegs").equals("No Records Found.")){
                        json_message = "No Records Found.";

                    }else{
                        JSONArray Reg_Json = jsonObj.getJSONArray("TownRegs");
                        int total_regs = 0;
                        for (int i = 0; i < Reg_Json.length(); i++) {

                            JSONObject c = Reg_Json.getJSONObject(i);


                            HashMap<String, String> result_arr = new HashMap<>();

                            // adding each child node to HashMap key => value

                            result_arr.put("Town_Name", c.getString("Town"));
                            result_arr.put("Reg_Count", c.getString("TotalRegs"));

                            total_regs = total_regs + Integer.parseInt(c.getString("TotalRegs"));


                            // adding contact to contact list
                            Resulting_Town_Regs.add(result_arr);



                        }

                        HashMap<String, String> result_arr = new HashMap<>();

                        result_arr.put("Town_Name", "TOTAL REGISTRATION");
                        result_arr.put("Reg_Count", String.valueOf(total_regs));

                        // adding contact to contact list
                        Resulting_Town_Regs.add(result_arr);


                        //Listview Process Trigger Here

                        json_message = "List Loaded";
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


            admin_swipe_cont.setRefreshing(false);

            if (json_message.equals("List Loaded")) {




                ListAdapter adapter = new SimpleAdapter(
                        act_Admin_Registration_Stats.this, Resulting_Town_Regs,
                        R.layout.list_reg_count, new String[]{"Town_Name", "Reg_Count"}, new int[]{R.id.txt_reg_District_Name,
                        R.id.txt_reg_Count}){

                };

                reg_listview.setAdapter(adapter );






                Toast.makeText(act_Admin_Registration_Stats.this,"Done!", Toast.LENGTH_LONG).show();

            }else if (json_message.equals("No Records Found.")) {

                Toast.makeText(act_Admin_Registration_Stats.this,json_message, Toast.LENGTH_LONG).show();

            }else if (json_message.equals("No Response from server")) {
                Toast.makeText(act_Admin_Registration_Stats.this,json_message , Toast.LENGTH_LONG).show();

            } else {

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Admin_Registration_Stats.this, finmsg + "\nException 418", Toast.LENGTH_LONG).show();


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
}