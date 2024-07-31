package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.holanda.bilecoagmaonlineregistration2021.R;

import java.util.Objects;

public class act_Main_Menu extends AppCompatActivity {


    static int UPDATE_REQUEST = 115;
    static int INTERNET_SCAN_QR_REQUEST = 116;
    static int INTERNET_SCAN_BARCODE_REQUEST = 117;
    static int INTERNET_LOGIN_REQUEST = 118;
    static int INTERNET_LOGOUT_REQUEST = 119;
    static int INTERNET_MANUAL_SEARCH_REQUEST = 120;

    static int BARCODE_OPEN_REQUEST = 101;
    static int OPEN_LOGIN_REQ = 102;
    static int OPEN_PRIV_REQ = 103;
    static int INTERNET_LOGIN_REQUEST_2 = 104;

    private boolean adLoaded=false;
    private AdView bannerAdView;
    

    ImageButton btnScan, btnStats, btnLogIn, btnLogOut, btnManualSearch, btnBILECOLive, btnViewRegistered, btnViewAdminReg, btnScanQR, btnCapSign;
    TextView txtLogInState, txtLoginLabel, txtLogoutLabel, txtManualLabel, txtStatsLabel, txtScanLabel, txtViewRegLabel, txtViewAdminRegLabel, txtScanQRLabel, txtCapOffSignLbl;

    String logged_username, logged_first_name, logged_middle_initial, logged_last_name, logged_position, logged_contact_number, logged_address;

    AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_BILECOAGMAOnlineRegistration2021);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

//        getSupportActionBar().setTitle("BILECOnek Mobile");
        Objects.requireNonNull(getSupportActionBar()).hide();


        bannerAdView = (AdView) findViewById(R.id.bannerAdView_Main);

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
                MobileAds.initialize(act_Main_Menu.this);

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


        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // For a flexible update, use AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            UPDATE_REQUEST);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            }
        });




        btnLogIn = findViewById(R.id.main_img_log_in);
        btnLogOut = findViewById(R.id.main_img_log_out);
        btnManualSearch = findViewById(R.id.main_img_manual_search);
        btnScan = findViewById(R.id.main_img_scan_Bar_Code);
        btnStats = findViewById(R.id.main_img_stats);
        btnBILECOLive = findViewById(R.id.main_img_facebook_live);
        btnViewRegistered = findViewById(R.id.main_img_registered_list);
        btnViewAdminReg = findViewById(R.id.main_img_admin_stats);
        btnScanQR = findViewById(R.id.main_img_QR_Scanner);
        btnCapSign = findViewById(R.id.main_img_Capture_Offline_Signature);

        txtLogInState = findViewById(R.id.main_txt_log_State);
        txtScanLabel = findViewById(R.id.main_txt_Scan_Label);
        txtManualLabel = findViewById(R.id.main_txt_Manual_Label);
        txtStatsLabel = findViewById(R.id.main_txt_Stats_Label);
        txtLoginLabel = findViewById(R.id.main_txt_Login_Label);
        txtLogoutLabel = findViewById(R.id.main_txt_Logout_Label);
        txtViewRegLabel = findViewById(R.id.main_txt_registered_Label);
        txtViewAdminRegLabel = findViewById(R.id.main_txt_admin_stats_Label);
        txtScanQRLabel = findViewById(R.id.main_txt_QR_Scanner_Label);
        txtCapOffSignLbl = findViewById(R.id.main_txt_Cap_Off_Sign_Label);


        btnCapSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerActivity = new Intent(getApplicationContext(), act_Get_Offline_Signature.class);

                startActivity(registerActivity);



            }
        });

        btnScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(act_Main_Menu.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){


//                            Intent registerActivity = new Intent(getApplicationContext(), act_Manual_Search_Selection.class);
//
//                            registerActivity.putExtra("Ver_Username_Reg", logged_username);
//                            startActivity(registerActivity);

                    Intent registerActivity = new Intent(getApplicationContext(), act_QR_Attendance_Onsite.class);

                    registerActivity.putExtra("Ver_Username_Reg", logged_username);
                    startActivity(registerActivity);



                }else {
                    ActivityCompat.requestPermissions(act_Main_Menu.this, new String[]{
                            Manifest.permission.INTERNET
                    }, INTERNET_SCAN_QR_REQUEST);

                }

            }
        });

        btnBILECOLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AlertDialog.Builder builder = new AlertDialog.Builder(act_Main_Menu.this);
//                builder.setTitle("Please select the facebook page to visit");
//                builder.setItems(new CharSequence[]
//                                {"www.facebook.com/bilecoinc", "www.facebook.com/bilecoofficial", "Cancel"},
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                switch (which) {
//                                    case 0:
//                                        startActivity(Open_Facebook_Page(getContext().getPackageManager(), "https://www.facebook.com/bilecoinc"));
//                                        break;
//                                    case 1:
//                                        startActivity(Open_Facebook_Page(getContext().getPackageManager(), "https://www.facebook.com/bilecoofficial"));
//                                        break;
//                                    case 2:
//                                        Toast.makeText(getContext(), "Visit cancelled.", Toast.LENGTH_SHORT).show();
//                                        break;
//                                }
//                            }
//                        });
//                builder.create().show();

                //startActivity(Open_Facebook_Page(act_Main_Menu.this.getPackageManager(), "https://www.facebook.com/bilecoofficial"));
                //startActivity(Open_Facebook_Page(act_Main_Menu.this.getPackageManager(), "https://www.facebook.com/bilecoofficial/live_videos/?ref=page_internal"));

                openFacebookProfile(act_Main_Menu.this, "225845967581313" , "https://www.facebook.com/bilecoofficial/live_videos/?ref=page_internal");



            }
        });







        btnScan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(act_Main_Menu.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(act_Main_Menu.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){



                    Intent registerActivity = new Intent(getApplicationContext(), act_Barcode_Scan_Reg.class);


                    startActivityForResult(registerActivity, BARCODE_OPEN_REQUEST);

//                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
//                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
//                    startActivityForResult(intent, 300);


                }else {
                    ActivityCompat.requestPermissions(act_Main_Menu.this, new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.INTERNET
                    }, INTERNET_SCAN_BARCODE_REQUEST);


                }
            }
        });


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(act_Main_Menu.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){

                    logged_username = "Guest";
                    txtLogInState.setText("Logged in as: ");
                    Intent loggingActivity = new Intent(getApplicationContext(), act_Logging_User.class);
                    startActivityForResult(loggingActivity, OPEN_LOGIN_REQ);


                }else {

                    ActivityCompat.requestPermissions(act_Main_Menu.this, new String[]{
                            Manifest.permission.INTERNET
                    }, INTERNET_LOGIN_REQUEST);

                }

            }
        });


        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(act_Main_Menu.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){

                    logged_username = "Guest";
                    txtLogInState.setText("Logged in as: ");

                    final String PREFS_NAME = "MyPrefsFile";

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    PackageInfo pInfo = null;
                    try {
                        pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    String version = pInfo.versionName;

                    settings.edit().putBoolean("keep_me_logged_" + version, false).apply();
                    settings.edit().putString("logged_username_" + version, null ).apply();
                    settings.edit().putString("logged_first_name_" + version, null ).apply();
                    settings.edit().putString("logged_middle_initial_" + version, null ).apply();
                    settings.edit().putString("logged_last_name_" + version, null ).apply();
                    settings.edit().putString("logged_position_" + version, null).apply();
                    settings.edit().putString("logged_contact_number_" + version, null ).apply();
                    settings.edit().putString("logged_address_" + version, null ).apply();

                    Intent loggingActivity = new Intent(getApplicationContext(), act_Logging_User.class);

                    startActivityForResult(loggingActivity, OPEN_LOGIN_REQ);


                }else {

                    ActivityCompat.requestPermissions(act_Main_Menu.this, new String[]{
                            Manifest.permission.INTERNET
                    }, INTERNET_LOGOUT_REQUEST);

                }

            }
        });


        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent StatsActivity = new Intent(getApplicationContext(), act_Statistics.class);

                StatsActivity.putExtra("Log_Username", logged_username);
                StatsActivity.putExtra("Log_FullName", logged_first_name + " " + logged_middle_initial + ". " + logged_last_name);
                StatsActivity.putExtra("Log_Contact", logged_contact_number);
                StatsActivity.putExtra("Log_Address", logged_address);
                StatsActivity.putExtra("Log_Position", logged_position);

                startActivity(StatsActivity);
            }
        });

        btnViewRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent RegActivity = new Intent(getApplicationContext(), act_Search_Registered_Logged.class);

                RegActivity.putExtra("Log_Username", logged_username);

                startActivity(RegActivity);


            }
        });

        btnViewAdminReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent RegActivity = new Intent(getApplicationContext(), act_Admin_Registration_Stats.class);

                startActivity(RegActivity);

            }
        });


        btnScan.setVisibility(View.VISIBLE);
        btnScan.setClickable(false);
        btnScan.setBackgroundTintList(ColorStateList.valueOf(Color.argb(90,0,0,0 )));
        btnScan.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
        txtScanLabel.setVisibility(View.VISIBLE);

        btnManualSearch.setVisibility(View.VISIBLE);
        btnManualSearch.setClickable(false);
        btnManualSearch.setBackgroundTintList(ColorStateList.valueOf(Color.argb(90,0,0,0 )));
        btnManualSearch.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
        txtManualLabel.setVisibility(View.VISIBLE);

        btnStats.setVisibility(View.GONE);
        txtStatsLabel.setVisibility(View.GONE);

        btnViewAdminReg.setVisibility(View.GONE);
        txtViewAdminRegLabel.setVisibility(View.GONE);

        btnScanQR.setVisibility(View.GONE);
        txtScanQRLabel.setVisibility(View.GONE);

        btnCapSign.setVisibility(View.GONE);
        txtCapOffSignLbl.setVisibility(View.GONE);

        btnViewRegistered.setVisibility(View.GONE);
        txtViewRegLabel.setVisibility(View.GONE);

        btnLogIn.setVisibility(View.VISIBLE);
        txtLoginLabel.setVisibility(View.VISIBLE);

        btnLogOut.setVisibility(View.GONE);
        txtLogoutLabel.setVisibility(View.GONE);


        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        PackageInfo pInfo = null;
        try {
            pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = pInfo.versionName;

        if (settings.getBoolean("my_first_time_" + version, true)) {

            //the app is being launched for first time, do something

            Log.d("Comments", "First time");

            // first time task

            // record the fact that the app has been started at least once


            Intent Priv_Act = new Intent(getApplicationContext(), act_Privacy_Policy.class);
            startActivityForResult(Priv_Act, OPEN_PRIV_REQ);



        }else{

            if (settings.getBoolean("keep_me_logged_" + version, true)) {


                logged_username  = settings.getString("logged_username_" + version, logged_username );
                logged_first_name = settings.getString("logged_first_name_" + version, logged_first_name );
                logged_middle_initial = settings.getString("logged_middle_initial_" + version, logged_middle_initial );
                logged_last_name = settings.getString("logged_last_name_" + version, logged_last_name );
                logged_position = settings.getString("logged_position_" + version, logged_position );
                logged_contact_number = settings.getString("logged_contact_number_" + version, logged_contact_number );
                logged_address = settings.getString("logged_address_" + version, logged_address );


                btnScan.setVisibility(View.VISIBLE);
                btnScan.setClickable(true);
                btnScan.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0,0,0,0 )));
                btnScan.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                txtScanLabel.setVisibility(View.VISIBLE);

                btnStats.setVisibility(View.VISIBLE);
                txtStatsLabel.setVisibility(View.VISIBLE);

                btnManualSearch.setVisibility(View.VISIBLE);
                btnManualSearch.setClickable(true);
                btnManualSearch.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0,0,0,0 )));
                btnManualSearch.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                txtManualLabel.setVisibility(View.VISIBLE);

                if(logged_position.substring(0,6).equals("BILECO")){

                    btnViewAdminReg.setVisibility(View.VISIBLE);
                    txtViewAdminRegLabel.setVisibility(View.VISIBLE);

                    btnScanQR.setVisibility(View.VISIBLE);
                    txtScanQRLabel.setVisibility(View.VISIBLE);

                    btnCapSign.setVisibility(View.VISIBLE);
                    txtCapOffSignLbl.setVisibility(View.VISIBLE);

                }else{

                    btnViewAdminReg.setVisibility(View.GONE);
                    txtViewAdminRegLabel.setVisibility(View.GONE);

                    btnScanQR.setVisibility(View.GONE);
                    txtScanQRLabel.setVisibility(View.GONE);

                    btnCapSign.setVisibility(View.GONE);
                    txtCapOffSignLbl.setVisibility(View.GONE);

                }

                btnViewRegistered.setVisibility(View.VISIBLE);
                txtViewRegLabel.setVisibility(View.VISIBLE);

                btnLogIn.setVisibility(View.GONE);
                txtLoginLabel.setVisibility(View.GONE);

                btnLogOut.setVisibility(View.VISIBLE);
                txtLogoutLabel.setVisibility(View.VISIBLE);

                txtLogInState.setText("Logged in as: " + logged_username);

                btnManualSearch.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(act_Main_Menu.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){


//                            Intent registerActivity = new Intent(getApplicationContext(), act_Manual_Search_Selection.class);
//
//                            registerActivity.putExtra("Ver_Username_Reg", logged_username);
//                            startActivity(registerActivity);

                            Intent registerActivity = new Intent(getApplicationContext(), act_Search_by_Account_Number.class);

                            registerActivity.putExtra("Ver_Username_Reg", logged_username);
                            startActivity(registerActivity);




                        }else {
                            ActivityCompat.requestPermissions(act_Main_Menu.this, new String[]{
                                    Manifest.permission.INTERNET
                            }, INTERNET_MANUAL_SEARCH_REQUEST);

                        }

                    }
                });





            }else{
                if(logged_username == null){
                    if (ContextCompat.checkSelfPermission(act_Main_Menu.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){

                        logged_username = "Guest";
                        //txtLogInState.setText("Logged in as: " + logged_username);
                        Intent loggingActivity = new Intent(getApplicationContext(), act_Logging_User.class);
                        startActivityForResult(loggingActivity, OPEN_LOGIN_REQ);


                    }else {

                        ActivityCompat.requestPermissions(act_Main_Menu.this, new String[]{
                                Manifest.permission.INTERNET
                        }, INTERNET_LOGIN_REQUEST_2);

                    }

                }


            }


        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                try {
                                    appUpdateManager.startUpdateFlowForResult(
                                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                                            appUpdateInfo,
                                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                                            AppUpdateType.IMMEDIATE,
                                            // The current activity making the update request.
                                            this,
                                            // Include a request code to later monitor this update request.
                                            UPDATE_REQUEST);
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == INTERNET_SCAN_QR_REQUEST){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Intent registerActivity = new Intent(getApplicationContext(), act_QR_Attendance_Onsite.class);

                registerActivity.putExtra("Ver_Username_Reg", logged_username);
                startActivity(registerActivity);


            } else {


                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == INTERNET_SCAN_BARCODE_REQUEST){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent registerActivity = new Intent(getApplicationContext(), act_Barcode_Scan_Reg.class);

                startActivityForResult(registerActivity, BARCODE_OPEN_REQUEST);


            } else {

                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == INTERNET_LOGIN_REQUEST){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                logged_username = "Guest";
                txtLogInState.setText("Logged in as: ");
                Intent loggingActivity = new Intent(getApplicationContext(), act_Logging_User.class);
                startActivityForResult(loggingActivity, OPEN_LOGIN_REQ);

            } else {

                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == INTERNET_LOGOUT_REQUEST){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                logged_username = "Guest";
                txtLogInState.setText("Logged in as: ");

                final String PREFS_NAME = "MyPrefsFile";

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                String version = pInfo.versionName;

                settings.edit().putBoolean("keep_me_logged_" + version, false).apply();
                settings.edit().putString("logged_username_" + version, null ).apply();
                settings.edit().putString("logged_first_name_" + version, null ).apply();
                settings.edit().putString("logged_middle_initial_" + version, null ).apply();
                settings.edit().putString("logged_last_name_" + version, null ).apply();
                settings.edit().putString("logged_position_" + version, null).apply();
                settings.edit().putString("logged_contact_number_" + version, null ).apply();
                settings.edit().putString("logged_address_" + version, null ).apply();

                Intent loggingActivity = new Intent(getApplicationContext(), act_Logging_User.class);

                startActivityForResult(loggingActivity, OPEN_LOGIN_REQ);


            } else {

                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == INTERNET_MANUAL_SEARCH_REQUEST){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                //                      Intent registerActivity = new Intent(getApplicationContext(), act_Manual_Search_Selection.class);
//
//                            registerActivity.putExtra("Ver_Username_Reg", logged_username);
//                            startActivity(registerActivity);

                Intent registerActivity = new Intent(getApplicationContext(), act_Search_by_Account_Number.class);

                registerActivity.putExtra("Ver_Username_Reg", logged_username);
                startActivity(registerActivity);


            } else {

                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == INTERNET_LOGIN_REQUEST_2){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                logged_username = "Guest";
                //txtLogInState.setText("Logged in as: " + logged_username);
                Intent loggingActivity = new Intent(getApplicationContext(), act_Logging_User.class);
                startActivityForResult(loggingActivity, OPEN_LOGIN_REQ);




            } else {

                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BARCODE_OPEN_REQUEST) {
            if (resultCode == RESULT_OK) {


                Intent registerActivity = new Intent(act_Main_Menu.this, act_Display_For_Reg.class);

                registerActivity.putExtra("Ver_Account_Name", data.getStringExtra("Reg_Account_Name"));
                registerActivity.putExtra("Ver_Account_Number", data.getStringExtra("Reg_Account_Number"));
                registerActivity.putExtra("Ver_Billing_Address", data.getStringExtra("Reg_Billing_Address"));
                registerActivity.putExtra("Ver_Class", data.getStringExtra("Reg_Class"));
                registerActivity.putExtra("Ver_Town", data.getStringExtra("Reg_Town"));
                registerActivity.putExtra("Ver_Mem_Number", data.getStringExtra("Reg_Mem_Number"));
                registerActivity.putExtra("Ver_Sequence", data.getStringExtra("Reg_Sequence"));
                registerActivity.putExtra("Ver_Registration_Check", "Not Registered");
                registerActivity.putExtra("Ver_Username_Reg", logged_username);

                startActivity(registerActivity);


            } else if (resultCode == 505) {

//                Intent registerActivity = new Intent(act_Main_Menu.this, act_Display_For_Reg.class);
//
//                registerActivity.putExtra("Ver_Account_Name", data.getStringExtra("Reg_Account_Name"));
//                registerActivity.putExtra("Ver_Account_Number", data.getStringExtra("Reg_Account_Number"));
//                registerActivity.putExtra("Ver_Billing_Address", data.getStringExtra("Reg_Billing_Address"));
//                registerActivity.putExtra("Ver_Class", data.getStringExtra("Reg_Class"));
//                registerActivity.putExtra("Ver_Registration_Check", "Registered");
//                registerActivity.putExtra("Ver_Username_Reg", logged_username);
//
//                startActivity(registerActivity);


                Intent nextActivity = new Intent(getApplicationContext(), act_Generate_QR_Stub.class);

                nextActivity.putExtra("Reg_Account_Name", data.getStringExtra("Reg_Account_Name"));
                nextActivity.putExtra("Reg_Account_Number", data.getStringExtra("Reg_Account_Number"));
                nextActivity.putExtra("Reg_Billing_Address", data.getStringExtra("Reg_Billing_Address"));
                nextActivity.putExtra("Reg_Class", data.getStringExtra("Reg_Class"));
                nextActivity.putExtra("Reg_Stub_Number", data.getStringExtra("Reg_Stub_Number"));
                nextActivity.putExtra("Reg_Contact", data.getStringExtra("Reg_Contact"));
                nextActivity.putExtra("Message", "Registered");

                startActivity(nextActivity);


            }
        }else if(requestCode == OPEN_PRIV_REQ) {
            if (resultCode == RESULT_OK) {

                final String PREFS_NAME = "MyPrefsFile";

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                PackageInfo pInfo = null;
                try {
                    pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                String version = pInfo.versionName;

                settings.edit().putBoolean("my_first_time_" + version, false).apply();

                if (logged_username == null) {
                    if (ContextCompat.checkSelfPermission(act_Main_Menu.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

                        logged_username = "Guest";
                        //txtLogInState.setText("Logged in as: " + logged_username);
                        Intent loggingActivity = new Intent(getApplicationContext(), act_Logging_User.class);
                        startActivityForResult(loggingActivity, OPEN_LOGIN_REQ);


                    } else {

                        ActivityCompat.requestPermissions(act_Main_Menu.this, new String[]{
                                Manifest.permission.INTERNET
                        }, INTERNET_LOGIN_REQUEST_2);

                    }

                }

            } else {

                finish();

            }


        }else if (requestCode == UPDATE_REQUEST) {
            if (resultCode != RESULT_OK) {

                Toast.makeText(act_Main_Menu.this,"Update flow failed! Result code: " + resultCode, Toast.LENGTH_LONG).show();
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }


        }else if(requestCode == OPEN_LOGIN_REQ){
            if (resultCode == RESULT_OK) {


                logged_username = data.getStringExtra("Log_Username");
                logged_first_name = data.getStringExtra("Log_First_Name");
                logged_middle_initial = data.getStringExtra("Log_Middle_Initial");
                logged_last_name = data.getStringExtra("Log_Last_Name");
                logged_position = data.getStringExtra("Log_Position");
                logged_contact_number = data.getStringExtra("Log_Contact_Number");
                logged_address = data.getStringExtra("Log_Address");

                if(data.getStringExtra("Log_Keep_Log").equals("Yes")){
                    final String PREFS_NAME = "MyPrefsFile";

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    PackageInfo pInfo = null;
                    try {
                        pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    String version = pInfo.versionName;

                    settings.edit().putBoolean("keep_me_logged_" + version, true).apply();
                    settings.edit().putString("logged_username_" + version, logged_username ).apply();
                    settings.edit().putString("logged_first_name_" + version, logged_first_name ).apply();
                    settings.edit().putString("logged_middle_initial_" + version, logged_middle_initial ).apply();
                    settings.edit().putString("logged_last_name_" + version, logged_last_name ).apply();
                    settings.edit().putString("logged_position_" + version, logged_position ).apply();
                    settings.edit().putString("logged_contact_number_" + version, logged_contact_number ).apply();
                    settings.edit().putString("logged_address_" + version, logged_address ).apply();

                }else{
                    final String PREFS_NAME = "MyPrefsFile";

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    PackageInfo pInfo = null;
                    try {
                        pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    String version = pInfo.versionName;

                    settings.edit().putBoolean("keep_me_logged_" + version, false).apply();
                    settings.edit().putString("logged_username_" + version, null ).apply();
                    settings.edit().putString("logged_first_name_" + version, null ).apply();
                    settings.edit().putString("logged_middle_initial_" + version, null ).apply();
                    settings.edit().putString("logged_last_name_" + version, null ).apply();
                    settings.edit().putString("logged_position_" + version, null).apply();
                    settings.edit().putString("logged_contact_number_" + version, null ).apply();
                    settings.edit().putString("logged_address_" + version, null ).apply();

                }

                btnScan.setVisibility(View.VISIBLE);
                btnScan.setClickable(true);
                btnScan.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0,0,0,0 )));
                btnScan.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                txtScanLabel.setVisibility(View.VISIBLE);

                btnStats.setVisibility(View.VISIBLE);
                txtStatsLabel.setVisibility(View.VISIBLE);

                btnManualSearch.setVisibility(View.VISIBLE);
                btnManualSearch.setClickable(true);
                btnManualSearch.setBackgroundTintList(ColorStateList.valueOf(Color.argb(0,0,0,0 )));
                btnManualSearch.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                txtManualLabel.setVisibility(View.VISIBLE);

                if(logged_position.substring(0,6).equals("BILECO")){



                    btnViewAdminReg.setVisibility(View.VISIBLE);
                    txtViewAdminRegLabel.setVisibility(View.VISIBLE);

                    btnScanQR.setVisibility(View.VISIBLE);
                    txtScanQRLabel.setVisibility(View.VISIBLE);

                    btnCapSign.setVisibility(View.VISIBLE);
                    txtCapOffSignLbl.setVisibility(View.VISIBLE);

                }else{



                    btnViewAdminReg.setVisibility(View.GONE);
                    txtViewAdminRegLabel.setVisibility(View.GONE);

                    btnScanQR.setVisibility(View.GONE);
                    txtScanQRLabel.setVisibility(View.GONE);

                    btnCapSign.setVisibility(View.GONE);
                    txtCapOffSignLbl.setVisibility(View.GONE);

                }

                btnViewRegistered.setVisibility(View.VISIBLE);
                txtViewRegLabel.setVisibility(View.VISIBLE);

                btnLogIn.setVisibility(View.GONE);
                txtLoginLabel.setVisibility(View.GONE);

                btnLogOut.setVisibility(View.VISIBLE);
                txtLogoutLabel.setVisibility(View.VISIBLE);

                txtLogInState.setText("Logged in as: " + logged_username);

                btnManualSearch.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(act_Main_Menu.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){


//                            Intent registerActivity = new Intent(getApplicationContext(), act_Manual_Search_Selection.class);
//
//                            registerActivity.putExtra("Ver_Username_Reg", logged_username);
//                            startActivity(registerActivity);

                            Intent registerActivity = new Intent(getApplicationContext(), act_Search_by_Account_Number.class);

                            registerActivity.putExtra("Ver_Username_Reg", logged_username);
                            startActivity(registerActivity);


                        }else {
                            ActivityCompat.requestPermissions(act_Main_Menu.this, new String[]{
                                    Manifest.permission.INTERNET
                            }, INTERNET_MANUAL_SEARCH_REQUEST);

                        }

                    }
                });


            }else if(resultCode == RESULT_CANCELED){
                logged_username = "Guest";
                logged_first_name = "Guest";
                logged_middle_initial = "Guest";
                logged_last_name = "Guest";
                logged_position = "Guest";
                logged_contact_number = "Guest";
                logged_address = "Guest";

                final String PREFS_NAME = "MyPrefsFile";

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                PackageInfo pInfo = null;
                try {
                    pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                String version = pInfo.versionName;

                settings.edit().putBoolean("keep_me_logged_" + version, false).apply();
                settings.edit().putString("logged_username_" + version, null ).apply();
                settings.edit().putString("logged_first_name_" + version, null ).apply();
                settings.edit().putString("logged_middle_initial_" + version, null ).apply();
                settings.edit().putString("logged_last_name_" + version, null ).apply();
                settings.edit().putString("logged_position_" + version, null).apply();
                settings.edit().putString("logged_contact_number_" + version, null ).apply();
                settings.edit().putString("logged_address_" + version, null ).apply();




                txtLogInState.setText("Logged in as: " + logged_username);




                btnManualSearch.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        if (ContextCompat.checkSelfPermission(act_Main_Menu.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){


//                            Intent registerActivity = new Intent(getApplicationContext(), act_Manual_Search_Selection.class);
//
//                            registerActivity.putExtra("Ver_Username_Reg", logged_username);
//                            startActivity(registerActivity);

                            Intent registerActivity = new Intent(getApplicationContext(), act_Search_by_Account_Number.class);

                            registerActivity.putExtra("Ver_Username_Reg", logged_username);
                            startActivity(registerActivity);


                        }else {
                            ActivityCompat.requestPermissions(act_Main_Menu.this, new String[]{
                                    Manifest.permission.INTERNET
                            }, INTERNET_MANUAL_SEARCH_REQUEST);

                        }

                    }
                });

                btnScan.setVisibility(View.VISIBLE);
                btnScan.setClickable(false);
                btnScan.setBackgroundTintList(ColorStateList.valueOf(Color.argb(90,0,0,0 )));
                btnScan.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                txtScanLabel.setVisibility(View.VISIBLE);


                btnManualSearch.setVisibility(View.VISIBLE);
                btnManualSearch.setClickable(false);
                btnManualSearch.setBackgroundTintList(ColorStateList.valueOf(Color.argb(90,0,0,0 )));
                btnManualSearch.setBackgroundTintMode(PorterDuff.Mode.SRC_OVER);
                txtManualLabel.setVisibility(View.VISIBLE);

                btnStats.setVisibility(View.GONE);
                txtStatsLabel.setVisibility(View.GONE);

                btnViewAdminReg.setVisibility(View.GONE);
                txtViewAdminRegLabel.setVisibility(View.GONE);

                btnScanQR.setVisibility(View.GONE);
                txtScanQRLabel.setVisibility(View.GONE);

                btnCapSign.setVisibility(View.GONE);
                txtCapOffSignLbl.setVisibility(View.GONE);

                btnViewRegistered.setVisibility(View.GONE);
                txtViewRegLabel.setVisibility(View.GONE);

                btnLogIn.setVisibility(View.VISIBLE);
                txtLoginLabel.setVisibility(View.VISIBLE);

                btnLogOut.setVisibility(View.GONE);
                txtLogoutLabel.setVisibility(View.GONE);



            }



        }else if(requestCode == INTERNET_SCAN_QR_REQUEST){

            if(resultCode == RESULT_OK){
                Intent registerActivity = new Intent(getApplicationContext(), act_QR_Attendance_Onsite.class);

                registerActivity.putExtra("Ver_Username_Reg", logged_username);
                startActivity(registerActivity);

            }

        }else if(requestCode == INTERNET_SCAN_BARCODE_REQUEST){
            if(resultCode == RESULT_OK){

                Intent registerActivity = new Intent(getApplicationContext(), act_Barcode_Scan_Reg.class);
                startActivityForResult(registerActivity, BARCODE_OPEN_REQUEST);


            }

        }else if(requestCode == INTERNET_LOGIN_REQUEST){
            if(resultCode == RESULT_OK){
                logged_username = "Guest";
                txtLogInState.setText("Logged in as: ");
                Intent loggingActivity = new Intent(getApplicationContext(), act_Logging_User.class);
                startActivityForResult(loggingActivity, OPEN_LOGIN_REQ);

            }

        }else if(requestCode == INTERNET_LOGOUT_REQUEST){
            if(resultCode == RESULT_OK){
                logged_username = "Guest";
                txtLogInState.setText("Logged in as: ");

                final String PREFS_NAME = "MyPrefsFile";

                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                PackageInfo pInfo = null;
                try {
                    pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                String version = pInfo.versionName;

                settings.edit().putBoolean("keep_me_logged_" + version, false).apply();
                settings.edit().putString("logged_username_" + version, null ).apply();
                settings.edit().putString("logged_first_name_" + version, null ).apply();
                settings.edit().putString("logged_middle_initial_" + version, null ).apply();
                settings.edit().putString("logged_last_name_" + version, null ).apply();
                settings.edit().putString("logged_position_" + version, null).apply();
                settings.edit().putString("logged_contact_number_" + version, null ).apply();
                settings.edit().putString("logged_address_" + version, null ).apply();

                Intent loggingActivity = new Intent(getApplicationContext(), act_Logging_User.class);

                startActivityForResult(loggingActivity, OPEN_LOGIN_REQ);

            }

        }else if(requestCode == INTERNET_MANUAL_SEARCH_REQUEST){
            if(resultCode == RESULT_OK){
                Intent registerActivity = new Intent(getApplicationContext(), act_Search_by_Account_Number.class);

                registerActivity.putExtra("Ver_Username_Reg", logged_username);
                startActivity(registerActivity);

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


//    private static Intent Open_Facebook_Page(PackageManager pm, String url){
//
//        Uri uri;
//        try {
//            pm.getPackageInfo("com.facebook.katana", 0);
//            // https://stackoverflow.com/a/24547437/1048340
//            uri = Uri.parse("fb://facewebmodal/f?href=" + url);
//        } catch (PackageManager.NameNotFoundException e) {
//            uri = Uri.parse(url);
//        }
//        return new Intent(Intent.ACTION_VIEW, uri);
//
//    }

    public void openFacebookProfile(Activity activity, String id, String url) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(activity, id, url);
        facebookIntent.setData(Uri.parse(facebookUrl));
        //Toast.makeText(act_Main_Menu.this, facebookUrl, Toast.LENGTH_LONG).show();
        activity.startActivity(facebookIntent);
    }

    public String getFacebookPageURL(Context context, String id, String url) {
        PackageManager packageManager = context.getPackageManager();


        if(appInstalledOrNot(this, "com.facebook.katana")){
            return "fb://page/" + id ;

        }else{
            return url;

        }


    }

    private static boolean appInstalledOrNot(Context context, String uri)
    {
        PackageManager pm = context.getPackageManager();
        try
        {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
        }

        return false;
    }


}