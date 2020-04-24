package edu.monash.kmhc.observerPattern;

/**
 * Based on the Observer pattern learnt in FIT3077 - lectures.
 */
public interface Observer {

    /**
     * Classes that implement this class must provide their own functionality and update accordingly
     * after getting notified by the Subject.
     */
    void update();
}
