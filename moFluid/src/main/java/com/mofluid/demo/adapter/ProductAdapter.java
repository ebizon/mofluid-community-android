package com.mofluid.demo.adapter;


import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.ShoppingItem;

import java.util.List;

import com.mofluid.magento2.R;
import com.mofluid.magento2.service.AppController;


/**
 * Adapter Class to showcase products in RecyclerView
 */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Typeface open_sans_semibold;

    private List<ShoppingItem> productListDetails;
    private ImageLoader imageLoaderVolley;

    private RecyclerViewClickListener mListener;

    public ProductAdapter(List<ShoppingItem> productListDetails, RecyclerViewClickListener onClickListener) {
        this.productListDetails = productListDetails;
        imageLoaderVolley = AppController.getInstance().getImageLoader();
        mListener = onClickListener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View singleItemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_feature_product, parent, false);
        ProductViewHolder myViewHolder = new ProductViewHolder(singleItemLayout);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {

        ShoppingItem item = productListDetails.get(position);

        // TODO: 31/07/18 Extract this functionality in styles.xml
        if(open_sans_semibold==null) open_sans_semibold = Typeface.createFromAsset(holder.containerLinearLayout.getContext().getAssets(), ConstantDataMember.OPEN_SANS_SEMIBOLD_FONT_STYLE);

        holder.titleTextView.setText(item.getName());
        holder.titleTextView.setTypeface(open_sans_semibold);

        holder.pricetextView.setTypeface(open_sans_semibold);

        holder.specialPriceTextView.setTypeface(open_sans_semibold);

        setPrice(holder.pricetextView,holder.specialPriceTextView,item.getPriceStr(),item.getSpecialPriceStr());

        imageLoaderVolley.get(item.getImage(), ImageLoader.getImageListener(holder.itemImageView, R.drawable.default_mofluid, R.drawable.default_mofluid));
    }


	private void setPrice(TextView txtV_regular_price, TextView txtV_final_price, String grouped_price_regular, String grouped_price_final) {
		double final_price=Double.valueOf(grouped_price_final);
		double regular_price=Double.valueOf(grouped_price_regular);

		if(final_price<regular_price && final_price>0) {
			txtV_regular_price.setVisibility(View.VISIBLE);
			txtV_final_price.setText(Utils.appendWithCurrencySymbol(final_price));
			setTextThrougLine(Utils.appendWithCurrencySymbol(regular_price), txtV_regular_price);

		} else {
			txtV_final_price.setText(Utils.appendWithCurrencySymbol(regular_price));
			txtV_regular_price.setVisibility(View.GONE);
		}
	}

	private void setTextThrougLine(String off_money_discounted, TextView txtTitle) {
		SpannableString discounted = new SpannableString(off_money_discounted);
		discounted.setSpan(new StrikethroughSpan(), 0, off_money_discounted.length(), 0);
		txtTitle.setText(discounted);

		String text = "dvs" + " calls";
		SpannableString ss = new SpannableString(text);
		ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), 0);

	}

    @Override
    public int getItemCount() {
        return productListDetails != null ? productListDetails.size() : 0;
    }

    public void setData(List<ShoppingItem> dataSet) {
        if (dataSet != null && dataSet.size() > 0) {
            productListDetails = null;
            productListDetails = dataSet;
            notifyDataSetChanged();
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //Click listener will be attached to containerLinearLayout
        LinearLayout containerLinearLayout;
        TextView titleTextView, pricetextView, specialPriceTextView;
        ImageView itemImageView;

        public ProductViewHolder(View itemView) {
            super(itemView);
            containerLinearLayout = (LinearLayout) itemView.findViewById(R.id.ll_item_container);
            containerLinearLayout.setOnClickListener(this);
            titleTextView = (TextView) itemView.findViewById(R.id.item_text);
            pricetextView = (TextView) itemView.findViewById(R.id.txtPrice);
            specialPriceTextView = (TextView) itemView.findViewById(R.id.txtSpecialPrice);
            itemImageView = (ImageView) itemView.findViewById(R.id.item_image);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(productListDetails.get(getAdapterPosition()));
        }
    }

    //Implement this interface in the parent activity or fragment and pass it into the adapter parameters
    public interface RecyclerViewClickListener {
        void onClick(ShoppingItem item);
    }

}
