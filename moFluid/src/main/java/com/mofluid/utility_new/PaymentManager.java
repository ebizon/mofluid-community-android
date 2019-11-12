package com.mofluid.utility_new;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;

import com.ebizon.fluid.Utils.AddressManager;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.ebizon.fluid.Utils.Utils;
import com.ebizon.fluid.model.Address;
import com.mofluid.fragment_new.WebViewFragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.HashMap;

public class PaymentManager {
    private final String TAG="PaymentManager";
    private static PaymentManager instance;
    private PaymentManager(){

    }
    public static PaymentManager getInstance(){
        if(instance==null)
            instance=new PaymentManager();
        return instance;
    }
  public void getDirectPayTransToken(double amount,final Callback callback){
      String url = URLManager.getDirectPayURL();
      HashMap<String,String> header=new HashMap<>();
      MySharedPreferences ob=MySharedPreferences.getInstance();
      Address shipping_address=null,billing_address=null;
      if(ob.get(ob.SHIPPING_ADDRESS_ID)!=null) {
          int ship_id = Integer.parseInt(ob.get(ob.SHIPPING_ADDRESS_ID));
          shipping_address= AddressManager.getInstance().getAddress(ship_id);
      }
      if(ob.get(ob.BILLING_ADDRESS_ID)!=null)
      {
          int bill_id=Integer.parseInt(ob.get(ob.BILLING_ADDRESS_ID));
          billing_address=AddressManager.getInstance().getAddress(bill_id);
      }
      String date= Utils.getCurrentDate("yyyy/MM/dd HH:mm");
      String body="";
      ResponseManager manager=new ResponseManager(ResponseManager.REQUEST_TYPE_POST, url, header, body, "application/xml; charset=utf-8",new ResponseReceived() {
          @Override
          public void onResponseReceived(Object response, int response_code) {
              if(response==null){
                  callback.callback(null,response_code);
                  return;
              }

              XmlPullParserFactory parserFactory;
              String trans_token=null;
              try {
              parserFactory=XmlPullParserFactory.newInstance();
                  XmlPullParser parser=parserFactory.newPullParser();
                  parser.setInput(new StringReader((String)response));
                  int event_type=parser.getEventType();
                  while (event_type!=XmlPullParser.END_DOCUMENT){
                      switch(event_type){
                          case XmlPullParser.START_TAG:
                              String name=parser.getName();
                              //L.d(TAG,"name="+name+ " token="+parser.nextText());
                              if(name.equals("TransToken"))
                              {
                                  trans_token=parser.nextText();
                                 // L.d(TAG,parser.getText()+parser.nextText()+parser.getText());
                              }
                              break;
                      }
                      event_type=parser.next();
                  }
              }catch (Exception e){
                  L.d(TAG,"error="+e.getMessage());
                  callback.callback(null,response_code);
              }
              callback.callback(trans_token,response_code);
          }
      }) ;
      manager.get();
  }
 public void openDirectPay(final double amount, final Context context,final Callback callback){
   this.getDirectPayTransToken(amount, new Callback() {
       @Override
       public void callback(Object o, int response_code) {
                           if(o==null){
                               callback.callback(o,response_code);
                               return;
                           }
                           L.d(TAG,"trans token="+o);
           String url=URLManager.getDirectPayWebViewURL((String) o);
           WebViewFragment.callback=callback;
           Intent intent=new Intent(context, WebViewFragment.class);
           intent.putExtra("url",url);
           context.startActivity(intent);
       }
   });
 }
}
