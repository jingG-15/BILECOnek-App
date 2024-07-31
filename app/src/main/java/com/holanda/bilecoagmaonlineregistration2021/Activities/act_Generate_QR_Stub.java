package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
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
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.holanda.bilecoagmaonlineregistration2021.R;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class act_Generate_QR_Stub extends AppCompatActivity {

    ImageView imgGenQR;
    TextView txtDetails, txtMessage;
    Button btnSaveToGallery, btnDone;

    String Disp_Account_Name, Disp_Account_Number, Disp_Billing_Address, Disp_Class, Disp_Gen_Stub, Disp_Contact, Disp_Message;
    String For_QR_Details;

    private ProgressDialog pDialog;

    Bitmap QR_bitmap;
    ByteArrayOutputStream swap_stream;
    String error_msg;
    String img_save_log;






    private boolean adLoaded=false;
    private AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_generate_qr_stub);


        getSupportActionBar().setTitle("Generated Stub");

        imgGenQR = findViewById(R.id.stub_img_QR_View);
        txtDetails = findViewById(R.id.stub_txt_Stub_Number);
        btnDone = findViewById(R.id.stub_btn_Done);
        btnSaveToGallery = findViewById(R.id.stub_btn_Save);
        txtMessage = findViewById(R.id.stub_txt_message);


        Disp_Account_Name = getIntent().getExtras().getString("Reg_Account_Name");
        Disp_Account_Number = getIntent().getExtras().getString("Reg_Account_Number");
        Disp_Billing_Address =  getIntent().getExtras().getString("Reg_Billing_Address");
        Disp_Class =  getIntent().getExtras().getString("Reg_Class");
        Disp_Gen_Stub = getIntent().getExtras().getString("Reg_Stub_Number");
        Disp_Contact = getIntent().getExtras().getString("Reg_Contact");
        Disp_Message = getIntent().getExtras().getString("Message");

        For_QR_Details = "Online||" +
                Disp_Gen_Stub + "||" +
                Disp_Account_Name + "||" +
                Disp_Account_Number + "||" +
                Disp_Billing_Address + "||" +
                Disp_Class;

        if(Disp_Message.equals("New")){
            txtMessage.setText("Stub number is saved in your Gallery. \nPlease take a screenshot of your Stub number or press the Save Button to manually save your Stub Number to your Gallery.\n\nThank you.");
                    //Stub number is saved in your Gallery. \nPlease take a screenshot of your Stub number or press the Save Button to manually save your Stub Number to your Gallery.\n\nThank you.
        }else if(Disp_Message.equals("Registered")){
            txtMessage.setText("Account is already Registered.\n\nStub number is saved in your Gallery. \nPlease take a screenshot of your Stub number or press the Save Button to manually save your Stub Number to your Gallery.\n\nThank you.");

        }



        bannerAdView = (AdView) findViewById(R.id.bannerAdView_4);

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
                MobileAds.initialize(act_Generate_QR_Stub.this);

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


        btnSaveToGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Save_to_Local().execute();
            }
        });









        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(Disp_Message.equals("New")){
//                    Intent registerActivity = new Intent(getApplicationContext(), act_Main_Menu.class);
//
//
//                    startActivity(registerActivity);
//
//                    finish();
//                }else if(Disp_Message.equals("Registered")){
//                    Intent intent = new Intent();
//
//                    setResult(RESULT_OK, intent);
//                    finish();
//
//                }

                Intent intent = new Intent();

                setResult(RESULT_OK, intent);
                finish();









            }
        });





        new Load_QR_Stub_Number().execute();



    }



    class Load_QR_Stub_Number extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Generate_QR_Stub.this);
            pDialog.setMessage("Loading Stub Number. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @SuppressLint("ClickableViewAccessibility")
        protected String doInBackground(String... args) {
            try {





                QR_bitmap = null;
                try {
                    QR_bitmap = textToImage(For_QR_Details  , 1000, 1000);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                QR_bitmap = Bitmap.createBitmap(QR_bitmap, 100, 100, 800, 800);

                swap_stream = new ByteArrayOutputStream();

                QR_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, swap_stream);




                //SAving layout to image section







                error_msg = "Done";
            } catch (Exception e) {
                e.printStackTrace();
                error_msg = new String("Exception: " + e.getMessage());
                //Toast.makeText(getContext(), new String("Exception: " + e.getMessage()), Toast.LENGTH_LONG).show();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/


        @SuppressLint("ClickableViewAccessibility")
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (error_msg.equals("Done")){


                String temp = "Stub #: " + Disp_Gen_Stub;
                imgGenQR.setImageBitmap(QR_bitmap);


                txtDetails.setText(temp);


                if(Disp_Message.equals("New")){
                    temp =  "Account Number: " + Disp_Account_Number + "\n" +
                            "Account Name: " + Disp_Account_Name + "\n" +
                            "Billing Address: " + Disp_Billing_Address + "\n" +
                            "Class: " + Disp_Class + "\n" +
                            "Contact #: " + Disp_Contact +
                            "\n\nStub number is saved in your Gallery. \nPlease take a screenshot of your Stub number or press the Save Button to manually save your Stub Number to your Gallery.\n\nThank you.";
                }else if(Disp_Message.equals("Registered")){
                    //txtMessage.setText("Account is already Registered.\n\nStub number is saved in your Gallery. \nPlease take a screenshot of your Stub number or press the Save Button to manually save your Stub Number to your Gallery.\n\nThank you.");
                    temp =  "Account Number: " + Disp_Account_Number + "\n" +
                            "Account Name: " + Disp_Account_Name + "\n" +
                            "Billing Address: " + Disp_Billing_Address + "\n" +
                            "Class: " + Disp_Class + "\n" +
                            "Contact #: " + Disp_Contact +
                            "\n\nAccount is already Registered.\n\nStub number is saved in your Gallery. \nPlease take a screenshot of your Stub number or press the Save Button to manually save your Stub Number to your Gallery.\n\nThank you.";
                }





                txtMessage.setText(temp);
                //Stub number is saved in your Gallery. \nPlease take a screenshot of your Stub number or press the Save Button to manually save your Stub Number to your Gallery.\n\nThank you.





                new Save_to_Local().execute();



                Toast.makeText(act_Generate_QR_Stub.this,"Image saved", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(act_Generate_QR_Stub.this,error_msg, Toast.LENGTH_LONG).show();
            }

        }

    }


    class Save_to_Local extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Generate_QR_Stub.this);
            pDialog.setMessage("Saving image to Gallery. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @SuppressLint("ClickableViewAccessibility")
        protected String doInBackground(String... args) {
            try {


                ConstraintLayout ll = findViewById(R.id.stub_cons_layout);

                ll.setDrawingCacheEnabled(true);
                Bitmap bitmap = ll.getDrawingCache();

                String root = Environment.getExternalStorageDirectory()
                        .toString();

                File newDir = new File(root + "/BILECO_AGMA_Stubs");
                newDir.mkdirs();

                File file = new File(newDir, Disp_Account_Number+ Disp_Gen_Stub + ".jpg");
                String s = file.getAbsolutePath();
                Log.i("Path of saved image.", s);
                System.err.print("Path of saved image." + s);

                try {

                    MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, Disp_Account_Number+ Disp_Gen_Stub + ".jpg" , "AGMA Ticket Number");


//                    FileOutputStream out = new FileOutputStream(file);
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//                    out.flush();
                    img_save_log = "Photo Saved";

//                    Toast.makeText(getApplicationContext(), "Photo Saved",
//                            Toast.LENGTH_SHORT).show();
                    //out.close();
                } catch (Exception e) {
                    img_save_log = "Photo Saved";

//                    Toast.makeText(getApplicationContext(), "Photo Saved",
//                            Toast.LENGTH_SHORT).show();
                    Log.e("Exception", e.toString());
                }






                error_msg = "Done";
            } catch (Exception e) {
                e.printStackTrace();
                error_msg = new String("Exception: " + e.getMessage());
                //Toast.makeText(getContext(), new String("Exception: " + e.getMessage()), Toast.LENGTH_LONG).show();
            }

            return null;
        }


        /**
         * After completing background task Dismiss the progress dialog
         * **/


        @SuppressLint("ClickableViewAccessibility")
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if (error_msg.equals("Done")){




                Toast.makeText(act_Generate_QR_Stub.this,"Image saved", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(act_Generate_QR_Stub.this,error_msg, Toast.LENGTH_LONG).show();
            }

        }

    }




    private Bitmap textToImage(String text, int width, int height) throws WriterException, NullPointerException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE,
                    width, height, null);
        } catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }

        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        int colorWhite = 0xFFFFFFFF;
        int colorBlack = 0xFF172C75;//0xFF000000;

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;
            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ? colorBlack : colorWhite;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, width, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
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