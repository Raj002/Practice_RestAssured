package org.utils;

import java.io.File;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class Utilities {
    public static String getCurrentDate(String actualDateFormat){
        DateFormat dateFormat = new SimpleDateFormat(actualDateFormat);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static int getRandomNumber(int maxRange){
        Random random = new Random();
        return random.nextInt(maxRange);
    }

    public static int getRandomNumberBetweenRange(int minRange, int maxRange){
        Random random = new Random();
        return random.nextInt(maxRange - minRange) + minRange;
    }

    public static String createDirectory (String filePath) {
        String currentDate = Utilities.getCurrentDate("dd_MM_YYYY_HHmmss");
        File logDir = new File(filePath + currentDate + "/");

        if (!logDir.exists()) {
            logDir.mkdirs();
            return logDir.getPath()+"/";
        }
        return null;
    }

    public static String getDate(int count, String format) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, count);
        Date date = calendar.getTime();
        Format formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }
}
