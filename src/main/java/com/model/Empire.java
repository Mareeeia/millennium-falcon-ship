package com.model;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class Empire {
    @Builder.Default
    private List<Pair<String, Integer>> bountyHunters = new ArrayList<>();
    private int countdown;
}
