package com.example.iot_1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {
TextView tvData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        tvData=findViewById(R.id.tvData);

        String data=MainActivity.db.viewStudent();

        if(data.length()==0){
            tvData.setText("no Number Found");
        }
        else
            tvData.setText(data);



    }

}
