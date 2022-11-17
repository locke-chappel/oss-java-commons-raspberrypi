package com.github.lc.oss.commons.raspberrypi.gpio;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.github.lc.oss.commons.testing.AbstractTest;

public class AbstractButtonListenerTest extends AbstractTest {
    private static class TestListener extends AbstractButtonListener {
        public int methodCalled = -1;

        @Override
        protected long getShortPressDuration() {
            return 100;
        }

        @Override
        protected long getLongPressDuration() {
            return 500;
        }

        @Override
        protected void doPress() {
            this.methodCalled = 1;
        }

        @Override
        protected void doShortPress() {
            this.methodCalled = 2;
        }

        @Override
        protected void doLongPress() {
            this.methodCalled = 3;
        }
    }

    @Test
    public void test_values() {
        AbstractButtonListener listener = new AbstractButtonListener() {
            @Override
            protected void doShortPress() {
            }

            @Override
            protected void doPress() {
            }

            @Override
            protected void doLongPress() {
            }
        };

        Assertions.assertEquals(750, listener.getShortPressDuration());
        Assertions.assertEquals(2000, listener.getLongPressDuration());
    }

    @Test
    public void test_releaseWithoutPress() {
        TestListener listener = new TestListener();
        PinIn pin = new PinIn(17);

        Assertions.assertEquals(-1, listener.methodCalled);
        listener.change(pin, false);
        Assertions.assertEquals(-1, listener.methodCalled);
    }

    @Test
    public void test_tooShortPress() {
        TestListener listener = new TestListener();
        PinIn pin = new PinIn(17);

        Assertions.assertEquals(-1, listener.methodCalled);
        listener.change(pin, true);
        Assertions.assertEquals(-1, listener.methodCalled);
        listener.change(pin, false);
        Assertions.assertEquals(-1, listener.methodCalled);
    }

    @Test
    public void test_press() {
        TestListener listener = new TestListener();
        PinIn pin = new PinIn(17);

        Assertions.assertEquals(-1, listener.methodCalled);
        listener.change(pin, true);
        Assertions.assertEquals(-1, listener.methodCalled);
        long now = System.currentTimeMillis();
        this.waitUntil(() -> System.currentTimeMillis() - now > 10);
        Assertions.assertEquals(-1, listener.methodCalled);
        listener.change(pin, false);
        Assertions.assertEquals(1, listener.methodCalled);
    }

    @Test
    public void test_shortPress() {
        TestListener listener = new TestListener();
        PinIn pin = new PinIn(17);

        Assertions.assertEquals(-1, listener.methodCalled);
        listener.change(pin, true);
        Assertions.assertEquals(-1, listener.methodCalled);
        long now = System.currentTimeMillis();
        this.waitUntil(() -> System.currentTimeMillis() - now > listener.getShortPressDuration(), listener.getShortPressDuration() + 100);
        Assertions.assertEquals(-1, listener.methodCalled);
        listener.change(pin, false);
        Assertions.assertEquals(2, listener.methodCalled);
    }

    @Test
    public void test_longPress() {
        TestListener listener = new TestListener();
        PinIn pin = new PinIn(17);

        Assertions.assertEquals(-1, listener.methodCalled);
        listener.change(pin, true);
        Assertions.assertEquals(-1, listener.methodCalled);
        long now = System.currentTimeMillis();
        this.waitUntil(() -> System.currentTimeMillis() - now > listener.getLongPressDuration(), listener.getLongPressDuration() + 100);
        Assertions.assertEquals(-1, listener.methodCalled);
        listener.change(pin, false);
        Assertions.assertEquals(3, listener.methodCalled);
    }
}
