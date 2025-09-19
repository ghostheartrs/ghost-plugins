package com.ghost.plugins.woodcutting.conditions;

import com.ghost.plugins.woodcutting.WoodcuttingConfig;
import com.ghost.plugins.woodcutting.script.ScriptContext;
import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;

@Slf4j
public class IsOutOfAreaCondition implements ConditionNode {

    private final Client client;
    private final ScriptContext context;
    private final WoodcuttingConfig config;

    @Inject
    public IsOutOfAreaCondition(Client client, ScriptContext context, WoodcuttingConfig config) {
        this.client = client;
        this.context = context;
        this.config = config;
    }

    @Override
    public boolean checkCondition() {
        if (config.wanderDistance() <= 0 || context.getStartLocation() == null) {
            return false;
        }

        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return false;
        }

        return localPlayer.getWorldLocation().distanceTo(context.getStartLocation()) > config.wanderDistance();
    }
}