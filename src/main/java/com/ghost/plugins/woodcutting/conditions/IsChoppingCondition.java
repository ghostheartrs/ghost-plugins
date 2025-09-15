package com.ghost.plugins.woodcutting.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;

import static com.ghost.plugins.woodcutting.WoodcuttingScript.WC_ANIMATIONS;

@Slf4j
public class IsChoppingCondition implements ConditionNode {

    private final Client client;

    @Inject
    public IsChoppingCondition(Client client) {
        this.client = client;
    }

    @Override
    public boolean checkCondition() {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return false;
        }
        return WC_ANIMATIONS.contains(localPlayer.getAnimation());
    }
}
