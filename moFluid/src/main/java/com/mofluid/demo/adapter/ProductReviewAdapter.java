package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.model.ProductReview;
import com.mofluid.magento2.R;

import java.util.ArrayList;

/**
 * Created by piyush-ios on 19/5/16.
 */
public class ProductReviewAdapter extends BaseAdapter {
    private  LayoutInflater inflater;
    ArrayList<ProductReview> productReviewList;
    Activity activity;
    private TextView txtv_review_row_title;
    private TextView txtv_review_row_description;
    private TextView txtv_read_more_review_description;
    private RatingBar ratingbar_average;
    private TextView txtv_review_row_footer;
    private Typeface open_sans_regular, open_sans_semibold;
    private boolean isExpanded = false;

    public ProductReviewAdapter() {
    }

    public ProductReviewAdapter(ArrayList<ProductReview> productReviewList ,Activity activity) {

        this.productReviewList=productReviewList;
        this.activity=activity;
        inflater=(LayoutInflater)activity.getSystemService(activity.LAYOUT_INFLATER_SERVICE);
        open_sans_semibold = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);
        open_sans_regular = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.OPEN_SANS_REGULAR_FONT_STYLE);
    }

    @Override
    public int getCount() {
        return productReviewList.size();

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView=inflater.inflate(R.layout.row_single_review, null);
        initialise(rootView);
        setFontStyle();
        setDataToViews(position);
        return rootView;
    }

    private void setDataToViews(int position) {

        txtv_review_row_title.setText(productReviewList.get(position).getTitle());
        txtv_review_row_description.setText(productReviewList.get(position).getDetail());
//        if(txtv_review_row_description.getText().toString().length()>=110)
//        {
//            txtv_read_more_review_description.setVisibility(View.VISIBLE);
//            isExpanded=true;
//        }
        String footer_text = "BY : "+productReviewList.get(position).getNickname() +"  |  "+productReviewList.get(position).getCreate_date();
        txtv_review_row_footer.setText(footer_text);
        ratingbar_average.setRating(productReviewList.get(position).getVoteAverage());

    }

    private void initialise(View rootView) {
        txtv_review_row_title = (TextView)rootView.findViewById(R.id.txtv_review_row_title);
        txtv_review_row_description = (TextView)rootView.findViewById(R.id.txtv_review_row_description);
        txtv_read_more_review_description = (TextView)rootView.findViewById(R.id.txtv_read_more_review_description);
        txtv_read_more_review_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExpanded)
                {
                    txtv_read_more_review_description.setText("Read less");
                    txtv_review_row_description.setMaxLines(Integer.MAX_VALUE);
                    txtv_review_row_description.setText("Hello");
                    isExpanded=false;
                }
                else
                {
                    txtv_read_more_review_description.setText("Read more");
                    txtv_review_row_description.setMaxLines(3);
                    isExpanded=false;
                }
            }
        });
        ratingbar_average = (RatingBar)rootView.findViewById(R.id.ratingbar_average);
        txtv_review_row_footer = (TextView)rootView.findViewById(R.id.txtv_review_row_footer);
    }

    private void setFontStyle()
    {
        txtv_review_row_title.setTypeface(open_sans_semibold);
        txtv_review_row_description.setTypeface(open_sans_regular);
        txtv_read_more_review_description.setTypeface(open_sans_regular);
        txtv_review_row_footer.setTypeface(open_sans_semibold);
        changeStarColor(ratingbar_average);

    }

    private void changeStarColor(RatingBar ratingBar)
    {
        Drawable drawable = ratingBar.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#E98604"), PorterDuff.Mode.SRC_ATOP);
    }


}
