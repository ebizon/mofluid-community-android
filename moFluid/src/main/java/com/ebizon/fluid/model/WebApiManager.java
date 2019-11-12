package com.ebizon.fluid.model;

import android.app.Activity;
import android.content.Context;

import com.ebizon.fluid.Utils.Config;
import com.ebizon.fluid.Utils.ConfigConstants;
import com.ebizon.fluid.Utils.MySharedPreferences;
import com.mofluid.magento2.R;

/**
 * Created by manish on 15/01/16.
 */
public class WebApiManager {
        private static  WebApiManager ourInstance = new WebApiManager();
        private String baseURL = "";
        private String baseURL2="";

        public static WebApiManager getInstance() {
            return ourInstance;
        }

        public static void setInstance()
        {
            ourInstance= new WebApiManager();
        }

        private WebApiManager() {
            this.initBaseURL();
            this.initBaseURL2();
        }

    private void initBaseURL(){
        if(MySharedPreferences.getInstance().get(MySharedPreferences.BASE_URL)!=null)
            this.baseURL=MySharedPreferences.getInstance().get(MySharedPreferences.BASE_URL);
        else
        this.baseURL = Config.getInstance().getBaseURL();
        this.baseURL +="?";
        this.baseURL += "store=" + Config.getInstance().getStoreValue();
        this.baseURL+="&callback="+"";
        this.baseURL += "&service=";
    }

    private void initBaseURL2(){
        if(MySharedPreferences.getInstance().get(MySharedPreferences.BASE_URL)!=null) this.baseURL2=MySharedPreferences.getInstance().get(MySharedPreferences.BASE_URL);
        else
        this.baseURL2 = Config.getInstance().getBaseURL();
//        this.baseURL2 +="callback&"; //MAGENTO2
        this.baseURL2 +="/api?";
        this.baseURL2 += "&cmd=";
    }

    private String getURL(String serviceExt){
        return this.baseURL + serviceExt;
    }
    private String getURL2(String serviceExt){
        return this.baseURL2 + serviceExt;
    }
   public String getBaseURL(){
        return this.baseURL;
   }
    public String getStoreDetailsURL(Context context){
        return this.getURL(context.getResources().getString(R.string.store_details));
    }
    public String getStoreDetailsURL(){
        return this.getURL("storedetails&theme=modern");
    }
    public String getFeatureProductsURL(Context context){
        return this.getURL(context.getResources().getString(R.string.featured_products));
    }
    //newly added by avnish
    public String getRelatedProductsURL(Context context){
        return this.getURL(context.getResources().getString(R.string.related_products))+"&product_id=%1$s";
    }
    public String getNewProductsURL(Context context){
        return this.getURL(context.getResources().getString(R.string.new_products));
    }

    public String getCouponURL(Context context){
        String couponURL = this.getURL(context.getResources().getString(R.string.check_out_service));
        couponURL += "&customerid=%1$s&currency=%2$s&products=%3$s&couponCode=%4$s";

        return couponURL;
    }

    public String getShippingMethodURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.check_out_service));
        url += "&customerid=%1$s&currency=%2$s&products=%3$s&address=%4$s&is_create_quote=%5$s&find_shipping=%6$s";

        return url;
    }

    public String getDownloadableShippingMethodURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.check_out_service));
        url += "&customerid=%1$s&currency=%2$s&products=%3$s&address=%4$s&is_create_quote=%5$s&shipmethod=%6$s&theme=%7$s";

        return url;
    }

    public String getPlaceOrderURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.place_order));
        url += "&customerid=%1$s&currency=%2$s&products=%3$s&address=%4$s&is_create_quote=%5$s&shipmethod=%6$s&paymentmethod=%7$s";

        return url;
    }


    public String getPlaceOrderURLStripe(Context context){
        String url = this.getURL(context.getResources().getString(R.string.place_order));
        url += "&customerid=%1$s&currency=%2$s&products=%3$s&address=%4$s&is_create_quote=%5$s&shipmethod=%6$s&paymentmethod=%7$s&transactionid=%8$s&messages=%9$s";

        return url;
    }

    public String getProductDetailURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.product_details));
        url += "&productid=%1$s";

        return url;
    }

    public String getProductReviewURL(Context context){
        String url = this.getURL2(context.getResources().getString(R.string.product_reviews));
        url += "&productid=%1$s";

        return url;
    }

    public String writeProductReviewURL(Context context){
        String url = this.getURL2(context.getResources().getString(R.string.write_reviews));
        url += "&store=%1$s&productid=%2$s&customerid=%3$s&nickname=%4$s&pricerating=%5$s&valuerating=%6$s&qualityrating=%7$s&reviewsummary=%8$s&comment=%9$s";

        return url;
    }

    public String getConfigurableProductURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.configurable_product_details));
        url += "&productid=%1$s";

        return url;
    }

    public String getProductImagesURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.product_details_images));
        url += "&productid=%1$s";

        return url;
    }

    public String getGroupedProductURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.grouped_product_details));
        url += "&productid=%1$s";

        return url;
    }

    public String getProductListURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.products));
        url += "&sorttype=%1$s&sortorder=%2$s&categoryid=%3$s&currentpage=%4$s&pagesize=%5$s";

        return url;
    }

    public String getSearchURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.search_data));
        url += "&sorttype=%1$s&sortorder=%2$s&search_data=%3$s&currentpage=%4$s&pagesize=%5$s";

        return url;
    }
    public String getSearchFilterURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.search_filter_data));
        url += "&search_data=%1$s";
        return url;
    }
    public String getCategoryURL(Context context){

        return this.getURL(context.getResources().getString(R.string.category_navigator));
    }


    public String getMultiStoreDataURL(Context context){

        return this.getURL2(context.getResources().getString(R.string.multi_store));
    }

    public String getUserProfileURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.user_profile));
        url += "&customerid=%1$s";

        return url;
    }

    public String getSignupURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.create_user));
        url += "&firstname=%1$s&lastname=%2$s&email=%3$s&password=%4$s";
        //store, $deviceid="KJKJ"+&JHJHJH="KKKJ", $pushtoken, $platform, $appname, $description

        return url;
    }

    public String getSignInURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.login_access));
        url += "&username=%1$s&password=%2$s";

        return url;
    }

    public String getPushRegisterURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.push_register));
        url += "&pushtoken=%1$s&platform=%2$s&deviceid=%3$s&appname=%4$s&description=%5$s";        //store, $deviceid="KJKJ"+&JHJHJH="KKKJ", $pushtoken, $platform, $appname, $description

        return url;
    }

    public String getUpdateProfileURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.update_profile));
        url += "&customerid=%1$s&billaddress=%2$s&shippaddress=%3$s&profile=%4$s&shipbillchoice=%5$s";
        return url;
    }

    public String getChangePasswordURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.change_password_service));
        url += "&customerid=%1$s&username=%2$s&oldpassword=%3$s&newpassword=%4$s";

        return url;
    }

    public String getForgotPasswordURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.forgot_password));
        url += "&email=%1$s";

        return url;
    }

    public String getSocialLogingURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.social_login));
        url += "&username=%1$s&firstname=%2$s&lastname=%3$s";

        return url;
    }

    public String getAllCMSPages(Context context){
        String url= this.getURL(context.getResources().getString(R.string.cms_page));
        url+="&pageId=%1$s";
        return url;
    }

    public String getWishList(){
        String url= this.getURL("getWishlist");
        url+="&customerid=%1$s";
        return url;
    }

    public String addToWishList(){
        String url= this.getURL("addtoWishlist");
        url+="&customerid=%1$s&pid=%2$s";
        return url;
    }

    public String removeFromWishList(){
        String url= this.getURL("removefromWishlist");
        url+="&customerid=%1$s&itemid=%2$s";
        return url;
    }


    public String getAllCountryURL(Context context){
        return this.getURL(context.getResources().getString(R.string.country_list));
    }
    public String getAllOrderURL(Context context){
        String url =this.getURL(context.getResources().getString(R.string.myorders));
        url += "&customerid=%1$s";
        return url;
    }
    public String getAlldownloadsURL(Context context){
        String url =this.getURL(context.getResources().getString(R.string.mydownload_service));
        url += "&customerid=%1$s";
        return url;
    }

    public String getAllStateURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.state_list));
        url += "&country=%1$s";
        return url;
    }
    public String getFilterProductsURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.get_filter_products));
        url+="&categoryid=%1$s&filter_type=%2$s&attribute_id=%3$s&sorttype=%4$s&sortorder=%5$s";
        return url;
    }

    public String getFacebookURL(){
        return ConfigConstants.FacebookURL;
    }

    public String getFilteredProductListURL2(Activity context) {

        String url = this.getURL(context.getResources().getString(R.string.filter_products));
        url += "&sorttype=%1$s&sortorder=%2$s&categoryid=%3$s&filter_type=%4$s&attribute_id=%5$s";

        return url;
    }

    public String getFilteredProductListURL(Activity context) {

        String url = this.getURL(context.getResources().getString(R.string.filter_products));
        url += "&sorttype=%1$s&sortorder=%2$s&categoryid=%3$s&filterdata=%4$s";

        return url;
    }
    public String getSearchFilteredProdcutListURL(Context context){
        String url = this.getURL(context.getResources().getString(R.string.search_data));
        url += "&sorttype=%1$s&sortorder=%2$s&search_data=%3$s&filterdata=%4$s";
        return url;
    }

    public String getBestSellerURL(Activity context) {
        return this.getURL(context.getResources().getString(R.string.bestsellers));
    }

    public String addToCartServer(Context context){
        String url =  this.getURL(context.getResources().getString(R.string.add_to_cart_server));
        url+= "&customerid=%1$s&product_id=%2$s&qty=%3$s";
        return url;
    }
    public String deleteFromCartServer(Context context){
        String url = this.getURL(context.getResources().getString(R.string.delete_from_cart_server));
        url+= "&customerid=%1$s&product_id=%2$s";
        return url;
    }

    public String getAnonymousCartQuantity(Context context){
        String url = this.getURL(context.getResources().getString(R.string.get_anonymous_cart_qiuantity));
        url+= "&product_id=%1$s";
        return url;
    }

    public String getUserCartFromServer(Context context){
        String url = this.getURL(context.getResources().getString(R.string.get_from_cart));
        url+= "&customerid=%1$s";
        return url;
    }

    public String getPaymentMethods(Context context) {
        String url = this.getURL(context.getResources().getString(R.string.get_payment_method));
        return url;
    }

    public String stripecustomercreate(Context context) {
        String url = this.getURL(context.getResources().getString(R.string.stripecustomercreate));
        url+= "&mofluid_Custid=%1$s&token_id=%2$s&email=%3$s&name=%4$s";
        return url;
    }

    public String chargeStripe(Context context) {
        String url = this.getURL(context.getResources().getString(R.string.chargeStripe));
        url+= "&customer_id=%1$s&price=%2$s&card_id=%3$s";
        return url;
    }

    public String retrieveCustomerStripe(Context context) {
        String url = this.getURL(context.getResources().getString(R.string.retrieveCustomerStripe));
        url+= "&customer_id=%1$s";
        return url;
    }

    public String createCardStripe(Context context) {
        String url = this.getURL(context.getResources().getString(R.string.createCardStripe));
        url+= "&customer_id=%1$s&token_id=%2$s";
        return url;
    }

    public String getTokenFromServer(Context context){
        String url = this.getURL(context.getResources().getString(R.string.gettoken));
        url+= "&authappid=%1$s";
        return url;
    }
    public String geCCAvenuePaymentURL() {
        String  baseUrl="http://brooksbicycle.com/index.php/paymentccavenue?paymentdata=";
        return baseUrl;
    }

    public String getReoredURL(Activity context) {
        String url = this.getURL(context.getResources().getString(R.string.re_order));
        url += "&pid=%1$s&orderid=%2$s";

        return url;
    }
    //cart
    public String getcartURL(){
        String url= this.getURL("get_cart_items");
        url+="&customerid=%1$s";
        return url;
    }
    public String clearCartURL(){
        String url=this.getURL("clearcart");
        url+="&customerid=%1$s";
        return url;
    }
    public String getUpdateCartItemURL(){
        String url=this.getURL("update_cart_item");
        url+="&customerid=%1$s&product_id=%2$s&count=%3$s";
        return url;

    }
    public String getRemoveCartItemURL(){
        String url=this.getURL("remove_cart_item");
        url+="&customerid=%1$s&itemid=%2$s";
        return url;
    }
    public String getInsertCartItemURL(){
        String url=this.getURL("add_cart_item");
        url+="&customerid=%1$s&item_data=%2$s";
        return url;
    }
    //address api
    public String getAddressListURL(){
    String url=this.getURL("address_list");
    url+="&customerid=%1$s";
    return url;
    }
    public String getAddressUrl(){
        String url=this.getURL("get_address");
        url+="&customerid=%1$s&addressid=%2$s";
        return url;
    }
    public String getAddAddressURL(){
        String url=this.getURL("add_new_address");
        url+="&customerid=%1$s&address_data=%2$s";
        return url;
    }
    public String getUpdateAddressURL(){
        String url=this.getURL("update_address");
        url+="&customerid=%1$s&addressid=%2$s&address_data=%3$s";
        return url;
    }
    public String getShippingMethodWithoutAddressURL(){
        String url=this.getURL("get_shipping_method");
        url+="&customerid=%1$s";
        return url;
    }

    public String getShippingMethodWithAddressURL(){
        String url=this.getURL("get_shipping_method");
        url+="&customerid=%1$s&addressid=%2$s";
        return url;
    }
    public String getcheckoutURL(){
        String url=this.getURL("checkout_new");
        url+="&customerid=%1$s&shipmethod=%2$s&shipcarrier=%3$s";
        return url;
    }
    public String getPlaceOrderUrl(){
        String url=this.getURL("placeorder");
        url+="&customerid=%1$s&paymentmethod=%2$s";
        return url;
    }
    public String getResetPasswordAppURL(){
        return "https://api.mofluid.com/forgotPassword/";

    }
    public String getCatalogProductImagePath(){
            String url=null;
        if(MySharedPreferences.getInstance().get(MySharedPreferences.BASE_URL)!=null)
            url=MySharedPreferences.getInstance().get(MySharedPreferences.BASE_URL);
        else
            url = Config.getInstance().getBaseURL();
        url=url.replace("/mofluidapi2","");
            return url+"/pub/media/catalog/product";
    }
  //New API integration
  public String getCategoryProductsURL(){
            String url=this.getURL("get_category_product");
            url+="&categoryid=%1$s&sortorder=%2$s&sorttype=%3$s&currentpage=%4$s&pagesize=%5$s&filterdata=%6$s";
            return url;
  }
}
