package ru.android.german.lesson5;

import android.location.Location;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by german on 20.10.14.
 */
public class Feed {
//    private Date date;
    private String link;

    private String details;
//    private Location location;
//    private double magnitude;

//    public Date getDate() { return date; }
    public String getLink() { return link; }
//
//    public String getDetails() { return details; }
//    public Location getLocation() { return location; }
//    public double getMagnitude() { return magnitude; }

    public Feed(/*Date date, */String details/*, Location location*/, /*double magnitude, */String link) {
//        this.date = date;
        this.details = details;
//        this.location = location;
//        this.magnitude = magnitude;
        this.link = link;
    }

    @Override
    public String toString() {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm");
//        String dateString = sdf.format(date);
//        return dateString + ": " + magnitude + " " + details;
        return details;
    }

}
