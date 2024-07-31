 package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.holanda.bilecoagmaonlineregistration2021.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;




public class act_Search_by_Account_Number extends AppCompatActivity {

    EditText txt_Account_Number;
    Button btn_Verify;
    String Account_Number_For_Ver;
    private ProgressDialog pDialog;
    String json_message;
    //String link;



    String Verified_Account_Number;
    String Verified_Account_Name;
    String Verified_Billing_Address;
    String Verified_Class;
    String Verified_Town;
    String Verified_Mem_Number;
    String Verified_Sequence;

    String logged_username;

    String Fetch_Stub;
    String Fetch_Contact;


    private boolean adLoaded=false;
    private AdView bannerAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_account_number);



        getSupportActionBar().setTitle("Search by Account Number");


        txt_Account_Number = findViewById(R.id.Reg_txt_Account_Number);
        btn_Verify = findViewById(R.id.Reg_btn_Verify);

        bannerAdView = (AdView) findViewById(R.id.bannerAdView_6);

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

                // Showing a simple Toast message to user when and ad is failed to load
                //Toast.makeText (MainActivity.this, "Ad Failed to Load ", Toast.LENGTH_LONG).show();
                MobileAds.initialize(act_Search_by_Account_Number.this);

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

        logged_username = getIntent().getExtras().getString("Ver_Username_Reg");




        btn_Verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account_Number_For_Ver = txt_Account_Number.getText().toString();

                if(Account_Number_For_Ver.isEmpty()){
                    txt_Account_Number.setError("Field is Required");
                    Toast.makeText(getApplicationContext(), "Field is required", Toast.LENGTH_LONG).show();


                }else {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(act_Search_by_Account_Number.this, new String[]{
                                Manifest.permission.INTERNET,
                                Manifest.permission.ACCESS_NETWORK_STATE
                        }, 100);
                    }else{

                        if(!Account_Number_For_Ver.isEmpty() && (Account_Number_For_Ver.length() >= 10 || Account_Number_For_Ver.length() <= 12)){
                            if(Account_Number_For_Ver.length() == 10){

                                //TODO Add Dash to complete formatting
                                Account_Number_For_Ver = Account_Number_For_Ver.substring(0,2) + "-" + Account_Number_For_Ver.substring(2,6) + "-" + Account_Number_For_Ver.substring(6, Account_Number_For_Ver.length());
                                //Toast.makeText(act_Search_by_Account_Number.this, "Received Short Format and Corrected", Toast.LENGTH_LONG).show();

                                //TODO Call Search Async Function




                                //link = getString(R.string.Webhost_GetAccountInfo ) + "/" + Account_Number_For_Ver;

                                new VerifyAccount().execute();







                            }else if(Account_Number_For_Ver.length() == 12){

                                //TODO Check if formatting is correct (Positioning of Dashes)

                                if(Account_Number_For_Ver.substring(2,3).equals("-") && Account_Number_For_Ver.substring(7,8).equals("-")){
                                   //S_Account_Number = temp;
                                    //Toast.makeText(act_Search_by_Account_Number.this, "Valid Format", Toast.LENGTH_LONG).show();
                                    //TODO Call Search Async Function


                                    //link = getString(R.string.Webhost_GetAccountInfo ) + "/" + Account_Number_For_Ver;

                                    new VerifyAccount().execute();






                                }else{

                                    Toast.makeText(act_Search_by_Account_Number.this, "Invalid Positioning of Dashes", Toast.LENGTH_LONG).show();

                                }


                            }else{
                                Toast.makeText(act_Search_by_Account_Number.this, "Not a Valid BILECO Account Number", Toast.LENGTH_LONG).show();
                                //TODO Error Message Here
                            }







                        }






                    }




                }

            }
        });


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








    class VerifyAccount extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Search_by_Account_Number.this);
            pDialog.setMessage("Verifying Account Number. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            //HttpURLConnection connection = null;
            //BufferedReader reader = null;

            if(internetConnectionAvailable(10000) == true){
                try {

                    String n_link = getString(R.string.Webshost_IP) + "/AGMA_2024_API/Check_Accounts_List.php";
                    HashMap<String, String> data_1 = new HashMap<>();
                    data_1.put("Search_Account_Number", Account_Number_For_Ver);


                    URL url = new URL(n_link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(getPostDataString(data_1));
                    wr.flush();


                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    // Read Server Response
                    while((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    reader.close();

                    if (line != null){
                        JSONArray jsonArray = new JSONArray(line);
                        JSONObject obj = jsonArray.getJSONObject(0);

                        try {
                            json_message = obj.getString("message");

                            if(json_message.equals("Match Found")){


                                Verified_Account_Name = obj.getString("F_Account_Name").toString();
                                Verified_Account_Number = obj.getString("F_Account_Number").toString();
                                Verified_Billing_Address = obj.getString("F_Address").toString();
                                Verified_Class =  obj.getString("F_Class").toString();
                                Verified_Town = obj.getString("F_Town").toString();
                                Verified_Mem_Number = obj.getString("F_Mem_Number").toString();
                                Verified_Sequence = obj.getString("F_Sequence").toString();

                                json_message = "Account Verified!";

                            }else{

                                json_message = obj.getString("message").toString();

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Getting JSON Array node
                    }else {
                        json_message = "No Response from server";

                    }

                    return null;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    json_message = new String("Exception: " + e.getMessage());
                }

            }else {
                //Toast.makeText(act_Search_by_Account_Number.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                json_message = "Please check your internet connection";

            }



            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }



            if(json_message.equals("No Response from server")){

                Toast.makeText(act_Search_by_Account_Number.this, json_message, Toast.LENGTH_LONG).show();

            }else if(json_message.equals("Account number does not exists!")){

                Toast.makeText(act_Search_by_Account_Number.this, json_message , Toast.LENGTH_LONG).show();

            }else if(json_message.equals("Error 500: Internal Server Error!")){

                Toast.makeText(act_Search_by_Account_Number.this, json_message , Toast.LENGTH_LONG).show();

            }else if(json_message.equals("Account Verified!")){

                new Search_Registration_record().execute();




            }else{

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8081", "Server 1");
                finmsg = finmsg.replace("ledgertest", "");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Search_by_Account_Number.this, finmsg + "\nException 351", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Manual_Search.this,  json_message.replace(getString(R.string.Webhost_Account_Ledgertest ), "Server").replace("/", "") , Toast.LENGTH_LONG).show();
            }

        }

    }


    class Search_Registration_record extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Search_by_Account_Number.this);
            pDialog.setMessage("Checking Registration Data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            if(internetConnectionAvailable(10000) == true){
                try {





                    String link=getString(R.string.Webshost_IP ) + "/AGMA_2024_API/CheckRegistration.php";
                    HashMap<String,String> data_1 = new HashMap<>();
                    data_1.put("Search_Account_Number", Verified_Account_Number);




                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( getPostDataString(data_1) );
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new
                            InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    // Read Server Response
                    while((line = reader.readLine()) != null) {
                        sb.append(line);

                        break;
                    }
                    reader.close();
                    if (line != null){
                        JSONArray jsonArray = new JSONArray(line);
                        JSONObject obj = jsonArray.getJSONObject(0);

                        json_message = obj.getString("message");

                        if(json_message.equals("Match Found")){

                            Fetch_Stub = obj.getString("Stub_Number");
                            Fetch_Contact = obj.getString("Contact");

                        }


                    }else{
                        json_message = "No Response from server";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    json_message = new String("Exception: " + e.getMessage());
                    //Toast.makeText(getContext(), new String("Exception: " + e.getMessage()), Toast.LENGTH_LONG).show();
                }
            }else{
                json_message = "Please check your internet connection";

            }



            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if(json_message.equals("Match Found")){
                //Toast.makeText(getContext(), json_message_1 , Toast.LENGTH_LONG).show();

                Intent nextActivity = new Intent(getApplicationContext(), act_Generate_QR_Stub.class);

                nextActivity.putExtra("Reg_Account_Name", Verified_Account_Name);
                nextActivity.putExtra("Reg_Account_Number", Verified_Account_Number);
                nextActivity.putExtra("Reg_Billing_Address", Verified_Billing_Address);
                nextActivity.putExtra("Reg_Class", Verified_Class);
                nextActivity.putExtra("Reg_Stub_Number", Fetch_Stub);
                nextActivity.putExtra("Reg_Contact", Fetch_Contact);
                nextActivity.putExtra("Message", "Registered");

                startActivity(nextActivity);


                finish();


                Toast.makeText(getApplicationContext(),"Account is already registered." , Toast.LENGTH_LONG).show();




            }else if(json_message.equals("Account is Valid")){


                Intent registerActivity = new Intent(act_Search_by_Account_Number.this, act_Display_For_Reg.class);

                registerActivity.putExtra("Ver_Account_Name", Verified_Account_Name);
                registerActivity.putExtra("Ver_Account_Number", Verified_Account_Number);
                registerActivity.putExtra("Ver_Billing_Address", Verified_Billing_Address);
                registerActivity.putExtra("Ver_Class", Verified_Class);
                registerActivity.putExtra("Ver_Town", Verified_Town);
                registerActivity.putExtra("Ver_Mem_Number", Verified_Mem_Number);
                registerActivity.putExtra("Ver_Sequence", Verified_Sequence);
                registerActivity.putExtra("Ver_Username_Reg", logged_username);
                registerActivity.putExtra("Ver_Registration_Check", "Not Registered");

                startActivity(registerActivity);
                finish();

            }else if(json_message.equals("Server under maintenance. Please try again later.")){

                AlertDialog.Builder builder1 = new AlertDialog.Builder(act_Search_by_Account_Number.this);
                builder1.setMessage("Server under maintenance. Please try again later.");
                builder1.setCancelable(true);

                builder1.setNegativeButton(
                        "Okay",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();


            }else{

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Search_by_Account_Number.this, finmsg + "\nException L475", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Manual_Search.this,  json_message.replace(getString(R.string.Webshost_IP ), "Server").replace("/", "") , Toast.LENGTH_LONG).show();
            }
        }

    }



    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
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