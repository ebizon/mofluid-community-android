package com.mofluid.magento2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.AddCartResponse;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.ShoppingCart;
import com.ebizon.fluid.model.ShoppingCartItem;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
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
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Pattern;


/**
 * Created by ebizon on 3/11/15.
 */
public class SignInSignUpFragment extends BaseFragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener, AddCartResponse {
    private static final int GoogleSignInCode = 9001;
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public static int Fb_tracker = 0;
    private GoogleApiClient googleApiClient;
    private EditText input_email;
    private EditText input_password;
    private TextInputLayout input_layout_email;
    private TextInputLayout input_layout_password;
    private CheckBox ch_box_show_password;
    private String TAG;
    private TextView txt_Login;
    private ShowAlertDialogBox showAlertDialogBoxObj;
    private ConnectionDetector cd;
    private final FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override

        public void onSuccess(LoginResult loginResult) {
            Log.d("saddam","sucess for fb login");
            AccessToken accessToken = loginResult.getAccessToken();
            Log.d(TAG, "onSuccess() called with: accessToken" + " = [" + accessToken + "]");
            Log.d(TAG, "onSuccess() called with: getUserId" + " = [" + accessToken.getUserId() + "]");
            Log.d(TAG, "onSuccess() called with: getApplicationId" + " = [" + accessToken.getApplicationId() + "]");
            Log.d(TAG, "onSuccess() called with: getToken" + " = [" + accessToken.getToken() + "]");
            Log.d(TAG, "onSuccess() called with: getPermissions" + " = [" + accessToken.getPermissions() + "]");

            if (cd.isConnectingToInternet()) {
                getFBProfileDetails(accessToken.getToken());
            } else {
                Activity activity = getActivity();
                if(isAdded()&&activity!=null)
                    showAlertDialogBoxObj.showCustomeDialogBoxWithoutTitle(getActivity(), getActivity().getResources().getString(R.string.internet_not_avalable));
            }
        }

        @Override
        public void onCancel() {
            WelcomeFragment2.isSocialLogin =false;
            Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            WelcomeFragment2.isSocialLogin =false;
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }
    };
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton login_button_fb;
    private Typeface open_sans_regular;
    private TextView txtV_item_counter;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;

    public SignInSignUpFragment() {
        //Empty
    }

    static boolean emailValidate(String target) {
//        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
//        return matcher.find();
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "Fragment onActivityResult() called resultCode= " + resultCode + " requestCode= " + requestCode);
        if (requestCode == GoogleSignInCode) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                handleGoogleSignInSuccess(result);
            } else {
                Activity activity = getActivity();
                if(isAdded()&&activity!=null)
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.google_plus_failed), Toast.LENGTH_SHORT).show();

            }
        } else {
            MainActivity.mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onConnected(Bundle bundle) {
        getProfileInformation();
    }

    private void getProfileInformation() {
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
            public void onResult(GoogleSignInResult result) {
                Log.e(TAG, "handleSignInResult:" + result.isSuccess());
                if (result.isSuccess()) {
                    handleGoogleSignInSuccess(result);
                } else {
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intent, GoogleSignInCode);
                }
            }
        });
    }

    private void handleGoogleSignInSuccess(GoogleSignInResult result) {

        GoogleSignInAccount account = result.getSignInAccount();
        try {
            String email = account.getEmail();
            String name = account.getDisplayName();
            if ((email != null) && (name != null)) {
                doSocialLogin(email, name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int status) {
        googleApiClient.connect();
    }

    @Override
    public void onResume() {
        //if (Fb_tracker == 2) {
          //  login_button_fb.performClick();
            //Fb_tracker = 0;
        //}
        MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            String headerText = getActivity().getResources().getString(R.string.login_header);
//            MainActivity.setHeaderText(headerText);
            mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sign_in_sign_up, null);
        MainActivity.ivAppLogo.setVisibility(View.INVISIBLE);
        MainActivity.product_name.setText("LOGIN");
        MainActivity.product_name.setVisibility(View.VISIBLE);
        TAG = getClass().getSimpleName();
        initialized();
        getViewControlls(rootView);
        ch_box_show_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.d(TAG, "PassWrdChecBox" + "= " + isChecked);
                if (isChecked) {
                    input_password.setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    input_password.setInputType(InputType.TYPE_CLASS_TEXT |
                            InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }

                input_password.setSelection(input_password.getText().length());
            }
        });

        txt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = input_email.getText().toString();
                String password = input_password.getText().toString();
                password = Utils.encodeToBase64(password);
                if (isAllFieldFilled()) {

                    String strUrl = WebApiManager.getInstance().getSignInURL(getActivity());
                    String url = String.format(strUrl, email, password);
                    WelcomeFragment2.isSocialLogin =false;
                    hitServiceverifyLogin(url);
                    if (!cd.isConnectingToInternet()) {
                        Activity activity = getActivity();
                        if(isAdded()&&activity!=null)
                            showAlertDialogBoxObj.showCustomeDialogBox(getActivity(), getActivity().getResources().getString(R.string.app_name), getActivity().getResources().getString(R.string.internet_not_avalable));
                    }
                }
            }
        });


        FrameLayout txt_login_with_facebook = (FrameLayout) rootView.findViewById(R.id.facebook_login_button);
        txt_login_with_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fb_tracker = 1;
                login_button_fb.performClick();

            }
        });

        FrameLayout txt_login_with_googleplus = (FrameLayout) rootView.findViewById(R.id.gmail_login_button);
        txt_login_with_googleplus.setOnClickListener(this);
//        loginButton.setBackgroundResource(R.drawable.cart_big);
//        loginButton.setText("Login");
//       login_button_fb.setCompoundDrawables(null, null, null, null);
        login_button_fb.setReadPermissions("public_profile", "user_friends", "email");
        login_button_fb.registerCallback(MainActivity.mCallbackManager, callback);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        /*googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();*/

        return rootView;

    }

    private boolean isAllFieldFilled() {
        boolean flag = true;
        Activity activity = getActivity();
        if(isAdded()&&activity!=null) {
            if (!emailValidate(input_email.getText().toString())) {
                input_layout_email.setErrorEnabled(true);
                input_layout_email.setError(getActivity().getResources().getString(R.string.correct_email));
                input_email.getBackground().setColorFilter(getResources().getColor(R.color.app_color), PorterDuff.Mode.SRC_ATOP);
                flag = false;
            } else {
                input_layout_email.setErrorEnabled(false);
                input_email.getBackground().setColorFilter(getResources().getColor(R.color.primary_color), PorterDuff.Mode.SRC_ATOP);
            }
            if (!(input_password.getText().toString().trim().length() > 0)) {
                input_layout_password.setErrorEnabled(true);
                input_layout_password.setError(getActivity().getResources().getString(R.string.password_Required));
                input_password.getBackground().setColorFilter(getResources().getColor(R.color.app_color), PorterDuff.Mode.SRC_ATOP);
                flag = false;
            } else {
                input_layout_password.setErrorEnabled(false);
                input_password.getBackground().setColorFilter(getResources().getColor(R.color.primary_color), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return flag;
    }

    private void initialized() {
        showAlertDialogBoxObj = new ShowAlertDialogBox();
        cd = new ConnectionDetector(getActivity());
        open_sans_regular = Typeface.createFromAsset(getActivity().getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
    }

    private void getViewControlls(View rootView) {
        TextView txt_create_an_account = (TextView) rootView.findViewById(R.id.txt_create_an_account);
        txt_create_an_account.setTypeface(open_sans_regular);
        txt_create_an_account.setOnClickListener(this);

        TextView txt_forgot_password = (TextView) rootView.findViewById(R.id.txt_forgot_password);
        txt_forgot_password.setTypeface(open_sans_regular);
        txt_forgot_password.setOnClickListener(this);
        login_button_fb = (LoginButton) rootView.findViewById(R.id.login_button);
        txtV_item_counter = (TextView) rootView.findViewById(R.id.txtV_item_counter);
        input_email = (EditText) rootView.findViewById(R.id.input_email);
        input_email.setTypeface(open_sans_regular);
        input_password = (EditText) rootView.findViewById(R.id.input_password);
        input_password.setTypeface(open_sans_regular);
        input_layout_email = (TextInputLayout) rootView.findViewById(R.id.input_layout_email);
        input_layout_email.setTypeface(open_sans_regular);
        input_layout_password = (TextInputLayout) rootView.findViewById(R.id.input_layout_password);
        input_layout_password.setTypeface(open_sans_regular);
        ch_box_show_password = (CheckBox) rootView.findViewById(R.id.ch_box_show_password);
        ch_box_show_password.setTypeface(open_sans_regular);

        txt_Login = (TextView) rootView.findViewById(R.id.txt_Login);
    }

    @Override
    public void onClick(View v) {
        HideKeyBoard.hideSoftKeyboard(getActivity());
        switch (v.getId()) {
            case R.id.txt_forgot_password:
                BaseFragment fragment = new ForgotPasswordFragment();
                callFragment(fragment);

                break;
            case R.id.txt_create_an_account:
                fragment = new SignUpFragment();
                callFragment(fragment);

                break;
            case R.id.gmail_login_button:
                signIn();
                WelcomeFragment2.isSocialLogin= true;
                break;

        }
    }

    private void signIn() {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        WelcomeFragment2.isSocialLogin= false;
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void hitServiceverifyLogin(String Url) {
        Log.d(TAG, "hitServiceverifyLogin() called with: " + "Url = [" + Url + "]");
        Log.d("PiyushK", "hitServiceverifyLogin() called with: " + "Url = [" + Url + "]");
        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();

        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(Url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Log.d("Volley= ", response);
                if (pDialog.isShowing())
                    pDialog.hide();

                try {
                    String email = "", loginStatus = "", firstName = "", lastName = "", password = "", userId = "",stripeid = "0";
                    JSONObject jsonOBJ = new JSONObject(response);
                    Log.d("saddam","hitServiceverifyLogin response="+response);
                    email = jsonOBJ.getString("username");
                    loginStatus = jsonOBJ.getString("login_status");
                    if (Integer.valueOf(loginStatus) > 0) {
                        firstName = jsonOBJ.getString("firstname");
                        lastName = jsonOBJ.getString("lastname");
                        userId = jsonOBJ.getString("id");
                        if(jsonOBJ.has("stripecustid"))
                            stripeid = jsonOBJ.getString("stripecustid");

                        if (jsonOBJ.has("password")) {
                            password = jsonOBJ.getString("password");
                        }


                        UserProfileItem user = new UserProfileItem(firstName, userId, lastName, loginStatus, password, email,stripeid);
                        MyDataBaseAdapter dbAdapter = new MyDataBaseAdapter(getActivity());
                        if (dbAdapter.isUserAvailable(email)) {
                            dbAdapter.updateOnlyUserProfileStatus(user);
                        } else {
                            dbAdapter.inserUserProfiletData(user);
                        }


                        SharedPreferences mySharedPreference=getActivity().getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
                        SharedPreferences.Editor preference_editor_session = mySharedPreference.edit();
                        preference_editor_session.putString(ConstantDataMember.USER_INFO_STRIPE_ID,stripeid);
                        preference_editor_session.commit();

                        UserSession.setSession(getActivity());
                        View view = getActivity().getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                        FragmentManager fm = getActivity().getFragmentManager();
                        syncCartwithServer();
                        UserManager.getInstance().setUser(user);
                        ShoppingCart.getInstance().setCartFromServer();
                        MainActivity.setCounterItemAddedCart();
                        fm.popBackStackImmediate();
                        Log.d("saddam","Mycart called");
                        callFragment(new MyCartFragment());
                    }
                    else {
//                            final Toast toast = Toast.makeText(getActivity(), "Please enter correct details !", Toast.LENGTH_LONG);
//                            toast.show();
//                            SimpleProductFragment.cancelToast(toast);
                        showAlertDialogBoxObj.showCustomeDialogBoxWithoutTitle(getActivity(), "User ID or password is incorrect");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                final Toast toast = Toast.makeText(getActivity(), "Slow Internet Connection !", Toast.LENGTH_LONG);
                toast.show();
                SimpleProductFragment.cancelToast(toast);
                pDialog.hide();
            }
        });
    }



    private void getFBProfileDetails(String tokesn) {
        String strUrl = WebApiManager.getInstance().getFacebookURL() + tokesn;
        Log.d(TAG, "ful url  " + strUrl + "");
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();
        Log.d("saddam","getFBProfileDetails url="+strUrl);
        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(strUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);

                if (pDialog.isShowing())
                    pDialog.hide();

                if (response != null) {
                    Log.d("saddam","getFBProfileDetails response="+response);
                    try {
                        JSONObject jsnOBJ = new JSONObject(response);
                        String email=null,name=null;
                        if(jsnOBJ.has("email"))
                        email = jsnOBJ.getString("email");
                        if(jsnOBJ.has("name"))
                        name = jsnOBJ.getString("name");
                        if(email!=null && name!=null)
                        doSocialLogin(email, name);
                        else{
                            LoginManager.getInstance().logOut();
                            Toast.makeText(getActivity(),"Your account is not registred with email on facebook",Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void doSocialLogin(String email, String name) {
        String nameArr[] = name.split(" ");
        String firstName = nameArr[0];
        String lastName = "";
        if (nameArr.length > 1) {
            lastName = nameArr[1];
            //lastName=EncodeString.encodeStrBase64Bit(lastName);
        }
        //firstName=EncodeString.encodeStrBase64Bit(firstName);
        String strUrl = WebApiManager.getInstance().getSocialLogingURL(getActivity());
        String url = String.format(strUrl, email, firstName, lastName);
        WelcomeFragment2.isSocialLogin =true;
        Log.d("saddam","doSocialLogin url="+url);
        hitServiceverifyLogin(url);
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

    private void syncCartwithServer() {
       /* if(ShoppingCart.getInstance().getCartItems().size()>0)
        {
            UserProfileItem currentuser = UserManager.getInstance().getUser();
            if(currentuser!=null && currentuser.getLogin_status()!="0") {
                Collection<ShoppingCartItem> shopItem= ShoppingCart.getInstance().getCartItems();
                Iterator<ShoppingCartItem> it = shopItem.iterator();
                while(it.hasNext())
                {
                    ShoppingCartItem item = it.next();
                    String customerid = currentuser.getId();
                    CartSync.getInstance().updateServerCartAdd2(customerid, item.getShoppingItem().getId(),item.getCount(), this); //CARTSYNC ADD
                }

            }

            else
                Log.d("Piyush", "Guest user, not calling cart sync add ");
        }*/
    }

    @Override
    public void processFinish(boolean output) {
        Log.d("November","Successfully coming inside response");
    }
}


