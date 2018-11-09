package com.salgado.jorge.keymanky.enviosmsmasivo;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText mEdit;
    private JobScheduler mJobScheduler;

    RequestQueue queue = Volley.newRequestQueue(this);
    String url ="http://www.google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Detectamos los permisos necesarios
        int hasSMSPermission = checkSelfPermission(Manifest.permission.SEND_SMS);
        if (hasSMSPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.SEND_SMS},0);
        }

        //Detectamos los permisos necesarios 2
        int hasSMSPermission2 = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (hasSMSPermission2 != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},0);
        }

        //Detectamos los permisos necesarios 3
        int hasSMSPermission3 = checkSelfPermission(Manifest.permission.INTERNET);
        if (hasSMSPermission3 != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.INTERNET},0);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mEdit = (EditText)findViewById(R.id.editText);

        //Instacia del Cron
        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.Run) {

            JobInfo.Builder builder = new JobInfo.Builder( 1,
                    new ComponentName( getPackageName(), SchedulerService.class.getName() ) );


            builder.setPeriodic( 12000 );//Every 12 seconds
            if( mJobScheduler.schedule( builder.build() ) <= 0 ) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                }
            }

            return true;

        }

        if (id == R.id.PruebaMsn) {
            SendMySMS();
            return true;
        }

        if (id == R.id.PruebaVolley) {
            MakeRequest();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showToast(String msg) {
        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    public void SendMySMS() {

        List<String> telefonos = new ArrayList<>();
        telefonos.add(telefonos.size(),"5555555555");
//        telefonos.add(telefonos.size(),"551020304050");

        for(int i=0;i<telefonos.size();i++){

            String message = mEdit.getText().toString();
            SmsManager sms = SmsManager.getDefault();

            // if message length is too long messages are divided
            List<String> messages = sms.divideMessage(message);
            try{
                for (String msg : messages) {
                    //PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), 0);
                    //PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), 0);
                    //sms.sendTextMessage(phone, null, msg, null, null);
                    sms.sendTextMessage(telefonos.get(i), null, msg, null, null);
                    showToast("Envio :" + telefonos.get(i) + " : (" + i + " de " + telefonos.size() + ")" );
                }
            }catch (Exception e){
                //Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.android.mms");
                //startActivity(launchIntent);
            }
        }




    }

    public void MakeRequest(){


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        showToast("Response is: "+ response.substring(0,500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showToast("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
