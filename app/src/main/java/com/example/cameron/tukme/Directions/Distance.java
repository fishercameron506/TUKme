package com.example.cameron.tukme.Directions;

/**
 * Created by Cameron on 8/13/2016.
 * Stores the distance data
 */
public class Distance {

    /**
     *
     * @param text Sets the text
     * @param val Sets the val
     *            Instantiate distance object
     *            Initialize variables
     */
    public Distance(String text, int val)
    {
        this.text = text;
        this.val = val;
    }

    /**
     *
     * @return Gets the text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text Sets the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return get the Value
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
