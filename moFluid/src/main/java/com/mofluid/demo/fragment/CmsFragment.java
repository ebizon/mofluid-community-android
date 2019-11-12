package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;


/**
 * Created by prashant on 2/5/16.
 */
@SuppressLint("ValidFragment")
public class CmsFragment extends  BaseFragment {
    RequestQueue requestQueue;
    String cms_service_url;
    String page_id;
    int id;
    ProgressDialog progressDialog;
     String title="";
     String content="";
    LinearLayout contentview;
    Typeface font_type;
    TextView content_view;
    View view;

    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    public CmsFragment(String id)
    {
        page_id=id;

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
              if(view==null) {
                  progressDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
                  progressDialog.setCancelable(false);
                  progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                  progressDialog.show();
                  view = inflater.inflate(R.layout.aboutus_layout, null);
                  MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
                  setcontenttoviews(view);
                  hitserviceforpagecontent();

              }
              return view;
    }

    private void setcontenttoviews(View layout) {
        contentview=(LinearLayout)layout.findViewById(R.id.content2);
        font_type = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        content_view=(TextView)layout.findViewById(R.id.content_view);
        content_view.setTypeface(font_type);
//        contentview.setText(content);
    }

    private void hitserviceforpagecontent() {
        requestQueue= Volley.newRequestQueue(getActivity());
        String url = WebApiManager.getInstance().getAllCMSPages(getActivity());
        cms_service_url = String.format(url,page_id);
        Log.d(TAG,"service for Cms called with url "+ cms_service_url);
        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(cms_service_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String strjson) {
                progressDialog.hide();

                try {
                    JSONObject response = new JSONObject(strjson);
                    title = response.getString("title");
                    //MainActivity.setHeaderText(title);
                    mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,title);
                    content = response.getString("content");

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.hide();
                }

                content_view.setText(Html.fromHtml(content));
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
            }
        });


    }

    @Override
    public void onResume() {
//        MainActivity.setHeaderText(title);
    super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

}
