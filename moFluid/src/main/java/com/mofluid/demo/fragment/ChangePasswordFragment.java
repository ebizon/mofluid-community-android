package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Validation;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.UserSession;
import com.mofluid.magento2.custome.widget.ShowAlertDialogBox;
import com.mofluid.magento2.database.MyDataBaseAdapter;
import com.mofluid.magento2.service.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by ebizon on 16/12/15.
 */
public class ChangePasswordFragment extends BaseFragment implements View.OnClickListener {
    private EditText edt_new_password;
    private EditText edt_confirm_password, edt_old_password;
    private TextView txtV_update;
    private String TAG;

    private String newpassword;
    private String change_status;
    private ShowAlertDialogBox showAlertDialogBoxObj;
    private SharedPreferences preferences_session;
    private ConnectionDetector cd;
    private  String headerText;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_change_password, null);

        initialized();
        getViewControll(rootView);

        txtV_update.setOnClickListener(this);

        return rootView;
    }

    private void initialized() {
        TAG = getClass().getSimpleName();
        showAlertDialogBoxObj = new ShowAlertDialogBox();
        preferences_session = getActivity().getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);

        cd=new ConnectionDetector(getActivity());
    }

    private void getViewControll(View rootView) {
        edt_new_password=(EditText) rootView.findViewById(R.id.edt_new_password);
        edt_old_password= (EditText)rootView.findViewById(R.id.edt_old_password);
        edt_confirm_password=(EditText) rootView.findViewById(R.id.edt_confirm_password);

        txtV_update=(TextView) rootView.findViewById(R.id.txtV_update);
    }

    @Override
    public void onClick(View view) {
        String new_pass=edt_new_password.getText().toString();
        String confirm_pass=edt_confirm_password.getText().toString();
        switch (view.getId()) {
            case R.id.txtV_update:
                if(Validation.isStrNotEmpty(new_pass)||Validation.isStrNotEmpty(confirm_pass)) {
                    if (isOldNewPssSame()) {
                        if (edt_confirm_password.getText().toString().length() > 5) {
                            if (cd.isConnectingToInternet()) {
                                hitServiceChangePass();
                               /* getActivity().onBackPressed();
                                Toast toast=Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.password_update), Toast.LENGTH_SHORT);
                                toast.show();
                                SimpleProductFragment.cancelToast(toast);*/
                            } else {
                                Activity activity = getActivity();
                                if(isAdded()&&activity!=null)
                                showAlertDialogBoxObj.showCustomeDialogBox(getActivity(), getActivity().getResources().getString(R.string.app_name), getActivity().getResources().getString(R.string.internet_not_avalable));
                            }
                        } else {
                            Activity activity = getActivity();
                            if(isAdded()&&activity!=null) {
                                Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.pass_length_must_be_more_than_six), Toast.LENGTH_SHORT);
                                toast.show();
                                SimpleProductFragment2.cancelToast(toast);
                            }
                        }
                    } else {
                        Activity activity = getActivity();
                        if(isAdded()&&activity!=null) {
                            Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.botth_pass_must_be_same), Toast.LENGTH_SHORT);
                            toast.show();
                            SimpleProductFragment2.cancelToast(toast);
                        }
                    }

                }
                else {
                    Activity activity = getActivity();
                    if(isAdded()&&activity!=null) {
                        Toast toast = Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.blank_entry), Toast.LENGTH_SHORT);
                        toast.show();
                        SimpleProductFragment2.cancelToast(toast);
                    }
                }
                break;
        }
    }

    private void hitServiceChangePass() {
        String strChangePssUrl= WebApiManager.getInstance().getChangePasswordURL(getActivity());
        Log.d("password",strChangePssUrl);

        Object customer_id = preferences_session.getString(ConstantDataMember.USER_INFO_USER_ID, "");
        Object cutomer_usr_name = preferences_session.getString(ConstantDataMember.USER_INFO_USER_NAME, "");

        Object cutomer_oldpassword = EncodeString.encodeStrBase64Bit(edt_old_password.getText().toString());
        Object cutomer_newpassword = EncodeString.encodeStrBase64Bit(edt_new_password.getText().toString());
        String url=String.format(strChangePssUrl, customer_id, cutomer_usr_name, cutomer_oldpassword, cutomer_newpassword);

        Log.d(TAG, "hitServiceChangePass() called with: " + "url = [" + url + "]");
        Log.d("PiyushK", "hitServiceChangePass() called with: " + "url = [" + url + "]");

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

                String message = "";

                if(response!=null){
                    try {
                        JSONObject jsonOBJ =new JSONObject(response);
                        newpassword =jsonOBJ.getString("newpassword");
                        change_status =jsonOBJ.getString("change_status");
                        message =jsonOBJ.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Log.d(TAG, " message " + message);

                if(change_status.equalsIgnoreCase("1")) {
                    UserProfileItem activeUser = UserSession.getActiveUser(getActivity());

                    /*logout if user active*/
                    if(activeUser!=null) {
                        activeUser.setPassword(newpassword);
                        MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
                        dbAdapter.updateOnlyUserProfilePssord(activeUser);
                        UserSession.setSession(getActivity());
                    }
                }

                Toast toast= Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
                toast.show();
                if(message.toString().equals("Incorrect Old Password.")) {
                    showAlertDialogBoxObj.showCustomeDialogBoxWithoutTitle(getActivity(), "Incorrect Old password");
                }
                else {
                    getActivity().onBackPressed();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
            }
        });



    }

    private boolean isOldNewPssSame() {

        return edt_new_password.getText().toString().trim().equals(edt_confirm_password.getText().toString().trim());
    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            headerText = getActivity().getResources().getString(R.string.change_password_header);
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
