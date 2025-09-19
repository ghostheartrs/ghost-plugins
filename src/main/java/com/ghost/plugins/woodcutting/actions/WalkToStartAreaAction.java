package com.ghost.plugins.woodcutting.actions;

import com.ghost.plugins.woodcutting.script.ScriptContext;
import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.movement.MovementService;
import com.kraken.api.interaction.movement.MovementState;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;

@Slf4j
public class WalkToStartAreaAction implements ActionNode {

    private final MovementService movementService;
    private final ScriptContext context;
    private final Client client;

    @Inject
    public WalkToStartAreaAction(MovementService movementService, ScriptContext context, Client client) {
        this.movementService = movementService;
        this.context = context;
        this.client = client;
    }

    @Override
    public BehaviorResult performAction() {
        Player localPlayer = client.getLocalPlayer();
        if (localPlayer == null || context.getStartLocation() == null) {
            return BehaviorResult.FAILURE;
        }
        
        if (localPlayer.getWorldLocation().distanceTo(context.getStartLocation()) <= 3) {
            context.setStatus("Returned to start area.");
            movementService.resetPath();
            return BehaviorResult.SUCCESS;
        }

        context.setStatus("Walking back to start");
        log.info("Player has wandered too far, walking back to start area.");

        if (movementService.walkTo(context.getStartLocation())) {
            return BehaviorResult.RUNNING;
        }

        log.warn("Failed to generate a path back to the start area.");
        return BehaviorResult.FAILURE;
    }
}