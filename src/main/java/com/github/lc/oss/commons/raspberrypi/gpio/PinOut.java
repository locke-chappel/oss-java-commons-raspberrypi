package com.github.lc.oss.commons.raspberrypi.gpio;

import com.github.lc.oss.commons.raspberrypi.util.Pi4JContext;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfigBuilder;
import com.pi4j.io.gpio.digital.DigitalState;

public class PinOut implements Pin {
    private final int pinNumber;

    private DigitalOutput pin;

    public PinOut(int pinNumber) {
        this.pinNumber = pinNumber;
    }

    public void init() {
        if (this.hasInitalized()) {
            throw new RuntimeException("Cannot initalize pin more than once.");
        }

        Context ctx = this.getContext();
        DigitalOutputConfigBuilder cfg = DigitalOutput.newConfigBuilder(ctx). //
                address(this.pinNumber). //
                shutdown(DigitalState.LOW). //
                initial(DigitalState.LOW). //
                provider("pigpio-digital-output");
        this.pin = ctx.create(cfg);
    }

    public boolean hasInitalized() {
        return this.pin != null;
    }

    public DigitalOutput getPin() {
        return this.pin;
    }

    public void on() {
        this.getPin().high();
    }

    public void off() {
        this.getPin().low();
    }

    public void toggle() {
        this.getPin().toggle();
    }

    protected Context getContext() {
        return Pi4JContext.getContext();
    }
}
