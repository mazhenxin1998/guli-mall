package com.mzx.gulimall.util;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-30 17:51 周三.
 */
public class IpToCity {

    final static Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");


    public static void main(String[] args) throws Exception {

        // 本机是: 111.164.196.240
        //System.out.println(getCityInfo(getV4IP()));
        //System.out.println(getInnetIp());
        System.out.println(getCityInfo("111.164.196.1"));
        System.out.println(getCityInfo("117.8.157.53"));
        System.out.println(getCityInfo("117.8.222.213"));
        System.out.println(getCityInfo("219.158.7.217"));
        System.out.println(getCityInfo("123.126.0.218"));
        System.out.println(getCityInfo("61.49.142.146"));
        System.out.println(getCityInfo("192.144.198.148"));

    }

    public static String getCityInfo(String ip) {

        //db
        //String dbPath = IpUtils.class.getResource("/city/ip2region.db").getPath();
        String path = IpToCity.class.getResource("/city/ip2region.db").getPath();
        File file = new File(path);
        if (file.exists() == false) {

            System.out.println("Error: Invalid ip2region.db file");

        }

        //查询算法
        //B-tree, B树搜索（更快）
        int algorithm = DbSearcher.BTREE_ALGORITHM;
        //Binary,使用二分搜索
        //DbSearcher.BINARY_ALGORITHM
        //Memory,加载内存（最快）
        //DbSearcher.MEMORY_ALGORITYM
        try {

            DbConfig config = new DbConfig();
            DbSearcher searcher = new DbSearcher(config, path);
            //define the method
            Method method = null;
            switch (algorithm) {

                case DbSearcher.BTREE_ALGORITHM:

                    method = searcher.getClass().getMethod("btreeSearch", String.class);
                    break;

                case DbSearcher.BINARY_ALGORITHM:

                    method = searcher.getClass().getMethod("binarySearch", String.class);
                    break;

                case DbSearcher.MEMORY_ALGORITYM:

                    method = searcher.getClass().getMethod("memorySearch", String.class);
                    break;

            }

            DataBlock dataBlock = null;
            if (Util.isIpAddress(ip) == false) {

                System.out.println("Error: Invalid ip address");

            }

            dataBlock = (DataBlock) method.invoke(searcher, ip);
            String ipInfo = dataBlock.getRegion();
            if (ipInfo != null || ipInfo == "") {

                ipInfo = ipInfo.replace("|0", "");

            }

            return ipInfo;

        } catch (Exception e) {

            e.printStackTrace();

        }

        return null;

    }


    /**
     * 192.168.56.1.
     *
     * @return
     * @throws SocketException
     */
    public static String getInnetIp() throws SocketException {

        String localip = null;// 本地IP，如果没有配置外网IP则返回它
        String netip = null;// 外网IP
        Enumeration<NetworkInterface> netInterfaces;
        netInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        boolean finded = false;// 是否找到外网IP
        while (netInterfaces.hasMoreElements() && !finded) {
            NetworkInterface ni = netInterfaces.nextElement();
            Enumeration<InetAddress> address = ni.getInetAddresses();
            while (address.hasMoreElements()) {
                ip = address.nextElement();
                if (!ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                    netip = ip.getHostAddress();
                    finded = true;
                    break;
                } else if (ip.isSiteLocalAddress()
                        && !ip.isLoopbackAddress()
                        && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                    localip = ip.getHostAddress();
                }
            }
        }
        if (netip != null && !"".equals(netip)) {
            return netip;
        } else {
            return localip;
        }
    }

    public static String getV4IP() throws Exception {

        String ip = "";
        String chinaz = "http://ip.chinaz.com";
        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n");
            }
            //System.out.println(inputLine.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            String ipstr = m.group(1);
            ip = ipstr;
            //System.out.println(ipstr);
        }
        return ip;
    }

}
