package io.github.lc.oss.commons.raspberrypi.i2c;

public interface SevenSegmentDisplay extends HT16K33Display {
    void display(String a, String b, boolean colon, String c, String d);
}
