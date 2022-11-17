package com.github.lc.oss.commons.raspberrypi.i2c;

import com.github.lc.oss.commons.raspberrypi.i2c.HT16K33Device.BlinkRates;
import com.github.lc.oss.commons.raspberrypi.i2c.HT16K33Device.Brightness;

public interface HT16K33Display extends Device {
    void on();

    void off();

    void blink(BlinkRates rate);

    void setBrightness(Brightness level);

    void clear();
}
