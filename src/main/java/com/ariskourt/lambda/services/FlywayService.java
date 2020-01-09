package com.ariskourt.lambda.services;

import com.ariskourt.lambda.models.Property;
import com.ariskourt.lambda.utils.FunctionalLocker;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FlywayService {

    private static final FunctionalLocker LOCKER = FunctionalLocker.create();

    private static FlywayService instance;

    private final PropertyService propertyService;
    private final DatasourceService datasourceService;

    @Getter
    private final Flyway flyway;

    private FlywayService() {
        this.propertyService = PropertyService.getInstance();
        this.datasourceService = DatasourceService.getInstance();
        this.flyway = new Flyway(Flyway
            .configure()
            .dataSource(datasourceService.getDataSource())
            .connectRetries(propertyService.getIntegerProperty(Property.FLYWAY_CONNECT_RETRIES, 10))
            .locations(getLocationPath()));
    }

    public static FlywayService getInstance() {
        if (instance == null) {
            LOCKER.doLocked(() -> instance = new FlywayService());
        }
        return instance;
    }

    private static String getLocationPath() {
        return "filesystem:" + System.getProperty("java.io.tmpdir") + "/";
    }

}
