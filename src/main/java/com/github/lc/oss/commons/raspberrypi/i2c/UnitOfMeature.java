package com.github.lc.oss.commons.raspberrypi.i2c;

public enum UnitOfMeature {
    Fahrenheit("F"),
    Celsius("C"),
    Kelvin("K");

    private final String unit;

    public String getUnit() {
        return this.unit;
    }

    private UnitOfMeature(String unit) {
        this.unit = unit;
    }
}
