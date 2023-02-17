package io.github.lc.oss.commons.raspberrypi.i2c;

public abstract class HT16K33Device extends I2CDevice implements HT16K33Display {
    protected static final byte CMD_SETUP = (byte) 0x20;
    protected static final byte CMD_DISPLAY_CONFIG = (byte) 0x80;
    protected static final byte CMD_BRIGHTNESS = (byte) 0xe0;
    protected static final byte CMD_DISPLAY_DATA = (byte) 0x00;

    public enum BlinkRates {
        OFF((byte) (0x00 | 0x01)),
        TWO_HZ((byte) (0x02 | 0x01)),
        ONE_HZ((byte) (0x04 | 0x01)),
        HALF_HZ((byte) (0x06 | 0x01));

        private final Byte value;

        private BlinkRates(byte value) {
            this.value = value;
        }

        byte get() {
            return this.value;
        }
    }

    public enum Brightness {
        LEVEL_00((byte) 0x00),
        LEVEL_01((byte) 0x01),
        LEVEL_02((byte) 0x02),
        LEVEL_03((byte) 0x03),
        LEVEL_04((byte) 0x04),
        LEVEL_05((byte) 0x05),
        LEVEL_06((byte) 0x06),
        LEVEL_07((byte) 0x07),
        LEVEL_08((byte) 0x08),
        LEVEL_09((byte) 0x09),
        LEVEL_10((byte) 0x0A),
        LEVEL_11((byte) 0x0B),
        LEVEL_12((byte) 0x0C),
        LEVEL_13((byte) 0x0D),
        LEVEL_14((byte) 0x0E),
        LEVEL_16((byte) 0x0F);

        private final Byte value;

        private Brightness(byte value) {
            this.value = value;
        }

        byte get() {
            return this.value;
        }
    }

    public HT16K33Device(String id, int address) {
        super(id, address);

        this.on();
        this.setBrightness(Brightness.LEVEL_00);
        this.blink(BlinkRates.OFF);
        this.setAvailable(true);
    }

    @Override
    public void on() {
        this.write((byte) (HT16K33Device.CMD_SETUP | 0x01));
        this.write((byte) (HT16K33Device.CMD_DISPLAY_CONFIG | 0x01));
    }

    @Override
    public void off() {
        this.write((byte) (HT16K33Device.CMD_SETUP | 0x00));
        this.write((byte) (HT16K33Device.CMD_DISPLAY_CONFIG | 0x00));
    }

    @Override
    public void blink(BlinkRates rate) {
        this.write((byte) (HT16K33Device.CMD_DISPLAY_CONFIG | rate.get()));
    }

    @Override
    public void setBrightness(Brightness level) {
        this.write((byte) (HT16K33Device.CMD_BRIGHTNESS | level.get()));
    }

    @Override
    public abstract void clear();

    protected void write(byte[] displayData) {
        this.getDevice().write(displayData);
    }

    protected void write(byte data) {
        this.getDevice().write(data);
    }
}
