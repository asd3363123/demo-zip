package com.lyf.zip;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class newMain {
    private static final char[] NUM = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };
    private static final int NUM_LEN = NUM.length;
    private static final char NUM_LAST = NUM[NUM_LEN - 1];
    private static final char NUM_FIRST = NUM[0];
    private static final char NULL = '\u0000';

    private static final String SOURCE = "f:\\test\\test.zip";
    private static final String TARGET = "f:\\test\\tt";
    private static final String LOG_PATH = "f:\\test\\test.log";

    private static final char[] result = new char[7];

    private static int len = 0;

    private static final int MAX_LEN = 5;

    public static void main(String[] args) {
        System.out.println(Arrays.toString(result));
        System.out.println(result[0]);
        System.out.println(result[0] == NULL);

        init();
        while (checkLength()) {
            doIt();
            addOne();
        }
    }

    private static void addOne() {
        addByBit(0);
    }

    /**
     * 在第 （index+1）位 加上 1
     */
    private static void addByBit(int index) {
        char a = result[index];
        if (a == NULL) {
            result[index] = NUM_FIRST;
            resetLength();
        } else if (a == NUM_LAST) {
            result[index] = NUM_FIRST;
            addByBit(index + 1);
            resetLength();
        } else {
            result[index] = NUM[Arrays.binarySearch(NUM, a) + 1];
        }
    }

    private static void doIt() {
        String password = getPassword();
        try {
            Main.extract(SOURCE, password, TARGET, new FileWriter(LOG_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println(" open file error ");
            System.exit(-1);
        }
    }

    private static String getPassword() {
        resetLength();
        return new String(result, 0, len);
    }

    private static boolean checkLength() {
        return len <= MAX_LEN;
    }

    private static void init() {
        result[0] = NUM[0];
        resetLength();
    }

    private static void resetLength() {
        if (result[len] != NULL) {
            len += 1;
        }
    }

}
