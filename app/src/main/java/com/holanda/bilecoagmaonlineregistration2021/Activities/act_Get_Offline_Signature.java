package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.holanda.bilecoagmaonlineregistration2021.R;
import com.kyanogen.signatureview.SignatureView;

//import com.github.gcacace.signaturepad.views.SignaturePad;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class act_Get_Offline_Signature extends AppCompatActivity {


    ImageView btnClear_Off, btnSave_Off;
    Bitmap bmp_signature_Off;
    SignatureView signatureView_Off;



    static int SAVE_FILE_REQ = 101;


    static int REQUEST_FILE_ACCESS = 100;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_offline_signature);


        Objects.requireNonNull(getSupportActionBar()).hide();


        btnClear_Off = findViewById(R.id.sigoff_imgbtn_clear2);
        btnSave_Off = findViewById(R.id.sigoff_imgbtn_done);
        signatureView_Off = findViewById(R.id.sigoff_sign_view);

        signatureView_Off.setPenColor(R.color.blue_500);


        btnClear_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView_Off.clearCanvas();
            }
        });

        btnSave_Off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ActivityCompat.requestPermissions(
                        act_Get_Offline_Signature.this,
                        permissions(),
                        REQUEST_FILE_ACCESS
                );



//                if (ContextCompat.checkSelfPermission(act_Get_Offline_Signature.this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED){
//
//                    if(!signatureView_Off.isEmpty()){
//                        bmp_signature_Off = signatureView_Off.getSignatureBitmap();
//
//                        Intent FilenameActivity = new Intent(getApplicationContext(), act_Enter_Filename.class);
//                        startActivityForResult(FilenameActivity, SAVE_FILE_REQ);
//
//
//                    }else{
//
//                        Toast.makeText(getApplicationContext(), "Signature is Empty!", Toast.LENGTH_LONG).show();
//                    }
//
//                }else {
//
//                    ActivityCompat.requestPermissions(act_Get_Offline_Signature.this, new String[]{
//                            Manifest.permission.READ_MEDIA_IMAGES
//                    }, REQUEST_FILE_ACCESS);
//
//                }


            }
        });




    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_FILE_ACCESS){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                if(!signatureView_Off.isBitmapEmpty()){
                    bmp_signature_Off = signatureView_Off.getSignatureBitmap();

                    Intent FilenameActivity = new Intent(getApplicationContext(), act_Enter_Filename.class);
                    startActivityForResult(FilenameActivity, SAVE_FILE_REQ);


                }else{

                    Toast.makeText(getApplicationContext(), "Signature is Empty!", Toast.LENGTH_LONG).show();
                }



            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }


    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SAVE_FILE_REQ){
            if(resultCode == RESULT_OK){

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){

                    String return_statement = createDirectoryAndSaveFile_ver_R_and_Up(bmp_signature_Off, data.getStringExtra("Sig_Off_Filename") + ".png");
                    if(return_statement.equals("Signature is saved.")){


                        Toast.makeText(getApplicationContext(), return_statement, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(), return_statement, Toast.LENGTH_LONG).show();

                    }

                }else {

                    String return_statement = createDirectoryAndSaveFile(bmp_signature_Off, data.getStringExtra("Sig_Off_Filename") + ".png");
                    if(return_statement.equals("Signature is saved.")){


                        Toast.makeText(getApplicationContext(), return_statement, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();

                    }else{
                        Toast.makeText(getApplicationContext(), return_statement, Toast.LENGTH_LONG).show();

                    }

                }

            }

        }

    }


    private String createDirectoryAndSaveFile_ver_R_and_Up(Bitmap imageToSave, String fileName) {
        String return_str = null;
        File direct = new File(this.getExternalFilesDir(null) + "/BILECOnek_Sign_PNG");


        if (!direct.exists()) {

            boolean goods = direct.mkdirs();

            if(goods){
                File file = new File(this.getExternalFilesDir(null) + "/BILECOnek_Sign_PNG/", fileName);
                if (file.exists()) {
                    //file.delete();
                    return_str = "Filename is already in use. Please use another filename.";
                }else{
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        imageToSave.compress(Bitmap.CompressFormat.JPEG, 50, out);
                        out.flush();
                        out.close();
                        return_str = "Signature is saved.";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


            }else{

                return_str = "Creation of directory failed. Please check file access permission.";


            }



        }else{
            File file = new File(this.getExternalFilesDir(null) + "/BILECOnek_Sign_PNG/", fileName);
            if (file.exists()) {
                //file.delete();
                return_str = "Filename is already in use. Please use another filename.";
            }else{
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    imageToSave.compress(Bitmap.CompressFormat.JPEG, 50, out);
                    out.flush();
                    out.close();
                    return_str = "Signature is saved.";
                } catch (Exception e) {
                    e.printStackTrace();
                    return_str = e.getMessage().toString();
                }

            }


        }



        return return_str;
    }

    private String createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        String return_str = null;
        File direct = new File(Environment.getExternalStorageDirectory() + "/BILECOnek_Sign_PNG");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/BILECOnek_Sign_PNG/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File("/sdcard/BILECOnek_Sign_PNG/", fileName);
        if (file.exists()) {
            //file.delete();
            return_str = "Filename is already in use. Please use another filename.";
        }else{
            try {
                FileOutputStream out = new FileOutputStream(file);
                imageToSave.compress(Bitmap.CompressFormat.JPEG, 50, out);
                out.flush();
                out.close();
                return_str = "Signature is saved.";
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return return_str;
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