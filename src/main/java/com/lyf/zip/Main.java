package com.lyf.zip;


import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.*;
import java.math.BigInteger;
import java.util.Random;

public class Main {

    private static final char[] NUM = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G',
            'H', 'I', 'J', 'K', 'L', 'M', 'N',
            'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    private static final int NUM_LEN = NUM.length;

    private static final BigInteger NUM_LEN_BIG_INT = BigInteger.valueOf(NUM_LEN);

    public static void main(String[] args) throws ZipException, IOException {
        String source = "f:\\test\\test.zip";
        String target = "f:\\test\\tt";
        FileWriter log = new FileWriter("f:\\test\\test.log");

//        ZipFile zipFile = new ZipFile(target);
//        ZipParameters parameters = new ZipParameters();
//        parameters.setPassword("1234");
//        //把source file加入到 ZipFile中
//        zipFile.createZipFile(new File(source), parameters);

//        extract(source, "gt", target, log);

//        System.out.println(getString(3));

//        String[] passwords = getDKEJ(NUM, 5);
//        System.out.println(Arrays.toString(passwords));
//        for (String pwd : passwords) {
//            extract(source, pwd, target, log);
//        }

//        extractRandom(source, target, log);

//        for (int i = 0; i < Integer.MAX_VALUE; i++) {
//            String pwd = getRandomPassword((int) (Math.random() * 10 + 1));
//            extract(source, pwd, target, log);
//        }
//
//        System.out.println(getPassword(BigInteger.valueOf(0)));
//        System.out.println(getPassword(BigInteger.valueOf(1)));
//        System.out.println(getPassword(BigInteger.valueOf(61)));
//        System.out.println(getPassword(BigInteger.valueOf(62)));
//        System.out.println(getPassword(BigInteger.valueOf(63)));
//
//        System.out.println(getRandomPassword(1));
//        System.out.println(getRandomPassword(2));
//        System.out.println(getRandomPassword(3));
//        System.out.println(getRandomPassword(4));
//        System.out.println(getRandomPassword(5));
//
//        BigInteger end = BigInteger.valueOf(NUM_LEN).pow(2).subtract(BigInteger.ONE)
//                .multiply(BigInteger.valueOf(NUM_LEN))
//                .divide(BigInteger.valueOf(NUM_LEN - 1));
//        System.out.println("end : " + end.longValue());

        PasswordMarker marker = new PasswordMarker(PasswordMarker.DEFAULT_CHAR_ARRAY, 4, 10);
        while (marker.hasNext()) {
            extract(source, marker.next(), target, log);
        }


    }

    /**
     * 获取笛卡尔机
     */
    private static String[] getDKEJ(char[] source, int time) {
        if (time < 1) {
            return new String[0];
        }
        int len = source.length;
        int size = (int) ((Math.pow(len, time) - 1) * len / (len - 1));
        String[] result = new String[size];

        //目前到了哪里
        int i = 0;
        //source 指针
        int s_i = 0;
        //result 指针
        int r_i = 0;

        while (i < size) {
            if (i < len) {
                result[i] = source[i] + "";
            } else {
                result[i] = result[s_i] + source[r_i];

                s_i += r_i / (len - 1);
                r_i++;
                r_i %= len;
            }
            i++;
        }

        return result;
    }

    private static String[] getDKEJ(char[] source, int time, FileWriter writer) {
        if (time < 1) {
            return new String[0];
        }
        int len = source.length;
        int size = (int) ((Math.pow(len, time) - 1) * len / (len - 1));
        String[] result = new String[size];

        //目前到了哪里
        int i = 0;
        //source 指针
        int s_i = 0;
        //result 指针
        int r_i = 0;

        while (i < size) {
            if (i < len) {
                result[i] = source[i] + "";
            } else {
                result[i] = result[s_i] + source[r_i];

                s_i += r_i / (len - 1);
                r_i++;
                r_i %= len;
            }
            i++;
        }

        return result;
    }

    /*
    将ZIP包中的文件解压到指定目录
    */
    public static void extract(String zipFilePath, String password, String destDir, FileWriter fileWriter) {
        InputStream is = null;
        OutputStream os = null;
        BufferedWriter log = new BufferedWriter(fileWriter);
        ZipFile zf;
        try {
            zf = new ZipFile(zipFilePath);
            zf.setFileNameCharset("utf-8");
            if (zf.isEncrypted()) {
                zf.setPassword(password);
            }

            for (Object obj : zf.getFileHeaders()) {
                FileHeader fileHeader = (FileHeader) obj;
                String destFilePath = destDir + "/" + fileHeader.getFileName();
                File destFile = new File(destFilePath);
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();//创建目录
                }
                os = new FileOutputStream(destFile);
                int readLen = -1;
                int BUFF_SIZE = 4096;
                byte[] buff = new byte[BUFF_SIZE];

                is = zf.getInputStream(fileHeader);

                while ((readLen = is.read(buff)) != -1) {
                    os.write(buff, 0, readLen);
                }
                long size = destFile.length();
                String logStr = String.format("success : %s , file size : %d \n", password, size);
                System.out.println(logStr);
                if (size > 40000) {
                    log.write(logStr);
                    log.newLine();
                    log.flush();
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println("password error : " + password);
        } finally {
            //关闭资源
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
            }

            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ioe) {
            }
        }
    }

    public static void extract(String zipFilePath, String[] password, String destDir) {
        InputStream is = null;
        OutputStream os = null;
        ZipFile zf;
        try {
            zf = new ZipFile(zipFilePath);
            zf.setFileNameCharset("utf-8");

            for (Object obj : zf.getFileHeaders()) {
                FileHeader fileHeader = (FileHeader) obj;
                String destFilePath = destDir + "/" + fileHeader.getFileName();
                File destFile = new File(destFilePath);
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();//创建目录
                }
                os = new FileOutputStream(destFile);
                int readLen = -1;
                int BUFF_SIZE = 4096;
                byte[] buff = new byte[BUFF_SIZE];


                for (String pwd : password
                ) {
                    if (zf.isEncrypted()) {
                        zf.setPassword(pwd);
                    }
                    try {
                        is = zf.getInputStream(fileHeader);
                        while ((readLen = is.read(buff)) != -1) {
                            os.write(buff, 0, readLen);
                        }
                        System.out.println("password = " + pwd);
                    } catch (Exception e) {
                        System.err.println("error password : " + pwd);
                    }
                }


            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println("password error : " + password);
        } finally {
            //关闭资源
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
            }

            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ioe) {
            }
        }
    }

    public static void extract(String zipFilePath, String destDir, BigInteger start, BigInteger end, FileWriter fileWriter) {
        InputStream is = null;
        OutputStream os = null;
        BufferedWriter log = new BufferedWriter(fileWriter);
        ZipFile zf;
        try {
            zf = new ZipFile(zipFilePath);
            zf.setFileNameCharset("utf-8");

            for (Object obj : zf.getFileHeaders()) {
                FileHeader fileHeader = (FileHeader) obj;
                String destFilePath = destDir + "/" + fileHeader.getFileName();
                File destFile = new File(destFilePath);
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();//创建目录
                }
                os = new FileOutputStream(destFile);
                int readLen = -1;
                int BUFF_SIZE = 4096;
                byte[] buff = new byte[BUFF_SIZE];
                long max = 0;


                while (start.compareTo(end) < 1) {
                    String pwd = getPassword(start);
                    if (zf.isEncrypted()) {
                        zf.setPassword(pwd);
                    }

                    try {
                        is = zf.getInputStream(fileHeader);
                        while ((readLen = is.read(buff)) != -1) {
                            os.write(buff, 0, readLen);
                        }
                        long l = destFile.length();
                        String logStr = String.format("success : %s , file size : %d \n", pwd, l);
                        System.out.println(logStr);
                        if (l > max) {
                            log.write(logStr);
                            log.newLine();
                            log.flush();
                            max = l;
                        }
                    } catch (Exception e) {
                        System.err.println("error password : " + pwd);
                    }

                    start = start.add(BigInteger.ONE);
                }


            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println(" start : " + start.intValue());
        } finally {
            //关闭资源
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
            }

            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ioe) {
            }
        }
    }

    /**
     * 效果相当于追加写，max会不断增加，弃用
     */
    @Deprecated
    public static void extractRandom(String zipFilePath, String destDir, FileWriter fileWriter) {
        InputStream is = null;
        OutputStream os = null;
        BufferedWriter log = new BufferedWriter(fileWriter);
        ZipFile zf;
        long max = 0;
        try {
            zf = new ZipFile(zipFilePath);
            zf.setFileNameCharset("utf-8");

            for (Object obj : zf.getFileHeaders()) {
                FileHeader fileHeader = (FileHeader) obj;
                String destFilePath = destDir + "/" + fileHeader.getFileName();
                File destFile = new File(destFilePath);
                if (!destFile.getParentFile().exists()) {
                    destFile.getParentFile().mkdirs();//创建目录
                }
                os = new FileOutputStream(destFile);
                int readLen = -1;
                int BUFF_SIZE = 4096;
                byte[] buff = new byte[BUFF_SIZE];

                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    String pwd = getRandomPassword((int) (Math.random() * 10 + 1));
                    if (zf.isEncrypted()) {
                        zf.setPassword(pwd);
                    }

                    try {
                        is = zf.getInputStream(fileHeader);
                        while ((readLen = is.read(buff)) != -1) {
                            os.write(buff, 0, readLen);
                        }
                        long l = destFile.length();
                        String logStr = String.format("success : %s , file size : %d \n", pwd, l);
                        System.out.println(logStr);
                        if (l > max) {
                            log.write(logStr);
                            log.newLine();
                            log.flush();
                            max = l;
                        }
                    } catch (Exception e) {
//                        e.printStackTrace();
                        System.err.println("error password : " + pwd + " , max + " + max);
                    }
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            System.err.println(" max : " + max);
        } finally {
            //关闭资源
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
            }

            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException ioe) {
            }
        }
    }

    private static String getPassword(BigInteger number) {
        if (number == null || BigInteger.ZERO.compareTo(number) > 0) {
            throw new NullPointerException("the number must > 0 , now is : " + number);
        }
        StringBuilder result = new StringBuilder();
        while (number.compareTo(NUM_LEN_BIG_INT) >= 0) {
            result.insert(0, NUM[number.mod(NUM_LEN_BIG_INT).intValue()]);
            number = number.divide(NUM_LEN_BIG_INT);
        }
        result.insert(0, NUM[number.intValue()]);

        return result.toString();
    }

    private static String getRandomPassword(int len) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < len; i++) {
            result.insert(0, NUM[(int) (Math.random() * NUM_LEN)]);
        }
        return result.toString();
    }
}