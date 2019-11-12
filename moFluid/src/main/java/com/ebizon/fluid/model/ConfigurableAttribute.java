package com.ebizon.fluid.model;

import com.ebizon.fluid.Utils.Validation;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by manish on 19/01/16.
 */
public class ConfigurableAttribute {
    private final String id;
    private final String label;
    private final String labelValue;
    private final String price;

    public static ConfigurableAttribute create(JSONObject jsonObject){
        ConfigurableAttribute configurableAttribute = null;
        try {
            String id = jsonObject.getString("attribute_id");
            String label = jsonObject.getString("frontend_label");
            String labelValue = jsonObject.getString("label");
            String price = jsonObject.getString("pricing_value");

            if (!Validation.isNull(id, label, labelValue, price)) {
                configurableAttribute = new ConfigurableAttribute(id, label, labelValue, price);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return configurableAttribute;
    }

    private ConfigurableAttribute(String id, String label, String labelValue, String price){
        this.id = id;
        this.label = label;
        this.labelValue = labelValue;
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }

        ConfigurableAttribute that = (ConfigurableAttribute)obj;

        return this.id.equals(that.id) && this.label.equals(that.label) && this.labelValue.equals(that.labelValue)
                && this.price.equals(that.price);

    }

    public String getId(){return this.id;}

    public String getLabel(){return this.label;}

    public String getLabelValue(){return this.labelValue;}

    public double getPrice(){return Double.parseDouble(this.price);}
}
