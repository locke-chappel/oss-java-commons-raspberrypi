package io.github.lc.oss.commons.raspberrypi.i2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2CRegister;

public class TMP007Test extends AbstractI2cTest {
    @Test
    public void test_getTemperature() {
        this.expectDevice();

        I2CRegister reg = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x02)).thenReturn(reg);

        TMP007 dev = new TMP007("id", 0, UnitOfMeature.Celsius) {
            @Override
            protected Context getContext() {
                return TMP007Test.this.context;
            }
        };

        Assertions.assertTrue(dev.isAvailable());

        I2CRegister reg2 = Mockito.mock(I2CRegister.class);
        Mockito.when(dev.getDevice().register(0x03)).thenReturn(reg2);
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        }).when(reg2).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        double result = dev.getTemperature();
        Assertions.assertEquals(0, result);

        dev.close();
    }

    @Test
    public void test_getTemperature_overflow() {
        this.expectDevice();

        I2CRegister reg = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x02)).thenReturn(reg);

        TMP007 dev = new TMP007("id", 0, UnitOfMeature.Celsius) {
            @Override
            protected Context getContext() {
                return TMP007Test.this.context;
            }
        };

        Assertions.assertTrue(dev.isAvailable());

        I2CRegister reg2 = Mockito.mock(I2CRegister.class);
        Mockito.when(dev.getDevice().register(0x03)).thenReturn(reg2);
        Mockito.doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                Assertions.assertEquals(2, data.length);
                data[0] = (byte) 0xFF;
                data[1] = (byte) 0xFF;
                return null;
            }
        }).when(reg2).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        double result = dev.getTemperature();
        Assertions.assertEquals(-0.03125, result);

        dev.close();
    }

    @Test
    public void test_getTemperature_notAvailable() {
        this.expectDevice();

        I2CRegister reg = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x02)).thenReturn(reg);

        TMP007 dev = new TMP007("id", 0, UnitOfMeature.Celsius) {
            @Override
            protected Context getContext() {
                return TMP007Test.this.context;
            }
        };

        Assertions.assertTrue(dev.isAvailable());
        dev.setAvailable(false);

        double result = dev.getTemperature();
        Assertions.assertEquals(ThermalI2CDevice.ERORR, result);

        dev.close();
    }
}
