package com.ariskourt.lambda.models;

import lombok.*;

import java.util.Date;

/***
 * Simple model class that represents information regarding a schema migration that took place
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MigrationInfoResource {

    private String version;
    private String description;
    private String usedScript;
    private String state;
    private Date installedOn;
    private Integer executionTime;

}
