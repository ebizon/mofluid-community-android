package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Utils;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;

import java.lang.reflect.Field;

public class MyCheckOutFragment extends BaseFragment implements View.OnClickListener {

    private RadioButton radio_btn_as_guest;
    private RadioButton radio_btn_create_account;
    private RadioButton radio_btn_signin;
    private TextView txtV_continue;
    private Typeface lat0_Font_ttf;
    private RelativeLayout enable_text_email;
    private EditText user_email;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_check_out, container, false);
        MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
//        MainActivity.ivAppLogo.setVisibility(View.INVISIBLE);
//        MainActivity.product_name.setText("");
        initialized();
        getControllView(rootView);
        setFontStyle();
        return rootView;
    }

    @Override
    public void onResume() {
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            String headerText = getActivity().getResources().getString(R.string.blank_header);
            //MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }

    private void setFontStyle() {
        radio_btn_as_guest.setTypeface(lat0_Font_ttf);
        radio_btn_create_account.setTypeface(lat0_Font_ttf);
        radio_btn_signin.setTypeface(lat0_Font_ttf);
        txtV_continue.setTypeface(lat0_Font_ttf);
    }

    private void initialized() {

        lat0_Font_ttf = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);

    }

    private void getControllView(View rootView) {


        radio_btn_as_guest=(RadioButton) rootView.findViewById(R.id.radio_btn_as_guest);
        radio_btn_as_guest.setOnClickListener(this);
        radio_btn_create_account=(RadioButton) rootView.findViewById(R.id.radio_btn_create_account);
        radio_btn_create_account.setOnClickListener(this);
        radio_btn_signin=(RadioButton) rootView.findViewById(R.id.radio_btn_signin);
        radio_btn_signin.setOnClickListener(this);
        txtV_continue=(TextView) rootView.findViewById(R.id.txtV_continue);
        txtV_continue.setOnClickListener(this);
        enable_text_email=(RelativeLayout)rootView.findViewById(R.id.rl_check_out_email_guest);
        user_email=(EditText)rootView.findViewById(R.id.edt_email_guest);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.radio_btn_as_guest:
                if(radio_btn_as_guest.isChecked())
                {   enable_text_email.setVisibility(View.VISIBLE);
                    radio_btn_create_account.setChecked(false);
                    radio_btn_signin.setChecked(false);
                }

                break;
            case R.id.radio_btn_create_account:
                if(radio_btn_create_account.isChecked())
                {
                    enable_text_email.setVisibility(View.GONE);
                    radio_btn_as_guest.setChecked(false);
                    radio_btn_signin.setChecked(false);

                }

                break;
            case R.id.radio_btn_signin:
                if(radio_btn_signin.isChecked())
                {
                    enable_text_email.setVisibility(View.GONE);
                    radio_btn_as_guest.setChecked(false);
                    radio_btn_create_account.setChecked(false);
                }

                break;
            case R.id.txtV_continue:

                FragmentManager fm = getActivity().getFragmentManager();
                Utils.hideKeyboardwithoutPopulate(getActivity());
                // callFragment();
                if(radio_btn_signin.isChecked()) {
                    fm.popBackStackImmediate();
                    callFragment(new SignInSignUpFragment());
                    WelcomeFragment2.isOrdering = true;
                }
                if(radio_btn_create_account.isChecked()) {
                    fm.popBackStackImmediate();
                    callFragment(new SignUpFragment());
                    WelcomeFragment2.isOrdering = true;
                }
                if(radio_btn_as_guest.isChecked()) {
                    String guest=user_email.getText().toString();
                    boolean validEmail =SignInSignUpFragment.emailValidate(guest);
                    if(validEmail==true)
                    {
                        HomeFragment.userEmail=guest;
                       // BillingAndShippingAddressFragment.isGuest=true;
                        fm.popBackStackImmediate();
                        callFragment(new BillingAndShippingAddressFragment());

                    }
                    else
                    {
                        Toast.makeText(this.getActivity(),"Please Enter Valid email", Toast.LENGTH_SHORT).show();
                    }

                }
//


                break;
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