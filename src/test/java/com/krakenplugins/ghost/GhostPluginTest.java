package com.krakenplugins.ghost;

import com.krakenplugins.ghost.mining.MiningPlugin;
import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class ExamplePluginTest {
    public static void main(String[] args) throws Exception {
        ExternalPluginManager.loadBuiltin(MiningPlugin.class);
        RuneLite.main(args);
    }
}