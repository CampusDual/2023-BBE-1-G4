package com.ontimize.dominiondiamondhotel.model.core.entity;

public class Forecast {
    private Headline Headline;
    private Day[] DailyForecasts;

    public Forecast(Headline headline, Day[] dailyForecasts) {
        this.Headline = headline;
        this.DailyForecasts = dailyForecasts;
    }

    public Headline getHeadline() {
        return Headline;
    }

    public void setHeadline(Headline headline) {
        this.Headline = headline;
    }

    public Day[] getDays() {
        return DailyForecasts;
    }

    public void setDays(Day[] days) {
        this.DailyForecasts = days;
    }

}
