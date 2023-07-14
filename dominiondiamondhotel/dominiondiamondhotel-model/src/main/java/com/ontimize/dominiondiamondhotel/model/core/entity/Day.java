package com.ontimize.dominiondiamondhotel.model.core.entity;

public class Day {
    private String date;
    private Temperature temperature;
    private DayAndNight dayTime;
    private DayAndNight night;

    public Day(String date, Temperature temperature, DayAndNight dayTime, DayAndNight night) {
        this.date = date;
        this.temperature = temperature;
        this.dayTime = dayTime;
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

    public DayAndNight getDayTime() {
        return dayTime;
    }

    public void setDayTime(DayAndNight dayTime) {
        this.dayTime = dayTime;
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

        public DayAndNight(String iconPhrase, boolean hasPrecipitation) {
            this.iconPhrase = iconPhrase;
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
