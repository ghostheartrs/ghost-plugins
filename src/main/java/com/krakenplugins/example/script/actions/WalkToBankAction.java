package com.krakenplugins.example.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.movement.MovementService;
import com.kraken.api.interaction.movement.MovementState;
import com.kraken.api.interaction.player.PlayerService;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import static com.krakenplugins.example.script.MiningScript.BANK_AREA;
import static com.krakenplugins.example.script.MiningScript.MINING_AREA;

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
        context.setStatus("Walking to Bank...");
        if(movementService.getCurrentState() == MovementState.ARRIVED || playerService.isInArea(BANK_AREA)) {
            return BehaviorResult.SUCCESS;
        }

        if(movementService.getCurrentState() == MovementState.WALKING && !playerService.isMoving()) {
            log.info("Walking to Bank...");
            movementService.walkTo(BANK_AREA);
            return BehaviorResult.RUNNING;
        }

        log.info("Initial walk to bank area at X={}, Y={}", MINING_AREA.getX(), MINING_AREA.getY());
        if(movementService.walkTo(BANK_AREA)) {
            context.setWalking(true);
            return BehaviorResult.RUNNING;
        } else {
            return BehaviorResult.FAILURE;
        }
    }
}