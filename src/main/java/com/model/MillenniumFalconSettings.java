package com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class MillenniumFalconSettings {
    private int autonomy;
    private String departure;
    private String arrival;
    @JsonProperty("routes_db")
    private String routesDB;
}
