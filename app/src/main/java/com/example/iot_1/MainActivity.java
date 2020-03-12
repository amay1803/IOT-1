package com.example.iot_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private static final String SMS_RECEIVED="android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG="SmsBroadcastReceiver";
    private static final int MY_PERMISSION_REQUEST_RECEIVE_SMS = 0;


    Button btnSave,btnView,btnReject;

    TextView tvNumber;
    String msg="",temp="";
    String cmpmsg;
    String phoneNo="";
    static DbHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSave = findViewById(R.id.btnSave);
        btnReject = findViewById(R.id.btnReject);
        btnView = findViewById(R.id.btnView);
        tvNumber = findViewById(R.id.tvNumber);

        db=new DbHandler(this);
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:" + "7977571987"));
                startActivity(i);


            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String r=msg;
                Student s1=new Student(r);
                MainActivity.db.addStudent(s1);
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ViewActivity.class);
                startActivity(i);

            }
        });


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, MY_PERMISSION_REQUEST_RECEIVE_SMS);
            }
        }


    }
    private void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle("Car arrived at your parking area")
                        .setContentText("Check the car number");

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

        NotificationCompat.Builder builder1 = new NotificationCompat.Builder(this).setAutoCancel(true);
        Intent resultIntent=new Intent(this,MainActivity.class);


    }





    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_RECEIVE_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "!!Permission required!!", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }


    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSmsReceiver, filter);
        super.onResume();

    }

    protected void onPause(){
        super.onPause();
        unregisterReceiver(mSmsReceiver);
    }

    public void unregisterReceiver(BroadcastReceiver mSmsReceiver) {
    }

    private BroadcastReceiver mSmsReceiver = new BroadcastReceiver() {





        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"Intent Received "+intent.getAction());


            if (intent.getAction()==SMS_RECEIVED);
            {
                Bundle dataBundle=intent.getExtras();
                if (dataBundle!=null){
                    Object[] mypdu=(Object[])dataBundle.get("pdus");
                    final SmsMessage[] message=new SmsMessage[mypdu.length];

                    for (int i=0; i<mypdu.length;i++){
                        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                            String format=dataBundle.getString("format");
                            message[i]=SmsMessage.createFromPdu((byte[])mypdu[i],format );
                        }
                        else
                        {
                            message[i]=SmsMessage.createFromPdu((byte[])mypdu[i]);

                        }
                        msg=message[i].getDisplayMessageBody();
                        temp=message[i].getDisplayMessageBody();
                        phoneNo = message[i].getDisplayOriginatingAddress();

                    }//7342745444
                    String num=phoneNo;
                    String phn="7342745444";

                   // num = num.replace("+91", "");
                   msg = msg.replace("Sent from your Twilio trial account - ", "");
                  //  temp = temp.replace("Sent from your Twilio trial account - ", "");
                   // if (num.equals(phn)){
                    Pattern pattern = Pattern.compile("Se*");
                    Matcher m = pattern.matcher(temp);
                    boolean bro=false;
                    while (m.find()){bro=true;
                        break;
                    }
                        if(bro){
                    Toast.makeText(context, "Message: " + msg + "\n Number:" + phoneNo, Toast.LENGTH_LONG).show();

                    msg=msg.trim();



                    String result=msg;
                    Boolean result1=db.checkNumber(result);
                    if(!result1==true){
                        tvNumber.setText(msg+"\nVechicle has access ");}
                    else {
                        addNotification();
                        tvNumber.setText(msg+"\nvechicle has no access ");

                    }
                }
                }



            }

        }

    };



}
