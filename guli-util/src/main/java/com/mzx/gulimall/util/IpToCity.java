package com.mzx.gulimall.util;

import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @author ZhenXinMa.
 * @slogan 脚踏实地向前看.
 * @create 2020-09-30 17:51 周三.
 */
public class IpToCity {

    public static void main(String[] args) {

        // 本机是: 111.164.196.240
        System.out.println(getCityInfo("111.164.196.240"));

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

}
