package com.krakenplugins.ghost.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.gameobject.GameObjectService;
import com.kraken.api.interaction.movement.MovementService;
import com.krakenplugins.ghost.woodcutting.WoodcuttingConfig;
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

        List<Integer> targetTreeIds = config.treeType().getTreeIds();

        Predicate<GameObject> treePredicate = o -> {
            if (!targetTreeIds.contains(o.getId())) {
                return false;
            }
            ObjectComposition comp = gameObjectService.convertToObjectComposition(o);
            return comp != null && gameObjectService.hasAction(comp, "Chop down");
        };

        GameObject nearestTree = gameObjectService.getGameObjects(treePredicate, localPlayer.getWorldLocation(), 40)
                .stream()
                .findFirst()
                .orElse(null);

        if (nearestTree == null) {
            log.info("No trees found nearby.");
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
