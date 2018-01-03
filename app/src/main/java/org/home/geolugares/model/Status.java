package org.home.geolugares.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kelvin on 17/11/2017.
 */

public class Status {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("value")
    @Expose
    private Integer value;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
