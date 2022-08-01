package com.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Jacksonized
public class Empire {
    private int countdown;
    @Builder.Default
    @JsonProperty("bounty_hunters")
    private List<BountyHunter> bountyHunters = new ArrayList<>();
}
