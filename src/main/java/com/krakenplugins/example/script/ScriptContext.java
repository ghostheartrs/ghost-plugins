package com.krakenplugins.example.script;

import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.TileObject;

import java.util.Random;

@Getter
@Setter
@Singleton
public class ScriptContext {
    private boolean isMining = false;
    private boolean isWalking = false;
    private TileObject targetRock = null;
    private long lastActionTime = 0;
    private final Random random = new Random();
}