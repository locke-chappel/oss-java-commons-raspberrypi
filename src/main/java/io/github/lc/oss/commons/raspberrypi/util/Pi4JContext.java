package io.github.lc.oss.commons.raspberrypi.util;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;

public class Pi4JContext {
    private static Context context;

    public static Context getContext() {
        if (Pi4JContext.context == null) {
            Pi4JContext.context = Pi4J.newAutoContext();
        }
        return Pi4JContext.context;
    }

    public static void shutdown() {
        if (Pi4JContext.context != null) {
            Pi4JContext.context.shutdown();
        }
    }

    private Pi4JContext() {
    }
}
