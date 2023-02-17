package io.github.lc.oss.commons.raspberrypi.i2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;

import io.github.lc.oss.commons.raspberrypi.util.Pi4JContext;

public class I2CDeviceTest extends AbstractI2cTest {

    @Test
    public void test_getDevice() {
        I2CDevice dev = new I2CDevice("2", 3) {
            @Override
            protected Context getContext() {
                return I2CDeviceTest.this.context;
            }
        };

        Assertions.assertEquals("2", dev.getID());
        Assertions.assertEquals(3, dev.getAddress());
        Assertions.assertFalse(dev.isAvailable());

        this.expectDevice();

        I2C device = dev.getDevice();
        Assertions.assertNotNull(device);
        I2C device2 = dev.getDevice();
        Assertions.assertSame(device, device2);

        Assertions.assertFalse(dev.isAvailable());
        dev.setAvailable(true);
        Assertions.assertTrue(dev.isAvailable());

        dev.close();
    }

    @Test
    public void test_getContext() {
        @SuppressWarnings("resource")
        I2CDevice dev = new I2CDevice("1", 1);

        Context expected = Pi4JContext.getContext();
        Assertions.assertSame(expected, dev.getContext());
    }
}
