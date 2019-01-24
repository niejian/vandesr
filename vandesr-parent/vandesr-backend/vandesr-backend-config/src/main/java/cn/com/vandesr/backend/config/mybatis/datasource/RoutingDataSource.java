package cn.com.vandesr.backend.config.mybatis.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

/**
 * 数据源路由
 * @author: nj
 * @date: 2019/1/21:上午11:55
 */
@Component
public class RoutingDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.get();
    }
}
