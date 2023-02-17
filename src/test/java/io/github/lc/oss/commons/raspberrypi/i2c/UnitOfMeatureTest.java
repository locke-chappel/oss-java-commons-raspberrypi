package io.github.lc.oss.commons.raspberrypi.i2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.lc.oss.commons.testing.AbstractTest;

public class UnitOfMeatureTest extends AbstractTest {
    @Test
    public void test_units() {
        Assertions.assertEquals("F", UnitOfMeature.Fahrenheit.getUnit());
        Assertions.assertEquals("C", UnitOfMeature.Celsius.getUnit());
        Assertions.assertEquals("K", UnitOfMeature.Kelvin.getUnit());
    }
}
