package com.ariskourt.lambda.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/***
 * Simple class that represents the response after a migration takes place
 */
@Getter
@Setter
@Builder
@ToString
public class MigrationResult {

    private boolean success;
    private Integer appliedCount;
    private String errorMessage;
    private List<MigrationInfoResource> migrations;

}
