package com.ghost.plugins.woodcutting.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.coords.WorldPoint;

@Getter
@RequiredArgsConstructor
public enum BankLocation {
    VARROCK_EAST("Varrock East", new WorldPoint(3253, 3420, 0)),
    VARROCK_WEST("Varrock West", new WorldPoint(3185, 3436, 0)),
    DRAYNOR_VILLAGE("Draynor Village", new WorldPoint(3094, 3244, 0)),
    LUMBRIDGE_CASTLE("Lumbridge Castle", new WorldPoint(3208, 3220, 2));

    private final String name;
    private final WorldPoint worldPoint;

    @Override
    public String toString() {
        return name;
    }
}