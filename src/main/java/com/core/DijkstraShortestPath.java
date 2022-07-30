package com.core;

import com.model.Planet;
import com.model.UniverseMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * Used as a heuristic in the A*-like algorithm.
 */
public class DijkstraShortestPath {

    // delete all nodes with more length than fuel tank
    // run dijkstra
    // calculate refueling on shortest path
    private Map<String, Planet> planetMap = new HashMap<>();

    public DijkstraShortestPath(UniverseMap universeMap) {
        initPlanets(universeMap);
    }

    private void initPlanets(UniverseMap universeMap) {
        universeMap.getPlanets().forEach(this::makePlanet);
    }

    private void makePlanet(Pair<String, String> route, int weight) {
        var startPlanet = planetMap.getOrDefault(route.getLeft(), new Planet(route.getLeft()));
        var destinationPlanet = planetMap.getOrDefault(route.getRight(), new Planet(route.getRight()));
        startPlanet.addAdjacentPlanet(destinationPlanet, weight);
//        destinationPlanet.addAdjacentPlanet(startPlanet, weight);
        this.planetMap.put(route.getLeft(), startPlanet);
        this.planetMap.put(route.getRight(), destinationPlanet);
    }

    public int calculateShortestPath(String source, String  destination) {
        planetMap.get(source).setHeuristicDistance(0);
        Set<Planet> settledPlanets = new HashSet<>();
        Queue<Planet> unsettledPlanets = new PriorityQueue<>(List.of(planetMap.get(source)));
        while (!unsettledPlanets.isEmpty()) {
            Planet currentPlanet = unsettledPlanets.poll();
            currentPlanet.getAdjacentPlanets().entrySet().stream()
                    .filter(entry -> !settledPlanets.contains(entry.getKey()))
                    .forEach(entry -> {
                        evaluateDistanceAndPath(entry.getKey(), entry.getValue(), currentPlanet);
                        unsettledPlanets.add(entry.getKey());
                    });
            settledPlanets.add(currentPlanet);
        }
        return planetMap.get(destination).getHeuristicDistance();
    }

    private void evaluateDistanceAndPath(Planet adjacentPlanet, Integer distance, Planet sourcePlanet) {
        int newDistance = sourcePlanet.getHeuristicDistance() + distance;
        if (newDistance < adjacentPlanet.getHeuristicDistance()) {
            adjacentPlanet.setHeuristicDistance(newDistance);
        }
    }
}
