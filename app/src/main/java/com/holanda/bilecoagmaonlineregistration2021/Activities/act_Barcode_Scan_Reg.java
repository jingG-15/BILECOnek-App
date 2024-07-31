package com.holanda.bilecoagmaonlineregistration2021.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import com.google.zxing.integration.android.IntentResult;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static java.lang.Boolean.TRUE;

public class act_Barcode_Scan_Reg extends AppCompatActivity implements ZXingScannerView.ResultHandler {


//    private SurfaceView surfaceView;
//    private BarcodeDetector barcodeDetector;
//    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
//    private ToneGenerator toneGen1;
//    private TextView barcodeText;
//    private String barcodeData;


    String Account_Number_For_Search;
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
    String Fetch_Stub;
    String Fetch_Contact;

    Boolean isCheckerRunning = false;

    ZXingScannerView mScannerView;

    private boolean adLoaded=false;
    private AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scan_reg);

        getSupportActionBar().setTitle("Scan Power Bill Bar Code");
//        toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
//        surfaceView = findViewById(R.id.surface_view);
//        barcodeText = findViewById(R.id.barcode_text);


        bannerAdView = (AdView) findViewById(R.id.bannerAdView_2);

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
                MobileAds.initialize(act_Barcode_Scan_Reg.this);

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


        //new IntentIntegrator(this).initiateScan();

        if (ActivityCompat.checkSelfPermission(act_Barcode_Scan_Reg.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            try {

                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlayout_scanner);
                mScannerView = new ZXingScannerView(act_Barcode_Scan_Reg.this);   // Programmatically initialize the scanner view<br />
                rl.addView(mScannerView);
                mScannerView.setResultHandler(act_Barcode_Scan_Reg.this); // Register ourselves as a handler for scan results.<br />
                List<BarcodeFormat> myformat = new ArrayList<>();
                myformat.add(BarcodeFormat.EAN_13);
                myformat.add(BarcodeFormat.EAN_8);
                myformat.add(BarcodeFormat.RSS_14);
                myformat.add(BarcodeFormat.CODE_39);
                myformat.add(BarcodeFormat.CODE_93);
                myformat.add(BarcodeFormat.CODE_128);
                myformat.add(BarcodeFormat.ITF);
                myformat.add(BarcodeFormat.CODABAR);
                myformat.add(BarcodeFormat.DATA_MATRIX);
                myformat.add(BarcodeFormat.PDF_417);

                mScannerView.setFormats(myformat);
                mScannerView.startCamera();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(act_Barcode_Scan_Reg.this, new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }




        //initialiseDetectorsAndSources();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                try {

                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlayout_scanner);
                    mScannerView = new ZXingScannerView(act_Barcode_Scan_Reg.this);   // Programmatically initialize the scanner view<br />
                    rl.addView(mScannerView);
                    mScannerView.setResultHandler(act_Barcode_Scan_Reg.this); // Register ourselves as a handler for scan results.<br />
                    List<BarcodeFormat> myformat = new ArrayList<>();
                    myformat.add(BarcodeFormat.EAN_13);
                    myformat.add(BarcodeFormat.EAN_8);
                    myformat.add(BarcodeFormat.RSS_14);
                    myformat.add(BarcodeFormat.CODE_39);
                    myformat.add(BarcodeFormat.CODE_93);
                    myformat.add(BarcodeFormat.CODE_128);
                    myformat.add(BarcodeFormat.ITF);
                    myformat.add(BarcodeFormat.CODABAR);
                    myformat.add(BarcodeFormat.DATA_MATRIX);
                    myformat.add(BarcodeFormat.PDF_417);

                    mScannerView.setFormats(myformat);
                    mScannerView.startCamera();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }



            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }


    }






    @Override
    protected void onPause() {
        super.onPause();
        getSupportActionBar().hide();
        //cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportActionBar().hide();


        mScannerView.resumeCameraPreview(this);

        //initialiseDetectorsAndSources();
    }

    @Override
    public void handleResult(Result result) {

        Log.e("", result.getText()); // Prints scan results<br />
        Log.e("", result.getBarcodeFormat().toString());

        //Toast.makeText(act_Barcode_Scan_Reg.this, "" + result.getText() + "\n" + result.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
        tg.startTone(ToneGenerator.TONE_PROP_BEEP);

        Account_Number_For_Search = result.getText();
        Toast.makeText(this, "Scanned: " + Account_Number_For_Search, Toast.LENGTH_LONG).show();

//        mScannerView.resumeCameraPreview(act_Barcode_Scan_Reg.this);


        if(!Account_Number_For_Search.isEmpty() && (Account_Number_For_Search.length() >= 10 || Account_Number_For_Search.length() <= 12)){
            if(Account_Number_For_Search.length() == 10){

                //TODO Add Dash to complete formatting
                Account_Number_For_Search = Account_Number_For_Search.substring(0,2) + "-" + Account_Number_For_Search.substring(2,6) + "-" + Account_Number_For_Search.substring(6, Account_Number_For_Search.length());
                //Toast.makeText(act_Barcode_Scan_Reg.this, "Received Short Format and Corrected", Toast.LENGTH_LONG).show();

                //TODO Call Search Async Function


                //link = getString(R.string.Webhost_GetAccountInfo ) + "/" + Account_Number_For_Search;
                new Verify_Account_Number().execute();


            }else if(Account_Number_For_Search.length() == 12){

                //TODO Check if formatting is correct (Positioning of Dashes)

                if(Account_Number_For_Search.substring(2,3).equals("-") && Account_Number_For_Search.substring(7,8).equals("-")){
                    //S_Account_Number = temp;
                    //Toast.makeText(act_Barcode_Scan_Reg.this, "Valid Format", Toast.LENGTH_LONG).show();
                    //TODO Call Search Async Function




                    //link = getString(R.string.Webhost_GetAccountInfo ) + "/" + Account_Number_For_Search;
                    new Verify_Account_Number().execute();





                }else{

                    Toast.makeText(act_Barcode_Scan_Reg.this, "Invalid Positioning of Dashes", Toast.LENGTH_LONG).show();
                    mScannerView.resumeCameraPreview(act_Barcode_Scan_Reg.this);

                }


            }else{
                Toast.makeText(act_Barcode_Scan_Reg.this, "Not a Valid BILECO Account Number", Toast.LENGTH_LONG).show();
                mScannerView.resumeCameraPreview(act_Barcode_Scan_Reg.this);
                //TODO Error Message Here
            }

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
        } catch (InterruptedException | TimeoutException | ExecutionException ignored) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }



    class Verify_Account_Number extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isCheckerRunning = true;
            pDialog = new ProgressDialog(act_Barcode_Scan_Reg.this);
            pDialog.setMessage("Verifying Account Number. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }


        @SuppressLint("ClickableViewAccessibility")
        protected String doInBackground(String... args) {




            if(internetConnectionAvailable(10000) == true){
                try {

                    String n_link = getString(R.string.Webshost_IP) + "/AGMA_2024_API/Check_Accounts_List.php";
                    HashMap<String, String> data_1 = new HashMap<>();
                    data_1.put("Search_Account_Number", Account_Number_For_Search);


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
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    reader.close();

                    if (line != null) {

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
                    } else {
                        json_message = "No Response from server";

                    }

                    return null;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    json_message = new String("Exception: " + e.getMessage());
                }

            }else {
                //Toast.makeText(act_Barcode_Scan_Reg.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
                json_message = "Please check your internet connection";

            }



            return null;

        }


        /**
         * After completing background task Dismiss the progress dialog
         **/


        @SuppressLint("ClickableViewAccessibility")
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }


            if (json_message.equals("No Response from server")) {


                mScannerView.resumeCameraPreview(act_Barcode_Scan_Reg.this);

                isCheckerRunning =  false;

                Toast.makeText(act_Barcode_Scan_Reg.this, json_message, Toast.LENGTH_LONG).show();

            } else if (json_message.equals("Account number does not exists!")) {


                mScannerView.resumeCameraPreview(act_Barcode_Scan_Reg.this);


                isCheckerRunning =  false;

                Toast.makeText(act_Barcode_Scan_Reg.this, json_message, Toast.LENGTH_LONG).show();

            } else if (json_message.equals("Error 500: Internal Server Error!")) {


                mScannerView.resumeCameraPreview(act_Barcode_Scan_Reg.this);


                isCheckerRunning =  false;

                Toast.makeText(act_Barcode_Scan_Reg.this, json_message, Toast.LENGTH_LONG).show();

            } else if (json_message.equals("Account Verified!")) {

                new Search_Registration_record().execute();


            } else {


                mScannerView.resumeCameraPreview(act_Barcode_Scan_Reg.this);


                isCheckerRunning =  false;

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("ledgertest", "");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Barcode_Scan_Reg.this, finmsg + "\nException L485", Toast.LENGTH_LONG).show();

                //Toast.makeText(act_Barcode_Scan_Reg.this, "Server Error!\nException: 397", Toast.LENGTH_LONG).show();
            }

        }
    }


    class Search_Registration_record extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Barcode_Scan_Reg.this);
            pDialog.setMessage("Checking Registration Data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            try {


                String link = getString(R.string.Webshost_IP) + "/AGMA_2024_API/CheckRegistration.php";
                HashMap<String, String> data_1 = new HashMap<>();
                data_1.put("Search_Account_Number", Verified_Account_Number);


                URL url = new URL(link);
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
                while ((line = reader.readLine()) != null) {
                    sb.append(line);

                    break;
                }
                reader.close();
                if (line != null) {
                    JSONArray jsonArray = new JSONArray(line);
                    JSONObject obj = jsonArray.getJSONObject(0);

                    json_message = obj.getString("message");

                    if(json_message.equals("Match Found")){

                        Fetch_Stub = obj.getString("Stub_Number");
                        Fetch_Contact = obj.getString("Contact");

                    }


                } else {
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
            if (json_message.equals("Match Found")) {







                isCheckerRunning =  false;
                Toast.makeText(act_Barcode_Scan_Reg.this, "Account is already registered.", Toast.LENGTH_LONG).show();


                Intent intent = new Intent();

                intent.putExtra("Reg_Account_Name", Verified_Account_Name);
                intent.putExtra("Reg_Account_Number", Verified_Account_Number);
                intent.putExtra("Reg_Billing_Address", Verified_Billing_Address);
                intent.putExtra("Reg_Class", Verified_Class);
                intent.putExtra("Reg_Stub_Number", Fetch_Stub);
                intent.putExtra("Reg_Contact", Fetch_Contact);
                intent.putExtra("Reg_Registration_Check", "Registered");
                setResult(505, intent);
                finish();



            } else if (json_message.equals("Account is Valid")) {





                isCheckerRunning =  false;
                Toast.makeText(act_Barcode_Scan_Reg.this, "Account is Valid.", Toast.LENGTH_LONG).show();



                Intent intent = new Intent();

                intent.putExtra("Reg_Account_Name", Verified_Account_Name);
                intent.putExtra("Reg_Account_Number", Verified_Account_Number);
                intent.putExtra("Reg_Billing_Address", Verified_Billing_Address);
                intent.putExtra("Reg_Class", Verified_Class);
                intent.putExtra("Reg_Town", Verified_Town);
                intent.putExtra("Reg_Mem_Number", Verified_Mem_Number);
                intent.putExtra("Reg_Sequence", Verified_Sequence);
                intent.putExtra("Reg_Registration_Check", "Not Registered");
                setResult(RESULT_OK, intent);
                finish();




            } else {


                mScannerView.resumeCameraPreview(act_Barcode_Scan_Reg.this);


                isCheckerRunning =  false;

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Barcode_Scan_Reg.this, finmsg + "\nException L682", Toast.LENGTH_LONG).show();



                //Toast.makeText(act_Barcode_Scan_Reg.this, json_message.replace(getString(R.string.Webshost_IP), "Server").replace("/", ""), Toast.LENGTH_LONG).show();
                //Toast.makeText(act_Barcode_Scan_Reg.this, "Server Error!\nException: 584", Toast.LENGTH_LONG).show();
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
