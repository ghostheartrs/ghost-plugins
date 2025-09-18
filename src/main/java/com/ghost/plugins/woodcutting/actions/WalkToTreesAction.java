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
import net.runelite.api.ObjectComposition;
import net.runelite.api.Player;

import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class WalkToTreesAction implements ActionNode {

    private final WoodcuttingConfig config;
    private final MovementService movementService;
    private final GameObjectService gameObjectService;
    private final Client client;

    @Inject
    public WalkToTreesAction(WoodcuttingConfig config, MovementService movementService, GameObjectService gameObjectService, Client client) {
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

        // Use the same high-level finder method to locate the nearest tree to walk to.
        GameObject nearestTree = gameObjectService.findReachableObject(
                config.treeType().getName(),
                true, // Exact name match
                40,   // Distance
                localPlayer.getWorldLocation(),
                true, // Check for an action
                "Chop down"
        );

        if (nearestTree == null) {
            log.info("No trees found to walk to.");
            return BehaviorResult.FAILURE;
        }

        log.info("Walking back to the trees.");
        if (movementService.walkTo(nearestTree.getWorldLocation())) {
            return BehaviorResult.RUNNING;
        }

        log.warn("Failed to generate a path back to the trees.");
        return BehaviorResult.FAILURE;
    }
}
