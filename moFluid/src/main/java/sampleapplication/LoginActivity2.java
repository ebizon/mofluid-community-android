package sampleapplication;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ebizon.fluid.model.UserProfileItem;
import com.mofluid.magento2.R;
import com.mofluid.magento2.fragment.PaymentTypeFragment;

import net.authorize.mobile.Result;
import sampleapplication.fragments.LoginActivityRetainedFragment;
import sampleapplication.services.AnetIntentService;

import static com.mofluid.magento2.fragment.PaymentTypeFragment.*;

/**
 * Allows the user to login into Authorize.Net sandbox account using valid
 * login and password credentials
 */
public class LoginActivity2 extends AnetBaseActivity implements
        LoginActivityRetainedFragment.OnFragmentInteractionListener{

    public static final String LOGIN_ID_TAG = "LOGIN_ID";
    public static final String PASSWORD_TAG = "PASSWORD";
    private static final String AUTHENTICATE_USER_FRAGMENT = "authenticate_user_fragment";
    private EditText loginIdEditText;
    private EditText passwordEditText;
    private TextView loginIdErrorMessageTextView;
    private TextView passwordErrorMessageTextView;
    private Drawable loginIdIcon;
    private Drawable passwordIcon;
    public static ProgressDialog loginProgressBar;
    private Button loginButton;
    private CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        loginProgressBar=new ProgressDialog(this);
        loginProgressBar.show();
        attemptLogin();
       /* setupClickableUI(findViewById(R.id.login_page));
        setupViews();*/
       /* setupErrorMessages();*/
    }

    /**
     * Sets variables with their views in the layout XML
     */
    private void setupViews() {
        loginIdEditText = (EditText) findViewById(R.id.loginId);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginIdErrorMessageTextView = (TextView) findViewById(R.id.loginId_error_message);
        passwordErrorMessageTextView = (TextView) findViewById(R.id.password_error_message);
        loginIdIcon = ((ImageView) findViewById(R.id.loginId_icon)).getDrawable();
        passwordIcon = ((ImageView) findViewById(R.id.password_icon)).getDrawable();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.snackbarPosition);
        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return attemptLoginOnEnter(keyCode);
            }
        });
        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }


    /**
     * Allows the user to login when an enter is clicked on the keyboard
     * @param keyCode the code for the physical key that was pressed
     * @return
     */
    public boolean attemptLoginOnEnter(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            hideSoftKeyboard(this);
            attemptLogin();
            return true;
        } else {
            return false;
        }
    }


    /**
     * Sends the credentials (username and password) from the edit texts
     * to a retained fragment for backend authentication.
     */
    public void attemptLogin() {
        String loginId = "prashantchauhan12345";
        String password = "Prashant1234";
        boolean credentialsVerified = verifyCredentials(loginId, password);
        if (!isNetworkAvailable()) {
            displaySnackbar(coordinatorLayout, "", R.string.snackbar_text_no_network_connection, R.string.snackbar_action_retry);
        } else if (!credentialsVerified) {
            displayOkDialog(getResources().getString(R.string.dialog_title_login_error), getResources().getString(R.string.dialog_message_login_error));
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            LoginActivityRetainedFragment retainedFragment = (LoginActivityRetainedFragment) fragmentManager.
                    findFragmentByTag(AUTHENTICATE_USER_FRAGMENT);
            if (retainedFragment == null) {
                retainedFragment = new LoginActivityRetainedFragment();
                Bundle credentials = new Bundle();
                credentials.putString(LOGIN_ID_TAG, loginId);
                credentials.putString(PASSWORD_TAG, password);
                retainedFragment.setArguments(credentials);
                fragmentManager.beginTransaction().add(retainedFragment,
                        AUTHENTICATE_USER_FRAGMENT).commit();
            } else {
                retainedFragment.startServiceAuthenticateUser(loginId, password);
            }

        }
    }


    /**
     * Checks if either login field is empty
     * @param loginId text entered in the login edit text
     * @param password text entered in the password edit text
     * @return
     */
    private boolean verifyCredentials(String loginId, String password) {
        if (loginId == null || loginId.trim().length() == 0)
            return false;
        if (password == null || password.trim().length() == 0)
            return false;
        return true;
    }


    /**
     * Sets up custom error messages for the login fields.
     */
    private void setupErrorMessages() {
        loginIdEditText.addTextChangedListener(new LoginTextWatcher
                (loginIdErrorMessageTextView, loginIdIcon, loginButton));
        passwordEditText.addTextChangedListener(new LoginTextWatcher
                (passwordErrorMessageTextView, passwordIcon, loginButton));
    }


    /**
     * Callback method that receives the results of the user authentication
     * from the intent service
     * @param resultData the result of the authentication
     */
    public void onReceiveAuthenticateUserResult(Bundle resultData) {
        Result loginResult = (Result) resultData.getSerializable
                (AnetIntentService.AUTHENTICATE_USER_STATUS);
        loginProgressBar.dismiss();
        if (loginResult.isResponseOk()) {
            String zipcode="";
            zipcode= getIntent().getStringExtra("ZIPCODE");
            Intent intent = new Intent(this, NavigationActivity.class);
            intent.putExtra("ZIPCODE", zipcode);
            startActivityForResult(intent,2);
        } else if (loginResult.getMessages().size() != 0){
            displayOkDialog(getResources().getString(R.string.dialog_title_login_error),
                    getResources().getString(R.string.dialog_message_login_error));
        } else {
            displayOkDialog(getResources().getString(R.string.dialog_title_login_error),
                    getResources().getString(R.string.dialog_message_unknown_error));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        Log.d("geting result from navigation activity", String.valueOf(requestCode)+data);
        if (resultCode==2)
            setResult(2,data);
        finish();
    }

    /**
     * Class used to display custom error messages when the
     * login fields are empty
     */
    private class LoginTextWatcher implements TextWatcher {
        private TextView textView;
        private Drawable icon;
        private Button loginButton;

        public LoginTextWatcher(TextView textView, Drawable icon, Button loginButton) {
            this.textView = textView;
            this.icon = icon;
            this.loginButton = loginButton;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}
        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                textView.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    icon.setTint(getResources().getColor(R.color.ErrorMessageColor));
                loginButton.setEnabled(false);
            } else {  // restores layout
                textView.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    icon.setTint(getResources().getColor(R.color.ThemeColor));
                loginButton.setEnabled(true);
            }
        }
    }
}
