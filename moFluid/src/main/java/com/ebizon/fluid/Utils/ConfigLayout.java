package com.ebizon.fluid.Utils;

import android.widget.LinearLayout;

/**
 * Created by piyush on 12/5/16.
 */
public class ConfigLayout {

    String header_id;
    SingleConfigLayout layout;


    public ConfigLayout()
    {

    }

    public ConfigLayout(String header_id, SingleConfigLayout layout)
    {
        this.header_id=header_id;
        this.layout=layout;
    }

    public int getHeader_id() {
        return header_id.hashCode();
    }

    public void setHeader_id(String header_id) {
        this.header_id = header_id;
    }

    public SingleConfigLayout getLayout() {
        return layout;
    }

    public void setLayout(SingleConfigLayout layout) {
        this.layout = layout;
    }


}
