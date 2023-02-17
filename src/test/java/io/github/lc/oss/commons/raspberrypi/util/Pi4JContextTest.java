package io.github.lc.oss.commons.raspberrypi.util;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.pi4j.context.Context;

import io.github.lc.oss.commons.testing.AbstractMockTest;

public class Pi4JContextTest extends AbstractMockTest {
    @Mock
    protected Context context;

    @BeforeEach
    public void init() {
        this.setContext(null);
    }

    @Test
    public void test_getContext() {
        Context result1 = Pi4JContext.getContext();
        Assertions.assertNotNull(result1);

        Context result2 = Pi4JContext.getContext();
        Assertions.assertSame(result1, result2);
    }

    @Test
    public void test_shutdown() {
        this.setContext(this.context);

        Mockito.when(this.context.shutdown()).thenReturn(null);

        Pi4JContext.shutdown();
    }

    @Test
    public void test_shutdown_noContext() {
        Pi4JContext.shutdown();
    }

    protected void setContext(Context context) {
        Field field = this.findField("context", Pi4JContext.class);
        this.setField(field, context, null);
    }
}
