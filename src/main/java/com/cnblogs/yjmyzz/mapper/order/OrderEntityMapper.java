package com.cnblogs.yjmyzz.mapper.order;

import com.cnblogs.yjmyzz.entity.OrderEntity;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;

/**
 * auto switch to db_main by annotation
 */
@Resource(name = "orderScannerConfigurer")
public interface OrderEntityMapper extends Mapper<OrderEntity> {
}