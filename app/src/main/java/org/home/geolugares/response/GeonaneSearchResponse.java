package org.home.geolugares.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.home.geolugares.model.Geoname;

import java.util.List;

/**
 * Created by Kelvin on 15/11/2017.
 */

public class GeonaneSearchResponse {

    @SerializedName("totalResultsCount")
    @Expose
    private Integer totalResultsCount;
    @SerializedName("geonames")
    @Expose
    private List<Geoname> geonames;

    public Integer getTotalResultsCount() {
        return totalResultsCount;
    }

    public void setTotalResultsCount(Integer totalResultsCount) {
        this.totalResultsCount = totalResultsCount;
    }

    public List<Geoname> getGeonames() {
        return geonames;
    }

    public void setGeonames(List<Geoname> geonames) {
        this.geonames = geonames;
    }
}
