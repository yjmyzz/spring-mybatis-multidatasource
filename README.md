## Spring - mybatis - multi - datasource sample

This project illustrate how to use multiple datasource with mybatis and spring.

---
##### prepare

firstly, you must create table "T_USER" in two mysql databases: db_1 and db_2

```sql 
CREATE TABLE T_USER
(
    D_USER_ID INT(11) PRIMARY KEY NOT NULL,
    D_USER_NAME VARCHAR(100) DEFAULT ''
);
```

and create table "T_ORDER"" in mysql database: db_main  

```sql
CREATE TABLE T_ORDER
(
    D_ORDER_ID INT(11) PRIMARY KEY NOT NULL,
    D_ORDER_NO VARCHAR(50) DEFAULT ''
);
```

and the jdbc properties(src/main/resources/properties/jdbc.properties) is follow:

```ini
jdbc-driver=com.mysql.jdbc.Driver

jdbc-key-1=db_1
jdbc-url-1=jdbc:mysql://default:3306/db_1?useUnicode=true&characterEncoding=utf8
jdbc-user-1=test
jdbc-password-1=123456

jdbc-key-2=db_2
jdbc-url-2=jdbc:mysql://default:3306/db_2?useUnicode=true&characterEncoding=utf8
jdbc-user-2=test
jdbc-password-2=123456

jdbc-key-main=db_main
jdbc-url-main=jdbc:mysql://default:3306/db_main?useUnicode=true&characterEncoding=utf8
jdbc-user-main=test
jdbc-password-main=123456
```
 

###### key config:
**src/main/resources/spring-database.xml**  

```xml
    ...
    
    <bean id="parentDataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init"
          destroy-method="close">
        <property name="driverClassName" value="${jdbc-driver}"/>
        <property name="url" value="${jdbc-url-1}"/>
        <property name="username" value="${jdbc-user-1}"/>
        <property name="password" value="${jdbc-password-1}"/>
        <property name="filters" value="stat"/>
        <property name="maxActive" value="20"/>
        <property name="initialSize" value="1"/>
        <property name="maxWait" value="60000"/>
        <property name="minIdle" value="1"/>
        <property name="timeBetweenEvictionRunsMillis" value="3000"/>
        <property name="minEvictableIdleTimeMillis" value="300000"/>
        <property name="validationQuery" value="SELECT 'x'"/>
        <property name="testWhileIdle" value="true"/>
        <property name="testOnBorrow" value="false"/>
        <property name="testOnReturn" value="false"/>
        <property name="poolPreparedStatements" value="true"/>
        <property name="maxPoolPreparedStatementPerConnectionSize" value="20"/>
        <property name="connectionInitSqls" value="set names utf8mb4;"/>
    </bean>

    <bean id="dataSource1" parent="parentDataSource">
        <property name="url" value="${jdbc-url-1}"/>
        <property name="username" value="${jdbc-user-1}"/>
        <property name="password" value="${jdbc-password-1}"/>
    </bean>

    <bean id="dataSource2" parent="parentDataSource">
        <property name="url" value="${jdbc-url-2}"/>
        <property name="username" value="${jdbc-user-2}"/>
        <property name="password" value="${jdbc-password-2}"/>
    </bean>

    <bean id="dataSourceMain" parent="parentDataSource">
        <property name="url" value="${jdbc-url-main}"/>
        <property name="username" value="${jdbc-user-main}"/>
        <property name="password" value="${jdbc-password-main}"/>
    </bean>

    <!-- method 1:  config switch routing db -->
    <bean id="dataSource" class="com.cnblogs.yjmyzz.utils.RoutingDataSource">
        <property name="targetDataSources">
            <map key-type="java.lang.String">
                <entry key="${jdbc-key-1}" value-ref="dataSource1"/>
                <entry key="${jdbc-key-2}" value-ref="dataSource2"/>
                <entry key="${jdbc-key-main}" value-ref="dataSourceMain"/>
            </map>
        </property>
    </bean>

    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="configLocation" value="classpath:mybatis-config.xml"></property>
        <property name="dataSource" ref="dataSource"/>
        <property name="mapperLocations">
            <array>
                <value>classpath:mybatis/*.xml</value>
            </array>
        </property>
    </bean>

    <bean id="userScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.cnblogs.yjmyzz.mapper.user"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
    </bean>

    <!-- method 2: config annotation auto switch-->
    <bean id="sqlSessionFactoryMain" class="org.mybatis.spring.SqlSessionFactoryBean">
        <property name="configLocation" value="classpath:mybatis-config.xml"></property>
        <property name="dataSource" ref="dataSourceMain"/>
        <property name="mapperLocations">
            <array>
                <value>classpath:mybatis/*.xml</value>
            </array>
        </property>
    </bean>

    <bean id="orderScannerConfigurer" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.cnblogs.yjmyzz.mapper.order"/>
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactoryMain"/>
    </bean>
    ...
    
```

##### key code:
**src/main/java/com/cnblogs/yjmyzz/utils/DBContext.java**  


```java
package com.cnblogs.yjmyzz.utils;

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

```

**src/main/java/com/cnblogs/yjmyzz/utils/RoutingDataSource.java**  


```java
package com.cnblogs.yjmyzz.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {

        return DBContext.getDBKey();
    }


}

```
**src/main/java/com/cnblogs/yjmyzz/mapper/order/OrderEntityMapper.java**  

if you don't like switch db by coding manually , you can use annotation like this :  


```java
/**
 * auto switch to db_main by annotation
 */
@Resource(name = "orderScannerConfigurer")
public interface OrderEntityMapper extends Mapper<OrderEntity> {
}
```


**src/main/java/com/cnblogs/yjmyzz/service/impl/UserServiceImpl.java**

```java

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    UserEntityMapper userEntityMapper;

    @Autowired
    OrderEntityMapper orderEntityMapper;


    @Override
    public void addUser(UserEntity userEntity) {
        //switch db
        DBContext.setDBKey(DBContext.getDBKeyByUserId(userEntity.getUserId()));
        userEntityMapper.insertSelective(userEntity);
    }

    @Override
    public UserEntity getUser(int userId) {
        //switch db
        DBContext.setDBKey(DBContext.getDBKeyByUserId(userId));
        return userEntityMapper.selectByPrimaryKey(userId);
    }

    @Override
    public void addOrder(OrderEntity orderEntity) {
        //since orderEntityMapper can auto switch db by annotation
        //so we don't need to switch db manually
        orderEntityMapper.insertSelective(orderEntity);
    }

    @Override
    public OrderEntity getOrder(int orderId) {
        //since orderEntityMapper can auto switch db by annotation
        //so we don't need to switch db manually
        return orderEntityMapper.selectByPrimaryKey(orderId);
    }

}
```


##### build&run:
this project use gradle to build and deploy. you just need to input the following command in terminal window: 

``` 
gradle run
```


when the application run complete. you can check the database.
