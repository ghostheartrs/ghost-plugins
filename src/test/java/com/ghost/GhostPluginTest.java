package com.ghost;

import com.ghost.plugins.gemcrab.GemCrabPlugin;
import com.ghost.plugins.mining.MiningPlugin;
import com.ghost.plugins.woodcutting.WoodcuttingPlugin;
import com.ghost.plugins.fuckentoa.FuckenToaPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class GhostPluginTest {
    public static void main(final String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(FuckenToaPlugin.class, MiningPlugin.class, GemCrabPlugin.class, WoodcuttingPlugin.class);
        RuneLite.main(args);
    }
}
