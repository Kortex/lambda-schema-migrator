package com.ariskourt.lambda.services;

import com.ariskourt.lambda.exceptions.MissingPropertyException;
import com.ariskourt.lambda.models.Property;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PropertyServiceTest {

    @Rule
    public final EnvironmentVariables variables = new EnvironmentVariables();

    private static final String STRING_VALUE = "VALUE";
    private static final String DEFAULT_STRING_VALUE = "DEFAULT_VALUE";

    private static final Integer INTEGER_VALUE = 1;
    private static final Integer DEFAULT_INTEGER_VALUE = 2;

    @Test
    public void getProperty_WhenKeyIsPresent_ValueIsReturned() {
        variables.set(Property.DATASOURCE_USERNAME.name(), STRING_VALUE);
        assertEquals(STRING_VALUE, PropertyService.getInstance().getProperty(Property.DATASOURCE_USERNAME, DEFAULT_STRING_VALUE));
    }

    @Test
    public void getProperty_WhenKeyIsNotPresent_DefaultValueIsReturned() {
        assertEquals(DEFAULT_STRING_VALUE, PropertyService.getInstance().getProperty(Property.DATASOURCE_USERNAME, DEFAULT_STRING_VALUE));
    }

    @Test
    public void getRequiredProperty_WhenKeyIsPresent_ValueIsReturned() {
        variables.set(Property.DATASOURCE_USERNAME.name(), STRING_VALUE);
        assertEquals(STRING_VALUE, PropertyService.getInstance().getRequiredProperty(Property.DATASOURCE_USERNAME));
    }

    @Test
    public void getRequiredProperty_WhenKeyIsNotPresent_ExceptionIsThrown() {
        assertThrows(MissingPropertyException.class, () -> PropertyService.getInstance().getRequiredProperty(Property.DATASOURCE_USERNAME));
    }

    @Test
    public void getIntegerProperty_WhenKeyIsPresent_IntegerValueIsReturned() {
        variables.set(Property.DATASOURCE_MAX_IDLE_CONNECTION.name(), String.valueOf(INTEGER_VALUE));
        assertEquals(INTEGER_VALUE, PropertyService.getInstance().getIntegerProperty(Property.DATASOURCE_MAX_IDLE_CONNECTION, DEFAULT_INTEGER_VALUE));
    }

    @Test
    public void getIntegerProperty_WhenKeyIsNotPresent_DefaultIntegerValueIsReturned() {
        assertEquals(DEFAULT_INTEGER_VALUE, PropertyService.getInstance().getIntegerProperty(Property.DATASOURCE_MAX_IDLE_CONNECTION, DEFAULT_INTEGER_VALUE));
    }


}