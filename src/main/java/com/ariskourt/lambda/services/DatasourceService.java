package com.ariskourt.lambda.services;

import com.ariskourt.lambda.models.Property;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.sql.DataSource;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DatasourceService {

    private static final Lock INIT_LOCK = new ReentrantLock();

    private static DatasourceService instance;

    @Getter
    private final BasicDataSource dataSource;

    private DatasourceService() {
        this(createDatasource());
    }

    /***
     * Static method that allows for accessing the singleton instance
     *
     * @return - A reference to the singleton instance
     */
    public static DatasourceService getInstance() {
        if (instance == null) {
            INIT_LOCK.lock();
            try {
                instance = new DatasourceService();
            } finally {
                INIT_LOCK.unlock();
            }
        }
        return instance;
    }

    private static BasicDataSource createDatasource() {
        var datasource = new BasicDataSource();
        datasource.setUrl(PropertyService.getInstance().getRequiredProperty(Property.DATASOURCE_URL));
        datasource.setUsername(PropertyService.getInstance().getRequiredProperty(Property.DATASOURCE_USERNAME));
        datasource.setPassword(PropertyService.getInstance().getRequiredProperty(Property.DATASOURCE_PASSWORD));
        datasource.setMinIdle(PropertyService.getInstance().getIntegerProperty(Property.DATASOURCE_MIN_IDLE_CONNECTIONS, 5));
        datasource.setMaxTotal(PropertyService.getInstance().getIntegerProperty(Property.DATASOURCE_MAX_IDLE_CONNECTION, 10));
        datasource.setMaxOpenPreparedStatements(PropertyService.getInstance().getIntegerProperty(Property.DATASOURCE_MAX_OPEN_STATEMENTS, 10));
        datasource.setPoolPreparedStatements(true);
        return datasource;
    }

}
