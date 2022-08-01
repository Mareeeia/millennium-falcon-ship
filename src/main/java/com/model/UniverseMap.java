package com.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;

@Getter
@AllArgsConstructor
public class UniverseMap {
    private final Map<Pair<String, String>, Integer> planets;
}
