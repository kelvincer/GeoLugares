package org.home.geolugares.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.home.geolugares.model.Geoname;
import org.home.geolugares.model.Status;

import java.util.List;

/**
 * Created by Kelvin on 15/11/2017.
 */

public class LugaresCercanosResponse {

    @SerializedName("geonames")
    @Expose
    private List<Geoname> geonames;
    @SerializedName("status")
    @Expose
    private Status status;

    public List<Geoname> getGeonames() {
        return geonames;
    }

    public void setGeonames(List<Geoname> geonames) {
        this.geonames = geonames;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
