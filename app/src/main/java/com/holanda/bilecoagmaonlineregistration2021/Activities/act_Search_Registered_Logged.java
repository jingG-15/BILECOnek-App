package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class act_Search_Registered_Logged extends AppCompatActivity {

    private boolean adLoaded=false;
    private AdView bannerAdView;

    private ProgressDialog pDialog;

    String logged_username, json_message, Search_Term ;

    SwipeRefreshLayout List_regs_Layout;

    EditText txtin_NameSearch;
    Button btnSearch;
    ListView NameList_View;


    ArrayList<HashMap<String, String>> Resulting_List;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_registered_logged);

        getSupportActionBar().setTitle("Search Registration");

        bannerAdView = (AdView) findViewById(R.id.bannerAdView_9_1);

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
                MobileAds.initialize(act_Search_Registered_Logged.this);

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



        txtin_NameSearch = findViewById(R.id.Regs_txtin_Name_Search);
        btnSearch = findViewById(R.id.Regs_btn_Search);
        NameList_View = findViewById(R.id.lst_Regs_List);

        List_regs_Layout = findViewById(R.id.Regs_List_Swipe_Layout);

        logged_username = getIntent().getExtras().getString("Log_Username");

        List_regs_Layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                Resulting_List = new ArrayList<>();
                new Load_Registered_by_User().execute();

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!txtin_NameSearch.getText().toString().equals("")){
                    Search_Term = txtin_NameSearch.getText().toString();



                    Resulting_List = new ArrayList<>();
                    new Search_Registered_by_User().execute();





                }else{
                    Toast.makeText(act_Search_Registered_Logged.this, "Account Name must not be empty", Toast.LENGTH_LONG).show();
                    txtin_NameSearch.setError("Account Name must not be empty");

                }






            }
        });







        Resulting_List = new ArrayList<>();
        new Load_Registered_by_User().execute();


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



    class Search_Registered_by_User extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Search_Registered_Logged.this);
            pDialog.setMessage("Searching Data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            if(internetConnectionAvailable(10000) == true){
                try {
                    String link=getString(R.string.Webshost_IP ) + "/AGMA_2024_API/SearchRegistrationList.php";

                    HashMap<String,String> data_1 = new HashMap<>();
                    data_1.put("Username", logged_username);
                    data_1.put("Search_Term", Search_Term);

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( getPostDataString(data_1) );
                    wr.flush();

                    reader = new BufferedReader(new
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
                        JSONObject jsonObj = new JSONObject(line);
                        if (jsonObj.getString("Reg_List").equals("No Records Found.")){
                            json_message = "No Records Found.";

                        }else{
                            JSONArray Reg_Json = jsonObj.getJSONArray("Reg_List");
                            for (int i = 0; i < Reg_Json.length(); i++) {

                                JSONObject c = Reg_Json.getJSONObject(i);


                                HashMap<String, String> result_arr = new HashMap<>();

                                // adding each child node to HashMap key => value

                                result_arr.put("Account_Number", c.getString("Bil_Account_Number"));
                                result_arr.put("Account_Name", c.getString("Bil_Account_Name"));
                                result_arr.put("Stub_Number", c.getString("Stub_Number"));
                                result_arr.put("Address", c.getString("Bil_Address"));
                                result_arr.put("Class", c.getString("Bil_Class"));
                                result_arr.put("Contact_Number", c.getString("Contact_Number"));
                                result_arr.put("Date_Registered", c.getString("Date_Registered"));

                                // adding contact to contact list
                                Resulting_List.add(result_arr);



                            }


                            //Listview Process Trigger Here

                            json_message = "List Loaded";
                        }
                        // Getting JSON Array node

                    }else if(line.equals("No Records Found.")){
                        json_message = "No Records Found.";

                    }else {
                        json_message = "No Response from server";

                    }

                    return null;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    json_message = new String("Exception: " + e.getMessage());
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        json_message = new String("Exception: " + e.getMessage());
                    }
                }

            }else {
                //Toast.makeText(act_Search_Registered_Logged.this, "Please check your internet connection", Toast.LENGTH_LONG).show();

                json_message = "Please check your internet connection";


            }



            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }




            if(json_message.equals("No Records Found.")){

                Toast.makeText(act_Search_Registered_Logged.this, json_message, Toast.LENGTH_LONG).show();

            }else if(json_message.equals("List Loaded")){

                ListAdapter adapter = new SimpleAdapter(
                        act_Search_Registered_Logged.this, Resulting_List,
                        R.layout.list_name_result, new String[]{"Account_Number", "Account_Name"}, new int[]{R.id.list_nameres_txt_Account_Number,
                        R.id.list_nameres_txt_Name}){

                };

                NameList_View.setAdapter(adapter );
                NameList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, String> selected_entry = Resulting_List.get(position);
                        //Toast.makeText(MainActivity.getContextOfApplication(), "ID Selected : " + selectedmovie.get("ID"),   Toast.LENGTH_LONG).show();


                        showCustomDialog(selected_entry.get("Account_Number"), selected_entry.get("Account_Name"),
                                selected_entry.get("Stub_Number"), selected_entry.get("Address"), selected_entry.get("Class"),
                                selected_entry.get("Contact_Number"), selected_entry.get("Date_Registered"));


                    }
                });






            }else if(json_message.equals("No Response from server")){

                Toast.makeText(act_Search_Registered_Logged.this, json_message , Toast.LENGTH_LONG).show();


            }else{

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8081", "Server 1");
                finmsg = finmsg.replace("ledgertest", "");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Search_Registered_Logged.this, finmsg + "\nException 351", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Manual_Search.this,  json_message.replace(getString(R.string.Webhost_Account_Ledgertest ), "Server").replace("/", "") , Toast.LENGTH_LONG).show();
            }

        }

    }



    class Load_Registered_by_User extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Search_Registered_Logged.this);
            pDialog.setMessage("Loading Data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;

            if(internetConnectionAvailable(10000) == true){
                try {
                    String link=getString(R.string.Webshost_IP ) + "/AGMA_2024_API/LoadRegistrationList.php";

                    HashMap<String,String> data_1 = new HashMap<>();
                    data_1.put("Username", logged_username);

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write( getPostDataString(data_1) );
                    wr.flush();

                    reader = new BufferedReader(new
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
                        JSONObject jsonObj = new JSONObject(line);
                        if (jsonObj.getString("Reg_List").equals("No Records Found.")){
                            json_message = "No Records Found.";

                        }else{
                            JSONArray Reg_Json = jsonObj.getJSONArray("Reg_List");
                            for (int i = 0; i < Reg_Json.length(); i++) {

                                JSONObject c = Reg_Json.getJSONObject(i);


                                HashMap<String, String> result_arr = new HashMap<>();

                                // adding each child node to HashMap key => value

                                result_arr.put("Account_Number", c.getString("Bil_Account_Number"));
                                result_arr.put("Account_Name", c.getString("Bil_Account_Name"));
                                result_arr.put("Stub_Number", c.getString("Stub_Number"));
                                result_arr.put("Address", c.getString("Bil_Address"));
                                result_arr.put("Class", c.getString("Bil_Class"));
                                result_arr.put("Contact_Number", c.getString("Contact_Number"));
                                result_arr.put("Date_Registered", c.getString("Date_Registered"));

                                // adding contact to contact list
                                Resulting_List.add(result_arr);



                            }


                            //Listview Process Trigger Here

                            json_message = "List Loaded";
                        }
                        // Getting JSON Array node

                    }else if(line.equals("No Records Found.")){
                        json_message = "No Records Found.";

                    }else {
                        json_message = "No Response from server";

                    }

                    return null;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    json_message = new String("Exception: " + e.getMessage());
                } finally {
                    try {
                        if (reader != null) {
                            reader.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        json_message = new String("Exception: " + e.getMessage());
                    }
                }

            }else{

                json_message = "Please check your internet connection.";
            }



            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }

            List_regs_Layout.setRefreshing(false);

            if(json_message.equals("No Records Found.")){

                Toast.makeText(act_Search_Registered_Logged.this, json_message, Toast.LENGTH_LONG).show();

            }else if(json_message.equals("List Loaded")){

                ListAdapter adapter = new SimpleAdapter(
                        act_Search_Registered_Logged.this, Resulting_List,
                        R.layout.list_name_result, new String[]{"Account_Number", "Account_Name"}, new int[]{R.id.list_nameres_txt_Account_Number,
                        R.id.list_nameres_txt_Name}){

                };

                NameList_View.setAdapter(adapter );
                NameList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, String> selected_entry = Resulting_List.get(position);
                        //Toast.makeText(MainActivity.getContextOfApplication(), "ID Selected : " + selectedmovie.get("ID"),   Toast.LENGTH_LONG).show();


                        showCustomDialog(selected_entry.get("Account_Number"), selected_entry.get("Account_Name"),
                                selected_entry.get("Stub_Number"), selected_entry.get("Address"), selected_entry.get("Class"),
                                selected_entry.get("Contact_Number"), selected_entry.get("Date_Registered"));


                    }
                });






            }else if(json_message.equals("No Response from server")){

                Toast.makeText(act_Search_Registered_Logged.this, json_message , Toast.LENGTH_LONG).show();


            }else{

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("ledgertest", "");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Search_Registered_Logged.this, finmsg + "\nException 351", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Manual_Search.this,  json_message.replace(getString(R.string.Webhost_Account_Ledgertest ), "Server").replace("/", "") , Toast.LENGTH_LONG).show();
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


    private void showCustomDialog(String...args) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_registration_info, viewGroup, false);


        TextView D_Account_Name = dialogView.findViewById(R.id.dialog_reg_Account_Name);
        TextView D_Account_Number = dialogView.findViewById(R.id.dialog_reg_Account_Number);
        TextView D_Stub_Number = dialogView.findViewById(R.id.dialog_reg_Stub_Number);
        TextView D_Address = dialogView.findViewById(R.id.dialog_reg_Address);
        TextView D_Class = dialogView.findViewById(R.id.dialog_reg_Account_Class);
        TextView D_Contact_Number = dialogView.findViewById(R.id.dialog_reg_Contact);
        TextView D_Date_Registered = dialogView.findViewById(R.id.dialog_reg_Date_Reg);

        D_Account_Name.setText(args[0]);
        D_Account_Number.setText(args[1]);
        D_Stub_Number.setText(args[2]);
        D_Address.setText(args[3]);
        D_Class.setText(args[4]);
        D_Contact_Number.setText(args[5]);
        D_Date_Registered.setText(args[6]);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



            }
        });

        builder.setPositiveButton("View Stub", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent nextActivity = new Intent(getApplicationContext(), act_Generate_QR_Stub.class);

                nextActivity.putExtra("Reg_Account_Name", args[1]);
                nextActivity.putExtra("Reg_Account_Number", args[0]);
                nextActivity.putExtra("Reg_Billing_Address", args[3]);
                nextActivity.putExtra("Reg_Class", args[4]);
                nextActivity.putExtra("Reg_Stub_Number", args[2]);
                nextActivity.putExtra("Reg_Contact", args[5]);
                nextActivity.putExtra("Message", "Registered");

                startActivity(nextActivity);

            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}