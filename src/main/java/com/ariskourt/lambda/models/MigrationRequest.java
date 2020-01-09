package com.ariskourt.lambda.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/***
 * Simple class that represents a request to the lambda, triggering the migration
 */
@NoArgsConstructor
@Getter
@Setter
public class MigrationRequest {

    private String bucketName;

}
