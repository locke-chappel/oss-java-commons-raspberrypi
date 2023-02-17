package io.github.lc.oss.commons.raspberrypi.gpio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.Digital;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;

import io.github.lc.oss.commons.raspberrypi.util.Pi4JContext;
import io.github.lc.oss.commons.testing.AbstractMockTest;

public class PinInTest extends AbstractMockTest {
    @Mock
    protected Context context;

    @Test
    public void test_initalize() {
        PinIn pin = new PinIn(0) {
            @Override
            protected Context getContext() {
                return PinInTest.this.context;
            }
        };

        Assertions.assertFalse(pin.hasInitalized());
        Assertions.assertNull(pin.getPin());

        DigitalInput di = Mockito.mock(DigitalInput.class);
        Mockito.when(this.context.create((DigitalInputConfigBuilder) ArgumentMatchers.notNull())).thenReturn(di);

        pin.init();

        Assertions.assertTrue(pin.hasInitalized());
        Assertions.assertSame(di, pin.getPin());

        try {
            pin.init();
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Cannot initalize pin more than once.", ex.getMessage());
        }

        Mockito.when(di.state()).thenReturn(DigitalState.LOW);
        Assertions.assertFalse(pin.read());
    }

    @Test
    public void test_initalize_withListeners() {
        PinIn pin = new PinIn(0, new PinListener() {
            @Override
            public void change(PinIn pin, boolean state) {

            }
        }) {
            @Override
            protected Context getContext() {
                return PinInTest.this.context;
            }
        };

        Assertions.assertFalse(pin.hasInitalized());
        Assertions.assertNull(pin.getPin());

        DigitalInput di = Mockito.mock(DigitalInput.class);
        Mockito.when(this.context.create((DigitalInputConfigBuilder) ArgumentMatchers.notNull())).thenReturn(di);

        pin.init();

        Assertions.assertTrue(pin.hasInitalized());
        Assertions.assertSame(di, pin.getPin());

        try {
            pin.init();
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Cannot initalize pin more than once.", ex.getMessage());
        }

        Mockito.when(di.state()).thenReturn(DigitalState.HIGH);
        Assertions.assertTrue(pin.read());
    }

    @Test
    public void test_getContext() {
        PinIn pin = new PinIn(0);

        Context expected = Pi4JContext.getContext();
        Assertions.assertSame(expected, pin.getContext());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void test_listener() {
        PinIn pin = new PinIn(0);

        DigitalStateChangeListener l1 = pin.newListener(new PinListener() {
            @Override
            public void change(PinIn pin, boolean state) {
                Assertions.assertTrue(state);
            }
        });
        l1.onDigitalStateChange(new DigitalStateChangeEvent<Digital>(null, DigitalState.HIGH));

        DigitalStateChangeListener l2 = pin.newListener(new PinListener() {
            @Override
            public void change(PinIn pin, boolean state) {
                Assertions.assertFalse(state);
            }
        });
        l2.onDigitalStateChange(new DigitalStateChangeEvent<Digital>(null, DigitalState.LOW));
    }
}
