package com.ontimize.dominiondiamondhotel.model.core.entity;

public class Forecast {
    private Headline headline;
    private Day[] dailyForecasts;

    public Forecast(Headline headline, Day[] dailyForecasts) {
        this.headline = headline;
        this.dailyForecasts = dailyForecasts;
    }

    public Headline getHeadline() {
        return headline;
    }

    public void setHeadline(Headline headline) {
        this.headline = headline;
    }

    public Day[] getDays() {
        return dailyForecasts;
    }

    public void setDays(Day[] days) {
        this.dailyForecasts = days;
    }

}
