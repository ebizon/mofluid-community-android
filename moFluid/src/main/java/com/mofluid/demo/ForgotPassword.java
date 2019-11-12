package com.mofluid.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ebizon.fluid.Utils.Config;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.LoginDemo;
import com.mofluid.magento2.R;
import com.mofluid.magento2.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassword  extends AppCompatActivity{
    String TAG="ForgotPassword";
    private Button forgot;
    private EditText editTextemail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        forgot=(Button) findViewById(R.id.forgot);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editTextemail=(EditText) findViewById(R.id.input_email);
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboardwithoutPopulate(ForgotPassword.this);
                if(!editTextemail.getText().toString().contains("@")) {
                    Toast.makeText(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_SHORT).show();
                    return;
                }
                Config.getInstance().load(getApplicationContext());
                String url= WebApiManager.getInstance().getResetPasswordAppURL();
                url+=editTextemail.getText().toString();
                GetUser guser=new GetUser();
                guser.execute(url);
            }
        });
    }
    private void gotoLoginDemo(){
        Intent intent=new Intent(ForgotPassword.this, com.mofluid.magento2.LoginDemo.class);
        startActivity(intent);
        finish();
    }
    private class GetUser extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(ForgotPassword.this);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(final String... strings) {
            NetworkAPIManager.getInstance(com.mofluid.magento2.service.AppController.getContext()).sendGetRequest(strings[0],
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG,"response="+response);
                            Toast.makeText(getApplicationContext(),"Email send to you mail id.",Toast.LENGTH_SHORT).show();
                           gotoLoginDemo();
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(TAG,"error"+error.toString());
                            Toast.makeText(com.mofluid.magento2.service.AppController.getContext(),"Invalid email",Toast.LENGTH_SHORT).show();
                        }
                    });

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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}
