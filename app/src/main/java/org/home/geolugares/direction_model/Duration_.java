package org.home.geolugares.direction_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kelvin on 10/12/2017.
 */

public class Duration_ {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("value")
    @Expose
    private Integer value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

}