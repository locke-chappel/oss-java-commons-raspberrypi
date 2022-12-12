package com.github.lc.oss.commons.raspberrypi.i2c;

import com.pi4j.io.i2c.I2CRegister;

/**
 * Java (Pi4J) implementation of SSD1306 I2C device.
 *
 * Largely an interpretation/translation of
 * https://github.com/adafruit/Adafruit_CircuitPython_SSD1306/blob/main/adafruit_ssd1306.py
 *
 * Tested with a 128x32 device, others probably work but may have bugs.
 */
public class SSD1306Device extends I2CDevice implements SSD1306Display {
    /**
     * Utility function to set a specific pixel in a buffer to on/off.
     *
     * Coordinates are zero-indexed
     */
    public static void setPixel(byte[] pixels, int x, int y, boolean on) {
        /*
         * Adapted from https://github.com/ondryaso/pi-ssd1306-java/blob/
         * 17fc197962992f812ca30fd9ce748f9d0f65fc0e/src/eu/ondryaso/ssd1306/Display.java
         * #L390
         */

        int width;
        int height;
        switch (pixels.length) {
            case 1024:
                width = 128;
                height = 64;
                break;
            case 512:
                width = 128;
                height = 32;
                break;
            case 384:
                width = 64;
                height = 48;
                break;
            case 256:
                width = 64;
                height = 32;
                break;
            case 192:
                width = 96;
                height = 16;
                break;
            default:
                throw new IllegalArgumentException("Unsupported screen buffer size");
        }

        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException(String.format("Coordinates are outside display dimensions (%dx%d)", width, height));
        }

        if (on) {
            pixels[x + y / 8 * width] |= 1 << (y & 7);
        } else {
            pixels[x + y / 8 * width] &= ~(1 << (y & 7));
        }
    }

    /**
     * 11111111
     */
    public static final byte ALL_ONES = (byte) 255; // bytes are signed in Java, so all 1's is the value -1
    /**
     * 00000000
     */
    public static final byte ALL_ZEROS = 0;
    /**
     * Other (non 3.3v) VCC
     */
    public static final int EXTERNAL_VCC = 0x01;
    /**
     * 3.3v VCC
     */
    public static final int SWITCHC_AP_VCC = 0x02;
    /**
     * Default SSD1306 I2C Address (0x3C)
     */
    protected static final int DEFAULT_I2C_ADDRESS = 0x3C;

    protected static final int COM_REG_ADDR = 0x00;
    protected static final int SET_CONTRAST = 0x81;
    protected static final int COLUMN_ADDR = 0x21;
    protected static final int PAGE_ADDR = 0x22;
    protected static final int SET_DISP_START_LINE = 0x40;
    protected static final int DISPLAY_OFF = 0xAE;
    protected static final int DISPLAY_ON = SSD1306Device.DISPLAY_OFF | 0x01;
    protected static final int SET_DISPLAY_CLOCK_DIV = 0xD5;
    protected static final int SET_MUX_RATIO = 0xA8;
    protected static final int SET_DISP_OFFSET = 0xD3;
    protected static final int SET_CHARGE_PUMP = 0x8D;
    protected static final int SET_MEM_ADDR = 0x20;
    protected static final int SET_SEG_REMAP = 0xA0;
    protected static final int COMSCANDEC = 0xC8;
    protected static final int SET_COM_PIN_CFG = 0xDA;
    protected static final int SET_COM_OUT_DIR = 0xC0;
    protected static final int SET_PRECHARGE = 0xD9;
    protected static final int SET_VCOM_DESEL = 0xDB;
    protected static final int SET_ENTIRE_ON = 0xA4;
    protected static final int SET_NORM_INV = 0xA6;
    protected static final int SET_IREF_SELECT = 0xAD;

    protected static final int MAX_I2C_DATA_CHUNK = 32; // bytes

    private final int width;
    private final int height;
    private final int pages;
    private final int bufferSize;
    private final int chunksPerFrame;
    private final int vccMode;

    private byte[] allOff;
    private byte[] allOn;

    /**
     * Assumes 3.3v VCC and default address of 0x3C
     */
    public SSD1306Device(String id, int width, int height) {
        this(id, SSD1306Device.DEFAULT_I2C_ADDRESS, width, height);
    }

    /**
     * Assumes 3.3v VCC
     */
    public SSD1306Device(String id, int address, int width, int height) {
        this(id, address, width, height, SSD1306Device.SWITCHC_AP_VCC);
    }

    public SSD1306Device(String id, int address, int width, int height, int vccMode) {
        super(id, address);

        if (width == 128) {
            if (height != 64 && height != 32) {
                throw new IllegalArgumentException("Invalid display height, only 128x64 and 128x32 are supported");
            }
        } else if (width == 96) {
            if (height != 16) {
                throw new IllegalArgumentException("Invalid display height, only 96x16 is supported");
            }
        } else if (width == 64) {
            if (height != 48 && height != 32) {
                throw new IllegalArgumentException("Invalid display height, only 64x48 and 64x32 are supported");
            }
        } else {
            throw new IllegalArgumentException("Invalid display width, only 96 and 128 pixels wide are supported");
        }

        if (vccMode != SSD1306Device.SWITCHC_AP_VCC && vccMode != SSD1306Device.EXTERNAL_VCC) {
            throw new IllegalArgumentException("Unsupported VCC Mode");
        }

        this.width = width;
        this.height = height;
        this.pages = this.height / 8;
        this.bufferSize = this.width * this.pages;
        this.chunksPerFrame = this.bufferSize / SSD1306Device.MAX_I2C_DATA_CHUNK;
        this.vccMode = vccMode;

        this.init();
    }

    protected byte[] allOff() {
        if (this.allOff == null) {
            byte[] data = new byte[this.getBufferSize()];

            for (int i = 0; i < this.bufferSize; i++) {
                data[i] = SSD1306Device.ALL_ZEROS;
            }

            this.allOff = data;
        }
        return this.allOff;
    }

    protected byte[] allOn() {
        if (this.allOn == null) {
            byte[] data = new byte[this.getBufferSize()];

            for (int i = 0; i < this.bufferSize; i++) {
                data[i] = SSD1306Device.ALL_ONES;
            }

            this.allOn = data;
        }
        return this.allOn;
    }

    protected void init() {
        this.command(SSD1306Device.DISPLAY_OFF);
        this.command(SSD1306Device.SET_MEM_ADDR);
        this.command(0x10); // 0x10 Page Addressing Mode, 0x00 Horizontal Addressing Mode
        this.command(SSD1306Device.SET_DISP_START_LINE);
        this.command(SSD1306Device.SET_SEG_REMAP | 0x1);
        this.command(SSD1306Device.SET_MUX_RATIO);
        this.command(this.height - 1);
        this.command(SSD1306Device.SET_COM_OUT_DIR | 0x08);
        this.command(SSD1306Device.SET_DISP_OFFSET);
        this.command(0x0);
        this.command(SSD1306Device.SET_COM_PIN_CFG);
        this.command(this.width > 2 * this.height ? 0x02 : 0x12);
        this.command(SSD1306Device.SET_DISPLAY_CLOCK_DIV);
        this.command(this.width == 96 ? 0x60 : 0x80); // 0x60 for 96x16, else 0x80
        this.command(SSD1306Device.SET_PRECHARGE);
        this.command(this.vccMode == SSD1306Device.EXTERNAL_VCC ? 0x22 : 0xF1);
        this.command(SSD1306Device.SET_VCOM_DESEL);
        this.command(0x30); // 0.83*Vcc # n.b. specs for ssd1306 64x32 oled screens imply this should be
                            // 0x40
        this.command(SSD1306Device.SET_CONTRAST);
        this.command(0xFF);
        this.command(SSD1306Device.SET_ENTIRE_ON); // output follows RAM contents
        this.command(SSD1306Device.SET_NORM_INV); // not inverted
        this.command(SSD1306Device.SET_IREF_SELECT);
        this.command(0x30); // enable internal IREF during display on
        this.command(SSD1306Device.SET_CHARGE_PUMP);
        this.command(this.vccMode == SSD1306Device.EXTERNAL_VCC ? 0x10 : 0x14);
        this.command(SSD1306Device.DISPLAY_ON);
    }

    @Override
    public void display(byte[] data) {
        if (data.length != this.getBufferSize()) {
            throw new IllegalArgumentException(String.format("Data length must match display buffer size of %d bytes", this.getBufferSize()));
        }

        this.command(SSD1306Device.COLUMN_ADDR);
        this.command(0); // Column start address. (0 = reset)
        this.command(this.width - 1); // Column end address.
        this.command(SSD1306Device.PAGE_ADDR);
        this.command(0); // Page start address. (0 = reset)
        this.command(this.pages - 1); // Page end address.
        this.write(data);
    }

    @Override
    public void clear() {
        this.display(this.allOff());
    }

    @Override
    public void fill() {
        this.display(this.allOn());
    }

    @Override
    public void on() {
        this.command(SSD1306Device.DISPLAY_ON);
    }

    @Override
    public void off() {
        this.command(SSD1306Device.DISPLAY_OFF);
    }

    @Override
    public void setContrast(int contrast) {
        if (contrast < 0 || contrast > 255) {
            throw new IllegalArgumentException("Constract must be between 0 and 255 inclusive");
        }

        this.command(SSD1306Device.SET_CONTRAST);
        this.command(contrast);
    }

    @Override
    public void setInvert(boolean invert) {
        this.command(SSD1306Device.SET_NORM_INV | (invert ? 0xFF : 0x00));
    }

    @Override
    public int getBufferSize() {
        return this.bufferSize;
    }

    protected void command(int command) {
        I2CRegister register = this.getDevice().register(SSD1306Device.COM_REG_ADDR);
        register.write(command);
    }

    protected void write(byte[] data) {
        I2CRegister register = this.getDevice().register(SSD1306Device.SET_DISP_START_LINE);
        for (int i = 0; i < this.chunksPerFrame; i++) {
            register.write(data, i * SSD1306Device.MAX_I2C_DATA_CHUNK, SSD1306Device.MAX_I2C_DATA_CHUNK);
        }
    }
}
