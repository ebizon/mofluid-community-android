package com.ebizon.fluid.StripePayment;

import com.stripe.android.Stripe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by piyush-ios on 28/12/16.
 */
public class StripeUser {

    public void setStripeCustomerID(String stripeCustomerID) {
        this.stripeCustomerID = stripeCustomerID;
    }

    String stripeCustomerID;
    String default_source;

    public void setDefault_source(String default_source) {
        this.default_source = default_source;
    }

    String description;
    String email;
    String sources_total;
    String url;
    ArrayList<StripeuserCard> savedCards;

    public StripeUser(String stripeCustomerID, String default_source, String description, String email, String sources_total, String url, ArrayList<StripeuserCard> savedCards) {
        this.stripeCustomerID = stripeCustomerID;
        this.default_source = default_source;
        this.description = description;
        this.email = email;
        this.sources_total = sources_total;
        this.url = url;
        this.savedCards = savedCards;
    }

    public static StripeUser parseUserResponseStripe(JSONObject output) {
        StripeUser user = null;
        ArrayList<StripeuserCard> cardList;
        try {
            String stripeCustomerID = output.getString("id");
            String default_cource = output.getString("default_source");
            String description = output.getString("description");
            String email = output.getString("email");
            JSONObject sources = output.getJSONObject("sources");
            String source_total = sources.getString("total_count");
            String url = sources.getString("url");
            JSONArray data = sources.getJSONArray("data");

            try {
                cardList = new ArrayList<>(Integer.parseInt(source_total));
            } catch (Exception e) {
                cardList = new ArrayList<>();
            }

            for (int i = 0; i < data.length(); i++) {
                JSONObject singleCard = data.getJSONObject(i);
                StripeuserCard card = parseSingleSource(singleCard);
                cardList.add(card);
            }
            user = new StripeUser(stripeCustomerID, default_cource, description, email, source_total, url, cardList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static StripeuserCard parseSingleSource(JSONObject singleCard) {
        try {
            String card_id = singleCard.getString("id");
            String object = singleCard.getString("object");
            String brand = singleCard.getString("brand");
            String country = singleCard.getString("country");
            String cvc_check = singleCard.getString("cvc_check");
            String exp_month = singleCard.getString("exp_month");
            String exp_year = singleCard.getString("exp_year");
            String last4 = singleCard.getString("last4");
            String name = singleCard.getString("name");
            StripeuserCard card = new StripeuserCard(card_id, object, brand, country, cvc_check, exp_month, exp_year, last4, name);
            return card;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getStripeCustomerID() {
        return stripeCustomerID;
    }

    public String getDefault_source() {
        return default_source;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public String getSources_total() {
        return sources_total;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<StripeuserCard> getSavedCards() {
        return savedCards;
    }

    public void addNewStripeCard(StripeuserCard singleCard)
    {
        this.savedCards.add(singleCard);
    }


}
