package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.holanda.bilecoagmaonlineregistration2021.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class act_Fullview_Rankings extends AppCompatActivity {

    SwipeRefreshLayout fullrank_swiper;

    private ProgressDialog pDialog;

    String json_message, Rank_Type;

    ArrayList<HashMap<String, String>> Resulting_Rank;
    ListView ranks_listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullview_rankings);

        Objects.requireNonNull(getSupportActionBar()).hide();


        fullrank_swiper = findViewById(R.id.full_ranks_swiper);

        ranks_listview = findViewById(R.id.lst_Full_Ranks);

        Rank_Type = getIntent().getExtras().getString("Rank_Type");

        fullrank_swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(internetConnectionAvailable(10000)){
                    Resulting_Rank = new ArrayList<>();
                    new Fetch_Rankings().execute();

                }else {
                    Toast.makeText(act_Fullview_Rankings.this, "Please check your internet connection", Toast.LENGTH_LONG).show();


                }

            }
        });


        if(internetConnectionAvailable(10000)){
            Resulting_Rank = new ArrayList<>();
            new Fetch_Rankings().execute();

        }else {
            Toast.makeText(act_Fullview_Rankings.this, "Please check your internet connection", Toast.LENGTH_LONG).show();


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



    class Fetch_Rankings extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(act_Fullview_Rankings.this);
            pDialog.setMessage("Contacting Server. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        protected String doInBackground(String... args) {
            try {
                String link;
                if(Rank_Type.equals("MCO")){
                    link = getString(R.string.Webshost_IP) + "/AGMA_2024_API/GetFullMCORankings.php";

                }else{
                    link = getString(R.string.Webshost_IP) + "/AGMA_2024_API/GetFullEMPRankings.php";

                }
                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);


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
                if (line != null){
                    JSONObject jsonObj = new JSONObject(line);
                    if (jsonObj.getString("FullRank").equals("No Records Found.")){
                        json_message = "No Records Found.";

                    }else{
                        JSONArray Reg_Json = jsonObj.getJSONArray("FullRank");
                        int curr_rank_grouping = 1;
                        String curr_count = "";
                        for (int i = 0; i < Reg_Json.length(); i++) {

                            JSONObject c = Reg_Json.getJSONObject(i);

                            if(curr_count.equals("")){
                                curr_count = c.getString("Regs");

                            }else if(!curr_count.equals(c.getString("Regs"))){
                                curr_count = c.getString("Regs");
                                curr_rank_grouping = curr_rank_grouping + 1;
                            }


                            HashMap<String, String> result_arr = new HashMap<>();

                            // adding each child node to HashMap key => value

                            result_arr.put("Reg_Count", "Total Registrations: " + c.getString("Regs"));
                            result_arr.put("Full_Name", c.getString("Full_Name"));
                            result_arr.put("Address", "Address: " + c.getString("F_Address"));
                            result_arr.put("Rank", String.valueOf(curr_rank_grouping));



                            // adding contact to contact list
                            Resulting_Rank.add(result_arr);



                        }

                        //Listview Process Trigger Here

                        json_message = "List Loaded";
                    }
                    // Getting JSON Array node

                }else {
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


            fullrank_swiper.setRefreshing(false);

            if (json_message.equals("List Loaded")) {


                ListAdapter adapter = new SimpleAdapter(
                        act_Fullview_Rankings.this, Resulting_Rank,
                        R.layout.list_ranks, new String[]{"Reg_Count", "Full_Name", "Address", "Rank"}, new int[]{R.id.lst_ranks_txt_Reg_Count,
                        R.id.lst_ranks_Full_Name, R.id.lst_ranks_txt_Address, R.id.lst_ranks_txt_Rank_Number}){

                };

                ranks_listview.setAdapter(adapter );






                Toast.makeText(act_Fullview_Rankings.this,"Done!", Toast.LENGTH_LONG).show();

            }else if (json_message.equals("No Records Found.")) {

                Toast.makeText(act_Fullview_Rankings.this,json_message, Toast.LENGTH_LONG).show();

            }else if (json_message.equals("No Response from server")) {
                Toast.makeText(act_Fullview_Rankings.this,json_message , Toast.LENGTH_LONG).show();

            } else {

                String finmsg = json_message;
                finmsg = finmsg.replace("222.127.55.108:8080", "Server 0");
                finmsg = finmsg.replace("/", "");
                Toast.makeText(act_Fullview_Rankings.this, finmsg + "\nException 418", Toast.LENGTH_LONG).show();


                //Toast.makeText(act_Statistics.this, json_message.replace(getString(R.string.Webshost_IP), "Server").replace("/", "").replace(getString(R.string.Webhost_Account_Ledgertest), "Server"), Toast.LENGTH_LONG).show();
            }
        }

    }
}