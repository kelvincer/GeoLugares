package org.home.geolugares.direction_model;

/**
 * Created by Kelvin on 10/12/2017.
 */

public class GeoLatLng {
    private double lat;
    private double lng;


    @Override
    public String toString() {
        return String.format("%.8f,%.8f", lat, lng);
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}