package com.util;

import com.model.UniverseMap;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class is used to retrieve a file from DB.
 *
 * @author w3spoint
 */
public class DBUtilTest {

    @Test
    public void testReadsDBFromFile() {
        String path = DBUtilTest.class.getClassLoader().getResource("universe_maps/universe.db").getPath();
        UniverseMap universeMap = DBUtil.getUniverseMap(path);

        assertThat(universeMap.getPlanets(), equalTo(makeLargerMap().getPlanets()));
    }

    @Test
    public void testWrongPathThrowsRE() {
        var thrown = assertThrows(RuntimeException.class, () -> DBUtil.getUniverseMap("wrong/path.db"));
    }

    private UniverseMap makeLargerMap() {
        return new UniverseMap(Map.of(Pair.of("Tatooine", "Dagobah"), 6,
                Pair.of("Dagobah", "Endor"), 4,
                Pair.of("Dagobah", "Hoth"), 1,
                Pair.of("Hoth", "Endor"), 1,
                Pair.of("Tatooine", "Hoth"), 6));
    }
}