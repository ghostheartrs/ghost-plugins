package com.ghost.plugins.woodcutting.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.movement.MovementService;
import com.kraken.api.interaction.movement.MovementState;
import com.ghost.plugins.woodcutting.WoodcuttingConfig;
import lombok.extern.slf4j.Slf4j;

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

        MovementState state = movementService.walkWithState(config.bankLocation().getWorldPoint(), 3);

        switch (state) {
            case ARRIVED:
                log.info("Arrived at bank.");
                return BehaviorResult.SUCCESS;
            case WALKING:
            case BLOCKED:
                return BehaviorResult.RUNNING;
            case FAILED:
            default:
                log.warn("Failed to generate a path to the bank.");
                return BehaviorResult.FAILURE;
        }
    }
}