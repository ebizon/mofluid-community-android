package com.mofluid.model_new;

import com.mofluid.utility_new.L;

public final class RespoonseCodeMap {
  private static final String TAG="RespoonseCodeMap";
  public static final int NO_INTERNETCONNECTION=0,WRONG_RESPONSE=1,BLANK_RESPONSE=2,PAYMENT_FAILED=3,PAYMENT_SUCCESS=4;
  public static String getMessage( int res_code) {
      L.d(TAG,"res_code="+res_code);
      if(res_code==NO_INTERNETCONNECTION)
          return "No Internet Connection!";
      if(res_code==WRONG_RESPONSE)
          return "Wrong response received.";
      if(res_code==BLANK_RESPONSE)
          return "No data found!";
      if(res_code==PAYMENT_FAILED)
          return "Payment failed!";
      if(res_code==PAYMENT_SUCCESS)
          return "Payment success.";
      return "";
  }
   }
