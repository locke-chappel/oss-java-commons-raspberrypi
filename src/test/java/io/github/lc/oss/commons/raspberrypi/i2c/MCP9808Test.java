package io.github.lc.oss.commons.raspberrypi.i2c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.pi4j.context.Context;
import com.pi4j.io.i2c.I2CRegister;

public class MCP9808Test extends AbstractI2cTest {
    @Test
    public void test_constructor_readError() {
        this.expectDevice();

        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x06)).thenReturn(reg1);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                return 1;
            }
        }).when(reg1).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        try (MCP9808 dev = new MCP9808("id", 10, UnitOfMeature.Celsius) {
            @Override
            protected Context getContext() {
                return MCP9808Test.this.context;
            }
        }) {
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Error initializing device", ex.getMessage());
        }
    }

    @Test
    public void test_constructor_wrongMid() {
        this.expectDevice();

        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x06)).thenReturn(reg1);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x01;
                data[1] = (byte) 0x54;
                return 2;
            }
        }).when(reg1).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        I2CRegister reg2 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x07)).thenReturn(reg2);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x04;
                data[1] = (byte) 0x00;
                return 2;
            }
        }).when(reg2).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        MCP9808 dev = new MCP9808("id", 10, UnitOfMeature.Celsius) {
            @Override
            protected Context getContext() {
                return MCP9808Test.this.context;
            }
        };

        Assertions.assertFalse(dev.isAvailable());

        dev.close();
    }

    @Test
    public void test_constructor_wrongDid() {
        this.expectDevice();

        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x06)).thenReturn(reg1);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x00;
                data[1] = (byte) 0x54;
                return 2;
            }
        }).when(reg1).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        I2CRegister reg2 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x07)).thenReturn(reg2);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x04;
                data[1] = (byte) 0x01;
                return 2;
            }
        }).when(reg2).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        MCP9808 dev = new MCP9808("id", 10, UnitOfMeature.Celsius) {
            @Override
            protected Context getContext() {
                return MCP9808Test.this.context;
            }
        };

        Assertions.assertFalse(dev.isAvailable());

        dev.close();
    }

    @Test
    public void test_getTemperature_notAvailable() {
        this.expectDevice();

        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x06)).thenReturn(reg1);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x00;
                data[1] = (byte) 0x54;
                return 2;
            }
        }).when(reg1).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        I2CRegister reg2 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x07)).thenReturn(reg2);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x04;
                data[1] = (byte) 0x00;
                return 2;
            }
        }).when(reg2).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        MCP9808 dev = new MCP9808("id", 10, UnitOfMeature.Celsius) {
            @Override
            protected Context getContext() {
                return MCP9808Test.this.context;
            }
        };

        Assertions.assertTrue(dev.isAvailable());

        dev.setAvailable(false);

        Assertions.assertEquals(ThermalI2CDevice.ERORR, dev.getTemperature());

        dev.close();
    }

    @Test
    public void test_getTemperature() {
        this.expectDevice();

        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x06)).thenReturn(reg1);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x00;
                data[1] = (byte) 0x54;
                return 2;
            }
        }).when(reg1).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        I2CRegister reg2 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x07)).thenReturn(reg2);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x04;
                data[1] = (byte) 0x00;
                return 2;
            }
        }).when(reg2).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        MCP9808 dev = new MCP9808("id", 10, UnitOfMeature.Celsius) {
            @Override
            protected Context getContext() {
                return MCP9808Test.this.context;
            }
        };

        Assertions.assertTrue(dev.isAvailable());

        I2CRegister reg3 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x05)).thenReturn(reg3);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x00;
                data[1] = (byte) 0x00;
                return 2;
            }
        }).when(reg3).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        Assertions.assertEquals(0, dev.getTemperature());

        dev.close();
    }

    @Test
    public void test_getTemperature_overflow() {
        this.expectDevice();

        I2CRegister reg1 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x06)).thenReturn(reg1);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x00;
                data[1] = (byte) 0x54;
                return 2;
            }
        }).when(reg1).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());
        I2CRegister reg2 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x07)).thenReturn(reg2);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x04;
                data[1] = (byte) 0x00;
                return 2;
            }
        }).when(reg2).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        MCP9808 dev = new MCP9808("id", 10, UnitOfMeature.Celsius) {
            @Override
            protected Context getContext() {
                return MCP9808Test.this.context;
            }
        };

        Assertions.assertTrue(dev.isAvailable());

        I2CRegister reg3 = Mockito.mock(I2CRegister.class);
        Mockito.when(this.i2c.register(0x05)).thenReturn(reg3);
        Mockito.doAnswer(new Answer<Integer>() {
            @Override
            public Integer answer(InvocationOnMock invocation) throws Throwable {
                byte[] data = invocation.getArgument(0);
                data[0] = (byte) 0x10;
                data[1] = (byte) 0x00;
                return 2;
            }
        }).when(reg3).read((byte[]) ArgumentMatchers.notNull(), ArgumentMatchers.anyInt(), ArgumentMatchers.anyInt());

        Assertions.assertEquals(-256, dev.getTemperature());

        dev.close();
    }
}
