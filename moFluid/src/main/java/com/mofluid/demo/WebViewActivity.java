package com.mofluid.magento2;

//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Toast;
//
//
//import com.ebizon.fluid.CCAvenueUtils.AvenuesParams;
//import com.ebizon.fluid.CCAvenueUtils.Constants;
//import com.ebizon.fluid.CCAvenueUtils.RSAUtility;
//import com.ebizon.fluid.CCAvenueUtils.ServiceHandler;
//import com.ebizon.fluid.CCAvenueUtils.ServiceUtility;
//
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EncodingUtils;
//
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.List;
//
//
//public class WebViewActivity extends Activity {
//    private ProgressDialog dialog;
//    String html, encVal;
//
//    private String currency = "INR";
//
//
////    private String merchant_id = "XXXXX";
////    private String access_code = "XXXXXXXXXXXXXXXXXX";
////    private String redirect_url = "http://www.theacecoder.com/RSA/ccavResponseHandler.php";
////    private String cancel_url = "http://www.theacecoder.com/RSA/ccavResponseHandler.php";
////    private String rsa_url = "http://www.theacecoder.com/RSA/GetRSA.php";
//
//
//    private String access_code = "4YRUXLSRO20O8NIH";
//    private String merchant_id = "2";
//    private String redirect_url = "http://122.182.6.216/merchant/ccavResponseHandler.jsp";
//    private String cancel_url = "http://122.182.6.216/merchant/ccavResponseHandler.jsp";
//    private String rsa_url = "http://122.182.6.216/merchant/GetRSA.jsp";
//
//    //NILESH
////    private String merchant_id = "96558";
////    private String access_code = "AVPK64DD93BN73KPNB"; //NILESH
////    private String redirect_url = "http://mofluidios.ebizontech.biz/ccavResponseHandler.php";
////    private String cancel_url = "http://mofluidios.ebizontech.biz/ccavResponseHandler.php";
////    private String rsa_url = "http://mofluidios.ebizontech.biz/GetRSA.php";
//
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.ebizon.fluid.CCAvenueUtils.AvenuesParams;
import com.ebizon.fluid.CCAvenueUtils.Constants;
import com.ebizon.fluid.CCAvenueUtils.RSAUtility;
import com.ebizon.fluid.CCAvenueUtils.ServiceHandler;
import com.ebizon.fluid.CCAvenueUtils.ServiceUtility;


public class WebViewActivity extends Activity {
    private ProgressDialog dialog;
    String html, encVal;

    private String access_code = "4YRUXLSRO20O8NIH";
    private String merchant_id = "2";
    private String redirect_url = "http://122.182.6.216/merchant/ccavResponseHandler.jsp";
    private String cancel_url = "http://122.182.6.216/merchant/ccavResponseHandler.jsp";
    private String rsa_url = "http://122.182.6.216/merchant/GetRSA.jsp";
    private String currency = "INR";
//    private String redirect_url = "http://www.theacecoder.com/RSA/ccavResponseHandler.php";
//    private String cancel_url = "http://www.theacecoder.com/RSA/ccavResponseHandler.php";
//    private String rsa_url = "http://www.theacecoder.com/RSA/GetRSA.php";


    private String amount = null;
    private String order_id = null;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        amount = getIntent().getStringExtra("amount");

        // Calling async task to get display content
        new RenderView().execute();
    }

    /**
     * Async task class to get json by making HTTP call
     * */
    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            dialog = new ProgressDialog(WebViewActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();

            Integer randomNum = ServiceUtility.randInt(0, 9999999);
            order_id = randomNum.toString();

            // Making a request to url and getting response
            List params = new ArrayList();
            params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, access_code));
            params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, order_id));

            String vResponse = sh.makeServiceCall(rsa_url, ServiceHandler.POST, params);
            System.out.println(vResponse);
            if(!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR")==-1){
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, amount));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, currency));
                encVal = RSAUtility.encrypt(vEncVal.substring(0,vEncVal.length()-1), vResponse);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            class MyJavaScriptInterface
            {
                @JavascriptInterface
                public void processHTML(String html)
                {
                    // process the html as needed by the app
                    String status = null;
                    if(html.indexOf("Failure")!=-1){
                        status = "Transaction Declined!";
                    }else if(html.indexOf("Success")!=-1){
                        status = "Transaction Successful!";
                    }else if(html.indexOf("Aborted")!=-1){
                        status = "Transaction Cancelled!";
                    }else{
                        status = "Status Not Known!";
                    }
                    //Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("transStatus",status);
                    setResult(3,intent);
                    finish();
                }
            }

            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    // Dismiss the progress dialog
                    if (dialog.isShowing())
                        dialog.dismiss();
                    if(url.indexOf("/ccavResponseHandler.jsp")!=-1){
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML(''+document.getElementsByTagName('html')[0].innerHTML+'');");
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });

			/* An instance of this class will be registered as a JavaScript interface */
            StringBuffer params = new StringBuffer();
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE,access_code));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID,merchant_id));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID,order_id));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL,redirect_url));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL,cancel_url));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL,URLEncoder.encode(encVal)));

            String vPostParams = params.substring(0,params.length()-1);
            try {
                webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
            } catch (Exception e) {
                showToast("Exception occured while opening webview.");
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }
}