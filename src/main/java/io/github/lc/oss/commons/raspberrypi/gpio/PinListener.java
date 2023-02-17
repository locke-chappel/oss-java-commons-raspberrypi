package io.github.lc.oss.commons.raspberrypi.gpio;

public interface PinListener {
    void change(PinIn pin, boolean state);
}
