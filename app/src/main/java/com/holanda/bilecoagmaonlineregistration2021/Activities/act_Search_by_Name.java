package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import java.net.MalformedURLException;
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
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class act_Search_by_Name extends AppCompatActivity {


    EditText txtin_NameSearch;
    Button btnSearch;


    String Search_Term, Search_Dialog_Account_Number;

    String Diag_Disp_Account_Number, Diag_Disp_Account_Name, Diag_Disp_Account_Class, Diag_Disp_Address, Diag_Disp_Town;


    private ProgressDialog pDialog;

    String json_message;

    ListView NameList_View;

    //String url_link;

    String logged_username;

    String  Fetch_Stub;
    String Fetch_Contact;


    ArrayList<HashMap<String, String>> Resulting_List;


    private boolean adLoaded=false;
    private AdView bannerAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_name);
        getSupportActionBar().setTitle("Enter Name");

        bannerAdView = (AdView) findViewById(R.id.bannerAdView_9);

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
                MobileAds.initialize(act_Search_by_Name.this);

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








        txtin_NameSearch = findViewById(R.id.Name_txtin_Name_Search);
        btnSearch = findViewById(R.id.Name_btn_Search);
        NameList_View = findViewById(R.id.lst_Names_List);

        logged_username = getIntent().getExtras().getString("Ver_Username_Reg");

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!txtin_NameSearch.getText().toString().equals("")){
                    Search_Term = txtin_NameSearch.getText().toString();
                    Search_Term = parseSearchString(Search_Term);


                    //link = getString(R.string.Webhost_GetAccountMatches ) + "/" + parseSearchString(Search_Term) + "/1" ;

                    Resulting_List = new ArrayList<>();


                    new Search_Name_Result().execute();








                }else{
                    Toast.makeText(act_Search_by_Name.this, "Account Name must not be empty", Toast.LENGTH_LONG).show();
                    txtin_NameSearch.setError("Account Name must not be empty");

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



    private String parseSearchString(String Term){


        Term = Term.replace(" ", "%20");
        Term = Term.replace("<", "%3C");
        Term = Term.replace(">", "%3E");
        Term = Term.replace("#", "%23");
        Term = Term.replace("%", "%25");
        Term = Term.replace("{", "%7B");
        Term = Term.replace("}", "%7D");
        Term = Term.replace("|", "%7C");
        Term = Term.replace("\\", "%5C");
        Term = Term.replace("^", "%5E");
        Term = Term.replace("~", "%7E");
        Term = Term.replace("[", "%5B");
        Term = Term.replace("]", "%5D");
        Term = Term.replace("`", "%60");
        Term = Term.replace(";", "%3B");
        Term = Term.replace("/", "%2F");
        Term = Term.replace("?", "%3F");
        Term = Term.replace(":", "%3A");
        Term = Term.replace("@", "%40");
        Term = Term.replace("=", "%3D");
        Term = Term.replace("&", "%26");
        Term = Term.replace("$", "%24");
        Term = Term.replace("\n", "%0A");
        Term = Term.replace("\r", "%0D");

        return Term;
    }


    class Search_Name_Result extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Search_by_Name.this);
            pDialog.setMessage("Searching. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {




            if(internetConnectionAvailable(10000) == true){

                try {
                    String n_link = getString(R.string.Webshost_IP) + "/AGMA_2024_API/Get_Accounts_List_by_Name.php";
                    HashMap<String, String> data_1 = new HashMap<>();
                    data_1.put("Search_Account_Name", Search_Term);


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
                        JSONObject jsonObj = new JSONObject(line);

                        json_message = "Init";

                        JSONArray arr_result = jsonObj.getJSONArray("MatchResult");

                        if(arr_result.length() > 0){

                            json_message = "Result Valid";

                            for (int i = 0; i < arr_result.length(); i++) {

                                JSONObject c = arr_result.getJSONObject(i);

                                HashMap<String, String> result_arr = new HashMap<>();

                                result_arr.put("Account_Number", c.getString("Account_Number"));
                                result_arr.put("Account_Name", c.getString(" Account_Name"));

                                // adding contact to contact list
                                Resulting_List.add(result_arr);


                            }

                        }else{
                            json_message = "No Entries Found";

                        }




                    }else {
                        json_message = "No Response from server";

                    }

                    return null;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    json_message = new String("Exception: " + e.getMessage());
                }

            }else {

                //Toast.makeText(act_Search_by_Name.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                json_message = "Please check your internet connection";

            }


            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }



            if(json_message.equals("Init")){

                Toast.makeText(act_Search_by_Name.this, json_message, Toast.LENGTH_LONG).show();

            }else if(json_message.equals("Result Valid")){

                ListAdapter adapter = new SimpleAdapter(
                        act_Search_by_Name.this, Resulting_List,
                        R.layout.list_name_result, new String[]{"Account_Number", "Account_Name"}, new int[]{R.id.list_nameres_txt_Account_Number,
                        R.id.list_nameres_txt_Name}){

                };

                NameList_View.setAdapter(adapter );
                NameList_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HashMap<String, String> selected_entry = Resulting_List.get(position);
                        //Toast.makeText(MainActivity.getContextOfApplication(), "ID Selected : " + selectedmovie.get("ID"),   Toast.LENGTH_LONG).show();


                        Search_Dialog_Account_Number = selected_entry.get("Account_Number");
                        //url_link = getString(R.string.Webhost_GetAccountInfo ) + "/" + Search_Dialog_Account_Number.trim();
                        //Toast.makeText(act_Search_by_Name.this, Search_Dialog_Account_Number, Toast.LENGTH_LONG).show();
                        new Get_Account_Info().execute();


                    }
                });






            }else if(json_message.equals("No Entries Found")){

                Toast.makeText(act_Search_by_Name.this, json_message , Toast.LENGTH_LONG).show();

            }else if(json_message.equals("No Response from server")){

                Toast.makeText(act_Search_by_Name.this, json_message , Toast.LENGTH_LONG).show();




            }else{

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8081", "Server 1");
                finmsg = finmsg.replace("ledgertest", "");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Search_by_Name.this, finmsg + "\nException 351", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Manual_Search.this,  json_message.replace(getString(R.string.Webhost_Account_Ledgertest ), "Server").replace("/", "") , Toast.LENGTH_LONG).show();
            }

        }

    }








    class Get_Account_Info extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Search_by_Name.this);
            pDialog.setMessage("Loading info. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {



            if(internetConnectionAvailable(10000) == true){
                try {

                    String n_link = getString(R.string.Webshost_IP) + "/AGMA_2024_API/Check_Accounts_List.php";
                    HashMap<String, String> data_1 = new HashMap<>();
                    data_1.put("Search_Account_Number", Search_Dialog_Account_Number.trim());


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

                    if (!(line == null)){
                        JSONArray jsonArray = new JSONArray(line);
                        JSONObject obj = jsonArray.getJSONObject(0);

                        try {
                            json_message = obj.getString("message");

                            if(json_message.equals("Match Found")){

                                Diag_Disp_Account_Name = obj.getString("F_Account_Name").toString();
                                Diag_Disp_Account_Number = obj.getString("F_Account_Number").toString();
                                Diag_Disp_Address = obj.getString("F_Address").toString();
                                Diag_Disp_Account_Class =  obj.getString("F_Class").toString();
                                Diag_Disp_Town = obj.getString("F_Town").toString();

                                json_message = "Account Verified!";

                            }else{

                                json_message = obj.getString("message").toString();

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }else {
                        json_message = "No Response from server";

                    }

                    return null;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    json_message = new String("Exception: " + e.getMessage());
                }

            }else{
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

                Toast.makeText(act_Search_by_Name.this, json_message, Toast.LENGTH_LONG).show();

            }else if(json_message.equals("Account number does not exists!")){

                Toast.makeText(act_Search_by_Name.this, json_message , Toast.LENGTH_LONG).show();

            }else if(json_message.equals("Data Loaded")){


                showCustomDialog(Diag_Disp_Account_Name, Diag_Disp_Account_Number, Diag_Disp_Account_Class, Diag_Disp_Address);


            }else{

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8081", "Server 1");
                finmsg = finmsg.replace("ledgertest", "");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Search_by_Name.this, finmsg + "\nException 351", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Manual_Search.this,  json_message.replace(getString(R.string.Webhost_Account_Ledgertest ), "Server").replace("/", "") , Toast.LENGTH_LONG).show();
            }

        }

    }


    private void showCustomDialog(String...args) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_account_info, viewGroup, false);

        TextView D_Account_Name = dialogView.findViewById(R.id.dialog_result_account_name);
        TextView D_Account_Number = dialogView.findViewById(R.id.dialog_result_account_number);
        TextView D_Class = dialogView.findViewById(R.id.dialog_result_account_class);
        TextView D_Address = dialogView.findViewById(R.id.dialog_result_address);

        D_Account_Name.setText(args[0]);
        D_Account_Number.setText(args[1]);
        D_Class.setText(args[2]);
        D_Address.setText(args[3]);

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {



            }
        });

        builder.setPositiveButton("Register", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new Search_Registration_record().execute();


            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    class Search_Registration_record extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Search_by_Name.this);
            pDialog.setMessage("Checking Registration Data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            try {





                String link=getString(R.string.Webshost_IP ) + "/AGMA_2024_API/CheckRegistration.php";
                HashMap<String,String> data_1 = new HashMap<>();
                data_1.put("Search_Account_Number", Diag_Disp_Account_Number);




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

                nextActivity.putExtra("Reg_Account_Name", Diag_Disp_Account_Name);
                nextActivity.putExtra("Reg_Account_Number", Diag_Disp_Account_Number);
                nextActivity.putExtra("Reg_Billing_Address", Diag_Disp_Address);
                nextActivity.putExtra("Reg_Class", Diag_Disp_Account_Class);
                nextActivity.putExtra("Reg_Stub_Number", Fetch_Stub);
                nextActivity.putExtra("Reg_Contact", Fetch_Contact);
                nextActivity.putExtra("Message", "Registered");

                startActivity(nextActivity);


                finish();


                Toast.makeText(getApplicationContext(),"Account is already registered." , Toast.LENGTH_LONG).show();




            }else if(json_message.equals("Account is Valid")){


                Intent nextActivity = new Intent(getApplicationContext(), act_Contact_Number.class);

                nextActivity.putExtra("Reg_Account_Name", Diag_Disp_Account_Name);
                nextActivity.putExtra("Reg_Account_Number", Diag_Disp_Account_Number);
                nextActivity.putExtra("Reg_Billing_Address", Diag_Disp_Address);
                nextActivity.putExtra("Reg_Class", Diag_Disp_Account_Class);
                nextActivity.putExtra("Reg_Username", logged_username);
                nextActivity.putExtra("Reg_Town", Diag_Disp_Town);

                startActivity(nextActivity);
                finish();

            }else{

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Search_by_Name.this, finmsg + "\nException L475", Toast.LENGTH_LONG).show();


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