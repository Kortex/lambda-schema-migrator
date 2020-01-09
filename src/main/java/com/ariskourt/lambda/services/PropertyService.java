package com.ariskourt.lambda.services;

import com.ariskourt.lambda.exceptions.MissingPropertyException;
import com.ariskourt.lambda.models.Property;
import com.ariskourt.lambda.utils.FunctionalLocker;

import java.util.Optional;

/***
 * A service allowing to access system environment defined properties. Properties should be set using their respective
 * {@link Property} key as a system environment variables.
 */
public class PropertyService {

    private static final FunctionalLocker LOCKER = FunctionalLocker.create();

    private static PropertyService instance;

    private PropertyService() {}

    /***
     * Static method that allows for accessing the singleton instance
     *
     * @return - A reference to the singleton instance
     */
    public static PropertyService getInstance() {
	if (instance == null) {
	    LOCKER.doLocked(() -> instance = new PropertyService());
	}
	return instance;
    }

    /***
     * Looks up the given property key in the system environment, returning the found value if found or
     * defaulting to the user provided default one/
     * @param property - The {@link Property} key to look in the environment
     * @param defaultValue - The default value to return in case no match is found
     * @return - Either the found value or the default one
     */
    public String getProperty(Property property, String defaultValue) {
        return Optional
	    .ofNullable(System.getenv(property.name()))
	    .orElse(defaultValue);
    }

    /***
     * Looks up the given property in the system environment, returning the found value or throwing a
     * {@link MissingPropertyException} in case the key is not found
     * @param property - The {@link Property} key to look in the environment
     * @return - The value specified for the key in question
     */
    public String getRequiredProperty(Property property) {
        return Optional
	    .ofNullable(System.getenv(property.name()))
	    .orElseThrow(() -> new MissingPropertyException("No value found for required property: " + property.name()));
    }

    /***
     * Looks up the given property key in the system environment, returning the found value if found or
     * defaulting to the user provided default one/
     * @param property - The {@link Property} key to look in the environment
     * @param defaultValue - The default value to return in case no match is found
     * @return - Either the found value or the default one
     */
    public Integer getIntegerProperty(Property property, Integer defaultValue) {
        return Optional
	    .ofNullable(System.getenv(property.name()))
	    .map(Integer::parseInt)
	    .orElse(defaultValue);
    }

}
