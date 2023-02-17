module io.github.lc.oss.commons.raspberrypi {
    requires transitive com.pi4j;
    requires transitive com.pi4j.plugin.pigpio;
    requires transitive java.desktop;

    exports io.github.lc.oss.commons.raspberrypi.gpio;
    exports io.github.lc.oss.commons.raspberrypi.i2c;
    exports io.github.lc.oss.commons.raspberrypi.util;
}
