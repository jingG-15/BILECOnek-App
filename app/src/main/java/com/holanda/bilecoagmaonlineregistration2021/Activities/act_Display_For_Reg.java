package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.holanda.bilecoagmaonlineregistration2021.R;

public class act_Display_For_Reg extends AppCompatActivity {


    String Found_Account_Number;
    String Found_Account_Name;
    String Found_Billing_Address;
    String Found_Class;
    String Found_Registration_Stat;
    String Found_Username;
    String Found_Town;
    String Found_Sequence;
    String Found_Mem_Number;


    TextView S_Account_Number, S_Account_Name, S_Billing_Address, S_Class, S_Message, S_Mem_Number;
    Button butt_Register, butt_Cancel, butt_return;



    private boolean adLoaded=false;
    private AdView bannerAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_for_reg);
        getSupportActionBar().setTitle("Review");

        Found_Account_Number = getIntent().getExtras().getString("Ver_Account_Number");
        Found_Account_Name = getIntent().getExtras().getString("Ver_Account_Name");
        Found_Billing_Address = getIntent().getExtras().getString("Ver_Billing_Address");
        Found_Class = getIntent().getExtras().getString("Ver_Class");
        Found_Registration_Stat = getIntent().getExtras().getString("Ver_Registration_Check");
        Found_Username = getIntent().getExtras().getString("Ver_Username_Reg");
        Found_Town = getIntent().getExtras().getString("Ver_Town");
        Found_Sequence = getIntent().getExtras().getString("Ver_Sequence");
        Found_Mem_Number = getIntent().getExtras().getString("Ver_Mem_Number");

        S_Account_Number = findViewById(R.id.ver_txt_Account_Number);
        S_Account_Name = findViewById(R.id.ver_txt_Account_Name);
        S_Billing_Address = findViewById(R.id.ver_txt_Billing_Address);
        S_Class = findViewById(R.id.ver_txt_Class);
        S_Message = findViewById(R.id.ver_txt_message);
        S_Mem_Number = findViewById(R.id.ver_txt_Mem_Number);

        S_Account_Number.setText(Found_Account_Number);
        S_Account_Name.setText(Found_Account_Name);
        S_Billing_Address.setText(Found_Billing_Address);
        S_Mem_Number.setText(Found_Mem_Number);

        if(Found_Class.equals("RES")){
            S_Class.setText("Residential");
        }else if(Found_Class.equals("COM")){
            S_Class.setText("Commercial");
        }else if(Found_Class.equals("PUB")){
            S_Class.setText("Public Building");
        }else if(Found_Class.equals("CU")){
            S_Class.setText("Coop Use");
        }else if(Found_Class.equals("HV")){
            S_Class.setText("High Voltage");
        }else if(Found_Class.equals("STR")){
            S_Class.setText("Street Light");
        }else{
            S_Class.setText(Found_Class);
        }

        butt_Cancel = findViewById(R.id.ver_btn_cancel);
        butt_Register = findViewById(R.id.ver_btn_register);
        butt_return = findViewById(R.id.ver_btn_Return);

        bannerAdView = (AdView) findViewById(R.id.bannerAdView_1);

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
                MobileAds.initialize(act_Display_For_Reg.this);

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


        if(Found_Sequence.equals("1.")){

            if(Found_Registration_Stat.equals("Not Registered")){

                butt_Register.setVisibility(View.VISIBLE);
                butt_Cancel.setVisibility(View.VISIBLE);
                butt_return.setVisibility(View.INVISIBLE);
                S_Message.setText("Is this your BILECO Account Details?\nPlease check the following:");

            }else{

                butt_Register.setVisibility(View.INVISIBLE);
                butt_Cancel.setVisibility(View.INVISIBLE);
                butt_return.setVisibility(View.VISIBLE);
                S_Message.setText("Account is already registered. Please enter another BILECO Account Number");

            }

        }else{

            butt_Register.setVisibility(View.INVISIBLE);
            butt_Cancel.setVisibility(View.INVISIBLE);
            butt_return.setVisibility(View.VISIBLE);
            S_Message.setText("Account verified is not a primary account. Please enter a valid primary account number.");

        }




        butt_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        butt_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();

                setResult(RESULT_CANCELED, intent);
                finish();



            }
        });

        butt_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent nextActivity = new Intent(getApplicationContext(), act_Contact_Number.class);

                nextActivity.putExtra("Reg_Account_Name", Found_Account_Name);
                nextActivity.putExtra("Reg_Account_Number", Found_Account_Number);
                nextActivity.putExtra("Reg_Billing_Address", Found_Billing_Address);
                nextActivity.putExtra("Reg_Class", Found_Class);
                nextActivity.putExtra("Reg_Username", Found_Username);
                nextActivity.putExtra("Reg_Town", Found_Town);




                startActivity(nextActivity);
                finish();



            }
        });


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