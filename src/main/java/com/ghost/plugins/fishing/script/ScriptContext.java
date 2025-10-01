package com.ghost.plugins.fishing.script;

import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;

@Getter
@Setter
@Singleton
public class ScriptContext {
    private String status = "Starting...";
    private long startTime = 0;
    private int startXp = 0;
    private int fishCaught = 0;
    private NPC targetFishingSpot;
}