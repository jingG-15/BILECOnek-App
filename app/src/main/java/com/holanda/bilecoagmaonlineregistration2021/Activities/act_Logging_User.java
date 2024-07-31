package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.holanda.bilecoagmaonlineregistration2021.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
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

public class act_Logging_User extends AppCompatActivity {

    EditText txtinUsername, txtinPassword;
    ImageButton btnShowPassword;
    Button btnLogin, btnLoginAsGuest;

    CheckBox keeplog;

    private ProgressDialog pDialog;

    String pass_username, pass_password;

    String json_message;

    String logged_Username, logged_First_Name, logged_Middle_Ini, logged_Last_Name, logged_Position, logged_Contact_Number, logged_Address, logged_Keep_Log;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging_user);

        txtinPassword = findViewById(R.id.log_password);
        txtinUsername = findViewById(R.id.log_username);

        btnShowPassword = findViewById(R.id.log_img_Show_Password);

        btnLogin = findViewById(R.id.log_btn_Login);
        btnLoginAsGuest = findViewById(R.id.log_btn_Guest_Login);



        keeplog = findViewById(R.id.log_chk_keep_log_data);

        logged_Keep_Log = "No";

        keeplog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    logged_Keep_Log = "Yes";

                }else{
                    logged_Keep_Log = "No";

                }

            }
        });


        btnLoginAsGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();

                intent.putExtra("Log_Username", "Guest");
                intent.putExtra("Log_Keep_Log", "No");

                setResult(RESULT_CANCELED, intent);
                finish();


            }
        });


        btnShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtinPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    btnShowPassword.setImageResource(R.drawable.ic_baseline_visibility_off_24);

                    //Show Password
                    txtinPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    btnShowPassword.setImageResource(R.drawable.ic_baseline_visibility_24);

                    //Hide Password
                    txtinPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String in_username = txtinUsername.getText().toString();
                String in_password = txtinPassword.getText().toString();

                if(in_password.isEmpty() || in_username.isEmpty()){
                    if(in_password.isEmpty()){
                        txtinPassword.setError("Password cannot be empty");
                        Toast.makeText(act_Logging_User.this, "Password cannot be empty.", Toast.LENGTH_SHORT).show();
                    }

                    if(in_username.isEmpty()){
                        txtinUsername.setError("Username cannot be empty");
                        Toast.makeText(act_Logging_User.this, "Username cannot be empty.", Toast.LENGTH_SHORT).show();

                    }
                }else{

                    pass_password = in_password;
                    pass_username = in_username;

                    new Check_Username_Validity().execute();







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





    class Check_Username_Validity extends AsyncTask<String, String, String> {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Logging_User.this);
            pDialog.setMessage("Logging in. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {

            if(internetConnectionAvailable(10000)){
                try {


                    String link=getString(R.string.Webshost_IP ) + "/AGMA_2024_API/CheckCredentials.php";
                    HashMap<String,String> data_1 = new HashMap<>();
                    data_1.put("Log_Username", pass_username);
                    data_1.put("Log_Password", pass_password);


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
                            logged_Username = obj.getString("Username");
                            logged_First_Name = obj.getString("First_Name");
                            logged_Middle_Ini = obj.getString("Middle_Initial");
                            logged_Last_Name = obj.getString("Last_Name");
                            logged_Position = obj.getString("Position");
                            logged_Contact_Number = obj.getString("Contact_Number");
                            logged_Address = obj.getString("Address");
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
                //Toast.makeText(act_Logging_User.this, "Please check your internet connection", Toast.LENGTH_LONG).show();
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
            if(json_message.equals("Match Found")){
                //Toast.makeText(getContext(), json_message_1 , Toast.LENGTH_LONG).show();

                Toast.makeText(act_Logging_User.this, "Login Success!", Toast.LENGTH_LONG).show();

                Intent intent = new Intent();

                intent.putExtra("Log_Username", logged_Username);
                intent.putExtra("Log_First_Name", logged_First_Name);
                intent.putExtra("Log_Middle_Initial", logged_Middle_Ini);
                intent.putExtra("Log_Last_Name", logged_Last_Name);
                intent.putExtra("Log_Position", logged_Position);
                intent.putExtra("Log_Contact_Number", logged_Contact_Number);
                intent.putExtra("Log_Address", logged_Address);
                intent.putExtra("Log_Keep_Log", logged_Keep_Log);

                setResult(RESULT_OK, intent);
                finish();



            }else if(json_message.equals("Entry Mismatch")){

                Toast.makeText(act_Logging_User.this, "Username or Password mismatch. Please check your input", Toast.LENGTH_LONG).show();


            }else{


                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Logging_User.this, finmsg + "\nException L253", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Logging_User.this, "Server Error!\nException: 250", Toast.LENGTH_LONG).show();
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