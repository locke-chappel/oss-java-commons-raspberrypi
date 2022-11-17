package com.github.lc.oss.commons.raspberrypi.i2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2CRegister;

public class SSD1306DeviceTest extends AbstractI2cTest {
    @Test
    public void test_constructor() {
        this.expectDevice();

        SSD1306Device device = new SSD1306Device("id", 1, 128, 32) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        Assertions.assertEquals("id", device.getID());
        Assertions.assertEquals(1, device.getAddress());
        Assertions.assertEquals(128, device.getWidth());
        Assertions.assertEquals(32, device.getHeight());

        device.close();
    }

    @Test
    public void test_begin_128x64() {
        this.expectDevice();

        I2CRegister register = Mockito.mock(I2CRegister.class);

        Mockito.when(this.i2c.register(ArgumentMatchers.anyInt())).thenReturn(register);

        SSD1306Device device = new SSD1306Device("id", 1, 128, 64) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        device.begin();

        device.close();
    }

    @Test
    public void test_begin_128x32() {
        this.expectDevice();

        I2CRegister register = Mockito.mock(I2CRegister.class);

        Mockito.when(this.i2c.register(ArgumentMatchers.anyInt())).thenReturn(register);

        SSD1306Device device = new SSD1306Device("id", 1, 128, 32) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        device.begin();

        device.close();
    }

    @Test
    public void test_begin_96x16() {
        this.expectDevice();

        I2CRegister register = Mockito.mock(I2CRegister.class);

        Mockito.when(this.i2c.register(ArgumentMatchers.anyInt())).thenReturn(register);

        SSD1306Device device = new SSD1306Device("id", 1, 96, 16) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        device.begin(0x1);

        device.close();
    }

    @Test
    public void test_begin_unsupportedSize_height() {
        this.expectDevice();

        SSD1306Device device = new SSD1306Device("id", 1, 128, 8) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        try {
            device.begin();
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Unsupported display dimensions", ex.getMessage());
        }

        device.close();
    }

    @Test
    public void test_begin_unsupportedSize_width() {
        this.expectDevice();

        SSD1306Device device = new SSD1306Device("id", 1, 1, 64) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        try {
            device.begin();
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Unsupported display dimensions", ex.getMessage());
        }

        device.close();
    }

    @Test
    public void test_begin_unsupportedSize() {
        this.expectDevice();

        SSD1306Device device = new SSD1306Device("id", 1, 96, 96) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        try {
            device.begin();
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Unsupported display dimensions", ex.getMessage());
        }

        device.close();
    }
}
