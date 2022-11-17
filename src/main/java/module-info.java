module com.github.lc.oss.commons.raspberrypi {
    requires transitive com.pi4j;
    requires transitive com.pi4j.plugin.pigpio;

    exports com.github.lc.oss.commons.raspberrypi.gpio;
    exports com.github.lc.oss.commons.raspberrypi.i2c;
    exports com.github.lc.oss.commons.raspberrypi.util;
}
