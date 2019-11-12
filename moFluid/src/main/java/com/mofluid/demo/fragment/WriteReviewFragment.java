package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.ebizon.fluid.Utils.Config;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.NetworkAPIManager;
import com.ebizon.fluid.model.WebApiManager;
import com.mofluid.magento2.EncodeString;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

/**
 * Created by piyush-ios on 19/5/16.
 */
public class WriteReviewFragment extends BaseFragment {

    private String PRODUCT_ID;
    private String CUSTOMER_ID;
    private String PRODUCT_NAME;
    private View rootView;
    private Context context;
    private String headerText;
    private TextView txtv_write_review_product_name;
    private AlertDialog.Builder builder;
    private TextView txtv_write_review_message;
    private TextView quality_rating;
    private TextView value_rating;
    private TextView price_rating;
    private TextView txtv_write_review_summary;
    private TextView txtv_write_review_description;
    private TextView txtv_write_review_nickname;
    private RatingBar quality_ratingbar;
    private RatingBar value_ratingbar;
    private RatingBar price_ratingbar;
    private Button btn_write_review_submit;
    private EditText edt_write_review_nickname;
    private EditText edt_write_review_description;
    private EditText edt_write_review_summary;
    private Typeface open_sans_semibold;
    private Typeface open_sans_regular;
    private JSONObject jsonOBJ;
    private boolean isReviewSubmit;
    private String finalMessage;
    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    @SuppressLint("ValidFragment")
    public WriteReviewFragment(String PRODUCT_ID, String PRODUCT_NAME, String CUSTOMER_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
        this.CUSTOMER_ID = CUSTOMER_ID;
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    public WriteReviewFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_pdp_write_review, container, false);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            isReviewSubmit = false;
            getviewControlls(rootView);
            setFontStyle();
            context = getActivity();

        }
        return rootView;
    }

    private void getviewControlls(View rootView) {
        //TEXTVIEWS
        txtv_write_review_product_name = (TextView) rootView.findViewById(R.id.txtv_write_review_product_name);
        txtv_write_review_message = (TextView) rootView.findViewById(R.id.txtv_write_review_message);
        quality_rating = (TextView) rootView.findViewById(R.id.quality_rating);
        value_rating = (TextView) rootView.findViewById(R.id.value_rating);
        price_rating = (TextView) rootView.findViewById(R.id.price_rating);
        txtv_write_review_summary = (TextView) rootView.findViewById(R.id.txtv_write_review_summary);
        txtv_write_review_description = (TextView) rootView.findViewById(R.id.txtv_write_review_description);
        txtv_write_review_nickname = (TextView) rootView.findViewById(R.id.txtv_write_review_nickname);

        //RATINGBARS
        quality_ratingbar = (RatingBar) rootView.findViewById(R.id.quality_ratingbar);
        value_ratingbar = (RatingBar) rootView.findViewById(R.id.value_ratingbar);
        price_ratingbar = (RatingBar) rootView.findViewById(R.id.price_ratingbar);

        //BUTTON
        btn_write_review_submit = (Button) rootView.findViewById(R.id.btn_write_review_submit);
        btn_write_review_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateoptions() == true) {
                    hitServicetoWriteReview();

                } else {
                    Activity activity = getActivity();
                    if (isAdded() && activity != null)
                        Toast.makeText(context, context.getResources().getString(R.string.validate_write_review_message), Toast.LENGTH_SHORT).show();
                }


            }
        });

        //EDITTEXT
        edt_write_review_summary = (EditText) rootView.findViewById(R.id.edt_write_review_summary);
        edt_write_review_description = (EditText) rootView.findViewById(R.id.edt_write_review_description);
        edt_write_review_nickname = (EditText) rootView.findViewById(R.id.edt_write_review_nickname);

    }

    private void createalert() {
        builder = new AlertDialog.Builder(context);
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            builder.setTitle(context.getResources().getString(R.string.write_review));
        }
        builder.setMessage(finalMessage);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                dialog.dismiss();
                callPRoductFragment();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                callPRoductFragment();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void callPRoductFragment() {
        Fragment frmnt = new SimpleProductFragment2();
        Bundle bundle = new Bundle();
        bundle.putString(ConstantDataMember.PRO_ID, PRODUCT_ID);
        bundle.putString(ConstantDataMember.PRO_NAME, PRODUCT_NAME);
        frmnt.setArguments(bundle);
        callFragment((BaseFragment) frmnt, frmnt.getClass().getSimpleName());
    }

    private boolean validateoptions() {
        boolean isValid = true;
        if (edt_write_review_summary.getText().length() == 0) {
            isValid = false;
            txtv_write_review_summary.setText(txtv_write_review_summary.getText().toString() + " *");
            txtv_write_review_summary.setTextColor(Color.RED);

        }
        if (edt_write_review_nickname.getText().length() == 0) {
            isValid = false;
            txtv_write_review_nickname.setText(txtv_write_review_nickname.getText().toString() + " *");
            txtv_write_review_nickname.setTextColor(Color.RED);
        }
        if (edt_write_review_description.getText().length() == 0) {
            isValid = false;
            txtv_write_review_description.setText(txtv_write_review_description.getText().toString() + " *");
            txtv_write_review_description.setTextColor(Color.RED);
        }

        return isValid;
    }

    private void setFontStyle() {

        txtv_write_review_product_name.setTypeface(open_sans_semibold);
        txtv_write_review_product_name.setText(PRODUCT_NAME);
        txtv_write_review_message.setTypeface(open_sans_regular);
        quality_rating.setTypeface(open_sans_regular);
        value_rating.setTypeface(open_sans_regular);
        price_rating.setTypeface(open_sans_regular);
        txtv_write_review_summary.setTypeface(open_sans_semibold);
        txtv_write_review_description.setTypeface(open_sans_semibold);
        txtv_write_review_nickname.setTypeface(open_sans_semibold);
        edt_write_review_description.setTypeface(open_sans_regular);
        edt_write_review_nickname.setTypeface(open_sans_regular);
        edt_write_review_summary.setTypeface(open_sans_regular);
        btn_write_review_submit.setTypeface(open_sans_regular);
        changeStarColor(quality_ratingbar);
        changeStarColor(value_ratingbar);
        changeStarColor(price_ratingbar);

    }

    private void changeStarColor(RatingBar ratingBar) {
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#E98604"), PorterDuff.Mode.SRC_ATOP);
    }


    private void hitServicetoWriteReview() {
        String nickname = edt_write_review_nickname.getText().toString();
        String reviewsummary = edt_write_review_summary.getText().toString();
        reviewsummary = EncodeString.encodeStrBase64Bit(reviewsummary);
        String comment = edt_write_review_description.getText().toString();
        comment = EncodeString.encodeStrBase64Bit(comment);
        int price_rating = (int) price_ratingbar.getRating();
        int value_rating = (int) value_ratingbar.getRating();
        int quality_rating = (int) quality_ratingbar.getRating();
        String url = WebApiManager.getInstance().writeProductReviewURL(context);
        String store = Config.getInstance().getStoreValue();
        final String finalURl = String.format(url, store, PRODUCT_ID, CUSTOMER_ID, nickname, price_rating, value_rating, quality_rating, reviewsummary, comment);

        final ProgressDialog pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.setCancelable(false);
        pDialog.show();


        NetworkAPIManager.getInstance(getActivity()).sendGetRequest(finalURl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Log.d("Volley= ", response);
                if (pDialog.isShowing())
                    pDialog.hide();

                if (response != null) {
                    try {
                        jsonOBJ = new JSONObject(response);
                        String result = jsonOBJ.getString("status");
                        if (result.equals("success")) {
                            isReviewSubmit = true;
                            String message = jsonOBJ.getString("review");
                            finalMessage = message;
                        } else {
                            isReviewSubmit = false;
                            Activity activity = getActivity();
                            if (isAdded() && activity != null)
                                finalMessage = context.getResources().getString(R.string.write_review_message_failure);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        pDialog.hide();
                        isReviewSubmit = false;
                        Activity activity = getActivity();
                        if (isAdded() && activity != null)
                            finalMessage = context.getResources().getString(R.string.write_review_message_failure);
                    }

                }
                createalert();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                pDialog.hide();
                isReviewSubmit = false;
                Activity activity = getActivity();
                if (isAdded() && activity != null)
                    Toast.makeText(context, context.getResources().getString(R.string.write_review_message_failure), Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void onResume() {
        Activity activity = getActivity();
        if (isAdded() && activity != null) {
            headerText = getActivity().getResources().getString(R.string.write_review);
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
