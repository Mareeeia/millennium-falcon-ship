package com.core;

import com.model.Planet;
import com.model.UniverseMap;
import lombok.Getter;

import java.util.*;

/**
 * Used as a heuristic in the A*-like algorithm.
 */
@Getter
public class DijkstraShortestPath {

    public static Map<String, Planet> mapOutDistancesToDestination(UniverseMap universeMap,
                                                                   String destination) {
        var planetMap = PlanetMapper.makePlanetMap(universeMap);
        planetMap.get(destination).setHeuristicDistance(0);
        Set<Planet> settledPlanets = new HashSet<>();
        Queue<Planet> unsettledPlanets = new PriorityQueue<>(List.of(planetMap.get(destination)));
        while (!unsettledPlanets.isEmpty()) {
            Planet currentPlanet = unsettledPlanets.poll();
            currentPlanet.getBackwardsAdjacent().entrySet().stream() //TODO: think of more elegant backwards
                    .filter(entry -> !settledPlanets.contains(entry.getKey()))
                    .forEach(entry -> {
                        evaluateDistanceAndPath(entry.getKey(), entry.getValue(), currentPlanet);
                        unsettledPlanets.add(entry.getKey());
                    });
            settledPlanets.add(currentPlanet);
        }
        return planetMap;
    }

    public static int calculateShortestPath(UniverseMap universeMap, String source, String destination) {
        var planetMap = mapOutDistancesToDestination(universeMap, destination);
        return planetMap.get(source).getHeuristicDistance();
    }

    private static void evaluateDistanceAndPath(Planet adjacentPlanet, Integer distance, Planet sourcePlanet) {
        int newDistance = sourcePlanet.getHeuristicDistance() + distance;
        if (newDistance < adjacentPlanet.getHeuristicDistance()) {
            adjacentPlanet.setHeuristicDistance(newDistance);
        }
    }
}
