package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.holanda.bilecoagmaonlineregistration2021.R;

public class act_Privacy_Policy extends AppCompatActivity {


    Button btnAgree, btnDisagree;
    TextView txt_data_consent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        btnAgree = findViewById(R.id.priv_btn_Agree);
        btnDisagree = findViewById(R.id.priv_btn_No_Agree);
        txt_data_consent = findViewById(R.id.priv_txt_content);

        getSupportActionBar().setTitle("Privacy Policy Statement");

        txt_data_consent.setText(Html.fromHtml(getResources().getString(R.string.consent_agreement)));

        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                setResult(RESULT_CANCELED, intent);
                finish();


            }
        });

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                setResult(RESULT_OK, intent);
                finish();




            }
        });


    }
}