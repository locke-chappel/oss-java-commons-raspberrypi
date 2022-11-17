package com.github.lc.oss.commons.raspberrypi.gpio;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.github.lc.oss.commons.raspberrypi.util.Pi4JContext;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.DigitalStateChangeEvent;
import com.pi4j.io.gpio.digital.DigitalStateChangeListener;
import com.pi4j.io.gpio.digital.PullResistance;

public class PinIn implements Pin {
    private final int pinNumber;
    private final PullResistance resistance;

    private Set<PinListener> listeners;
    private DigitalInput pin;

    public PinIn(int pinNumber, PinListener... listeners) {
        this(pinNumber, PullResistance.PULL_DOWN, listeners);
    }

    public PinIn(int pinNumber, PullResistance resistance, PinListener... listeners) {
        this.pinNumber = pinNumber;
        this.listeners = new HashSet<>(Arrays.asList(listeners));
        this.resistance = resistance;
    }

    public void init() {
        if (this.hasInitalized()) {
            throw new RuntimeException("Cannot initalize pin more than once.");
        }

        Context ctx = this.getContext();
        DigitalInputConfigBuilder cfg = DigitalInput.newConfigBuilder(ctx). //
                address(this.pinNumber). //
                pull(this.resistance). //
                debounce(3000l). //
                provider("pigpio-digital-input");
        this.pin = ctx.create(cfg);
        for (PinListener listener : this.listeners) {
            this.pin.addListener(this.newListener(listener));
        }
    }

    public boolean hasInitalized() {
        return this.pin != null;
    }

    public DigitalInput getPin() {
        return this.pin;
    }

    public boolean read() {
        return this.getPin().state() == DigitalState.HIGH;
    }

    protected Context getContext() {
        return Pi4JContext.getContext();
    }

    protected DigitalStateChangeListener newListener(PinListener listener) {
        return new DigitalStateChangeListener() {
            @Override
            public void onDigitalStateChange(@SuppressWarnings("rawtypes") DigitalStateChangeEvent event) {
                listener.change(PinIn.this, event.state() == DigitalState.HIGH);
            }
        };
    }
}
