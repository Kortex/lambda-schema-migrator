package com.ariskourt.lambda.services;

import com.ariskourt.lambda.converters.MigrationInfoConverter;
import com.ariskourt.lambda.models.MigrationResult;
import com.ariskourt.lambda.utils.FunctionalLocker;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.api.FlywayException;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class FlywayMigrationService {

    private static final FunctionalLocker LOCKER = FunctionalLocker.create();

    private static FlywayMigrationService instance;

    private final FlywayService flywayService;
    private final MigrationFileService fileService;
    private final MigrationInfoConverter converter;

    private FlywayMigrationService() {
        this(FlywayService.getInstance(),
            MigrationFileService.getInstance(),
            new MigrationInfoConverter());
    }

    public static FlywayMigrationService getInstance() {
        if (instance == null) {
            LOCKER.doLocked(() -> instance = new FlywayMigrationService());
        }
        return instance;
    }

    public MigrationResult migrate(String bucketName) {
        log.info("Attempting schema migration using contents of bucket: {}", bucketName);

        fileService.downloadFrom(bucketName);

        try {
            var applied = flywayService
                .getFlyway()
                .migrate();

            return MigrationResult.builder()
                .appliedCount(applied)
                .success(true)
                .migrations(Arrays
                    .stream(flywayService
                        .getFlyway()
                        .info()
                        .applied())
                    .map(converter)
                    .collect(Collectors.toList()))
                .build();

        } catch (FlywayException e) {
            log.error("An error has occurred while applying a schema migration. Error code was {}", e.getErrorCode(), e);
            return MigrationResult.builder()
                .errorMessage(e.getMessage())
                .success(false)
                .build();
        }
    }

}
