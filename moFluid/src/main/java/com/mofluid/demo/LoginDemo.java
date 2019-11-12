package com.mofluid.magento2;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.mofluid.demo.ForgotPassword;
import com.mofluid.magento2.service.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by saddam on 13/3/18.
 */

public class LoginDemo extends AppCompatActivity {
    private static final String TAG = "LoginDemo";
    private EditText editTextemail;
    private EditText editTextpassword;
    private Button buttonLogin;
    private JSONObject body;
    private String storeId;
    private final String url="https://api.mofluid.com/appLogin";
    private TextView forgot_password,see_demo,privacy_policy;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_login);
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(),  "fonts/Ubuntu-B.ttf");
        editTextemail=(EditText) findViewById(R.id.input_email);
        editTextpassword=(EditText) findViewById(R.id.input_password);
        buttonLogin=(Button)findViewById(R.id.btn_login);
        forgot_password=(TextView) findViewById(R.id.txt_forgot_password);
        see_demo=(TextView) findViewById(R.id.seedemo);
        privacy_policy=(TextView) findViewById(R.id.privacy_policy1);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 gotoForgotPassword();
            }
        });
        see_demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 gotoMainActivity();
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://mofluid.com/privacy");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        storeId=getIntent().getStringExtra("StoreID");
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboardwithoutPopulate(LoginDemo.this);
                String email=editTextemail.getText().toString();
                String password=editTextpassword.getText().toString();
                if(email==null || !email.contains("@")){
                    Toast.makeText(getApplicationContext(),"Please enter valid email id",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password==null || password.length()<=0){
                    Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                    return;
                }
                body=new JSONObject();
                try {
                    body.put("email", email);
                    body.put("password", password);
                } catch(JSONException e){}
                GetUser user=new GetUser();
                user.execute(url);

            }
        });
    }
 private void gotoForgotPassword(){
     Log.d(TAG,"go to ForgotPassword.");
     Intent intent=new Intent(LoginDemo.this,ForgotPassword.class);
     startActivity(intent);
     //finish();
 }
    private void gotoMainActivity(){
        Log.d(TAG,"go to MaiActivity.");
        Intent intent=new Intent(LoginDemo.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("StoreID",storeId);
        getApplicationContext().startActivity(intent);
        LoginDemo.this.finish();
    }
    private class GetUser extends AsyncTask<String,String,String>{
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(LoginDemo.this);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(final String... strings) {
            NetworkAPIManager.getInstance(AppController.getContext()).sendPostRequestNonToken(strings[0],
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG,"response="+response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String base_url=json.getString("website");
                                String websiteId=json.getString("websiteId");
                                NetworkAPIManager.getInstance(AppController.getContext()).sendGetRequest("https://api.mofluid.com/storeConfig/id/"+websiteId, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.d(TAG,"response="+response);
                                        try {
                                            JSONObject json = new JSONObject(response);
                                            String currency_code=json.getString("currencyCode");
                                            String store_id=json.getString("storeId");
                                            MySharedPreferences.getInstance().set(MySharedPreferences.CURRENCY_CODE,currency_code);
                                            MySharedPreferences.getInstance().set(MySharedPreferences.STORE_ID,store_id);
                                           // gotoMainActivity();
                                        }catch (JSONException e){

                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                });
                                String extention="mofluidapi2";
                                if(base_url.charAt(base_url.length()-1)!='/')
                                    extention="/"+extention;
                                MySharedPreferences.getInstance().set(MySharedPreferences.BASE_URL,base_url+extention);
                                MySharedPreferences.getInstance().set(MySharedPreferences.isLogedin,"true");
                                 gotoMainActivity();
                            }catch (JSONException e){
                                Toast.makeText(AppController.getContext(),"Invalid email or password",Toast.LENGTH_SHORT).show();
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG,"error"+error.toString());
                            Toast.makeText(AppController.getContext(),"Invalid email or password",Toast.LENGTH_SHORT).show();
                        }
                    },body.toString());

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

        }
    }
}
