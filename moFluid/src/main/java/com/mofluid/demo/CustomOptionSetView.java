package com.mofluid.magento2;

import com.ebizon.fluid.model.CustomOptionSet;

import java.util.ArrayList;

/**
 * Created by manish on 22/01/16.

 */
public class CustomOptionSetView{
    private final CustomOptionSet optionSet;
    private final ArrayList<CustomOptionView> views;

    public CustomOptionSetView(CustomOptionSet optionSet, ArrayList<CustomOptionView> views){
        this.optionSet = optionSet;
        this.views = views;
    }

    public void check(int optionId){
        if (this.optionSet.getType().equalsIgnoreCase("checkbox")){
            for(CustomOptionView view : this.views){
                if (view.getId() == optionId){
                    view.toggle();
                }
            }
        }else if(this.optionSet.getType().equalsIgnoreCase("radio")){
            for(CustomOptionView view : this.views){
                view.check(false);
                if (view.getId() == optionId){
                    view.check(true);
                }
            }
        }
    }

    public CustomOptionSet getOptionSet() {
        return optionSet;
    }

    public ArrayList<CustomOptionView> getViews() {
        return views;
    }
}
