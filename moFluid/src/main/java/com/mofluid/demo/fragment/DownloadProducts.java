package com.mofluid.magento2.fragment;

/**
 * Created by prashant-ios on 5/5/16.
 */
public class DownloadProducts {
    String product_name;
    String order_date;
    String order_status;
    String order_remain;

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }

    String download_url;

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getOrder_remain() {
        return order_remain;
    }

    public void setOrder_remain(String order_remain) {
        this.order_remain = order_remain;
    }
}
