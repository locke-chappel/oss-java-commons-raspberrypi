package com.github.lc.oss.commons.raspberrypi.i2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2CRegister;

public class SSD1306DeviceTest extends AbstractI2cTest {
    @SuppressWarnings("resource")
    @Test
    public void test_constructor_exceptions_dimensions() {
        try {
            new SSD1306Device("id", 1, 1);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Invalid display width, only 96 and 128 pixels wide are supported", ex.getMessage());
        }

        try {
            new SSD1306Device("id", 128, 1);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Invalid display height, only 128x64 and 128x32 are supported", ex.getMessage());
        }

        try {
            new SSD1306Device("id", 96, 1);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Invalid display height, only 96x16 is supported", ex.getMessage());
        }

        try {
            new SSD1306Device("id", 64, 1);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Invalid display height, only 64x48 and 64x32 are supported", ex.getMessage());
        }

        /* Valid dimensions - code coverage, should only fail on VCC */
        try {
            new SSD1306Device("id", 0x00, 128, 64, -1010);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Unsupported VCC Mode", ex.getMessage());
        }

        try {
            new SSD1306Device("id", 0x00, 128, 32, -1010);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Unsupported VCC Mode", ex.getMessage());
        }

        try {
            new SSD1306Device("id", 0x00, 96, 16, -1010);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Unsupported VCC Mode", ex.getMessage());
        }

        try {
            new SSD1306Device("id", 0x00, 64, 48, -1010);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Unsupported VCC Mode", ex.getMessage());
        }

        try {
            new SSD1306Device("id", 0x00, 64, 32, -1010);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Unsupported VCC Mode", ex.getMessage());
        }
    }

    @Test
    public void test_constructor_externalVcc() {
        this.expectDevice();
        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(SSD1306Device.COM_REG_ADDR)).thenReturn(reg1);

        SSD1306Device dev = new SSD1306Device("id", SSD1306Device.DEFAULT_I2C_ADDRESS, 128, 64, SSD1306Device.EXTERNAL_VCC) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        Assertions.assertEquals(1024, dev.getBufferSize());

        dev.close();
    }

    @Test
    public void test_constructor_96x16() {
        this.expectDevice();
        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(SSD1306Device.COM_REG_ADDR)).thenReturn(reg1);

        SSD1306Device dev = new SSD1306Device("id", 96, 16) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        Assertions.assertEquals(192, dev.getBufferSize());

        dev.close();
    }

    @Test
    public void test_constructor_defaults() {
        this.expectDevice();
        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(SSD1306Device.COM_REG_ADDR)).thenReturn(reg1);

        SSD1306Device dev = new SSD1306Device("id", 128, 32) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        Assertions.assertEquals(512, dev.getBufferSize());

        dev.close();
    }

    @Test
    public void test_on_off() {
        this.expectDevice();
        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(SSD1306Device.COM_REG_ADDR)).thenReturn(reg1);

        SSD1306Device dev = new SSD1306Device("id", 128, 32) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        dev.on();
        dev.off();

        dev.close();
    }

    @Test
    public void test_setInvert() {
        this.expectDevice();
        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(SSD1306Device.COM_REG_ADDR)).thenReturn(reg1);

        SSD1306Device dev = new SSD1306Device("id", 128, 32) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        dev.setInvert(true);
        dev.setInvert(false);

        dev.close();
    }

    @Test
    public void test_setContrast() {
        this.expectDevice();
        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(SSD1306Device.COM_REG_ADDR)).thenReturn(reg1);

        SSD1306Device dev = new SSD1306Device("id", 128, 32) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        dev.setContrast(0);
        dev.setContrast(255);

        try {
            dev.setContrast(-1);
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Constract must be between 0 and 255 inclusive", ex.getMessage());
        }

        try {
            dev.setContrast(256);
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Constract must be between 0 and 255 inclusive", ex.getMessage());
        }

        dev.close();
    }

    @Test
    public void test_fill_clear() {
        this.expectDevice();
        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(SSD1306Device.COM_REG_ADDR)).thenReturn(reg1);

        I2CRegister reg2 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(SSD1306Device.SET_DISP_START_LINE)).thenReturn(reg2);

        SSD1306Device dev = new SSD1306Device("id", 128, 32) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        dev.fill();
        dev.clear();
        dev.fill();
        dev.clear();

        dev.close();
    }

    @Test
    public void test_display_error() {
        this.expectDevice();
        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(SSD1306Device.COM_REG_ADDR)).thenReturn(reg1);

        SSD1306Device dev = new SSD1306Device("id", 128, 32) {
            @Override
            protected Context getContext() {
                return SSD1306DeviceTest.this.context;
            }
        };

        try {
            dev.display(new byte[1]);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Data length must match display buffer size of 512 bytes", ex.getMessage());
        }

        dev.close();
    }

    @Test
    public void test_setPixel_errors() {
        try {
            SSD1306Device.setPixel(null, 0, 0, true);
            Assertions.fail("Expected exception");
        } catch (NullPointerException ex) {
            // pass
        }

        try {
            SSD1306Device.setPixel(new byte[0], 0, 0, true);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Unsupported screen buffer size", ex.getMessage());
        }

        try {
            SSD1306Device.setPixel(new byte[1], 0, 0, true);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Unsupported screen buffer size", ex.getMessage());
        }

        try {
            SSD1306Device.setPixel(new byte[511], 0, 0, true);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Unsupported screen buffer size", ex.getMessage());
        }

        try {
            SSD1306Device.setPixel(new byte[4096], 0, 0, true);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Unsupported screen buffer size", ex.getMessage());
        }

        try {
            SSD1306Device.setPixel(new byte[512], -1, -1, true);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Coordinates are outside display dimensions (128x32)", ex.getMessage());
        }

        try {
            SSD1306Device.setPixel(new byte[512], 0, -1, true);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Coordinates are outside display dimensions (128x32)", ex.getMessage());
        }

        try {
            // Zero-indexed, 128x32
            SSD1306Device.setPixel(new byte[512], 128, 32, true);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Coordinates are outside display dimensions (128x32)", ex.getMessage());
        }

        try {
            // Zero-indexed, 128x64
            SSD1306Device.setPixel(new byte[1024], 128, 64, true);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Coordinates are outside display dimensions (128x64)", ex.getMessage());
        }

        try {
            // Valid x, invalid y
            SSD1306Device.setPixel(new byte[192], 32, 17, true);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Coordinates are outside display dimensions (96x16)", ex.getMessage());
        }

        try {
            // Invalid x, valid y
            SSD1306Device.setPixel(new byte[256], 64, 16, true);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Coordinates are outside display dimensions (64x32)", ex.getMessage());
        }
    }

    @Test
    public void test_setPixel() {
        byte[] pixels = new byte[1024];
        Assertions.assertEquals(0x00, pixels[0]);
        SSD1306Device.setPixel(pixels, 0, 0, true);
        Assertions.assertEquals(0x01, pixels[0]);
        SSD1306Device.setPixel(pixels, 0, 0, false);
        Assertions.assertEquals(0x00, pixels[0]);

        pixels = new byte[512];
        Assertions.assertEquals(0x00, pixels[0]);
        SSD1306Device.setPixel(pixels, 0, 0, true);
        Assertions.assertEquals(0x01, pixels[0]);
        SSD1306Device.setPixel(pixels, 0, 0, false);
        Assertions.assertEquals(0x00, pixels[0]);

        pixels = new byte[384];
        Assertions.assertEquals(0x00, pixels[0]);
        SSD1306Device.setPixel(pixels, 0, 0, true);
        Assertions.assertEquals(0x01, pixels[0]);
        SSD1306Device.setPixel(pixels, 0, 0, false);
        Assertions.assertEquals(0x00, pixels[0]);

        pixels = new byte[256];
        Assertions.assertEquals(0x00, pixels[0]);
        SSD1306Device.setPixel(pixels, 0, 0, true);
        Assertions.assertEquals(0x01, pixels[0]);
        SSD1306Device.setPixel(pixels, 0, 0, false);
        Assertions.assertEquals(0x00, pixels[0]);

        pixels = new byte[192];
        Assertions.assertEquals(0x00, pixels[0]);
        SSD1306Device.setPixel(pixels, 0, 0, true);
        Assertions.assertEquals(0x01, pixels[0]);
        SSD1306Device.setPixel(pixels, 0, 0, false);
        Assertions.assertEquals(0x00, pixels[0]);
    }
}
