package com.ecommerce.catalog.observer.core;

import lombok.Getter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class Observable {

    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    @Getter
    private final PropertyChangeSupport observers;

    // --------------------------------------
    // -        Constructors                -
    // --------------------------------------

    public Observable(){
        this.observers = new PropertyChangeSupport(this);
    }

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        this.observers.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        this.observers.removePropertyChangeListener(pcl);
    }



}
