package com.ebizon.fluid.model;

/**
 * Created by manish on 20/01/16.
 */
public class Entity {
    private final String id;

    Entity(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }
}
