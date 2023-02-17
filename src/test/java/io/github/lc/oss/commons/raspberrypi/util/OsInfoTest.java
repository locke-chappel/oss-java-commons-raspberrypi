package io.github.lc.oss.commons.raspberrypi.util;

import java.lang.reflect.Field;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.github.lc.oss.commons.testing.AbstractTest;

public class OsInfoTest extends AbstractTest {
    private Properties originalProperties;

    @BeforeEach
    public void init() {
        this.originalProperties = System.getProperties();
    }

    @AfterEach
    public void cleanup() {
        System.setProperties(this.originalProperties);
    }

    @Test
    public void test_isLinux() {
        this.clear();
        Properties props = new Properties();
        System.setProperties(props);
        Assertions.assertFalse(OsInfo.isLinux());

        this.clear();
        System.setProperty("os.name", "");
        Assertions.assertFalse(OsInfo.isLinux());

        this.clear();
        System.setProperty("os.name", " \r \t \n \t ");
        Assertions.assertFalse(OsInfo.isLinux());

        this.clear();
        System.setProperty("os.name", "Windows");
        Assertions.assertFalse(OsInfo.isLinux());
        // test cache
        Assertions.assertFalse(OsInfo.isLinux());

        this.clear();
        System.setProperty("os.name", "Linux");
        Assertions.assertTrue(OsInfo.isLinux());
        // test cache
        Assertions.assertTrue(OsInfo.isLinux());

        this.clear();
        System.setProperty("os.name", "linux");
        Assertions.assertTrue(OsInfo.isLinux());

        this.clear();
        System.setProperty("os.name", "LINUX");
        Assertions.assertTrue(OsInfo.isLinux());

        this.clear();
        System.setProperty("os.name", "lInUx");
        Assertions.assertTrue(OsInfo.isLinux());
    }

    @Test
    public void test_isArm() {
        this.clear();
        Properties props = new Properties();
        System.setProperties(props);
        Assertions.assertFalse(OsInfo.isArm());

        this.clear();
        System.setProperty("os.arch", "");
        Assertions.assertFalse(OsInfo.isArm());

        this.clear();
        System.setProperty("os.arch", " \r \t \n \t ");
        Assertions.assertFalse(OsInfo.isArm());

        this.clear();
        System.setProperty("os.arch", "x64");
        Assertions.assertFalse(OsInfo.isArm());
        // test cache
        Assertions.assertFalse(OsInfo.isArm());

        this.clear();
        System.setProperty("os.arch", "ARM");
        Assertions.assertTrue(OsInfo.isArm());
        // test cache
        Assertions.assertTrue(OsInfo.isArm());

        this.clear();
        System.setProperty("os.arch", "arm");
        Assertions.assertTrue(OsInfo.isArm());

        this.clear();
        System.setProperty("os.arch", "Arm");
        Assertions.assertTrue(OsInfo.isArm());

        this.clear();
        System.setProperty("os.arch", "aRm");
        Assertions.assertTrue(OsInfo.isArm());
    }

    @Test
    public void test_isRaspbian() {
        this.clear();
        System.setProperty("os.name", "Windows");
        System.setProperty("os.arch", "x64");
        Assertions.assertFalse(OsInfo.isRaspbian());

        this.clear();
        System.setProperty("os.name", "Windows");
        System.setProperty("os.arch", "ARM");
        Assertions.assertFalse(OsInfo.isRaspbian());

        this.clear();
        System.setProperty("os.name", "Linux");
        System.setProperty("os.arch", "x64");
        Assertions.assertFalse(OsInfo.isRaspbian());

        this.clear();
        System.setProperty("os.name", "Linux");
        System.setProperty("os.arch", "ARM");
        Assertions.assertTrue(OsInfo.isRaspbian());
    }

    private void clear() {
        Field osField = this.findField("os", OsInfo.class);
        Assertions.assertNotNull(osField);
        Field archField = this.findField("arch", OsInfo.class);
        Assertions.assertNotNull(archField);
        this.setField(osField, null, null);
        this.setField(archField, null, null);
    }
}
