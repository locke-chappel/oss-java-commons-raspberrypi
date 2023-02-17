package io.github.lc.oss.commons.raspberrypi.i2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.pi4j.context.Context;

import io.github.lc.oss.commons.raspberrypi.i2c.HT16K33Device.BlinkRates;
import io.github.lc.oss.commons.raspberrypi.i2c.HT16K33Device.Brightness;

public class HT16K33DeviceTest extends AbstractI2cTest {
    @Test
    public void test_functions() {
        this.expectDevice();

        HT16K33Device dev = new HT16K33Device("id", 70) {
            @Override
            protected Context getContext() {
                return HT16K33DeviceTest.this.context;
            }

            @Override
            public void clear() {
            }
        };

        Assertions.assertTrue(dev.isAvailable());

        dev.blink(BlinkRates.OFF);

        dev.clear();

        dev.off();

        dev.on();

        dev.setBrightness(Brightness.LEVEL_08);

        dev.write(new byte[0]);

        dev.close();
    }
}
