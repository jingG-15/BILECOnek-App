package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.holanda.bilecoagmaonlineregistration2021.R;
import com.kyanogen.signatureview.SignatureView;


//import com.github.gcacace.signaturepad.views.SignaturePad;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

public class act_Get_Signature extends AppCompatActivity {

    ImageView btnClear, btnSave, btnBrowse;
    Bitmap bmp_signature;
    SignatureView signatureView;

    int INTERNET_REQ_SIGN_CHOOSER = 104;
    int GET_SAVED_SIGN_REQ = 105;

    private boolean adLoaded=false;
    private AdView bannerAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_signature);

        Objects.requireNonNull(getSupportActionBar()).hide();


        btnClear = findViewById(R.id.sig_imgbtn_clear);
        btnSave = findViewById(R.id.sig_imgbtn_save_sig);
        signatureView = findViewById(R.id.sig_sign_view);
        btnBrowse = findViewById(R.id.sig_imgbtn_browse_signature);

        bannerAdView = (AdView) findViewById(R.id.bannerAdView_5);

        signatureView.setPenColor(R.color.blue_700);

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
                MobileAds.initialize(act_Get_Signature.this);

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

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ActivityCompat.requestPermissions(
                        act_Get_Signature.this,
                        permissions(),
                        INTERNET_REQ_SIGN_CHOOSER
                );






//                if (ContextCompat.checkSelfPermission(act_Get_Signature.this, permissions()) == PackageManager.PERMISSION_GRANTED){
//
//                    Intent nextActivity = new Intent(getApplicationContext(), act_Signature_Chooser.class);
//
//                    startActivityForResult(nextActivity, GET_SAVED_SIGN_REQ);
//
//                }else {
//
//                    ActivityCompat.requestPermissions(act_Get_Signature.this, new String[]{
//                            Manifest.permission.READ_MEDIA_IMAGES
//                    }, INTERNET_REQ_SIGN_CHOOSER);
//
//                }







            }
        });




        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signatureView.clearCanvas();
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!signatureView.isBitmapEmpty()){
                    bmp_signature = signatureView.getSignatureBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp_signature.compress(Bitmap.CompressFormat.JPEG,70,stream);
                    byte[] byteArray = stream.toByteArray();
                    String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    Intent nextActivity = new Intent(getApplicationContext(), act_Photo_ID.class);



                    nextActivity.putExtra("Reg_Account_Name", getIntent().getExtras().getString("Reg_Account_Name"));
                    nextActivity.putExtra("Reg_Account_Number", getIntent().getExtras().getString("Reg_Account_Number"));
                    nextActivity.putExtra("Reg_Billing_Address", getIntent().getExtras().getString("Reg_Billing_Address"));
                    nextActivity.putExtra("Reg_Class", getIntent().getExtras().getString("Reg_Class"));
                    nextActivity.putExtra("Reg_Contact", getIntent().getExtras().getString("Reg_Contact"));
                    nextActivity.putExtra("Reg_Username", getIntent().getExtras().getString("Reg_Username"));
                    nextActivity.putExtra("Reg_Town", getIntent().getExtras().getString("Reg_Town"));
                    nextActivity.putExtra("Reg_Signature", encodedImage);




                    startActivity(nextActivity);
                    finish();





                }else{

                    Toast.makeText(getApplicationContext(), "Signature is Empty!", Toast.LENGTH_LONG).show();
                }
            }
        });




    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        File dir;

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){


                            dir = new File( getApplicationContext().getExternalFilesDir(null) + "/BILECOnek_Sign_PNG/" );

                        }else{

                            dir = new File( "/sdcard/BILECOnek_Sign_PNG/" );

                        }



                        Bitmap signBitmap = BitmapFactory.decodeFile(dir.getPath() + "/" + data.getStringExtra("Selected_Sign_Name"));

                        Bitmap mutableBitmap = signBitmap.copy(Bitmap.Config.RGB_565, true);

                        signatureView.setBitmap(mutableBitmap);
                    }
                }
            });


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == INTERNET_REQ_SIGN_CHOOSER){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Intent nextActivity = new Intent(getApplicationContext(), act_Signature_Chooser.class);

                //startActivityForResult(nextActivity, GET_SAVED_SIGN_REQ);

                someActivityResultLauncher.launch(nextActivity);








            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }


    }


    ActivityResultLauncher<Intent> activityResultLaunch = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == 123) {
                        // ToDo : Do your stuff...
                    } else if(result.getResultCode() == 321) {
                        // ToDo : Do your stuff...
                    }
                }
            });



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INTERNET_REQ_SIGN_CHOOSER){
            if(resultCode == RESULT_OK){

                Intent nextActivity = new Intent(getApplicationContext(), act_Signature_Chooser.class);

                startActivity(nextActivity);

            }

        }else if(requestCode == GET_SAVED_SIGN_REQ){
            if(resultCode == RESULT_OK){

                File dir;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){


                   dir = new File( getApplicationContext().getExternalFilesDir(null) + "/BILECOnek_Sign_PNG/" );

                }else{

                    dir = new File( "/sdcard/BILECOnek_Sign_PNG/" );

                }



                Bitmap signBitmap = BitmapFactory.decodeFile(dir.getPath() + "/" + data.getStringExtra("Selected_Sign_Name"));

                Bitmap mutableBitmap = signBitmap.copy(Bitmap.Config.RGB_565, true);

                signatureView.setBitmap(mutableBitmap);

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



    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO
    };

    public static String[] permissions() {
        String[] p;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            p = storge_permissions_33;
        } else {
            p = storge_permissions;
        }
        return p;
    }
}