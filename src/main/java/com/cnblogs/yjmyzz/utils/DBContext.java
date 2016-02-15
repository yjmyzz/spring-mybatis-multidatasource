package com.cnblogs.yjmyzz.utils;

/**
 * Created by yangjunming on 2/15/16.
 * author: yangjunming@huijiame.com
 */
public class DBContext {

    //define count of database and it must match with resources/properties/jdbc.properties
    private static final int DB_COUNT = 2;

    private static final ThreadLocal<String> tlDbKey = new ThreadLocal<String>();

    public static String getDBKey() {
        return tlDbKey.get();
    }

    public static void setDBKey(String dbKey) {
        tlDbKey.set(dbKey);
    }

    public static String getDBKeyByUserId(int userId) {
        int dbIndex = userId % DB_COUNT;
        return "db_" + (++dbIndex);
    }
}
