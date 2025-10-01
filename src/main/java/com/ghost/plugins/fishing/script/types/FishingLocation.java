package com.ghost.plugins.fishing.script.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;

@Getter
@RequiredArgsConstructor
public enum FishingLocation {
    DRAYNOR_VILLAGE("Draynor Village", new WorldPoint(3086, 3232, 0), new WorldPoint(3094, 3422, 0)),
    BARBARIAN_VILLAGE("Barbarian Village", new WorldPoint(3105, 3433, 0), new WorldPoint(3094, 3422, 0));

    private final String name;
    private final WorldPoint fishingSpot;
    private final WorldPoint bank;

    @Override
    public String toString() {
        return name;
    }
}