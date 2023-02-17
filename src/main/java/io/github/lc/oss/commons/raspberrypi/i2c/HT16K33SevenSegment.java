package io.github.lc.oss.commons.raspberrypi.i2c;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HT16K33SevenSegment extends HT16K33Device implements SevenSegmentDisplay {
    private static final byte[] CLEAR = new byte[] { HT16K33Device.CMD_DISPLAY_DATA, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

    private static final byte ADD_DECIMAL = (byte) 0x80;

    private static final Map<String, Byte> BITMAP;
    static {
        /**
         * <pre>
         *       __A__           The binary representation is:
         *      |     |              PGFE DCBA    (P = Decimal Point)
         *    F |     | B
         *      |__G__|          __
         *      |     |            |  => The number "7" is 0000 0111 = 0x07
         *    E |     | C          |     With decimal "7." is 1000 0111 = 0x87
         *      |_____| .
         *         D     P
         * </pre>
         */

        Map<String, Byte> map = new HashMap<>();

        map.put("0", (byte) 0x3F);
        map.put("1", (byte) 0x06);
        map.put("2", (byte) 0x5B);
        map.put("3", (byte) 0x4F);
        map.put("4", (byte) 0x66);
        map.put("5", (byte) 0x6D);
        map.put("6", (byte) 0x7D);
        map.put("7", (byte) 0x07);
        map.put("8", (byte) 0x7F);
        map.put("9", (byte) 0x6F);
        map.put("A", (byte) 0x77);
        map.put("a", (byte) 0x5F);
        map.put("B", (byte) 0x7C); // only lower
        map.put("b", (byte) 0x7C);
        map.put("C", (byte) 0x39);
        map.put("c", (byte) 0x58);
        map.put("D", (byte) 0x5E); // only lower
        map.put("d", (byte) 0x5E);
        map.put("E", (byte) 0x79);
        map.put("e", (byte) 0x79); // only upper
        map.put("F", (byte) 0x71);
        map.put("f", (byte) 0x71); // only upper
        map.put("G", (byte) 0x3D);
        map.put("g", (byte) 0x3D); // only upper
        map.put("H", (byte) 0x76);
        map.put("h", (byte) 0x74);
        map.put("I", (byte) 0x30);
        map.put("i", (byte) 0x30); // only upper
        map.put("J", (byte) 0x1E);
        map.put("j", (byte) 0x1E); // only upper
        map.put("L", (byte) 0x38);
        map.put("l", (byte) 0x38); // only upper
        map.put("N", (byte) 0x54); // only lower
        map.put("n", (byte) 0x54);
        map.put("O", (byte) 0x3F); // same as 0
        map.put("o", (byte) 0x5C);
        map.put("P", (byte) 0x73);
        map.put("p", (byte) 0x73); // only upper
        map.put("Q", (byte) 0x67); // only lower
        map.put("q", (byte) 0x67);
        map.put("R", (byte) 0x50); // only lower
        map.put("r", (byte) 0x50);
        map.put("S", (byte) 0x6D);
        map.put("s", (byte) 0x6D); // only upper
        map.put("T", (byte) 0x78);
        map.put("t", (byte) 0x78); // only lower
        map.put("U", (byte) 0x3E);
        map.put("u", (byte) 0x1C);
        map.put("Y", (byte) 0x6E); // only lower
        map.put("y", (byte) 0x6E);

        map.put("0.", (byte) (0x3F | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("1.", (byte) (0x06 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("2.", (byte) (0x5B | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("3.", (byte) (0x4F | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("4.", (byte) (0x66 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("5.", (byte) (0x6D | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("6.", (byte) (0x7D | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("7.", (byte) (0x07 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("8.", (byte) (0x7F | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("9.", (byte) (0x6F | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("A.", (byte) (0x77 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("a.", (byte) (0x5F | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("B.", (byte) (0x7C | HT16K33SevenSegment.ADD_DECIMAL)); // only lower
        map.put("b.", (byte) (0x7C | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("C.", (byte) (0x39 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("c.", (byte) (0x58 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("D.", (byte) (0x5E | HT16K33SevenSegment.ADD_DECIMAL)); // only lower
        map.put("d.", (byte) (0x5E | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("E.", (byte) (0x79 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("e.", (byte) (0x79 | HT16K33SevenSegment.ADD_DECIMAL)); // only upper
        map.put("F.", (byte) (0x71 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("f.", (byte) (0x71 | HT16K33SevenSegment.ADD_DECIMAL)); // only upper
        map.put("G.", (byte) (0x3D | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("g.", (byte) (0x3D | HT16K33SevenSegment.ADD_DECIMAL)); // only upper
        map.put("H.", (byte) (0x76 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("h.", (byte) (0x74 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("I.", (byte) (0x30 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("i.", (byte) (0x30 | HT16K33SevenSegment.ADD_DECIMAL)); // only upper
        map.put("J.", (byte) (0x1E | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("j.", (byte) (0x1E | HT16K33SevenSegment.ADD_DECIMAL)); // only upper
        map.put("L.", (byte) (0x38 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("l.", (byte) (0x38 | HT16K33SevenSegment.ADD_DECIMAL)); // only upper
        map.put("N.", (byte) (0x54 | HT16K33SevenSegment.ADD_DECIMAL)); // only lower
        map.put("n.", (byte) (0x54 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("O.", (byte) (0x3F | HT16K33SevenSegment.ADD_DECIMAL)); // same as 0
        map.put("o.", (byte) (0x5C | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("P.", (byte) (0x73 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("p.", (byte) (0x73 | HT16K33SevenSegment.ADD_DECIMAL)); // only upper
        map.put("Q.", (byte) (0x67 | HT16K33SevenSegment.ADD_DECIMAL)); // only lower
        map.put("q.", (byte) (0x67 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("R.", (byte) (0x50 | HT16K33SevenSegment.ADD_DECIMAL)); // only lower
        map.put("r.", (byte) (0x50 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("S.", (byte) (0x6D | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("s.", (byte) (0x6D | HT16K33SevenSegment.ADD_DECIMAL)); // only upper
        map.put("T.", (byte) (0x78 | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("t.", (byte) (0x78 | HT16K33SevenSegment.ADD_DECIMAL)); // only lower
        map.put("U.", (byte) (0x3E | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("u.", (byte) (0x1C | HT16K33SevenSegment.ADD_DECIMAL));
        map.put("Y.", (byte) (0x6E | HT16K33SevenSegment.ADD_DECIMAL)); // only lower
        map.put("y.", (byte) (0x6E | HT16K33SevenSegment.ADD_DECIMAL));

        BITMAP = Collections.unmodifiableMap(map);
    }

    public HT16K33SevenSegment(String id, int address) {
        super(id, address);
    }

    @Override
    public void clear() {
        this.write(HT16K33SevenSegment.CLEAR);
    }

    @Override
    public void display(String a, String b, boolean colon, String c, String d) {
        this.write(new byte[] { //
                HT16K33Device.CMD_DISPLAY_DATA, // command
                this.charToByte(a), // char 1
                0x00, // ignored
                this.charToByte(b), // char 2
                0x00, // ignored
                colon ? (byte) 0xFF : (byte) 0x00, // colon
                0x00, // ignored
                this.charToByte(c), // char 3
                0x00, // ignored
                this.charToByte(d) // char 4
        });
    }

    private byte charToByte(String s) {
        Byte b = HT16K33SevenSegment.BITMAP.get(s);
        if (b == null) {
            return 0x00;
        }
        return b;
    }
}
