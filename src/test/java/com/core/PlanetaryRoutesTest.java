package com.core;

import com.model.Empire;
import com.model.UniverseMap;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlanetaryRoutesTest {
    @Test
    public void testGetSuccessChanceNoHunters() {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(makeSmallMap());
        PlanetaryRoutes planetaryRoutes = new PlanetaryRoutes(dijkstraShortestPath, Empire.builder().countdown(6).build());
        int path = planetaryRoutes.calculateMissionSuccessChance("Tatooine", "Endor");
        assertEquals(100, path);
    }

    @Test
    public void testGetSuccessChanceShortCountdown() {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(makeLargerMap());
        PlanetaryRoutes planetaryRoutes = new PlanetaryRoutes(dijkstraShortestPath, makeEmpireWithCountdown(7));
        int path = planetaryRoutes.calculateMissionSuccessChance("Tatooine", "Endor");
        assertEquals(0, path);
    }

    @Test
    public void testGetSuccessChanceCountdown8() {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(makeLargerMap());
        PlanetaryRoutes planetaryRoutes = new PlanetaryRoutes(dijkstraShortestPath, makeEmpireWithCountdown(8));
        int path = planetaryRoutes.calculateMissionSuccessChance("Tatooine", "Endor");
        assertEquals(81, path);
    }

    @Test
    public void testGetSuccessChanceCountdown9() {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(makeLargerMap());
        PlanetaryRoutes planetaryRoutes = new PlanetaryRoutes(dijkstraShortestPath, makeEmpireWithCountdown(9));
        int path = planetaryRoutes.calculateMissionSuccessChance("Tatooine", "Endor");
        assertEquals(90, path);
    }

    @Test
    public void testGetSuccessChanceCountdown10() {
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(makeLargerMap());
        PlanetaryRoutes planetaryRoutes = new PlanetaryRoutes(dijkstraShortestPath, makeEmpireWithCountdown(10));
        int path = planetaryRoutes.calculateMissionSuccessChance("Tatooine", "Endor");
        assertEquals(100, path);
    }

    private UniverseMap makeSmallMap() {
        return new UniverseMap(Map.of(Pair.of("Tatooine", "Dagobah"), 4,
                Pair.of("Dagobah", "Endor"), 1));
    }

    private Empire makeEmpireWithCountdown(int countdown) {
        var hunterCoords = List.of(
                Pair.of("Hoth", 6),
                Pair.of("Hoth", 7),
                Pair.of("Hoth", 8)
        );

        return Empire.builder().bountyHunters(hunterCoords).countdown(countdown).build();
    }

    private UniverseMap makeLargerMap() {
        return  new UniverseMap (Map.of(Pair.of("Tatooine", "Dagobah"), 6,
                Pair.of("Dagobah", "Endor"), 4,
                Pair.of("Dagobah", "Hoth"), 1,
                Pair.of("Hoth", "Endor"), 1,
                Pair.of("Tatooine", "Hoth"), 6));
    }
}
