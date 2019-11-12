package com.mofluid.magento2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.AddressData;
import com.mofluid.magento2.fragment.BaseFragment;
import com.mofluid.magento2.fragment.OrderAcknowledgeFragment;

/**
 * Created by ebizon on 3/4/17.
 */
// TODO: 03/08/18 Change the name from CcAvenue to CcAvenueFragment
public class CcAvenue  extends BaseFragment {

    private AddressData billingGuest;
    private  AddressData shippingGuest;
    private  boolean isGuest;
    private  boolean isPaymentDone;
    private  String orderid;
    private  String finalUrl;
    String TAG="KnetPaymentFragment";
    View rootView;

    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    public CcAvenue() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public CcAvenue(String url, String orderid, boolean isPaymentDone, AddressData billingGuest, AddressData shippingGuest, boolean isGuest) {
        this.finalUrl=url;
        this.orderid=orderid;
        this.isPaymentDone=isPaymentDone;
        this.billingGuest=billingGuest;
        this.shippingGuest=shippingGuest;
        this.isGuest=isGuest;
    }

    @SuppressLint("ValidFragment")
    public CcAvenue(String finalUrl, String orderId, boolean isPaymentDone) {
        this.finalUrl=finalUrl;
        this.orderid=orderId;
        this.isPaymentDone=isPaymentDone;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_ccavenue, container, false);
        initialized(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
      //  MainActivity.showBackArrow(true);
        //MainActivity.setHeaderText("Payment");
        mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,"Payment");
    }

    private void initialized(View rootView) {
        final boolean[] flag = {false};
        WebView webviewKnetPayment=(WebView) rootView.findViewById(R.id.webviewKnetPayment);
        WebSettings mWebSettings = webviewKnetPayment.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        webviewKnetPayment.addJavascriptInterface(this, "androidCallBackInterface");
        webviewKnetPayment.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                Log.d(TAG, "shouldOverrideUrlLoading() called with:  url = [" + url + "]");
                return true;
            }
            public void onLoadResource (WebView view, String url) {
                if(!flag[0]) {
                    flag[0] =true;
                    showProgreassBar();
                }
            }
            public void onPageFinished(WebView view, String url) {
                hideProgressBar();
            }
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                hideProgressBar();
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.payment_fails),Toast.LENGTH_SHORT).show();
                getActivity().getFragmentManager().popBackStackImmediate();
                Log.d(TAG, "onReceivedError() called with: view = [" + view + "], errorCode = [" + errorCode + "], description = [" + description + "], failingUrl = [" + failingUrl + "]");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                hideProgressBar();
                Log.d(TAG, "onReceivedHttpError() called with: view = [" + view + "], request = [" + request + "], errorResponse = [" + errorResponse + "]");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                hideProgressBar();
                Log.d(TAG, "onReceivedError() called with: view = [" + view + "], request = [" + request + "], error = [" + error + "]");
            }

        });
        Log.d(TAG, "initialized() called with: finalUrl = [" + finalUrl + "]");
        webviewKnetPayment.loadUrl(finalUrl);
    }

    private void hideProgressBar() {
        LinearLayout llShowProgressBar=(LinearLayout) rootView.findViewById(R.id.llShowProgressBar);
        llShowProgressBar.setVisibility(View.GONE);
    }

    private void showProgreassBar() {
        LinearLayout llShowProgressBar=(LinearLayout) rootView.findViewById(R.id.llShowProgressBar);
        llShowProgressBar.setVisibility(View.VISIBLE);
    }

    @JavascriptInterface
    public void callBackFormData(String data){
        if (isGuest == false) {
            if (data.equalsIgnoreCase("success")) {
                callFragment(new OrderAcknowledgeFragment(orderid, true));
            } else {
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.payment_fails),Toast.LENGTH_SHORT).show();
               getActivity().getFragmentManager().popBackStackImmediate();
            }
        }
        else {
            if (data.equalsIgnoreCase("success")) {
                callFragment(new OrderAcknowledgeFragment(orderid, true, billingGuest, shippingGuest, isGuest));
            } else {
                Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.payment_fails),Toast.LENGTH_SHORT).show();
                getActivity().getFragmentManager().popBackStackImmediate();
            }
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        //Unregister the listener
        mListener = null;
    }


}
