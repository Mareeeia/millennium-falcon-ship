package com.model;

import com.core.PlanetaryRoutes;
import lombok.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Planet implements Comparable<Planet> {

    private final String name;
    protected Integer heuristicDistance = Integer.MAX_VALUE;
    private Map<Planet, Integer> adjacentPlanets = new HashMap<>();

    private Map<Planet, Integer> backwardsAdjacent = new HashMap<>();

    public Planet(String name) {
        this.name = name;
    }

    public Planet(Planet planet) {
        this.adjacentPlanets = planet.adjacentPlanets;
        this.name = planet.getName();
        this.heuristicDistance = planet.heuristicDistance;
    }

    public void addAdjacentPlanet(Planet planet, int weight) {
        this.adjacentPlanets.put(planet, weight);
    }

    public void addBackwardsAdjacentPlanet(Planet planet, int weight) {
        this.backwardsAdjacent.put(planet, weight);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Planet planet) {
        return Integer.compare(this.heuristicDistance, planet.heuristicDistance);
    }

}
