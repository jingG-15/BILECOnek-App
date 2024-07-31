package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.holanda.bilecoagmaonlineregistration2021.R;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class act_Photo_ID extends AppCompatActivity {

    static int REQUEST_CAMERA = 100;
    static int REQUEST_CAMERA_2 = 101;
    static int REQUEST_OPEN_GALLERY = 103;
    static int REQUEST_GALLERY_2 = 104;
    static int RESULT_REQ_GALLERY = 105;
    static int RESULT_REQ_CAMERA = 106;

    static int REQUEST_INTERNET_NETWORK = 107;

    TextView txtmessage;
    Button btnOpenCamera, btnOpenCamera2, btnProceed, btnOpenGallery1, btnOpenGallery2;
    ImageView imgIDPhoto;

    ContentValues values;
    Uri imageUri;

    private ProgressDialog pDialog;

    Bitmap Save_Photo_ID;

    String Save_Account_Name, Save_Account_Number, Save_Billing_Address, Save_Class, Save_Signature, Save_Contact_Number, Save_Username, Save_Town;

    String json_message;
    String Resulting_Stub;


    private boolean adLoaded=false;
    private AdView bannerAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_id);

        getSupportActionBar().setTitle("Valid ID Photo");

        txtmessage = findViewById(R.id.photo_txt_message);
        btnOpenCamera = findViewById(R.id.photo_btn_open_cam_1);
        btnOpenCamera2 = findViewById(R.id.photo_btn_open_cam_2);
        btnProceed = findViewById(R.id.photo_btn_proceed);
        imgIDPhoto = findViewById(R.id.photo_img_ID_pic);
        btnOpenGallery1 = findViewById(R.id.photo_btn_open_gallery_1);
        btnOpenGallery2 = findViewById(R.id.photo_btn_open_gallery_2);

        imgIDPhoto.setVisibility(View.INVISIBLE);
        btnProceed.setVisibility(View.INVISIBLE);
        btnOpenCamera2.setVisibility(View.INVISIBLE);
        btnOpenGallery2.setVisibility(View.INVISIBLE);

        txtmessage.setVisibility(View.VISIBLE);
        btnOpenCamera.setVisibility(View.VISIBLE);
        btnOpenGallery1.setVisibility(View.VISIBLE);
        txtmessage.setVisibility(View.VISIBLE);

        Save_Account_Name =  getIntent().getExtras().getString("Reg_Account_Name");
        Save_Account_Number = getIntent().getExtras().getString("Reg_Account_Number");
        Save_Billing_Address =  getIntent().getExtras().getString("Reg_Billing_Address");
        Save_Class =  getIntent().getExtras().getString("Reg_Class");
        Save_Signature = getIntent().getExtras().getString("Reg_Signature");
        Save_Contact_Number = getIntent().getExtras().getString("Reg_Contact");
        Save_Username = getIntent().getExtras().getString("Reg_Username");
        Save_Town = getIntent().getExtras().getString("Reg_Town");


        bannerAdView = (AdView) findViewById(R.id.bannerAdView_7);

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
                MobileAds.initialize(act_Photo_ID.this);

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



        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ActivityCompat.requestPermissions(
                        act_Photo_ID.this,
                        permissions(),
                        REQUEST_CAMERA
                );






//                if (ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
//                        ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                        ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//
//                    imageUri = null;
//                    values = new ContentValues();
//                    values.put(MediaStore.Images.Media.TITLE, "Snapshot");
//                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
//                    imageUri = act_Photo_ID.this.getContentResolver().insert(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//                    startActivityForResult(intent, RESULT_REQ_CAMERA);
//                    //Toast.makeText(getContext(), imageUri.toString(), Toast.LENGTH_LONG).show();
//
//
//
//
//                }else {
//                    ActivityCompat.requestPermissions(act_Photo_ID.this, new String[]{
//                            Manifest.permission.CAMERA,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                    }, REQUEST_CAMERA);
//
//
//                }



            }
        });


        btnOpenCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ActivityCompat.requestPermissions(
                        act_Photo_ID.this,
                        permissions(),
                        REQUEST_CAMERA_2
                );


//                if (ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
//                        ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                        ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//
//                    imageUri = null;
//                    values = new ContentValues();
//                    values.put(MediaStore.Images.Media.TITLE, "Snapshot");
//                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
//                    imageUri = act_Photo_ID.this.getContentResolver().insert(
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//                    startActivityForResult(intent, RESULT_REQ_CAMERA);
//                    //Toast.makeText(getContext(), imageUri.toString(), Toast.LENGTH_LONG).show();
//
//
//
//
//                }else {
//                    ActivityCompat.requestPermissions(act_Photo_ID.this, new String[]{
//                            Manifest.permission.CAMERA,
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                    }, REQUEST_CAMERA_2);
//
//
//                }



            }
        });



        btnOpenGallery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ActivityCompat.requestPermissions(
                        act_Photo_ID.this,
                        permissions(),
                        REQUEST_OPEN_GALLERY
                );


//                if (ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                        ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//
//
//                    imageUri = null;
//                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                    photoPickerIntent.setType("image/*");
//                    startActivityForResult(photoPickerIntent, RESULT_REQ_GALLERY);
//
//
//                }else {
//                    ActivityCompat.requestPermissions(act_Photo_ID.this, new String[]{
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                    }, REQUEST_OPEN_GALLERY);
//
//
//                }





            }
        });

        btnOpenGallery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ActivityCompat.requestPermissions(
                        act_Photo_ID.this,
                        permissions(),
                        REQUEST_GALLERY_2
                );

//                if (ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
//                        ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//
//
//                    imageUri = null;
//                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                    photoPickerIntent.setType("image/*");
//                    startActivityForResult(photoPickerIntent, RESULT_REQ_GALLERY);
//
//
//                }else {
//                    ActivityCompat.requestPermissions(act_Photo_ID.this, new String[]{
//                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                    }, REQUEST_GALLERY_2);
//
//
//                }


            }
        });





    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_REQ_CAMERA && imageUri != null) {
            new Load_Cap_Image().execute();
        }else if(requestCode == RESULT_REQ_GALLERY){
            if (resultCode == RESULT_OK && data != null) {

                imageUri = data.getData();

                new Load_Cap_Image().execute();

            }else {
                Toast.makeText(act_Photo_ID.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CAMERA){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


//                imageUri = null;
//                values = new ContentValues();
//                values.put(MediaStore.Images.Media.TITLE, "Snapshot");
//                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
//                imageUri = act_Photo_ID.this.getContentResolver().insert(
//                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//                startActivityForResult(intent, 108);
//                //Toast.makeText(getContext(), imageUri.toString(), Toast.LENGTH_LONG).show();

                imageUri = null;
                    values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, "Snapshot");
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                    imageUri = act_Photo_ID.this.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                    startActivityForResult(intent, RESULT_REQ_CAMERA);



            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == REQUEST_INTERNET_NETWORK){

            new Insert_New_Registrant_AGMA().execute();


        }else if(requestCode == REQUEST_CAMERA_2){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                imageUri = null;
                values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "Snapshot");
                values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                imageUri = act_Photo_ID.this.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

                startActivityForResult(intent, RESULT_REQ_CAMERA);
                //Toast.makeText(getContext(), imageUri.toString(), Toast.LENGTH_LONG).show();



            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == REQUEST_GALLERY_2){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                imageUri = null;
                Intent photoPickerIntent;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        photoPickerIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                    } else {
                        photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    }

                    photoPickerIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), RESULT_REQ_GALLERY);



            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == REQUEST_OPEN_GALLERY){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                imageUri = null;
                Intent photoPickerIntent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    photoPickerIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                } else {
                    photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                photoPickerIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(photoPickerIntent, "Select Picture"), RESULT_REQ_GALLERY);



            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }


    }


    class Load_Cap_Image extends AsyncTask<String, String, String> {

        Bitmap thumbnail;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Photo_ID.this);
            pDialog.setMessage("Processing your Photo. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            try {

                String file_path = getRealPathFromURI(imageUri);
                ExifInterface ei = new ExifInterface(file_path);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);


                Save_Photo_ID  = MediaStore.Images.Media.getBitmap(
                        act_Photo_ID.this.getContentResolver(), imageUri);

                switch(orientation) {

                    case ExifInterface.ORIENTATION_ROTATE_90:
                        Save_Photo_ID = rotateImage(thumbnail, 90);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_180:
                        Save_Photo_ID = rotateImage(thumbnail, 180);
                        break;

                    case ExifInterface.ORIENTATION_ROTATE_270:
                        Save_Photo_ID = rotateImage(thumbnail, 270);
                        break;

                    case ExifInterface.ORIENTATION_NORMAL:
                    default:
                        Save_Photo_ID  = MediaStore.Images.Media.getBitmap(
                                act_Photo_ID.this.getContentResolver(), imageUri);
                }




            } catch (Exception e) {
                e.printStackTrace();
                //Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            if(Save_Photo_ID != null){
                imgIDPhoto.setImageBitmap(Save_Photo_ID);

                imgIDPhoto.setVisibility(View.VISIBLE);
                btnProceed.setVisibility(View.VISIBLE);
                btnOpenCamera2.setVisibility(View.VISIBLE);
                btnOpenGallery2.setVisibility(View.VISIBLE);

                txtmessage.setVisibility(View.INVISIBLE);
                btnOpenCamera.setVisibility(View.INVISIBLE);
                btnOpenGallery1.setVisibility(View.INVISIBLE);
                txtmessage.setVisibility(View.INVISIBLE);

                btnProceed.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        ActivityCompat.requestPermissions(
                                act_Photo_ID.this,
                                permissions(),
                                REQUEST_INTERNET_NETWORK
                        );




//                        if(ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
//                                ContextCompat.checkSelfPermission(act_Photo_ID.this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED){
//
//                            new Insert_New_Registrant_AGMA().execute();
//
//
//
//                        }else{
//                            ActivityCompat.requestPermissions(act_Photo_ID.this, new String[]{
//                                    Manifest.permission.INTERNET,
//                                    Manifest.permission.ACCESS_NETWORK_STATE
//                            }, 103);
//
//                        }

                    }
                });



                Toast.makeText(act_Photo_ID.this, "Done!", Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(act_Photo_ID.this, "Image null!", Toast.LENGTH_LONG).show();
            }



        }

    }

    public String getRealPathFromURI(Uri contentUri) {

        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
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



    class Insert_New_Registrant_AGMA extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Photo_ID.this);
            pDialog.setMessage("Uploading Registration Data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            if(internetConnectionAvailable(10000) == true){
                try {

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    Save_Photo_ID.compress(Bitmap.CompressFormat.JPEG,60,stream);
                    byte[] byteArray = stream.toByteArray();
                    String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);


                    String link;
                    if(Save_Username.equals("Guest")){
                        link=getString(R.string.Webshost_IP ) + "/AGMA_2024_API/InsertNewPreReg.php";

                    }else{
                        link=getString(R.string.Webshost_IP ) + "/AGMA_2024_API/InsertNewMCOReg.php";

                    }


                    HashMap<String,String> data_1 = new HashMap<>();
                    data_1.put("P_Account_Name", Save_Account_Name);
                    data_1.put("P_Account_Number", Save_Account_Number);
                    data_1.put("P_Billing_Address", Save_Billing_Address);
                    data_1.put("P_Class", Save_Class);
                    data_1.put("P_Contact_Number", Save_Contact_Number);
                    data_1.put("P_Town", Save_Town);
                    data_1.put("P_Photo_ID", encodedImage);
                    data_1.put("P_Photo_Signature", Save_Signature);
                    data_1.put("P_Username", Save_Username);



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

                        if(json_message.equals("Data Submit Successfully")){

                            Resulting_Stub = obj.getString("result_stub");

                        }



                    }else{
                        json_message = "No Response from server";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    json_message = new String("Exception: " + e.getMessage());
                    //Toast.makeText(getContext(), new String("Exception: " + e.getMessage()), Toast.LENGTH_LONG).show();
                }

            }else {
                //Toast.makeText(act_Photo_ID.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
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
            if(json_message.equals("Data Submit Successfully")){
                //Toast.makeText(getContext(), json_message_1 , Toast.LENGTH_LONG).show();




                Intent nextActivity = new Intent(getApplicationContext(), act_Generate_QR_Stub.class);



                nextActivity.putExtra("Reg_Account_Name", Save_Account_Name);
                nextActivity.putExtra("Reg_Account_Number", Save_Account_Number);
                nextActivity.putExtra("Reg_Billing_Address", Save_Billing_Address);
                nextActivity.putExtra("Reg_Class", Save_Class);
                nextActivity.putExtra("Reg_Stub_Number", Resulting_Stub);
                nextActivity.putExtra("Reg_Contact", Save_Contact_Number);
                nextActivity.putExtra("Message", "New");



                startActivity(nextActivity);
                finish();


                Toast.makeText(act_Photo_ID.this, "Data Submission Success!", Toast.LENGTH_LONG).show();



            }else if(json_message.equals("No Response from server")){
                Toast.makeText(act_Photo_ID.this, json_message + ". Please check your internet connection or contact BILECO IT division for technical details.", Toast.LENGTH_LONG).show();
            }else if(json_message.equals("Try Again Err: 10")){
                //new Create_New_Profile().execute();
                Toast.makeText(act_Photo_ID.this, "Internal Server Error 10." , Toast.LENGTH_LONG).show();
            }else{

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Photo_ID.this, finmsg + "\nException L535", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Photo_ID.this,  json_message.replace(getString(R.string.Webshost_IP ), "Server").replace("/", "") , Toast.LENGTH_LONG).show();
                //Toast.makeText(act_Photo_ID.this, "Server Error!\nException: 584", Toast.LENGTH_LONG).show();
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



    public static String[] storge_permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
    };

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] storge_permissions_33 = {
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_AUDIO,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE
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