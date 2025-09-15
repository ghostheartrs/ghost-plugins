package com.ghost.plugins.woodcutting.conditions;

import com.google.inject.Inject;
import com.kraken.api.core.script.node.ConditionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.ghost.plugins.woodcutting.WoodcuttingConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.coords.WorldPoint;

import java.util.List;

@Slf4j
public class AtTreesCondition implements ConditionNode {

    private final WoodcuttingConfig config;
    private final GameObjectService gameObjectService;
    private final Client client;

    @Inject
    public AtTreesCondition(WoodcuttingConfig config, GameObjectService gameObjectService, Client client) {
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

        WorldPoint playerLocation = localPlayer.getWorldLocation();
        List<Integer> targetTreeIds = config.treeType().getTreeIds();

        return !gameObjectService.getGameObjects(o -> targetTreeIds.contains(o.getId()), playerLocation, 15).isEmpty();
    }
}
