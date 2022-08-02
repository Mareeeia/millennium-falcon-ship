package com.util;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileUtilTest {

    private static final String FALCON = "falcon_config/falcon_initial_config.json";
    private static final String EMPIRE = "empire/basic_empire.json";

    @Test
    public void testReadsFalconFile() throws IOException {
        String path = FileUtilTest.class.getClassLoader().getResource(FALCON).getPath();
        var falcon = FileUtil.getFalconSettings(path);
        assertThat(falcon.getDeparture(), equalTo("Tatooine"));
        assertThat(falcon.getArrival(), equalTo("Endor"));
        assertThat(falcon.getAutonomy(), equalTo(6));
    }

    @Test
    public void testReadsEmpireFile() throws IOException {
        String path = FileUtilTest.class.getClassLoader().getResource(EMPIRE).getPath();
        var empire = FileUtil.getEmpireSettings(path);
        assertThat(empire.getCountdown(), equalTo(8));
        assertThat(empire.getBountyHunters(), hasSize(3));
    }

    @Test
    public void testFalconThrowsNPEs() {
        var thrown = assertThrows(NullPointerException.class, () -> FileUtil.getFalconSettings(null));
        assertEquals("path is marked non-null but is null", thrown.getMessage());
    }

    @Test
    public void testEmptyFalconThrowsREs() {
        var thrown = assertThrows(RuntimeException.class, () -> FileUtil.getFalconSettings(""));
        assertEquals("Path can't be empty.", thrown.getMessage());
    }

    @Test
    public void testEmpireThrowsNPEs() {
        var thrown = assertThrows(NullPointerException.class, () -> FileUtil.getEmpireSettings(null));
        assertEquals("path is marked non-null but is null", thrown.getMessage());
    }

    @Test
    public void testEmptyEmpireThrowsREs() {
        var thrown = assertThrows(RuntimeException.class, () -> FileUtil.getEmpireSettings(""));
        assertEquals("Path can't be empty.", thrown.getMessage());
    }

    @Test
    public void testEmptyDBThrowsNPEs() {
        var thrown = assertThrows(NullPointerException.class, () -> FileUtil.getUniverseMap(null));
        assertEquals("path is marked non-null but is null", thrown.getMessage());
    }

    @Test
    public void testEmptyDBThrowsREs() {
        var thrown = assertThrows(RuntimeException.class, () -> FileUtil.getUniverseMap(""));
        assertEquals("Path can't be empty.", thrown.getMessage());
    }
}
