package com.ebizon.fluid.Utils;

import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by piyush on 16/5/16.
 */
public class SingleConfigLayout {

    LinearLayout parent;
    ArrayList<View> child;

    public void setChild(ArrayList<View> child) {
        this.child = child;
    }

    public SingleConfigLayout(LinearLayout parent, ArrayList<View> child)
    {
        this.parent=parent;

        this.child=child;
    }

    public SingleConfigLayout(LinearLayout parent)
    {
        this.parent=parent;
        this.child=new ArrayList<>();
    }

    public LinearLayout getParent() {
        return parent;
    }


    public ArrayList<View> getChild() {
        return child;
    }




}
