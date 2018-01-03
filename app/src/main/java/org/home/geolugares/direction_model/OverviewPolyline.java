package org.home.geolugares.direction_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Kelvin on 10/12/2017.
 */

public class OverviewPolyline {

    @SerializedName("points")
    @Expose
    private String points;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

}