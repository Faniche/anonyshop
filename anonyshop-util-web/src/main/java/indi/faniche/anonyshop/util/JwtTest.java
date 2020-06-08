package indi.faniche.anonyshop.util;

/* File:   JwtTest.java
 * -------------------------
 * Author: faniche
 * Date:   4/30/20
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", "1");
        map.put("username", "Faniche");
        String ip = "187.45.12.1";
        String time = new SimpleDateFormat("yyyyMMdd HHmmss").format(new Date());

        String cmsg = JwtUtil.encode("2020anonyshop.tech", map, ip+time);
        System.out.printf(cmsg);
    }
}
