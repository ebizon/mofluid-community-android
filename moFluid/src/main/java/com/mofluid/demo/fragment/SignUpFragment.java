package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.HideKeyBoard;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.UserSession;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.database.MyDataBaseAdapter;
import com.mofluid.magento2.service.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Created by ebizon on 4/11/15.
 */
public class SignUpFragment extends BaseFragment implements View.OnClickListener {
    private EditText input_fname,input_lname;
    private EditText input_email;
    private EditText input_password;
    private CheckBox ch_box_show_password;
    private  String headerText;
    private String TAG;
    private String fname,lname;
    private String email;
    private String password;
    private JSONObject jsonOBJ;
    private String registrationStatus;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private TextInputLayout input_layout_fname;
    private TextInputLayout input_layout_lname;
    private TextInputLayout input_layout_email;
    private TextInputLayout input_layout_password;
    private ConnectionDetector cd;
    private ShowAlertDialogBox showAlertDialogBoxObj;
    private String user_email;
    private String user_firstname;
    private String user_lastname;
    private String user_password;
    private String user_id;
    private ProgressDialog pDialog;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_up, null);
        initialized();
        TAG=getClass().getSimpleName();
        getControllsView(rootView);

        ch_box_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.d(TAG, "PassWrdChecBox" + "= " + isChecked);
                if(isChecked) {
                    input_password.setInputType(InputType.TYPE_CLASS_TEXT );

                } else {
                    input_password.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                input_password.setSelection(input_password.getText().length());

            }
        });
        return rootView;
    }

    private void initialized() {
        cd = new ConnectionDetector(getActivity());
        showAlertDialogBoxObj=new ShowAlertDialogBox();
    }

    private void getControllsView(View rootView) {
        TextView txt_login = (TextView) rootView.findViewById(R.id.txt_login);
        txt_login.setOnClickListener(this);
        input_fname=(EditText) rootView.findViewById(R.id.input_fname);
        input_lname=(EditText) rootView.findViewById(R.id.input_lname);
        input_email=(EditText) rootView.findViewById(R.id.input_email);
        input_password=(EditText) rootView.findViewById(R.id.input_password);
        TextView btn_signup = (TextView) rootView.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);
        ch_box_show_password=(CheckBox) rootView.findViewById(R.id.ch_box_show_password);

        input_layout_fname=(TextInputLayout) rootView.findViewById(R.id.input_layout_fname);
        input_layout_lname=(TextInputLayout) rootView.findViewById(R.id.input_layout_lname);
        input_layout_email=(TextInputLayout) rootView.findViewById(R.id.input_layout_email);
        input_layout_password=(TextInputLayout) rootView.findViewById(R.id.input_layout_password);

    }

    @Override
    public void onClick(View v) {
        HideKeyBoard.hideSoftKeyboard(getActivity());
        switch (v.getId()) {
            case R.id.txt_login:
                BaseFragment fragment=new SignInSignUpFragment();
                callFragment(fragment);

                break;

            case R.id.btn_signup:
                fname=input_fname.getText().toString();
                lname=input_lname.getText().toString();
                email=input_email.getText().toString();
                password=input_password.getText().toString();

                if( validate(input_fname,input_lname,input_email,input_password)) {
                    password = Utils.encodeToBase64(password);
                    if(cd.isConnectingToInternet()) {
                        submitSignUpDtaToServer();
                    }
                    else
                    {
                        Activity activity = getActivity();
                    if(isAdded()&&activity!=null)
                        showAlertDialogBoxObj.showCustomeDialogBox(getActivity(), getActivity().getResources().getString(R.string.internet_connection), getActivity().getResources().getString(R.string.internet_not_avalable));
                }
                }

                break;
        }

    }

    private boolean validate(EditText input_fname, EditText input_lname, EditText input_email, EditText input_password) {
        boolean flag = true;
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {

            if (!isFullname(input_fname.getText().toString())) {
                input_layout_fname.setErrorEnabled(true);
                input_layout_fname.setError(getActivity().getResources().getString(R.string.required));
                input_fname.getBackground().setColorFilter(getResources().getColor(R.color.app_color), PorterDuff.Mode.SRC_ATOP);
                flag = false;
            } else {
                input_layout_fname.setErrorEnabled(false);
                input_fname.getBackground().setColorFilter(getResources().getColor(R.color.primary_color), PorterDuff.Mode.SRC_ATOP);
            }

            if (!isFullname(input_lname.getText().toString())) {
                input_layout_lname.setErrorEnabled(true);
                input_layout_lname.setError(getActivity().getResources().getString(R.string.required));
                input_lname.getBackground().setColorFilter(getResources().getColor(R.color.app_color), PorterDuff.Mode.SRC_ATOP);
                flag = false;
            } else {
                input_layout_lname.setErrorEnabled(false);
                input_lname.getBackground().setColorFilter(getResources().getColor(R.color.primary_color), PorterDuff.Mode.SRC_ATOP);
            }

            if (!emailValidate(input_email.getText().toString())) {
                input_layout_email.setErrorEnabled(true);
                input_layout_email.setError(getActivity().getResources().getString(R.string.correct_email));
                input_email.getBackground().setColorFilter(getResources().getColor(R.color.app_color), PorterDuff.Mode.SRC_ATOP);
                flag = false;
            } else {
                input_layout_email.setErrorEnabled(false);
                input_email.getBackground().setColorFilter(getResources().getColor(R.color.primary_color), PorterDuff.Mode.SRC_ATOP);
            }

            if (!(input_password.getText().toString().trim().length() >= 6)) {
                input_layout_password.setErrorEnabled(true);
                input_layout_password.setError(getActivity().getResources().getString(R.string.password_length));
                input_password.getBackground().setColorFilter(getResources().getColor(R.color.app_color), PorterDuff.Mode.SRC_ATOP);
                flag = false;
            } else {
                input_layout_password.setErrorEnabled(false);
                input_password.getBackground().setColorFilter(getResources().getColor(R.color.primary_color), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return flag;
    }

    private void submitSignUpDtaToServer() {
        String strUrl= WebApiManager.getInstance().getSignupURL(getActivity());

        fname = EncodeString.encodeStrBase64Bit(fname);
        lname = EncodeString.encodeStrBase64Bit(lname);

        String url=String.format(strUrl,fname,lname,email,password);
        Log.d(TAG, "submitSignUpDtaToServer() called with: fname " + fname+"");
        Log.d(TAG, "submitSignUpDtaToServer() called with: lname " + lname+"");
        Log.d(TAG, "submitSignUpDtaToServer() called with: email " + email+"");
        Log.d(TAG, "submitSignUpDtaToServer() called with: password " + password+"");
        Log.d(TAG, "submitSignUpDtaToServer() called with: Final " + url+"");

        pDialog = new ProgressDialog(getActivity(),R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();

        SignUpOnServer signUpOnServer = new SignUpOnServer();
        signUpOnServer.execute(url);



    }
    private boolean isFullname(String str) {
        String expression = "^[a-zA-Z\\s]+";
        return str.matches(expression) && !str.trim().equals(" ") && str.trim().length() > 0;
    }
    private static boolean emailValidate(String target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            headerText = getActivity().getResources().getString(R.string.sign_up_header);
//            MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }

    private class SignUpOnServer extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params) {


            NetworkAPIManager.getInstance(getActivity()).sendGetRequest(params[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    String stripeid="0";
                    Log.d("Volley= ", response);
                    if(pDialog.isShowing())
                        pDialog.hide();

                    if(response!=null) {
                        try {
                            jsonOBJ =new JSONObject(response);
                            user_email=jsonOBJ.getString("email");
                            user_firstname=jsonOBJ.getString("firstname");
                            user_lastname=jsonOBJ.getString("lastname");
                            user_password=jsonOBJ.getString("password");
                            registrationStatus=jsonOBJ.getString("status");
                            user_id=jsonOBJ.getString("id");
                            stripeid = jsonOBJ.getString("stripecustid");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    if(registrationStatus.equals("0")) {
                        showAlertDialogBoxObj. showCustomeDialogBoxWithoutTitle(getActivity(),"You are already registered with this email id.");
                    } else {
                        UserProfileItem user= new UserProfileItem(user_firstname, user_id, user_lastname, registrationStatus, user_password,  user_email,stripeid);

                    /*get Active user*/
                        UserProfileItem activeUser = UserSession.getActiveUser(getActivity());

                        MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
                    /*logout if user active*/
                        if(activeUser!=null) {
                            activeUser.setLogin_status("0");
                            dbAdapter.updateOnlyUserProfileStatus(activeUser);
                        }

                        long insrtid= dbAdapter.inserUserProfiletData(user);

                    /*set session*/
                        UserSession.setSession(getActivity());
                        UserManager.getInstance().setUser(user);
                        Log.d(TAG, "onResponse() called with: " + "inserted id = [" + insrtid + "]");
                        BaseFragment fragment=new MyCartFragment();
                        callFragment(fragment);
                        showAlertDialogBoxObj.showCustomeDialogBoxWithoutTitle(getActivity(), "You have registered successfully");
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                    pDialog.hide();
                }
            });

            return null;
        }
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
