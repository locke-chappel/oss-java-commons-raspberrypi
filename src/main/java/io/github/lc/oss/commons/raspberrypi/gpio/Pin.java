package io.github.lc.oss.commons.raspberrypi.gpio;

public interface Pin {
    void init();

    boolean hasInitalized();
}
