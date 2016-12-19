package com.example.cameron.tukme.Directions;

/**
 * Created by Cameron on 8/13/2016.
 * Sets the duration of the trip
 */
public class Duration
{
    /**
     *
     * @param text Sets the text
     * @param val Sets the value
     *            instantiate the Duration object
     *            initialize variables
     *
     */
    public Duration(String text, int val)
    {
        this.text = text;
        this.val = val;
    }

    /**
     *
     * @return Gets the Text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text Sets the Text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return Gets the value
     */
    public int getVal() {
        return val;
    }

    /**
     *
     * @param val Sets the value
     */
    public void setVal(int val) {
        this.val = val;
    }

    //private variables
    private String text;
    private int val;
}
