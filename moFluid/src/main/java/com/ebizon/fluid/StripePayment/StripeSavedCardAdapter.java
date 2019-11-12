package com.ebizon.fluid.StripePayment;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebizon.fluid.model.UserManager;
import com.mofluid.magento2.R;

import java.util.ArrayList;

/**
 * Created by piyush-ios on 29/12/16.
 */
public class StripeSavedCardAdapter extends BaseAdapter {

    Activity context;
    ArrayList<StripeuserCard> cardList;
    LayoutInflater inflater;

    public StripeSavedCardAdapter(ArrayList<StripeuserCard> cardList, Activity context) {
        this.cardList = cardList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int position) {
        return cardList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.row_stripe_card_save, null);
        TextView txtv_card_details_stripe = (TextView) view.findViewById(R.id.txtv_card_details_stripe);
        ImageView imgv_card_type_stripe = (ImageView) view.findViewById(R.id.imgv_card_type_stripe);
        ImageView imgv_checked_stripe = (ImageView) view.findViewById(R.id.imgv_checked_stripe);
        StripeuserCard currentcard = cardList.get(position);
        String type = currentcard.getBrand();
        String last4 = currentcard.getLast4();
        String cvc_check = currentcard.getCvc_check();
        String card_id = currentcard.getCard_id();
        String exp_month = currentcard.getExp_month();
        String exp_year = currentcard.getExp_year();

        setcardtypeimage(imgv_card_type_stripe, type);
        setCardText(txtv_card_details_stripe,type,last4);
        setcheckedornot(imgv_checked_stripe,card_id,txtv_card_details_stripe);

        return view;
    }

    private void setcheckedornot(ImageView imgv_checked_stripe, String card_id, TextView txtv_card_details_stripe) {
        String def_card = UserManager.getInstance().getStripeUserDetails().getDefault_source();
        if(card_id.equals(def_card))
        {
            imgv_checked_stripe.setVisibility(View.VISIBLE);
            txtv_card_details_stripe.setTextColor(context.getResources().getColor(R.color.stripeBlue));
        }
        else {
            imgv_checked_stripe.setVisibility(View.INVISIBLE);
            txtv_card_details_stripe.setTextColor(context.getResources().getColor(R.color.font_pdp_product_name));
        }
    }

    private void setCardText(TextView txtv_card_details_stripe, String type, String last4) {
        String text = "<b>" + type + "</b> " + " Ending in " + "<b>" + last4 + "</b> ";
        txtv_card_details_stripe.setText(Html.fromHtml(text));
    }

    private void setcardtypeimage(ImageView imgv_card_type_stripe, String type) {
        if (type.equalsIgnoreCase("Visa"))
            imgv_card_type_stripe.setImageDrawable(context.getResources().getDrawable(R.drawable.visa));
        else if (type.equalsIgnoreCase("MasterCard"))
            imgv_card_type_stripe.setImageDrawable(context.getResources().getDrawable(R.drawable.mastercard));
        else if (type.equalsIgnoreCase("Amex")||type.equalsIgnoreCase("American Express"))
            imgv_card_type_stripe.setImageDrawable(context.getResources().getDrawable(R.drawable.amex));
        else if (type.equalsIgnoreCase("Discover"))
            imgv_card_type_stripe.setImageDrawable(context.getResources().getDrawable(R.drawable.discover));
        else
            imgv_card_type_stripe.setImageDrawable(context.getResources().getDrawable(R.drawable.stripe_card));
    }
}
