package com.ebizon.fluid.model;

/**
 * Created by piyush-ios on 19/5/16.
 */
public class ReviewVote {

    String name;
    String percent;
    String value;

    public ReviewVote(String name, String percent, String value) {;
        this.name = name;
        this.percent = percent;
        this.value = value;
    }



    public String getName() {
        return name;
    }

    public String getPercent() {
        return percent;
    }

    public int getValue() {
        Float val = Float.parseFloat(this.value);
        return Math.round(val);
    }
}
