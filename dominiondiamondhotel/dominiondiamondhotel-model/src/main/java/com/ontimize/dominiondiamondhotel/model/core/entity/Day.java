package com.ontimize.dominiondiamondhotel.model.core.entity;

public class Day {
    private String date;
    private Temperature temperature;
    private DayAndNight day;
    private DayAndNight night;

    public Day(String date, Temperature temperature, DayAndNight day, DayAndNight night) {
        this.date = date;
        this.temperature = temperature;
        this.day = day;
        this.night = night;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public void setTemperature(Temperature temperature) {
        this.temperature = temperature;
    }

    public DayAndNight getDay() {
        return day;
    }

    public void setDay(DayAndNight day) {
        this.day = day;
    }

    public DayAndNight getNight() {
        return night;
    }

    public void setNight(DayAndNight night) {
        this.night = night;
    }

    private static class DayAndNight {
        private String iconPhrase;
        private boolean hasPrecipitation;

        public DayAndNight(String IconPhrase, boolean hasPrecipitation) {
            this.iconPhrase = IconPhrase;
            this.hasPrecipitation = hasPrecipitation;
        }

        public String getIconPhrase() {
            return iconPhrase;
        }

        public void setIconPhrase(String iconPhrase) {
            this.iconPhrase = iconPhrase;
        }

        public boolean isHasPrecipitation() {
            return hasPrecipitation;
        }

        public void setHasPrecipitation(boolean hasPrecipitation) {
            this.hasPrecipitation = hasPrecipitation;
        }
    }
}
