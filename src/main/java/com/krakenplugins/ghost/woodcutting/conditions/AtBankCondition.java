package com.krakenplugins.ghost.woodcutting.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.krakenplugins.ghost.woodcutting.WoodcuttingConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Player;

@Slf4j
public class AtBankCondition implements ConditionNode {

    private final WoodcuttingConfig config;
    private final GameObjectService gameObjectService;
    private final Client client;

    @Inject
    public AtBankCondition(WoodcuttingConfig config, GameObjectService gameObjectService, Client client) {
        this.config = config;
        this.gameObjectService = gameObjectService;
        this.client = client;
    }

    @Override
    public boolean checkCondition() {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return false;
        }

        GameObject bankBooth = gameObjectService.findReachableObject("", false, 10, localPlayer.getWorldLocation(), true, "Bank");
        return bankBooth != null;
    }
}
