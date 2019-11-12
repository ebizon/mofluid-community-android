package com.mofluid.utility_new;

import android.util.Pair;

import com.ebizon.fluid.model.WebApiManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitializerApiManager {
private static InitializerApiManager instance;
private InitializerApiManager(){}
public static InitializerApiManager getInstance(){
if(instance==null)
    instance=new InitializerApiManager();
return instance;
}
public void getCurrency(final Callback callback){
String url= WebApiManager.getInstance().getStoreDetailsURL();
    Map<String,String> header=new HashMap<>();
ResponseManager manager=new ResponseManager(ResponseManager.REQUEST_TYPE_GET, url, header, null, new ResponseReceived() {
    @Override
    public void onResponseReceived(Object response, int response_code) {
                            if(response==null){
                                callback.callback(null,response_code);
                                return;
                            }
        List<Pair> list=new ArrayList<>();
         try {
             JSONObject jsonObject = new JSONObject((String) response);
             JSONObject currency=jsonObject.getJSONObject("currency");
             String curs=currency.getString("allow");
             String[] cur_list=curs.split(",");
             for(int i=0;i<cur_list.length;i++)
                 list.add(new Pair(cur_list[i],cur_list[i]));
          callback.callback(list,response_code);
         }catch (JSONException e){
             L.d("saddam","error="+e.getMessage());
             callback.callback(null,response_code);
             return;
         }

    }
});
manager.get();
}
public void getLanguage(final Callback callback){
    String url= null;
    Map<String,String> header=new HashMap<>();
    ResponseManager manager=new ResponseManager(ResponseManager.REQUEST_TYPE_GET, url, header, null, new ResponseReceived() {
        @Override
        public void onResponseReceived(Object response, int response_code) {
            if(response==null){
                callback.callback(null,response_code);
                return;
            }

        }
    });
    manager.get();
}
}
