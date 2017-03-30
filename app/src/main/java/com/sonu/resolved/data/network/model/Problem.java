package com.sonu.resolved.data.network.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;
import com.sonu.resolved.ui.problem.adapter.TypeFactory;
import com.sonu.resolved.ui.problem.adapter.Visitable;

/**
 * Created by sonu on 7/3/17.
 */

public class Problem implements ClusterItem, Visitable {
    private double latitude, longitude;
    private String title;
    private String description;
    private int pid;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getSnippet() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "pid=" + pid +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public int type(TypeFactory typeFactory) {
        return 0;
    }
}
