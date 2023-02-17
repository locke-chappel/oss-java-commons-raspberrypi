package io.github.lc.oss.commons.raspberrypi.i2c;

public interface Device {
    String getID();

    int getAddress();

    boolean isAvailable();
}
