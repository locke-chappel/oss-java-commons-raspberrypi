package io.github.lc.oss.commons.raspberrypi.i2c;

import io.github.lc.oss.commons.raspberrypi.util.Pi4JContext;
import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;

public class I2CDevice implements Device, AutoCloseable {
    private final String id;
    private final int bus;
    private final int address;

    private boolean available = false;
    private I2C device;

    public I2CDevice(String id, int address) {
        this(id, 1, address);
    }

    public I2CDevice(String id, int bus, int address) {
        this.id = id;
        this.bus = bus;
        this.address = address;
    }

    protected I2C getDevice() {
        if (this.device == null) {
            Context ctx = this.getContext();
            I2CConfig cfg = I2C.newConfigBuilder(ctx). //
                    id(this.id). //
                    bus(this.bus). //
                    device(this.address). //
                    provider("pigpio-i2c"). //
                    build();
            this.device = ctx.create(cfg);
            this.device.initialize(this.getContext());
        }
        return this.device;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public int getAddress() {
        return this.address;
    }

    @Override
    public boolean isAvailable() {
        return this.available;
    }

    protected void setAvailable(boolean available) {
        this.available = available;
    }

    protected Context getContext() {
        return Pi4JContext.getContext();
    }

    @Override
    public void close() {
        this.getDevice().close();
    }
}
