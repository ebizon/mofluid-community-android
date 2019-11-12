package com.mofluid.magento2.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.OnFragmentInteractionListener;
import com.ebizon.fluid.model.ProductReview;
import com.ebizon.fluid.model.UserManager;
import com.ebizon.fluid.model.UserProfileItem;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;
import com.mofluid.magento2.MainActivity;
import com.mofluid.magento2.R;
import com.mofluid.magento2.adapter.ProductReviewAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by piyush-ios on 19/5/16.
 */
public class AllReviewFragment extends BaseFragment {

    private Typeface open_sans_semibold;
    private Typeface open_sans_regular;
    private int totalReviews;
    ArrayList<ProductReview> allReviewList;
    private View rootView;
    Context activity;
    private Activity context;
    private String PRODUCT_ID, PRODUCT_NAME;
    private String headerText;
    private ExpandableHeightListView all_review_listv;
    private TextView txtv_all_review_total;
    private Button btn_write_review_all;
    private TextView txt_not_login_review;

    //Listener to talk back to calling activity
    private OnFragmentInteractionListener mListener;


    public AllReviewFragment() {
    }
    @SuppressLint("ValidFragment")
    public AllReviewFragment(ArrayList<ProductReview> allReviewList,int totalReviews, Context activity, String PRODUCT_ID, String PRODUCT_NAME) {
        this.allReviewList = allReviewList;
        this.totalReviews = totalReviews;
        this.activity=activity;
        open_sans_semibold = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        open_sans_regular = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
        this.PRODUCT_ID = PRODUCT_ID;
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(rootView==null) {
            rootView = inflater.inflate(R.layout.fragment_pdp_all_reviews, container, false);
            MainActivity.ivBackMenu.setVisibility(View.VISIBLE);
            initialize(rootView);
            context= getActivity();
            setdatatoadapter();

        }
        return rootView;
    }

    private void setdatatoadapter() {

        ProductReviewAdapter allproductAdapter = new ProductReviewAdapter(allReviewList, getActivity());
        all_review_listv.setExpanded(true);
        all_review_listv.setAdapter(allproductAdapter);

    }

    private void initialize(View rootView)
    {
        if(isAdded()&&activity!=null)
        headerText= getActivity().getResources().getString(R.string.customer_reviews);
        all_review_listv= (ExpandableHeightListView) rootView.findViewById(R.id.listv_pdp_all_review_list);
        txtv_all_review_total = (TextView)rootView.findViewById(R.id.txtv_all_review_total);
        txt_not_login_review = (TextView)rootView.findViewById(R.id.txt_not_login_review);
        btn_write_review_all = (Button)rootView.findViewById(R.id.btn_write_review_all);
        UserProfileItem activeUser = UserManager.getInstance().getUser();
        if(activeUser==null){
            btn_write_review_all.setVisibility(View.INVISIBLE);
            txt_not_login_review.setVisibility(View.VISIBLE);
        }
        else {
            btn_write_review_all.setVisibility(View.VISIBLE);
            txt_not_login_review.setVisibility(View.INVISIBLE);
        }
        btn_write_review_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserProfileItem activeUser = UserManager.getInstance().getUser();
                WriteReviewFragment writeReview = new WriteReviewFragment(PRODUCT_ID,PRODUCT_NAME,activeUser.getId());
                callFragment(writeReview, "WriteReviewFragment");
            }
        });

        txt_not_login_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragment signin = new SignInSignUpFragment();
                callFragment(signin, "SignInSignUpFragment");
            }
        });
        if(isAdded()&&activity!=null) {
            String str = String.format(activity.getResources().getString(R.string.total_reviews), totalReviews);
            txtv_all_review_total.setText(str);
            txtv_all_review_total.setTypeface(open_sans_semibold);
        }
    }

    public void onResume() {
        //MainActivity.setHeaderText(headerText);
        super.onResume();
        mListener.onFragmentMessage(ConstantDataMember.SET_TITLE_TAG,headerText);
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
