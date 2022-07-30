package com.model;

import com.core.PlanetaryRoutes;
import lombok.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Planet implements Comparable<Planet> {

    private final String name;
    protected Integer heuristicDistance = Integer.MAX_VALUE;
    private Map<Planet, Integer> adjacentPlanets = new HashMap<>();

    public void addAdjacentPlanet(Planet planet, int weight) {
        this.adjacentPlanets.put(planet, weight);
    }

    @Override
    public int compareTo(Planet planet) {
        return Integer.compare(this.heuristicDistance, planet.heuristicDistance);
    }

}
