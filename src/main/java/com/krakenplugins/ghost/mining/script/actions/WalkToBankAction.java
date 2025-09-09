package com.krakenplugins.ghost.mining.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.movement.MovementService;
import com.kraken.api.interaction.movement.MovementState;
import com.kraken.api.interaction.player.PlayerService;
import com.krakenplugins.ghost.mining.script.BaseScriptNode;
import com.krakenplugins.ghost.mining.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import static com.krakenplugins.ghost.mining.script.MiningScript.BANK_AREA;

@Slf4j
public class WalkToBankAction extends BaseScriptNode implements ActionNode {

    private final MovementService movementService;
    private final PlayerService playerService;

    @Inject
    public WalkToBankAction(Client client, ScriptContext context, MovementService movementService, PlayerService playerService) {
        super(client, context);
        this.movementService = movementService;
        this.playerService = playerService;
    }

    @Override
    public BehaviorResult performAction() {
        if(context.getTargetRock() != null) {
            context.setTargetRock(null);
        }

        if(playerService.isInArea(BANK_AREA)) {
            movementService.resetPath();
            return BehaviorResult.SUCCESS;
        }

        if(movementService.getCurrentState() == MovementState.ARRIVED) {
            movementService.resetPath();
            return BehaviorResult.SUCCESS;
        }

        boolean isWalking = movementService.getCurrentState() == MovementState.WALKING;
        if((isWalking && movementService.getMovementStats().getDistanceToNextWaypoint() < 5) || (!playerService.isMoving() && isWalking)) {
            movementService.walkTo(BANK_AREA);
            return BehaviorResult.RUNNING;
        }

        if(movementService.walkTo(BANK_AREA)) {
            return BehaviorResult.RUNNING;
        } else {
            return BehaviorResult.FAILURE;
        }
    }
}