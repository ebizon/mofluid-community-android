package com.mofluid.fragment_new;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.ebizon.fluid.Utils.MySharedPreferences;
import com.mofluid.magento2.R;
import com.mofluid.utility_new.Callback;
import com.mofluid.utility_new.InitializerApiManager;

import java.util.ArrayList;
import java.util.List;

public class LangCurrecncyFragment extends com.mofluid.magento2.fragment.BaseFragment{
private ListView list_view;
private View view;
public final  int LANG_VIEW=0,CURRENCY_VIEW=1;
private int cur_view=LANG_VIEW;
private List<Pair> key_value_list;
public void setView(int view){
this.cur_view=view;
}
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(view==null)
        view=inflater.inflate(R.layout.lang_currency,null);
        this.list_view=(ListView) view.findViewById(R.id.list);
        switch (cur_view){
            case LANG_VIEW:
                setLangView();
                break;
            case CURRENCY_VIEW:
                setCurrencyView();
                break;
        }
        this.list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String value=(String) key_value_list.get(i).second;
                  switch (cur_view){
                      case LANG_VIEW:
                         setStoreId(value.split(",")[0]);
                         setSystemLang(value.split(",")[1]);
                          break;
                      case CURRENCY_VIEW:
                          setCurrency(value);
                          break;
                  }
            }
        });
        return view;
    }
private void errorMessage(){
    Toast.makeText(getActivity(),"Some error occured.",Toast.LENGTH_SHORT).show();
}
private void setLangView(){
    InitializerApiManager.getInstance().getLanguage(new Callback() {
        @Override
        public void callback(Object o, int response_code) {
                            if(o==null){
                            errorMessage();
                            return;
                            }
            key_value_list=(List<Pair>)o;
                            setAdapter();

        }
    });
}
private void setCurrencyView(){
InitializerApiManager.getInstance().getCurrency(new Callback() {
    @Override
    public void callback(Object o, int response_code) {
        if(o==null){
            errorMessage();
            return;
        }
        key_value_list=(List<Pair>)o;
        setAdapter();
    }
});
}
private void setAdapter(){
    ArrayList<String> items=new ArrayList<>();
    for(int i=0;i<this.key_value_list.size();i++)
        items.add((String) this.key_value_list.get(i).second);
    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, items);
    this.list_view.setAdapter(adapter);
}
private void setStoreId(String id){
    MySharedPreferences.getInstance().set(MySharedPreferences.STORE_ID,id);
}
private void setSystemLang(String lang_code){
    MySharedPreferences.getInstance().set(MySharedPreferences.SYSTEM_LANG,lang_code);
}
private void setCurrency(String currency_code){
    MySharedPreferences.getInstance().set(MySharedPreferences.CURRENCY_CODE,currency_code);

}
}
