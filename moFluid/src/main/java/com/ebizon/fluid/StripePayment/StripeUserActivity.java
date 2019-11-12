package com.ebizon.fluid.StripePayment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class StripeUserActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "StripeUserActivity";
    ;
    StripeSavedCardAdapter adapter;
    String err_message = "";
    private TextView no_saved_cards_stripe;
    private ListView listV_saved_cards_stripe;
    private ArrayList<StripeuserCard> cardList;
    private StripeuserCard cardforPay = null;
    private TextView product_name;
    private ProgressDialog progressDialog;
    private boolean isExistingStripeUser= false;
    private String price="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_user);
        price = getIntent().getStringExtra(StripeConstants.STRIPE_CARD_PRICE);
        getviewControls();
        checkfrosaveduser();
    }

    private void checkfrosaveduser() {
        UserProfileItem currentuser = UserManager.getInstance().getUser();
            String str_id = getIntent().getStringExtra(StripeConstants.USER_STRIPE_ID);
            Log.d(TAG,"Stripe User id is "+str_id);
//        str_id = "cus_9tpXavO3xmMAU2";
            if(currentuser!=null)
                currentuser.setStripeID(str_id);
            if (currentuser!=null && currentuser.getStripeID() != null && !currentuser.getStripeID().equals("0")) {
                // user is old user
                Log.d(TAG, "Existing Stripe user");
                isExistingStripeUser = true;
                hitservicetofetchStripeUserDetails(currentuser.getStripeID());
            } else {
                Log.d(TAG, "New Stripe user, no saved cards");
                isExistingStripeUser = false;
                // user is new stripe user , need to create his account

         }

    }

    private void setAdapterforSavedCards() {
        StripeUser user = UserManager.getInstance().getStripeUserDetails();
        if (user != null) {
            Log.d(TAG, "USer is not null");
            int totalSavedCards = Integer.parseInt(user.getSources_total());
            if (totalSavedCards > 0) {
                listV_saved_cards_stripe.setVisibility(View.VISIBLE);
                no_saved_cards_stripe.setVisibility(View.GONE);
                ArrayList<StripeuserCard> temp = user.getSavedCards();
                cardList.clear();
                cardList.addAll(temp);
                if (cardList.size() >= 5) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, this.getResources().getDimensionPixelOffset(R.dimen.stripe_list_height));
                    listV_saved_cards_stripe.setLayoutParams(params);
                } else {
                    ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    listV_saved_cards_stripe.setLayoutParams(params);
                }
                adapter.notifyDataSetChanged();
            } else {
                listV_saved_cards_stripe.setVisibility(View.GONE);
                no_saved_cards_stripe.setVisibility(View.VISIBLE);
                // keep view as it is and go for add card
            }
        } else
            Log.d(TAG, "USer is NULL NULL null");
        ;
    }


    private void getviewControls() {
        LinearLayout add_new_card = (LinearLayout) findViewById(R.id.ll_add_card);
        add_new_card.setOnClickListener(this);
        LinearLayout ll_back_menu = (LinearLayout) findViewById(R.id.ll_back_menu);
        ll_back_menu.setOnClickListener(this);
        product_name = (TextView)findViewById(R.id.product_name);
        Button btn_pay_now_stripe = (Button) findViewById(R.id.btn_pay_now_stripe);
        btn_pay_now_stripe.setOnClickListener(this);
        progressDialog = new ProgressDialog(StripeUserActivity.this);
        no_saved_cards_stripe = (TextView) findViewById(R.id.no_saved_cards_stripe);
        listV_saved_cards_stripe = (ListView) findViewById(R.id.listV_saved_cards_stripe);
        cardList = new ArrayList<>();
        adapter = new StripeSavedCardAdapter(cardList, this);
        listV_saved_cards_stripe.setAdapter(adapter);
        listV_saved_cards_stripe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StripeuserCard selectedCard = cardList.get(position);
                StripeUser user = UserManager.getInstance().getStripeUserDetails();
                user.setDefault_source(selectedCard.getCard_id());
                cardforPay = selectedCard;
                adapter.notifyDataSetChanged();

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_add_card:
                Intent intent = new Intent(this, StripePaymentActivity.class);
                intent.putExtra(StripeConstants.STRIPE_CARD_PRICE,price);
                intent.putExtra(StripeConstants.STRIPE_CARD_SELECTED_ID,"");
                intent.putExtra(StripeConstants.ALREADY_ADDED_CARD,false);
                intent.putExtra(StripeConstants.ALREADY_EXISTING_USER,isExistingStripeUser);
                startActivityForResult(intent, StripeConstants.STRIPE_REQUEST_CODE);
                break;
            case R.id.ll_back_menu:
                StripeUserActivity.this.onBackPressed();
                break;
            case R.id.btn_pay_now_stripe:
                performPayNowAction();
                break;


        }
    }

    private void performPayNowAction() {
        if (cardforPay == null) {
            findDefaultCardUser();
        }
        Log.d(TAG, "Card selected for paying is " + cardforPay.getCard_id());
        String exp_month = cardforPay.getExp_month();
        String exp_year = cardforPay.getExp_year();
        String cvc_check = cardforPay.getCvc_check();
        boolean isCardValid = checkCardDetailsForvalid(exp_month, exp_year, cvc_check);
        if (!isCardValid) {
            showStripeDialogBoxWithoutTitle(err_message, false);
        } else {
            showStripeDialogBoxWithoutTitle("Confirm Payment ? ", true);
        }


    }

    private boolean checkCardDetailsForvalid(String exp_month, String exp_year, String cvc_check) {
        boolean isCardValid = true;
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        if (Integer.parseInt(exp_year) < currentYear) {
            isCardValid = false;
            err_message = "Card year has expired";
        } else if (Integer.parseInt(exp_year) == currentYear) {
            if (Integer.parseInt(exp_month) < currentMonth) {
                isCardValid = false;
                err_message = "Card date has expired";
            }
        } else isCardValid = true;

        if (!cvc_check.equals("pass")) {
            isCardValid = false;
            err_message = "CVC Check failed/invalid";
        }
        return isCardValid;
    }

    private void findDefaultCardUser() {
        String def_card = UserManager.getInstance().getStripeUserDetails().getDefault_source();
        Iterator<StripeuserCard> it = cardList.iterator();
        while (it.hasNext()) {
            StripeuserCard card = it.next();
            if (card.getCard_id().equals(def_card))
                cardforPay = card;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == StripeConstants.STRIPE_REQUEST_CODE) {
            // returned from payment and add card page
            String message = "";
            String payment_result;
            String transaction_id;
            if(data!=null) {
                payment_result = data.getStringExtra(StripeConstants.STRIPE_PAYMENT_RESULT);
                transaction_id = data.getStringExtra(StripeConstants.STRIPE_TRANS_ID);


                if (resultCode == RESULT_OK) {
                    // success payment
                    message = "Payment Successful";
                    StripeuserCard selected_card = (StripeuserCard) data.getSerializableExtra(StripeConstants.STRIPE_CARD_SELECTED_CARD);
                    Intent intent = new Intent();
                    intent.putExtra(StripeConstants.STRIPE_MESSAGE, message);
                    intent.putExtra(StripeConstants.STRIPE_PAYMENT_RESULT, payment_result);
                    intent.putExtra(StripeConstants.STRIPE_TRANS_ID, transaction_id);
                    intent.putExtra(StripeConstants.STRIPE_CARD_SELECTED_CARD,selected_card);
                    setResult(RESULT_OK, intent);
                    finish();

                } else {
                    //failure payment
                    message = data.getStringExtra(StripeConstants.STRIPE_MESSAGE);
                    if(payment_result.equals("back")) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent();
                        intent.putExtra(StripeConstants.STRIPE_MESSAGE, message);
                        intent.putExtra(StripeConstants.STRIPE_PAYMENT_RESULT, payment_result);
                        intent.putExtra(StripeConstants.STRIPE_TRANS_ID, transaction_id);
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }
                }


            }


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        intent.putExtra(StripeConstants.STRIPE_MESSAGE, "");
        intent.putExtra(StripeConstants.STRIPE_PAYMENT_RESULT, "back");
        intent.putExtra(StripeConstants.STRIPE_TRANS_ID, "0");
        setResult(RESULT_CANCELED, intent);
        finish();

    }

    private void hitservicetofetchStripeUserDetails(String customer_id) {

        progressDialog.setMessage("Fetching saved cards !");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        String url = WebApiManager.getInstance().retrieveCustomerStripe(StripeUserActivity.this);
        url = String.format(url, customer_id);
        Log.d(TAG, "Service to retrieve data for stripe user called with " + url);
        NetworkAPIManager.getInstance(this).sendGetRequest(url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                try {
                    JSONObject output = new JSONObject(response);
                    StripeUser stripeuserdata = StripeUser.parseUserResponseStripe(output);
                    UserManager.getInstance().setStripeUserDetails(stripeuserdata);
                    Log.d(TAG, "User details fetched success ");
                    setAdapterforSavedCards();

                } catch (JSONException e) {
                    Log.d(TAG, "User details fetched failure ");
                    Toast.makeText(StripeUserActivity.this,"Some error Occured ! Please try later", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                progressDialog.dismiss();

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d(TAG, "User details fetched failure ");
                VolleyLog.d(TAG, "Error: Feature Pro " + error.getMessage());
                Toast.makeText(StripeUserActivity.this,"Some error Occured ! Please try later", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showStripeDialogBoxWithoutTitle(String message, final boolean cancel) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.CENTER;
       /* wmlp.x = 50;   //x position
        wmlp.y = 50;   //y position*/


        dialog.setContentView(R.layout.stripedialogbox);

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        Button btnBialog = (Button) dialog.findViewById(R.id.btnBialog);
        Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
        if (cancel)
            btnCancel.setVisibility(View.VISIBLE);
        else
            btnCancel.setVisibility(View.GONE);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);
        tvMessage.setText(message);
        dialog.setCancelable(false);

        btnBialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cancel)
                    dialog.cancel();
                else {
                    // Open to Stripe Payment and do payment now.
                    Intent intent = new Intent(StripeUserActivity.this,StripePaymentActivity.class);
                    intent.putExtra(StripeConstants.STRIPE_CARD_SELECTED_ID,cardforPay.getCard_id());
                    intent.putExtra(StripeConstants.STRIPE_CARD_SELECTED_CARD, cardforPay);
                    intent.putExtra(StripeConstants.ALREADY_ADDED_CARD,true);
                    intent.putExtra(StripeConstants.ALREADY_EXISTING_USER,isExistingStripeUser);
                    intent.putExtra(StripeConstants.STRIPE_CARD_PRICE,price);
                    dialog.dismiss();
                    startActivityForResult(intent,StripeConstants.STRIPE_REQUEST_CODE);
                }

            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(product_name!=null)
            product_name.setText("Stripe Payment Method");
        StripeUser user = UserManager.getInstance().getStripeUserDetails();
        if(user!=null)
        {
            if(cardList!=null && adapter!=null) {
                cardList.clear();
                cardList.addAll(user.getSavedCards());
                adapter.notifyDataSetChanged();
            }
        }

    }
}

