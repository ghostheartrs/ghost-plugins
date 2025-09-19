package com.ghost.plugins.woodcutting.script;

import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.GameObject;
import net.runelite.api.coords.WorldPoint;

@Getter
@Setter
@Singleton
public class ScriptContext {
    private String status = "Starting...";
    private long startTime = 0;
    private int startXp = 0;
    private int logsCut = 0;
    private GameObject targetTree;
    private WorldPoint startLocation;
}