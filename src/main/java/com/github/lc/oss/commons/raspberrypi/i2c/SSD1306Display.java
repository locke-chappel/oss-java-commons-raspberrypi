package com.github.lc.oss.commons.raspberrypi.i2c;

public interface SSD1306Display extends Device {
    /**
     * @return The number of bytes in each frame
     */
    int getBufferSize();

    /**
     * Set all pixels to off
     */
    void clear();

    /**
     * Write the given data to the display. 1-bit turns a pixel on, 0-bit turns the
     * pixel off.
     *
     * The array length in bytes is given by {@linkplain #getBufferSize()}.
     *
     * This implementation uses Paged addressing mode (as opposed to Horizontal
     * mode).
     */
    void display(byte[] data);

    /**
     * Sets all pixels to on
     */
    void fill();

    /**
     * Turns the display on
     */
    void on();

    /**
     * Turns the display off
     */
    void off();

    /**
     * Sets the contrast, 0-255 inclusive
     */
    void setContrast(int contrast);

    /**
     * Inverts each pixel (all 0's become 1's and vice versa)
     */
    void setInvert(boolean invert);
}
