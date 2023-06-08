package com.ontimize.dominiondiamondhotel.model.core.utils;

public class BookingUtils {
    private BookingUtils() {

    }

    public static boolean calificationCheck(int calification) {
        return calification > 0 && calification <= 10;
    }
}
