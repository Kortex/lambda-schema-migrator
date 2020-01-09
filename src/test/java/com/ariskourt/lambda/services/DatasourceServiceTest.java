package com.ariskourt.lambda.services;

import com.ariskourt.lambda.models.Property;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static org.junit.jupiter.api.Assertions.*;

public class DatasourceServiceTest {

    @Rule
    public EnvironmentVariables variables = new EnvironmentVariables();

    @Before
    public void setUp() {
        variables.set(Property.DATASOURCE_URL.name(), "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");
        variables.set(Property.DATASOURCE_USERNAME.name(), "user");
        variables.set(Property.DATASOURCE_PASSWORD.name(), "password");
    }

    @Test
    public void getInstance_WhenCalled_VerifySingletonIsReturned() {
        var service = DatasourceService.getInstance();
        assertEquals(service, DatasourceService.getInstance());
    }

    @Test
    public void getIstance_WheCalled_ConnectionIsRunning() {
        var service = DatasourceService.getInstance();
        assertNotNull(service.getDataSource());
        assertFalse(service.getDataSource().isClosed());
    }

}