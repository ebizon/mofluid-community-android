package com.ebizon.fluid.model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by manish on 12/01/16.
 */
public class CustomOptionSet {
    private final String  id;
    private final String name;
    private final String type;
    private final String isRequired;
    private final ArrayList<CustomOption> options;
    public CustomOptionSet(String id, String name, String type, String isRequired, ArrayList<CustomOption> options){
        this.id = id;
        this.name = name;
        this.type = type;
        this.isRequired = isRequired;
        this.options = options;
    }

    public String getName(){return this.name;}

    public String getIsRequired(){return this.isRequired;}

    public String getType() {return this.type;}

    public int getId(){
        return Integer.parseInt(this.id);
    }

    public ArrayList<CustomOption> getoptionsList(){
        return this.options;
    }

    public Iterator<CustomOption> getOptions(){
        return this.options.iterator();
    }

    public ArrayList<String> getSelectedIds(){
        ArrayList<String> selectedIds = new ArrayList<>();
        for (CustomOption option : this.options) {
            if (option.getSelected()) {
                selectedIds.add(option.getIdStr());
            }
        }

        return selectedIds;
    }

    public double getCustomCost() {
        double price = 0.0;
        if(options!=null) {
            for (CustomOption option : this.options) {
                if (option.getSelected()) {
                    price += option.getPrice();
                }
            }
        }

        return price;
    }
}
