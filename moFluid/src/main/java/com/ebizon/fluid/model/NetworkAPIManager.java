package com.ebizon.fluid.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ebizon.fluid.Utils.Config;
import com.mofluid.magento2.MainActivity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by manish on 25/03/16.
 */
public class NetworkAPIManager {
    private RequestQueue queue;
    private String deviceID="12345",tokenID="",secretKey="";
    private static NetworkAPIManager ourInstance = null;

    public static NetworkAPIManager getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new NetworkAPIManager(context);
        }
        return ourInstance;
    }

    private NetworkAPIManager(Context context){
        if(this.queue == null && context != null){
            this.queue = Volley.newRequestQueue(context);
            this.queue.start();
        }
    }

    public void sendGetRequest(String url, Response.Listener<String> responseListener,
                               Response.ErrorListener errorListener){
        StringRequest urlRequest = new StringRequest(Request.Method.GET,
                url, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authappid", ourInstance.deviceID);
                params.put("token", ourInstance.tokenID);
              //  params.put("secretkey", ourInstance.secretKey);
                params.put("Authorization", Config.getInstance().getMofluiApiAccessKey());
                return params;
            }
        };


            if(this.queue != null){
            this.queue.add(urlRequest);
           // urlRequest.setTag()
        }

        MainActivity.retryRequest(urlRequest);
    }


    public void sendGetRequestNonToken(String url, Response.Listener<String> responseListener,
                               Response.ErrorListener errorListener){
        StringRequest urlRequest = new StringRequest(Request.Method.GET,
                url, responseListener, errorListener){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("authappid", ourInstance.deviceID);
                params.put("token", ourInstance.tokenID);
                params.put("Authorization", Config.getInstance().getMofluiApiAccessKey());
                return params;
            }
        };

        if(this.queue != null){
            this.queue.add(urlRequest);
        }

        MainActivity.retryRequest(urlRequest);
    }
    public void sendPostRequestNonToken(String url, Response.Listener<String> responseListener,
                                        Response.ErrorListener errorListener, final String body){
        StringRequest postRequest=new StringRequest(Request.Method.POST,url,responseListener,errorListener){
            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return body == null ? null : body.getBytes("utf-8");
                }
                catch(UnsupportedEncodingException e){
                    return null;

                }
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", Config.getInstance().getMofluiApiAccessKey());
                return params;
            }
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };
          if(this.queue!=null)
            this.queue.add(postRequest);

    }


    public void setDeviceID(String deviceID) {
        ourInstance.deviceID = deviceID;
    }

    public void setTokenID(String tokenID) {
        ourInstance.tokenID = tokenID;
    }

    public void setSecretKey(String secretKey) {
        ourInstance.secretKey = secretKey;
    }
}
