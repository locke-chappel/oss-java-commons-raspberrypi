package com.github.lc.oss.commons.raspberrypi.i2c;

import com.pi4j.io.i2c.I2CRegister;

/*
 * Base on source code from:
 *    https://github.com/ControlEverythingCommunity/TMP007/blob/master/Java/TMP007.java
 */
public class TMP007 extends ThermalI2CDevice {
    public TMP007(String id, int address, UnitOfMeature uom) {
        super(id, address, uom);

        // Select configuration register
        // Continuous conversion, comparator mode
        byte[] config = { 0x15, 0x40 };
        I2CRegister reg = this.getDevice().register(0x02);
        reg.write(config, 0, 2);
        this.setAvailable(true);
    }

    @Override
    public double getTemperature() {
        if (!this.isAvailable()) {
            return ThermalI2CDevice.ERORR;
        }

        // Read 2 bytes of data from address 0x03(3)
        // temp msb, temp lsb
        byte[] data = new byte[2];
        I2CRegister reg = this.getDevice().register(0x03);
        reg.read(data, 0, 2);

        // Convert the data to 14-bits
        int temp = ((data[0] & 0xFF) * 256 + (data[1] & 0xFC)) / 4;
        if (temp > 8191) {
            temp -= 16384;
        }
        double celcius = temp * 0.03125;
        return this.convert(celcius, this.getUnitOfMeasure());
    }
}
