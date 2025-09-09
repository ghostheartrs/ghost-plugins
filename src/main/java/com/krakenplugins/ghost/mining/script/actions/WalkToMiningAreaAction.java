package com.krakenplugins.example.mining.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.inventory.InventoryService;
import com.kraken.api.interaction.movement.MovementService;
import com.kraken.api.interaction.movement.MovementState;
import com.kraken.api.interaction.player.PlayerService;
import com.krakenplugins.example.mining.script.BaseScriptNode;
import com.krakenplugins.example.mining.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import static com.krakenplugins.example.mining.script.MiningScript.MINING_AREA;

@Slf4j
public class WalkToMiningAreaAction extends BaseScriptNode implements ActionNode {

    private final MovementService movementService;
    private final PlayerService playerService;
    private final InventoryService inventoryService;

    @Inject
    public WalkToMiningAreaAction(Client client, ScriptContext context, MovementService movementService, PlayerService playerService, InventoryService inventoryService) {
        super(client, context);
        this.movementService = movementService;
        this.playerService = playerService;
        this.inventoryService = inventoryService;
    }

    @Override
    public BehaviorResult performAction() {
        // Do not attempt to walk to the mining area with a full inventory.
        if(inventoryService.all().size() >= 28) {
            return BehaviorResult.FAILURE;
        }

        context.setStatus("Walking to Mine...");
        if(movementService.getCurrentState() == MovementState.ARRIVED || playerService.isInArea(MINING_AREA)) {
            movementService.resetPath();
            return BehaviorResult.SUCCESS;
        }

        // Continue executing the movement towards the destination if we have started the path and we aren't currently moving
        boolean isWalking = movementService.getCurrentState() == MovementState.WALKING;
        if((isWalking && movementService.getMovementStats().getDistanceToNextWaypoint() < 5) || (!playerService.isMoving() && isWalking)) {
            movementService.walkTo(MINING_AREA);
            return BehaviorResult.RUNNING;
        }

        if(movementService.walkTo(MINING_AREA)) {
            return BehaviorResult.RUNNING;
        } else {
            return BehaviorResult.FAILURE;
        }
    }
}