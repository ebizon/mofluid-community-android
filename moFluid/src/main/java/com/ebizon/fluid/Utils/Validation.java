package com.ebizon.fluid.Utils;

/**
 * Created by ebizon on 17/11/15.
 */
public class Validation {
    public static boolean isStrNotEmpty(String arg)
    {
        if(arg!=null) {
            arg = arg.trim();
            return arg.length() > 0;
        }else
            return false;
    }

    public static boolean isNull(Object...args){
        boolean isN = false;
        for (Object arg : args) {
            if (arg == null){
                isN = true;
                break;
            }
        }

        return  isN;
    }
}
