package com.mzx.time;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-12-01 14:34 周二.
 */
public class DateTimeTest {

    /**
     * 利用当前时间构造出最近三天的三件范围.
     */
    @Test
    public void t1() {

        // 起始时间.
        LocalDate now = LocalDate.now();
        LocalDate date = now.plusDays(1);
        // 结束时间.
        LocalDate localDate = date.plusDays(1);

        System.out.println(now.toString());
        System.out.println(localDate.toString());

        LocalTime mine = LocalTime.MIN;
        LocalTime max = LocalTime.MAX;
        System.out.println(mine);
        System.out.println(max);

        System.out.println("------------");

        LocalDateTime start = LocalDateTime.of(now, mine);
        LocalDateTime end = LocalDateTime.of(localDate, max);

        String format = start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println(format.toString());


    }

}
