package com.example.cameron.tukme.Directions;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cameron on 8/13/2016.
 * Stores all the route information
 */
public class Route {


    /**
     * initialize the variables
     * Instantiate the Route object
     */
    public Route()
    {
        this.distance = null;
        this.duration = null;
        this.jobAddress = "";
        this.jobLocation = null;
        this.myAddress = "";
        this.myLocation = null;
        this.points = null;
    }

    /**
     *
     * @return Gets the distance
     */
    public Distance getDistance() {
        return distance;
    }

    /**
     *
     * @param distance Sets the distance
     */
    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    /**
     *
     * @return Get the duration
     */
    public Duration getDuration() {
        return duration;
    }

    /**
     *
     * @param duration Sets the duration object
     */
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    /**
     *
     * @return gets the job Address
     */
    public String getJobAddress() {
        return jobAddress;
    }

    /**
     *
     * @param jobAddress sets the job address
     */
    public void setJobAddress(String jobAddress) {
        this.jobAddress = jobAddress;
    }

    /**
     *
     * @return gets the job location
     */
    public LatLng getJobLocation() {
        return jobLocation;
    }

    /**
     *
     * @param jobLocation Sets the job location
     */
    public void setJobLocation(LatLng jobLocation) {
        this.jobLocation = jobLocation;
    }

    /**
     *
     * @return get users address
     */
    public String getMyAddress() {
        return myAddress;
    }

    /**
     *
     * @param myAddress Sets the users address
     */
    public void setMyAddress(String myAddress) {
        this.myAddress = myAddress;
    }

    /**
     *
     * @return Gets the users location
     */
    public LatLng getMyLocation() {
        return myLocation;
    }

    /**
     *
     * @param myLocation Sets the users location
     */
    public void setMyLocation(LatLng myLocation) {
        this.myLocation = myLocation;
    }

    /**
     *
     * @return gets the long and lat points
     */
    public List<LatLng> getPoints() {
        return points;
    }

    /**
     *
     * @param points Sets the long and lat points
     */
    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    //private variables
    private Distance distance;
    private Duration duration;
    private String jobAddress;
    private LatLng jobLocation;
    private String myAddress;
    private LatLng myLocation;

    private List<LatLng> points;
}
