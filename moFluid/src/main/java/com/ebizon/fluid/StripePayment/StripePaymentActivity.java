package com.ebizon.fluid.StripePayment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.fragment.HomeFragment;
import com.stripe.android.*;

import com.mofluid.magento2.R;
import com.stripe.android.exception.AuthenticationException;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by piyush-ios on 20/12/16.
 */
public class StripePaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "StripePaymentActivity";
    private EditText edt_expiryDate;
    private LinearLayout layout_add_card;
    private ProgressDialog progressDialog;
    private TextView product_name;
    private boolean old_customer=false;
    private TextView error_stripe_invalid;
    private StripeuserCard selected_card;
    private String price;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);
        getviewControls();
        getDatafromIntent();


    }

    private void getDatafromIntent() {
        if (getIntent().getBooleanExtra(StripeConstants.ALREADY_ADDED_CARD, false) == true) {
            //perform action without adding card
            String selected_card_id = getIntent().getStringExtra(StripeConstants.STRIPE_CARD_SELECTED_ID);
             selected_card = (StripeuserCard) getIntent().getSerializableExtra(StripeConstants.STRIPE_CARD_SELECTED_CARD);
            price = getIntent().getStringExtra(StripeConstants.STRIPE_CARD_PRICE);
            TextView totalAmountStripe = (TextView) findViewById(R.id.totalAmountStripe);
            totalAmountStripe.setText(price);
            hitservicetoChargeUser(selected_card_id, price);
            if(product_name!=null)
                product_name.setText("Stripe Payment Processing");
        } else {
            //new card add normal functionality
            layout_add_card.setVisibility(View.VISIBLE);
            if(product_name!=null)
                product_name.setText("Add Card");
            old_customer = getIntent().getBooleanExtra(StripeConstants.ALREADY_EXISTING_USER,false);
            Log.d(TAG,"Create a new customer is :"+ old_customer);

        }
    }

    private void getviewControls() {
        edt_expiryDate = (EditText) findViewById(R.id.expiryDateStripe);
        edt_expiryDate.addTextChangedListener(mDateEntryWatcher);
        layout_add_card = (LinearLayout) findViewById(R.id.layout_add_card);
        product_name = (TextView)findViewById(R.id.product_name);
        error_stripe_invalid = (TextView)findViewById(R.id.error_stripe_invalid);
        TextView totalAmountStripe = (TextView) findViewById(R.id.totalAmountStripe);
        totalAmountStripe.setText(price);
        LinearLayout ll_back_menu = (LinearLayout) findViewById(R.id.ll_back_menu);
        ll_back_menu.setOnClickListener(this);

        Button btnSubmitStripe = (Button) findViewById(R.id.btnSubmitStripe);
        btnSubmitStripe.setOnClickListener(this);

        progressDialog = new ProgressDialog(StripePaymentActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Processing Payment !");
        selected_card=null;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnSubmitStripe:
                doSubmitButtonAction();
                break;
            case R.id.ll_back_menu:
                StripePaymentActivity.this.onBackPressed();
            default:
                //
        }

    }

    private void doSubmitButtonAction() {
        progressDialog.show();
        boolean res;
        EditText cardNumberStripeEditText = (EditText) findViewById(R.id.cardNumberStripe);
        EditText cvvNumberStripe = (EditText) findViewById(R.id.cvvNumberStripe);
        TextView totalAmountStripe = (TextView) findViewById(R.id.totalAmountStripe);
        String expDate = edt_expiryDate.getText().toString();
        int cardExpMonth = Integer.parseInt(expDate.substring(0, 2));
        int cardExpYear = Integer.parseInt(expDate.substring(3));

        Card card = new Card(
                cardNumberStripeEditText.getText().toString(),
                cardExpMonth,
                cardExpYear,
                cvvNumberStripe.getText().toString()
        );

        card.validateNumber();
        card.validateCVC();
        card.validateExpiryDate();

        if (!card.validateCard()) {
            // show error
            Log.d("STRIPE", "Error in validating card ");
            String error_msg = "<ul>";
            progressDialog.dismiss();
            if(!card.validateNumber())
                error_msg=error_msg+"<li>Invalid Card Number"+"</li><br/>";
            if(!card.validateExpiryDate())
                error_msg=error_msg+"<li>Invalid Expiry Date"+"</li><br/>";
            if(!card.validateCVC())
                error_msg=error_msg+"<li>Invalid CVC number"+"</li><br/>";

            error_msg+="</ul>";

            error_stripe_invalid.setVisibility(View.VISIBLE);
            error_stripe_invalid.setText(Html.fromHtml(error_msg));

        } else {
            error_stripe_invalid.setVisibility(View.GONE);
            Log.d("STRIPE", "Validating card successful ");
            try {
                createTokenFromCard(card);
                res = true;
            } catch (AuthenticationException e) {
                e.printStackTrace();
                progressDialog.dismiss();
                res = false;
            }
        }

    }

    private void createTokenFromCard(Card card) throws AuthenticationException {

        Stripe stripe = new Stripe("StripePaymentActivity.this");//plz checnge here
        stripe.setDefaultPublishableKey("pk_test_MQ5ZrAUo5aWUqMEymGtLttPg");
        stripe.createToken(
                card,
                new TokenCallback() {
                    public void onSuccess(Token token) {
                        // Send token to your server
                        Log.d("STRIPE", "Token is " + token.toString());
                        if(!old_customer)
                        hitservicetosendTokentoServer(token);
                        else
                        hitServicetoCreateCardforOldCustomer(token);
                    }

                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(StripePaymentActivity.this,
                                error.getMessage(),
                                Toast.LENGTH_LONG
                        ).show();
                        Log.d("STRIPE", "Error in creating token is " + error.toString());
                        progressDialog.dismiss();
                    }
                }
        );
    }

    private void hitServicetoCreateCardforOldCustomer(Token token) {

        progressDialog.show();

        final UserProfileItem currentuser = UserManager.getInstance().getUser();
        if (currentuser != null && currentuser.getLogin_status() != "0") {
            String url = WebApiManager.getInstance().createCardStripe(this);
            url = String.format(url, currentuser.getStripeID(), token.getId());
            Log.d(TAG, "Service to createnewcard called with " + url);
            NetworkAPIManager.getInstance(this).sendGetRequest(url, new Response.Listener<String>() {
                String result = "";

                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response);
                    Log.d("Volley= ", response);
                    try {
                        JSONObject output = new JSONObject(response);
                        if (output.has("httpStatus")) {
                            // error has occured
                            JSONObject obj = output.getJSONObject("jsonBody");
                            JSONObject err = obj.getJSONObject("error");
                            String type = err.getString("type");
                            String message = err.getString("message");
                            publishSuccessBack("failure", "0", message);
                        } else {
                            //card created successfully.
                            StripeuserCard card = StripeUser.parseSingleSource(output);
                            UserManager.getInstance().getStripeUserDetails().addNewStripeCard(card);
                            String card_id_to_pay = card.getCard_id();
                            selected_card = card;
                            //Do Payment hit service
                            String price = getIntent().getStringExtra(StripeConstants.STRIPE_CARD_PRICE);
                            hitservicetoChargeUser(card_id_to_pay, price);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        publishSuccessBack("failure", "0", "Some error occured.");
                    }


                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                    publishSuccessBack("failure", "0", "Some error occured.");
                }
            });
        }

    }

    private void hitservicetosendTokentoServer(Token token) {
        progressDialog.show();
        UserProfileItem currentuser = UserManager.getInstance().getUser();
        String mofluidid,firstname="",lastname="";
        if (currentuser != null && currentuser.getLogin_status() != "0") {
            mofluidid = currentuser.getId();
            firstname = currentuser.getFirstname();
            lastname = currentuser.getFirstname();
        }
        else {
            mofluidid = "0";
            firstname = "Guest";
            lastname = "User";

        }
            String url = WebApiManager.getInstance().stripecustomercreate(this);
            String fullname = firstname + " " + lastname;
            fullname = EncodeString.encodeStrBase64Bit(fullname);
            url = String.format(url, mofluidid, token.getId(), HomeFragment.userEmail, fullname);
            Log.d(TAG, "Service to stripecreateuser called with " + url);
            NetworkAPIManager.getInstance(this).sendGetRequest(url, new Response.Listener<String>() {
                String result = "";

                @Override
                public void onResponse(String response) {
                    Log.d("Piyush", response);
                    Log.d("Volley= ", response);
                    try {
                        JSONObject output = new JSONObject(response);
                        if (output.has("httpStatus")) {
                            // error has occured
                            JSONObject obj = output.getJSONObject("jsonBody");
                            JSONObject err = obj.getJSONObject("error");
                            String type = err.getString("type");
                            String message = err.getString("message");
                            publishSuccessBack("failure", "0", message);
                        } else {
                            //User created successfully.
                            StripeUser stripeuserdata = StripeUser.parseUserResponseStripe(output);
                            UserManager.getInstance().setStripeUserDetails(stripeuserdata);
                            SharedPreferences mySharedPreference=StripePaymentActivity.this.getSharedPreferences(ConstantDataMember.USER_INFO_SESSION, Context.MODE_PRIVATE);
                            SharedPreferences.Editor preference_editor_session = mySharedPreference.edit();
                            preference_editor_session.putString(ConstantDataMember.USER_INFO_STRIPE_ID,stripeuserdata.getStripeCustomerID());
                            preference_editor_session.commit();
                            //Do Payment hit service
                            String price = getIntent().getStringExtra(StripeConstants.STRIPE_CARD_PRICE);
                            hitservicetoChargeUser(null, price);
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        if(progressDialog.isShowing())
                            progressDialog.dismiss();
                        publishSuccessBack("failure", "0", "Some error occured.");
                    }


                }

            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                    publishSuccessBack("failure", "0", "Some error occured.");
                }
            });


    }

    public void hitservicetoChargeUser(String selected_card_id, String price) {

        if(!progressDialog.isShowing())
            progressDialog.show();
        UserProfileItem currentuser = UserManager.getInstance().getUser();
        if (currentuser != null && currentuser.getLogin_status() != "0") {
            StripeUser stripeuser = UserManager.getInstance().getStripeUserDetails();
            if (stripeuser != null) {
                String url = WebApiManager.getInstance().chargeStripe(StripePaymentActivity.this);
                String customer_id = stripeuser.getStripeCustomerID();
                String card_id = selected_card_id;
                url = String.format(url, customer_id, price, card_id);
                Log.d(TAG, "Service to charge user with stripe called with " + url);
                NetworkAPIManager.getInstance(this).sendGetRequest(url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        Log.d(TAG, response);
                        try {
                            JSONObject output = new JSONObject(response);
                            String status = output.getString("status");
                            String failure_message = null;
                            if (status.equals("succeeded")) {
                                //success payment
                                String transID = output.getString("id");
                                int amount = output.getInt("amount"); // in cents
                                String currency = output.getString("currency");
                                String customer_id = output.getString("customer");
                                String failure_code = output.getString("failure_code");
                                failure_message = output.getString("failure_message");
                                boolean paid = output.getBoolean("paid");
                                JSONObject outcome = output.getJSONObject("outcome");
                                JSONObject source = output.getJSONObject("source");
                                StripeuserCard cardused = StripeUser.parseSingleSource(source);

                                publishSuccessBack(status, transID, failure_message);
                            } else {
                                publishSuccessBack(status, "0", failure_message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            publishSuccessBack("failure", "0", "Some error occured.");
                        }


                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                        publishSuccessBack("failure", "0", "Some error occured.");
                    }
                });
            }
        }
    }

    private void publishSuccessBack(String status, String transID, String failure_message) {
        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra(StripeConstants.STRIPE_PAYMENT_RESULT, status);
        intent.putExtra(StripeConstants.STRIPE_TRANS_ID, transID);
        intent.putExtra(StripeConstants.STRIPE_CARD_SELECTED_CARD,selected_card);
        if (status.equals("succeeded")) {
            setResult(RESULT_OK, intent);
            finish();
        } else {
            intent.putExtra(StripeConstants.STRIPE_MESSAGE, failure_message);
            setResult(RESULT_CANCELED, intent);
            finish();
        }

    }

    private TextWatcher mDateEntryWatcher = new TextWatcher() {

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            Boolean isValid = true;
            if (working.length() == 2 && before == 0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                    isValid = false;
                    working += "/";
                    edt_expiryDate.setText(working);
                    edt_expiryDate.setSelection(working.length());
                } else {
                    working += "/";
                    edt_expiryDate.setText(working);
                    edt_expiryDate.setSelection(working.length());
                }
            } else if (working.length() == 5 && before == 0) {
                String enteredYear = working.substring(3);
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                String currentyearStr = String.valueOf(currentYear);
                currentyearStr = currentyearStr.substring(2);
                if (Integer.parseInt(enteredYear) < Integer.parseInt(currentyearStr)) {
                    isValid = false;
                }
                String enteredMonth = working.substring(0, 2);
                if ((Integer.parseInt(enteredMonth) > 12 || Integer.parseInt(enteredMonth) < 0))
                    isValid = false;
            } else if (working.length() != 5) {
                isValid = false;
            }

            if (!isValid) {
                edt_expiryDate.setError("Enter a valid date: MM/YY");
            } else {
                edt_expiryDate.setError(null);
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }


    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(progressDialog!=null&&progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(progressDialog!=null)
        progressDialog.dismiss();

        Intent intent = new Intent();
        intent.putExtra(StripeConstants.STRIPE_PAYMENT_RESULT, "back");
        intent.putExtra(StripeConstants.STRIPE_TRANS_ID, "0");
        intent.putExtra(StripeConstants.STRIPE_MESSAGE, "");
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}