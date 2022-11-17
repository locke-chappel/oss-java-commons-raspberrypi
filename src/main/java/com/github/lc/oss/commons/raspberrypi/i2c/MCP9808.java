package com.github.lc.oss.commons.raspberrypi.i2c;

import java.io.IOException;

import com.pi4j.io.i2c.I2CRegister;

/*
 * Base on source code from:
 *   https://github.com/OlivierLD/raspberry-pi4j-samples/blob/master/I2C.SPI/src/i2c/sensor/MCP9808.java
 */
@SuppressWarnings("unused")
public class MCP9808 extends ThermalI2CDevice {
    // Registers
    private final static int MCP9808_REG_CONFIG = 0x01;
    private final static int MCP9808_REG_UPPER_TEMP = 0x02;
    private final static int MCP9808_REG_LOWER_TEMP = 0x03;
    private final static int MCP9808_REG_CRIT_TEMP = 0x04;
    private final static int MCP9808_REG_AMBIENT_TEMP = 0x05;
    private final static int MCP9808_REG_MANUF_ID = 0x06;
    private final static int MCP9808_REG_DEVICE_ID = 0x07;

    // Configuration register values.
    private final static int MCP9808_REG_CONFIG_SHUTDOWN = 0x0100;
    private final static int MCP9808_REG_CONFIG_CRITLOCKED = 0x0080;
    private final static int MCP9808_REG_CONFIG_WINLOCKED = 0x0040;
    private final static int MCP9808_REG_CONFIG_INTCLR = 0x0020;
    private final static int MCP9808_REG_CONFIG_ALERTSTAT = 0x0010;
    private final static int MCP9808_REG_CONFIG_ALERTCTRL = 0x0008;
    private final static int MCP9808_REG_CONFIG_ALERTSEL = 0x0002;
    private final static int MCP9808_REG_CONFIG_ALERTPOL = 0x0002;
    private final static int MCP9808_REG_CONFIG_ALERTMODE = 0x0001;

    public MCP9808(String id, int address, UnitOfMeature uom) {
        super(id, address, uom);

        int mid = 0, did = 0;
        try {
            mid = this.readU16BE(MCP9808.MCP9808_REG_MANUF_ID);
            did = this.readU16BE(MCP9808.MCP9808_REG_DEVICE_ID);
            this.setAvailable(mid == 0x0054 && did == 0x0400);
        } catch (Exception ex) {
            this.setAvailable(false);
            throw new RuntimeException("Error initializing device", ex);
        }
    }

    @Override
    public double getTemperature() {
        if (!this.isAvailable()) {
            return ThermalI2CDevice.ERORR;
        }

        int raw = this.readU16BE(MCP9808.MCP9808_REG_AMBIENT_TEMP);
        float temp = raw & 0x0FFF;
        temp /= 16.0;
        if ((raw & 0x1000) != 0x0) {
            temp -= 256;
        }
        return this.convert(temp, this.getUnitOfMeasure());
    }

    private int readU16BE(int register) {
        try {
            byte[] bytes = new byte[2];
            I2CRegister reg = this.getDevice().register(register);
            int read = reg.read(bytes, 0, bytes.length);
            if (read != bytes.length) {
                throw new IOException("Cannot read 2 bytes from 0x" + Integer.toHexString(register));
            }
            return ((bytes[0] & 0xFF) << 8) + (bytes[1] & 0xFF);
        } catch (IOException ex) {
            throw new RuntimeException("Error reading from device", ex);
        }
    }
}
