package com.krakenplugins.ghost;

import com.krakenplugins.ghost.gemcrab.GemCrabPlugin;
import com.krakenplugins.ghost.mining.MiningPlugin;
import com.krakenplugins.ghost.woodcutting.WoodcuttingPlugin;
import com.krakenplugins.ghost.fuckentoa.FuckenToaPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GhostPluginTest {
    public static void main(final String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(FuckenToaPlugin.class, MiningPlugin.class, GemCrabPlugin.class, WoodcuttingPlugin.class);
        RuneLite.main(args);
    }
}
