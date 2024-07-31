package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.holanda.bilecoagmaonlineregistration2021.R;

public class act_Contact_Number extends AppCompatActivity {

    EditText txtin_Contact;
    Button btn_Skip, btn_Confirm;

    private boolean adLoaded=false;
    private AdView bannerAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_number);

        getSupportActionBar().setTitle("Enter Contact Number");

        btn_Confirm = findViewById(R.id.con_btn_Confirm);
        btn_Skip = findViewById(R.id.con_btn_Skip);

        txtin_Contact = findViewById(R.id.con_txtin_Contact_number);

        bannerAdView = (AdView) findViewById(R.id.bannerAdView_3);

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
                MobileAds.initialize(act_Contact_Number.this);

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


        btn_Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextActivity = new Intent(getApplicationContext(), act_Get_Signature.class);

                nextActivity.putExtra("Reg_Account_Name", getIntent().getExtras().getString("Reg_Account_Name"));
                nextActivity.putExtra("Reg_Account_Number", getIntent().getExtras().getString("Reg_Account_Number"));
                nextActivity.putExtra("Reg_Billing_Address", getIntent().getExtras().getString("Reg_Billing_Address"));
                nextActivity.putExtra("Reg_Class", getIntent().getExtras().getString("Reg_Class"));
                nextActivity.putExtra("Reg_Username", getIntent().getExtras().getString("Reg_Username"));
                nextActivity.putExtra("Reg_Town", getIntent().getExtras().getString("Reg_Town"));
                nextActivity.putExtra("Reg_Contact", "NONE");

                startActivity(nextActivity);
                finish();




            }
        });


        btn_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contact = txtin_Contact.getText().toString();
                if(!contact.isEmpty()){
                    Intent nextActivity = new Intent(getApplicationContext(), act_Get_Signature.class);

                    nextActivity.putExtra("Reg_Account_Name", getIntent().getExtras().getString("Reg_Account_Name"));
                    nextActivity.putExtra("Reg_Account_Number", getIntent().getExtras().getString("Reg_Account_Number"));
                    nextActivity.putExtra("Reg_Billing_Address", getIntent().getExtras().getString("Reg_Billing_Address"));
                    nextActivity.putExtra("Reg_Class", getIntent().getExtras().getString("Reg_Class"));
                    nextActivity.putExtra("Reg_Username", getIntent().getExtras().getString("Reg_Username"));
                    nextActivity.putExtra("Reg_Town", getIntent().getExtras().getString("Reg_Town"));
                    nextActivity.putExtra("Reg_Contact", contact);

                    startActivity(nextActivity);
                    finish();


                }else{
                    txtin_Contact.setError("This field should not be empty.");

                }

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