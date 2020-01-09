package com.ariskourt.lambda.clients;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.ariskourt.lambda.models.Property;
import com.ariskourt.lambda.services.PropertyService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/***
 * Simple class implementing the Supplier interface, returning a configured {@link AmazonS3} client object
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class S3Client {

    private static final Lock INIT_LOCK = new ReentrantLock();

    private static S3Client instance;

    @Getter
    private final AmazonS3 client;

    private S3Client() {
	this(AmazonS3ClientBuilder.standard()
	.withCredentials(new AWSStaticCredentialsProvider(awsCredentials()))
	.withRegion(awsRegion())
	.build());
    }

    public static S3Client getInstance() {
	if (instance == null) {
	    INIT_LOCK.lock();
	    try {
		instance = new S3Client();
	    } finally {
		INIT_LOCK.unlock();
	    }
	}
	return instance;
    }

    private static AWSCredentials awsCredentials() {
        return new BasicAWSCredentials(
	    PropertyService.getInstance().getRequiredProperty(Property.AWS_ACCESS_KEY),
	    PropertyService.getInstance().getRequiredProperty(Property.AWS_ACCESS_SECRET)
	);
    }

    private static String awsRegion() {
        return PropertyService
	    .getInstance()
	    .getProperty(Property.AWS_BUCKET_REGION, "us-east-2");
    }

}
