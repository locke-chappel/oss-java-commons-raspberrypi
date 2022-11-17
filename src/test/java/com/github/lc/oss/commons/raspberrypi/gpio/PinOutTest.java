package com.github.lc.oss.commons.raspberrypi.gpio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.github.lc.oss.commons.raspberrypi.util.Pi4JContext;
import com.github.lc.oss.commons.testing.AbstractMockTest;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;

public class PinOutTest extends AbstractMockTest {
    @Mock
    protected Context context;

    @Test
    public void test_initalize() {
        PinOut pin = new PinOut(0) {
            @Override
            protected Context getContext() {
                return PinOutTest.this.context;
            }
        };

        Assertions.assertFalse(pin.hasInitalized());
        Assertions.assertNull(pin.getPin());

        DigitalOutput d = Mockito.mock(DigitalOutput.class);
        Mockito.when(this.context.create((DigitalOutputConfigBuilder) ArgumentMatchers.notNull())).thenReturn(d);

        pin.init();

        Assertions.assertTrue(pin.hasInitalized());
        Assertions.assertSame(d, pin.getPin());

        try {
            pin.init();
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Cannot initalize pin more than once.", ex.getMessage());
        }

        pin.on();
        pin.off();
        pin.toggle();
    }

    @Test
    public void test_getContext() {
        PinOut pin = new PinOut(0);

        Context expected = Pi4JContext.getContext();
        Assertions.assertSame(expected, pin.getContext());
    }
}
