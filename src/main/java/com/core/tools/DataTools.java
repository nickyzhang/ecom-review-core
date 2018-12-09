package com.core.tools;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DataTools {
    public static void main(String[] args) throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse("2015-01-01 00:00:00");
        System.out.println(date1.getTime());

        Date date2 = sdf.parse("2018-01-01 00:00:00");
        System.out.println(date2.getTime());
    }
}
