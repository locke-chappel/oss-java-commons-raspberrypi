package io.github.lc.oss.commons.raspberrypi.i2c;

import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.pi4j.context.Context;
import com.pi4j.context.ContextProperties;
import com.pi4j.io.i2c.I2C;
import com.pi4j.io.i2c.I2CConfig;

import io.github.lc.oss.commons.testing.AbstractMockTest;

public abstract class AbstractI2cTest extends AbstractMockTest {
    @Mock
    protected Context context;
    @Mock
    protected I2C i2c;

    protected void expectDevice() {
        ContextProperties properties = Mockito.mock(ContextProperties.class);
        Mockito.when(this.context.create((I2CConfig) ArgumentMatchers.notNull())).thenReturn(this.i2c);
        Mockito.when(this.context.properties()).thenReturn(properties);
    }
}
