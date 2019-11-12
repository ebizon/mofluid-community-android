package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.HideKeyBoard;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.service.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ebizon on 4/11/15.
 */
public class ForgotPasswordFragment extends BaseFragment {

    private EditText input_email;
    private TextView btn_retriev_pasword;
    private String headerText;
    private String TAG;
    private JSONObject jsonOBJ;
    private String forgetPassResponse;
    private ConnectionDetector cd;
    private ShowAlertDialogBox showAlertDialogBoxObj;
    private TextInputLayout input_layout_email;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forgot_password, null);
        getviewControlls(rootView);
        initialized();
        TAG=getClass().getSimpleName();

        btn_retriev_pasword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HideKeyBoard.hideSoftKeyboard(getActivity());


                if(cd.isConnectingToInternet()) {
                    if(isAllFieldFilled()) {
                        hitServiceVerifyLogin(input_email.getText().toString());
                    }

                }
                else {
                    Activity activity = getActivity();
                    if (isAdded() && activity != null)
                        showAlertDialogBoxObj.showCustomeDialogBox(getActivity(), getActivity().getResources().getString(R.string.app_name), getActivity().getResources().getString(R.string.internet_not_avalable));
                }
            }

        });
        return rootView;
    }

    private void getviewControlls(View rootView) {

        input_email=(EditText) rootView.findViewById(R.id.input_email);
        btn_retriev_pasword=(TextView) rootView.findViewById(R.id.btn_retriev_pasword);
        input_layout_email=(TextInputLayout) rootView.findViewById(R.id.input_layout_email);




    }
    private void initialized() {
        cd = new ConnectionDetector(getActivity());
        showAlertDialogBoxObj=new ShowAlertDialogBox();
    }


    private void hitServiceVerifyLogin(String email) {
        String strUrl= WebApiManager.getInstance().getForgotPasswordURL(getActivity());
        String url=String.format(strUrl,email);
        final ProgressDialog pDialog = new ProgressDialog(getActivity(),R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();
        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Log.d("Volley= ", response);
                if(pDialog.isShowing())
                    pDialog.hide();

                if(response!=null)
                {

                    try {
                        jsonOBJ =new JSONObject(response);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        forgetPassResponse =jsonOBJ.getString("response");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                Log.d(TAG, "Response " + forgetPassResponse);

                if(forgetPassResponse.equalsIgnoreCase("error"))
                {
                    showAlertDialogBoxObj.showCustomeDialogBoxWithoutTitle(getActivity(),"Please Enter Valid Email");
                }
                else
                {
                    showAlertDialogBoxObj. showCustomeDialogBoxWithoutTitle(getActivity(),"Change Your Password");
                }
                //Toast.makeText(getActivity(), "LoginStatus " + forgetPassResponse, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });


    }
    private boolean isAllFieldFilled() {
        boolean flag=true;
        if(!emailValidate(input_email.getText().toString())) {
            input_layout_email.setErrorEnabled(true);
            Activity activity = getActivity();
            if (isAdded() && activity != null)
            {
                input_layout_email.setError(getActivity().getResources().getString(R.string.required));
            input_email.getBackground().setColorFilter(getResources().getColor(R.color.app_color), PorterDuff.Mode.SRC_ATOP);
        }
            flag=false;
        }
        else
        {
            input_layout_email.setErrorEnabled(false);
            //  input_layout_fname.setError(getActivity().getResources().getString(R.string.required));
            Activity activity = getActivity();
            if(isAdded()&&activity!=null)
            input_email.getBackground().setColorFilter(getResources().getColor(R.color.primary_color), PorterDuff.Mode.SRC_ATOP);
        }

        return flag;
    }

    private static boolean emailValidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            headerText = getActivity().getResources().getString(R.string.forgot_password_header);
//            MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        try {
            // TODO: 03/08/18 Find out what this code does
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
