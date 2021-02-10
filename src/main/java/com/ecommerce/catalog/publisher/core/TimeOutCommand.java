package com.ecommerce.catalog.publisher.core;

import java.util.concurrent.TimeUnit;

public class TimeOutCommand {


    // --------------------------------------
    // -        Attributes                  -
    // --------------------------------------

    private Integer unitTime;
    private TimeUnit timeConvert;

    // --------------------------------------
    // -        Constructors                -
    // --------------------------------------

    public TimeOutCommand(Integer unitTime, TimeUnit timeConvert) {
        this.unitTime = unitTime;
        this.timeConvert = timeConvert;
    }

    // --------------------------------------
    // -        Methods                     -
    // --------------------------------------

    public Integer getUnitTime() {
        return unitTime;
    }

    public TimeUnit getTimeConvert() {
        return timeConvert;
    }
}
