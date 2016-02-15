package com.cnblogs.yjmyzz.main;

import com.cnblogs.yjmyzz.entity.UserEntity;
import com.cnblogs.yjmyzz.service.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by yangjunming on 2/15/16.
 * author: yangjunming@huijiame.com
 */
public class SampleApplication {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring-database.xml");

        UserService service = ctx.getBean(UserService.class);


        // test begin ====>

        for (int i = 1; i < 10; i++) {
            UserEntity user = service.getUser(i);
            if (user != null) {
                System.out.println(user);
            } else {
                user = new UserEntity(i, "User-" + i);
                service.addUser(user);
            }
        }


        // <===== test end


        ctx.close();
    }
}
