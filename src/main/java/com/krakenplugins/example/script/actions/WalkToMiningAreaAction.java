package com.krakenplugins.example.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.SleepService;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.movement.MovementService;
import com.kraken.api.interaction.movement.MovementState;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import static com.krakenplugins.example.script.MiningScript.MINING_AREA;

@Slf4j
public class WalkToMiningAreaAction extends BaseScriptNode implements ActionNode {

    private final MovementService movementService;
    private final SleepService sleepService;

    @Inject
    public WalkToMiningAreaAction(Client client, ScriptContext context, MovementService movementService, SleepService sleepService) {
        super(client, context);
        this.movementService = movementService;
        this.sleepService = sleepService;
    }

    @Override
    public BehaviorResult performAction() {
        if(movementService.getCurrentState() == MovementState.WALKING) {
            return BehaviorResult.SUCCESS;
        }

        log.info("Walking to mining area at X={}, Y={}", MINING_AREA.getX(), MINING_AREA.getY());
        if(movementService.walkTo(MINING_AREA)) {
            context.setWalking(true);
            sleepService.sleep(4000, 7000);
            return BehaviorResult.RUNNING;
        } else {
            return BehaviorResult.FAILURE;
        }
    }
}