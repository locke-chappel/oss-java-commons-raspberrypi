package com.github.lc.oss.commons.raspberrypi.i2c;

import org.junit.jupiter.api.Test;

import com.pi4j.context.Context;

public class HT16K33SevenSegmentTest extends AbstractI2cTest {
    @Test
    public void test_functions() {
        this.expectDevice();

        HT16K33SevenSegment dev = new HT16K33SevenSegment("id", 70) {
            @Override
            protected Context getContext() {
                return HT16K33SevenSegmentTest.this.context;
            }
        };

        dev.clear();

        dev.display("0", "0", false, "0", "0");

        dev.display(null, "A", true, "8.", "5");

        dev.close();
    }
}
