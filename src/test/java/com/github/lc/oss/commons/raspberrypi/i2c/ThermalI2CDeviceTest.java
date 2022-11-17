package com.github.lc.oss.commons.raspberrypi.i2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.pi4j.context.Context;

public class ThermalI2CDeviceTest extends AbstractI2cTest {
    @Test
    public void test_read() {
        ThermalI2CDevice dev = new ThermalI2CDevice("id", 10, UnitOfMeature.Kelvin) {
            @Override
            public double getTemperature() {
                return -3.14159;
            }

            @Override
            protected Context getContext() {
                return ThermalI2CDeviceTest.this.context;
            }
        };

        this.expectDevice();

        Assertions.assertEquals(-3.14159, dev.read());

        dev.close();
    }

    @Test
    public void test_getFormattedTemperature_celsius() {
        ThermalI2CDevice dev = new ThermalI2CDevice("id", 10, UnitOfMeature.Celsius) {
            @Override
            public double getTemperature() {
                return 2.718;
            }

            @Override
            protected Context getContext() {
                return ThermalI2CDeviceTest.this.context;
            }
        };

        this.expectDevice();

        Assertions.assertEquals("2.72 C", dev.getFormattedTemperature());
        Assertions.assertEquals(2.718, dev.convert(dev.getTemperature(), UnitOfMeature.Celsius));
        Assertions.assertEquals(36.8924, dev.convert(dev.getTemperature(), UnitOfMeature.Fahrenheit));
        Assertions.assertEquals(275.868, dev.convert(dev.getTemperature(), UnitOfMeature.Kelvin));

        dev.close();
    }

    @Test
    public void test_getFormattedTemperature_fahrenheit() {
        ThermalI2CDevice dev = new ThermalI2CDevice("id", 10, UnitOfMeature.Fahrenheit) {
            @Override
            public double getTemperature() {
                return 2.718;
            }

            @Override
            protected Context getContext() {
                return ThermalI2CDeviceTest.this.context;
            }
        };

        this.expectDevice();

        Assertions.assertEquals("2.72 F", dev.getFormattedTemperature());

        dev.close();
    }

    @Test
    public void test_getFormattedTemperature_kelvin() {
        ThermalI2CDevice dev = new ThermalI2CDevice("id", 10, UnitOfMeature.Kelvin) {
            @Override
            public double getTemperature() {
                return 2.718;
            }

            @Override
            protected Context getContext() {
                return ThermalI2CDeviceTest.this.context;
            }
        };

        this.expectDevice();

        Assertions.assertEquals("2.72 K", dev.getFormattedTemperature());

        dev.close();
    }

    @Test
    public void test_getFormattedTemperature_error() {
        ThermalI2CDevice dev = new ThermalI2CDevice("id", 10, UnitOfMeature.Celsius) {
            @Override
            public double getTemperature() {
                return ThermalI2CDevice.ERORR;
            }

            @Override
            protected Context getContext() {
                return ThermalI2CDeviceTest.this.context;
            }
        };

        this.expectDevice();

        Assertions.assertEquals("Error reading temperature from device.", dev.getFormattedTemperature());

        dev.close();
    }
}
