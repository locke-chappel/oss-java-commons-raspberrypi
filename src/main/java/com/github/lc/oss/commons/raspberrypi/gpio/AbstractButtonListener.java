package com.github.lc.oss.commons.raspberrypi.gpio;

public abstract class AbstractButtonListener implements PinListener {
    private static final long MIN_THRESHOLD = 5;

    private long lastChange = -1;

    @Override
    public void change(PinIn pin, boolean state) {
        long now = System.currentTimeMillis();

        if (state) {
            this.lastChange = now;
        } else if (this.lastChange > 0) {
            this.buttonPressed(now - this.lastChange);
            this.lastChange = -1;
        }
    }

    protected void buttonPressed(long duration) {
        if (duration < AbstractButtonListener.MIN_THRESHOLD) {
            return;
        }

        if (duration >= this.getLongPressDuration()) {
            this.doLongPress();
        } else if (duration >= this.getShortPressDuration()) {
            this.doShortPress();
        } else {
            this.doPress();
        }
    }

    protected long getShortPressDuration() {
        return 750;
    }

    protected long getLongPressDuration() {
        return 2000;
    }

    protected abstract void doPress();

    protected abstract void doShortPress();

    protected abstract void doLongPress();
}
