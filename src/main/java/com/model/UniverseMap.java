package com.model;

import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

@Data
public class UniverseMap {

    private final Map<Pair<String, String>, Integer> planets;

    public int getDistance(String planet1, String planet2) {
        return this.planets.get(Pair.of(planet1, planet2));
    }

}
