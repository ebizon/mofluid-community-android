package com.ebizon.fluid.Utils;

/**
 * Created by ebizon on 28/6/16.
 */
public class ConvertValues {
    private static ConvertValues ourInstance = new ConvertValues();

    public static ConvertValues getInstance() {
        return ourInstance;
    }

    private ConvertValues() {
    }
    public int convertStrToInt(String str)
    {
        int value=0;
        try {
             value =(int)Float.parseFloat(str);
        }
        catch (Exception ex) {


        }
        return value;
    }
    public double convertStrToDouble(String str)
    {
        double value=0;
        try {
            value = Double.parseDouble(str);
        }
        catch (Exception ex) {


        }
        return value;
    }
}
