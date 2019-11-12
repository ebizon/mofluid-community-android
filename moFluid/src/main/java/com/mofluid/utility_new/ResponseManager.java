package com.mofluid.utility_new;

import android.os.AsyncTask;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.mofluid.model_new.RespoonseCodeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ResponseManager {
    private final String TAG = "ResponseManager";
    private String URL;
    private ResponseReceived received;
    public final static int REQUEST_TYPE_GET = 0;
    public final static int REQUEST_TYPE_PUT = 1;
    public final static int REQUEST_TYPE_POST = 2;
    private int REQUEST_TYPE;
    private Map<String, String> header;
    private String body;
    private String content_type;
    public ResponseManager(final int REQUEST_TYPE, String URL, Map<String, String> header, String body, ResponseReceived received) {
        this.URL = URL;
        this.received = received;
        this.header = header;
        this.body = body;
        this.REQUEST_TYPE = REQUEST_TYPE;
        this.content_type="application/json; charset=utf-8";
    }
    public ResponseManager(final int REQUEST_TYPE, String URL, Map<String, String> header, String body,String content_type, ResponseReceived received) {
        this.URL = URL;
        this.received = received;
        this.header = header;
        this.body = body;
        this.REQUEST_TYPE = REQUEST_TYPE;
        this.content_type=content_type;
    }



    public void get() {
        new MyAsynckTask().execute(this.URL);
    }

    private class MyAsynckTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private boolean isValidResponse(String res) {
            /*try {
                new JSONObject(res);
            } catch (JSONException ex) {
                try {
                    new JSONArray(res);
                } catch (JSONException ex1) {
                    return true;
                }
            }*/
            return true;
        }

        @Override
        protected String doInBackground(final String... strings) {
            L.d(TAG, "url=" + strings[0]);
            ApiRequestManager manager = ApiRequestManager.getInstance();
            switch (REQUEST_TYPE) {
                case REQUEST_TYPE_GET:
                    L.d(TAG, "Request type GET");
                    if(header!=null)
                    L.d(TAG, "Header=" + header.toString());
                    manager.sendGetRequest(strings[0], new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    L.d(TAG, "response=" + response);
                                    if (isValidResponse(response))
                                        received.onResponseReceived(response,200 );
                                    else
                                        received.onResponseReceived(null, RespoonseCodeMap.WRONG_RESPONSE);

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    int res_code=error!=null && error.networkResponse!=null?error.networkResponse.statusCode:RespoonseCodeMap.NO_INTERNETCONNECTION;
                                    L.e(TAG, "Error=" + error.toString());
                                    received.onResponseReceived(null,res_code);
                                }
                            }, header);
                    break;
                case REQUEST_TYPE_PUT:
                    break;
                case REQUEST_TYPE_POST:
                    L.d(TAG, "Request type POST");
                    manager.sendPostRequest(strings[0], new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    L.d(TAG, "response=" + response);
                                    if (isValidResponse(response))
                                        received.onResponseReceived(response, 200);
                                    else
                                        received.onResponseReceived(null,RespoonseCodeMap.WRONG_RESPONSE);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    int res_code=error!=null && error.networkResponse!=null?error.networkResponse.statusCode:RespoonseCodeMap.NO_INTERNETCONNECTION;
                                    L.e(TAG, "Error=" + error.toString() + " " + error.networkResponse + " " + error.getMessage());
                                    received.onResponseReceived(null,res_code);
                                }
                            }, header, body,content_type);
                    break;
                default:

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
