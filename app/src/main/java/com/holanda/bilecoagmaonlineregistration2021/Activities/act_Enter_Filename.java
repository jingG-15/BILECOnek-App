package com.holanda.bilecoagmaonlineregistration2021.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.holanda.bilecoagmaonlineregistration2021.R;

public class act_Enter_Filename extends AppCompatActivity {


    EditText txtin_Filename;
    Button btn_Save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_filename);

        txtin_Filename = findViewById(R.id.txtin_filename);
        btn_Save = findViewById(R.id.btn_Save_Filename);


        btn_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = txtin_Filename.getText().toString().trim();

                if(!(filename.isEmpty() || filename.equals("") || filename == null)){
                    Intent intent = new Intent();

                    intent.putExtra("Sig_Off_Filename", filename);
                    setResult(RESULT_OK, intent);
                    finish();


                }else{

                    txtin_Filename.setError("Filename must not be empty.");



                }





            }
        });





    }
}