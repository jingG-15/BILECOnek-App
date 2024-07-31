package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.encoder.QRCode;
import com.holanda.bilecoagmaonlineregistration2021.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class act_QR_Attendance_Onsite extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnswitchcam;
    Integer curr_cam_id = 0;
    private ProgressDialog pDialog;

    String User_Logged;

    String Reg_Mode;
    String Stub_Number;
    String Account_name;
    String Account_Number;
    String Billing_Address;
    String Class;


    String json_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_attendance_onsite);
        Objects.requireNonNull(getSupportActionBar()).hide();

        User_Logged =  getIntent().getExtras().getString("Ver_Username_Reg");



        if (ActivityCompat.checkSelfPermission(act_QR_Attendance_Onsite.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

            try {

                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlayout_scanner_qr);
                mScannerView = new ZXingScannerView(act_QR_Attendance_Onsite.this);   // Programmatically initialize the scanner view<br />
                rl.addView(mScannerView);
                mScannerView.setResultHandler(act_QR_Attendance_Onsite.this); // Register ourselves as a handler for scan results.<br />
                List<BarcodeFormat> myformat = new ArrayList<>();
                myformat.add(BarcodeFormat.QR_CODE);
                mScannerView.setFormats(myformat);
                mScannerView.startCamera();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            ActivityCompat.requestPermissions(act_QR_Attendance_Onsite.this, new
                    String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }


        btnswitchcam = findViewById(R.id.btn_Switch_Cam);
        btnswitchcam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(curr_cam_id == 0){

                    try{
                        curr_cam_id = 1;
                        mScannerView.stopCamera();
                        mScannerView.startCamera(curr_cam_id);
                    } catch (Exception e) {
                        curr_cam_id = 0;
                        e.printStackTrace();
                    }

                }else{

                    try{
                        curr_cam_id = 0;
                        mScannerView.stopCamera();
                        mScannerView.startCamera(curr_cam_id);
                    } catch (Exception e) {
                        curr_cam_id = 1;
                        e.printStackTrace();
                    }

                }

            }
        });





    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                try {

                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlayout_scanner_qr);
                    mScannerView = new ZXingScannerView(act_QR_Attendance_Onsite.this);   // Programmatically initialize the scanner view<br />
                    rl.addView(mScannerView);
                    mScannerView.setResultHandler(act_QR_Attendance_Onsite.this); // Register ourselves as a handler for scan results.<br />
                    List<BarcodeFormat> myformat = new ArrayList<>();
                    myformat.add(BarcodeFormat.QR_CODE);

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
    public void onResume() {
        super.onResume();
        // Register ourselves as a handler for scan results.
        mScannerView.setResultHandler(this);
        // Start camera on resume
        try{
            mScannerView.startCamera(curr_cam_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop camera on pause
        mScannerView.stopCamera();
    }


    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here
        // Prints scan results


        if(rawResult.getBarcodeFormat().toString().equals("QR_CODE")){
            Reg_Mode = null;
            Stub_Number = null;
            Account_name = null;
            Account_Number = null;
            Billing_Address = null;
            Class = null;
            String div;
            String result_QR;
            Integer step = 1;

            Integer prev_index = 0;



            result_QR = rawResult.getText();
            for(int i = 0; i < result_QR.length(); i++){
                if(i < result_QR.length() - 2) {
                    div = result_QR.substring(i, i + 2);
                    if (div.equals("||")) {
                        if (step == 1){

                            Reg_Mode = result_QR.substring(prev_index, i);
                            prev_index = i + 2;
                            i++;
                        } else if (step == 2) {

                            Stub_Number = result_QR.substring(prev_index, i);
                            prev_index = i + 2;
                            i++;

                        } else if (step == 3) {
                            Account_Number = result_QR.substring(prev_index, i);
                            prev_index = i + 2;
                            i++;


                        } else if (step == 4) {

                            Account_name = result_QR.substring(prev_index, i);
                            prev_index = i + 2;
                            i++;

                        } else if (step == 5) {

                            Billing_Address = result_QR.substring(prev_index, i);
                            i = i + 2;
                            Class = result_QR.substring(i, result_QR.length());
                            break;


                        }

                        step++;

                    }
                }

            }






//        Toast.makeText(this, Reg_Mode + "\n" +
//                Stub_Number + "\n" +
//                Account_name + "\n" +
//                Account_Number + "\n" +
//                Billing_Address + "\n" +
//                Class, Toast.LENGTH_LONG).show();




            if(Reg_Mode != null){
                if(Reg_Mode.equals("Online") && Stub_Number != null
                        && Account_name != null && Account_Number != null){

                    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    tg.startTone(ToneGenerator.TONE_PROP_BEEP);
                    new Log_QR_Attendee().execute();


                }else{
                    Toast.makeText(this, "Invalid Registration Stub", Toast.LENGTH_LONG).show();

                    ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                    tg.startTone(ToneGenerator.TONE_PROP_NACK);

                    mScannerView.resumeCameraPreview(this);
                }


            }else{
                Toast.makeText(this, "Invalid Registration Stub", Toast.LENGTH_LONG).show();

                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                tg.startTone(ToneGenerator.TONE_PROP_NACK);

                mScannerView.resumeCameraPreview(this);

            }

        }else{
            Toast.makeText(this, "Invalid Format", Toast.LENGTH_LONG).show();
            mScannerView.resumeCameraPreview(this);

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
        } catch (InterruptedException | ExecutionException | TimeoutException ignored) {
        }
        return inetAddress!=null && !inetAddress.equals("");
    }


    class Log_QR_Attendee extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_QR_Attendance_Onsite.this);
            pDialog.setMessage("Uploading Attendance Data. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            if(internetConnectionAvailable(10000) == true){
                try {





                    String link=getString(R.string.Webshost_IP ) + "/AGMA_2024_API/InsertQRAttendance.php";

                    HashMap<String,String> data_1 = new HashMap<>();
                    data_1.put("Q_Account_Name", Account_name);
                    data_1.put("Q_Account_Number", Account_Number);
                    data_1.put("Q_Stub_Number", Stub_Number);
                    data_1.put("Q_Address", Billing_Address);
                    data_1.put("Q_Class", Class);
                    data_1.put("Q_Logged_User", User_Logged);



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
            mScannerView.stopCamera();

            mScannerView.resumeCameraPreview(act_QR_Attendance_Onsite.this);

            AlertDialog.Builder builder1 = new AlertDialog.Builder(act_QR_Attendance_Onsite.this);


            if(json_message.equals("Data Submit Successfully")){
                //Toast.makeText(getContext(), json_message_1 , Toast.LENGTH_LONG).show();


                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);




//                Toast.makeText(act_QR_Attendance_Onsite.this, "Data Submission Success!", Toast.LENGTH_LONG).show();

                builder1.setMessage("Attendance Submission Success!");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Done",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                tg.stopTone();
                                dialog.cancel();
                                mScannerView.startCamera(curr_cam_id);
                            }
                        });

                AlertDialog alert11 = builder1.create();

//                alert11.requestWindowFeature(Window.FEATURE_LEFT_ICON);
//                alert11.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.baseline_check_circle_24);
                alert11.setIcon(R.drawable.baseline_check_circle_24);
//                alert11.setContentView(R.layout.custom_dialog);
                alert11.setTitle("Success!");


                tg.startTone(ToneGenerator.TONE_PROP_ACK);

                alert11.show();

            }else if(json_message.equals("No Response from server")){
//                Toast.makeText(act_QR_Attendance_Onsite.this, json_message + ". Please check your internet connection or contact BILECO IT division for technical details.", Toast.LENGTH_LONG).show();



                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                builder1.setMessage("No Response from server");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Done",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                tg.stopTone();
                                dialog.cancel();
                                mScannerView.startCamera(curr_cam_id);
                            }
                        });

                AlertDialog alert11 = builder1.create();

//                alert11.requestWindowFeature(Window.FEATURE_LEFT_ICON);
//                alert11.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.baseline_cloud_off_24);
                alert11.setIcon(R.drawable.baseline_cloud_off_24);
//                alert11.setContentView(R.layout.custom_dialog);
                alert11.setTitle("Warning!");


                tg.startTone(ToneGenerator.TONE_PROP_ACK);

                alert11.show();


            }else if(json_message.equals("Try Again Err: 10")){
                //new Create_New_Profile().execute();
                Toast.makeText(act_QR_Attendance_Onsite.this, "Internal Server Error 10." , Toast.LENGTH_LONG).show();
                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                tg.startTone(ToneGenerator.TONE_PROP_NACK);
            }else{
                ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);


                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("/", "");
//                Toast.makeText(act_QR_Attendance_Onsite.this, finmsg + "\nException L535", Toast.LENGTH_LONG).show();
//


                builder1.setMessage(finmsg);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Done",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                tg.stopTone();
                                dialog.cancel();
                                mScannerView.startCamera(curr_cam_id);
                            }
                        });

                AlertDialog alert11 = builder1.create();

//                alert11.requestWindowFeature(Window.FEATURE_LEFT_ICON);
//                alert11.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.baseline_report_problem_24);
                alert11.setIcon(R.drawable.baseline_report_problem_24);
//                alert11.setContentView(R.layout.custom_dialog);
                alert11.setTitle("Warning!");


                tg.startTone(ToneGenerator.	TONE_DTMF_C);
                alert11.show();

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


}