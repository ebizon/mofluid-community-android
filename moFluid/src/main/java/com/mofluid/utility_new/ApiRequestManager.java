package com.mofluid.utility_new;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ApiRequestManager {
private final String TAG="ApiRequestManager";
private static ApiRequestManager instance;
private RequestQueue requestQueue;
private ApiRequestManager(){
    Context context= com.mofluid.magento2.service.AppController.getContext();
    if(this.requestQueue==null && context!=null){
        this.requestQueue = Volley.newRequestQueue(context);
        this.requestQueue.start();
    }
}
public static ApiRequestManager getInstance(){
    if(instance==null)
        instance=new ApiRequestManager();
    return instance;
}
public void sendGetRequest(String url, Response.Listener<String> responseListener, Response.ErrorListener errorListener, final Map<String, String> header){
    StringRequest urlRequest = new StringRequest(Request.Method.GET,
            url, responseListener, errorListener){
        @Override
        public Map<String, String> getHeaders() {
            return header;
        }
    };
 if(this.requestQueue!=null) this.requestQueue.add(urlRequest);
}

public void sendPostRequest(String url, Response.Listener<String> responseListener, Response.ErrorListener errorListener, final Map<String, String> header, final String body, final String content_tpye){
    StringRequest postRequest=new StringRequest(Request.Method.POST,url,responseListener,errorListener){
        @Override
        public byte[] getBody() {
            L.d(TAG,"body="+body);
            try {
                return body == null ? null : body.getBytes("utf-8");
            }
            catch(UnsupportedEncodingException e){
                return null;

            }
        }

        @Override
        public Map<String, String> getHeaders() {
            L.d(TAG,"Header="+header.toString());
            return header;
        }

        @Override
        public String getBodyContentType() {
             return content_tpye;
        }
    };
    if(this.requestQueue!=null) this.requestQueue.add(postRequest);

}
}
