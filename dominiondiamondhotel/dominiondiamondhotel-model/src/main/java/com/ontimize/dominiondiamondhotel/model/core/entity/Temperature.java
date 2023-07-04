package com.ontimize.dominiondiamondhotel.model.core.entity;

public class Temperature {
    private MaxOrMinTemperature Minimum;
    private MaxOrMinTemperature Maximum;

    public Temperature(MaxOrMinTemperature minimum, MaxOrMinTemperature maximum) {
        this.Minimum = minimum;
        this.Maximum = maximum;
    }

    public MaxOrMinTemperature getMinimum() {
        return Minimum;
    }

    public void setMinimum(MaxOrMinTemperature minimum) {
        this.Minimum = minimum;
    }

    public MaxOrMinTemperature getMaximum() {
        return Maximum;
    }

    public void setMaximum(MaxOrMinTemperature maximum) {
        this.Maximum = maximum;
    }

    private static class MaxOrMinTemperature {
        private int Value;
        private String Unit;

        public MaxOrMinTemperature(int value, String unit) {
            this.Value = value;
            this.Unit = unit;
        }

        public int getValue() {
            return Value;
        }

        public void setValue(int value) {
            this.Value = value;
        }

        public String getUnit() {
            return Unit;
        }

        public void setUnit(String unit) {
            this.Unit = unit;
        }
    }
}
