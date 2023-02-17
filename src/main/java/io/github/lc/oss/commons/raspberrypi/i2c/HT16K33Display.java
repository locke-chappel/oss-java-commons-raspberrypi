package io.github.lc.oss.commons.raspberrypi.i2c;

import io.github.lc.oss.commons.raspberrypi.i2c.HT16K33Device.BlinkRates;
import io.github.lc.oss.commons.raspberrypi.i2c.HT16K33Device.Brightness;

public interface HT16K33Display extends Device {
    void on();

    void off();

    void blink(BlinkRates rate);

    void setBrightness(Brightness level);

    void clear();
}
