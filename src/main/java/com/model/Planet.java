package com.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * We want to use our planet network in two ways:
 * 1) As navigation map for our OnboardComputer
 * 2) As a heuristic in our A* algorithm - which requires us to obtain distance to destination
 * by running Dijkstra shortest path backwards (so finding the distance from any node to the end node).
 */
@Getter
@Setter
public class Planet implements Comparable<Planet> {

    private final String planetName;
    private Integer heuristicDistance = Integer.MAX_VALUE;
    private Map<Planet, Integer> adjacentPlanets = new HashMap<>();

    public Planet(String planetName) {
        this.planetName = planetName;
    }

    public Planet(Planet planet) {
        this.adjacentPlanets = new HashMap<>(planet.adjacentPlanets);
        this.planetName = planet.planetName;
        this.heuristicDistance = planet.heuristicDistance;
    }

    public void addAdjacentPlanet(Planet planet, int weight) {
        this.adjacentPlanets.put(planet, weight);
    }

    @Override
    public int compareTo(Planet planet) {
        return Integer.compare(this.heuristicDistance, planet.heuristicDistance);
    }

}
