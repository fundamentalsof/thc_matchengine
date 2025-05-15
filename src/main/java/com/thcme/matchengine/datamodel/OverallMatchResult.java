package com.thcme.matchengine.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverallMatchResult {
    private List<MatchResultPerOrderKey> matchedPositions;
}
