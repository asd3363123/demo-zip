package com.lyf.zip;

import java.util.Arrays;

/**
 * PasswordMarker maker = new PasswordMarker();
 * while (maker.hasNext()){
 * String next = maker.next();
 * ...
 * }
 */
public class PasswordMarker {
    public static final char[] DEFAULT_CHAR_ARRAY = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };
    private static final int DEFAULT_CHAR_ARRAY_LENGTH = DEFAULT_CHAR_ARRAY.length;
    private static final char DEFAULT_CHAR_ARRAY_FIRST = DEFAULT_CHAR_ARRAY[0];
    private static final char DEFAULT_CHAR_ARRAY_LAST = DEFAULT_CHAR_ARRAY[DEFAULT_CHAR_ARRAY_LENGTH - 1];
    /**
     * 最高支持20位
     */
    public static final int LEN_SUPPORT = 20;
    private static final char NULL = '\u0000';
    private final char[] RESULT = new char[22];

    private int charArrayLen;
    private char charArrayLast;
    private char charArrayFirst;
    private int len = 0;

    private char[] charArray;
    private int minBit;
    private int maxBit;

    public PasswordMarker() {
        this.charArray = DEFAULT_CHAR_ARRAY;
        this.minBit = 1;
        this.maxBit = LEN_SUPPORT;
        this.charArrayLen = DEFAULT_CHAR_ARRAY_LENGTH;
        this.charArrayFirst = DEFAULT_CHAR_ARRAY_FIRST;
        this.charArrayLast = DEFAULT_CHAR_ARRAY_LAST;
        init();
    }

    public PasswordMarker(char[] charArray, int minBit, int maxBit) {
        if (charArray == null || charArray.length < 1) {
            throw new NullPointerException("the charArray must not be null or empty ");
        }
        if (minBit < 1) {
            throw new NullPointerException("the minBit must greater than one ");
        }
        if (minBit >= maxBit) {
            throw new NullPointerException("the minBit must less than maxBit ");
        }
        if (maxBit > LEN_SUPPORT) {
            throw new NullPointerException("the minBit must less than PasswordMarker.LEN_SUPPORT ");
        }
        this.charArray = charArray;
        this.minBit = minBit;
        this.maxBit = maxBit;
        init();
    }

    /**
     * 是否有下一个密码
     */
    public boolean hasNext() {
        return checkLength();
    }

    /**
     * 获取下一个密码
     */
    public String next() {
        if (!hasNext()) {
            throw new NullPointerException("There is no more value ");
        }
        String next = getPassword();
        addOne();
        return next;
    }

    /**
     * 自增一
     */
    private void addOne() {
        addByBit(0);
    }

    /**
     * 在第 （index+1）位 加上 1
     */
    private void addByBit(int index) {
        char a = RESULT[index];
        if (a == NULL) {
            RESULT[index] = charArrayFirst;
            resetLength();
        } else if (a == charArrayLast) {
            RESULT[index] = charArrayFirst;
            addByBit(index + 1);
        } else {
            RESULT[index] = charArray[Arrays.binarySearch(charArray, a) + 1];
        }
    }

    private String getPassword() {
        resetLength();
        return new String(RESULT, 0, len);
    }

    private boolean checkLength() {
        return len <= maxBit;
    }

    private void init() {
        charArrayLen = charArray.length;
        charArrayFirst = charArray[0];
        charArrayLast = charArray[charArrayLen - 1];
        for (len = 0; len < minBit; len++) {
            RESULT[len] = charArrayFirst;
        }
        resetLength();
    }

    private void resetLength() {
        if (RESULT[len] != NULL) {
            len += 1;
        }
    }

    public static void main(String[] args) {
        PasswordMarker marker = new PasswordMarker(PasswordMarker.DEFAULT_CHAR_ARRAY, 4, 10);
        while (marker.hasNext()) {
            System.out.println(marker.next());
        }

    }
}
