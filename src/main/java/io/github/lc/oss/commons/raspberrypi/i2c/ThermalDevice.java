package io.github.lc.oss.commons.raspberrypi.i2c;

public interface ThermalDevice extends InputDevice {
    UnitOfMeature getUnitOfMeasure();

    double getTemperature();

    String getFormattedTemperature();
}
