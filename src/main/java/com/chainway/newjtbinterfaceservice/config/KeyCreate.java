package com.chainway.newjtbinterfaceservice.config;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

public class KeyCreate implements Serializable,Comparable {

    private static final long serialVersionUID = -4856846361193249489L;

    private final long mostSigBits;

    private final long leastSigBits;

    private transient int version = -1;

    private transient int variant = -1;

    private transient volatile long timestamp = -1;

    private transient int sequence = -1;

    private transient long node = -1;

    private transient int hashCode = -1;

    private static volatile SecureRandom numberGenerator = null;

    private KeyCreate(byte[] data) {
        if (data.length == 16) {
            long msb = 0;
            long lsb = 0;
            for (int i = 0; i < 8; i++) {
                msb = (msb << 8) | (data[i] & 0xff);
            }
            for (int i = 8; i < 16; i++) {
                lsb = (lsb << 8) | (data[i] & 0xff);
            }
            this.mostSigBits = msb;
            this.leastSigBits = lsb;
        } else {
            this.mostSigBits = 0;
            this.leastSigBits = 0;
            throw new RuntimeException("UUID主键异常！");
        }
    }

    public KeyCreate(long mostSigBits, long leastSigBits) {
        this.mostSigBits = mostSigBits;
        this.leastSigBits = leastSigBits;
    }

    public static KeyCreate randomUUID() {
        SecureRandom ng = numberGenerator;
        if (ng == null) {
            numberGenerator = ng = new SecureRandom();
        }

        byte[] randomBytes = new byte[16];
        ng.nextBytes(randomBytes);
        randomBytes[6] &= 0x0f;
        randomBytes[6] |= 0x40;
        randomBytes[8] &= 0x3f;
        randomBytes[8] |= 0x80;
        return new KeyCreate(randomBytes);
    }

    public static KeyCreate nameUUIDByBroFromBytes(byte[] name) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            throw new InternalError("MD5 not supported");
        }
        byte[] md5Bytes = md.digest(name);
        md5Bytes[6] &= 0x0f;
        md5Bytes[6] |= 0x30;
        md5Bytes[8] &= 0x3f;
        md5Bytes[8] |= 0x80;
        return new KeyCreate(md5Bytes);
    }

    public static KeyCreate fromString(String name) {
        String[] components = split(name, "-");
        if (components.length != 5) {
            throw new IllegalArgumentException("Invalid UUIDByBro string: "
                    + name);
        }
        for (int i = 0; i < 5; i++) {
            components[i] = "0x" + components[i];
        }

        long mostSigBits = Long.decode(components[0]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[1]).longValue();
        mostSigBits <<= 16;
        mostSigBits |= Long.decode(components[2]).longValue();

        long leastSigBits = Long.decode(components[3]).longValue();
        leastSigBits <<= 48;
        leastSigBits |= Long.decode(components[4]).longValue();

        return new KeyCreate(mostSigBits, leastSigBits);
    }

    public long getLeastSignificantBits() {
        return leastSigBits;
    }

    public long getMostSignificantBits() {
        return mostSigBits;
    }

    public int version() {
        if (version < 0) {
            version = (int) ((mostSigBits >> 12) & 0x0f);
        }
        return version;
    }

    public int variant() {
        if (variant < 0) {
            if ((leastSigBits >>> 63) == 0) {
                variant = 0;
            } else if ((leastSigBits >>> 62) == 2) {
                variant = 2;
            } else {
                variant = (int) (leastSigBits >>> 61);
            }
        }
        return variant;
    }

    public long timestamp() {
        if (version() != 1) {
            throw new UnsupportedOperationException(
                    "Not a time-based KeyCreate");
        }
        long result = timestamp;
        if (result < 0) {
            result = (mostSigBits & 0x0000000000000FFFL) << 48;
            result |= ((mostSigBits >> 16) & 0xFFFFL) << 32;
            result |= mostSigBits >>> 32;
            timestamp = result;
        }
        return result;
    }

    public int clockSequence() {
        if (version() != 1) {
            throw new UnsupportedOperationException(
                    "Not a time-based KeyCreate");
        }
        if (sequence < 0) {
            sequence = (int) ((leastSigBits & 0x3FFF000000000000L) >>> 48);
        }
        return sequence;
    }

    public long node() {
        if (version() != 1) {
            throw new UnsupportedOperationException(
                    "Not a time-based KeyCreate");
        }
        if (node < 0) {
            node = leastSigBits & 0x0000FFFFFFFFFFFFL;
        }
        return node;
    }

    public String toString() {
        return (digits(mostSigBits >> 32, 8) + digits(mostSigBits >> 16, 4)
                + digits(mostSigBits, 4) + digits(leastSigBits >> 48, 4) + digits(
                leastSigBits, 12));
    }

    private static String digits(long val, int digits) {
        long hi = 1L << (digits * 4);
        return Long.toHexString(hi | (val & (hi - 1))).substring(1);
    }

    public int hashCode() {
        if (hashCode == -1) {
            hashCode = (int) ((mostSigBits >> 32) ^ mostSigBits
                    ^ (leastSigBits >> 32) ^ leastSigBits);
        }
        return hashCode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof KeyCreate)) {
            return false;
        }
        if (((KeyCreate) obj).variant() != this.variant()) {
            return false;
        }
        KeyCreate id = (KeyCreate) obj;
        return (mostSigBits == id.mostSigBits && leastSigBits == id.leastSigBits);
    }

    public int compareTo(Object val) {
        return (this.mostSigBits < ((KeyCreate) val).mostSigBits ? -1
                : (this.mostSigBits > ((KeyCreate) val).mostSigBits ? 1
                : (this.leastSigBits < ((KeyCreate) val).leastSigBits ? -1
                : (this.leastSigBits > ((KeyCreate) val).leastSigBits ? 1
                : 0))));
    }

    private void readObject(java.io.ObjectInputStream in)
            throws java.io.IOException, ClassNotFoundException {

        in.defaultReadObject();
        version = -1;
        variant = -1;
        timestamp = -1;
        sequence = -1;
        node = -1;
        hashCode = -1;
    }

    /**
     * WSAD中String无split()
     *
     * @param str
     * @param symbol
     * @return
     */
    private static String[] split(String str, String symbol) {
        LinkedList list = new LinkedList();
        while (str.indexOf(symbol) >= 0) {
            int index = str.indexOf(symbol);
            list.add(str.substring(0, index));
            if (index + 1 < str.length()) {
                str = str.substring(index + 1, str.length());
            }
        }
        list.add(str);
        String[] result = new String[list.size()];
        Iterator iter = list.iterator();
        int i = 0;
        while (iter.hasNext()) {
            result[i] = (String) iter.next();
            i++;
        }
        return result;
    }

    public static String getbs() {
        Date date = new Date();
        // 格式化时间格式为年月日小时分秒毫秒
        SimpleDateFormat DateFormat = new SimpleDateFormat("yyyyMMddHHmmSSSSSS");
        String basic = DateFormat.format(date);
        Random rdm = new Random();
        int randnum1 = rdm.nextInt(10);
        basic += randnum1;
        int randnum2 = rdm.nextInt(10);
        basic += randnum2;
        return basic.toString();
    }

    public static String newCombID() {
        //UUID uuid = UUID.randomUUID();
        String newId = UUID.randomUUID().toString().trim().replaceAll("-", "");
        newId = newId.toUpperCase();
        return newId;
    }

}
