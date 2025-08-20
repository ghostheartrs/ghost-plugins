package com.krakenplugins.example.script.actions;

import com.google.inject.Inject;
import com.kraken.api.core.script.BehaviorResult;
import com.kraken.api.core.script.node.ActionNode;
import com.kraken.api.interaction.movement.MovementService;
import com.krakenplugins.example.script.BaseScriptNode;
import com.krakenplugins.example.script.ScriptContext;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;

import static com.krakenplugins.example.script.MiningScript.BANK_AREA;

@Slf4j
public class WalkToBankAction extends BaseScriptNode implements ActionNode {

    private final MovementService movementService;

    @Inject
    public WalkToBankAction(Client client, ScriptContext context, MovementService movementService) {
        super(client, context);
        this.movementService = movementService;
    }

    @Override
    public BehaviorResult performAction() {
        log.info("Walking to bank at {}", BANK_AREA);

        if (movementService.walkTo(BANK_AREA)) {
            return BehaviorResult.RUNNING;
        } else {
            log.warn("Failed to initiate walk to bank");
            return BehaviorResult.FAILURE;
        }
    }
}