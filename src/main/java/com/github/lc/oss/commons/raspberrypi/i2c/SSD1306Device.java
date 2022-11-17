package com.github.lc.oss.commons.raspberrypi.i2c;

import com.pi4j.io.i2c.I2CRegister;

/* https://github.com/ondryaso/pi-ssd1306-java/blob/master/src/eu/ondryaso/ssd1306/Display.java */
/** WIP */
@SuppressWarnings("unused")
public class SSD1306Device extends I2CDevice implements SSD1306Display {
    private static final short SSD1306_I2C_ADDRESS = 0x3C;
    private static final short SSD1306_SETCONTRAST = 0x81;
    private static final short SSD1306_DISPLAYALLON_RESUME = 0xA4;
    private static final short SSD1306_DISPLAYALLON = 0xA5;
    private static final short SSD1306_NORMALDISPLAY = 0xA6;
    private static final short SSD1306_INVERTDISPLAY = 0xA7;
    private static final short SSD1306_DISPLAYOFF = 0xAE;
    private static final short SSD1306_DISPLAYON = 0xAF;
    private static final short SSD1306_SETDISPLAYOFFSET = 0xD3;
    private static final short SSD1306_SETCOMPINS = 0xDA;
    private static final short SSD1306_SETVCOMDETECT = 0xDB;
    private static final short SSD1306_SETDISPLAYCLOCKDIV = 0xD5;
    private static final short SSD1306_SETPRECHARGE = 0xD9;
    private static final short SSD1306_SETMULTIPLEX = 0xA8;
    private static final short SSD1306_SETLOWCOLUMN = 0x00;
    private static final short SSD1306_SETHIGHCOLUMN = 0x10;
    private static final short SSD1306_SETSTARTLINE = 0x40;
    private static final short SSD1306_MEMORYMODE = 0x20;
    private static final short SSD1306_COLUMNADDR = 0x21;
    private static final short SSD1306_PAGEADDR = 0x22;
    private static final short SSD1306_COMSCANINC = 0xC0;
    private static final short SSD1306_COMSCANDEC = 0xC8;
    private static final short SSD1306_SEGREMAP = 0xA0;
    private static final short SSD1306_CHARGEPUMP = 0x8D;
    private static final short SSD1306_EXTERNALVCC = 0x1;
    private static final short SSD1306_SWITCHCAPVCC = 0x2;

    private static final short SSD1306_ACTIVATE_SCROLL = 0x2F;
    private static final short SSD1306_DEACTIVATE_SCROLL = 0x2E;
    private static final short SSD1306_SET_VERTICAL_SCROLL_AREA = 0xA3;
    private static final short SSD1306_RIGHT_HORIZONTAL_SCROLL = 0x26;
    private static final short SSD1306_LEFT_HORIZONTAL_SCROLL = 0x27;
    private static final short SSD1306_VERTICAL_AND_RIGHT_HORIZONTAL_SCROLL = 0x29;
    private static final short SSD1306_VERTICAL_AND_LEFT_HORIZONTAL_SCROLL = 0x2A;

    private final int width;
    private final int height;
    private final int pages;

    protected int vccState;
    private byte[] buffer;

    public SSD1306Device(String id, int address, int width, int height) {
        super(id, address);

        this.width = width;
        this.height = height;
        this.pages = height / 8;
        this.buffer = new byte[width * this.pages];
    }

    /**
     * Begin with SWITCHCAPVCC VCC mode
     *
     * @see Constants#SSD1306_SWITCHCAPVCC
     */
    public void begin() {
        this.begin(SSD1306Device.SSD1306_SWITCHCAPVCC);
    }

    /**
     * Begin with specified VCC mode (can be SWITCHCAPVCC or EXTERNALVCC)
     *
     * @param vccState VCC mode
     * @see Constants#SSD1306_SWITCHCAPVCC
     * @see Constants#SSD1306_EXTERNALVCC
     */
    public void begin(int vccState) {
        this.vccState = vccState;
        this.initDisplay();
        this.getDevice().write(SSD1306Device.SSD1306_DISPLAYON);
        this.clear();
        this.display();
    }

    private void initDisplay() {
        if (this.width == 128 && this.height == 64) {
            this.init(0x3F, 0x12, 0x80);
        } else if (this.width == 128 && this.height == 32) {
            this.init(0x1F, 0x02, 0x80);
        } else if (this.width == 96 && this.height == 16) {
            this.init(0x0F, 0x02, 0x60);
        } else {
            throw new RuntimeException("Unsupported display dimensions");
        }
    }

    private void init(int multiplex, int compins, int ratio) {
        this.command(SSD1306Device.SSD1306_DISPLAYOFF);
        this.command(SSD1306Device.SSD1306_SETDISPLAYCLOCKDIV);
        this.command((short) ratio);
        this.command(SSD1306Device.SSD1306_SETMULTIPLEX);
        this.command((short) multiplex);
        this.command(SSD1306Device.SSD1306_SETDISPLAYOFFSET);
        this.command((short) 0x0);
        this.command(SSD1306Device.SSD1306_SETSTARTLINE);
        this.command(SSD1306Device.SSD1306_CHARGEPUMP);

        if (this.vccState == SSD1306Device.SSD1306_EXTERNALVCC) {
            this.command((short) 0x10);
        } else {
            this.command((short) 0x14);
        }

        this.command(SSD1306Device.SSD1306_MEMORYMODE);
        this.command((short) 0x00);
        this.command((short) (SSD1306Device.SSD1306_SEGREMAP | 0x1));
        this.command(SSD1306Device.SSD1306_COMSCANDEC);
        this.command(SSD1306Device.SSD1306_SETCOMPINS);
        this.command((short) compins);
        this.command(SSD1306Device.SSD1306_SETCONTRAST);

        if (this.vccState == SSD1306Device.SSD1306_EXTERNALVCC) {
            this.command((short) 0x9F);
        } else {
            this.command((short) 0xCF);
        }

        this.command(SSD1306Device.SSD1306_SETPRECHARGE);

        if (this.vccState == SSD1306Device.SSD1306_EXTERNALVCC) {
            this.command((short) 0x22);
        } else {
            this.command((short) 0xF1);
        }

        this.command(SSD1306Device.SSD1306_SETVCOMDETECT);
        this.command((short) 0x40);
        this.command(SSD1306Device.SSD1306_DISPLAYALLON_RESUME);
        this.command(SSD1306Device.SSD1306_NORMALDISPLAY);
    }

    /**
     * Clears the buffer by creating a new byte array
     */
    public void clear() {
        this.buffer = new byte[this.width * this.pages];
    }

    /**
     * Sends the buffer to the display
     */
    public synchronized void display() {
        this.command(SSD1306Device.SSD1306_COLUMNADDR);
        this.command(0);
        this.command(this.width - 1);
        this.command(SSD1306Device.SSD1306_PAGEADDR);
        this.command(0);
        this.command(this.pages - 1);

        for (int i = 0; i < this.buffer.length; i += 16) {
            this.write(0x40, this.buffer[i]);
        }
    }

    private void command(int cmd) {
        I2CRegister register = this.getDevice().register(0);
        register.write(cmd);
    }

    private void write(int address, byte data) {
        I2CRegister register = this.getDevice().register(address);
        register.write(data);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }
}
