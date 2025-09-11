package com.krakenplugins.ghost;

import com.krakenplugins.ghost.gemcrab.GemCrabPlugin;
import com.krakenplugins.ghost.mining.MiningPlugin;
import com.krakenplugins.ghost.woodcutting.WoodcuttingPlugin;
import com.krakenplugins.ghost.fuckentoa.FuckenToaPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GhostPluginTest {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(
                MiningPlugin.class,
                WoodcuttingPlugin.class,
                FuckenToaPlugin.class,
                GemCrabPlugin.class
        );
        RuneLite.main(args);
    }
}