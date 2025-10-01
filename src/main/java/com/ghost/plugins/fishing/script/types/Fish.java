package com.ghost.plugins.fishing.script.types;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.ItemID;

@Getter
@RequiredArgsConstructor
public enum Fish {
    SHRIMP("Shrimp & Anchovies", "Net", ItemID.RAW_SHRIMPS, 1511),
    TROUT("Trout & Salmon", "Bait", ItemID.RAW_TROUT, 1526);

    private final String name;
    private final String action;
    private final int itemID;
    private final int fishingSpotNpcId;

    @Override
    public String toString() {
        return name;
    }
}