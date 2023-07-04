package com.ontimize.dominiondiamondhotel.model.core.entity;

import java.util.Date;

public class Day {
    private String Date;
    private Temperature Temperature;
    private DayAndNight Day;
    private DayAndNight Night;

    public Day(String date, Temperature temperature, DayAndNight day, DayAndNight night) {
        this.Date = date;
        this.Temperature = temperature;
        this.Day = day;
        this.Night = night;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public Temperature getTemperature() {
        return Temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.Temperature = temperature;
    }

    public DayAndNight getDay() {
        return Day;
    }

    public void setDay(DayAndNight day) {
        this.Day = day;
    }

    public DayAndNight getNight() {
        return Night;
    }

    public void setNight(DayAndNight night) {
        this.Night = night;
    }

    private static class DayAndNight {
        private String IconPhrase;
        private boolean HasPrecipitation;

        public DayAndNight(String IconPhrase, boolean hasPrecipitation) {
            this.IconPhrase = IconPhrase;
            this.HasPrecipitation = hasPrecipitation;
        }

        public String getIconPhrase() {
            return IconPhrase;
        }

        public void setIconPhrase(String iconPhrase) {
            this.IconPhrase = iconPhrase;
        }

        public boolean isHasPrecipitation() {
            return HasPrecipitation;
        }

        public void setHasPrecipitation(boolean hasPrecipitation) {
            this.HasPrecipitation = hasPrecipitation;
        }
    }
}
