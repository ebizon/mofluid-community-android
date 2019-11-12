package com.ebizon.fluid.model;

/**
 * Created by ebizon on 1/7/16.
 */
public class FilterOptionItem {
    String name,id;
    boolean isChecked;
    String counter;

    public FilterOptionItem(String id, String name, boolean isChecked,String counter) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
        this.counter = counter;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getId() {
        return id;
    }

    public FilterOptionItem setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public FilterOptionItem setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
