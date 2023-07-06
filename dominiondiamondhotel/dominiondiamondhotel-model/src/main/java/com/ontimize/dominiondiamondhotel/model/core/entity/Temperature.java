package com.ontimize.dominiondiamondhotel.model.core.entity;

public class Temperature {
    private MaxOrMinTemperature minimum;
    private MaxOrMinTemperature maximum;

    public Temperature(MaxOrMinTemperature minimum, MaxOrMinTemperature maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public MaxOrMinTemperature getMinimum() {
        return minimum;
    }

    public void setMinimum(MaxOrMinTemperature minimum) {
        this.minimum = minimum;
    }

    public MaxOrMinTemperature getMaximum() {
        return maximum;
    }

    public void setMaximum(MaxOrMinTemperature maximum) {
        this.maximum = maximum;
    }

    private static class MaxOrMinTemperature {
        private int value;
        private String unit;

        public MaxOrMinTemperature(int value, String unit) {
            this.value = value;
            this.unit = unit;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }
}
