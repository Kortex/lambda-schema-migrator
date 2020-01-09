package com.ariskourt.lambda.converters;

import com.ariskourt.lambda.models.MigrationInfoResource;
import org.flywaydb.core.api.MigrationInfo;

import java.util.function.Function;

/***
 * Converter class responsible for creating a response based on the {@link MigrationInfo} object provided by FlyWay
 */
public class MigrationInfoConverter implements Function<MigrationInfo, MigrationInfoResource> {

    @Override
    public MigrationInfoResource apply(MigrationInfo migrationInfo) {
	return MigrationInfoResource
	    .builder()
	    .description(migrationInfo.getDescription())
	    .installedOn(migrationInfo.getInstalledOn())
	    .state(migrationInfo.getState().getDisplayName())
	    .executionTime(migrationInfo.getExecutionTime())
	    .usedScript(migrationInfo.getScript())
	    .version(migrationInfo.getVersion().getVersion())
	    .build();
    }

}
