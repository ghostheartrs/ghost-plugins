package com.ghost.plugins.gemcrab.script;

import com.google.inject.Singleton;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.NPC;

@Getter
@Setter
@Singleton
public class ScriptContext {
    private NPC targetCrab = null;
    private int crabsKilled = 0;
    private String status = "";
    private String runtime = "";
}
