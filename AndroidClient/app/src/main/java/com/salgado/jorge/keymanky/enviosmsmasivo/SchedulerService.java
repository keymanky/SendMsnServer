package com.salgado.jorge.keymanky.enviosmsmasivo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class SchedulerService extends JobService {

    private final String TAG_REQUEST = "MY_TAG";
    private RequestQueue mVolleyQueue;
    private RequestQueue mVolleyQueue2;

    private String idWebService;
    private String urlWebService = "http://localhost/msn/api/getMsn/";
    private String number;
    private String msn;

    private Handler mJobHandler = new Handler( new Handler.Callback() {
        @Override
        public boolean handleMessage( Message msg ) {

            mVolleyQueue = Volley.newRequestQueue(getApplicationContext());
            //getMsn(urlWebService);

            jobFinished( (JobParameters) msg.obj, false );
            return true;
        }
    } );

    @Override
    public boolean onStartJob(JobParameters params ) {
        mJobHandler.sendMessage( Message.obtain( mJobHandler, 1, params ) );
        return true;
    }

    @Override
    public boolean onStopJob( JobParameters params ) {
        mJobHandler.removeMessages( 1 );
        return false;
    }

    private void getMsn(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.toString();
                //showToast(response);

                JSONObject jobj = new JSONObject();
                try {
                    jobj = new JSONObject(response);
                    number = jobj.getString("numero");
                    msn = jobj.getString("msn");
                    idWebService = jobj.getString("id");
                    showToast("Msn: " + msn + " <<" + number + ">> " + idWebService);
                    sendMySMS(number, msn);

                    mVolleyQueue2 = Volley.newRequestQueue(getApplicationContext());
                    updateMsn("http:localhost/msn/api/updateMsn/" + idWebService);

                } catch (JSONException e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if( error instanceof NetworkError) {
                } else if( error instanceof ServerError) {
                } else if( error instanceof AuthFailureError) {
                } else if( error instanceof ParseError) {
                } else if( error instanceof NoConnectionError) {
                } else if( error instanceof TimeoutError) {
                }
            }
        });

        stringRequest.setShouldCache(true);
        stringRequest.setTag(TAG_REQUEST);
        mVolleyQueue.add(stringRequest);
    }

    private void updateMsn(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.toString();
                showToast(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if( error instanceof NetworkError) {
                    //texto_a_imprimir = error.toString();
                } else if( error instanceof ServerError) {
                    //texto_a_imprimir = error.toString();
                } else if( error instanceof AuthFailureError) {
                    //texto_a_imprimir = error.toString();
                } else if( error instanceof ParseError) {
                    //texto_a_imprimir = error.toString();
                } else if( error instanceof NoConnectionError) {
                    //texto_a_imprimir = error.toString();
                } else if( error instanceof TimeoutError) {
                    //texto_a_imprimir = error.toString();
                }
            }
        });
        /*
        //Set a retry policy in case of SocketTimeout & ConnectionTimeout Exceptions. Volley does retry for you if you have specified the policy.
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjRequest.setTag(TAG_REQUEST);
        mVolleyQueue.add(jsonObjRequest);
        */
        stringRequest.setShouldCache(true);
        stringRequest.setTag(TAG_REQUEST);
        mVolleyQueue2.add(stringRequest);
    }

    public void sendMySMS(String number, String msn) {

        List<String> telefonos = new ArrayList<>();
        telefonos.add(telefonos.size(),number);

        for(int i=0;i<telefonos.size();i++){

            try{
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(telefonos.get(i), null, msn, null, null);
            }catch (Exception e){
                showToast("Mensaje enviado sin exito " + e);
            }

        }
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
}