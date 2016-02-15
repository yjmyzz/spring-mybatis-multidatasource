package test.com.cnblogs.yjmyzz.utils;

import com.cnblogs.yjmyzz.utils.DBContext;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by yangjunming on 2/15/16.
 * author: yangjunming@huijiame.com
 */
public class TestDBContext {

    @Test
    public void testGetDBKeyByUserId() {

        String key = DBContext.getDBKeyByUserId(1);
        System.out.println(key);
        Assert.assertEquals(key, "db_2");

        key = DBContext.getDBKeyByUserId(2);
        System.out.println(key);
        Assert.assertEquals(key, "db_1");

        key = DBContext.getDBKeyByUserId(3);
        System.out.println(key);
        Assert.assertEquals(key, "db_2");
    }
}
