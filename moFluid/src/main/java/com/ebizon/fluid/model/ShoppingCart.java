package com.ebizon.fluid.model;

import android.util.Log;

import com.mofluid.magento2.database.MyDataBaseAdapter;
import com.mofluid.magento2.fragment.BaseFragment;
import com.mofluid.magento2.fragment.MyCartFragment;
import com.mofluid.magento2.fragment.SimpleProductFragment2;
import com.mofluid.magento2.service.AppController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by manish on 08/03/16.
 */
public class ShoppingCart {
    private static ShoppingCart ourInstance = new ShoppingCart();
    private MyDataBaseAdapter dataBaseAdapter;
    public static ShoppingCart getInstance() {
        return ourInstance;
    }

    private HashMap<String, ShoppingCartItem> cart = new HashMap<>();
    private String coupon;

    private ShoppingCart() {
        this.dataBaseAdapter=new MyDataBaseAdapter(AppController.getContext());
    }
    public void setCartFromServer()
    {
      /*  CartSync.getInstance().UpdateAppCart(UserManager.getInstance().getUser().getId(), new IResponseListener() {
            @Override
            public void onResponse(boolean result, ArrayList<ShoppingCartItem> data) {
                if(result==true)
                {
                    for (int i = 0; i < data.size(); i++) {
                        ShoppingCartItem item = data.get(i);
                        ShoppingCart.getInstance().addItemFromServer(item);
                    }
                }
                else {

                }
            }
        });*/
    }
    public void addItem(ShoppingCartItem item){
        this.cart.put(item.getShoppingItem().getId(), item);
        this.dataBaseAdapter.insertItemtoCart(item);
    }

    public void addItem(ShoppingCartItem item, BaseFragment simpleProductFragment2){
        this.cart.put(item.getShoppingItem().getParentID(), item);
        this.dataBaseAdapter.insertItemtoCart(item);
        UserProfileItem currentuser = UserManager.getInstance().getUser();
      /*  if(currentuser!=null && currentuser.getLogin_status()!="0") {
            String customerid = currentuser.getId();
            CartSync.getInstance().updateServerCartAdd(customerid, item.getShoppingItem().getId(),item.getCount(),simpleProductFragment2); //CARTSYNC ADD
        }
        else {*/
            Log.d("Piyush", "Guest user, not calling cart sync add ");
            if (simpleProductFragment2.getClass().getSimpleName().equals("SimpleProductFragment2")) {
                SimpleProductFragment2 f = (SimpleProductFragment2) simpleProductFragment2;
                f.callF();
                Log.d("PiyushCarySync", "called fragmet name");
            }
      //  }
    }

    public void deleteItem(ShoppingCartItem item, MyCartFragment myCartFragment){
        this.cart.remove(item.getShoppingItem().getId());
        int res = this.dataBaseAdapter.deleteSingleItemFromCart(item);
        if(res>0)
        {
            //success
        }
		
      /*  UserProfileItem currentuser = UserManager.getInstance().getUser();
        if(currentuser!=null && currentuser.getLogin_status()!="0") {
            String customerid = currentuser.getId();
            CartSync.getInstance().updateServerCartDelete(customerid,item.getShoppingItem().getId()); //CARTSYNC DELETE
        }
        else
            Log.d("Piyush", "Guest user, not calling cart sync delete ");
*/
        if(this.cart.isEmpty()){
            this.coupon = null;
        }
    }

    public int getCount(String id){
        int count = 0;

        if(this.cart.containsKey(id)){
            count = this.cart.get(id).getCount();
        }

        return count;
    }

    public int getCount(ShoppingItem item){
        return this.getCount(item.getId());
    }

    public int getNumDifferentItems(){
        return this.cart.size();
    }

    public double getSubTotal(ShoppingItem item){
        return getCount(item) * item.getFinalPrice();
    }

    public double getSubTotal(){
        double subTotal = 0.0;

        Set<String> itemIds = this.cart.keySet();
        for(String id : itemIds){
            ShoppingItem item = this.cart.get(id).getShoppingItem();
            subTotal += this.getSubTotal(item);
        }

        return subTotal;
    }

    public Collection<ShoppingCartItem> getCartItems(){
        return this.cart.values();
    }

    public void clearCart(){
        this.cart.clear();
    }

    public void addItemFromServer(ShoppingCartItem item)
    {
        this.cart.put(item.getShoppingItem().getId(), item);
    }

    public void addAllCartFromDB(HashMap<String,ShoppingCartItem> cartList)
    {
        this.cart.putAll(cartList);
    }

    public String getCartItemsId()
    {
        String res = "";
        Iterator itr =  this.cart.keySet().iterator();
             while (itr.hasNext()) {
            String key = (String) itr.next();
            if(itr.hasNext())
            res+=key+",";
            else
                res+=key+"";
        }
        Log.d("HashMap",res);
        return res;
    }

    public ShoppingCartItem getSingleCartItemfromID(String key)
    {
        return this.cart.get(key);
    }


}
