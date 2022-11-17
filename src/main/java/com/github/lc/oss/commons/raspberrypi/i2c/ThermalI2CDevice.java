package com.github.lc.oss.commons.raspberrypi.i2c;

public abstract class ThermalI2CDevice extends I2CDevice implements ThermalDevice {
    public static final double ERORR = Double.MIN_NORMAL;

    private final UnitOfMeature uom;

    @Override
    public UnitOfMeature getUnitOfMeasure() {
        return this.uom;
    }

    public ThermalI2CDevice(String id, int address, UnitOfMeature uom) {
        super(id, address);
        this.uom = uom;
    }

    protected double convert(double celcius, UnitOfMeature to) {
        switch (to) {
            case Fahrenheit:
                return celcius * 1.8 + 32;
            case Kelvin:
                return celcius + 273.15;
            case Celsius:
            default:
                return celcius;
        }
    }

    @Override
    public Object read() {
        return this.getTemperature();
    }

    @Override
    public abstract double getTemperature();

    @Override
    public String getFormattedTemperature() {
        double temp = this.getTemperature();
        if (temp == ThermalI2CDevice.ERORR) {
            return "Error reading temperature from device.";
        }
        return String.format("%.2f %s", temp, this.getUnitOfMeasure().getUnit());
    }
}
