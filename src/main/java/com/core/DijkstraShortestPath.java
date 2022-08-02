package com.core;

import com.model.Planet;
import com.model.UniverseMap;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Used as a heuristic in the A* algorithm.
 */
@Component
@EnableAutoConfiguration
public class DijkstraShortestPath {

    private final PlanetMapper planetMapper;

    public DijkstraShortestPath(PlanetMapper planetMapper) {
        this.planetMapper = planetMapper;
    }

    public Map<String, Planet> mapOutDistancesToDestination(UniverseMap universeMap,
                                                            String destination) {
        var planetMap = this.planetMapper.makePlanetMap(universeMap);
        planetMap.get(destination).setHeuristicDistance(0);
        Set<Planet> settledPlanets = new HashSet<>();
        Queue<Planet> unsettledPlanets = new PriorityQueue<>(List.of(planetMap.get(destination)));
        while (!unsettledPlanets.isEmpty()) {
            Planet currentPlanet = unsettledPlanets.poll();
            // We use Dijkstra to get the distance from every planet to the destination
            currentPlanet.getAdjacentPlanets().entrySet().stream()
                    .filter(entry -> !settledPlanets.contains(entry.getKey()))
                    .forEach(entry -> {
                        evaluateDistanceAndPath(entry.getKey(), entry.getValue(), currentPlanet);
                        unsettledPlanets.add(entry.getKey());
                    });
            settledPlanets.add(currentPlanet);
        }
        return planetMap;
    }

    public int calculateShortestPath(UniverseMap universeMap, String source, String destination) {
        var planetMap = mapOutDistancesToDestination(universeMap, destination);
        return planetMap.get(source).getHeuristicDistance();
    }

    private void evaluateDistanceAndPath(Planet adjacentPlanet, Integer distance, Planet sourcePlanet) {
        int newDistance = sourcePlanet.getHeuristicDistance() + distance;
        if (newDistance < adjacentPlanet.getHeuristicDistance()) {
            adjacentPlanet.setHeuristicDistance(newDistance);
        }
    }
}
