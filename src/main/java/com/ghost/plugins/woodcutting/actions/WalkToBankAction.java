package com.ghost.plugins.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.kraken.api.interaction.movement.MovementService;
import com.ghost.plugins.woodcutting.WoodcuttingConfig;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Player;

@Slf4j
public class WalkToBankAction implements ActionNode {

    private final WoodcuttingConfig config;
    private final MovementService movementService;
    private final GameObjectService gameObjectService;
    private final Client client;

    @Inject
    public WalkToBankAction(WoodcuttingConfig config, MovementService movementService, GameObjectService gameObjectService, Client client) {
        this.config = config;
        this.movementService = movementService;
        this.gameObjectService = gameObjectService;
        this.client = client;
    }

    @Override
    public BehaviorResult performAction() {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null) {
            return BehaviorResult.FAILURE;
        }

        GameObject bankBooth = gameObjectService.findReachableObject("", false, 20, localPlayer.getWorldLocation(), true, "Bank");

        if (bankBooth == null) {
            log.warn("No bank found nearby.");
            return BehaviorResult.FAILURE;
        }

        log.info("Walking to the nearest bank.");
        if (movementService.walkTo(bankBooth.getWorldLocation())) {
            return BehaviorResult.RUNNING;
        }

        log.warn("Failed to generate a path to the bank.");
        return BehaviorResult.FAILURE;
    }
}
