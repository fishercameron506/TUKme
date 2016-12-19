package com.example.cameron.tukme.Directions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cameron on 8/13/2016.
 * interface
 * implemented in the concrete maps
 * class
 */
public interface DirectionSeekerListener
{
    /**
     * starts up the directions
     */
    void directionStart();

    /**
     *
     * @param routes Gets the routes to map out
     */
    void directionSuccess(List<Route> routes);
}
