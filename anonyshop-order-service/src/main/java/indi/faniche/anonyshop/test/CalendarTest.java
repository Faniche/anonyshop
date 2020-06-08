package indi.faniche.anonyshop.test;

/* File:   CalendarTest.java
 * -------------------------
 * Author: faniche
 * Date:   5/13/20
 */

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class CalendarTest {
    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 30; i++) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MMDDHHmmss");
            Random random = new Random();
            int rand = random.nextInt(900) + 100;
            String time =  sdf.format(calendar.getTime());
            String seq = time + String.valueOf(rand);

        }


    }
}
