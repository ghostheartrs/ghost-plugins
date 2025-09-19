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

    @Inject
    public WalkToBankAction(WoodcuttingConfig config, MovementService movementService) {
        this.config = config;
        this.movementService = movementService;
    }

    @Override
    public BehaviorResult performAction() {
        log.info("Walking to {} bank.", config.bankLocation().getName());
        if (movementService.walkTo(config.bankLocation().getWorldPoint())) {
            return BehaviorResult.RUNNING;
        }
        log.warn("Failed to generate a path to the bank.");
        return BehaviorResult.FAILURE;
    }
}