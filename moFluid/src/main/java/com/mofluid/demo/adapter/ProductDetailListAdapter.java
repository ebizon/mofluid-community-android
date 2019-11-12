package com.mofluid.magento2.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.ebizon.fluid.Utils.ConstantDataMember;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.productDetailListItem;
import com.mofluid.magento2.R;
import com.mofluid.magento2.service.AppController;

import java.util.ArrayList;

/**
 * 
 * @author manish.s
 *
 */
public class ProductDetailListAdapter extends BaseAdapter {

	private  Typeface lat0_Font_ttf;
	private  String TAG = "";
	private final Activity activity;
	private final ArrayList<productDetailListItem> productListDetails;
	private final ImageLoader imageLoaderVolley = AppController.getInstance().getImageLoader();

	public ProductDetailListAdapter(Activity activity, ArrayList<productDetailListItem> productListDetails) {
		this.activity=activity;
		this.productListDetails=productListDetails;
		TAG=getClass().getSimpleName();
		if(activity!=null)
		lat0_Font_ttf = Typeface.createFromAsset(activity.getAssets(), ConstantDataMember.REGULAR_FONT_STYLE);
	}

	@Override
	public int getCount() {

		return productListDetails.size();
	}

	@Override
	public Object getItem(int arg0) {

		return productListDetails.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		 RecordHolder holder;

//		if (row == null) {
			LayoutInflater inflater = activity.getLayoutInflater();
			row = inflater.inflate(R.layout.row_product_detail_list_adapter, parent, false);

			holder = new RecordHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.item_text);
			holder.imgV_id = (ImageView) row.findViewById(R.id.imgV_id);
			holder.txtV_price = (TextView) row.findViewById(R.id.txtV_price);
			holder.txtV_special_price = (TextView) row.findViewById(R.id.txtV_special_price);
			row.setTag(holder);
//		} else {
//			holder = (RecordHolder) row.getTag();
//		}

		productDetailListItem item = productListDetails.get(position);
		holder.txtTitle.setText(item.getName());
		holder.txtTitle.setTypeface(lat0_Font_ttf);
		holder.txtV_price.setTypeface(lat0_Font_ttf);
		holder.txtV_special_price.setTypeface(lat0_Font_ttf);
		setPrice(holder.txtV_price,holder.txtV_special_price,item.getPrice(),item.getSpclprice());
        if(item.getImageurl()!=null)
		imageLoaderVolley.get(item.getImageurl(), ImageLoader.getImageListener(holder.imgV_id ,
				R.drawable.default_mofluid, R.drawable.default_mofluid));
		return row;
	}

	static class RecordHolder {
		TextView txtTitle;
		TextView txtV_price;
		TextView txtV_special_price;
		ImageView imgV_id;

	}

	private void setPrice(TextView txtV_regular_price, TextView txtV_final_price, String grouped_price_regular, String grouped_price_final) {
		Log.d(TAG, grouped_price_regular + grouped_price_final);
		if(grouped_price_final==null)
			grouped_price_final="0";
		if(grouped_price_regular==null)
			grouped_price_regular="0";
		double final_price=Double.valueOf(grouped_price_final);
		double regular_price=Double.valueOf(grouped_price_regular);

		if(final_price<regular_price && final_price>0) {
			txtV_final_price.setText(Utils.appendWithCurrencySymbol(final_price));
			setTextThrougLine(Utils.appendWithCurrencySymbol(regular_price), txtV_regular_price);
			txtV_regular_price.setVisibility(View.VISIBLE);
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
}