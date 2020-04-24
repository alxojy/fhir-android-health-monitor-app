package edu.monash.kmhc.observerPattern;

import java.util.HashSet;

/**
 * Based on the Observer pattern learnt in FIT3077 - lectures.
 */
public abstract class Subject {

    private HashSet<Observer> Observers = new HashSet<Observer>();

    /**
     * Add Observer to get noitified.
     * @param o Observer
     */
    public void attach(Observer o) {
        Observers.add(o);
    }

    /**
     * Detach Observer to stop notifying Observer.
     * @param o Observer
     */
    public void detach(Observer o) {
        Observers.remove(o);
    }

    /**
     * Notifies Observers when there's change in data.
     */
    public void notifyObservers() {
        for (Observer o : Observers) {
            o.update();
        }
    }

}
