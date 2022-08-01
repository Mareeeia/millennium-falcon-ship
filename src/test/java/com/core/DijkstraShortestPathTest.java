package com.core;

import com.model.UniverseMap;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DijkstraShortestPathTest {


    @Test
    public void getShortestPathSmallMap() {
        int path = DijkstraShortestPath.calculateShortestPath(makeSmallMap(), "Tatooine", "Endor");
        assertEquals(5, path);
    }

    @Test
    public void getShortestPathLargerMap() {
        int path = DijkstraShortestPath.calculateShortestPath(makeLargerMap(), "Tatooine", "Endor");
        assertEquals(7, path);
    }

    private UniverseMap makeSmallMap() {
        return new UniverseMap(Map.of(Pair.of("Tatooine", "Dagobah"), 4,
                Pair.of("Dagobah", "Endor"), 1));
    }

    private UniverseMap makeLargerMap() {
        return new UniverseMap(Map.of(Pair.of("Tatooine", "Dagobah"), 6,
                Pair.of("Dagobah", "Endor"), 4,
                Pair.of("Dagobah", "Hoth"), 1,
                Pair.of("Hoth", "Endor"), 1,
                Pair.of("Tatooine", "Hoth"), 6));
    }
}
