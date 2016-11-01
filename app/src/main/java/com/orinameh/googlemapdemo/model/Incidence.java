package com.orinameh.googlemapdemo.model;

/**
 * Created by davidevhade on 9/26/16.
 */

public class Incidence {

    private int image;
    private String desc;

    public Incidence(){

    }

    public Incidence(int image, String desc) {
        this.image = image;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
