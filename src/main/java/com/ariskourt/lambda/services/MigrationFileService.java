package com.ariskourt.lambda.services;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.ariskourt.lambda.clients.S3Client;
import com.ariskourt.lambda.exceptions.MigrationFileFetchingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.file.Paths;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MigrationFileService {

    private static final Lock INIT_LOCK = new ReentrantLock();

    private static MigrationFileService instance;

    private final S3Client client;
    private final TransferManager transferManager;

    private MigrationFileService() {
	this.client = S3Client.getInstance();
	this.transferManager = TransferManagerBuilder
	    .standard()
	    .withS3Client(client.getClient())
	    .build();
    }

    public static MigrationFileService getInstance() {
        if (instance == null) {
            INIT_LOCK.lock();
            try {
		instance = new MigrationFileService();
	    } finally {
                INIT_LOCK.unlock();
	    }
	}
        return instance;
    }

    public void downloadFrom(String bucketName) {
        var location = downloadLocation();
        log.info("Fetching migration files from bucket {} saving under {}", bucketName, location.getAbsolutePath());
	try {
	    transferManager
		.downloadDirectory(bucketName, null , location)
		.waitForCompletion();
	} catch (InterruptedException e) {
	    log.error("Fetching contents of bucket {} has failed. Client was interrupted", bucketName, e);
	    Thread.currentThread().interrupt();
	    throw new MigrationFileFetchingException("S3 client was interrupted while fetching the migration scripts");
	} catch (AmazonClientException e) {
	    log.error("Fetching contents of bucket {} has failed. An client error has occurred {}", bucketName, e);
	    throw new MigrationFileFetchingException("AWS client experienced and error while fetching migration scripts");
	}
    }

    private File downloadLocation() {
        return Paths.get(System.getProperty("java.io.tmpdir")).toFile();
    }

}
